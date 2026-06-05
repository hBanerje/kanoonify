package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.multiplatform.kanoonify.data.LawyerDataProvider
import com.multiplatform.kanoonify.domain.model.Lawyer
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawyerAccessViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.AppCard
import com.multiplatform.kanoonify.presentation.ui.components.MonogramIcon
import com.multiplatform.kanoonify.presentation.ui.components.SectionHeader
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun LawyerProfileScreen(
    lawyerId: String,
    accessViewModel: LawyerAccessViewModel,
    onChatClick: (Lawyer) -> Unit
) {
    val lawyer = LawyerDataProvider.findById(lawyerId)
    val accessState by accessViewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(accessState.authenticationSuccess, lawyer) {
        if (accessState.authenticationSuccess && lawyer != null) {
            accessViewModel.consumeAuthenticationSuccess()
            onChatClick(lawyer)
        }
    }

    LaunchedEffect(accessState.errorMessage) {
        val msg = accessState.errorMessage
        if (!msg.isNullOrBlank()) {
            snackbarHostState.showSnackbar(msg)
            accessViewModel.consumeError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (lawyer == null) {
            Text(
                text = stringResource(Res.string.lawyer_profile_not_found),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(Dimens.ScreenHorizontal)
            )
            return@Box
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Dimens.ScreenHorizontal)
            ) {
                Spacer(Modifier.height(Dimens.SpaceL))

                AnimatedEntrance { ProfileHeader(lawyer) }

                Spacer(Modifier.height(Dimens.SpaceL))

                AnimatedEntrance(delayMillis = 80) { StatsRow(lawyer) }

                Spacer(Modifier.height(Dimens.SpaceL))

                AnimatedEntrance(delayMillis = 140) {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SectionHeader(title = stringResource(Res.string.lawyer_profile_section_about))
                            Spacer(Modifier.height(Dimens.SpaceS))
                            Text(
                                text = lawyer.bio,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(Modifier.height(Dimens.SpaceM))

                AnimatedEntrance(delayMillis = 200) {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SectionHeader(title = stringResource(Res.string.lawyer_profile_section_practice))
                            Spacer(Modifier.height(Dimens.SpaceS))
                            DetailLine(
                                label = stringResource(Res.string.lawyer_profile_field_specialisation),
                                value = lawyer.specialization
                            )
                            DetailLine(
                                label = stringResource(Res.string.lawyer_profile_field_experience),
                                value = stringResource(Res.string.lawyer_profile_years_value, lawyer.experienceYears)
                            )
                            DetailLine(
                                label = stringResource(Res.string.lawyer_profile_field_location),
                                value = lawyer.location
                            )
                            DetailLine(
                                label = stringResource(Res.string.lawyer_profile_field_rating),
                                value = stringResource(Res.string.lawyer_profile_rating_format, lawyer.rating.toString())
                            )
                            DetailLine(
                                label = stringResource(Res.string.lawyer_profile_field_fee_per_session),
                                value = stringResource(Res.string.lawyer_profile_fee_format, lawyer.feePerSession)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(Dimens.SpaceM))

                AnimatedEntrance(delayMillis = 260) {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SectionHeader(title = stringResource(Res.string.lawyer_profile_section_languages))
                            Spacer(Modifier.height(Dimens.SpaceS))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS),
                                verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)
                            ) {
                                lawyer.languages.forEach { LanguageChip(it) }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(Dimens.SpaceXXL))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .navigationBarsPadding()
                    .padding(
                        horizontal = Dimens.ScreenHorizontal,
                        vertical = Dimens.SpaceM
                    )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val isAuthenticating = accessState.isAuthenticating
                    val biometricAvailable = accessState.biometricAvailable
                    val ctaEnabled = lawyer.isOnline && !isAuthenticating

                    Button(
                        onClick = {

                            accessViewModel.requestAuthentication(
                                title = "Secure Consultation",
                                subtitle = "Verify it's you",
                                description = "Biometric authentication is required " +
                                    "before connecting to your lawyer."
                            )
                        },
                        enabled = ctaEnabled,
                        shape = RoundedCornerShape(Dimens.RadiusL),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isAuthenticating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(Dimens.SpaceS))
                            Text(
                                text = "Authenticating…",
                                style = MaterialTheme.typography.labelLarge
                            )
                        } else {
                            Text(
                                text = if (lawyer.isOnline)
                                    "🔒  Start Secure Consultation"
                                else
                                    stringResource(Res.string.lawyer_profile_offline_button),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    Spacer(Modifier.height(Dimens.SpaceS))

                    Text(
                        text = if (biometricAvailable)
                            "🔒 Protected by biometric authentication"
                        else
                            "⚠️ Biometric unavailable on this device",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 88.dp)
        )
    }
}

@Composable
private fun ProfileHeader(lawyer: Lawyer) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        MonogramIcon(
            text = initials(lawyer.name),
            background = MaterialTheme.colorScheme.primary,
            size = 72.dp,
            cornerRadius = Dimens.RadiusXL
        )
        Spacer(Modifier.width(Dimens.SpaceL))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = lawyer.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(Dimens.SpaceXS))
            Text(
                text = lawyer.specialization,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(Dimens.SpaceXS))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            if (lawyer.isOnline) Color(0xFF2E7D32)
                            else MaterialTheme.colorScheme.outline
                        )
                )
                Spacer(Modifier.width(Dimens.SpaceXS))
                Text(
                    text = if (lawyer.isOnline)
                        stringResource(Res.string.lawyer_profile_online_now)
                    else
                        stringResource(Res.string.lawyer_status_offline),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatsRow(lawyer: Lawyer) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
    ) {
        StatTile(
            value = stringResource(Res.string.lawyer_profile_years_plus, lawyer.experienceYears),
            label = stringResource(Res.string.lawyer_profile_stat_years),
            modifier = Modifier.weight(1f)
        )
        StatTile(
            value = stringResource(Res.string.lawyer_profile_rating_format, lawyer.rating.toString()),
            label = stringResource(Res.string.lawyer_profile_stat_rating),
            modifier = Modifier.weight(1f)
        )
        StatTile(
            value = stringResource(Res.string.lawyer_profile_fee_format, lawyer.feePerSession),
            label = stringResource(Res.string.lawyer_profile_stat_per_session),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatTile(value: String, label: String, modifier: Modifier = Modifier) {
    AppCard(modifier = modifier, contentPadding = Dimens.SpaceM) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(Dimens.SpaceXS))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DetailLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SpaceXS),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun LanguageChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens.RadiusPill))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = Dimens.SpaceM, vertical = Dimens.SpaceXS)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun initials(fullName: String): String =
    fullName
        .removePrefix("Adv.")
        .trim()
        .split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")
