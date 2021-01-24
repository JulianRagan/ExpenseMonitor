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

public abstract class BasicModel {
    private DatabaseAccess dba;
    private Context context;
    private List<Place> places;
    private List<Product> products;
    private List<Category> categories;
    private List<Currency> currencies;
    private List<UnitOfMeasure> units;

    public BasicModel (Context context){
        dba = new DatabaseAccess(context);
        this.context = context;
        places = new ArrayList<>();
        products = new ArrayList<>();
        categories = new ArrayList<>();
        currencies = new ArrayList<>();
        units = new ArrayList<>();

    }

    abstract void fetchData();

    protected void fetchCategories(){
        dba = dba.open();
        Cursor rs;
        rs = dba.fetchAny(DBQueries.qryGetAllFrom("Category"));
        if(rs.moveToFirst()){
            do{
                int id = rs.getInt(rs.getColumnIndex("id"));
                String name = rs.getString(rs.getColumnIndex("Name"));
                String desc = "";
                if (!rs.isNull(rs.getColumnIndex("Description"))) {
                    desc = rs.getString(rs.getColumnIndex("Description"));
                }
                categories.add(new Category(id, name, desc));
            }while(rs.moveToNext());
        }
        rs.close();
        dba.close();
    }

    protected void fetchPlaces(){
        dba = dba.open();
        Cursor rs;
        rs = dba.fetchAny(DBQueries.qryGetAllFrom("Place"));
        if(rs.moveToFirst()){
            do{
                int id = rs.getInt(rs.getColumnIndex("id"));
                String name = rs.getString(rs.getColumnIndex("Name"));
                Place p = new Place(id, name);
                int index = rs.getColumnIndex("Description");
                if(!rs.isNull(index)){
                    p.setDescription(rs.getString(index));
                }
                index = rs.getColumnIndex("StreetName");
                if(!rs.isNull(index)){
                    p.setStreet(rs.getString(index));
                }
                index = rs.getColumnIndex("StretNumber");
                if(!rs.isNull(index)){
                    p.setStreetNumber(rs.getString(index));
                }
                index = rs.getColumnIndex("CityName");
                if(!rs.isNull(index)){
                    p.setCity(rs.getString(index));
                }
                index = rs.getColumnIndex("CountryName");
                if(!rs.isNull(index)){
                    p.setCountry(rs.getString(index));
                }
                places.add(p);
            }while(rs.moveToNext());
        }
        rs.close();
        dba.close();
    }

    protected void fetchProducts(){
        dba = dba.open();
        Cursor rs;
        rs = dba.fetchAny(DBQueries.qryGetAllFrom("Product"));
        if(rs.moveToFirst()){
            do{
                int id = rs.getInt(rs.getColumnIndex("id"));
                String name = rs.getString(rs.getColumnIndex("Name"));
                String desc = "";
                if (!rs.isNull(rs.getColumnIndex("Description"))){
                    desc = rs.getString(rs.getColumnIndex("Description"));
                }
                int catId = rs.getInt(rs.getColumnIndex("Category_id"));
                int dumId = rs.getInt(rs.getColumnIndex("DefaultUnit_id"));
                products.add(new Product(id, name, desc, catId, dumId));
            }while (rs.moveToNext());
        }
        rs.close();
        rs = dba.fetchAny(DBQueries.qryGetAllFrom("UnitOfMeasure"));
        if(rs.moveToFirst()){
            do{
                int id = rs.getInt(rs.getColumnIndex("id"));
                String abbr = rs.getString(rs.getColumnIndex("Abbreviation"));
                String desc = "";
                if (!rs.isNull(rs.getColumnIndex("Description"))){
                    desc = rs.getString(rs.getColumnIndex("Description"));
                }
                units.add(new UnitOfMeasure(id, desc, abbr));
            }while (rs.moveToNext());
        }
        rs.close();
        dba.close();
    }

    protected void fetchCurrencies(){
        dba = dba.open();
        Cursor rs;
        rs = dba.fetchAny(DBQueries.qryGetAllFrom("Currency"));
        if(rs.moveToFirst()){
            do{
                int id = rs.getInt(rs.getColumnIndex("id"));
                String name = rs.getString(rs.getColumnIndex("Name"));
                String iso = rs.getString(rs.getColumnIndex("ISOCode"));
                double exrate = rs.getDouble(rs.getColumnIndex("ExchangeRate"));
                currencies.add(new Currency(id, name, iso, exrate));
            }while (rs.moveToNext());
        }
        rs.close();
        dba.close();
    }

    protected void fetchUnits(){
        dba = dba.open();
        Cursor rs;
        rs = dba.fetchAny(DBQueries.qryGetAllFrom("UnitOfMeasure"));
        if(rs.moveToFirst()){
            do{
                int id = rs.getInt(rs.getColumnIndex("id"));
                String abbr = rs.getString(rs.getColumnIndex("Abbreviation"));
                String desc = "";
                if (!rs.isNull(rs.getColumnIndex("Description"))){
                    desc = rs.getString(rs.getColumnIndex("Description"));
                }
                units.add(new UnitOfMeasure(id, desc, abbr));
            }while (rs.moveToNext());
        }
        rs.close();
        dba.close();
    }

    public int getCategoryIdByName(String name) throws NoSuchElementException {
        for(Category c:categories){
            if(c.getName().contentEquals(name)){
                return c.getId();
            }
        }
        throw new NoSuchElementException();
    }

    public ArrayAdapter<Place> getPlacesAdapter(Context context){
        ArrayAdapter<Place> retval = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, places);
        retval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return retval;
    }

    public ArrayAdapter<Category> getCategoryAdapter(Context context){
        ArrayAdapter<Category> retval = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, categories);
        retval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return retval;
    }

    public ArrayAdapter<Currency> getCurrencyAdapter(Context context){
        ArrayAdapter<Currency> retval = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, currencies);
        retval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return retval;
    }

    public ArrayAdapter<UnitOfMeasure> getUnitsAdapter(Context context){
        ArrayAdapter<UnitOfMeasure> retval = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, units);
        retval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return retval;
    }

    public ArrayAdapter<Product> getProductsAdapter(Context context, String category){
        List<Product> byCat = new ArrayList<>();
        int catId = getCategoryIdByName(category);
        for(Product p:products){
            if(p.getCategoryId() == catId){
                byCat.add(p);
            }
        }
        if(byCat.size() == 0){
            byCat.add(new Product(0, "Brak produktów", "", catId, 0));
        }
        ArrayAdapter<Product> retval = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, byCat);
        retval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return retval;
    }
}
