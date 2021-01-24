package com.jrcw.expensemonitor;

import android.content.Context;
import android.database.Cursor;

import com.jrcw.expensemonitor.db.DBQueries;
import com.jrcw.expensemonitor.db.DatabaseAccess;

public class Monitor {
    private static Monitor instance;
    private DatabaseAccess dba;
    private Context context;
    private String delay;
    private int defaultCurrencyId;
    private double defaultExchangeRate;
    private int maxQuota;
    private int maxQuantity;

    private Monitor(Context context){
        this.context = context;
        dba = new DatabaseAccess(context);
        getPrefs();
    }

    public static Monitor getInstance(Context context) {
        if(instance == null){
            instance = new Monitor(context);
        }
        return instance;
    }

    private void getPrefs(){
        dba.open();
        Cursor rs;
        rs = dba.fetchAny(DBQueries.qryGetAllFrom("Preferences"));
        if(rs.moveToFirst()) {
            do {
                delay = rs.getString(rs.getColumnIndex("DefaultEntryDelay"));
                defaultCurrencyId = rs.getInt(rs.getColumnIndex("DefaultCurrency_id"));
                defaultExchangeRate = rs.getDouble(rs.getColumnIndex("DefaultExchangeRate"));
                maxQuota = rs.getInt(rs.getColumnIndex("DefaultMaxForQuotaSlider"));
                maxQuantity = rs.getInt(rs.getColumnIndex("DefaultMaxForQuantitySlider"));
            } while (rs.moveToNext());
        }
        rs.close();
        dba.close();
    }

    public String getDelay() {
        return delay;
    }

    public int getDefaultCurrencyId() {
        return defaultCurrencyId;
    }

    public double getDefaultExchangeRate() {
        return defaultExchangeRate;
    }

    public int getMaxQuota() {
        return maxQuota;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }
}
