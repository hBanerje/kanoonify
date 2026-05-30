package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.screens.viewmodel.ProfileState
import com.multiplatform.kanoonify.presentation.screens.viewmodel.ProfileViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.FloatingBottomBar
import com.multiplatform.kanoonify.presentation.ui.components.FloatingOrbBackground
import com.multiplatform.kanoonify.presentation.ui.components.PremiumCard
import com.multiplatform.kanoonify.presentation.ui.components.ProfileHeader
import com.multiplatform.kanoonify.presentation.ui.components.SettingsRow
import com.multiplatform.kanoonify.presentation.ui.components.StatsCard
import kanoonify.composeapp.generated.resources.Res
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onAskClick: () -> Unit,
    onHomeTabClick: () -> Unit,
    onSearchTabClick: () -> Unit,
    onSavedTabClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KanoonifyPremiumColors.BgDeep)
    ) {
        FloatingOrbBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.ScreenHorizontal)
        ) {
            Spacer(Modifier.height(Dimens.SpaceXL))

            AnimatedEntrance(durationMillis = 500, slidePx = 22f) {
                ProfileHeader(
                    name = state.user.name,
                    memberSince = stringResource(
                        Res.string.profile_member_since,
                        state.user.memberSinceLabel
                    ),
                    membershipLabel = if (state.user.isPremium)
                        stringResource(Res.string.profile_membership_premium)
                    else
                        stringResource(Res.string.profile_membership_free),
                    isPremium = state.user.isPremium,
                    editLabel = stringResource(Res.string.profile_edit_action),
                    onEditClick = viewModel::onEditProfileClick
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 100) {
                ProfileSectionTitle(stringResource(Res.string.profile_section_stats_title))
            }
            Spacer(Modifier.height(Dimens.SpaceM))
            StatsGrid(state = state)

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 220) {
                ProfileSectionTitle(stringResource(Res.string.profile_section_premium_title))
            }
            Spacer(Modifier.height(Dimens.SpaceM))
            AnimatedEntrance(delayMillis = 280, slidePx = 24f) {
                PremiumCard(
                    title = stringResource(Res.string.profile_premium_card_title),
                    subtitle = stringResource(Res.string.profile_premium_card_subtitle),
                    benefits = listOf(
                        stringResource(Res.string.profile_premium_benefit_ai),
                        stringResource(Res.string.profile_premium_benefit_lawyer),
                        stringResource(Res.string.profile_premium_benefit_offline),
                        stringResource(Res.string.profile_premium_benefit_export)
                    ),
                    comingSoonLabel = stringResource(Res.string.profile_premium_badge_coming_soon),
                    ctaLabel = stringResource(Res.string.profile_premium_cta),
                    onCtaClick = viewModel::onPremiumCtaClick
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 340) {
                ProfileSectionTitle(stringResource(Res.string.profile_section_settings_title))
            }
            Spacer(Modifier.height(Dimens.SpaceM))
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
                SettingsRow(
                    glyph = "\uD83D\uDD14",
                    title = stringResource(Res.string.profile_settings_notifications),
                    subtitle = stringResource(Res.string.profile_settings_notifications_sub),
                    accent = KanoonifyPremiumColors.NeonBlue,
                    toggleState = state.preferences.notificationsEnabled,
                    onToggle = viewModel::onToggleNotifications,
                    onClick = {}
                )
                SettingsRow(
                    glyph = "\uD83C\uDF10",
                    title = stringResource(Res.string.profile_settings_language),
                    subtitle = stringResource(Res.string.profile_settings_language_sub),
                    accent = KanoonifyPremiumColors.NeonCyan,
                    trailing = state.preferences.language,
                    onClick = { /* future language picker */ }
                )
                SettingsRow(
                    glyph = "\uD83C\uDFA8",
                    title = stringResource(Res.string.profile_settings_theme),
                    subtitle = stringResource(Res.string.profile_settings_theme_sub),
                    accent = KanoonifyPremiumColors.NeonIndigo,
                    trailing = state.preferences.themeLabel,
                    onClick = { /* future theme picker */ }
                )
                SettingsRow(
                    glyph = "\uD83D\uDD12",
                    title = stringResource(Res.string.profile_settings_biometric),
                    subtitle = stringResource(Res.string.profile_settings_biometric_sub),
                    accent = KanoonifyPremiumColors.NeonViolet,
                    toggleState = state.preferences.biometricLockEnabled,
                    onToggle = viewModel::onToggleBiometricLock,
                    onClick = {}
                )
                SettingsRow(
                    glyph = "\uD83D\uDEE1",
                    title = stringResource(Res.string.profile_settings_privacy),
                    subtitle = stringResource(Res.string.profile_settings_privacy_sub),
                    accent = KanoonifyPremiumColors.AccentLaws,
                    onClick = viewModel::onPrivacyPolicyClick
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 400) {
                ProfileSectionTitle(stringResource(Res.string.profile_section_security_title))
            }
            Spacer(Modifier.height(Dimens.SpaceM))
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
                SettingsRow(
                    glyph = "\uD83E\uDDD1",
                    title = stringResource(Res.string.profile_security_face_id),
                    subtitle = stringResource(Res.string.profile_security_face_id_sub),
                    accent = KanoonifyPremiumColors.NeonBlue,
                    toggleState = state.preferences.faceIdEnabled,
                    onToggle = viewModel::onToggleFaceId,
                    onClick = {}
                )
                SettingsRow(
                    glyph = "\uD83D\uDCC2",
                    title = stringResource(Res.string.profile_security_documents),
                    subtitle = stringResource(Res.string.profile_security_documents_sub),
                    accent = KanoonifyPremiumColors.AccentConstitution,
                    toggleState = state.preferences.secureDocumentsEnabled,
                    onToggle = viewModel::onToggleSecureDocuments,
                    onClick = {}
                )
                SettingsRow(
                    glyph = "\uD83D\uDD10",
                    title = stringResource(Res.string.profile_security_app_lock),
                    subtitle = stringResource(Res.string.profile_security_app_lock_sub),
                    accent = KanoonifyPremiumColors.AccentEmergency,
                    toggleState = state.preferences.appLockEnabled,
                    onToggle = viewModel::onToggleAppLock,
                    onClick = {}
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 460) {
                ProfileSectionTitle(stringResource(Res.string.profile_section_legal_title))
            }
            Spacer(Modifier.height(Dimens.SpaceM))
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
                SettingsRow(
                    glyph = "\uD83D\uDCC4",
                    title = stringResource(Res.string.profile_legal_privacy_policy),
                    subtitle = " ",
                    accent = KanoonifyPremiumColors.NeonBlue,
                    onClick = viewModel::onPrivacyPolicyClick
                )
                SettingsRow(
                    glyph = "\uD83D\uDCDC",
                    title = stringResource(Res.string.profile_legal_terms),
                    subtitle = " ",
                    accent = KanoonifyPremiumColors.AccentConstitution,
                    onClick = viewModel::onTermsClick
                )
                SettingsRow(
                    glyph = "\u2709",
                    title = stringResource(Res.string.profile_legal_contact),
                    subtitle = " ",
                    accent = KanoonifyPremiumColors.NeonCyan,
                    onClick = viewModel::onContactSupportClick
                )
                SettingsRow(
                    glyph = "\u2139",
                    title = stringResource(Res.string.profile_legal_about),
                    subtitle = " ",
                    accent = KanoonifyPremiumColors.NeonIndigo,
                    onClick = viewModel::onAboutClick
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            Text(
                text = stringResource(Res.string.profile_app_version),
                color = KanoonifyPremiumColors.TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.6.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(120.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = Dimens.SpaceL)
        ) {
            FloatingBottomBar(
                homeLabel = stringResource(Res.string.landing_bottom_nav_home),
                searchLabel = stringResource(Res.string.landing_bottom_nav_search),
                askLabel = stringResource(Res.string.landing_bottom_nav_ask),
                savedLabel = stringResource(Res.string.landing_bottom_nav_saved),
                profileLabel = stringResource(Res.string.landing_bottom_nav_profile),
                selectedIndex = 4,
                onHomeClick = onHomeTabClick,
                onSearchClick = onSearchTabClick,
                onAskClick = onAskClick,
                onSavedClick = onSavedTabClick,
                onProfileClick = { }
            )
        }
    }
}

@Composable
private fun ProfileSectionTitle(text: String) {
    Text(
        text = text,
        color = KanoonifyPremiumColors.TextHi,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.2.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun StatsGrid(state: ProfileState) {
    data class StatVD(val label: String, val value: String, val glyph: String, val accent: Color)
    val stats = listOf(
        StatVD(stringResource(Res.string.profile_stat_searches),       state.stats.searches.toString(),         "\uD83D\uDD0D", KanoonifyPremiumColors.NeonBlue),
        StatVD(stringResource(Res.string.profile_stat_saved),          state.stats.savedItems.toString(),       "\uD83D\uDCD1", KanoonifyPremiumColors.NeonViolet),
        StatVD(stringResource(Res.string.profile_stat_coi_reads),      state.stats.constitutionReads.toString(),"\uD83D\uDCDC", KanoonifyPremiumColors.AccentConstitution),
        StatVD(stringResource(Res.string.profile_stat_consultations),  state.stats.consultations.toString(),    "\uD83D\uDC68\u200D\u2696\uFE0F", KanoonifyPremiumColors.AccentLawyer)
    )
    val rows = stats.chunked(2)
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)) {
        rows.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
            ) {
                row.forEachIndexed { colIndex, stat ->
                    val index = rowIndex * 2 + colIndex
                    Box(modifier = Modifier.weight(1f)) {
                        AnimatedEntrance(
                            delayMillis = 160L + index * 70L,
                            durationMillis = 420,
                            slidePx = 22f
                        ) {
                            StatsCard(
                                label = stat.label,
                                value = stat.value,
                                glyph = stat.glyph,
                                accent = stat.accent,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

