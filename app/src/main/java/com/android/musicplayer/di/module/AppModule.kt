package com.android.musicplayer.di.module

import com.android.musicplayer.domain.repository.PlaylistRepository
import com.android.musicplayer.domain.repository.SongRepository
import com.android.musicplayer.domain.usecase.*
import com.android.musicplayer.presentation.playlist.PlaylistViewModel
import com.android.musicplayer.presentation.songlist.SonglistViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {

    viewModel { SonglistViewModel(get(), get(), get()) }

    single { createGetSongsUseCase(get()) }

    single { createDeleteSongUseCase(get()) }

    single { createSaveSongDataUseCase(get()) }

    viewModel { PlaylistViewModel(get(), get(), get()) }

    single { createGetPlaylistsUseCase(get()) }

    single { createDeletePlaylistUseCase(get()) }

    single { createPlaylistUseCase(get()) }

    single { createPlaylistRepository(get()) }

    single { createSongRepository(get()) }
}

//Song Usecases
fun createSaveSongDataUseCase(
    songRepository: SongRepository
): SaveSongDataUseCase {
    return SaveSongDataUseCase(songRepository)
}

fun createDeleteSongUseCase(
    songRepository: SongRepository
): DeleteSongUseCase {
    return DeleteSongUseCase(songRepository)
}


fun createGetSongsUseCase(
    songRepository: SongRepository
): GetSongsUseCase {
    return GetSongsUseCase(songRepository)
}

//Playlist Usecases
fun createPlaylistUseCase(
    playlistRepository: PlaylistRepository
): CreatePlaylistUsecase {
    return CreatePlaylistUsecase(playlistRepository)
}

fun createDeletePlaylistUseCase(
    playlistRepository: PlaylistRepository
): DeletePlaylistUseCase {
    return DeletePlaylistUseCase(playlistRepository)
}


fun createGetPlaylistsUseCase(
    playlistRepository: PlaylistRepository
): GetPlaylistsUseCase {
    return GetPlaylistsUseCase(playlistRepository)
}
