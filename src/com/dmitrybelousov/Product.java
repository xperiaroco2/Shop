package com.dmitrybelousov;

public class Product {

    private String name;
    private double price;
    private String classification;
    private double size;
    private String composition;
    private int inStock;
    private int soldCount = 0;
    private int purchaseCount = 0;

    public Product(String name, double price, String classification, double size, String composition, int inStock) {
        this.name = name;
        this.price = price;
        this.classification = classification;
        this.size = size;
        this.composition = composition;
        this.inStock = inStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(int soldCount) {
        this.soldCount = soldCount;
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", classification='" + classification + '\'' +
                ", size=" + size +
                ", composition='" + composition + '\'' +
                ", inStock=" + inStock +
                '}';
    }
}
