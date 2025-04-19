package model;

public class Promotion {
    private int id;
    private String clientType; // "ancien" ou "nouveau"
    private double discount;
    private String description;

    public Promotion(int id, String clientType, double discount, String description) {
        this.id = id;
        this.clientType = clientType;
        this.discount = discount;
        this.description = description;
    }

    public Promotion(String clientType, double discount, String description) {
        this(0, clientType, discount, description);
    }

    public int getId() { return id; }
    public String getClientType() { return clientType; }
    public double getDiscount() { return discount; }
    public String getDescription() { return description; }

    public void setId(int id) { this.id = id; }
    public void setClientType(String clientType) { this.clientType = clientType; }
    public void setDiscount(double discount) { this.discount = discount; }
    public void setDescription(String description) { this.description = description; }
}
