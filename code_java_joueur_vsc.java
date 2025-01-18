package srcapping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FbrefDynamicScraper {

    private static final String[] urls = {
    		"https://fbref.com/fr/comps/13/Statistiques-Ligue-1",
    		"https://fbref.com/fr/comps/13/stats/Statistiques-Ligue-1",
    		"https://fbref.com/fr/comps/13/keepers/Statistiques-Ligue-1",
    		"https://fbref.com/fr/comps/13/keepersadv/Statistiques-Ligue-1",
    		"https://fbref.com/fr/comps/13/shooting/Statistiques-Ligue-1",
    		"https://fbref.com/fr/comps/13/passing/Statistiques-Ligue-1",
    		
            "https://fbref.com/fr/comps/13/passing_types/Statistiques-Ligue-1",
            "https://fbref.com/fr/comps/13/gca/Statistiques-Ligue-1",
            "https://fbref.com/fr/comps/13/defense/Statistiques-Ligue-1",
            "https://fbref.com/fr/comps/13/possession/Statistiques-Ligue-1",
            "https://fbref.com/fr/comps/13/playingtime/Statistiques-Ligue-1",
            
    };

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "Lien vers le selenium");

        WebDriver driver = new ChromeDriver();

        try {
            for (String url : urls) {
                // Charger l'URL
                driver.get(url);
                Thread.sleep(5000);

                // Extraire toutes les tables avec la classe `stats_table`
                List<WebElement> tables = driver.findElements(By.cssSelector("table.stats_table"));
                for (WebElement table : tables) {
                    // Identifier le type de table par son attribut `id`
                    String tableId = table.getAttribute("id");

                    if (tableId != null) {
                        if (tableId.contains("misc")) {
                            // Table des statistiques diverses (joueurs)
                            saveTableToCSV(table, "Joueur", driver.getTitle(), tableId);
                        } else if (tableId.contains("squads")) {
                            // Table des équipes
                            saveTableToCSV(table, "Equipe", driver.getTitle(), tableId);
                        } else if (tableId.contains("passing") || tableId.contains("shooting") || tableId.contains("defense")) {
                            // Autres tables spécifiques aux joueurs
                            saveTableToCSV(table, "Joueur", driver.getTitle(), tableId);
                        } else {
                            System.out.println("Table inconnue ignorée : " + tableId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void saveTableToCSV(WebElement table, String type, String title, String tableId) throws IOException {
        // Nettoyer le titre pour nommer le fichier
        String fileName = title.replace(" ", "_").replaceAll("[\\\\/:*?\"<>|]", "") + "_" + tableId + "_" + type + ".csv";

        // Extraire les en-têtes
        List<WebElement> headers = table.findElements(By.cssSelector("thead th"));
        List<String> headerNames = new ArrayList<>();
        for (WebElement header : headers) {
            headerNames.add(header.getText());
        }

        // Extraire les lignes
        List<WebElement> rows = table.findElements(By.cssSelector("tbody tr"));
        List<List<String>> rowData = new ArrayList<>();
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.cssSelector("td"));
            List<String> cellData = new ArrayList<>();
            for (WebElement cell : cells) {
                cellData.add(cell.getText());
            }
            rowData.add(cellData);
        }

        // Sauvegarder dans un fichier CSV
        saveToCSV(fileName, headerNames, rowData);
        System.out.println("Données sauvegardées dans : " + fileName);
    }

    private static void saveToCSV(String fileName, List<String> headers, List<List<String>> data) throws IOException {
        FileWriter csvWriter = new FileWriter(fileName);

        // Écrire les en-têtes
        csvWriter.append(String.join(";", headers));
        csvWriter.append("\n");

        // Écrire les données
        for (List<String> row : data) {
            csvWriter.append(String.join(";", row));
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();
    }
}
