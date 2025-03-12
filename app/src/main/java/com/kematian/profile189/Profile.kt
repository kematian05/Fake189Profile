package com.kematian.profile189

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val username: String,
    val gender: String,
    val birthDate: String,
    val telephone: String,
    val email: String,
    val language: String,
    val profilePictureUri: String?
)
