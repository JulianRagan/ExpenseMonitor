package com.jrcw.expensemonitor;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import com.jrcw.expensemonitor.containers.UpdateDataListener;
import com.jrcw.expensemonitor.popupWindows.AddCategoryPopupController;
import com.jrcw.expensemonitor.popupWindows.AddPlacePopupController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


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
        ((EditText)view.findViewById(R.id.editTextDate)).setOnFocusChangeListener(new DateFocusListener());
        ((EditText)view.findViewById(R.id.editTextTime)).addTextChangedListener(new TextTimeWatcher());
        ((EditText)view.findViewById(R.id.etAmount)).addTextChangedListener(new TextAmountWatcher());
        ((EditText)view.findViewById(R.id.etDescription)).addTextChangedListener(new TextDescriptionWatcher());
        ((Spinner)view.findViewById(R.id.spPlace)).setOnItemSelectedListener(new PlaceSelectedItemListener());
        setAdapterPlaces();
        ((Spinner)view.findViewById(R.id.spCategory)).setOnItemSelectedListener(new CategorySelectedItemListener());
        setAdapterCategories();
        ((Spinner)view.findViewById(R.id.spCurrency)).setOnItemSelectedListener(new CurrencySelectedItemListener());
        setAdapterCurrencies();
    }

    private void setAdapterPlaces(){
        ((Spinner)view.findViewById(R.id.spPlace)).setAdapter(model.getPlacesAdapter(view));
    }

    private  void setAdapterCategories(){
        ((Spinner)view.findViewById(R.id.spCategory)).setAdapter(model.getCategoryAdapter(view));
    }

    private void setAdapterCurrencies(){
        ((Spinner)view.findViewById(R.id.spCurrency)).setAdapter(model.getCurrencyAdapter(view));
    }

    private void toastError(String msg){
        Toast.makeText(view, msg, Toast.LENGTH_LONG).show();
    }

    private void showPopupWindow(PopupWindowType pwt, View v){
        LayoutInflater inflater = (LayoutInflater) view.getSystemService(LAYOUT_INFLATER_SERVICE);
        View pview = null;
        switch(pwt){
            case PLACE:
                //pview = inflater.inflate(R.layout.addition_place, null);
                pview = inflater.inflate(R.layout.addition_place_simple, null);
                break;
            case CATEGORY:
                pview = inflater.inflate(R.layout.addition_category, null);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + pwt);
        }
//        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
//        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(pview, width, height, focusable);
        switch(pwt){
            case PLACE:
                AddPlacePopupController ctrl = new AddPlacePopupController(pview, popupWindow, view,
                        model.getPlaces());
                ctrl.setUpdateDataListener(new PopupUpdateDataListener());
                break;
            case CATEGORY:
                AddCategoryPopupController cc = new AddCategoryPopupController(pview, popupWindow,
                        view, model.getCategories());
                cc.setUpdateDataListener(new PopupUpdateDataListener());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + pwt);
        }
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0,0);
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
            showPopupWindow(PopupWindowType.PLACE, v);
        }
    }

    private class AddCategoryOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            showPopupWindow(PopupWindowType.CATEGORY, v);
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

        private boolean isSeparator(char a){
            if(a == '-' || a == '/' || a == '.') return true;
            return false;
        }

        @Override
        public void afterTextChanged(Editable s) {
            String date = s.toString();
            boolean dd = false;
            boolean mm = false;
            boolean yy = false;
            char sep = 0;
            int fsl = 0;

            if(date.length() > 0) dd = true;
            for(int i = 0; i < date.length(); i++){
                char ch = date.charAt(i);
                if(isSeparator(ch)){
                    if(dd && !mm){
                        sep = ch;
                        fsl = i;
                        int day = Integer.parseInt(date.substring(0, i-1));
                        if(day > 31){
                            toastError("Miesiąc ma maksymalnie 31 dni");
                            s.clear();
                        }else if(day < 1){
                            toastError("Miesiąc zaczyna się od 1-ego");
                            s.clear();
                        }else{
                            mm = true;
                        }
                    }else if(dd && mm){
                        if(sep != ch){
                            date.replace(ch, sep);
                            s.clear();
                            s.insert(0, date);
                        }
                        yy = true;
                    }else if(dd && mm && yy){
                        s.clear();
                        s.insert(0, date.substring(0, i-1));
                        toastError("Niepoprawna data");
                        break;
                    }
                }
                if(dd && mm && !yy){
                    if(i-1 > fsl) {
                        int month = Integer.parseInt(date.substring(fsl+1, i+1));
                        if (month > 12) {
                            toastError("Rok ma maksymalnie 12 miesięcy");
                        } else if (fsl - i == 2) {
                            if (month < 1) toastError("Nieprawidłowy miesiąc");
                            s.clear();
                            s.insert(0, date.substring(0, fsl));
                        }
                    }
                }
            }
        }
    }

    private class DateFocusListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                String date = ((EditText) v).getText().toString();
                if (!date.contentEquals("")) {
                    String ds = getSeparator(date);
                    if (ds.contentEquals("")) {
                        toastError("Nieprawidłowa data");
                    } else {
                        String[] c = date.split(ds);
                        if (c.length != 3) {
                            toastError("Nieprawidłowa data");
                        } else {
                            if (c[2].length() == 2) {
                                SimpleDateFormat sdfyy = new SimpleDateFormat("yyyy");
                                c[2] = sdfyy.format(new Date()).substring(0, 1) + c[2];
                            } else if (c[2].length() != 4) {
                                toastError("Nieprawidłowa data");
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("dd" + ds + "MM" + ds + "yyyy");
                            String newDate = c[0] + ds + c[1] + ds + c[2];
                            try {
                                Date d = sdf.parse(newDate);
                                if (d.after(new Date())) {
                                    toastError("Podana data jest w przyszłości");
                                }
                            } catch (ParseException e) {
                                toastError("Nieprawidłowa data");
                            }
                        }
                    }
                }
            }
        }

        private String getSeparator(String date){
            if(date.contains(".")){
                return ".";
            }
            if(date.contains("-")){
                return "-";
            }
            if(date.contains("/")){
                return "/";
            }
            return "";
        }
    }

    private class TextTimeWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String time = s.toString();
            boolean hh = false;
            boolean mm = false;
            boolean ss = false;
            char sep = 0;
            int fsl = 0;

            String date = s.toString();
            boolean dd = false;
            boolean yy = false;

            if (time.length() > 0) hh = true;
            for(int i = 0; i < date.length(); i++) {
                char ch = date.charAt(i);
                if (isSeparator(ch)) {
                    if (hh && !mm) {
                        sep = ch;
                        fsl = i;
                        int hour = Integer.parseInt(time.substring(0, i - 1));
                        if (hour > 23 || hour < 0) {
                            toastError("Podaj godzinę w przedziale 0 - 23");
                            s.clear();
                        } else {
                            mm = true;
                        }
                    } else if (dd && mm) {
                        if (sep != ch) {
                            date.replace(ch, sep);
                            s.clear();
                            s.insert(0, time);
                        }
                        int minute = Integer.parseInt(time.substring(fsl+1, i-1));
                        if(minute > 59 || minute < 0){
                            s.clear();
                            s.insert(0, time.substring(0, fsl));
                            toastError("Podaj minuty w zakresie 0 - 59");
                        }else {
                            ss = true;
                        }
                    } else if (hh && mm && ss) {
                        s.clear();
                        s.insert(0, time.substring(0, i - 1));
                        toastError("Niepoprawny czas");
                        break;
                    }
                }
            }
        }

        private boolean isSeparator(char a) {
            if (a == ':' || a == ',' || a == '.') return true;
            return false;
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

    private class PopupUpdateDataListener implements UpdateDataListener{

        @Override
        public void dataUpdated(PopupWindowType source) {
            switch(source){
                case PLACE:
                    model.updatePlaces();
                    setAdapterPlaces();
                    break;
                case CATEGORY:
                    model.updateCategories();
                    setAdapterCategories();
                    break;
                default:
                    //TODO obsługa błędu
            }
        }
    }

}
