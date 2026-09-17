package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.WallpaperCategory
import com.example.ui.MainViewModel
import com.example.ui.theme.DivineCreamBg
import com.example.ui.theme.DivineCreamBorder
import com.example.ui.theme.DivineCreamCard
import com.example.ui.theme.DivineGoldPrimary
import com.example.ui.theme.DivineTextDark
import com.example.ui.theme.DivineTextMuted
import com.example.ui.theme.SaffronOrange

@Composable
fun CategoriesScreen(
    viewModel: MainViewModel,
    onCategoryClick: (WallpaperCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val allWallpapers by viewModel.allWallpapers.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DivineCreamBg)
            .testTag("categories_screen_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(
                    text = "Sacred Categories",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = DivineTextDark
                    )
                )
                Text(
                    text = "Explore devotional wallpapers honoring deities and traditions",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DivineTextMuted
                    )
                )
            }
        }

        items(WallpaperCategory.entries, key = { it.id }) { category ->
            val count = allWallpapers.count { it.category.equals(category.id, ignoreCase = true) }

            CategoryCard(
                category = category,
                wallpaperCount = count,
                onClick = {
                    viewModel.selectCategory(category)
                    onCategoryClick(category)
                }
            )
        }
    }
}

@Composable
private fun CategoryCard(
    category: WallpaperCategory,
    wallpaperCount: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, DivineCreamBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("category_card_${category.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DivineCreamCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Sacred Symbol / Deity Artwork Thumbnail
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, DivineGoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (category.iconDrawableRes != null && category.iconDrawableRes != 0) {
                        Image(
                            painter = painterResource(id = category.iconDrawableRes),
                            contentDescription = category.displayName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        val iconText = when (category) {
                            WallpaperCategory.MAHADEV -> "🔱"
                            WallpaperCategory.HANUMAN -> "🚩"
                            WallpaperCategory.SHRI_RAM -> "🏹"
                            WallpaperCategory.KRISHNA -> "🦚"
                            WallpaperCategory.GANESH -> "🐘"
                            WallpaperCategory.DURGA_MAA -> "🦁"
                            WallpaperCategory.LAKSHMI_MAA -> "🪷"
                            WallpaperCategory.RADHA_KRISHNA -> "💖"
                            WallpaperCategory.SPIRITUAL_OM -> "🕉️"
                            WallpaperCategory.FESTIVALS -> "🪔"
                        }
                        Text(text = iconText, fontSize = 22.sp)
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DivineTextDark
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = category.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DivineTextMuted,
                            fontSize = 11.5.sp
                        ),
                        maxLines = 1
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SaffronOrange.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$wallpaperCount wallpapers",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SaffronOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = DivineTextMuted,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
