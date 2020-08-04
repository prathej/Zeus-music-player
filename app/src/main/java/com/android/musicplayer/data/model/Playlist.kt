package com.android.musicplayer.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Suppress("DIFFERENT_NAMES_FOR_THE_SAME_PARAMETER_IN_SUPERTYPES")
@Entity
@Parcelize
data class Playlist(
    var playlistName: String?,
    var albumArt: String?
) : Parcelable{
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}