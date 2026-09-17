package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.WallpaperCategory
import com.example.ui.MainViewModel
import com.example.ui.components.WallpaperCard
import com.example.ui.theme.DivineCreamBg
import com.example.ui.theme.DivineTextDark
import com.example.ui.theme.DivineTextMuted
import com.example.ui.theme.SaffronOrange

@Composable
fun CategoryDetailScreen(
    category: WallpaperCategory,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allWallpapers by viewModel.allWallpapers.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    val categoryWallpapers = allWallpapers.filter {
        it.category.equals(category.id, ignoreCase = true) ||
                (it.deity?.contains(category.deity, ignoreCase = true) == true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DivineCreamBg)
            .testTag("category_detail_screen")
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("category_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = SaffronOrange
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = DivineTextDark
                    )
                )
                Text(
                    text = "${categoryWallpapers.size} sacred wallpapers",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = DivineTextMuted
                    )
                )
            }
        }

        if (categoryWallpapers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "🕉️", fontSize = 48.sp)
                    Text(
                        text = "New sacred wallpapers arriving soon for ${category.displayName}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = DivineTextMuted
                        )
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("category_wallpapers_grid"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(categoryWallpapers, key = { it.id }) { wallpaper ->
                    WallpaperCard(
                        wallpaper = wallpaper,
                        isFavorite = favorites.any { it.id == wallpaper.id },
                        onWallpaperClick = { viewModel.selectWallpaper(wallpaper) },
                        onFavoriteToggle = { viewModel.toggleFavorite(wallpaper) }
                    )
                }
            }
        }
    }
}
