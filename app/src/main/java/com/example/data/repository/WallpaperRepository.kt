package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.local.FavoriteWallpaperEntity
import com.example.data.model.Wallpaper
import com.example.data.model.WallpaperCategory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WallpaperRepository(context: Context) {
    private val favoriteDao = AppDatabase.getInstance(context).favoriteDao()

    private val _wallpapers = MutableStateFlow<List<Wallpaper>>(emptyList())
    val wallpapers = _wallpapers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        // Initialize with default rich curated collection immediately
        _wallpapers.value = getCuratedSampleWallpapers()
    }

    suspend fun refreshWallpapers() = withContext(Dispatchers.IO) {
        _isLoading.value = true
        _error.value = null
        try {
            // Attempt to query Firestore
            val firestore = FirebaseFirestore.getInstance()
            val snapshot = firestore.collection("wallpapers")
                .limit(100)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val remoteList = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    @Suppress("UNCHECKED_CAST")
                    Wallpaper(
                        id = doc.id,
                        title = data["title"] as? String ?: "Sacred Devotion",
                        category = data["category"] as? String ?: "spiritual",
                        tags = (data["tags"] as? List<String>) ?: emptyList(),
                        imageUrl = data["imageUrl"] as? String ?: "",
                        thumbnailUrl = data["thumbnailUrl"] as? String ?: "",
                        featured = data["featured"] as? Boolean ?: false,
                        trending = data["trending"] as? Boolean ?: false,
                        daily = data["daily"] as? Boolean ?: false,
                        downloads = (data["downloads"] as? Number)?.toLong() ?: 0L,
                        views = (data["views"] as? Number)?.toLong() ?: 0L,
                        createdAt = (data["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                        deity = data["deity"] as? String
                    )
                }
                if (remoteList.isNotEmpty()) {
                    _wallpapers.value = remoteList
                }
            } else {
                // Keep the curated devotional collection
                _wallpapers.value = getCuratedSampleWallpapers()
            }
        } catch (e: Exception) {
            Log.w("WallpaperRepository", "Firestore fetch skipped or unavailable: ${e.message}")
            // Fallback gracefully without crash
            if (_wallpapers.value.isEmpty()) {
                _wallpapers.value = getCuratedSampleWallpapers()
            }
            _error.value = "Offline Mode: Showing offline devotional collection"
        } finally {
            _isLoading.value = false
        }
    }

    fun dismissError() {
        _error.value = null
    }

    fun getFavorites(): Flow<List<FavoriteWallpaperEntity>> = favoriteDao.getAllFavorites()

    fun isFavorite(id: String): Flow<Boolean> = favoriteDao.isFavorite(id)

    suspend fun toggleFavorite(wallpaper: Wallpaper, isFav: Boolean) = withContext(Dispatchers.IO) {
        if (isFav) {
            favoriteDao.deleteFavorite(wallpaper.id)
        } else {
            favoriteDao.insertFavorite(
                FavoriteWallpaperEntity(
                    id = wallpaper.id,
                    title = wallpaper.title,
                    category = wallpaper.category,
                    imageUrl = wallpaper.imageUrl,
                    thumbnailUrl = wallpaper.thumbnailUrl,
                    localDrawableRes = wallpaper.localDrawableRes,
                    addedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun incrementViewCount(wallpaperId: String) = withContext(Dispatchers.IO) {
        _wallpapers.value = _wallpapers.value.map { wp ->
            if (wp.id == wallpaperId) wp.copy(views = wp.views + 1) else wp
        }
        try {
            FirebaseFirestore.getInstance().collection("wallpapers")
                .document(wallpaperId)
                .update("views", com.google.firebase.firestore.FieldValue.increment(1))
        } catch (ignored: Exception) {}
    }

    suspend fun incrementDownloadCount(wallpaperId: String) = withContext(Dispatchers.IO) {
        _wallpapers.value = _wallpapers.value.map { wp ->
            if (wp.id == wallpaperId) wp.copy(downloads = wp.downloads + 1) else wp
        }
        try {
            FirebaseFirestore.getInstance().collection("wallpapers")
                .document(wallpaperId)
                .update("downloads", com.google.firebase.firestore.FieldValue.increment(1))
        } catch (ignored: Exception) {}
    }

    private fun getCuratedSampleWallpapers(): List<Wallpaper> {
        val list = mutableListOf<Wallpaper>()

        // 1. Mahadev Himalayan Dhyana (Exact wallpaper from reference screenshot)
        list.add(
            Wallpaper(
                id = "mahadev_om_01",
                title = "Mahadev Himalayan Dhyana",
                category = WallpaperCategory.MAHADEV.id,
                tags = listOf("mahadev", "shiva", "om", "meditation", "himalayas", "trishul", "bholenath"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = true,
                trending = true,
                daily = true,
                downloads = 5420L,
                views = 18900L,
                localDrawableRes = R.drawable.wp_mahadev_om,
                deity = "Lord Shiva"
            )
        )

        // 2. Temple Sunset with Golden Om (From reference screenshot)
        list.add(
            Wallpaper(
                id = "temple_sunset_01",
                title = "Sunset Temple Golden Om",
                category = WallpaperCategory.SPIRITUAL_OM.id,
                tags = listOf("sunset", "temple", "om", "peace", "river", "ghat", "positivity"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = true,
                trending = true,
                daily = false,
                downloads = 4280L,
                views = 14200L,
                localDrawableRes = R.drawable.wp_temple_sunset,
                deity = "Spiritual Om"
            )
        )

        // 3. Lord Krishna Divine Melody
        list.add(
            Wallpaper(
                id = "krishna_01",
                title = "Shri Krishna Flute & Mor Pankh",
                category = WallpaperCategory.KRISHNA.id,
                tags = listOf("krishna", "bansuri", "vrindavan", "govinda", "radhe", "janmashtami"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = true,
                trending = true,
                daily = false,
                downloads = 3890L,
                views = 12900L,
                localDrawableRes = R.drawable.img_krishna_divine,
                deity = "Lord Krishna"
            )
        )

        // 4. Sacred Lotus with Glowing Om (From reference screenshot)
        list.add(
            Wallpaper(
                id = "lotus_om_01",
                title = "Sacred Golden Lotus Om",
                category = WallpaperCategory.SPIRITUAL_OM.id,
                tags = listOf("lotus", "om", "positivity", "peace", "sunrise", "purity"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = true,
                trending = true,
                daily = false,
                downloads = 3640L,
                views = 11800L,
                localDrawableRes = R.drawable.wp_lotus_om,
                deity = "Spiritual Om"
            )
        )

        // 5. Maryada Purushottam Shri Ram
        list.add(
            Wallpaper(
                id = "ram_01",
                title = "Maryada Purushottam Shri Ram",
                category = WallpaperCategory.SHRI_RAM.id,
                tags = listOf("ram", "ayodhya", "shri ram", "sita", "dharma", "diwali", "bow"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = true,
                trending = true,
                daily = false,
                downloads = 3940L,
                views = 13810L,
                localDrawableRes = R.drawable.thumb_shriram,
                deity = "Lord Ram"
            )
        )

        // 6. Veer Hanuman Divine Aura
        list.add(
            Wallpaper(
                id = "hanuman_01",
                title = "Veer Hanuman Divine Aura",
                category = WallpaperCategory.HANUMAN.id,
                tags = listOf("hanuman", "bajrangbali", "sankatmochan", "devotion", "power", "rambhakt"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = true,
                trending = true,
                daily = false,
                downloads = 4120L,
                views = 15200L,
                localDrawableRes = R.drawable.thumb_hanuman,
                deity = "Lord Hanuman"
            )
        )

        // 7. Vighnaharta Ganesha Blessings
        list.add(
            Wallpaper(
                id = "ganesh_01",
                title = "Vighnaharta Ganesha Blessings",
                category = WallpaperCategory.GANESH.id,
                tags = listOf("ganesh", "ganpati", "vinayaka", "auspicious", "shubh", "modak"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = true,
                trending = true,
                daily = false,
                downloads = 3620L,
                views = 12230L,
                localDrawableRes = R.drawable.thumb_ganesh,
                deity = "Lord Ganesh"
            )
        )

        // 8. Maa Durga Shakti & Grace
        list.add(
            Wallpaper(
                id = "durga_01",
                title = "Maa Durga Shakti & Grace",
                category = WallpaperCategory.DURGA_MAA.id,
                tags = listOf("durga", "navratri", "shakti", "devi", "maa", "bhavani"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = true,
                trending = true,
                daily = false,
                downloads = 3180L,
                views = 10450L,
                localDrawableRes = R.drawable.thumb_durgamaa,
                deity = "Maa Durga"
            )
        )

        // 9. Maha Lakshmi Divine Abundance
        list.add(
            Wallpaper(
                id = "lakshmi_01",
                title = "Maha Lakshmi Divine Abundance",
                category = WallpaperCategory.LAKSHMI_MAA.id,
                tags = listOf("lakshmi", "prosperity", "wealth", "diwali", "lotus", "grace"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = true,
                trending = true,
                daily = false,
                downloads = 3760L,
                views = 11100L,
                localDrawableRes = R.drawable.thumb_lakshmimaa,
                deity = "Goddess Lakshmi"
            )
        )

        // 10. Radha Krishna Vrindavan Prem
        list.add(
            Wallpaper(
                id = "radha_krishna_01",
                title = "Radha Krishna Vrindavan Prem",
                category = WallpaperCategory.RADHA_KRISHNA.id,
                tags = listOf("radha krishna", "radhe", "prem", "eternal love", "vrindavan", "bhakti"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = true,
                trending = true,
                daily = false,
                downloads = 4240L,
                views = 13850L,
                localDrawableRes = R.drawable.thumb_radhakrishna,
                deity = "Radha Krishna"
            )
        )

        // 11. Today's Divine Wallpaper Hero
        list.add(
            Wallpaper(
                id = "today_divine_01",
                title = "Today's Divine Wallpaper",
                category = WallpaperCategory.SPIRITUAL_OM.id,
                tags = listOf("today", "om", "divine", "positivity", "faith", "peace"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = true,
                trending = true,
                daily = true,
                downloads = 6800L,
                views = 24500L,
                localDrawableRes = R.drawable.banner_today_divine,
                deity = "Divine Om"
            )
        )

        // 12. Additional Mahadev Meditation
        list.add(
            Wallpaper(
                id = "mahadev_02",
                title = "Lord Shiva Bholenath Trinetra",
                category = WallpaperCategory.MAHADEV.id,
                tags = listOf("mahadev", "shiva", "trishul", "damru", "bholenath", "rudra"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = false,
                trending = false,
                daily = false,
                downloads = 2100L,
                views = 7100L,
                localDrawableRes = R.drawable.thumb_mahadev,
                deity = "Lord Shiva"
            )
        )

        // 13. Deepawali Sacred Golden Diyas
        list.add(
            Wallpaper(
                id = "festivals_01",
                title = "Deepawali Sacred Golden Diyas",
                category = WallpaperCategory.FESTIVALS.id,
                tags = listOf("diwali", "festivals", "diya", "lights", "celebration", "shubh"),
                imageUrl = "",
                thumbnailUrl = "",
                featured = false,
                trending = true,
                daily = false,
                downloads = 2430L,
                views = 8200L,
                localDrawableRes = R.drawable.wp_temple_sunset,
                deity = "Festivals"
            )
        )

        return list
    }
}
