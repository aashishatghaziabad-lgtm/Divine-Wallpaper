package com.example.util

import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.data.model.Wallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

enum class WallpaperTarget(val label: String, val flag: Int) {
    HOME_SCREEN("Home Screen", WallpaperManager.FLAG_SYSTEM),
    LOCK_SCREEN("Lock Screen", WallpaperManager.FLAG_LOCK),
    BOTH("Both Screens", WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
}

object WallpaperHelper {

    suspend fun loadBitmap(context: Context, wallpaper: Wallpaper): Bitmap? = withContext(Dispatchers.IO) {
        try {
            if (wallpaper.localDrawableRes != null && wallpaper.localDrawableRes != 0) {
                BitmapFactory.decodeResource(context.resources, wallpaper.localDrawableRes)
            } else if (wallpaper.imageUrl.isNotBlank()) {
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(wallpaper.imageUrl)
                    .allowHardware(false) // Software bitmap for manipulation and WallpaperManager
                    .build()
                val result = (loader.execute(request) as? SuccessResult)?.drawable
                (result as? BitmapDrawable)?.bitmap
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun setDeviceWallpaper(
        context: Context,
        wallpaper: Wallpaper,
        target: WallpaperTarget
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val bitmap = loadBitmap(context, wallpaper)
                ?: return@withContext Result.failure(Exception("Failed to decode wallpaper image"))

            val wallpaperManager = WallpaperManager.getInstance(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, target.flag)
            } else {
                wallpaperManager.setBitmap(bitmap)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveWallpaperToGallery(
        context: Context,
        wallpaper: Wallpaper
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val bitmap = loadBitmap(context, wallpaper)
                ?: return@withContext Result.failure(Exception("Could not load image to save"))

            val filename = "Divine_${wallpaper.category}_${System.currentTimeMillis()}.jpg"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/DivineWallpapers")
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }

                val resolver = context.contentResolver
                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    ?: return@withContext Result.failure(Exception("Unable to create MediaStore entry"))

                resolver.openOutputStream(imageUri).use { stream: OutputStream? ->
                    if (stream == null || !bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        return@withContext Result.failure(Exception("Failed to write image data"))
                    }
                }

                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(imageUri, contentValues, null, null)

                Result.success(imageUri)
            } else {
                val directory = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "DivineWallpapers"
                )
                if (!directory.exists()) directory.mkdirs()

                val file = File(directory, filename)
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                }

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DATA, file.absolutePath)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: Uri.fromFile(file)

                Result.success(uri)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun shareWallpaper(
        context: Context,
        wallpaper: Wallpaper
    ) = withContext(Dispatchers.IO) {
        try {
            val bitmap = loadBitmap(context, wallpaper)
            val shareIntent = Intent(Intent.ACTION_SEND)

            if (bitmap != null) {
                val cachePath = File(context.cacheDir, "wallpapers")
                cachePath.mkdirs()
                val file = File(cachePath, "divine_share_${System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }

                val contentUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )

                shareIntent.type = "image/jpeg"
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                shareIntent.type = "text/plain"
            }

            val shareText = "🕉️ Check out this sacred devotional wallpaper: \"${wallpaper.title}\" on Divine Wallpapers app! ✨"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, wallpaper.title)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)

            val chooser = Intent.createChooser(shareIntent, "Share Divine Wallpaper")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
