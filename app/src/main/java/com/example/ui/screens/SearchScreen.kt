package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.WallpaperCard
import com.example.ui.theme.DivineCreamBg
import com.example.ui.theme.DivineCreamBorder
import com.example.ui.theme.DivineCreamCard
import com.example.ui.theme.DivineCreamSurface
import com.example.ui.theme.DivineTextDark
import com.example.ui.theme.DivineTextMuted
import com.example.ui.theme.SaffronOrange

@Composable
fun SearchScreen(
    viewModel: MainViewModel,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedDeity by viewModel.selectedDeityFilter.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    val quickKeywords = listOf("Mahadev", "Krishna", "Hanuman", "Shri Ram", "Ganesh", "Shivratri", "Diwali", "Om", "Durga Maa")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DivineCreamBg)
            .testTag("search_screen")
    ) {
        // Search Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("search_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = SaffronOrange
                    )
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = {
                    Text(
                        text = "Search by deity, title, mantra...",
                        color = DivineTextMuted,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = SaffronOrange
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.onSearchQueryChanged("") },
                            modifier = Modifier.testTag("clear_search_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = DivineTextMuted
                            )
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("search_text_input"),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DivineCreamSurface,
                    unfocusedContainerColor = DivineCreamSurface,
                    focusedBorderColor = SaffronOrange,
                    unfocusedBorderColor = DivineCreamBorder,
                    focusedTextColor = DivineTextDark,
                    unfocusedTextColor = DivineTextDark,
                    cursorColor = SaffronOrange
                ),
                singleLine = true
            )
        }

        // Quick Tag Suggestions Carousel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickKeywords.forEach { keyword ->
                val isSelected = searchQuery.equals(keyword, ignoreCase = true) || selectedDeity.equals(keyword, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) SaffronOrange else DivineCreamCard)
                        .border(
                            1.dp,
                            if (isSelected) SaffronOrange else DivineCreamBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            if (isSelected) {
                                viewModel.onSearchQueryChanged("")
                                viewModel.selectDeityFilter(null)
                            } else {
                                viewModel.onSearchQueryChanged(keyword)
                            }
                        }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("search_tag_$keyword")
                ) {
                    Text(
                        text = keyword,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (isSelected) Color.White else DivineTextDark,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    )
                }
            }
        }

        // Results Count Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (searchQuery.isBlank()) "All Wallpapers" else "Results for \"$searchQuery\"",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = DivineTextDark,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Text(
                text = "${searchResults.size} wallpapers",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = SaffronOrange,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // Search Results Grid
        if (searchResults.isEmpty()) {
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
                    Text(text = "🔍", fontSize = 42.sp)
                    Text(
                        text = "No devotional wallpapers found",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = DivineTextDark,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Try searching for \"Mahadev\", \"Krishna\", \"Hanuman\", or \"Diwali\"",
                        style = MaterialTheme.typography.bodySmall.copy(
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
                    .testTag("search_results_grid"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(searchResults, key = { it.id }) { wallpaper ->
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
