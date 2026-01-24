# CLAUDE.md - Instructions pour Claude Code

## 📱 À propos du projet

**PlaquistePro** est une application Android hybride (Capacitor) pour les plaquistes professionnels.

### Stack technique
- **Frontend** : HTML/CSS/JavaScript vanilla (PWA)
- **Mobile** : Capacitor (WebView Android)
- **Base de données** : IndexedDB (locale)
- **IA** : API Claude via Cloudflare Worker

### Structure du projet

```
PlaquistePro-Android/
├── android/                    # Projet Android natif (Capacitor)
│   └── app/src/main/
│       ├── assets/public/      # ← Fichiers web de l'application
│       │   ├── js/             # Scripts JavaScript
│       │   ├── css/            # Styles CSS
│       │   └── index.html      # Page principale
│       ├── java/.../           # Code Java natif
│       └── res/                # Ressources Android
├── www/                        # Copie des fichiers web (source)
├── package.json
└── capacitor.config.json
```

---

## 🔧 Règles de développement

### JavaScript
- Utiliser JavaScript ES6+ vanilla (pas de framework)
- Fonctions async/await pour les opérations asynchrones
- Commenter les fonctions principales
- Préfixer les versions dans les commentaires de fichiers

### Fichiers importants
- `js/database.js` - Gestion IndexedDB (ne pas modifier la structure sans migration)
- `js/config.js` - Configuration (URL Worker IA intégrée)
- `js/documentManager.js` - Module natif pour PDF/partage
- `js/ia.js` - Analyse IA des plans
- `android/MainActivity.java` - Pont JavaScript ↔ Android natif

### Conventions de nommage
- camelCase pour les fonctions et variables
- UPPER_CASE pour les constantes
- Préfixe descriptif pour les fonctions (load, save, render, handle, etc.)

---

## 📐 Règles métier (Plâtrerie)

### Calculs de métrés
- **Mètres linéaires (ml)** = Périmètre (somme des côtés)
  - Pièce 4m × 3m → 4 + 3 + 4 + 3 = 14 ml
- **Mètres carrés (m²)** = Surface (longueur × largeur)
  - Pièce 4m × 3m → 4 × 3 = 12 m²

### Types d'ouvrages
- **Cloisons** : mesurées en linéaire (ml) × hauteur
- **Doublages** : mesurés en linéaire (ml) × hauteur  
- **Plafonds** : mesurés en surface (m²)

### Coefficients standards
- Perte matériaux : 10% (coef 1.10)
- Entraxe montants : 60cm par défaut
- Hauteur standard : 2.50m

---

## 🚀 Commandes utiles

### Développement
```bash
# Synchroniser les fichiers web vers Android
npx cap sync android

# Ouvrir dans Android Studio
npx cap open android
```

### Git
```bash
# Voir les modifications
git status

# Commit des changements
git add .
git commit -m "Description des changements"

# Pousser vers GitHub
git push
```

---

## ⚠️ Points d'attention

1. **Ne jamais modifier** directement les fichiers dans `android/app/src/main/assets/public/` 
   - Modifier dans `www/` puis faire `npx cap sync android`

2. **Base de données IndexedDB**
   - Version actuelle : 4
   - Toujours incrémenter DB_VERSION si modification du schéma

3. **Worker IA Cloudflare**
   - URL : `https://wild-wave-3a56.alkhyomgame.workers.dev`
   - Intégrée dans config.js (DEFAULT_WORKER_URL)

4. **Module natif DocumentBridge**
   - Défini dans MainActivity.java
   - Accessible via `window.DocumentBridge` en JavaScript

---

## 📝 Changelog récent

### v6.1.0
- Amélioration calculs IA (ml vs m²)
- URL Worker intégrée

### v6.0.0
- Module DocumentBridge natif Android
- Partage/impression PDF fonctionnels
- DocumentManager.js réutilisable

---

## 🎯 Fonctionnalités principales

1. **Gestion des chantiers** - CRUD complet
2. **Feuilles d'heures** - Par semaine/employé
3. **Quantitatif matériaux** - Calculs automatiques
4. **Analyse IA** - Détection ouvrages sur plans
5. **Génération PDF** - Export et partage
6. **Dossiers** - Gestion fichiers par chantier
7. **Calculateurs** - Outils métier (plaques, rails, etc.)
8. **Géolocalisation** - Position des chantiers
