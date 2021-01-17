package com.jrcw.expensemonitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LimitsActivityModel {
    private int funds;
    private int quantity;
    private Date from;
    private Date to;
    private List<String>categories;
    private List<String>products;
    private String category;
    private String product;

    public LimitsActivityModel() {
        funds = 0;
        quantity = 0;
        from = new Date();
        to = new Date();
        categories = getCategoryList();
        products = getProductsList(categories.get(0));
        category = categories.get(0);
        product = products.get(0);


    }

    private List<String> getProductsList(String category) {
        //TODO pobieranie danych z bazy danych
        List<String>retval = new ArrayList<>();
        switch (category) {
            case "Alkohol":
                retval.add("Żubr");
                retval.add("Tyski");
                break;
            case "Owoce":
                retval.add("Mango");
                retval.add("Cutryna");
                break;
            case "Warzywa":
                retval.add("Marchew");
                retval.add("Papryka");
                break;

        }
        return retval;
    }

    private List<String> getCategoryList() {
        //TODO pobieranie danych z bazy danych
        List<String>retval = new ArrayList<>();
        retval.add("Alkohol");
        retval.add("Owoce");
        retval.add("Warzywa");
        return retval;
    }

    public void saveLimit() {
        //TODO zapisanie limitu do bazy danych
    }

    public void setFunds(int funds) {
        this.funds = funds;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getProducts() {
        return products;
    }
}

