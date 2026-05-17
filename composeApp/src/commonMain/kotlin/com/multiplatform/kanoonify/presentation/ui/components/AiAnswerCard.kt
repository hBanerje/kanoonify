package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.multiplatform.kanoonify.domain.model.AskAnswer
import com.multiplatform.kanoonify.presentation.theme.Dimens
import kotlinx.coroutines.delay

/**
 * Structured AI response broken into three cards:
 *  - Your Rights
 *  - Applicable Law
 *  - What You Should Do
 *
 * Each card animates in sequentially with a typing reveal effect for its body.
 */
@Composable
fun AiAnswerCard(
    answer: AskAnswer,
    modifier: Modifier = Modifier
) {
    when (answer) {
        is AskAnswer.Found -> Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
        ) {
            AnimatedSection(
                index = 0,
                title = "Your Rights",
                body  = answer.rights
            )
            AnimatedSection(
                index = 1,
                title = "Applicable Law",
                body  = answer.applicableLaw
            )
            AnimatedSection(
                index = 2,
                title = "What You Should Do",
                body  = answer.whatToDo
            )
        }
        is AskAnswer.NotFound -> AppCard(modifier = modifier.fillMaxWidth()) {
            Column {
                Text(
                    text = "No matching law found",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(Dimens.SpaceXS))
                Text(
                    text = "Try describing your situation differently — e.g. \"police stopped me without warrant\".",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AnimatedSection(
    index: Int,
    title: String,
    body: String,
    perSectionDelay: Long = 250L
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * perSectionDelay)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(380)) +
            expandVertically(animationSpec = tween(380)),
        exit  = fadeOut() + shrinkVertically()
    ) {
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                CardSectionTitle(title = title.uppercase())
                Spacer(Modifier.height(Dimens.SpaceS))
                TypingText(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

