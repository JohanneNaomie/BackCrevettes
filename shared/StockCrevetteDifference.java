package crevettes;

public class StockCrevetteDifference {
    private String date;
    private double differenceQuantite;
    private String type;
    private double differencePrix;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getDifferenceQuantite() {
        return differenceQuantite;
    }

    public void setDifferenceQuantite(double differenceQuantite) {
        this.differenceQuantite = differenceQuantite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDifferencePrix() {
        return differencePrix;
    }

    public void setDifferencePrix(double differencePrix) {
        this.differencePrix = differencePrix;
    }
}
