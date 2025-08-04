package com.nuun.track.core.pref_data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nuun.track.domain.auth.response.UserDomain
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PrefDataStoreManagerImpl @Inject constructor(
    private val preferences: DataStore<Preferences>,
    private val moshi: Moshi,
) : PrefDataStoreManager {

    // region pref key
    private val userDetail = stringPreferencesKey(USER_DETAIL_PREF)
    private val updateFlag = booleanPreferencesKey(UPDATE_FLAG)
    // endregion pref key

    // region adapter
    private val userAdapter = moshi.adapter(UserDomain::class.java)
    // endregion adapter

    override suspend fun storeUser(data: UserDomain) {
        preferences.edit {
            it[userDetail] = userAdapter.toJson(data)
        }
    }

    override fun getUser(): Flow<UserDomain?> {
        return preferences.data.map {
            it[userDetail]?.let { profile ->
                userAdapter.fromJson(profile)
            }
        }
    }

    override suspend fun setUpdateFlag(status: Boolean) {
        preferences.edit {
            it[updateFlag] = status
        }
    }

    override fun getUpdateFlag(): Flow<Boolean> {
        return preferences.data.map {
            it[updateFlag] == true
        }
    }

    override suspend fun clearDataStore() {
        preferences.edit {
            it.clear()
        }
    }

    companion object {
        private const val USER_DETAIL_PREF = "USER_DETAIL_PREF"
        private const val SELECTED_HOME = "SELECTED_HOME"
        private const val SELECTED_MODEL = "SELECTED_MODEL"
        private const val SUB_DEVICES = "SUB_DEVICES"
        private const val WIFI_DETAIL_PREF = "WIFI_DETAIL_PREF"
        private const val BLUETOOTH_INFO_PREF = "BLUETOOTH_INFO_PREF"
        private const val DEVICE_INFO_PREF = "DEVICE_INFO_PREF"
        private const val DEVICE_STATUS = "DEVICE_STATUS"
        private const val SCENE_CONFIG = "SCENE_CONFIG"
        private const val SOUND_STATE = "SOUND_STATE"
        private const val HOME_STATE = "HOME_STATE"
        private const val BULLETIN_STATE = "BULLETIN_STATE"
        private const val VIEW_TYPE = "VIEW_TYPE"
        private const val THING_LIST = "THING_LIST"
        private const val UPDATE_FLAG = "UPDATE_FLAG"
    }
}