package com.kematian.profile189.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    @Update
    suspend fun updateProfile(profile: Profile)

    @Query("SELECT * FROM profiles WHERE id = 1")
    fun getProfile(): Flow<Profile?>

    @Query("DELETE FROM profiles WHERE id = 1")
    suspend fun deleteProfile()
}