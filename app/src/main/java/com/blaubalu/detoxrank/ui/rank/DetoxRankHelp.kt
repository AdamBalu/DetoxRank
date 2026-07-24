package com.blaubalu.detoxrank.ui.rank

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.ui.theme.LocalThemeStyle
import androidx.compose.ui.unit.dp

@Composable
fun DetoxRankHelp(
    rankViewModel: RankViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    AnimatedVisibility(
        visible = rankViewModel.helpDisplayed.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        BackHandler {
            rankViewModel.setHelpDisplayed(false)
        }
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painterResource(id = R.drawable.help_icon),
                            contentDescription = null,
                            modifier = Modifier.height(34.dp)
                        )
                        Text(
                            text = stringResource(R.string.help_heading),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                    IconButton(onClick = { rankViewModel.setHelpDisplayed(false) }) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = stringResource(R.string.close),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                HelpSection(
                    sectionId = R.string.tab_rank,
                    descriptionId = R.string.help_rank_description,
                    imageId = R.drawable.ranknavicon
                )
                HelpSection(
                    sectionId = R.string.tab_tasks,
                    descriptionId = R.string.help_tasks_description,
                    imageId = R.drawable.tasksnavicon
                )
                HelpSection(
                    sectionId = R.string.tab_timer,
                    descriptionId = R.string.help_timer_description,
                    imageId = R.drawable.timernavicon
                )
                HelpSection(
                    sectionId = R.string.tab_theory,
                    descriptionId = R.string.help_theory_description,
                    imageId = R.drawable.theorynavicon
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * One help topic as a themed card: icon chip + section name, description below
 */
@Composable
fun HelpSection(
    @StringRes sectionId: Int,
    @StringRes descriptionId: Int,
    @DrawableRes imageId: Int,
    modifier: Modifier = Modifier
) {
    val themeStyle = LocalThemeStyle.current
    Card(
        shape = themeStyle.cardShape ?: MaterialTheme.shapes.medium,
        border = themeStyle.cardBorder,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Image(
                        painterResource(id = imageId),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = stringResource(sectionId),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }
            Text(
                text = stringResource(descriptionId),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}
