package com.android.musicplayer.presentation.playlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.android.musicplayer.R
import com.android.musicplayer.data.model.Playlist
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.presentation.songlist.OnSonglistAdapterListener
import com.android.musicplayer.presentation.songlist.SonglistAdapter
import kotlinx.android.synthetic.main.holder_song.view.*
import java.io.File
import kotlin.properties.Delegates

class PlaylistAdapter(val mListener: OnPlaylistAdapterListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var playlists: List<Playlist> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewSongItemHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_playlist, parent, false)
        return PlaylistViewHolder(viewSongItemHolder)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PlaylistAdapter.PlaylistViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): Playlist {
        return playlists[position]
    }


    inner class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun onBind(playlist: Playlist) {
            Log.i(PlaylistAdapter.TAG, "SongViewHolder onBind: $playlist")
            itemView.music_item_name_text_view.text = playlist.playlistName ?: ""

            playlist.albumArt?.let { nonNullImage ->
                itemView.music_item__avatar_image_view.load(File(nonNullImage)) {
                    crossfade(true)
                    placeholder(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.placeholder
                        )
                    )
                    error(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.placeholder
                        )
                    )
                }
            }

            itemView.setOnLongClickListener {
                mListener.removePlaylistItem(playlist)
                true
            }

            itemView.setOnClickListener {
                mListener.openSonglist(playlist)
                //mListener.playSong(song, playlists as ArrayList<Playlist>)
            }

        }
    }

    companion object {
        private val TAG = PlaylistAdapter::class.java.name
    }
}