package ch.eshopit.bildwidget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

/** Einstellungs-Bildschirm: Bild wählen, Text und Link eingeben, speichern. */
class MainActivity : AppCompatActivity() {

    private lateinit var preview: ImageView
    private lateinit var textInput: EditText
    private lateinit var urlInput: EditText

    // Bildauswahl über die System-Fotoauswahl (keine Berechtigung nötig)
    private val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            try {
                WidgetPrefs.saveImage(this, uri)
                showPreview()
            } catch (e: Exception) {
                Toast.makeText(this, R.string.image_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preview = findViewById(R.id.preview_image)
        textInput = findViewById(R.id.input_text)
        urlInput = findViewById(R.id.input_url)

        textInput.setText(WidgetPrefs.getText(this))
        urlInput.setText(WidgetPrefs.getUrl(this))
        showPreview()

        findViewById<Button>(R.id.button_pick).setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        findViewById<Button>(R.id.button_save).setOnClickListener { save() }

        findViewById<Button>(R.id.button_add_widget).setOnClickListener { requestPinWidget() }
    }

    private fun save() {
        val text = textInput.text.toString().trim()
        var url = urlInput.text.toString().trim()

        if (url.isNotEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://$url"
            }
            if (!Patterns.WEB_URL.matcher(url).matches()) {
                urlInput.error = getString(R.string.url_invalid)
                return
            }
            urlInput.setText(url)
        }

        WidgetPrefs.save(this, text, url)
        BildWidgetProvider.updateAll(this)
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show()
    }

    /** Fragt den Launcher, das Widget direkt auf den Homescreen zu legen. */
    private fun requestPinWidget() {
        val manager = AppWidgetManager.getInstance(this)
        if (manager.isRequestPinAppWidgetSupported) {
            manager.requestPinAppWidget(ComponentName(this, BildWidgetProvider::class.java), null, null)
        } else {
            Toast.makeText(this, R.string.pin_not_supported, Toast.LENGTH_LONG).show()
        }
    }

    private fun showPreview() {
        val bitmap = WidgetPrefs.loadImage(this)
        if (bitmap != null) preview.setImageBitmap(bitmap)
        else preview.setImageResource(R.drawable.placeholder)
    }
}
