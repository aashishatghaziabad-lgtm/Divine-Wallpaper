package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.NavigationTab
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.CategoryDetailScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WallpaperDetailScreen
import com.example.ui.theme.DevotionalNavyCard
import com.example.ui.theme.DevotionalNavyDark
import com.example.ui.theme.DevotionalNavySurface
import com.example.ui.theme.DivineCreamBg
import com.example.ui.theme.DivineCreamSurface
import com.example.ui.theme.DivineGoldPrimary
import com.example.ui.theme.DivineTextDark
import com.example.ui.theme.DivineTextMuted
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SaffronOrange
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                DivineWallpapersApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DivineWallpapersApp(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedWallpaper by viewModel.selectedWallpaper.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Handle Android system back gesture
    BackHandler(enabled = selectedWallpaper != null || selectedCategory != null || isSearching) {
        if (selectedWallpaper != null) {
            viewModel.selectWallpaper(null)
        } else if (selectedCategory != null) {
            viewModel.selectCategory(null)
        } else if (isSearching) {
            viewModel.closeSearch()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = if (selectedWallpaper != null) Color.Black else DivineCreamBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            // Hide bottom bar in full-screen wallpaper preview
            if (selectedWallpaper == null) {
                NavigationBar(
                    containerColor = DivineCreamSurface,
                    contentColor = DivineTextDark,
                    tonalElevation = 3.dp,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_nav_bar")
                ) {
                    // Tab 1: Home
                    NavigationBarItem(
                        selected = currentTab == NavigationTab.HOME && selectedCategory == null && !isSearching,
                        onClick = {
                            viewModel.selectCategory(null)
                            viewModel.closeSearch()
                            viewModel.setTab(NavigationTab.HOME)
                        },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavigationTab.HOME && selectedCategory == null && !isSearching) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Home",
                                fontWeight = if (currentTab == NavigationTab.HOME) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        colors = navigationBarItemColors(),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    // Tab 2: Categories
                    NavigationBarItem(
                        selected = (currentTab == NavigationTab.CATEGORIES || selectedCategory != null) && !isSearching,
                        onClick = {
                            viewModel.closeSearch()
                            viewModel.setTab(NavigationTab.CATEGORIES)
                        },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavigationTab.CATEGORIES) Icons.Filled.Category else Icons.Outlined.Category,
                                contentDescription = "Categories",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Categories",
                                fontWeight = if (currentTab == NavigationTab.CATEGORIES) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        colors = navigationBarItemColors(),
                        modifier = Modifier.testTag("nav_item_categories")
                    )

                    // Tab 3: Favorites
                    NavigationBarItem(
                        selected = currentTab == NavigationTab.FAVORITES && !isSearching,
                        onClick = {
                            viewModel.selectCategory(null)
                            viewModel.closeSearch()
                            viewModel.setTab(NavigationTab.FAVORITES)
                        },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavigationTab.FAVORITES) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorites",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Favorites",
                                fontWeight = if (currentTab == NavigationTab.FAVORITES) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        colors = navigationBarItemColors(),
                        modifier = Modifier.testTag("nav_item_favorites")
                    )

                    // Tab 4: Settings
                    NavigationBarItem(
                        selected = currentTab == NavigationTab.SETTINGS && !isSearching,
                        onClick = {
                            viewModel.selectCategory(null)
                            viewModel.closeSearch()
                            viewModel.setTab(NavigationTab.SETTINGS)
                        },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavigationTab.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                                contentDescription = "Settings",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Settings",
                                fontWeight = if (currentTab == NavigationTab.SETTINGS) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        colors = navigationBarItemColors(),
                        modifier = Modifier.testTag("nav_item_settings")
                    )
                }
            }
        }
    ) { innerPadding ->
        if (selectedWallpaper != null) {
            WallpaperDetailScreen(
                wallpaper = selectedWallpaper!!,
                viewModel = viewModel,
                onBack = { viewModel.selectWallpaper(null) }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when {
                    // If user activated search -> Show Search Screen
                    isSearching -> {
                        SearchScreen(
                            viewModel = viewModel,
                            onBack = { viewModel.closeSearch() }
                        )
                    }
                    // If a category is selected -> Show Category Wallpapers Page
                    selectedCategory != null -> {
                        CategoryDetailScreen(
                            category = selectedCategory!!,
                            viewModel = viewModel,
                            onBack = { viewModel.selectCategory(null) }
                        )
                    }
                    // Otherwise show standard tab destination
                    else -> {
                        when (currentTab) {
                            NavigationTab.HOME -> {
                                HomeScreen(
                                    viewModel = viewModel,
                                    onNavigateToSearch = {
                                        viewModel.openSearch()
                                    },
                                    onNavigateToCategories = {
                                        viewModel.setTab(NavigationTab.CATEGORIES)
                                    }
                                )
                            }
                            NavigationTab.CATEGORIES -> {
                                CategoriesScreen(
                                    viewModel = viewModel,
                                    onCategoryClick = { category ->
                                        viewModel.selectCategory(category)
                                    }
                                )
                            }
                            NavigationTab.FAVORITES -> {
                                FavoritesScreen(
                                    viewModel = viewModel,
                                    onExploreClick = { viewModel.setTab(NavigationTab.HOME) }
                                )
                            }
                            NavigationTab.SETTINGS -> {
                                SettingsScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun navigationBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = SaffronOrange,
    selectedTextColor = SaffronOrange,
    indicatorColor = Color.Transparent,
    unselectedIconColor = DivineTextMuted,
    unselectedTextColor = DivineTextMuted
)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Divine Wallpapers - $name",
        modifier = modifier,
        color = DivineGoldPrimary
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Android") }
}
