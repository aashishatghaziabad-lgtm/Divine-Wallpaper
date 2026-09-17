package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Wallpaper
import com.example.ui.MainViewModel
import com.example.ui.components.WallpaperCard
import com.example.ui.theme.DivineCreamBg
import com.example.ui.theme.DivineCreamCard
import com.example.ui.theme.DivineTextDark
import com.example.ui.theme.DivineTextMuted
import com.example.ui.theme.LotusRose
import com.example.ui.theme.SaffronOrange

@Composable
fun FavoritesScreen(
    viewModel: MainViewModel,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val allWallpapers by viewModel.allWallpapers.collectAsStateWithLifecycle()

    val favoriteWallpapers = favorites.mapNotNull { fav ->
        allWallpapers.find { it.id == fav.id } ?: Wallpaper(
            id = fav.id,
            title = fav.title,
            category = fav.category,
            imageUrl = fav.imageUrl,
            thumbnailUrl = fav.thumbnailUrl,
            localDrawableRes = fav.localDrawableRes
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DivineCreamBg)
            .testTag("favorites_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = LotusRose,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "Favorite Wallpapers",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = DivineTextDark
                        )
                    )
                }
                Text(
                    text = "Saved locally on this device • Ready for offline darshan",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DivineTextMuted
                    )
                )
            }

            if (favoriteWallpapers.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SaffronOrange.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "${favoriteWallpapers.size} saved",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SaffronOrange,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        if (favoriteWallpapers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(DivineCreamCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🪷", fontSize = 38.sp)
                    }

                    Text(
                        text = "No favorites saved yet",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DivineTextDark
                        )
                    )

                    Text(
                        text = "Tap the heart icon on any devotional wallpaper to save it for quick access anytime, even offline.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = DivineTextMuted
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = onExploreClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SaffronOrange,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("explore_wallpapers_button")
                    ) {
                        Text(
                            text = "Explore Sacred Wallpapers 🕉️",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("favorites_grid"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(favoriteWallpapers, key = { it.id }) { wallpaper ->
                    WallpaperCard(
                        wallpaper = wallpaper,
                        isFavorite = true,
                        onWallpaperClick = { viewModel.selectWallpaper(wallpaper) },
                        onFavoriteToggle = { viewModel.toggleFavorite(wallpaper) }
                    )
                }
            }
        }
    }
}
