package com.android.musicplayer.presentation.songlist

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.android.musicplayer.R
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.presentation.songplayer.SongPlayerActivity
import com.android.player.BaseSongPlayerActivity
import com.android.player.model.ASong
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_playlist.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class SonglistActivity : BaseSongPlayerActivity(), OnSonglistAdapterListener {

    private var adapter: SonglistAdapter? = null
    private val viewModel: SonglistViewModel by viewModel()
    private var playlistId: Int = 0

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.apply {
            playlistId = intent.getIntExtra("playlistId",-1)
            Toast.makeText(this@SonglistActivity, playlistId, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        setSupportActionBar(toolbar)

        playlistId = intent.getIntExtra("playlistId",-1)

        adapter = SonglistAdapter(this)
        playlist_recycler_view.adapter = adapter


        fab.setOnClickListener { view ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isReadPhoneStatePermissionGranted()) openMusicList()
                else requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE
                )
            } else openMusicList()
        }

        viewModel.playlistData.observe(this, Observer {
            adapter?.songs = it
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.getSongsFromDb(playlistId)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_AUDIO_KEY) {
            data?.data?.let {
                addSong(it)
            }
        }
    }

    private fun addSong(musicData: Uri) {
        /*    val cursor = activity?.contentResolver?.query(musicData, null,null, null, null)*/
        val cursor = contentResolver?.query(
            musicData,
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION
            ), null, null, null
        )
        while (cursor?.moveToNext() == true) {
            val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
            val duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

            val cursorAlbums = contentResolver?.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART),
                MediaStore.Audio.Albums._ID + "=?",
                arrayOf<String>(albumId),
                null
            )
            var albumArt: String? = null
            if (cursorAlbums?.moveToFirst() == true) {
                albumArt =
                    cursorAlbums.getString(cursorAlbums.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))
            }

            val song = Song(
                id.toInt(),
                title.toString(),
                path.toString(),
                artist,
                albumArt,
                duration,
                AUDIO_TYPE,
                playlistId
            )
            viewModel.saveSongData(song)
        }
        cursor?.close()
    }


    private fun isReadPhoneStatePermissionGranted(): Boolean {
        val firstPermissionResult = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return firstPermissionResult == PackageManager.PERMISSION_GRANTED
    }

    private fun showRemoveSongItemConfirmDialog(song: Song) {
        // setup the alert builder
        AlertDialog.Builder(this)
            .setMessage("Are you sure to remove this song?")
            // add a button
            .apply {
                setPositiveButton(R.string.yes) { _, _ ->
                    removeMusicFromList(song)
                }
                setNegativeButton(R.string.no) { _, _ ->
                    // User cancelled the dialog
                }
            }
            // create and show the alert dialog
            .show()
    }

    override fun removeSongItem(song: Song) {
        showRemoveSongItemConfirmDialog(song)
    }

    private fun removeMusicFromList(song: Song) {
        playerViewModel.stop()
        viewModel.removeItemFromList(song)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {// Permission Granted
                    openMusicList()
                } else {
                    // Permission Denied
                    Snackbar.make(
                        playlist_recycler_view,
                        getString(R.string.you_denied_permission),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun openMusicList() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_AUDIO_KEY)
    }


    override fun playSong(song: Song, songs: ArrayList<Song>) {
        SongPlayerActivity.start(this, song, songs)
    }


    companion object {

        private val TAG = SonglistActivity::class.java.name
        const val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_CODE = 7031
        const val PICK_AUDIO_KEY = 2017
        const val AUDIO_TYPE = 3


        /*fun start(context: Context, song: Song, songList: ArrayList<Song>) {
            val intent = Intent(context, SongPlayerActivity::class.java).apply {
                putExtra(ASong::class.java.name, song)
                putExtra(SONG_LIST_KEY, songList)
            }
            context.startActivity(intent)
        }*/


        fun start(context: Context,playlistId: Int) {
            val intent = Intent(context, SonglistActivity::class.java)
            intent.putExtra("playlistId", playlistId)
            context.startActivity(intent)
        }
    }

}
