# SAE_scrapping_sd2
Travail réalisé par `Tristan` et `Arnaud` en SD2 
- [Site fbref ](https://fbref.com/fr/)
- [Lien Power BI](https://app.powerbi.com/MobileRedirect.html?ctid=a51a6642-5911-4306-a13c-f4731ab9c63f&Context=share-report&reportPage=a804966662e089394710&bookmarkGuid=007e0217-3f78-4324-a714-e4628da2e722&action=OpenReport&groupObjectId=589558de-5f05-424b-8637-806e696d3591&reportObjectId=c49f1f60-56b0-46dd-9ccb-a6e99ceeeb14&pbi_source=mobile_ios)

## Description du projet
Le projet consiste à collecter des informations détaillées sur les joueurs de la Ligue 1 à partir du site FBref. Ces informations comprennent :


- Les statistiques de tirs (nombre total, pourcentage de tirs cadrés).
- Les statistiques de passes (courtes, longues, taux de réussite).
- Les performances sur phases arrêtées.

Nous avons utilisé le langage de programmation Java pour réaliser le web scraping des données. Dans un premier temps, nous avons extrait des informations textuelles sur les joueurs et les performances de la Ligue 1. Ensuite, nous avons procédé à un scraping des photos des joueurs.
Après avoir collecté toutes les données, nous les avons importées dans Power BI pour créer des visualisations interactives et analytiques des statistiques récoltées.


## Étapes du projet
 1. **Définition des objectifs et des données cibles**
  - Identifier les données à extraire (statistiques de tirs, passes, etc.).
  - Repérer les pages pertinentes sur le site FBref pour le scraping.

2. **Scraping des données avec Java**
  - Utilisation de bibliothèques Java comme Selenium pour extraire/interagir avec les éléments de la page.
  - Extraction des tableaux de statistiques des joueurs et des informations liées à la Ligue 1.
  - Validation et structuration des données dans un format exploitable : CSV.

3. **Scraping des photos des joueurs**
  - Récupération des URLs des images à partir des pages des joueurs.
  - Téléchargement des images localement en utilisant des requêtes HTTP avec des bibliothèques Java comme HttpURLConnection ou Apache HttpClient.

4. **Préparation et nettoyage des données**
  - Vérification de la qualité des données : suppression des doublons, gestion des données manquantes.
  - Structuration pour une intégration facile dans Power BI.

5. **Visualisation des données avec Power BI**
  - Importation des fichiers de données dans Power BI.
  - Création de tableaux de bord interactifs pour visualiser :
    - Les performances des joueurs (tirs, passes, phases arrêtées).
    - Comparaisons entre joueurs par équipe.
