# 📱 Guide de Migration Android - PlaquistePro

## Prérequis

### 1. Installer Node.js
- Télécharger sur https://nodejs.org (version LTS)
- Vérifier : `node -v` et `npm -v`

### 2. Installer Android Studio
- Télécharger sur https://developer.android.com/studio
- Pendant l'installation, cocher :
  - Android SDK
  - Android SDK Platform
  - Android Virtual Device (émulateur)
- Après installation, ouvrir Android Studio > SDK Manager > SDK Tools :
  - Cocher "Android SDK Command-line Tools"
  - Cocher "Android SDK Build-Tools"

### 3. Configurer les variables d'environnement
**Windows :**
```
ANDROID_HOME = C:\Users\VOTRE_NOM\AppData\Local\Android\Sdk
Path += %ANDROID_HOME%\platform-tools
```

**Mac/Linux :**
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

---

## Étape 1 : Créer le projet

```bash
# Créer un dossier pour le projet
mkdir PlaquistePro-Android
cd PlaquistePro-Android

# Initialiser npm
npm init -y

# Installer Capacitor
npm install @capacitor/core @capacitor/cli @capacitor/android

# Initialiser Capacitor
npx cap init PlaquistePro com.oliveirarogel.plaquistepro --web-dir=www
```

---

## Étape 2 : Copier les fichiers web

```bash
# Créer le dossier www
mkdir www

# Copier tes fichiers dedans :
# - index.html
# - manifest.json
# - sw.js
# - icon192.png
# - icon512.png
```

---

## Étape 3 : Ajouter la plateforme Android

```bash
# Ajouter Android
npx cap add android

# Synchroniser les fichiers
npx cap sync android
```

---

## Étape 4 : Configurer l'application

### Modifier `capacitor.config.json` :
```json
{
  "appId": "com.oliveirarogel.plaquistepro",
  "appName": "PlaquistePro",
  "webDir": "www",
  "server": {
    "androidScheme": "https"
  },
  "plugins": {
    "SplashScreen": {
      "launchShowDuration": 2000,
      "backgroundColor": "#1E3A5F",
      "showSpinner": false
    }
  }
}
```

### Modifier l'icône de l'app
Remplacer les fichiers dans :
- `android/app/src/main/res/mipmap-hdpi/ic_launcher.png` (72x72)
- `android/app/src/main/res/mipmap-mdpi/ic_launcher.png` (48x48)
- `android/app/src/main/res/mipmap-xhdpi/ic_launcher.png` (96x96)
- `android/app/src/main/res/mipmap-xxhdpi/ic_launcher.png` (144x144)
- `android/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png` (192x192)

---

## Étape 5 : Tester sur émulateur ou téléphone

### Option A : Émulateur
```bash
# Ouvrir dans Android Studio
npx cap open android

# Dans Android Studio : Run > Run 'app'
```

### Option B : Téléphone connecté en USB
1. Activer le "Mode développeur" sur ton téléphone :
   - Paramètres > À propos > Numéro de build (taper 7 fois)
2. Activer "Débogage USB" dans les options développeur
3. Connecter le téléphone en USB
4. ```bash
   npx cap run android
   ```

---

## Étape 6 : Générer l'APK (pour distribuer)

### APK de debug (pour tester)
```bash
cd android
./gradlew assembleDebug
```
L'APK sera dans : `android/app/build/outputs/apk/debug/app-debug.apk`

### APK de release (pour Play Store)
```bash
# Générer une clé de signature
keytool -genkey -v -keystore plaquistepro-release.keystore -alias plaquistepro -keyalg RSA -keysize 2048 -validity 10000

# Créer le fichier android/key.properties
storePassword=VOTRE_MOT_DE_PASSE
keyPassword=VOTRE_MOT_DE_PASSE
keyAlias=plaquistepro
storeFile=../plaquistepro-release.keystore

# Générer l'APK release
cd android
./gradlew assembleRelease
```
L'APK sera dans : `android/app/build/outputs/apk/release/app-release.apk`

---

## Étape 7 : Ajouter des fonctionnalités natives (optionnel)

### Notifications Push
```bash
npm install @capacitor/push-notifications
npx cap sync
```

### Caméra (améliorer la prise de photo)
```bash
npm install @capacitor/camera
npx cap sync
```

### Stockage sécurisé
```bash
npm install @capacitor/preferences
npx cap sync
```

---

## Commandes utiles

| Commande | Description |
|----------|-------------|
| `npx cap sync` | Synchroniser après modification des fichiers web |
| `npx cap open android` | Ouvrir dans Android Studio |
| `npx cap run android` | Lancer sur appareil connecté |
| `npx cap build android` | Compiler le projet |

---

## Résolution de problèmes

### "SDK location not found"
Créer un fichier `android/local.properties` :
```
sdk.dir=C:\\Users\\VOTRE_NOM\\AppData\\Local\\Android\\Sdk
```

### "JAVA_HOME not set"
Installer JDK 17 et configurer JAVA_HOME :
```
JAVA_HOME = C:\Program Files\Java\jdk-17
```

### L'app ne se charge pas
Vérifier que tous les fichiers sont dans le dossier `www/` puis :
```bash
npx cap sync android
```

---

## Structure finale du projet

```
PlaquistePro-Android/
├── www/
│   ├── index.html
│   ├── manifest.json
│   ├── sw.js
│   ├── icon192.png
│   └── icon512.png
├── android/
│   └── (projet Android Studio)
├── node_modules/
├── capacitor.config.json
├── package.json
└── package-lock.json
```

---

## Publication sur Google Play Store

1. Créer un compte développeur Google Play (25$ une fois)
   https://play.google.com/console

2. Générer un AAB (Android App Bundle) au lieu d'APK :
   ```bash
   cd android
   ./gradlew bundleRelease
   ```

3. Uploader le fichier `.aab` sur la Play Console

4. Remplir les informations :
   - Description
   - Captures d'écran
   - Icône
   - Politique de confidentialité

---

## Support

En cas de problème, vérifier :
1. Version de Node.js (>= 16)
2. Version de Java/JDK (17 recommandé)
3. Android SDK installé correctement
4. Variables d'environnement configurées
