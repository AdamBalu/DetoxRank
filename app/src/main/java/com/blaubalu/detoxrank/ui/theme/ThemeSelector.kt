package com.blaubalu.detoxrank.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaubalu.detoxrank.data.user.UiTheme

/**
 * Data class representing a theme option for display
 */
data class ThemeOption(
    val theme: UiTheme,
    val name: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val backgroundColor: Color,
    val isPremium: Boolean = false
)

/**
 * Get all available theme options
 */
fun getThemeOptions(): List<ThemeOption> = listOf(
    ThemeOption(
        theme = UiTheme.Default,
        name = "Default",
        primaryColor = md_theme_dark_primary,
        secondaryColor = md_theme_dark_secondary,
        backgroundColor = md_theme_dark_background,
        isPremium = false
    ),
    ThemeOption(
        theme = UiTheme.Light,
        name = "Light",
        primaryColor = md_theme_light_primary,
        secondaryColor = md_theme_light_secondary,
        backgroundColor = md_theme_light_background,
        isPremium = false
    ),
    ThemeOption(
        theme = UiTheme.Dark,
        name = "Dark",
        primaryColor = md_theme_dark_primary,
        secondaryColor = md_theme_dark_secondary,
        backgroundColor = Color(0xFF0A0A0A),
        isPremium = false
    ),
    ThemeOption(
        theme = UiTheme.Monochrome,
        name = "Monochrome",
        primaryColor = Color(0xFF9E9E9E),
        secondaryColor = Color(0xFF616161),
        backgroundColor = Color(0xFF1A1A1A),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.GreenShades,
        name = "Forest",
        primaryColor = Color(0xFF4CAF50),
        secondaryColor = Color(0xFF81C784),
        backgroundColor = Color(0xFF0D1F0E),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.BlueShades,
        name = "Ocean",
        primaryColor = Color(0xFF2196F3),
        secondaryColor = Color(0xFF64B5F6),
        backgroundColor = Color(0xFF0A1929),
        isPremium = true
    )
)

/**
 * Icon button to open theme selector
 */
@Composable
fun ThemeSelectorButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Palette,
            contentDescription = "Select Theme",
            tint = MaterialTheme.colorScheme.secondaryContainer
        )
    }
}

/**
 * Bottom sheet for theme selection
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectorSheet(
    isVisible: Boolean,
    currentTheme: UiTheme,
    purchasedThemes: Set<UiTheme>,
    onThemeSelected: (UiTheme) -> Unit,
    onPurchaseTheme: (UiTheme) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Theme",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    items(getThemeOptions()) { option ->
                        val isOwned = purchasedThemes.contains(option.theme)
                        val isSelected = currentTheme == option.theme
                        
                        ThemeCard(
                            option = option,
                            isSelected = isSelected,
                            isOwned = isOwned,
                            onClick = {
                                onThemeSelected(option.theme)
                            }
                        )

                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Card representing a single theme option
 */
@Composable
fun ThemeCard(
    option: ThemeOption,
    isSelected: Boolean,
    isOwned: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                ) else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = option.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Color preview circles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(option.primaryColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(option.secondaryColor)
                )
            }
            
            // Theme name
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                if (!isOwned && option.isPremium) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Premium",
                            tint = rank_color,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = " Locked",
                            style = MaterialTheme.typography.labelSmall,
                            color = rank_color,
                            fontSize = 10.sp
                        )
                    }
                }
                Text(
                    text = option.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = option.primaryColor,
                    textAlign = TextAlign.Center,
                )
            }
            
            // Selected checkmark
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
