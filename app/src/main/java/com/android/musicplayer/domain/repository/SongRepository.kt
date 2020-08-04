package com.android.musicplayer.domain.repository

import com.android.musicplayer.data.model.Song

interface SongRepository {

    fun saveSongData(song: Song):Long

    fun getSongs(playlistId:Int): List<Song>?

    fun delete(song: Song)

}