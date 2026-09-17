package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.Wallpaper
import com.example.ui.MainViewModel
import com.example.ui.theme.DevotionalNavyBorder
import com.example.ui.theme.DevotionalNavyCard
import com.example.ui.theme.DevotionalNavyDark
import com.example.ui.theme.DevotionalNavySurface
import com.example.ui.theme.DivineGoldPrimary
import com.example.ui.theme.LotusRose
import com.example.ui.theme.SaffronOrange
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.util.WallpaperTarget

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WallpaperDetailScreen(
    wallpaper: Wallpaper,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val isFavorite = favorites.any { it.id == wallpaper.id }

    var showControls by remember { mutableStateOf(true) }
    var showSetWallpaperDialog by remember { mutableStateOf(false) }
    var showInfoSheet by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { showControls = !showControls }
            .testTag("wallpaper_detail_screen")
    ) {
        // High Resolution Image (Full Bleed)
        if (wallpaper.localDrawableRes != null && wallpaper.localDrawableRes != 0) {
            Image(
                painter = painterResource(id = wallpaper.localDrawableRes),
                contentDescription = wallpaper.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else if (wallpaper.imageUrl.isNotBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(wallpaper.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = wallpaper.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Top App Bar Overlay
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut() + slideOutVertically { -it },
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.85f), Color.Transparent)
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable(onClick = onBack),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Info Button
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .clickable { showInfoSheet = true }
                                .testTag("wallpaper_info_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Info",
                                tint = Color.White
                            )
                        }

                        // Favorite Button
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .clickable { viewModel.toggleFavorite(wallpaper) }
                                .testTag("detail_favorite_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) LotusRose else Color.White
                            )
                        }
                    }
                }
            }
        }

        // Bottom Floating Dock Overlay (matching reference screenshot)
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 20.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(32.dp))
                    .testTag("detail_floating_dock"),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF131825).copy(alpha = 0.94f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Download Action
                    DockActionButton(
                        icon = Icons.Outlined.FileDownload,
                        label = "Download",
                        buttonColor = Color(0xFF222B3D),
                        iconTint = Color.White,
                        onClick = { viewModel.downloadWallpaper() },
                        modifier = Modifier.testTag("detail_download_button")
                    )

                    // 2. Set Wallpaper Action (Prominent Blue Circular Button)
                    DockActionButton(
                        icon = Icons.Outlined.Wallpaper,
                        label = "Set Wallpaper",
                        buttonColor = Color(0xFF0284C7),
                        iconTint = Color.White,
                        isPrimary = true,
                        onClick = { showSetWallpaperDialog = true },
                        modifier = Modifier.testTag("detail_set_wallpaper_button")
                    )

                    // 3. Share Action
                    DockActionButton(
                        icon = Icons.Outlined.Share,
                        label = "Share",
                        buttonColor = Color(0xFF222B3D),
                        iconTint = Color.White,
                        onClick = { viewModel.shareWallpaper() },
                        modifier = Modifier.testTag("detail_share_button")
                    )
                }
            }
        }

        // Set Wallpaper Dialog
        if (showSetWallpaperDialog) {
            SetWallpaperDialog(
                onDismiss = { showSetWallpaperDialog = false },
                onSelectTarget = { target ->
                    showSetWallpaperDialog = false
                    viewModel.setDeviceWallpaper(target)
                }
            )
        }

        // Info Dialog
        if (showInfoSheet) {
            WallpaperInfoDialog(
                wallpaper = wallpaper,
                onDismiss = { showInfoSheet = false }
            )
        }
    }
}

@Composable
private fun DockActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    buttonColor: Color,
    iconTint: Color,
    isPrimary: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val size = if (isPrimary) 56.dp else 48.dp
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(buttonColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(if (isPrimary) 26.dp else 22.dp)
            )
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.White,
                fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
private fun SetWallpaperDialog(
    onDismiss: () -> Unit,
    onSelectTarget: (WallpaperTarget) -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DevotionalNavySurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DivineGoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(4.dp)
                .testTag("set_wallpaper_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Set Devotional Wallpaper",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close",
                            tint = TextMutedLight
                        )
                    }
                }

                Text(
                    text = "Select where you would like to apply this sacred wallpaper on your Android device:",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMutedLight)
                )

                // Option 1: Home Screen
                WallpaperTargetOption(
                    title = "Home Screen",
                    subtitle = "Display blessings on your main launcher",
                    icon = Icons.Outlined.PhoneAndroid,
                    onClick = { onSelectTarget(WallpaperTarget.HOME_SCREEN) }
                )

                // Option 2: Lock Screen
                WallpaperTargetOption(
                    title = "Lock Screen",
                    subtitle = "Receive divine darshan every time you wake device",
                    icon = Icons.Outlined.Lock,
                    onClick = { onSelectTarget(WallpaperTarget.LOCK_SCREEN) }
                )

                // Option 3: Both
                WallpaperTargetOption(
                    title = "Both Screens",
                    subtitle = "Home & Lock screen simultaneously",
                    icon = Icons.Outlined.Wallpaper,
                    isRecommended = true,
                    onClick = { onSelectTarget(WallpaperTarget.BOTH) }
                )
            }
        }
    }
}

@Composable
private fun WallpaperTargetOption(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isRecommended: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (isRecommended) DivineGoldPrimary else DevotionalNavyBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .testTag("target_option_$title"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRecommended) DevotionalNavyCard else DevotionalNavyDark
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DivineGoldPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DivineGoldPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    )
                    if (isRecommended) {
                        Box(
                            modifier = Modifier
                                .background(DivineGoldPrimary, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "RECOMMENDED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.Black,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMutedLight,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WallpaperInfoDialog(
    wallpaper: Wallpaper,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DevotionalNavySurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DevotionalNavyBorder, RoundedCornerShape(20.dp))
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Wallpaper Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close",
                            tint = TextMutedLight
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = wallpaper.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DivineGoldPrimary
                        )
                    )
                    Text(
                        text = "Category: ${wallpaper.category.replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextPrimaryLight)
                    )
                    if (wallpaper.deity != null) {
                        Text(
                            text = "Deity: ${wallpaper.deity}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMutedLight)
                        )
                    }
                    Text(
                        text = "Views: ${wallpaper.views}  •  Downloads: ${wallpaper.downloads}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMutedLight)
                    )
                }

                if (wallpaper.tags.isNotEmpty()) {
                    Text(
                        text = "Tags",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryLight
                        )
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        wallpaper.tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DevotionalNavyCard)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "#$tag",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMutedLight
                                    )
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "All wallpapers are curated, high-resolution original devotional artwork honoring Hindu spirituality with verified commercial rights.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMutedLight,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
