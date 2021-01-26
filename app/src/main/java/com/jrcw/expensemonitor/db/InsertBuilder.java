package com.jrcw.expensemonitor.db;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertBuilder {
    private String table;
    private String qry;
    private String val;

    /**
     * @param table - name of table to which insert will be carried out
     * @param firstField - name of first column
     * @param firstData - first data (Integer, Double, String or Date)
     * @param special - Decimal2 or Decimal3 or null
     */
    public InsertBuilder(String table, String firstField, Object firstData, String special) throws Exception{
        qry = "INSERT INTO " + table + "(" + firstField;
        val = "VALUES (";
        addData(firstData, special);

    }


    private boolean addData(Object data, String special) throws Exception{
        boolean added = false;
        String sep = ", ";
        if(val.endsWith("(")) sep = "";
        if(special != null){
            DecimalFormat df = null;
            if(special.contentEquals("Decimal2")) {
                df = new DecimalFormat("#.00");
            }else if(special.contentEquals("Decimal3")){
                df = new DecimalFormat("#.000");
            }
            if(df != null){
                if(data instanceof Double) {
                    val += sep + "'" + df.format(data) + "'";
                    added = true;
                }
            }
        }else {
            if (data instanceof String){
                val += sep + "'" + (String)data + "'";
                added = true;
            }else if(data instanceof Integer){
                val += sep + ((Integer)data).intValue();
                added = true;
            }else if(data instanceof Date){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                val += sep + "'" + dateFormat.format((Date)data) + "'";
                added = true;
            }
        }
        if(!added) throw new Exception ("Unable to process data, unsupported data type");
        return added;
    }

    /**
     * Method that adds data field do insert statement
     * @param field - name of column to hold data
     * @param data - actual data to store(Integer, Double, String or Date)
     * @param special - Decimal2 or Decimal3 or null
     */
    public void addFieldAndData(String field, Object data, String special) throws Exception{
        if(addData(data, special)) qry += ", " + field;
    }

    public String getQry(){
        qry += ") " + val + ");";
        return qry;
    }
}
