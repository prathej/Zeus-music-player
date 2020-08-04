package com.android.musicplayer.presentation.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.musicplayer.data.model.Playlist
import com.android.musicplayer.data.model.Song
import com.android.musicplayer.domain.usecase.*

class PlaylistViewModel(
    private val createPlaylistUsecase: CreatePlaylistUsecase,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase
) : ViewModel(){

    val playlistData = MutableLiveData<List<Playlist>>()

    fun createPlaylist(playlist: Playlist) {
        createPlaylistUsecase.createPlaylist(playlist)
        playlistData.value = getPlaylistsUseCase.getPlaylists()
    }

    fun getPlaylistsFromDb() {
        playlistData.value = getPlaylistsUseCase.getPlaylists()
    }

    fun removeItemFromList(playlist: Playlist) {
        deletePlaylistUseCase.deletePlaylist(playlist)
        val list = playlistData.value as ArrayList<Playlist>
        list.remove(playlist)
        playlistData.value = list
    }

}