package com.jrcw.expensemonitor.containers;

import java.util.Date;

public class ExpenseDetail {
    private Date expenditureTime;
    private int id;
    private int productId;
    private int categoryId;
    private int unitId;
    private int currencyId;
    private double quantity;
    private double amount;
    private double exchangeRate;

    public ExpenseDetail(Date expenditureTime, int id) {
        this.expenditureTime = expenditureTime;
        this.id = id;
    }

    public Date getExpenditureTime() {
        return expenditureTime;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
