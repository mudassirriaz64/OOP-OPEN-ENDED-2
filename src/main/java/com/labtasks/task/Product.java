package com.labtasks.task;

import java.io.File;

public class Product {
    private int id;
    private String name;
    private double price;
    private String imagePath;
    private String description;

    public Product(int id, String name, double price, File imageFile, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imageFile != null ? imageFile.getPath() : null;
        this.description = description;
    }

    public int getId()
    {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public File getImageFile() {
        return imagePath != null ? new File(imagePath) : null;
    }
}
