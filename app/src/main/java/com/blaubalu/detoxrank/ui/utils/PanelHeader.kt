package com.blaubalu.detoxrank.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Header shared by the full-screen panels (theme select, theme shop, ranks,
 * achievements): a title paired with a compact tonal close button. Keeps the
 * heading size and close-button proportions consistent across every panel.
 *
 * Pass panel-specific padding/insets through [modifier].
 */
@Composable
fun PanelHeader(
    title: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    closeDescription: String = "Close"
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        FilledTonalIconButton(
            onClick = onClose,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = closeDescription,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
