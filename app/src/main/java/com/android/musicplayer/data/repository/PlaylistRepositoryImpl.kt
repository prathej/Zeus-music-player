package com.android.musicplayer.data.repository

import com.android.musicplayer.data.model.Playlist
import com.android.musicplayer.data.source.local.AppDatabase
import com.android.musicplayer.domain.repository.PlaylistRepository

class PlaylistRepositoryImpl(private val appDatabase: AppDatabase) : PlaylistRepository{
    override fun createPlaylist(playlist: Playlist): Long {
        return appDatabase.playlistDao.insert(playlist)
    }

    override fun getPlayLists(): List<Playlist>? {
        return appDatabase.playlistDao.loadAll()
    }

    override fun delete(playlist: Playlist) {
        return appDatabase.playlistDao.delete(playlist)
    }
}