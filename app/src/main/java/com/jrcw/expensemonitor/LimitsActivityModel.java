package com.jrcw.expensemonitor;

import android.content.Context;

import androidx.appcompat.view.ContextThemeWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private String fromField;
    private String toField;


    public LimitsActivityModel(Context context) {
        super(context);


    }
    @Override
    protected void fetchData() {
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

    public String getFromField() {
        return fromField;
    }

    public void setFromField(String fromField) {
        this.fromField = fromField;
    }

    public String getToField() {
        return toField;
    }

    public void setToField(String toField) {
        this.toField = toField;
    }

    public boolean isMinimalDataSet() throws Exception {
        if(fromField != null) {
            if (fromField.contentEquals("")) {
                throw new Exception("No from date");
            }
        }else {
            throw new Exception("No from date");
        }
        if (toField != null) {
            if (toField.contentEquals("")) {
                throw new Exception("No to date");
            }
        }else {
            throw new Exception("No to date");
        }

        String ds = determineSeparator(fromField);
        String ts = determineSeparator(toField);
        SimpleDateFormat sdf = new SimpleDateFormat("dd"+ds+"MM"+ds+"yyyy");
        try {
            from = sdf.parse(formatDatePart(fromField, ds));
        }catch (ParseException e){
            return false;
        }
        return true;
    }
    private String determineSeparator(String dateOrTime) throws Exception{
        if(dateOrTime.contains(":")){
            return ":";
        }
        if(dateOrTime.contains(".")){
            return ".";
        }
        if(dateOrTime.contains("-")){
            return "-";
        }
        if(dateOrTime.contains("/")){
            return "/";
        }
        throw new Exception("separator");
    }
    private String formatDatePart(String dateString, String ds) throws Exception{
        String[] comps;
        if(ds.contentEquals(".")){
            comps = dateString.split("\\.");
        }else {
            comps = dateString.split(ds);
        }
        if(comps.length != 3){
            throw  new Exception("Bad date format");
        }
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String yy = sdf.format(d).substring(0,2);
        if(comps[0].length() == 1) comps[0] = "0" + comps[0];
        else if(comps[0].length() != 2) throw new Exception("Bad day field");
        if(comps[1].length() == 1) comps[1] = "0" + comps[1];
        else if (comps[1].length() != 2) throw new Exception("Bad month field");
        if(comps[2].length() == 2) comps[2] = yy + comps[2];
        else if(comps[2].length() != 4) throw new Exception("Bad year field");
        return comps[0] + ds + comps[1] + ds + comps[2];
    }
    public void clear(){
        fromField = "";
        toField = "";
        funds = 0;
        quantity = 0;
   //     SimpleDateFormat= null;
  //      expenditureTotal = 0.0;
   //     description = "";
    }

}


