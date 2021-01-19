package com.jrcw.expensemonitor;

import com.jrcw.expensemonitor.db.DatabaseAccess;

import java.util.List;

public class ExpenseEntryModel {
    DatabaseAccess dba;
    List<String> places;

    public ExpenseEntryModel(DatabaseAccess dba){
        this.dba = dba;
        fetchData();
    }

    private void fetchData(){

    }

}
