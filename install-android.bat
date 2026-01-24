@echo off
echo ============================================
echo   PlaquistePro - Installation Android
echo ============================================
echo.

REM Vérifier Node.js
node -v >nul 2>&1
if errorlevel 1 (
    echo [ERREUR] Node.js n'est pas installe.
    echo Telechargez-le sur https://nodejs.org
    pause
    exit /b 1
)
echo [OK] Node.js installe

REM Créer le dossier www s'il n'existe pas
if not exist "www" mkdir www

REM Copier les fichiers web
echo.
echo Copie des fichiers web...
if exist "index.html" copy "index.html" "www\" >nul
if exist "manifest.json" copy "manifest.json" "www\" >nul
if exist "sw.js" copy "sw.js" "www\" >nul
if exist "icon192.png" copy "icon192.png" "www\" >nul
if exist "icon512.png" copy "icon512.png" "www\" >nul
echo [OK] Fichiers copies dans www/

REM Installer les dépendances
echo.
echo Installation des dependances npm...
call npm install

REM Initialiser Capacitor si pas déjà fait
if not exist "android" (
    echo.
    echo Ajout de la plateforme Android...
    call npx cap add android
)

REM Synchroniser
echo.
echo Synchronisation...
call npx cap sync android

echo.
echo ============================================
echo   Installation terminee !
echo ============================================
echo.
echo Prochaines etapes :
echo   1. npx cap open android   (ouvrir dans Android Studio)
echo   2. Run ^> Run 'app'       (lancer sur emulateur/telephone)
echo.
echo Pour generer un APK :
echo   cd android
echo   gradlew assembleDebug
echo.
pause
