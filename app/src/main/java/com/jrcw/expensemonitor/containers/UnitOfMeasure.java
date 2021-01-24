package com.jrcw.expensemonitor.containers;

public class UnitOfMeasure {
    private int id;
    private String description;
    private String abbreviation;

    public UnitOfMeasure(int id, String description, String abbreviation) {
        this.id = id;
        this.description = description;
        this.abbreviation = abbreviation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString() {
        return abbreviation;
    }
}
