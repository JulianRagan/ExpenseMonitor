package com.jrcw.expensemonitor.db;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateBuilder {
    private String qry;
    private String val;

    public UpdateBuilder(String table){
        qry = "UPDATE " + table + " SET ";
        val = "";
    }

    private void addData(String field, Object data, String special) throws Exception{
        boolean added = false;
        String sep = ", ";
        if(val.contentEquals("")) sep = "";
        if(data == null){
            val += sep + field + " = null";
        }else if(special != null){
            DecimalFormat df = null;
            if(special.contentEquals("Decimal2")) {
                df = new DecimalFormat("#.00");
            }else if(special.contentEquals("Decimal3")){
                df = new DecimalFormat("#.000");
            }
            if(df != null){
                if(data instanceof Double) {
                    val += sep + field + " = '" + df.format(data) + "'";
                    added = true;
                }
            }
        }else {
            if (data instanceof String){
                val += sep + field + " = '" + (String)data + "'";
                added = true;
            }else if(data instanceof Integer){
                val += sep + field + " = " + ((Integer)data).intValue();
                added = true;
            }else if(data instanceof Date){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                val += sep + field + " = '" + dateFormat.format((Date)data) + "'";
                added = true;
            }
        }
        if(!added) throw new Exception ("Unable to process data, unsupported data type");
    }

    /**
     * Method that adds data field do insert statement
     * @param field - name of column to hold data
     * @param data - actual data to store(Integer, Double, String, Date or null)
     * @param special - Decimal2 or Decimal3 or null
     */
    public void addFieldAndData(String field, Object data, String special) throws Exception{
        addData(field, data, special);
    }

    /**
     * @param where - full WHERE clause
     * @return
     */
    public String getQry(String where){
        qry += val + " " + where + ";";
        return qry;
    }
}
