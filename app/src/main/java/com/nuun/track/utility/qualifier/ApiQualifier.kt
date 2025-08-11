package com.nuun.track.utility.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OkhttpQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthApiQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReservationApiQualifier