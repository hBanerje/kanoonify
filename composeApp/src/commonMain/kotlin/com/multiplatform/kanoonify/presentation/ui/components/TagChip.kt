package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KTagFineBg
import com.multiplatform.kanoonify.presentation.theme.KTagFineFg
import com.multiplatform.kanoonify.presentation.theme.KTagJailBg
import com.multiplatform.kanoonify.presentation.theme.KTagJailFg
import com.multiplatform.kanoonify.presentation.theme.KTagRightBg
import com.multiplatform.kanoonify.presentation.theme.KTagRightFg

enum class LawTag(val label: String) {
    FINE("Fine"),
    JAIL("Jail"),
    RIGHT("Right")
}

/** Deterministic mapping from punishment text → tag. */
fun deriveLawTag(punishment: String): LawTag {
    val p = punishment.lowercase()
    return when {
        p.contains("jail") || p.contains("imprison") || p.contains("custody") ||
            p.contains("years") || p.contains("year ") || p.contains("month") -> LawTag.JAIL
        p.contains("fine") || p.contains("rs") || p.contains("₹") ||
            p.contains("challan") || p.contains("penalty") -> LawTag.FINE
        else -> LawTag.RIGHT
    }
}

@Composable
fun TagChip(tag: LawTag, modifier: Modifier = Modifier) {
    val (bg, fg) = when (tag) {
        LawTag.FINE  -> KTagFineBg to KTagFineFg
        LawTag.JAIL  -> KTagJailBg to KTagJailFg
        LawTag.RIGHT -> KTagRightBg to KTagRightFg
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens.RadiusPill))
            .background(bg)
            .padding(horizontal = Dimens.SpaceM, vertical = Dimens.SpaceXS)
    ) {
        Text(
            text  = tag.label,
            color = fg,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

