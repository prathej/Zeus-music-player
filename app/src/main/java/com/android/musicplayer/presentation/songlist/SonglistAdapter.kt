package com.android.musicplayer.presentation.songlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.android.musicplayer.R
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.presentation.songlist.SonglistAdapter.SongViewHolder
import kotlinx.android.synthetic.main.holder_song.view.*
import java.io.File
import kotlin.properties.Delegates

/**
 * This class is responsible for converting each data entry [Song]
 * into [SongViewHolder] that can then be added to the AdapterView.
 *
 * Created by ZARA.
 */
internal class SonglistAdapter(val mListener: OnSonglistAdapterListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var songs: List<Song> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }


    /**
     * This method is called right when adapter is created &
     * is used to initialize ViewHolders
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewSongItemHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_song, parent, false)
        return SongViewHolder(viewSongItemHolder)
    }

    /** It is called for each ViewHolder to bind it to the adapter &
     * This is where we pass data to ViewHolder
     * */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SongViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): Song {
        return songs[position]
    }

    /**
     * This method returns the size of collection that contains the items we want to display
     * */
    override fun getItemCount(): Int {
        return songs.size
    }

    inner class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun onBind(song: Song) {
            Log.i(TAG, "SongViewHolder onBind: $song")
            itemView.music_item_name_text_view.text = song.title ?: ""

            song.clipArt?.let { nonNullImage ->
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
                mListener.removeSongItem(song)
                true
            }

            itemView.setOnClickListener {
                mListener.playSong(song, songs as ArrayList<Song>)
            }

        }
    }

    companion object {
        private val TAG = SonglistAdapter::class.java.name
    }

}
