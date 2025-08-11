package com.nuun.track.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// region neutral
val Accent100     = Color(30 / 255f, 30 / 255f, 30 / 255f, 1f)
val Amber500      = Color(243 / 255f, 192 / 255f, 79 / 255f, 1f)
val Green200      = Color(94 / 255f, 200 / 255f, 144 / 255f, 1f)

val Neutral100    = Color(255 / 255f, 255 / 255f, 255 / 255f, 1f)
val Neutral200    = Color(240 / 255f, 240 / 255f, 240 / 255f, 1f)
val Neutral300    = Color(220 / 255f, 220 / 255f, 220 / 255f, 1f)
val Neutral400    = Color(214 / 255f, 214 / 255f, 214 / 255f, 1f)
val Neutral500    = Color(171 / 255f, 171 / 255f, 171 / 255f, 1f)
val Neutral600    = Color(122 / 255f, 122 / 255f, 122 / 255f, 1f)
val Neutral700    = Color(89 / 255f, 89 / 255f, 89 / 255f, 1f)
val Neutral800    = Color(91 / 255f, 91 / 255f, 91 / 255f, 1f)
val Neutral900    = Color(67 / 255f, 67 / 255f, 67 / 255f, 1f)
val Neutral1000   = Color(74 / 255f, 74 / 255f, 74 / 255f, 1f)
val Neutral1100   = Color(50 / 255f, 50 / 255f, 50 / 255f, 1f)
val Neutral1200   = Color(40 / 255f, 40 / 255f, 40 / 255f, 1f)
val Neutral1300   = Color(38 / 255f, 38 / 255f, 38 / 255f, 1f)

val Red500        = Color(231 / 255f, 53 / 255f, 98 / 255f, 1f)

//
//val Neutral100 = Color(0xFFFFFFFF) // (White)
//val Neutral200 = Color(0xFFF0F0F0) // (Very Light Gray)
//val Neutral300 = Color(0xFFDCDCDC) // (Light Gray)
//val Neutral400 = Color(0xFFD6D6D6) // (Light Gray)
//val Neutral450 = Color(0xFFBFBFBF) // (Light Gray)
//val Neutral500 = Color(0xFFABABAB) // (Light Gray)
//val Neutral600 = Color(0xFF7A7A7A) // (Medium Gray)
//val Neutral700 = Color(0xFF595959) // (Medium Dark Gray)
//val Neutral800 = Color(0xFF5B5B5B) // (Dark Gray)
//val Neutral900 = Color(0xFF434343) // (Dark Gray)
//val Neutral1000 = Color(0xFF4A4A4A) // (Darker Gray)
//val Neutral1100 = Color(0xFF323232) // (Charcoal, Very Dark Gray)
//val Neutral1200 = Color(0xFF282828) // (Almost Black)
//val Neutral1300 = Color(0xFF262626) // (Black, Very Dark Gray)
// endregion neutral

// region accent
//val Accent100 = Color(0xFF1E1E1E) // (Very Dark Gray/Black)
//val Accent200 = Color(0xFF5EC890) // (Mint Green)
//val Accent500 = Color(0xFFE73562) // (Pink/Raspberry Red)
// endregion accent

val ColorDisabled = Neutral900
val ColorEnabled = Color(0xFF1A7DAB)
val ColorInputDefault = Neutral800
val ColorBgIcon = Neutral700
val ColorOnline = Neutral200
val ColorOffline = Neutral900
val ColorTopBar = Neutral1100

// region primary
val ColorPrimary = Accent100
val ColorOnPrimary = Neutral1100
val ColorPrimaryDark = Accent100
val ColorOnPrimaryDark = Neutral1100
// endregion primary

// region button
val ColorBtnTextPrimary = Neutral1100
val ColorBtnTextSecondary = Neutral100
val ColorBtnTextTertiary = Neutral500
val ColorBtnTextOutline = Neutral100

val ColorBtnBgPrimary = Neutral100
val ColorBtnBgSecondary = Neutral800
val ColorBtnBgTertiary = Neutral1100
val ColorBtnBgOutline = Color.Transparent

val ColorBtnContentTertiary = Neutral900
val ColorBtnContentPrimary = Neutral1100
val ColorBtnContentSecondary = Neutral1100
val ColorBtnContentOutline = Neutral1100
// endregion button

// region switch
val ColorSwitchTrackUnchecked = Neutral800
//val ColorSwitchTrackChecked = Accent200
val ColorSwitchThumb = Neutral100
// endregion switch

//region text
val ColorTextDefault = Neutral100
val ColorTextCaption = Neutral300
val ColorTextButton = Neutral1000
val ColorTextError = Color(0xFFD01414)
val ColorTextLeak = Color(0xFF3097E5)
val ColorTextInput = Neutral500
val ColorTextInputDisabled = Neutral600
//endregion text

// region background
val ColorBgNav = Neutral1300
val ColorContainer = Accent100
val ColorBgToolbar = Neutral1100
// endregion background

// region scheme
val LightColorScheme = lightColorScheme(
    background = ColorContainer,
    primary = ColorPrimary,
    onPrimary = ColorOnPrimary,
)

val DarkColorScheme = darkColorScheme(
    background = ColorContainer,
    primary = ColorPrimaryDark,
    onPrimary = ColorOnPrimaryDark,
)
// endregion scheme