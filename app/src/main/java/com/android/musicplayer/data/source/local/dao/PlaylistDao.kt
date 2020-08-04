package com.android.musicplayer.data.source.local.dao

import androidx.room.*
import com.android.musicplayer.data.model.Playlist

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playlist: Playlist): Long

    @Query("SELECT * FROM Playlist")
    fun loadAll(): MutableList<Playlist>

    @Delete
    fun delete(playlist: Playlist)

    @Query("DELETE FROM Playlist")
    fun deleteAll()

    @Query("SELECT * FROM Playlist where id = :playlistId")
    fun loadOneBySongId(playlistId: Long): Playlist?

    @Query("SELECT * FROM Playlist where playlistName = :songTitle")
    fun loadOneBySongTitle(songTitle: String): Playlist?

    @Update
    fun update(song: Playlist)
}