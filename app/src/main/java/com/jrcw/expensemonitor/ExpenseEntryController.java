package com.jrcw.expensemonitor;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import com.jrcw.expensemonitor.containers.Category;
import com.jrcw.expensemonitor.containers.Currency;
import com.jrcw.expensemonitor.containers.DetailContent;
import com.jrcw.expensemonitor.containers.DetailEntryAction;
import com.jrcw.expensemonitor.containers.EntryAction;
import com.jrcw.expensemonitor.containers.Place;
import com.jrcw.expensemonitor.containers.UpdateDataListener;
import com.jrcw.expensemonitor.popupWindows.AddCategoryPopupController;
import com.jrcw.expensemonitor.popupWindows.AddDetailPopupController;
import com.jrcw.expensemonitor.popupWindows.AddPlacePopupController;
import com.jrcw.expensemonitor.popupWindows.AddProductPopupController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class ExpenseEntryController {
    private ExpenseEntryActivity view;
    private ExpenseEntryModel model;
    private DetailContent placeHolder;

    public ExpenseEntryController(ExpenseEntryActivity view, ExpenseEntryModel model) {
        this.view = view;
        this.model = model;
        initControls();
        Monitor.getInstance(view);
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
        ((CheckBox)view.findViewById(R.id.detailLater)).setOnClickListener(new LaterOnClickListener());
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


    private void handleMinimalaDataSetExceptions(Exception e) {
        switch (e.getMessage()){
            case "Bad hours field":
                toastError("Nie udało się przetworzyć godziny, sprawdź czas");
                break;
            case "Bad minutes field":
                toastError("Nie udało się przetworzyć minut, sprawdź czas");
                break;
            case "Bad seconds field":
                toastError("Nie udało się przetworzyć sekund, sprawdź czas");
                break;
            case "Bad day field":
                toastError("Nie udało się przetworzyć dnia w dacie");
                break;
            case "Bad month field":
                toastError("Nie udało się przetworzyć miesiąca w dacie");
                break;
            case "Bad year field":
                toastError("Nie udało się przetworzyć roku w dacie");
                break;
            case "separator":
                toastError("Nieprawdidłowy czas lub data");
                break;
            case "No date":
                toastError("Brak daty");
                break;
            case "Bad place":
                toastError("O podanym czasie jest już zachowany zakup w innym miejscu");
                break;
            case "Exists":
                toastError("Dla podanego czasu jest już zachowany zakup");
                break;
            default:
                toastError("Nieznany błąd");
                e.printStackTrace();
        }
    }


    private void showPopupWindow(PopupWindowType pwt, View v){
        LayoutInflater inflater = (LayoutInflater) view.getSystemService(LAYOUT_INFLATER_SERVICE);
        View pview;
        switch(pwt){
            case PLACE:
                pview = inflater.inflate(R.layout.addition_place_simple, null);
                break;
            case CATEGORY:
                pview = inflater.inflate(R.layout.addition_category, null);
                break;
            case DETAILS:
                pview = inflater.inflate(R.layout.details_panel, null);
                break;
            case PRODUCT:
                pview = inflater.inflate(R.layout.addition_products, null);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + pwt);
        }
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
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
            case DETAILS:
                AddDetailPopupController dc = new AddDetailPopupController(pview, v, popupWindow,
                        view, model.getTimeOfTransaction());
                dc.setDetailEntryAction(new DetailAction());
                if(placeHolder != null) dc.restoreFromDetailContent(placeHolder);
                break;
            case PRODUCT:
                AddProductPopupController pc = new AddProductPopupController(pview, popupWindow, view,
                        model.getCategories(), model.getUnits(), model.getProducts());
                pc.setUpdateDataListener(new PopupUpdateDataListener());
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
        popup.setOnMenuItemClickListener(new ActivitySwitch());
        popup.getMenu().findItem(R.id.menuExpenses).setEnabled(false);
        popup.show();
    }

    public void clear(){
        model.clear();
        ((EditText)view.findViewById(R.id.editTextDate)).setText("");
        ((EditText)view.findViewById(R.id.editTextTime)).setText("");
        ((EditText)view.findViewById(R.id.etAmount)).setText("");
        ((EditText)view.findViewById(R.id.etDescription)).setText("");
        if(((CheckBox)view.findViewById(R.id.detailLater)).isChecked()){
            ((CheckBox)view.findViewById(R.id.detailLater)).setChecked(false);
            ((Button)view.findViewById(R.id.btnEnterDetail)).setEnabled(true);
        }

    }

    private class ActivitySwitch implements PopupMenu.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            runActivity(view.getResources().getResourceName(item.getItemId()));
            return false;
        }

        private void runActivity(String name){
            Intent myIntent = null;
            switch (name){
                case "com.jrcw.expensemonitor:id/menuExpenses":
                    myIntent = new Intent(view, ExpenseEntryActivity.class);
                    break;
                case "com.jrcw.expensemonitor:id/menuLimits":
                    myIntent = new Intent(view, LimitsActivity.class);
                    break;
                case "com.jrcw.expensemonitor:id/menuStatistics":
                    myIntent = new Intent(view, Statistics.class);
                    break;
                default:
                    toastError("Nieznana funkcja menu");
            }
            if(myIntent != null){
                view.startActivity(myIntent);
            }
        }
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
            try {
                if(model.isMinimalDataSetForStoring()){
                    model.storeExpense();
                    clear();
                }
            } catch (Exception e) {
                handleMinimalaDataSetExceptions(e);
            }
        }
    }

    private class LaterOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            boolean later = ((CheckBox)v).isChecked();
            model.setLater(later);
            if(later){
                ((Button)view.findViewById(R.id.btnEnterDetail)).setEnabled(false);
            }else{
                ((Button)view.findViewById(R.id.btnEnterDetail)).setEnabled(true);
            }
        }
    }

    private class EnterDetailsOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            try {
                if(model.isMinimalDataSetForDetails()){
                    model.storeForDetails();
                    showPopupWindow(PopupWindowType.DETAILS, v);
                }
            } catch (Exception e) {
                handleMinimalaDataSetExceptions(e);
            }
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
                        int day = Integer.parseInt(date.substring(0, i));
                        if(day > 31){
                            toastError("Miesiąc ma maksymalnie 31 dni");
                            s.clear();
                            break;
                        }else if(day < 1){
                            toastError("Miesiąc zaczyna się od 1-ego");
                            s.clear();
                            break;
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
                            break;
                        }
                    }
                }
            }
            model.setDateString(s.toString());
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
                        String[] c;
                        if(ds.contentEquals(".")){
                            c = date.split("\\.");
                        }else {
                            c = date.split(ds);
                        }
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
            int ssl = 0;

            if (time.length() > 0) hh = true;
            for(int i = 0; i < time.length(); i++) {
                char ch = time.charAt(i);
                if (isSeparator(ch)) {
                    if (hh && !mm) {
                        sep = ch;
                        fsl = i;
                        String str = time.substring(0, i);
                        int hour = Integer.parseInt(time.substring(0, i));
                        if (hour > 23) {
                            toastError("Podaj godzinę w przedziale 0 - 23");
                            s.clear();
                            break;
                        } else if (hour < 0) {
                            toastError("Podaj godzinę w przedziale 0 - 23");
                            s.clear();
                            break;
                        } else {
                            mm = true;
                        }
                    } else if (hh && mm && !ss) {
                        if (sep != ch) {
                            time.replace(ch, sep);
                            s.clear();
                            s.insert(0, time);
                            break;
                        }
                        int minute = Integer.parseInt(time.substring(fsl+1, i));
                        if (minute > 59 || minute < 0) {
                            s.clear();
                            s.insert(0, time.substring(0, fsl + 1));
                            toastError("Podaj minuty w zakresie 0 - 59");
                            break;
                        } else {
                            ss = true;
                            ssl = i;
                        }
                    } else if (hh && mm && ss) {
                        s.clear();
                        s.insert(0, time.substring(0, i));
                        toastError("Niepoprawny czas");
                        break;
                    }
                }
                if(hh && mm && ss){
                    if((i - 1 - ssl)>0){
                        int second = Integer.parseInt(time.substring(ssl+1, i+1));
                        if(second > 59 || second < 0){
                            s.clear();
                            s.insert(0, time.substring(0, ssl+1));
                            toastError("Podaj minuty w zakresie 0 - 59");
                            break;
                        }
                    }
                }
            }
            model.setTimeString(s.toString());
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
            try{
                if(!s.toString().contentEquals("")) {
                    double d = Double.parseDouble(s.toString());
                    model.setExpenditureTotal(d);
                }
            }catch (NumberFormatException e){
                toastError("Nieprawidłowa kwota");
            }
        }
    }

    private class TextDescriptionWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            model.setDescription(s.toString());
        }
    }

    private class PlaceSelectedItemListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int pid = ((Place)parent.getItemAtPosition(position)).getId();
            model.setPlaceId(pid);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class CategorySelectedItemListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int cid = ((Category)parent.getItemAtPosition(position)).getId();
            model.setCategoryId(cid);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class CurrencySelectedItemListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int cid = ((Currency)parent.getItemAtPosition(position)).getId();
            model.setCurrencyId(cid);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class DetailAction implements DetailEntryAction {

        @Override
        public void entryAction(EntryAction ea, DetailContent dc) {
            switch (ea){
                case ADD_PRODUCT:
                    placeHolder = dc;
                    showPopupWindow(PopupWindowType.PRODUCT, dc.getView());
                    break;
                case ADD_CATEGORY:
                    placeHolder = dc;
                    showPopupWindow(PopupWindowType.CATEGORY, dc.getView());
                    break;
                case END_ENTRY:
                    placeHolder = null;
                    clear();
            }
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
                    if(placeHolder != null){
                        showPopupWindow(PopupWindowType.DETAILS, placeHolder.getView());
                    }
                    break;
                case PRODUCT:
                    showPopupWindow(PopupWindowType.DETAILS, placeHolder.getView());
            }
        }
    }

}
