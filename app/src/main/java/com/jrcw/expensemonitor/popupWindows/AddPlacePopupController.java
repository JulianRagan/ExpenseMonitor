package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jrcw.expensemonitor.R;

public class AddPlacePopupController {
    View view;
    PopupWindow window;
    AddPlacePopupModel model;
    Context context;

    public AddPlacePopupController(View view, AddPlacePopupModel model, PopupWindow w, Context c){
        this.view = view;
        this.model = model;
        this.window = w;
        this.context = c;
        initControlls();
    }

    private void initControlls(){

    }

    private class AddPlaceOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(model.isMinimalDataSet()){
                model.storePlace();
                window.dismiss();
            }else{
                Toast.makeText(context, "Należy podać nazwę miejsca", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class CancelOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            window.dismiss();
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
