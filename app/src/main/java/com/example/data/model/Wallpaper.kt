package com.example.data.model

import com.example.R

data class Wallpaper(
    val id: String,
    val title: String,
    val category: String,
    val tags: List<String> = emptyList(),
    val imageUrl: String = "",
    val thumbnailUrl: String = "",
    val featured: Boolean = false,
    val trending: Boolean = false,
    val daily: Boolean = false,
    val downloads: Long = 0L,
    val views: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
    val localDrawableRes: Int? = null,
    val deity: String? = null
)

enum class WallpaperCategory(
    val id: String,
    val displayName: String,
    val description: String,
    val deity: String,
    val iconDrawableRes: Int? = null
) {
    MAHADEV("mahadev", "Mahadev", "Lord Shiva, Bholenath & Kailash wallpapers", "Lord Shiva", R.drawable.thumb_mahadev),
    SHRI_RAM("ram", "Shri Ram", "Maryada Purushottam & Ayodhya wallpapers", "Lord Ram", R.drawable.thumb_shriram),
    KRISHNA("krishna", "Krishna", "Shri Krishna, flute melodies & Govinda", "Lord Krishna", R.drawable.thumb_krishna),
    HANUMAN("hanuman", "Hanuman", "Bajrangbali, Sankat Mochan & devotion", "Lord Hanuman", R.drawable.thumb_hanuman),
    GANESH("ganesh", "Ganesh", "Vighnaharta, Ganpati Bappa wallpapers", "Lord Ganesh", R.drawable.thumb_ganesh),
    DURGA_MAA("durga", "Durga Maa", "Maa Durga, Navratri & Shakti wallpapers", "Maa Durga", R.drawable.thumb_durgamaa),
    LAKSHMI_MAA("lakshmi", "Lakshmi Maa", "Goddess of wealth, prosperity & grace", "Goddess Lakshmi", R.drawable.thumb_lakshmimaa),
    RADHA_KRISHNA("radha_krishna", "Radha Krishna", "Eternal divine love & Vrindavan", "Radha Krishna", R.drawable.thumb_radhakrishna),
    SPIRITUAL_OM("spiritual", "Spiritual / Om", "Sacred mantras, yantras & temple peace", "Spiritual", R.drawable.ic_om_logo),
    FESTIVALS("festivals", "Festivals", "Diwali, Shivratri, Janmashtami celebrations", "Festivals", R.drawable.wp_temple_sunset)
}
