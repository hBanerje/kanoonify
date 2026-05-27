package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.multiplatform.kanoonify.presentation.screens.components.KanoonifyLogo
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance

@Composable
fun LandingScreen(
    onAskClick: () -> Unit,
    onBrowseLawsClick: () -> Unit,
    onCoiClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.SpaceXL),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedEntrance { KanoonifyLogo() }

            Spacer(Modifier.height(Dimens.SpaceL))

            AnimatedEntrance(delayMillis = 100) {
                Text(
                    text  = "Kanoonify",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(Modifier.height(Dimens.SpaceXS))
            AnimatedEntrance(delayMillis = 160) {
                Text(
                    text  = "Know your rights, instantly.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 240) {
                Button(
                    onClick = onAskClick,
                    shape = RoundedCornerShape(Dimens.RadiusL),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor   = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpaceL)
                ) {
                    Text("Ask Kanoonify", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(Modifier.height(Dimens.SpaceM))

            AnimatedEntrance(delayMillis = 300) {
                OutlinedButton(
                    onClick = onBrowseLawsClick,
                    shape = RoundedCornerShape(Dimens.RadiusL),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpaceL)
                ) {
                    Text("Browse Laws", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(Modifier.height(Dimens.SpaceM))

            AnimatedEntrance(delayMillis = 360) {
                Card(
                    onClick = onCoiClick,
                    shape = RoundedCornerShape(Dimens.RadiusL),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor   = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = Dimens.SpaceXS),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpaceL)
                ) {
                    Text(
                        text = "Constitution of India",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.SpaceL),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}