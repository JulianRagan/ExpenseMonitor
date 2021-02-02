package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jrcw.expensemonitor.R;
import com.jrcw.expensemonitor.containers.Place;

import java.util.List;

public class AddPlacePopupController {
    View view;
    PopupWindow window;
    AddPlacePopupModel model;
    Context context;

    public AddPlacePopupController(View view, PopupWindow w, Context c, List<Place>places){
        this.view = view;
        this.model = new AddPlacePopupModel(places);
        this.window = w;
        this.context = c;
        initControlls();
    }

    private void initControlls(){
        ((EditText)view.findViewById(R.id.txtNamePlace)).addTextChangedListener(new NameTextWatcher());
        ((EditText)view.findViewById(R.id.txtAddDescriptionPlace)).addTextChangedListener(new DescriptionTextWatcher());
        ((EditText)view.findViewById(R.id.txtRoadPlace)).addTextChangedListener(new StreetTextWatcher());
        ((EditText)view.findViewById(R.id.txtNumberPlace)).addTextChangedListener(new NumberTextWatcher());
        ((EditText)view.findViewById(R.id.txtCityPlace)).addTextChangedListener(new CityTextWatcher());
        ((EditText)view.findViewById(R.id.txtCountryPlace)).addTextChangedListener(new CountryTextWatcher());
        ((Button)view.findViewById(R.id.btnAddAdditionPlace)).setOnClickListener(new AddPlaceOnClickListener());
        ((Button)view.findViewById(R.id.btnCancelAdditionPlace)).setOnClickListener(new CancelOnClickListener());
        ((ListView)view.findViewById(R.id.lvAdditionPlace)).setAdapter(model.getPlacesAdapter(context));

    }

    private void importPlace(Place p){
        model.pupulateFromPlace(p);
        ((EditText)view.findViewById(R.id.txtNamePlace)).setText(p.getName());
        ((EditText)view.findViewById(R.id.txtAddDescriptionPlace)).setText(p.getDescription());
        ((EditText)view.findViewById(R.id.txtRoadPlace)).setText(p.getStreet());
        ((EditText)view.findViewById(R.id.txtNumberPlace)).setText(p.getStreetNumber());
        ((EditText)view.findViewById(R.id.txtCityPlace)).setText(p.getCity());
        ((EditText)view.findViewById(R.id.txtCountryPlace)).setText(p.getCountry());
        ((Button)view.findViewById(R.id.btnAddAdditionPlace)).setText("Zachowaj");
        ((Button)view.findViewById(R.id.btnCancelAdditionPlace)).setText("Odrzuć");
    }
    private void clear(){
        model.clear();
        ((EditText)view.findViewById(R.id.txtNamePlace)).setText("");
        ((EditText)view.findViewById(R.id.txtAddDescriptionPlace)).setText("");
        ((EditText)view.findViewById(R.id.txtRoadPlace)).setText("");
        ((EditText)view.findViewById(R.id.txtNumberPlace)).setText("");
        ((EditText)view.findViewById(R.id.txtCityPlace)).setText("");
        ((EditText)view.findViewById(R.id.txtCountryPlace)).setText("");
        ((Button)view.findViewById(R.id.btnAddAdditionPlace)).setText("Dodaj");
        ((Button)view.findViewById(R.id.btnCancelAdditionPlace)).setText("Anuluj");
    }

    private void toastError(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    private class AddPlaceOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String mode = ((String)((Button)v).getText()).toLowerCase();
            switch(mode){
                case "dodaj":
                    if(!model.placeExists()){
                        if(model.isMinimalDataSet()){
                            model.storePlace(context);
                            window.dismiss();
                        }else{
                            toastError("Należy podać nazwę miejsca");
                        }
                    }else{
                        toastError("Podana nazwa już istnieje w systemie");
                    }
                    break;
                case "zachowaj":
                    if(model.isMinimalDataSet()){
                        model.updatePlace(context);
                        window.dismiss();
                    }
                    break;
            }

        }
    }

    private class ListOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Place p = (Place)parent.getItemAtPosition(position);
            view.animate().setDuration(200l).alpha(0.0f);

        }
    }

    private class CancelOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String mode = ((String)((Button)v).getText()).toLowerCase();
            switch(mode){
                case "odrzuć":
                    clear();
                    break;
                case "anuluj":
                    window.dismiss();
                    break;
            }

        }
    }

    private class NameTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            model.setName(s.toString());
        }
    }

    private class DescriptionTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            model.setDescription(s.toString());
        }
    }

    private class StreetTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            model.setStreet(s.toString());
        }
    }

    private class NumberTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            model.setNumber(s.toString());
        }
    }

    private class CityTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            model.setCity(s.toString());
        }
    }

    private class CountryTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            model.setCountry(s.toString());
        }
    }
}
