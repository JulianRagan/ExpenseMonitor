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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import com.jrcw.expensemonitor.containers.Category;
import com.jrcw.expensemonitor.containers.Currency;
import com.jrcw.expensemonitor.containers.Product;
import com.jrcw.expensemonitor.containers.UpdateDataListener;
import com.jrcw.expensemonitor.popupWindows.AddCategoryPopupController;
import com.jrcw.expensemonitor.popupWindows.AddProductPopupController;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class LimitsActivityController {
    private LimitsActivity view;
    private LimitsActivityModel model;

    public LimitsActivityController(LimitsActivity view, LimitsActivityModel model) {
        this.view = view;
        this.model = model;
        initView();
    }

    private void initView() {
        ((Button) view.findViewById(R.id.btnAddCategory)).setOnClickListener(new KategorieOnClickListener());
        ((Button) view.findViewById(R.id.btnAddProduct)).setOnClickListener(new ProduktyOnClickListener());
        ((CheckBox) view.findViewById(R.id.ckbCategory)).setOnClickListener(new CheckBoxListener());
        ((Spinner) view.findViewById(R.id.spCategory)).setOnItemSelectedListener(new CategorySelectedItemListener());
        setAdapterCategories();
        ((Spinner) view.findViewById(R.id.spProduct)).setOnItemSelectedListener(new ProductsSelectedItemListener());
        setAdapterProducts();
        ((Spinner) view.findViewById(R.id.spCurrencyLimits)).setOnItemSelectedListener(new CurrencySelectedItemListener());
        setAdapterCurrencies();
        ((SeekBar) view.findViewById(R.id.seekFundusze)).setOnSeekBarChangeListener(new FunduszeSeekBarListener());
        ((SeekBar) view.findViewById(R.id.seekSztuki)).setOnSeekBarChangeListener(new SztukiSeekBarListener());
        ((EditText) view.findViewById(R.id.etFunds)).addTextChangedListener(new FunduszeTextChangedListener());
        ((EditText) view.findViewById(R.id.etQuantity)).addTextChangedListener(new SztukiTextChangedListener());
        ((EditText) view.findViewById(R.id.etFrom)).addTextChangedListener(new OdEditTextListener());
        ((EditText) view.findViewById(R.id.etTo)).addTextChangedListener(new DoEditTextListener());
        ((Button) view.findViewById(R.id.btnSet)).setOnClickListener(new UstawOnClickListener());
        ((Button) view.findViewById(R.id.btnCancel)).setOnClickListener(new CancelOnClickListener());
        ((Button) view.findViewById(R.id.btnFunction2)).setOnClickListener(new ButtonFunctionListener());
        clear();
    }

    private void setAdapterCategories() {
        ((Spinner) view.findViewById(R.id.spCategory)).setAdapter(model.getCategoryAdapter(view));
    }

    private void setAdapterCurrencies() {
        ((Spinner) view.findViewById(R.id.spCurrencyLimits)).setAdapter(model.getCurrencyAdapter(view));
    }

    private void setAdapterProducts() {
        String categoryName = ((Category) ((Spinner) view.findViewById(R.id.spCategory))
                .getSelectedItem()).getName();
        ((Spinner) view.findViewById(R.id.spProduct)).setAdapter(
                model.getProductsAdapter(view, categoryName));
    }

    private void resetSeekBars(){
        Monitor m = Monitor.getInstance(view);
        ((SeekBar) view.findViewById(R.id.seekFundusze)).setMax(m.getMaxQuota());
        ((SeekBar) view.findViewById(R.id.seekSztuki)).setMax(m.getMaxQuantity());
    }

    private void clear(){
        ((CheckBox) view.findViewById(R.id.ckbCategory)).setChecked(false);
        ((EditText) view.findViewById(R.id.etFunds)).setText("0");
        ((EditText) view.findViewById(R.id.etQuantity)).setText("0");
        ((EditText) view.findViewById(R.id.etFrom)).setText("");
        ((EditText) view.findViewById(R.id.etTo)).setText("");
        resetSeekBars();
    }

    private void handleMinimalaDataSetExceptions(Exception e) {
        switch (e.getMessage()) {
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
            case "No from date":
                toastError("Brak daty od");
                break;
            case "No to date":
                toastError("Brak daty do");
                break;
            case "Bad from date":
                toastError("Nieprawidłowa data w polu od");
                break;
            case "Bad to date":
                toastError("Nieprawidłowa data w polu do");
                break;
            case "Equal dates":
                toastError("Daty są sobie równe");
                break;
            case "Limit has not been set":
                toastError("Ustal jeden z limitów");
                break;
            default:
                toastError("Nieznany błąd");
                break;
        }
    }

    private boolean isSeparator(char a){
        if(a == '-' || a == '/' || a == '.') return true;
        return false;
    }

    private void checkDateString(Editable s){
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
    }

    private void toastError(String msg) {
        Toast.makeText(view, msg, Toast.LENGTH_LONG).show();
    }

    private void showPopupWindow(PopupWindowType pwt, View v) {
        LayoutInflater inflater = (LayoutInflater) view.getSystemService(LAYOUT_INFLATER_SERVICE);
        View pview;
        switch (pwt) {
            case PRODUCT:
                pview = inflater.inflate(R.layout.addition_products, null);
                break;
            case CATEGORY:
                pview = inflater.inflate(R.layout.addition_category, null);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + pwt);
        }
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(pview, width, height, focusable);
        switch (pwt) {
            case PRODUCT:
                AddProductPopupController ctrl = new AddProductPopupController(pview, popupWindow,
                        view, model.getCategories(), model.getUnits(), model.getProducts());
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
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    public void showPopup(View v, LimitsActivity view) {
        PopupMenu popup = new PopupMenu(view, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_function, popup.getMenu());
        popup.getMenu().findItem(R.id.menuLimits).setEnabled(false);
        popup.setOnMenuItemClickListener(new ActivitySwitch());
        popup.show();
    }

    private class ActivitySwitch implements PopupMenu.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            runActivity(view.getResources().getResourceName(item.getItemId()));
            return false;
        }

        private void runActivity(String name) {
            Intent myIntent = null;
            switch (name) {
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
            if (myIntent != null) {
                view.startActivity(myIntent);
            }
        }
    }


    private class KategorieOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            showPopupWindow(PopupWindowType.CATEGORY, v);
            ((EditText)view.findViewById(R.id.etFunds)).setText("");
            ((EditText)view.findViewById(R.id.etQuantity)).setText("");
            ((EditText)view.findViewById(R.id.etFrom)).setText("");
            ((EditText)view.findViewById(R.id.etTo)).setText("");
        }
    }

    private class ButtonFunctionListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showPopup(v, view);
        }
    }

    private class ProduktyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            {
                showPopupWindow(PopupWindowType.PRODUCT, v);
            }
        }
    }

    private class UstawOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                if (model.isMinimalDataSet()) {
                    model.storeLimit();
                    clear();
                }
            } catch (Exception e) {
                handleMinimalaDataSetExceptions(e);
            }
        }
    }


    private class CancelOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    private class SztukiTextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            try {
                SeekBar seekbar = view.getSeekBarByName("Sztuki");
                Integer Wal = Integer.parseInt(s.toString());
                if (Wal > seekbar.getMax()) {
                    seekbar.setMax(Wal);
                }
                if (Wal >= 0) {
                    seekbar.setProgress(Wal);
                }
                model.setQuantity(Wal);
                SeekBar as = view.getSeekBarByName("Fundusze");
                if(as.getProgress() > 0){
                    toastError("Ogranicz ilość lub fundusze");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class FunduszeTextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            try {
                SeekBar seekbar = view.getSeekBarByName("Fundusze");

                Integer Wal = Integer.parseInt(s.toString());
                if (Wal > seekbar.getMax()) {
                    seekbar.setMax(Wal);
                }
                if (Wal >= 0) {
                    seekbar.setProgress(Wal);
                }
                model.setFunds(Wal);
                SeekBar as = view.getSeekBarByName("Sztuki");
                if(as.getProgress() > 0){
                    toastError("Ogranicz ilość lub fundusze");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class FunduszeSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                try {
                    view.getEditTextByName("Fundusze").setText(String.valueOf(progress));
                    model.setFunds(progress);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    private class SztukiSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                try {
                    view.getEditTextByName("Sztuki").setText(String.valueOf(progress));
                    model.setQuantity(progress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    private class OdEditTextListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            checkDateString(s);
            model.setFromField(s.toString());
        }
    }

    private class DoEditTextListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            checkDateString(s);
            model.setToField(s.toString());
        }
    }


    private class CheckBoxListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            CompoundButton btn = (CompoundButton) view.findViewById(R.id.ckbCategory);
            model.setOnlyCategory(btn.isChecked());
            if (btn.isChecked()) {
                view.findViewById(R.id.btnAddProduct).setEnabled(false);
                view.findViewById(R.id.spProduct).setEnabled(false);
            } else {
                view.findViewById(R.id.btnAddProduct).setEnabled(true);
                view.findViewById(R.id.spProduct).setEnabled(true);
            }
        }
    }

    private class CurrencySelectedItemListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int currencyId = ((Currency) parent.getItemAtPosition(position)).getId();
            model.setCurrencyId(currencyId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class ProductsSelectedItemListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int productId = ((Product) parent.getItemAtPosition(position)).getId();
            model.setProductId(productId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class CategorySelectedItemListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int categoryId = ((Category) parent.getItemAtPosition(position)).getId();
            model.setCategoryId(categoryId);
            setAdapterProducts();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class PopupUpdateDataListener implements UpdateDataListener {

        @Override
        public void dataUpdated(PopupWindowType source) {
            switch (source) {
                case PRODUCT:
                    model.updateProducts();
                    setAdapterProducts();
                    break;
                case CATEGORY:
                    model.updateCategories();
                    setAdapterCategories();
                    break;
            }
        }
    }
}


