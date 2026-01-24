# 📱 PlaquistePro - Version Android

## Fichiers fournis

```
├── index.html              ← Application web principale
├── manifest.json           ← Manifest PWA
├── sw.js                   ← Service Worker
├── icon192.png             ← Icône 192x192
├── icon512.png             ← Icône 512x512
├── capacitor.config.json   ← Configuration Capacitor
├── package.json            ← Dépendances npm
├── install-android.bat     ← Script installation (Windows)
├── install-android.sh      ← Script installation (Mac/Linux)
├── GUIDE_ANDROID.md        ← Guide complet détaillé
└── android-resources/
    ├── colors.xml          ← Couleurs personnalisées
    └── styles.xml          ← Thème de l'app
```

## Installation rapide

### Windows
1. Installer Node.js : https://nodejs.org
2. Installer Android Studio : https://developer.android.com/studio
3. Mettre tous les fichiers dans un dossier
4. Double-cliquer sur `install-android.bat`

### Mac/Linux
```bash
# Installer Node.js et Android Studio d'abord
chmod +x install-android.sh
./install-android.sh
```

## Lancer l'application

```bash
# Ouvrir dans Android Studio
npx cap open android

# Ou lancer directement sur téléphone connecté
npx cap run android
```

## Générer un APK

```bash
cd android
./gradlew assembleDebug
# APK dans : android/app/build/outputs/apk/debug/app-debug.apk
```

## Support

Voir `GUIDE_ANDROID.md` pour le guide complet avec résolution de problèmes.
