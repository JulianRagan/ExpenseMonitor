package com.jrcw.expensemonitor;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.PopupMenu;


public class ExpenseEntryController {
    private ExpenseEntryActivity view;
    private ExpenseEntryModel model;

    public ExpenseEntryController(ExpenseEntryActivity view, ExpenseEntryModel model) {
        this.view = view;
        this.model = model;
        initControls();
    }

    private void initControls(){
        ((Button)view.findViewById(R.id.btnFunction)).setOnClickListener(new FunctionMenuOnClickListener());
        ((Button)view.findViewById(R.id.btnAddPlace)).setOnClickListener(new AddPlaceOnClickListener());
        ((Button)view.findViewById(R.id.btnAddCategory)).setOnClickListener(new AddCategoryOnClickListener());
        ((Button)view.findViewById(R.id.btnAddExpense)).setOnClickListener(new AddExpenseOnClickListener());
        ((Button)view.findViewById(R.id.btnEnterDetail)).setOnClickListener(new EnterDetailsOnClickListener());
        ((EditText)view.findViewById(R.id.editTextDate)).addTextChangedListener(new TextDateWatcher());
        ((EditText)view.findViewById(R.id.editTextTime)).addTextChangedListener(new TextTimeWatcher());
        ((EditText)view.findViewById(R.id.etAmount)).addTextChangedListener(new TextAmountWatcher());
        ((EditText)view.findViewById(R.id.etDescription)).addTextChangedListener(new TextDescriptionWatcher());
        ((Spinner)view.findViewById(R.id.spPlace)).setOnItemSelectedListener(new PlaceSelectedItemListener());
        ((Spinner)view.findViewById(R.id.spCategory)).setOnItemSelectedListener(new CategorySelectedItemListener());
        ((Spinner)view.findViewById(R.id.spCurrency)).setOnItemSelectedListener(new CurrencySelectedItemListener());
    }

    public void showPopup(View v, ExpenseEntryActivity view){
        PopupMenu popup = new PopupMenu(view, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_function, popup.getMenu());
        popup.show();
    }

    private class FunctionMenuOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            showPopup(v, view);
        }
    }

    private class AddPlaceOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }

    private class AddCategoryOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }

    private class AddExpenseOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }

    private class EnterDetailsOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }

    private class TextDateWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class TextTimeWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class TextAmountWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class TextDescriptionWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class PlaceSelectedItemListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class CategorySelectedItemListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class CurrencySelectedItemListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
