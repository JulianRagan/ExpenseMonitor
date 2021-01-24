package com.jrcw.expensemonitor;

import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;

import com.jrcw.expensemonitor.containers.Category;
import com.jrcw.expensemonitor.containers.Currency;
import com.jrcw.expensemonitor.containers.Place;
import com.jrcw.expensemonitor.containers.Product;
import com.jrcw.expensemonitor.containers.UnitOfMeasure;
import com.jrcw.expensemonitor.db.DBQueries;
import com.jrcw.expensemonitor.db.DatabaseAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ExpenseEntryModel extends BasicModel{

    public ExpenseEntryModel(Context context) {
        super(context);
    }

    @Override
    void fetchData() {
        fetchCategories();
        fetchCurrencies();
        fetchPlaces();
        fetchProducts();
        fetchUnits();
    }
}
