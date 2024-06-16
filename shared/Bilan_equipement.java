package crevettes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.*;

public class Bilan_equipement {

    public List<StockEquipment> getTheoriqueStock(String dateDebut, String dateFin)throws Exception  {
        String query = "SELECT type, quantite, prixUnitaire, dateDebut FROM TheoriqueStock " +
                "WHERE dateDebut BETWEEN CAST(? AS DATE) AND CAST(? AS DATE);";
        return executeQuery(query, dateDebut, dateFin, "dateDebut");
    }

    public List<StockEquipment> getReelStock(String dateDebut, String dateFin)throws Exception  {
        String query = "SELECT type, quantite, prixUnitaire, date FROM ReelStock " +
                "WHERE date BETWEEN CAST(? AS DATE) AND CAST(? AS DATE);";
        return executeQuery(query, dateDebut, dateFin, "date");
    }

    private List<StockEquipment> executeQuery(String query, String dateDebut, String dateFin, String dateColumn)throws Exception {
        List<StockEquipment> stockDepenseList = new ArrayList<>();
        try (Connection conn = Connexion.connectePostgres();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dateDebut);
            stmt.setString(2, dateFin);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                StockEquipment sd = new StockEquipment();
                sd.setType(rs.getString("type"));
                sd.setQuantite(rs.getInt("quantite"));
                sd.setPrixUnitaire(rs.getInt("prixUnitaire"));
                sd.setDate(rs.getString(dateColumn));
                stockDepenseList.add(sd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockDepenseList;
    }

    public List<StockEquipmentDifference> calculateDifferences(List<StockEquipment> theorique,
            List<StockEquipment> real) {
        List<StockEquipmentDifference> differences = new ArrayList<>();

        for (StockEquipment theo : theorique) {
            StockEquipment realItem = real.stream()
                    .filter(r -> r.getType().equals(theo.getType()) && r.getDate().equals(theo.getDate()))
                    .findFirst()
                    .orElse(null);

            if (realItem != null) {
                StockEquipmentDifference diff = new StockEquipmentDifference();
                diff.setType(theo.getType());
                diff.setDifferenceQuantite(theo.getQuantite() - realItem.getQuantite());
                diff.setDifferencePrix((theo.getQuantite() * theo.getPrixUnitaire())
                        - (realItem.getQuantite() * realItem.getPrixUnitaire()));
                differences.add(diff);
            } else {
                StockEquipmentDifference diff = new StockEquipmentDifference();
                diff.setType(theo.getType());
                diff.setDifferenceQuantite(theo.getQuantite());
                diff.setDifferencePrix(theo.getQuantite() * theo.getPrixUnitaire());
                differences.add(diff);
            }
        }

        for (StockEquipment realItem : real) {
            StockEquipment theoItem = theorique.stream()
                    .filter(t -> t.getType().equals(realItem.getType()) && t.getDate().equals(realItem.getDate()))
                    .findFirst()
                    .orElse(null);

            if (theoItem == null) {
                StockEquipmentDifference diff = new StockEquipmentDifference();
                diff.setType(realItem.getType());
                diff.setDifferenceQuantite(-realItem.getQuantite());
                diff.setDifferencePrix(-(realItem.getQuantite() * realItem.getPrixUnitaire()));
                differences.add(diff);
            }
        }

        return differences;
    }
}
