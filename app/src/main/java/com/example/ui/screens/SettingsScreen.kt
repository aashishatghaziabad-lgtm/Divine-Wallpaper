package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.DivineCreamBg
import com.example.ui.theme.DivineCreamBorder
import com.example.ui.theme.DivineCreamCard
import com.example.ui.theme.DivineCreamSurface
import com.example.ui.theme.DivineGoldPrimary
import com.example.ui.theme.DivineTextDark
import com.example.ui.theme.DivineTextMuted
import com.example.ui.theme.SaffronOrange

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var notificationsEnabled by remember { mutableStateOf(true) }
    var selectedQuality by remember { mutableStateOf("Ultra High (Original)") }
    var showQualityDialog by remember { mutableStateOf(false) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DivineCreamBg)
            .testTag("settings_screen_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = "Settings & Preferences",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = DivineTextDark
                    )
                )
                Text(
                    text = "Personalize your divine wallpaper darshan",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DivineTextMuted
                    )
                )
            }
        }

        // Section: General Preferences
        item {
            Text(
                text = "PREFERENCES",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = SaffronOrange,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DivineCreamCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DivineCreamBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    // Notifications Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = null,
                                tint = SaffronOrange,
                                modifier = Modifier.size(22.dp)
                            )
                            Column {
                                Text(
                                    text = "Daily Darshan Notifications",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = DivineTextDark,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = "Receive sacred wallpaper of the day alerts",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = DivineTextMuted,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SaffronOrange,
                                uncheckedThumbColor = DivineTextMuted,
                                uncheckedTrackColor = DivineCreamBorder
                            ),
                            modifier = Modifier.testTag("notifications_switch")
                        )
                    }

                    // Divider
                    Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(DivineCreamBorder))

                    // Theme Aesthetic Info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Palette,
                                contentDescription = null,
                                tint = SaffronOrange,
                                modifier = Modifier.size(22.dp)
                            )
                            Column {
                                Text(
                                    text = "Divine Warm Aesthetic",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = DivineTextDark,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = "Sacred ivory canvas with saffron accents",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = DivineTextMuted,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Text(
                            text = "Active",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SaffronOrange,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    // Divider
                    Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(DivineCreamBorder))

                    // Download Quality
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showQualityDialog = true }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Download,
                                contentDescription = null,
                                tint = SaffronOrange,
                                modifier = Modifier.size(22.dp)
                            )
                            Column {
                                Text(
                                    text = "Download Quality",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = DivineTextDark,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = selectedQuality,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = DivineTextMuted,
                                        fontSize = 11.sp
                                    )
                                )
                            }
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

        // Section: About & Community
        item {
            Text(
                text = "COMMUNITY & LEGAL",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = SaffronOrange,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DivineCreamCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DivineCreamBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    SettingsRow(
                        icon = Icons.Outlined.Share,
                        title = "Share Divine Wallpapers",
                        subtitle = "Spread devotional blessings with family & friends",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Experience peace, faith, and positivity with Divine Wallpapers! Download sacred Hindu wallpapers for Mahadev, Krishna, Shri Ram, and Hanuman: https://play.google.com/store/apps/details?id=com.aistudio.divinewallpapers"
                                )
                            }
                            context.startActivity(Intent.createChooser(intent, "Share Divine Wallpapers"))
                        }
                    )

                    Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(DivineCreamBorder))

                    SettingsRow(
                        icon = Icons.Outlined.RateReview,
                        title = "Rate on Google Play",
                        subtitle = "Support independent devotional artists with 5 stars",
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.aistudio.divinewallpapers")
                            )
                            context.startActivity(intent)
                        }
                    )

                    Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(DivineCreamBorder))

                    SettingsRow(
                        icon = Icons.Outlined.PrivacyTip,
                        title = "Privacy Policy",
                        subtitle = "Zero tracking • No account required • Offline first",
                        onClick = { showPrivacyPolicyDialog = true }
                    )

                    Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(DivineCreamBorder))

                    SettingsRow(
                        icon = Icons.Outlined.Description,
                        title = "Terms of Service",
                        subtitle = "Curated devotional content & usage rights",
                        onClick = { showTermsDialog = true }
                    )

                    Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(DivineCreamBorder))

                    SettingsRow(
                        icon = Icons.Outlined.Info,
                        title = "About Divine Wallpapers",
                        subtitle = "Version 1.0 (Android 16 / API 36+ Compliant)",
                        onClick = { showAboutDialog = true }
                    )
                }
            }
        }

        // Sacred Blessing Note
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "ॐ सर्वे भवन्तु सुखिनः सर्वे सन्तु निरामयाः",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SaffronOrange,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "May all beings be happy • May all beings be peaceful",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DivineTextMuted,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }

    // Quality Selection Dialog
    if (showQualityDialog) {
        Dialog(onDismissRequest = { showQualityDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DivineCreamSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DivineCreamBorder),
                modifier = Modifier.padding(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Select Download Quality",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DivineTextDark
                        )
                    )

                    listOf("Ultra High (Original)", "High Quality (1080p)", "Data Saver (720p)").forEach { quality ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedQuality = quality
                                    showQualityDialog = false
                                }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            RadioButton(
                                selected = selectedQuality == quality,
                                onClick = {
                                    selectedQuality = quality
                                    showQualityDialog = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = SaffronOrange,
                                    unselectedColor = DivineTextMuted
                                )
                            )
                            Text(
                                text = quality,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = DivineTextDark
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    // Privacy Policy Dialog
    if (showPrivacyPolicyDialog) {
        Dialog(onDismissRequest = { showPrivacyPolicyDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DivineCreamSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DivineCreamBorder),
                modifier = Modifier.padding(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Privacy Policy",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SaffronOrange
                            )
                        )
                        IconButton(onClick = { showPrivacyPolicyDialog = false }, modifier = Modifier.size(24.dp)) {
                            Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close", tint = DivineTextMuted)
                        }
                    }

                    Text(
                        text = "Divine Wallpapers is committed to protecting your privacy:\n\n" +
                                "• No Account Required: You can freely browse, preview, and download wallpapers without creating an account or providing an email address.\n" +
                                "• Zero Personal Data Collection: We do not collect or sell your name, phone number, location, contacts, or personal identifiers.\n" +
                                "• Local Storage: Favorites and settings are stored locally on your device in secure Room database and never uploaded to remote servers without your permission.\n" +
                                "• Android Media Permissions: Wallpapers are saved using modern Android MediaStore APIs, requiring zero broad storage permissions on Android 10+ and Android 16.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DivineTextDark,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }
    }

    // Terms Dialog
    if (showTermsDialog) {
        Dialog(onDismissRequest = { showTermsDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DivineCreamSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DivineCreamBorder),
                modifier = Modifier.padding(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Terms & Conditions",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SaffronOrange
                            )
                        )
                        IconButton(onClick = { showTermsDialog = false }, modifier = Modifier.size(24.dp)) {
                            Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close", tint = DivineTextMuted)
                        }
                    }

                    Text(
                        text = "1. Content: All devotional wallpapers in Divine Wallpapers are curated for personal devotional and spiritual display.\n" +
                                "2. Commercial Use: Wallpapers are for personal wallpaper display. Redistribution or commercial resale is prohibited.\n" +
                                "3. Reverence: Content is designed with respect and reverence toward Hindu religious beliefs.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DivineTextDark,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }
    }

    // About Dialog
    if (showAboutDialog) {
        Dialog(onDismissRequest = { showAboutDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DivineCreamSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SaffronOrange.copy(alpha = 0.5f)),
                modifier = Modifier.padding(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(22.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "🕉️", fontSize = 42.sp)
                    Text(
                        text = "Divine Wallpapers",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = DivineTextDark
                        )
                    )
                    Text(
                        text = "Version 1.0 (Production Release)",
                        style = MaterialTheme.typography.labelMedium.copy(color = SaffronOrange)
                    )
                    Text(
                        text = "Targeting Android 16 (API 36+) for Google Play Store compliance in 2026. Made with devotion and modern Jetpack Compose.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DivineTextMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SaffronOrange,
                modifier = Modifier.size(22.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = DivineTextDark,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DivineTextMuted,
                        fontSize = 11.sp
                    )
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = DivineTextMuted,
            modifier = Modifier.size(14.dp)
        )
    }
}
