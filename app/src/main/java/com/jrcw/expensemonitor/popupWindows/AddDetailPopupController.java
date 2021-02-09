package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.jrcw.expensemonitor.PopupWindowType;
import com.jrcw.expensemonitor.R;
import com.jrcw.expensemonitor.containers.Category;
import com.jrcw.expensemonitor.containers.Currency;
import com.jrcw.expensemonitor.containers.DetailContent;
import com.jrcw.expensemonitor.containers.DetailEntryAction;
import com.jrcw.expensemonitor.containers.EntryAction;
import com.jrcw.expensemonitor.containers.Product;
import com.jrcw.expensemonitor.containers.UnitOfMeasure;
import com.jrcw.expensemonitor.containers.UpdateDataListener;

import java.util.Date;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AddDetailPopupController {
    View view;
    View src;
    PopupWindow window;
    AddDetailPopupModel model;
    Context context;
    DetailEntryAction action;

    public AddDetailPopupController(View view, View src, PopupWindow w, Context c, Date entryDate){
        this.view = view;
        this.window = w;
        this.context = c;
        this.src = src;
        model = new AddDetailPopupModel(c, entryDate);
        InitControls();
    }

    public void setDetailEntryAction (DetailEntryAction action){
        this.action = action;
    }


    public void restoreFromDetailContent(DetailContent dc){
        for(int i = 0; i < model.getCategories().size(); i++){
            if(model.getCategories().get(i).getId() == dc.getCategoryId()){
                ((Spinner)view.findViewById(R.id.spCategoryCostDetails)).setSelection(i);
                break;
            }
        }
        SpinnerAdapter adapter = ((Spinner)view.findViewById(R.id.spProducts)).getAdapter();
        for(int i = 0; i < adapter.getCount(); i++){
            Product p = (Product)adapter.getItem(i);
            if( p.getId() == dc.getProductId()){
                ((Spinner)view.findViewById(R.id.spProducts)).setSelection(i);
                break;
            }
        }
        for(int i = 0; i < model.getUnits().size(); i++){
            if(model.getUnits().get(i).getId() == dc.getUnitId()){
                ((Spinner)view.findViewById(R.id.spUnit)).setSelection(i);
                break;
            }
        }
        for(int i = 0; i < model.getCurrencies().size(); i++){
            if(model.getCurrencies().get(i).getId() == dc.getUnitId()){
                ((Spinner)view.findViewById(R.id.spCurrency)).setSelection(i);
                break;
            }
        }
    }

    private void InitControls(){
        ((Button)view.findViewById(R.id.btnAddExpenseDetails)).setOnClickListener(
                new AddExpenseOnClickListener());
        ((Button)view.findViewById(R.id.btnAddCategoryCostDetails)).setOnClickListener(
                new AddCategoryOnClickListener());
        ((Button)view.findViewById(R.id.btnAddProductExpenseDetails)).setOnClickListener(
                new AddProductOnClickListener());
        ((Button)view.findViewById(R.id.btnEndExpenseDetails)).setOnClickListener(
                new EndExpenseOnClickListener());
        ((Spinner)view.findViewById(R.id.spCategoryCostDetails)).setOnItemSelectedListener(
                new CategoryItemSelectedListener());
        ((Spinner)view.findViewById(R.id.spProducts)).setOnItemSelectedListener(
                new ProductItemSelectedListener());
        ((Spinner)view.findViewById(R.id.spUnit)).setOnItemSelectedListener(
                new UnitItemSelectedListener());
        ((Spinner)view.findViewById(R.id.spCurrency)).setOnItemSelectedListener(
                new CurrencyItemSelectedListener());
        ((EditText)view.findViewById(R.id.txtQuantity)).addTextChangedListener(
                new QuantityTextWatcher());
        ((EditText)view.findViewById(R.id.txtPrice)).addTextChangedListener(new PriceTextWatcher());
        setAdapterCategories();
        setAdapterProducts();
        setAdapterUnits();
        setCurrencyAdapter();
    }

    private void setAdapterCategories(){
        ((Spinner)view.findViewById(R.id.spCategoryCostDetails)).setAdapter(
                model.getCategoryAdapter(context));
    }

    private void setAdapterProducts(){
        String categoryName = ((Category)((Spinner)view.findViewById(R.id.spCategoryCostDetails))
                .getSelectedItem()).getName();
        ((Spinner)view.findViewById(R.id.spProducts)).setAdapter(
                model.getProductsAdapter(context, categoryName));
    }

    private void setAdapterUnits(){
        ((Spinner)view.findViewById(R.id.spUnit)).setAdapter(model.getUnitsAdapter(context));
        int uid = ((Product)((Spinner)view.findViewById(R.id.spProducts)).getSelectedItem())
                .getDefaultUnitId();
        for(int i = 0; i < model.getUnits().size(); i++){
            if(model.getUnits().get(i).getId() == uid){
                ((Spinner)view.findViewById(R.id.spUnit)).setSelection(i);
                break;
            }
        }
    }

    private void setCurrencyAdapter(){
        ((Spinner)view.findViewById(R.id.spCurrency)).setAdapter(model.getCurrencyAdapter(context));
    }

    private void clear(){
        model.clear();
        ((EditText)view.findViewById(R.id.txtQuantity)).setText("");
        ((EditText)view.findViewById(R.id.txtPrice)).setText("");
    }

    private void toastError(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    private DetailContent storeContent(){
        DetailContent dc = new DetailContent();
        dc.setCategoryId(model.getCategoryId());
        dc.setCurrencyId(model.getCurrencyId());
        dc.setPrice(model.getAmountField());
        dc.setQuantity(model.getQuantityField());
        dc.setUnitId(model.getUnitId());
        dc.setView(src);
        return dc;
    }

    private class AddExpenseOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            try {
                if(model.isMinimalDataSet()){
                    model.storeDetail();
                    clear();
                }
            } catch (Exception e) {
                switch (e.getMessage()){
                    case "Amount":
                        toastError("Popraw kwotę");
                        break;
                    case "Quantity":
                        toastError("Popraw ilość");
                        break;
                    default:
                        toastError("Nieznany błąd");
                }
            }
        }
    }

    private class AddCategoryOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            DetailContent dc = storeContent();
            window.dismiss();
            action.entryAction(EntryAction.ADD_CATEGORY,dc);
        }
    }

    private class AddProductOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            DetailContent dc = storeContent();
            window.dismiss();
            action.entryAction(EntryAction.ADD_PRODUCT,dc);
        }
    }

    private class EndExpenseOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            window.dismiss();
            action.entryAction(EntryAction.END_ENTRY,null);
        }
    }

    private class PriceTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            model.setAmountField(s.toString());
        }
    }

    private class QuantityTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            model.setQuantityField(s.toString());
        }
    }

    private class CategoryItemSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int cid = ((Category)parent.getItemAtPosition(position)).getId();
            model.setCategoryId(cid);
            setAdapterProducts();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class ProductItemSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int pid = ((Product)parent.getItemAtPosition(position)).getId();
            model.setProductId(pid);
            setAdapterUnits();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class UnitItemSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int uid = ((UnitOfMeasure)parent.getItemAtPosition(position)).getId();
            model.setUnitId(uid);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class CurrencyItemSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int cid = ((Currency)parent.getItemAtPosition(position)).getId();
            model.setCurrencyId(cid);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

}
