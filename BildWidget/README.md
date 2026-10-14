# Bild-Widget (Android)

Eine Android-App mit Homescreen-Widget, das ein **Bild** und einen **Text** anzeigt.
**Tippen aufs Bild** öffnet deinen **Link** im Browser, Tippen auf den Text öffnet die App.

## Öffnen & starten
1. Android Studio (Ladybug oder neuer) öffnen → **File › Open** → Ordner `BildWidget` wählen.
2. Gradle-Sync abwarten (Android Studio lädt Gradle 8.9 automatisch).
3. Handy per USB (Entwickleroptionen/USB-Debugging an) oder Emulator wählen → **Run ▶**.

## Installationsdatei (APK) erstellen
**Variante A – Android Studio:** Menü **Build › Build App Bundle(s) / APK(s) › Build APK(s)**.
Die Datei liegt danach in `app/build/outputs/apk/debug/app-debug.apk`.

**Variante B – GitHub (ohne Android Studio):**
1. Auf github.com ein neues Repository anlegen und den Inhalt des Ordners `BildWidget` hochladen
   (auch den versteckten Ordner `.github`).
2. Im Repository auf **Actions** gehen – der Build „APK bauen“ startet automatisch (ca. 3–5 Min.).
3. Den fertigen Lauf öffnen → unten unter **Artifacts** „BildWidget-apk“ herunterladen und entpacken.

**Auf dem Handy installieren:** `app-debug.apk` aufs Handy kopieren, antippen und
„Installation aus unbekannten Quellen“ für die Datei-App erlauben.

## Benutzen
1. App öffnen → **Bild auswählen**, Text und Link eingeben → **Speichern**.
2. **Widget zum Homescreen hinzufügen** tippen – oder Homescreen lange drücken → Widgets → „Bild-Widget“.
3. Änderungen in der App werden sofort in allen Widgets übernommen.

## Dateien
- `MainActivity.kt` – Einstellungsbildschirm (Bild, Text, Link)
- `BildWidgetProvider.kt` – das Widget und die Klick-Aktionen
- `WidgetPrefs.kt` – speichert Text/Link und das verkleinerte Bild
- `res/layout/widget_layout.xml` – Aussehen des Widgets
- `res/xml/bild_widget_info.xml` – Grösse & Einstellungen des Widgets

Mindestversion: Android 9 (API 28). Paketname: `ch.eshopit.bildwidget` (in `app/build.gradle.kts` änderbar).
