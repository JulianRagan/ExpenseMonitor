package com.jrcw.expensemonitor;

import android.content.Context;
import android.database.Cursor;

import androidx.appcompat.view.ContextThemeWrapper;

import com.jrcw.expensemonitor.db.DatabaseAccess;
import com.jrcw.expensemonitor.db.InsertBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LimitsActivityModel extends BasicModel{
    private int funds = 0;
    private int quantity = 0;
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

    public void setFunds(int funds) {
        this.funds = funds;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setOnlyCategory(boolean onlyCategory) {
        this.onlyCategory = onlyCategory;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public void setFromField(String fromField) {
        this.fromField = fromField;
    }

    public void setToField(String toField) {
        this.toField = toField;
    }

    public boolean isMinimalDataSet() throws Exception {
        if(fromField != null) {
            if (fromField.contentEquals(""))  throw new Exception("No from date");
        }else throw new Exception("No from date");
        if (toField != null) {
            if (toField.contentEquals("")) throw new Exception("No to date");
        }else throw new Exception("No to date");

        String ds = determineSeparator(fromField);
        String ts = determineSeparator(toField);
        SimpleDateFormat sdf = new SimpleDateFormat("dd"+ds+"MM"+ds+"yyyy");
        try {
            from = sdf.parse(formatDatePart(fromField, ds));
        }catch (ParseException e){
            throw new Exception("Bad from date");
        }
        sdf = new SimpleDateFormat("dd"+ts+"MM"+ts+"yyyy");
        try {
            to = sdf.parse(formatDatePart(toField, ts));
        }catch (ParseException e){
            throw new Exception("Bad to date");
        }
        if (sdf.format(to).contentEquals(sdf.format(from))){
            throw new Exception ("Equal dates");
        }
        if(funds > 0 && quantity > 0){
            throw new Exception("Both limits set");
        }
        if(funds == 0 && quantity == 0){
            throw new Exception("Limit has not been set");
        }
        return true;
    }
    private String determineSeparator(String dateOrTime) throws Exception{
        if(dateOrTime.contains(":")) return ":";
        if(dateOrTime.contains(".")) return ".";
        if(dateOrTime.contains("-")) return "-";
        if(dateOrTime.contains("/")) return "/";
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
        from = null;
        to = null;
    }

    public void storeLimit() throws Exception{
        DatabaseAccess dba = new DatabaseAccess(context);
        dba.open();
        int id = getNextLimitId(dba);
        try{
            dba.executeQuery(buildInsert(id));
        }finally {
            dba.close();
        }
    }

    private String buildInsert(int id) throws Exception{
        InsertBuilder ib = new InsertBuilder("Limits", "StartDate", from, null);
        ib.addFieldAndData("EndDate", to, null);
        ib.addFieldAndData("id", id, null);
        ib.addFieldAndData("Category_id", categoryId, null);
        if(!onlyCategory) ib.addFieldAndData("Product_id", productId, null);
        ib.addFieldAndData("Currency_id", currencyId, null);
        ib.addFieldAndData("Quota", (double)funds, "Decimal2");
        ib.addFieldAndData("Quantity", (double)quantity, "Decimal3");
        return ib.getQry();
    }

    private int getNextLimitId(DatabaseAccess dba){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String qry = "SELECT MAX(id) AS id FROM Limits WHERE StartDate = '";
        qry += sdf.format(from) + "' AND EndDate = '" + sdf.format(to) + "';";
        int retval = 0;
        Cursor rs = dba.fetchAny(qry);
        if(rs.moveToFirst()){
            retval = rs.getInt(rs.getColumnIndex("id"));
        }
        rs.close();
        return retval+1;
    }
}


