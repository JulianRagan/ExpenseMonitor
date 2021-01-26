package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;

import com.jrcw.expensemonitor.db.DatabaseAccess;
import com.jrcw.expensemonitor.db.InsertBuilder;

public class AddPlacePopupModel {
    DatabaseAccess dba;
    private String name;
    private String description;
    private String street;
    private String city;
    private String country;
    private String number;

    public AddPlacePopupModel(Context c){
        dba = new DatabaseAccess(c);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isMinimalDataSet(){
        if(name != null){
            if(!name.contentEquals("")){
                return true;
            }
        }
        return false;
    }

    public void storePlace(){
        dba.open();
        try{
            InsertBuilder ib = new InsertBuilder("Place", "Name", name, null);
            if(description != null) {
                if (!description.contentEquals("")) {
                    ib.addFieldAndData("Description", description, null);
                }
            }
            if(street != null) {
                if (!street.contentEquals("")) {
                    ib.addFieldAndData("StreetName", street, null);
                }
            }
            if(number != null){
                if(!number.contentEquals("")){
                    ib.addFieldAndData("StreetNumber", number, null);
                }
            }
            if(city != null){
                if(!city.contentEquals("")){
                    ib.addFieldAndData("CityName", city, null);
                }
            }
            if(country != null){
                if(!country.contentEquals("")){
                    ib.addFieldAndData("CountryName", country, null);
                }
            }
            dba.executeQuery(ib.getQry());
            dba.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
