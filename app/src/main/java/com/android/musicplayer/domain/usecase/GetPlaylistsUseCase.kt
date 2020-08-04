package com.android.musicplayer.domain.usecase

import com.android.musicplayer.data.model.Playlist
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.domain.repository.PlaylistRepository

class GetPlaylistsUseCase(private val playlistRepository: PlaylistRepository) {

    fun getPlaylists(): List<Playlist>? {
        return playlistRepository.getPlayLists()
    }

}