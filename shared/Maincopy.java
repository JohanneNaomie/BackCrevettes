package util;

import java.util.List;
import crevettes.*;
//ihorenantsoa main
public class Maincopy {
    public static void main(String[] args)throws Exception {
        Bilan_equipement bilanEquipement = new Bilan_equipement();
        BilanCrevettes bilanCrevettes = new BilanCrevettes();
        String dateDebut = "2023-01-01";
        String dateFin = "2023-12-31";

        List<StockEquipment> theoriqueStock = bilanEquipement.getTheoriqueStock(dateDebut, dateFin);
        List<StockEquipment> reelStock = bilanEquipement.getReelStock(dateDebut, dateFin);

        List<StockEquipment> thrstc = bilanEquipement.getTheoriqueStock(dateDebut, dateFin);
        List<StockEquipment> reelstc = bilanEquipement.getReelStock(dateDebut, dateFin);
        List<StockEquipmentDifference> differences = bilanEquipement.calculateDifferences(thrstc, reelstc);

        // Afficher les stocks théoriques
        System.out.println("Stock Théorique:");
        for (StockEquipment stock : theoriqueStock) {
            System.out.println("Type : " + stock.getType() + ", Quantité : " + stock.getQuantite() +
                    ", Prix Unitaire : " + stock.getPrixUnitaire() + ", Date : " + stock.getDate());
        }

        // Afficher les stocks réels
        System.out.println("Stock Réel:");
        for (StockEquipment stock : reelStock) {
            System.out.println("Type : " + stock.getType() + ", Quantité : " + stock.getQuantite() +
                    ", Prix Unitaire : " + stock.getPrixUnitaire() + ", Date : " + stock.getDate());
        }

        System.out.println("Bilan equipment");
        for (StockEquipmentDifference diff : differences) {
            System.out.println("Type : " + diff.getType() +
                    ", Différence quantite : " + diff.getDifferenceQuantite() +
                    ", Différence Prix : " + diff.getDifferencePrix());
        }

        System.out.println("Bilan crevettes");
        List<StockCrevette> thrstcCrevettes = bilanCrevettes.getTheoriqueStockCrevette(dateDebut, dateFin);
        List<StockCrevette> reelstcCrevettes = bilanCrevettes.getReelStockCrevette(dateDebut, dateFin);
        List<StockCrevetteDifference> differencesCrevettes = bilanCrevettes.calculateDifferences(thrstcCrevettes,
                reelstcCrevettes);

        for (StockCrevetteDifference diff : differencesCrevettes) {
            System.out.println("Date : " + diff.getDate() +
                    ", Différence quantité : " + diff.getDifferenceQuantite());
        }
    }
}
