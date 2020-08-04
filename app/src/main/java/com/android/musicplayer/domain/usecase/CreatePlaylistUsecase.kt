package com.android.musicplayer.domain.usecase

import com.android.musicplayer.data.model.Playlist
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.domain.repository.PlaylistRepository
import com.android.musicplayer.domain.repository.SongRepository

class CreatePlaylistUsecase(private val playlistRepository: PlaylistRepository) {

    fun createPlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist)
    }
}