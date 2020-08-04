package com.android.musicplayer.presentation.playlist

import com.android.musicplayer.data.model.Playlist
import com.android.musicplayer.data.model.Song

interface OnPlaylistAdapterListener {
    fun removePlaylistItem(playlist: Playlist)

    fun openSonglist(playlist: Playlist)
}