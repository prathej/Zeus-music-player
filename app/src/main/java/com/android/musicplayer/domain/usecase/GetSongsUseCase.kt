package com.android.musicplayer.domain.usecase

import com.android.musicplayer.data.model.Song
import com.android.musicplayer.domain.repository.SongRepository

class GetSongsUseCase(private val songRepository: SongRepository) {
    fun getSongs(playlistId:Int): List<Song>? {
        return songRepository.getSongs(playlistId)
    }
}