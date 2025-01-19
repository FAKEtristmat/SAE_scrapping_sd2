import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;



import java.io.BufferedReader;

import java.io.BufferedWriter;

import java.io.File;

import java.io.FileReader;

import java.io.FileWriter;

import java.net.URL;

import java.time.Duration;



public class BulkImageDownloader {



    public static void main(String[] args) {



        // Configurer les options de Chrome

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-popup-blocking"); // Désactiver le blocage des popups

        options.addArguments("--start-maximized"); // Démarrer en plein écran



        // Chemin vers chromedriver

        System.setProperty("webdriver.chrome.driver",

                "C:\\Users\\nonog\\OneDrive\\Documents\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");



        WebDriver driver = new ChromeDriver(options);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));



        // Définir les chemins des fichiers

        String inputCsvPath = "C:\\Users\\nonog\\OneDrive\\Documents\\scrap fbref\\photo_player.csv";

        String outputCsvPath = "C:\\Users\\nonog\\OneDrive\\Documents\\scrap fbref\\player_images.csv";



        // Assurez-vous que le répertoire de sortie existe

        File outputFile = new File(outputCsvPath);

        File parentDir = outputFile.getParentFile();

        if (!parentDir.exists()) {

            parentDir.mkdirs();

        }



        // Utiliser try-with-resources pour gérer les ressources automatiquement

        try (BufferedReader br = new BufferedReader(new FileReader(inputCsvPath));

             BufferedWriter bw = new BufferedWriter(new FileWriter(outputCsvPath))) {



            // Écrire l'en-tête du fichier CSV de sortie

            bw.write("PlayerName,ImageURL");

            bw.newLine();



            String line;

            while ((line = br.readLine()) != null) {

                String playerName = line.split(",")[0].trim();

                String imageUrl = telechargerImagePourJoueur(driver, wait, playerName);

                if (imageUrl != null) {

                    // Écrire le nom du joueur et l'URL de l'image dans le fichier CSV

                    bw.write("\"" + playerName + "\",\"" + imageUrl + "\"");

                    bw.newLine();

                    System.out.println("URL enregistrée pour " + playerName + " : " + imageUrl);

                } else {

                    // Enregistrer une ligne avec seulement le nom du joueur si l'URL n'est pas trouvée

                    bw.write("\"" + playerName + "\",\"\"");

                    bw.newLine();

                    System.err.println("Image introuvable pour : " + playerName);

                }

            }



            System.out.println("Tous les liens d'images ont été récupérés et enregistrés.");



        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            // Fermer le navigateur après le traitement de tous les joueurs

            driver.quit();

        }

    }



    /**

     * Récupère l'URL de l'image pour un joueur donné.

     *

     * @param driver     Le WebDriver utilisé pour naviguer sur le site.

     * @param wait       L'objet WebDriverWait pour gérer les attentes explicites.

     * @param playerName Le nom du joueur dont l'image est recherchée.

     * @return L'URL de l'image si trouvée, sinon null.

     */

    private static String telechargerImagePourJoueur(WebDriver driver, WebDriverWait wait, String playerName) {

        try {

            driver.get("https://fbref.com/fr/");



            // Attendre que la barre de recherche soit cliquable

            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.name("search")));

            searchBox.clear();

            searchBox.sendKeys(playerName);

            searchBox.submit();



            // Attendre que la page du joueur charge complètement

            wait.until(ExpectedConditions.titleContains("Stats"));



            // Petit debug : afficher le titre de la page

            System.out.println("Titre de la page après recherche: " + driver.getTitle());



            // Localiser l'image du joueur

            String imageUrl = locatePlayerImage(driver, wait, playerName);

            if (imageUrl != null) {

                // Convertir en URL absolue si nécessaire

                imageUrl = getAbsoluteUrl(driver, imageUrl);

                return imageUrl;

            } else {

                System.err.println("Image introuvable pour : " + playerName);

                // Optionnel : capturer une capture d'écran pour débogage

                // captureScreenshot(driver, "C:\\path\\to\\screenshots\\error_" + playerName.replace(" ", "_") + ".png");

                return null;

            }



        } catch (Exception e) {

            System.err.println("Impossible de trouver l'image pour : " + playerName

                    + " - " + e.getMessage());

            // Optionnel : capturer une capture d'écran en cas d'exception

            // captureScreenshot(driver, "C:\\path\\to\\screenshots\\error_" + playerName.replace(" ", "_") + ".png");

            return null;

        }

    }



    /**

     * Localise l'élément image du joueur et récupère son URL.

     *

     * @param driver     Le WebDriver utilisé pour naviguer sur le site.

     * @param wait       L'objet WebDriverWait pour gérer les attentes explicites.

     * @param playerName Le nom du joueur dont l'image est recherchée.

     * @return L'URL de l'image si trouvée, sinon null.

     */

    private static String locatePlayerImage(WebDriver driver, WebDriverWait wait, String playerName) {

        try {

            // Utiliser un sélecteur basé sur l'attribut alt de l'image

            // Exemple : <img alt="Dimitry Bertaud" src="...">

            WebElement playerImage = wait.until(

                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='meta']//img[contains(@alt, '" + playerName + "')]"))

            );

            String imageUrl = playerImage.getAttribute("src");

            System.out.println("URL de l'image récupérée : " + imageUrl); // Debug

            return imageUrl;

        } catch (Exception e) {

            System.err.println("Impossible de localiser l'image : " + e.getMessage());

            return null;

        }

    }



    /**

     * Convertit une URL relative en URL absolue basée sur la page actuelle.

     *

     * @param driver   Le WebDriver utilisé pour naviguer sur le site.

     * @param imageUrl L'URL de l'image à convertir.

     * @return L'URL absolue de l'image.

     */

    private static String getAbsoluteUrl(WebDriver driver, String imageUrl) {

        try {

            URL base = new URL(driver.getCurrentUrl());

            URL absolute = new URL(base, imageUrl);

            return absolute.toString();

        } catch (Exception e) {

            System.err.println("Erreur lors de la création de l'URL absolue : " + e.getMessage());

            return imageUrl; // Retourne l'URL originale si une erreur se produit

        }

    }



    // Optionnel : Méthode pour capturer des captures d'écran en cas d'erreur

    /*

    private static void captureScreenshot(WebDriver driver, String filePath) {

        try {

            // Créer le répertoire des captures d'écran s'il n'existe pas

            File screenshotDir = new File("C:\\path\\to\\screenshots\\");

            if (!screenshotDir.exists()) {

                screenshotDir.mkdirs();

            }



            // Capturer et enregistrer la capture d'écran

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            FileHandler.copy(screenshot, new File(filePath));