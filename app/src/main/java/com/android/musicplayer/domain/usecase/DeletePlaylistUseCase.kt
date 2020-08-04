package com.android.musicplayer.domain.usecase

import com.android.musicplayer.data.model.Playlist
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.domain.repository.PlaylistRepository

class DeletePlaylistUseCase(private val playlistRepository: PlaylistRepository) {

    fun deletePlaylist(playlist: Playlist) {
        playlistRepository.delete(playlist)
    }

}