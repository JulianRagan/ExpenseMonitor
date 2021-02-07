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
import com.jrcw.expensemonitor.popupWindows.AddPlacePopupController;

import java.sql.Date;

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
        ((Spinner)view.findViewById(R.id.spCategory)).setOnItemSelectedListener(new CategorySelectedItemListener());
        setAdapterCategories();
        ((Spinner)view.findViewById(R.id.spProduct)).setOnItemSelectedListener(new ProductsSelectedItemListener());
        setAdapterProducts();
        ((SeekBar) view.findViewById(R.id.seekFundusze)).setOnSeekBarChangeListener(new FunduszeSeekBarListener());
        ((Spinner)view.findViewById(R.id.spCurrencyLimits)).setOnItemSelectedListener(new CurrencySelectedItemListener());
        setAdapterCurrencies();
        ((SeekBar) view.findViewById(R.id.seekSztuki)).setOnSeekBarChangeListener(new SztukiSeekBarListener());
        ((EditText) view.findViewById(R.id.etFunds)).addTextChangedListener(new FunduszeTextChangedListener());
        ((EditText) view.findViewById(R.id.etQuantity)).addTextChangedListener(new SztukiTextChangedListener());
        ((EditText) view.findViewById(R.id.etFrom)).setOnClickListener(new OdEditTextListener());
        ((EditText) view.findViewById(R.id.etTo)).setOnClickListener(new DoEditTextListener());
        ((Button) view.findViewById(R.id.btnSet)).setOnClickListener(new UstawOnClickListener());
        ((Button) view.findViewById(R.id.btnCancel)).setOnClickListener(new EdytujOnClickListener());

    }

    private void setAdapterCategories() {
        ((Spinner) view.findViewById(R.id.spCategory)).setAdapter(model.getCategoryAdapter(view));
    }

    private void setAdapterCurrencies() {
        ((Spinner) view.findViewById(R.id.spCurrency)).setAdapter(model.getCurrencyAdapter(view));
    }

    private void setAdapterProducts() {
        String Cat = model.getCategories().get(0).getName();
        ((Spinner) view.findViewById(R.id.spProduct)).setAdapter(model.getProductsAdapter(view, Cat));
    }

    private void setAdapterProducts(String Cat) {

        ((Spinner) view.findViewById(R.id.spProduct)).setAdapter(model.getProductsAdapter(view, Cat));
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



    private class KategorieOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            {
                showPopupWindow(PopupWindowType.CATEGORY, v);


            }
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

        }
    }

    private class EdytujOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    private class SztukiTextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

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
            } catch (NumberFormatException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private class FunduszeTextChangedListener implements TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

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
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
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
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private class OdEditTextListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                String txt = view.getEditTextByName("DataOd").getText().toString();
                Date OD = Date.valueOf(txt);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class DoEditTextListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                String txt = view.getEditTextByName("DataDo").getText().toString();
                Date DO = Date.valueOf(txt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class CheckBoxListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            CompoundButton btn = (CompoundButton) view.findViewById(R.id.ckbCategory);
            if (btn.isChecked()) {

            }
        }
    }
    private class CurrencySelectedItemListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int currencyId = ((Currency)parent.getItemAtPosition(position)).getId();
            model.setCurrencyId(currencyId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private class ProductsSelectedItemListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int productId = ((Product)parent.getItemAtPosition(position)).getId();
            model.setProductId(productId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private class CategorySelectedItemListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int categoryId = ((Category)parent.getItemAtPosition(position)).getId();
            model.setCategoryId(categoryId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private class PopupUpdateDataListener implements UpdateDataListener {

        @Override
        public void dataUpdated(PopupWindowType source) {
            switch(source){
                case PLACE:

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
