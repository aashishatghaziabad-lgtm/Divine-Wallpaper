package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.Wallpaper
import com.example.data.model.WallpaperCategory
import com.example.ui.MainViewModel
import com.example.ui.components.WallpaperCard
import com.example.ui.theme.DivineCreamBg
import com.example.ui.theme.DivineCreamBorder
import com.example.ui.theme.DivineCreamCard
import com.example.ui.theme.DivineCreamSurface
import com.example.ui.theme.DivineGoldPrimary
import com.example.ui.theme.DivineTextDark
import com.example.ui.theme.DivineTextMuted
import com.example.ui.theme.DivineTextSubtitle
import com.example.ui.theme.LotusRose
import com.example.ui.theme.SaffronOrange

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategories: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allWallpapers by viewModel.allWallpapers.collectAsStateWithLifecycle()
    val dailyWallpaper by viewModel.dailyWallpaper.collectAsStateWithLifecycle()
    val trendingWallpapers by viewModel.trendingWallpapers.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .background(DivineCreamBg)
            .testTag("home_screen_grid"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 28.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top App Header: Sacred Lotus + Title + Subtitle
        item(span = { GridItemSpan(2) }) {
            HomeDevotionalHeader()
        }

        // Search Bar Capsule (pill shape with white background)
        item(span = { GridItemSpan(2) }) {
            HomeSearchBar(onSearchClick = onNavigateToSearch)
        }

        // Offline / Error Notification Banner with Retry Option
        if (error != null) {
            item(span = { GridItemSpan(2) }) {
                OfflineErrorBanner(
                    errorMessage = error ?: "Connection issue",
                    onRetry = { viewModel.retryRefresh() },
                    onDismiss = { viewModel.dismissError() }
                )
            }
        }

        // "Today's Divine Wallpaper" Banner
        item(span = { GridItemSpan(2) }) {
            TodayDivineWallpaperBanner(
                wallpaper = dailyWallpaper ?: allWallpapers.firstOrNull(),
                onClick = {
                    val target = dailyWallpaper ?: allWallpapers.firstOrNull()
                    if (target != null) viewModel.selectWallpaper(target)
                }
            )
        }

        // "Categories" Section with 4-Column x 2-Row Grid
        item(span = { GridItemSpan(2) }) {
            CategoriesSection(
                onCategoryClick = { category ->
                    viewModel.selectCategory(category)
                    onNavigateToCategories()
                },
                onSeeAllClick = onNavigateToCategories
            )
        }

        // "Trending Wallpapers" Horizontal Carousel
        if (trendingWallpapers.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                TrendingSection(
                    trendingWallpapers = trendingWallpapers,
                    favorites = favorites.map { it.id }.toSet(),
                    onWallpaperClick = { viewModel.selectWallpaper(it) },
                    onFavoriteToggle = { viewModel.toggleFavorite(it) },
                    onSeeAllClick = onNavigateToCategories
                )
            }
        }

        // Section Title: "Latest Wallpapers" Grid Header
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Wallpaper,
                    contentDescription = null,
                    tint = SaffronOrange,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "All Devotional Wallpapers",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DivineTextDark
                    )
                )
            }
        }

        // 2-Column Grid of Devotional Wallpapers
        items(allWallpapers, key = { it.id }) { wallpaper ->
            WallpaperCard(
                wallpaper = wallpaper,
                isFavorite = favorites.any { it.id == wallpaper.id },
                onWallpaperClick = { viewModel.selectWallpaper(wallpaper) },
                onFavoriteToggle = { viewModel.toggleFavorite(wallpaper) }
            )
        }
    }
}

/**
 * Top Devotional Header matching reference screenshot:
 * Centered Lotus symbol, Serif "Divine Wallpapers", and "Faith • Peace • Positivity".
 */
@Composable
private fun HomeDevotionalHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sacred Lotus Icon
        Image(
            painter = painterResource(id = R.drawable.ic_sacred_lotus),
            contentDescription = "Sacred Lotus",
            modifier = Modifier.size(34.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Title in Serif Typography
        Text(
            text = "Divine Wallpapers",
            style = MaterialTheme.typography.displayMedium.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = DivineTextDark,
                letterSpacing = 0.5.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Subtitle
        Text(
            text = "Faith  •  Peace  •  Positivity",
            style = MaterialTheme.typography.labelMedium.copy(
                color = DivineTextSubtitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.5.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Search Bar Capsule Pill matching reference screenshot.
 */
@Composable
private fun HomeSearchBar(onSearchClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(DivineCreamSurface)
            .border(1.dp, DivineCreamBorder, RoundedCornerShape(24.dp))
            .clickable(onClick = onSearchClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("home_search_bar")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = DivineTextMuted,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Search wallpapers...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = DivineTextMuted,
                    fontSize = 14.sp
                )
            )
        }
    }
}

/**
 * Offline / Error Banner providing clear error feedback and recovery.
 */
@Composable
private fun OfflineErrorBanner(
    errorMessage: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("offline_error_banner"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFED7AA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CloudOff,
                    contentDescription = null,
                    tint = SaffronOrange,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DivineTextDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(
                    onClick = onRetry,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Retry",
                        tint = SaffronOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Retry",
                        color = SaffronOrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = DivineTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * "Today's Divine Wallpaper" Banner matching reference screenshot:
 * Warm sunset temple background with glowing golden Om, title, subtitle, and "View Now" button.
 */
@Composable
private fun TodayDivineWallpaperBanner(
    wallpaper: Wallpaper?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(148.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("today_divine_banner"),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Artwork
            Image(
                painter = painterResource(id = R.drawable.banner_today_divine),
                contentDescription = "Today's Divine Wallpaper",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Warm gradient overlay for legibility
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.70f),
                                Color.Black.copy(alpha = 0.40f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Left Content: Title, Subtitle, "View Now" pill button
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Today's Divine Wallpaper",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 17.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Let positivity fill your day",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFF5EBE1),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }

                // "View Now" pill button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFFF7ED))
                        .clickable(onClick = onClick)
                        .padding(horizontal = 16.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = "View Now",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = DivineTextDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

/**
 * "Categories" Section with 4-Column x 2-Row Grid of Deity Artwork.
 */
@Composable
private fun CategoriesSection(
    onCategoryClick: (WallpaperCategory) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Section Header: "Categories" | "See All"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DivineTextDark,
                    fontSize = 18.sp
                )
            )
            Text(
                text = "See All",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = SaffronOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                modifier = Modifier
                    .clickable(onClick = onSeeAllClick)
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 8 Categories displayed in 2 rows of 4 columns
        val primaryCategories = listOf(
            WallpaperCategory.MAHADEV,
            WallpaperCategory.SHRI_RAM,
            WallpaperCategory.KRISHNA,
            WallpaperCategory.HANUMAN,
            WallpaperCategory.GANESH,
            WallpaperCategory.DURGA_MAA,
            WallpaperCategory.LAKSHMI_MAA,
            WallpaperCategory.RADHA_KRISHNA
        )

        // Row 1 (4 items)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            primaryCategories.take(4).forEach { category ->
                CategoryGridItem(
                    category = category,
                    onClick = { onCategoryClick(category) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row 2 (4 items)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            primaryCategories.drop(4).take(4).forEach { category ->
                CategoryGridItem(
                    category = category,
                    onClick = { onCategoryClick(category) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Individual Category Grid Item matching screenshot:
 * Rounded square with deity image + centered name below.
 */
@Composable
private fun CategoryGridItem(
    category: WallpaperCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Rounded Square Image Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DivineCreamCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            if (category.iconDrawableRes != null && category.iconDrawableRes != 0) {
                Image(
                    painter = painterResource(id = category.iconDrawableRes),
                    contentDescription = category.displayName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DivineCreamCard),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🕉️", fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Category Name
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = DivineTextDark,
                fontSize = 11.5.sp
            ),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * "Trending Wallpapers" Horizontal Scrolling Carousel matching screenshot.
 */
@Composable
private fun TrendingSection(
    trendingWallpapers: List<Wallpaper>,
    favorites: Set<String>,
    onWallpaperClick: (Wallpaper) -> Unit,
    onFavoriteToggle: (Wallpaper) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trending Wallpapers",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DivineTextDark,
                    fontSize = 18.sp
                )
            )
            Text(
                text = "See All",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = SaffronOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                modifier = Modifier
                    .clickable(onClick = onSeeAllClick)
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            trendingWallpapers.take(6).forEach { wp ->
                Box(modifier = Modifier.width(135.dp)) {
                    WallpaperCard(
                        wallpaper = wp,
                        isFavorite = favorites.contains(wp.id),
                        onWallpaperClick = { onWallpaperClick(wp) },
                        onFavoriteToggle = { onFavoriteToggle(wp) }
                    )
                }
            }
        }
    }
}
