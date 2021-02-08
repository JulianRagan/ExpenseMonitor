package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;
import android.database.Cursor;

import com.jrcw.expensemonitor.BasicModel;
import com.jrcw.expensemonitor.containers.Currency;
import com.jrcw.expensemonitor.db.DatabaseAccess;
import com.jrcw.expensemonitor.db.InsertBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDetailPopupModel extends BasicModel {
    private int categoryId;
    private int productId;
    private int unitId;
    private int currencyId;
    private double amount;
    private double quantity;
    private String amountField;
    private String quantityField;
    private Date entryDate;

    public AddDetailPopupModel(Context context, Date entryDate) {
        super(context);
        this.entryDate = entryDate;
    }

    @Override
    protected void fetchData() {
        fetchCategories();
        fetchCurrencies();
        fetchProducts();
        fetchUnits();
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public void setAmountField(String amountField) {
        this.amountField = amountField;
    }

    public void setQuantityField(String quantityField) {
        this.quantityField = quantityField;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getProductId() {
        return productId;
    }

    public int getUnitId() {
        return unitId;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public String getAmountField() {
        return amountField;
    }

    public String getQuantityField() {
        return quantityField;
    }

    public boolean isMinimalDataSet() throws Exception{
        if(amountField != null && quantityField != null){
            try {
                amount = Double.parseDouble(amountField);
            }catch (NumberFormatException e){
                throw new Exception("Amount");
            }
            try{
                quantity = Double.parseDouble(quantityField);
            }catch (NumberFormatException e){
                throw new Exception("Quantity");
            }
        }
        return true;
    }

    public void clear() {
        amountField = "";
        quantityField = "";
    }

    public double getExchangeRate(){
        for(Currency c:getCurrencies()){
            if(c.getId() == currencyId){
                return c.getExchangeRate();
            }
        }
        return 1.00;
    }

    public void storeDetail(){
        DatabaseAccess dba = new DatabaseAccess(context);
        dba.open();
        int id = getNextDetailId(dba);
        try {
            InsertBuilder ib = new InsertBuilder("ExpenseDetail", "time", entryDate, null);
            ib.addFieldAndData("id",id,null);
            ib.addFieldAndData("Product_id", productId, null);
            ib.addFieldAndData("Category_id", categoryId, null);
            ib.addFieldAndData("Quantity", quantity, "Decimal3");
            ib.addFieldAndData("UnitOfMeasure_id", unitId, null);
            ib.addFieldAndData("Amount", amount, "Decimal2");
            ib.addFieldAndData("Currency_id", currencyId, null);
            ib.addFieldAndData("ExchangeRate", getExchangeRate(), "Decimal2");
            dba.executeQuery(ib.getQry());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            dba.close();
        }

    }

    private int getNextDetailId(DatabaseAccess dba){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String qry = "SELECT MAX(id) AS id FROM ExpenseDetail WHERE time = '" + sdf.format(entryDate) + "';";
        int retval = 0;
        Cursor rs = dba.fetchAny(qry);
        if(rs.moveToFirst()){
            retval = rs.getInt(rs.getColumnIndex("id"));
        }
        rs.close();
        return retval+1;
    }
}
