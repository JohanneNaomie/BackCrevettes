package crevettes;

public class StockEquipmentDifference {
    private String type;
    private double differenceQuantite;
    private double differencePrix;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDifferenceQuantite() {
        return differenceQuantite;
    }

    public void setDifferenceQuantite(double differenceQuantite) {
        this.differenceQuantite = differenceQuantite;
    }

    public double getDifferencePrix() {
        return differencePrix;
    }

    public void setDifferencePrix(double differencePrix) {
        this.differencePrix = differencePrix;
    }

    @Override
    public String toString() {
        return "StockEquipmentDifference{" +
                "type='" + type + '\'' +
                ", differenceQuantite=" + differenceQuantite +
                ", differencePrix=" + differencePrix +
                '}';
    }
}
