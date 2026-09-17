package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.FavoriteWallpaperEntity
import com.example.data.model.Wallpaper
import com.example.data.model.WallpaperCategory
import com.example.data.repository.WallpaperRepository
import com.example.util.WallpaperHelper
import com.example.util.WallpaperTarget
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavigationTab(val label: String) {
    HOME("Home"),
    CATEGORIES("Categories"),
    FAVORITES("Favorites"),
    SETTINGS("Settings")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WallpaperRepository(application)

    // Current top-level bottom nav tab
    private val _currentTab = MutableStateFlow(NavigationTab.HOME)
    val currentTab = _currentTab.asStateFlow()

    // Wallpaper detail screen state (null means not in detail screen)
    private val _selectedWallpaper = MutableStateFlow<Wallpaper?>(null)
    val selectedWallpaper = _selectedWallpaper.asStateFlow()

    // Category page state (null means not inside specific category view)
    private val _selectedCategory = MutableStateFlow<WallpaperCategory?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    // Search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedDeityFilter = MutableStateFlow<String?>(null)
    val selectedDeityFilter = _selectedDeityFilter.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    // All wallpapers from Repository
    val allWallpapers = repository.wallpapers

    // Favorites from Room Database
    val favorites = repository.getFavorites().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val isLoading = repository.isLoading
    val error = repository.error

    // Snackbars / UI feedback messages
    private val _uiEvents = MutableSharedFlow<String>()
    val uiEvents = _uiEvents.asSharedFlow()

    // Derived states
    val dailyWallpaper = allWallpapers.map { wallpapers ->
        wallpapers.firstOrNull { it.daily } ?: wallpapers.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val trendingWallpapers = allWallpapers.map { wallpapers ->
        wallpapers.filter { it.trending || it.downloads > 1000 }.sortedByDescending { it.downloads }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredWallpapers = allWallpapers.map { wallpapers ->
        wallpapers.filter { it.featured }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchResults = combine(allWallpapers, _searchQuery, _selectedDeityFilter) { wallpapers, query, deity ->
        wallpapers.filter { wp ->
            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                val clean = query.trim().lowercase()
                wp.title.lowercase().contains(clean) ||
                        wp.category.lowercase().contains(clean) ||
                        (wp.deity?.lowercase()?.contains(clean) == true) ||
                        wp.tags.any { it.lowercase().contains(clean) }
            }

            val matchesDeity = if (deity == null) {
                true
            } else {
                wp.deity.equals(deity, ignoreCase = true) ||
                        wp.tags.any { it.equals(deity, ignoreCase = true) } ||
                        wp.category.equals(deity, ignoreCase = true)
            }

            matchesQuery && matchesDeity
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.refreshWallpapers()
        }
    }

    fun retryRefresh() {
        viewModelScope.launch {
            repository.refreshWallpapers()
        }
    }

    fun dismissError() {
        repository.dismissError()
    }

    fun setTab(tab: NavigationTab) {
        _currentTab.value = tab
        // Exit sub-screens if changing tabs
        _selectedCategory.value = null
        _isSearching.value = false
    }

    fun openSearch() {
        _isSearching.value = true
    }

    fun closeSearch() {
        _isSearching.value = false
        _searchQuery.value = ""
        _selectedDeityFilter.value = null
    }

    fun selectWallpaper(wallpaper: Wallpaper?) {
        _selectedWallpaper.value = wallpaper
        if (wallpaper != null) {
            viewModelScope.launch {
                repository.incrementViewCount(wallpaper.id)
            }
        }
    }

    fun selectCategory(category: WallpaperCategory?) {
        _selectedCategory.value = category
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun selectDeityFilter(deity: String?) {
        _selectedDeityFilter.value = if (_selectedDeityFilter.value == deity) null else deity
    }

    fun isFavorite(wallpaperId: String): Boolean {
        return favorites.value.any { it.id == wallpaperId }
    }

    fun toggleFavorite(wallpaper: Wallpaper) {
        val isFav = isFavorite(wallpaper.id)
        viewModelScope.launch {
            repository.toggleFavorite(wallpaper, isFav)
            _uiEvents.emit(if (isFav) "Removed from Favorites" else "Saved to Favorites ❤️")
        }
    }

    fun setDeviceWallpaper(target: WallpaperTarget) {
        val wp = _selectedWallpaper.value ?: return
        viewModelScope.launch {
            _uiEvents.emit("Setting wallpaper on ${target.label}...")
            val result = WallpaperHelper.setDeviceWallpaper(getApplication(), wp, target)
            if (result.isSuccess) {
                _uiEvents.emit("Sacred wallpaper applied to ${target.label} ✨")
            } else {
                _uiEvents.emit("Failed to set wallpaper: ${result.exceptionOrNull()?.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun downloadWallpaper() {
        val wp = _selectedWallpaper.value ?: return
        viewModelScope.launch {
            _uiEvents.emit("Downloading wallpaper to Gallery...")
            val result = WallpaperHelper.saveWallpaperToGallery(getApplication(), wp)
            if (result.isSuccess) {
                repository.incrementDownloadCount(wp.id)
                _uiEvents.emit("Saved to Pictures/DivineWallpapers in Gallery! 📥")
            } else {
                _uiEvents.emit("Download failed: ${result.exceptionOrNull()?.localizedMessage}")
            }
        }
    }

    fun shareWallpaper() {
        val wp = _selectedWallpaper.value ?: return
        viewModelScope.launch {
            WallpaperHelper.shareWallpaper(getApplication(), wp)
        }
    }
}
