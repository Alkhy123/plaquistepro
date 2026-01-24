#!/bin/bash

echo "============================================"
echo "  PlaquistePro - Installation Android"
echo "============================================"
echo ""

# Vérifier Node.js
if ! command -v node &> /dev/null; then
    echo "[ERREUR] Node.js n'est pas installé."
    echo "Installez-le avec: brew install node (Mac) ou apt install nodejs (Linux)"
    exit 1
fi
echo "[OK] Node.js installé ($(node -v))"

# Créer le dossier www s'il n'existe pas
mkdir -p www

# Copier les fichiers web
echo ""
echo "Copie des fichiers web..."
[ -f "index.html" ] && cp index.html www/
[ -f "manifest.json" ] && cp manifest.json www/
[ -f "sw.js" ] && cp sw.js www/
[ -f "icon192.png" ] && cp icon192.png www/
[ -f "icon512.png" ] && cp icon512.png www/
echo "[OK] Fichiers copiés dans www/"

# Installer les dépendances
echo ""
echo "Installation des dépendances npm..."
npm install

# Initialiser Capacitor si pas déjà fait
if [ ! -d "android" ]; then
    echo ""
    echo "Ajout de la plateforme Android..."
    npx cap add android
fi

# Synchroniser
echo ""
echo "Synchronisation..."
npx cap sync android

echo ""
echo "============================================"
echo "  Installation terminée !"
echo "============================================"
echo ""
echo "Prochaines étapes :"
echo "  1. npx cap open android   (ouvrir dans Android Studio)"
echo "  2. Run > Run 'app'        (lancer sur émulateur/téléphone)"
echo ""
echo "Pour générer un APK :"
echo "  cd android"
echo "  ./gradlew assembleDebug"
echo ""
