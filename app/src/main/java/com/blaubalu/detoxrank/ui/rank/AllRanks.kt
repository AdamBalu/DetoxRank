package com.blaubalu.detoxrank.ui.rank

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaubalu.detoxrank.data.local.LocalRankDataProvider.ranksSeparated
import com.blaubalu.detoxrank.ui.theme.JosefinSans
import com.blaubalu.detoxrank.ui.theme.LocalThemeStyle
import com.blaubalu.detoxrank.ui.theme.glassCardSheen
import com.blaubalu.detoxrank.ui.utils.PanelHeader

@Composable
fun AllRanks(
  rankViewModel: RankViewModel,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = rankViewModel.ranksDisplayed.value,
    enter = fadeIn(),
    exit = fadeOut()
  ) {
    BackHandler {
      rankViewModel.setRanksDisplayed(false)
    }
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .align(Alignment.TopCenter)
          // edge-to-edge (target SDK 36): keep the header below the status bar
          .statusBarsPadding()
      ) {
        PanelHeader(
          title = "Ranks",
          onClose = { rankViewModel.setRanksDisplayed(false) },
          modifier = Modifier.padding(start = 20.dp, end = 12.dp, top = 10.dp, bottom = 4.dp)
        )
        LazyColumn(
          // edge-to-edge (target SDK 36): last card clears the nav bar
          contentPadding = WindowInsets.navigationBars.asPaddingValues()
        ) {
          items(ranksSeparated) { rankList ->
            Card(
              shape = LocalThemeStyle.current.cardShape ?: CardDefaults.shape,
              border = LocalThemeStyle.current.cardBorder,
              colors = CardDefaults.cardColors(
                MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp),
                contentColor = MaterialTheme.colorScheme.onSurface
              ),
              modifier = Modifier.padding(
                start = 14.dp,
                end = 14.dp,
                top = 6.dp,
                bottom = 6.dp
              )
            ) {
              Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .fillMaxWidth()
                  .glassCardSheen(LocalThemeStyle.current.cardSheen),
              ) {
                rankList.forEach { rank ->
                  Column(modifier = Modifier.padding(8.dp)) {
                    Image(
                      painterResource(id = getRankDrawableId(rank)),
                      contentDescription = null,
                      modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                    )
                    Text(
                      rank.rankName,
                      fontSize = 16.sp,
                      fontFamily = JosefinSans,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                  }
                }
              }
            }
          }
          item {
            Spacer(modifier = Modifier.height(16.dp))
          }
        }
      }
    }
  }
}