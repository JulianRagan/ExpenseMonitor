package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.jrcw.expensemonitor.containers.Category;
import com.jrcw.expensemonitor.containers.Place;
import com.jrcw.expensemonitor.db.DatabaseAccess;
import com.jrcw.expensemonitor.db.InsertBuilder;
import com.jrcw.expensemonitor.db.UpdateBuilder;

import java.util.List;

public class AddCategoryPopupModel {
    private String name;
    private String description;
    private Category category;
    private List<Category> categories;

    public AddCategoryPopupModel(List<Category> categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void populateFromCategory(Category category){
        this.category = category;
        name = category.getName();
        description = category.getDescription();
    }

    public void clear(){
        name = "";
        description = "";
        category = null;
    }

    public boolean isMinimalDataSet(){
        if(name != null){
            if(!name.contentEquals("")){
                return true;
            }
        }
        return false;
    }

    public boolean categoryExists(){
        boolean retval = false;
        if(name != null) {
            if(!name.contentEquals("")) {
                for (Category c : categories) {
                    if (c.getName().contentEquals(name)) {
                        retval = true;
                    }
                }
            }
        }
        return retval;
    }

    public ArrayAdapter<Category> getCategoriesAdapter(Context context){
        ArrayAdapter<Category> retval = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, categories);
        return retval;
    }

    public void storeCategory(Context c){
        DatabaseAccess dba = new DatabaseAccess(c);
        dba.open();
        try{
            InsertBuilder ib = new InsertBuilder("Category", "Name", name, null);
            if(description != null) {
                if (!description.contentEquals("")) {
                    ib.addFieldAndData("Description", description, null);
                }
            }
            dba.executeQuery(ib.getQry());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dba.close();
        }
    }

    public void updateCategory(Context c){
        DatabaseAccess dba = new DatabaseAccess(c);
        dba.open();
        UpdateBuilder ub = new UpdateBuilder("Category");
        try {
            if (!name.contentEquals(category.getName())) {
                ub.addFieldAndData("Name", name, null);
            }
            if(!description.contentEquals(category.getDescription())){
                ub.addFieldAndData("Description", description, null);
            }
            dba.executeQuery(ub.getQry("WHERE id = " + category.getId() + ";"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dba.close();
        }
    }

}
