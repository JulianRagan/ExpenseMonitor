package com.jrcw.expensemonitor;

import android.content.Context;
import android.database.Cursor;

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

}
