package com.jrcw.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;

import com.jrcw.expensemonitor.db.DatabaseAccess;

public class ExpenseEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenseentry);
        DatabaseAccess dba = new DatabaseAccess(this);
        ExpenseEntryModel model = new ExpenseEntryModel(dba);
        new ExpenseEntryController(this, model);

    }

    public View getComponentByName(String name){
        return findViewById(R.id.btnFunction);
    }
}