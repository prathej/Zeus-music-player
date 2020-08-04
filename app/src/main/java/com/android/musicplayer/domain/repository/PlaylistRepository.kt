package com.android.musicplayer.domain.repository

import com.android.musicplayer.data.model.Playlist
import com.android.musicplayer.data.model.Song

interface PlaylistRepository {
    fun createPlaylist(playlist: Playlist):Long

    fun getPlayLists(): List<Playlist>?

    fun delete(playlist: Playlist)
}