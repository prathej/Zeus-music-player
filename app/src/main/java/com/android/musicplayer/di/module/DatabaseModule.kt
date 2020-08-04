package com.android.musicplayer.di.module

import android.app.Application
import androidx.room.Room
import com.android.musicplayer.data.repository.PlaylistRepositoryImpl
import com.android.musicplayer.data.repository.SongRepositoryImp
import com.android.musicplayer.data.source.local.AppDatabase
import com.android.musicplayer.data.source.local.dao.PlaylistDao
import com.android.musicplayer.data.source.local.dao.SongDao
import com.android.musicplayer.domain.repository.PlaylistRepository
import com.android.musicplayer.domain.repository.SongRepository
import org.koin.dsl.module

val DatabaseModule = module {

    single { createAppDatabase(get()) }

    single { createSongDao(get()) }

    single { createPlaylistDao(get()) }

}

internal fun createAppDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        AppDatabase.DB_NAME
    )
        // .fallbackToDestructiveMigration()//allows database to be cleared after upgrading version
        .allowMainThreadQueries()
        .build()
}


fun createSongDao(appDatabase: AppDatabase): SongDao {
    return appDatabase.songDao
}

fun createPlaylistDao(appDatabase: AppDatabase): PlaylistDao {
    return appDatabase.playlistDao
}

fun createSongRepository(appDatabase: AppDatabase): SongRepository {
    return SongRepositoryImp(appDatabase)
}

/*fun createPlaylistRepository(appDatabase: AppDatabase): SongRepository {
    return SongRepositoryImp(appDatabase)
}*/

fun createPlaylistRepository(appDatabase: AppDatabase): PlaylistRepository {
    return PlaylistRepositoryImpl(appDatabase)
}

