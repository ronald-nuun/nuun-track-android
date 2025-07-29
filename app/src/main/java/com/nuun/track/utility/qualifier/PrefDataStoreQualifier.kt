package com.nuun.track.utility.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PrefDataStoreQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PrefDataStoreManagerQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EncryptedPrefQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EncryptedPrefManagerQualifier