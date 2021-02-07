package com.jrcw.expensemonitor;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseEntryModel extends BasicModel{
    private String dateString;
    private String timeString;
    private Date timeOfTransaction;
    private double expenditureTotal = 0.0;
    private String description;
    private int categoryId;
    private int currencyId;
    private int placeId;

    public ExpenseEntryModel(Context context) {
        super(context);
    }

    @Override
    protected void fetchData() {
        fetchCurrencies();
        fetchCategories();
        fetchPlaces();
        if(getCategories().size() > 0) categoryId = getCategories().get(0).getId();
        if(getCurrencies().size() > 0) currencyId = getCurrencies().get(0).getId();
        if(getPlaces().size() > 0) placeId = getPlaces().get(0).getId();
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public double getExpenditureTotal() {
        return expenditureTotal;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public void setExpenditureTotal(double expenditureTotal) {
        this.expenditureTotal = expenditureTotal;
    }

    public Date getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public boolean isMinimalDataSetForDetails() throws Exception{
        if(dateString != null) {
            if (dateString.contentEquals("")) {
                throw new Exception("No date");
            }
            if (timeString != null) {
                if (timeString.contentEquals("")) timeString = "00:00:00";
            } else {
                timeString = "00:00:00";
            }
        }else {
            throw new Exception("No date");
        }
        String ds = determineSeparator(dateString);
        String ts = determineSeparator(timeString);
        SimpleDateFormat sdf = new SimpleDateFormat("dd"+ds+"MM"+ds+"yyyy"+" "+"HH"+ts+"mm"+ts+"ss");
        try {
            timeOfTransaction = sdf.parse(formatDatePart(dateString, ds) + " " + formatTimePart(timeString, ts));
        }catch (ParseException e){
            return false;
        }
        return true;
    }

    public boolean isMinimalDataSetForStoring() throws Exception{
        if(isMinimalDataSetForDetails()) {
            if (expenditureTotal > 0.0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private String formatTimePart(String timeString, String ts) throws Exception{
        String[] comps = timeString.split(ts);
        if(comps.length > 3){
            throw  new Exception("Bad time format");
        }
        if(comps[0].length() == 1) comps[0] = "0" + comps[0];
        else if(comps[0].length() != 2) throw new Exception("Bad hours field");
        if(comps.length == 1) return comps[0] + ts + "00" + ts + "00";
        if(comps[1].length() == 1) comps[1] = "0" + comps[1];
        else if(comps[1].length() != 2) throw new Exception("Bad minutes field");
        if(comps.length == 2) return comps[0] + ts + comps[1] + ts + "00";
        if(comps[2].length() == 1) {
            comps[2] = "0" + comps[2];
            return comps[0] + ts + comps[1] + ts + comps[2];
        }
        else if(comps[2].length() != 2) throw new Exception("Bad seconds field");
        return timeString;
    }

    private String formatDatePart(String dateString, String ds) throws Exception{
        String[] comps = dateString.split(ds);
        if(comps.length != 3){
            throw  new Exception("Bad date format");
        }
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String yy = sdf.format(d).substring(0,1);
        if(comps[0].length() == 1) comps[0] = "0" + comps[0];
        else if(comps[0].length() != 2) throw new Exception("Bad day field");
        if(comps[1].length() == 1) comps[1] = "0" + comps[1];
        else if (comps[1].length() != 2) throw new Exception("Bad month field");
        if(comps[2].length() == 2) comps[2] = yy + comps[2];
        else if(comps[2].length() != 4) throw new Exception("Bad year field");
        return comps[0] + ds + comps[1] + ds + comps[2];
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

    public void storeExpense(){

    }

    public void storeForDetails(){

    }
}
