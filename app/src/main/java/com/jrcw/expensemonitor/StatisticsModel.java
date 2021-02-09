package com.jrcw.expensemonitor;

import android.content.Context;
import android.database.Cursor;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jrcw.expensemonitor.containers.Category;
import com.jrcw.expensemonitor.containers.Expense;
import com.jrcw.expensemonitor.containers.ExpenseDetail;
import com.jrcw.expensemonitor.db.DBQueries;
import com.jrcw.expensemonitor.db.DatabaseAccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatisticsModel extends BasicModel{
    private List<Expense> expenses;
    private List<ExpenseDetail> details;
    private String fromField;
    private String toField;
    private Date from;
    private Date to;
    private int categoryId;
    private int productId;
    private int placeId;

    public StatisticsModel(Context context) {
        super(context);
    }

    @Override
    protected void fetchData() {
        fetchCategories();
        fetchCurrencies();
        fetchPlaces();
        fetchProducts();
        fetchUnits();
        fetchExpenses();
    }

    public void setFromField(String fromField) {
        this.fromField = fromField;
    }

    public void setToField(String toField) {
        this.toField = toField;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    private void fetchExpenses(){
        DatabaseAccess dba = new DatabaseAccess(context);
        dba.open();
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        expenses = new ArrayList<>();
        Cursor rs = dba.fetchAny(DBQueries.qryGetAllFrom("Expense"));
        if(rs.moveToFirst()){
            do{
                try {
                    Date d = dateTime.parse(rs.getString(rs.getColumnIndex("time")));
                    int placeId = rs.getInt(rs.getColumnIndex("Place_id"));
                    String description = rs.getString(rs.getColumnIndex("Description"));
                    expenses.add(new Expense(d, description, placeId));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }while (rs.moveToNext());
        }
        rs.close();
        details = new ArrayList<>();
        rs = dba.fetchAny(DBQueries.qryGetAllFrom("ExpenseDetail"));
        if(rs.moveToFirst()){
            do{
                try {
                    Date d = dateTime.parse(rs.getString(rs.getColumnIndex("time")));
                    int id = rs.getInt(rs.getColumnIndex("id"));
                    ExpenseDetail ed = new ExpenseDetail(d, id);
                    ed.setProductId(rs.getInt(rs.getColumnIndex("Product_id")));
                    ed.setCategoryId(rs.getInt(rs.getColumnIndex("Category_id")));
                    ed.setUnitId(rs.getInt(rs.getColumnIndex("UnitOfMeasure_id")));
                    ed.setCurrencyId(rs.getInt(rs.getColumnIndex("Currency_id")));
                    ed.setAmount(rs.getDouble(rs.getColumnIndex("Amount")));
                    ed.setQuantity(rs.getDouble(rs.getColumnIndex("Quantity")));
                    ed.setExchangeRate(rs.getDouble(rs.getColumnIndex("ExchangeRate")));
                    details.add(ed);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }while (rs.moveToNext());
        }
        rs.close();
        dba.close();
    }

    public void parseDates() throws Exception{
        if(fromField != null) {
            if (fromField.contentEquals("")) throw new Exception("No from date");
        }else {
            throw new Exception("No from date");
        }
        if (toField != null) {
            if (toField.contentEquals("")) throw new Exception("No to date");
        }else {
            throw new Exception("No to date");
        }
        String fds = determineSeparator(fromField);
        String tds = determineSeparator(toField);
        SimpleDateFormat fdf = new SimpleDateFormat("dd"+fds+"MM"+fds+"yyyy");
        SimpleDateFormat tdf = new SimpleDateFormat("dd"+tds+"MM"+tds+"yyyy");
        try {
            from = fdf.parse(formatDatePart(fromField, fds));
        }catch (ParseException e){
            throw new Exception("Bad from date");
        }
        try {
            to = tdf.parse(formatDatePart(toField, fds));
        }catch (ParseException e){
            throw new Exception("Bad to date");
        }
    }

    private String formatDatePart(String dateString, String ds) throws Exception{
        String[] comps;
        if(ds.contentEquals(".")){
            comps = dateString.split("\\.");
        }else {
            comps = dateString.split(ds);
        }
        if(comps.length != 3){
            throw new Exception("Bad date format");
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

    private String determineSeparator(String dateOrTime) throws Exception{
        if(dateOrTime.contains(":")) return ":";
        if(dateOrTime.contains(".")) return ".";
        if(dateOrTime.contains("-")) return "-";
        if(dateOrTime.contains("/")) return "/";
        throw new Exception("separator");
    }

    private String getCategoryNameById(int categoryId){
        for(Category c:getCategories()){
            if(categoryId == c.getId()){
                return c.getName();
            }
        }
        throw new IllegalArgumentException("Bad category id");
    }

    public PieData evaluateExpenseDistribution() throws Exception{
        parseDates();
        List<PieEntry> entries = new ArrayList<>();
        double[] exppercat = new double[getCategories().size()];
        for(ExpenseDetail ed: details){
            if(ed.getExpenditureTime().after(from) && ed.getExpenditureTime().before(to)){
                exppercat[ed.getCategoryId()] += ed.getAmount();
            }
        }
        double sumForPeriod = 0.0;
        for(int i = 0; i < exppercat.length; i++){
            sumForPeriod += exppercat[i];
        }
        for(int i = 0; i < exppercat.length; i++){
            if(exppercat[i] > 0.0){
                float f = (float)(exppercat[i]/sumForPeriod);
                entries.add(new PieEntry(f,getCategoryNameById(i)));
            }
        }
        PieDataSet pds = new PieDataSet(entries, "Podział wydatków na kategorie");
        return new PieData(pds);
    }
}
