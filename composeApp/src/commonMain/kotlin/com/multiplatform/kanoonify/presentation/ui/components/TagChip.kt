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
import com.multiplatform.kanoonify.domain.model.LawTag
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KTagFineBg
import com.multiplatform.kanoonify.presentation.theme.KTagFineFg
import com.multiplatform.kanoonify.presentation.theme.KTagJailBg
import com.multiplatform.kanoonify.presentation.theme.KTagJailFg
import com.multiplatform.kanoonify.presentation.theme.KTagRightBg
import com.multiplatform.kanoonify.presentation.theme.KTagRightFg

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
