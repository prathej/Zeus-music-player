package com.android.musicplayer.presentation.playlist

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.android.musicplayer.R
import com.android.musicplayer.data.model.Playlist
import com.android.musicplayer.presentation.songlist.SonglistActivity
import kotlinx.android.synthetic.main.activity_playlist.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class PlaylistActivity : AppCompatActivity(),OnPlaylistAdapterListener {

    private var adapter: PlaylistAdapter? = null
    private val viewModel: PlaylistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist2)
        setSupportActionBar(toolbar)

        adapter = PlaylistAdapter(this)
        playlist_recycler_view.adapter = adapter


        fab.setOnClickListener { view ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //
                withEditText()
            } else {

            }
        }

        viewModel.playlistData.observe(this, Observer {
            adapter?.playlists = it
        })

    }

    override fun onStart() {
        super.onStart()
        viewModel.getPlaylistsFromDb()
    }

    override fun removePlaylistItem(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override fun openSonglist(playlist: Playlist) {
        SonglistActivity.start(this,playlistId = playlist.id)
    }

    fun withEditText() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Enter Playlist name")
        val dialogLayout = inflater.inflate(R.layout.dialog_playlist, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Create") {
                dialogInterface,
                i -> createPlaylist(editText.text.toString())
        }
        builder.show()
    }

    fun createPlaylist(name: String){
        if(name!=""){
            viewModel.createPlaylist(Playlist(name,""))
            //adapter?.notifyDataSetChanged()
        }else{

        }
    }

}