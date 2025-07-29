package com.nuun.track.core.pref_data_store

import com.nuun.track.domain.auth.response.UserDomain
import kotlinx.coroutines.flow.Flow

interface PrefDataStoreManager {

    suspend fun storeUser(data: UserDomain)

    fun getUser(): Flow<UserDomain?>

    suspend fun clearDataStore()
}