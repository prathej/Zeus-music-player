package com.android.musicplayer.presentation.songlist

import com.android.musicplayer.data.model.Song

/**
 * To make an interaction between [SonglistActivity]
 * & [SonglistAdapter]
 *
 * @author ZARA
 * */
interface OnSonglistAdapterListener {

    fun playSong(song: Song, songs: ArrayList<Song>)

    fun removeSongItem(song: Song)
}