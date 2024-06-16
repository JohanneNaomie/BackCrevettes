package crevettes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.*;

public class BilanCrevettes {

    public List<StockCrevette> getTheoriqueStockCrevette(String dateDebut, String dateFin)throws Exception {
        String query = "SELECT Bassin.nom AS bassin, StockCrevette.poids, StockCrevette.date FROM StockCrevette " +
                "JOIN Bassin ON StockCrevette.idBassin = Bassin.idBassin " +
                "WHERE StockCrevette.date BETWEEN CAST(? AS DATE) AND CAST(? AS DATE) AND StockCrevette.action = 1;";
        return executeQuery(query, dateDebut, dateFin);
    }

    public List<StockCrevette> getReelStockCrevette(String dateDebut, String dateFin) throws Exception{
        String query = "SELECT Bassin.nom AS bassin, StockCrevette.poids, StockCrevette.date FROM StockCrevette " +
                "JOIN Bassin ON StockCrevette.idBassin = Bassin.idBassin " +
                "WHERE StockCrevette.date BETWEEN CAST(? AS DATE) AND CAST(? AS DATE) AND StockCrevette.action = 2;";
        return executeQuery(query, dateDebut, dateFin);
    }

    private List<StockCrevette> executeQuery(String query, String dateDebut, String dateFin)throws Exception {
        List<StockCrevette> stockCrevetteList = new ArrayList<>();
        try (Connection conn = Connexion.connectePostgres();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dateDebut);
            stmt.setString(2, dateFin);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                StockCrevette sc = new StockCrevette();
                sc.setType(rs.getString("bassin"));
                sc.setQuantite(rs.getDouble("poids"));
                sc.setDate(rs.getString("date")); 
                stockCrevetteList.add(sc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockCrevetteList;
    }

    public List<StockCrevetteDifference> calculateDifferences(List<StockCrevette> theorique, List<StockCrevette> real) {
        List<StockCrevetteDifference> differences = new ArrayList<>();

        for (StockCrevette theo : theorique) {
            StockCrevette realItem = real.stream()
                    .filter(r -> r.getType().equals(theo.getType()) && r.getDate().equals(theo.getDate()))
                    .findFirst()
                    .orElse(null);

            if (realItem != null) {
                StockCrevetteDifference diff = new StockCrevetteDifference();
                diff.setDate(theo.getDate());
                diff.setType(theo.getType());
                diff.setDifferenceQuantite(theo.getQuantite() - realItem.getQuantite());
                diff.setDifferencePrix((theo.getQuantite() * theo.getPrixUnitaire())
                        - (realItem.getQuantite() * realItem.getPrixUnitaire()));
                differences.add(diff);
            } else {
                StockCrevetteDifference diff = new StockCrevetteDifference();
                diff.setDate(theo.getDate());
                diff.setType(theo.getType());
                diff.setDifferenceQuantite(theo.getQuantite());
                diff.setDifferencePrix(theo.getQuantite() * theo.getPrixUnitaire());
                differences.add(diff);
            }
        }

        for (StockCrevette realItem : real) {
            StockCrevette theoItem = theorique.stream()
                    .filter(t -> t.getType().equals(realItem.getType()) && t.getDate().equals(realItem.getDate()))
                    .findFirst()
                    .orElse(null);

            if (theoItem == null) {
                StockCrevetteDifference diff = new StockCrevetteDifference();
                diff.setDate(realItem.getDate());
                diff.setType(realItem.getType());
                diff.setDifferenceQuantite(-realItem.getQuantite());
                diff.setDifferencePrix(-(realItem.getQuantite() * realItem.getPrixUnitaire()));
                differences.add(diff);
            }
        }

        return differences;
    }
}
