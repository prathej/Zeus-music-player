package com.android.musicplayer.data.repository

import com.android.musicplayer.data.model.Song
import com.android.musicplayer.data.source.local.AppDatabase
import com.android.musicplayer.domain.repository.SongRepository

class SongRepositoryImp(private val appDatabase: AppDatabase) : SongRepository {

    override fun delete(song: Song) {
        appDatabase.songDao.delete(song)
    }

    override fun getSongs(playlistId:Int): List<Song>? {
        return appDatabase.songDao.loadAll(playlistId)
    }

    override fun saveSongData(song: Song):Long {
        return appDatabase.songDao.insert(song)
    }
}