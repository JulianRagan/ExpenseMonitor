package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.jrcw.expensemonitor.containers.Category;
import com.jrcw.expensemonitor.containers.Product;
import com.jrcw.expensemonitor.containers.UnitOfMeasure;
import com.jrcw.expensemonitor.db.DatabaseAccess;
import com.jrcw.expensemonitor.db.InsertBuilder;
import com.jrcw.expensemonitor.db.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;

public class AddProductPopupModel {
    private String name;
    private String description;
    private Product product;
    private List<Product> products;
    private int categoryId;
    private int unitId;
    private List<Category> categories;
    private List<UnitOfMeasure> units;
    private Context context;


    public AddProductPopupModel(List<Product> products, List<Category> categories, List<UnitOfMeasure> units, Context context) {
        this.context = context;
        this.categories = categories;
        this.units = units;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String definition) {
        this.name = definition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public void populateFromProduct(Product product) {
        this.product = product;
        name = product.getName();
        description = product.getDescription();
        unitId = product.getDefaultUnitId();
        categoryId = product.getCategoryId();

    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public void clear(){
        name = "";
        description = "";
        product = null;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public boolean isMinimalDataSet() {
        if (name != null) {
            if (!name.contentEquals("")) {
                return true;
            }
        }
        return false;
    }

    public boolean productExists() {
        boolean retval = false;
        if (name != null) {
            if (!name.contentEquals("")) {
                for (Product p : products) {
                    if (p.getName().contentEquals(name)) {
                        retval = true;
                    }
                }
            }
        }
        return retval;
    }

    public ArrayAdapter<Product> getProductAdapter(Context context, int categoryId){
        List<Product> filtr = new ArrayList<>();
        for (Product p:products){
            if (p.getCategoryId() == categoryId) filtr.add(p);
        }
        ArrayAdapter<Product> retval = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1,filtr);
        return retval;
    }

    public ArrayAdapter<Category> getCategoriesAdapter(Context context){
        ArrayAdapter<Category> retval = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, categories);
        return retval;
    }

    public ArrayAdapter<UnitOfMeasure> getUnitAdapter(Context context){
        ArrayAdapter<UnitOfMeasure> retval = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, units);
        return retval;
    }

    public void updateProduct(Context p){
        DatabaseAccess dba = new DatabaseAccess(p);
        dba.open();
        UpdateBuilder ub = new UpdateBuilder("Product");
        try {
            if (!name.contentEquals(product.getName())) {
                ub.addFieldAndData("Name", name, null);
            }
            if(!description.contentEquals(product.getDescription())){
                ub.addFieldAndData("Description", description, null);
            }
            if(unitId != product.getDefaultUnitId()){
                ub.addFieldAndData("DefaultUnit_id", unitId, null);
            }
            dba.executeQuery(ub.getQry("WHERE id = " + product.getId() + ";"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dba.close();
        }
    }

    public void storeProduct(Context context) {
        DatabaseAccess dba = new DatabaseAccess(context);
        dba.open();
        try{
            InsertBuilder ib = new InsertBuilder("Product", "Name", name, null);
            if(description != null) {
                if (!description.contentEquals("")) {
                    ib.addFieldAndData("Description", description, null);
                }

            }
            ib.addFieldAndData("DefaultUnit_id", unitId, null);
            ib.addFieldAndData("Category_id", categoryId, null);
            dba.executeQuery(ib.getQry());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dba.close();
        }
    }


}
