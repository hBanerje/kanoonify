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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.multiplatform.kanoonify.data.LawyerDataProvider
import com.multiplatform.kanoonify.domain.model.Lawyer
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.AppCard
import com.multiplatform.kanoonify.presentation.ui.components.MonogramIcon
import com.multiplatform.kanoonify.presentation.ui.components.SectionHeader

@Composable
fun LawyerProfileScreen(
    lawyerId: String,
    onChatClick: (Lawyer) -> Unit
) {
    val lawyer = LawyerDataProvider.findById(lawyerId)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (lawyer == null) {
            Text(
                text = "Lawyer not found",
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
                            SectionHeader(title = "About")
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
                            SectionHeader(title = "Practice")
                            Spacer(Modifier.height(Dimens.SpaceS))
                            DetailLine(label = "Specialisation", value = lawyer.specialization)
                            DetailLine(label = "Experience", value = "${lawyer.experienceYears} years")
                            DetailLine(label = "Location", value = lawyer.location)
                            DetailLine(label = "Rating", value = "★ ${lawyer.rating}")
                            DetailLine(label = "Fee per session", value = "₹${lawyer.feePerSession}")
                        }
                    }
                }

                Spacer(Modifier.height(Dimens.SpaceM))

                AnimatedEntrance(delayMillis = 260) {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SectionHeader(title = "Languages")
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

            // Sticky CTA
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
                Button(
                    onClick = { onChatClick(lawyer) },
                    enabled = lawyer.isOnline,
                    shape = RoundedCornerShape(Dimens.RadiusL),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (lawyer.isOnline) "Chat with ${shortName(lawyer.name)}"
                        else "Currently offline",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
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
                    text = if (lawyer.isOnline) "Online now" else "Offline",
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
        StatTile(value = "${lawyer.experienceYears}+", label = "Years", modifier = Modifier.weight(1f))
        StatTile(value = "★ ${lawyer.rating}", label = "Rating", modifier = Modifier.weight(1f))
        StatTile(value = "₹${lawyer.feePerSession}", label = "Per session", modifier = Modifier.weight(1f))
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

private fun shortName(fullName: String): String {
    val cleaned = fullName.removePrefix("Adv.").trim()
    return cleaned.split(" ").firstOrNull() ?: cleaned
}

