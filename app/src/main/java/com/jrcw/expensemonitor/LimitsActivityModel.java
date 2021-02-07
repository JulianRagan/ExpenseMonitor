package com.jrcw.expensemonitor;

import android.content.Context;

import androidx.appcompat.view.ContextThemeWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LimitsActivityModel extends BasicModel{
    private int funds;
    private int quantity;
    private Date from;
    private Date to;
    private int categoryId;
    private int productId;
    private boolean onlyCategory;
    private int currencyId;

    public LimitsActivityModel(Context context) {
        super(context);


    }
    @Override
    void fetchData() {
        fetchCategories();
        fetchProducts();
        fetchCurrencies();
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

    public int getFunds() {
        return funds;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public boolean isOnlyCategory() {
        return onlyCategory;
    }

    public void setOnlyCategory(boolean onlyCategory) {
        this.onlyCategory = onlyCategory;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }
}

