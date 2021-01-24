package com.jrcw.expensemonitor.containers;

public class Product {
    private int id;
    private String name;
    private String description;
    private int categoryId;
    private int defaultUnitId;

    public Product(int id, String name, String description, int categoryId, int defaultUnitId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.defaultUnitId = defaultUnitId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getDefaultUnitId() {
        return defaultUnitId;
    }

    public void setDefaultUnitId(int defaultUnitId) {
        this.defaultUnitId = defaultUnitId;
    }

    @Override
    public String toString() {
        return name;
    }
}
