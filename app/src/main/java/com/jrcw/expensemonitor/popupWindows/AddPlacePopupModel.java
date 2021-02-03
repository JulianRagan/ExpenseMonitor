package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.jrcw.expensemonitor.containers.Place;
import com.jrcw.expensemonitor.db.DatabaseAccess;
import com.jrcw.expensemonitor.db.InsertBuilder;
import com.jrcw.expensemonitor.db.UpdateBuilder;

import java.util.List;

public class AddPlacePopupModel {
    private List<Place> places;
    private String name;
    private String description;
    private String street;
    private String city;
    private String country;
    private String number;
    private Place place;

    public AddPlacePopupModel(List<Place> places){
        this.places = places;
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

    public void pupulateFromPlace(Place p){
        name = p.getName();
        description = p.getDescription();
        street = p.getStreet();
        number = p.getStreetNumber();
        city = p.getCity();
        country = p.getCountry();
        place = p;
    }

    public void clear(){
        name = "";
        description = "";
        street = "";
        number = "";
        city = "";
        country = "";
        place = null;
    }

    public boolean isMinimalDataSet(){
        if(name != null){
            if(!name.contentEquals("")){
                return true;
            }
        }
        return false;
    }

    public boolean placeExists(){
        boolean retval = false;
        if(name != null) {
            if(!name.contentEquals("")) {
                for (Place p : places) {
                    if (p.getName().contentEquals(name)) {
                        retval = true;
                    }
                }
            }
        }
        return retval;
    }

    public ArrayAdapter<Place> getPlacesAdapter(Context context){
        ArrayAdapter<Place> retval = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, places);
        return retval;
    }

    public void storePlace(Context c){
        DatabaseAccess dba = new DatabaseAccess(c);
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
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dba.close();
        }
    }

    public void updatePlace(Context c){
        DatabaseAccess dba = new DatabaseAccess(c);
        dba.open();
        UpdateBuilder ub = new UpdateBuilder("Place");
        try {
            if (!name.contentEquals(place.getName())) {
                ub.addFieldAndData("Name", name, null);
            }
            if(!description.contentEquals(place.getDescription())){
                ub.addFieldAndData("Description", description, null);
            }
            if(!street.contentEquals(place.getStreet())){
                ub.addFieldAndData("StreetName", street, null);
            }
            if(!number.contentEquals(place.getStreetNumber())){
                ub.addFieldAndData("StreetNumber", number, null);
            }
            if(!city.contentEquals(place.getCity())){
                ub.addFieldAndData("CityName", city, null);
            }
            if(!country.contentEquals(place.getCountry())){
                ub.addFieldAndData("CountryName", country, null);
            }
            dba.executeQuery(ub.getQry("WHERE id = " + place.getId() + ";"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dba.close();
        }
    }
}
