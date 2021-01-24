package com.jrcw.expensemonitor.containers;

public class Currency {
    private int id;
    private String name;
    private String ISOCode;
    private double exchangeRate;

    public Currency(int id, String name, String ISOCode, double exchangeRate) {
        this.id = id;
        this.name = name;
        this.ISOCode = ISOCode;
        this.exchangeRate = exchangeRate;
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

    public String getISOCode() {
        return ISOCode;
    }

    public void setISOCode(String ISOCode) {
        this.ISOCode = ISOCode;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return ISOCode;
    }
}
