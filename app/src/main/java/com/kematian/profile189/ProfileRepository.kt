package com.kematian.profile189

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileRepository(private val profileDao: ProfileDao) {

    val profile: Flow<Profile?> = profileDao.getProfile()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveProfile(
        username: String,
        gender: String,
        birthDate: LocalDate?,
        telephone: String,
        email: String,
        language: String,
        profilePictureUri: String?
    ) {
        val dateString = birthDate?.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) ?: ""

        val profile = Profile(
            username = username,
            gender = gender,
            birthDate = dateString,
            telephone = telephone,
            email = email,
            language = language,
            profilePictureUri = profilePictureUri
        )

        Log.d("SavedProfile", "Profile saved: $profile")
        profileDao.insertProfile(profile)
    }

    suspend fun updateProfile(profile: Profile) {
        profileDao.updateProfile(profile)
    }

    suspend fun deleteProfile() {
        profileDao.deleteProfile()
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileRepository? = null

        fun getInstance(context: Context): ProfileRepository {
            return INSTANCE ?: synchronized(this) {
                val database = AppDatabase.getDatabase(context)
                val instance = ProfileRepository(database.profileDao())
                INSTANCE = instance
                instance
            }
        }
    }
}