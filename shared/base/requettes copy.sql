--ihorenantsoa
--getTheoreticalStockAndExpenses
--L'objectif de cette requête est de calculer les besoins théoriques en équipements 
--pour les bassins dans une période donnée 
--(du 1er janvier 2023 au 31 décembre 2023). 
--Elle agrège les quantités d'équipements nécessaires par type d'équipement et par saison, 
--tout en tenant compte du prix unitaire de chaque équipement.

--getTheoriqueAndExpenses
CREATE VIEW TheoriqueStock AS
SELECT 
    equipement.nom AS type, 
    SUM(besoinbassin.qteEquipement) AS quantite, 
    equipement.prixUnitaire, 
    saison.dateDebut
FROM 
    BesoinBassin besoinbassin
JOIN 
    Equipement equipement ON besoinbassin.idEquipement = equipement.idEquipement
JOIN 
    Saison saison ON saison.idSaison = besoinbassin.idBassin
GROUP BY 
    equipement.nom, 
    equipement.prixUnitaire, 
    saison.dateDebut;


--getRealStockAndExpenses
CREATE VIEW ReelStock AS
SELECT 
    equipement.nom AS type, 
    SUM(achatequipement.quantite) AS quantite, 
    equipement.prixUnitaire, 
    achatequipement.date
FROM 
    AchatEquipement achatequipement
JOIN 
    Equipement equipement ON achatequipement.idEquipement = equipement.idEquipement
GROUP BY 
    equipement.nom, 
    equipement.prixUnitaire, 
    achatequipement.date;

--getTheoriqueAndExpensesCrevettes
CREATE VIEW TheoriqueStockCrevette AS
SELECT 
    b.nom AS bassin,
    SUM(ec.poids) AS poids,
    s.dateDebut
FROM 
    Bassin b
JOIN 
    BesoinBassin bb ON b.idBassin = bb.idBassin
JOIN 
    Saison s ON s.idSaison = bb.idBassin
JOIN 
    EtatCrevette ec ON ec.idBassin = b.idBassin
GROUP BY 
    b.nom, 
    s.dateDebut;



--getRealAndExpensesCrevettes
CREATE VIEW ReelStockCrevette AS
SELECT 
    b.nom AS bassin,
    SUM(sc.poids) AS poids,
    sc.date
FROM 
    Bassin b
JOIN 
    StockCrevette sc ON b.idBassin = sc.idBassin
GROUP BY 
    b.nom, 
    sc.date;




--stockTheorie
SELECT type, quantite, prixUnitaire, dateDebut FROM TheoriqueStock 
                WHERE dateDebut BETWEEN CAST('2023-01-01' AS DATE) AND CAST('2023-12-31' AS DATE);

--stockReel
SELECT type, quantite, prixUnitaire, date FROM ReelStock 
                WHERE date BETWEEN CAST('2023-01-01' AS DATE) AND CAST('2023-12-31' AS DATE);

--crevettesTheorie
SELECT bassin, poids, dateDebut FROM TheoriqueStockCrevette
                WHERE dateDebut BETWEEN CAST('2023-01-01' AS DATE) AND CAST('2023-12-31' AS DATE);

--crevettesRéel
SELECT bassin, poids, date FROM ReelStockCrevette 
                WHERE date BETWEEN CAST('2023-01-01' AS DATE) AND CAST('2023-12-31' AS DATE);