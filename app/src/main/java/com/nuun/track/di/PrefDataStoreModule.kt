package com.nuun.track.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.nuun.track.core.encrypted_pref_data_store.EncryptedPrefManager
import com.nuun.track.core.encrypted_pref_data_store.EncryptedPrefManagerImpl
import com.nuun.track.core.pref_data_store.PrefDataStoreManager
import com.nuun.track.core.pref_data_store.PrefDataStoreManagerImpl
import com.nuun.track.utility.qualifier.EncryptedPrefManagerQualifier
import com.nuun.track.utility.qualifier.EncryptedPrefQualifier
import com.nuun.track.utility.qualifier.PrefDataStoreManagerQualifier
import com.nuun.track.utility.qualifier.PrefDataStoreQualifier
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefDataStoreModule {

    private const val SESSION_NAME = "NuunTrack"
    private val Context.sessionPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
        SESSION_NAME
    )

    private const val ENCRYPTED_SESSION_NAME = "EncryptedBluetoothApp"

    @Provides
    @Singleton
    @PrefDataStoreQualifier
    fun providesSessionPrefDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.sessionPreferencesDataStore

    @Provides
    @Singleton
    @PrefDataStoreManagerQualifier
    fun providesSessionPrefDataStoreManager(
        @PrefDataStoreQualifier preferences: DataStore<Preferences>,
        moshi: Moshi
    ): PrefDataStoreManager = PrefDataStoreManagerImpl(preferences, moshi)

    @Provides
    @Singleton
    fun providesMasterKey(): String {
        return MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    }

    @Provides
    @Singleton
    @EncryptedPrefQualifier
    fun providesEncryptedPref(
        @ApplicationContext context: Context,
        masterKey: String
    ): SharedPreferences = EncryptedSharedPreferences.create(
        ENCRYPTED_SESSION_NAME,
        masterKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    @Provides
    @Singleton
    @EncryptedPrefManagerQualifier
    fun providesEncryptedPrefManager(
        @EncryptedPrefQualifier sharedPreferences: SharedPreferences,
    ): EncryptedPrefManager = EncryptedPrefManagerImpl(sharedPreferences)
}