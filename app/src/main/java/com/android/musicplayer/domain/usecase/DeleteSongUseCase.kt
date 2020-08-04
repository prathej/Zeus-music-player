package com.android.musicplayer.domain.usecase

import com.android.musicplayer.data.model.Song
import com.android.musicplayer.domain.repository.SongRepository

class DeleteSongUseCase(private val songRepository: SongRepository) {


    fun deleteSongItem(song: Song) {
        songRepository.delete(song)
    }
}