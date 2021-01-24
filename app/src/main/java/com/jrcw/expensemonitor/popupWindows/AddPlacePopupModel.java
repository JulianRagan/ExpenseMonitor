package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;

import com.jrcw.expensemonitor.db.DatabaseAccess;

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
        //TODO dodać zapis danych
    }
}
