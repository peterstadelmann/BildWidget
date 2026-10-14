package ch.eshopit.bildwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews

/**
 * Homescreen-Widget: zeigt Bild + Text.
 * Tippen aufs Bild öffnet den Link im Browser,
 * Tippen auf den Text öffnet die App zum Bearbeiten.
 */
class BildWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { updateWidget(context, manager, it) }
    }

    companion object {

        /** Aktualisiert alle Widgets dieser App (z. B. nach dem Speichern). */
        fun updateAll(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, BildWidgetProvider::class.java))
            ids.forEach { updateWidget(context, manager, it) }
        }

        private fun updateWidget(context: Context, manager: AppWidgetManager, widgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            // Text
            views.setTextViewText(R.id.widget_text, WidgetPrefs.getText(context))

            // Bild
            val bitmap = WidgetPrefs.loadImage(context)
            if (bitmap != null) {
                views.setImageViewBitmap(R.id.widget_image, bitmap)
            } else {
                views.setImageViewResource(R.id.widget_image, R.drawable.placeholder)
            }

            // Klick auf den Text -> App öffnen
            val appIntent = Intent(context, MainActivity::class.java)
            val appPending = PendingIntent.getActivity(
                context, 0, appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_text, appPending)

            // Klick auf das Bild -> Link öffnen (oder App, falls kein Link gesetzt)
            val url = WidgetPrefs.getUrl(context)
            val imagePending = if (url.isNotBlank()) {
                val linkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                PendingIntent.getActivity(
                    context, 1, linkIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                appPending
            }
            views.setOnClickPendingIntent(R.id.widget_image, imagePending)

            manager.updateAppWidget(widgetId, views)
        }
    }
}
