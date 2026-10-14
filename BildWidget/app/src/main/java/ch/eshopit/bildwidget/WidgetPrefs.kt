package ch.eshopit.bildwidget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * Speichert Text, Link und Bild für das Widget.
 * Das Bild wird verkleinert in den internen App-Speicher kopiert,
 * damit das Widget es auch nach einem Neustart noch anzeigen kann.
 */
object WidgetPrefs {

    private const val PREFS = "bild_widget"
    private const val KEY_TEXT = "text"
    private const val KEY_URL = "url"
    private const val IMAGE_FILE = "widget_image.jpg"

    /** Max. Kantenlänge – Widgets haben ein Speicherlimit für Bitmaps. */
    private const val MAX_SIZE = 600

    fun getText(ctx: Context): String =
        prefs(ctx).getString(KEY_TEXT, null) ?: ctx.getString(R.string.default_text)

    fun getUrl(ctx: Context): String = prefs(ctx).getString(KEY_URL, "") ?: ""

    fun save(ctx: Context, text: String, url: String) {
        prefs(ctx).edit()
            .putString(KEY_TEXT, text)
            .putString(KEY_URL, url)
            .apply()
    }

    /** Liest ein Bild aus der Galerie, verkleinert es und speichert es intern. */
    fun saveImage(ctx: Context, uri: Uri) {
        val source = ImageDecoder.createSource(ctx.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            val w = info.size.width
            val h = info.size.height
            val scale = minOf(1f, MAX_SIZE.toFloat() / maxOf(w, h))
            decoder.setTargetSize((w * scale).toInt().coerceAtLeast(1), (h * scale).toInt().coerceAtLeast(1))
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }
        FileOutputStream(imageFile(ctx)).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
    }

    fun loadImage(ctx: Context): Bitmap? {
        val file = imageFile(ctx)
        return if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
    }

    private fun imageFile(ctx: Context) = File(ctx.filesDir, IMAGE_FILE)

    private fun prefs(ctx: Context) = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
