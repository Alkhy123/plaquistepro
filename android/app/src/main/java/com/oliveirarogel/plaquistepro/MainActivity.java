package com.oliveirarogel.plaquistepro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
import android.content.Context;

import androidx.core.content.FileProvider;

import com.getcapacitor.BridgeActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class MainActivity extends BridgeActivity {
    
    private static final String TAG = "PlaquistePro";
    private WebView webView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Ajouter l'interface JavaScript
        getBridge().getWebView().post(() -> {
            webView = getBridge().getWebView();
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new DocumentBridge(), "DocumentBridge");
            Log.d(TAG, "DocumentBridge initialisé");
        });
    }
    
    /**
     * Pont JavaScript pour la gestion des documents
     */
    public class DocumentBridge {
        
        /**
         * Vérifie si le pont est disponible
         */
        @JavascriptInterface
        public boolean isAvailable() {
            Log.d(TAG, "isAvailable appelé");
            return true;
        }
        
        /**
         * Affiche un message Toast
         */
        @JavascriptInterface
        public void showToast(String message) {
            runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
        }
        
        /**
         * Sauvegarde et partage un document HTML
         */
        @JavascriptInterface
        public boolean shareHtmlDocument(String htmlContent, String fileName) {
            Log.d(TAG, "shareHtmlDocument: " + fileName);
            
            try {
                File file = saveHtmlToFile(htmlContent, fileName);
                if (file == null) return false;
                
                shareFile(file, "text/html", fileName);
                return true;
                
            } catch (Exception e) {
                Log.e(TAG, "Erreur shareHtmlDocument", e);
                showToastOnUI("Erreur: " + e.getMessage());
                return false;
            }
        }
        
        /**
         * Sauvegarde et partage un document PDF (base64)
         */
        @JavascriptInterface
        public boolean sharePdfDocument(String base64Data, String fileName) {
            Log.d(TAG, "sharePdfDocument: " + fileName);
            
            try {
                File file = savePdfToFile(base64Data, fileName);
                if (file == null) return false;
                
                shareFile(file, "application/pdf", fileName);
                return true;
                
            } catch (Exception e) {
                Log.e(TAG, "Erreur sharePdfDocument", e);
                showToastOnUI("Erreur: " + e.getMessage());
                return false;
            }
        }
        
        /**
         * Sauvegarde un document PDF dans les téléchargements
         */
        @JavascriptInterface
        public String savePdfToDownloads(String base64Data, String fileName) {
            Log.d(TAG, "savePdfToDownloads: " + fileName);
            
            try {
                // Extraire les données base64 pures
                String pureBase64 = base64Data;
                if (base64Data.contains(",")) {
                    pureBase64 = base64Data.substring(base64Data.indexOf(",") + 1);
                }
                
                byte[] pdfBytes = Base64.decode(pureBase64, Base64.DEFAULT);
                
                // Dossier Téléchargements public
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File plaquisteDir = new File(downloadsDir, "PlaquistePro");
                if (!plaquisteDir.exists()) {
                    plaquisteDir.mkdirs();
                }
                
                // Nom de fichier sécurisé
                String safeFileName = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
                if (!safeFileName.endsWith(".pdf")) {
                    safeFileName += ".pdf";
                }
                
                // Éviter les doublons
                File pdfFile = new File(plaquisteDir, safeFileName);
                int counter = 1;
                String baseName = safeFileName.replace(".pdf", "");
                while (pdfFile.exists()) {
                    pdfFile = new File(plaquisteDir, baseName + "_" + counter + ".pdf");
                    counter++;
                }
                
                FileOutputStream fos = new FileOutputStream(pdfFile);
                fos.write(pdfBytes);
                fos.close();
                
                String path = pdfFile.getAbsolutePath();
                Log.d(TAG, "PDF sauvegardé: " + path);
                showToastOnUI("✅ PDF sauvegardé dans Téléchargements/PlaquistePro");
                
                return path;
                
            } catch (Exception e) {
                Log.e(TAG, "Erreur savePdfToDownloads", e);
                showToastOnUI("Erreur: " + e.getMessage());
                return null;
            }
        }
        
        /**
         * Imprime un document HTML via le système d'impression Android
         */
        @JavascriptInterface
        public void printHtmlDocument(String htmlContent, String documentName) {
            Log.d(TAG, "printHtmlDocument: " + documentName);
            
            runOnUiThread(() -> {
                try {
                    // Créer une WebView pour l'impression
                    WebView printWebView = new WebView(MainActivity.this);
                    printWebView.getSettings().setJavaScriptEnabled(true);
                    
                    printWebView.setWebViewClient(new android.webkit.WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            // Créer le job d'impression
                            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                            PrintDocumentAdapter adapter = printWebView.createPrintDocumentAdapter(documentName);
                            
                            String jobName = "PlaquistePro - " + documentName;
                            printManager.print(jobName, adapter, new PrintAttributes.Builder().build());
                        }
                    });
                    
                    // Charger le HTML
                    printWebView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
                    
                } catch (Exception e) {
                    Log.e(TAG, "Erreur printHtmlDocument", e);
                    showToastOnUI("Erreur impression: " + e.getMessage());
                }
            });
        }
        
        /**
         * Ouvre un PDF avec une application externe
         */
        @JavascriptInterface
        public boolean openPdfExternal(String base64Data, String fileName) {
            Log.d(TAG, "openPdfExternal: " + fileName);
            
            try {
                File file = savePdfToFile(base64Data, fileName);
                if (file == null) return false;
                
                Uri fileUri = FileProvider.getUriForFile(
                    MainActivity.this,
                    getPackageName() + ".fileprovider",
                    file
                );
                
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(fileUri, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                
                runOnUiThread(() -> {
                    try {
                        startActivity(Intent.createChooser(intent, "Ouvrir avec"));
                    } catch (Exception e) {
                        showToastOnUI("Aucune application PDF trouvée");
                    }
                });
                
                return true;
                
            } catch (Exception e) {
                Log.e(TAG, "Erreur openPdfExternal", e);
                showToastOnUI("Erreur: " + e.getMessage());
                return false;
            }
        }
        
        // ==================
        // Méthodes utilitaires
        // ==================
        
        private File saveHtmlToFile(String htmlContent, String fileName) throws Exception {
            File cacheDir = new File(getCacheDir(), "documents");
            if (!cacheDir.exists()) cacheDir.mkdirs();
            
            String safeFileName = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
            if (!safeFileName.endsWith(".html")) {
                safeFileName = safeFileName.replace(".pdf", "") + ".html";
            }
            
            File file = new File(cacheDir, safeFileName);
            FileWriter writer = new FileWriter(file);
            writer.write(htmlContent);
            writer.close();
            
            return file;
        }
        
        private File savePdfToFile(String base64Data, String fileName) throws Exception {
            String pureBase64 = base64Data;
            if (base64Data.contains(",")) {
                pureBase64 = base64Data.substring(base64Data.indexOf(",") + 1);
            }
            
            byte[] pdfBytes = Base64.decode(pureBase64, Base64.DEFAULT);
            
            File cacheDir = new File(getCacheDir(), "documents");
            if (!cacheDir.exists()) cacheDir.mkdirs();
            
            String safeFileName = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
            if (!safeFileName.endsWith(".pdf")) {
                safeFileName += ".pdf";
            }
            
            File file = new File(cacheDir, safeFileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(pdfBytes);
            fos.close();
            
            return file;
        }
        
        private void shareFile(File file, String mimeType, String title) {
            Uri fileUri = FileProvider.getUriForFile(
                MainActivity.this,
                getPackageName() + ".fileprovider",
                file
            );
            
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType(mimeType);
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            
            runOnUiThread(() -> {
                startActivity(Intent.createChooser(shareIntent, "Partager"));
            });
        }
        
        private void showToastOnUI(String message) {
            runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show());
        }
    }
}
