package com.kematian.profile189.viewmodels

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.core.content.edit
import com.kematian.profile189.room.Profile
import com.kematian.profile189.room.ProfileRepository

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    init {
        viewModelScope.launch {
            repository.profile.collect { profile ->
                _profile.value = profile
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveProfile(
        username: String,
        gender: String,
        birthDate: String?,
        telephone: String,
        email: String,
        language: String,
        profilePictureUri: String?
    ) {
        viewModelScope.launch {
            repository.saveProfile(
                username = username,
                gender = gender,
                birthDate = birthDate,
                telephone = telephone,
                email = email,
                language = language,
                profilePictureUri = profilePictureUri
            )
        }
    }

    fun deleteProfile() {
        viewModelScope.launch {
            repository.deleteProfile()
        }
    }

    fun saveImageUriToPreferences(context: Context, uri: Uri?) {
        val sharedPreferences = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit() { putString("profile_image_uri", uri?.toString()) }
    }

    fun getImageUriFromPreferences(context: Context): Uri? {
        val sharedPreferences = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        val uriString = sharedPreferences.getString("profile_image_uri", null)
        return uriString?.toUri()
    }

    class ProfileViewModelFactory(private val repository: ProfileRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}