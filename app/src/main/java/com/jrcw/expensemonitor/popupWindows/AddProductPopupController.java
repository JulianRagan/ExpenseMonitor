package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.jrcw.expensemonitor.LimitsActivityController;
import com.jrcw.expensemonitor.PopupWindowType;
import com.jrcw.expensemonitor.R;
import com.jrcw.expensemonitor.containers.Category;
import com.jrcw.expensemonitor.containers.DetailContent;
import com.jrcw.expensemonitor.containers.Product;
import com.jrcw.expensemonitor.containers.UnitOfMeasure;
import com.jrcw.expensemonitor.containers.UpdateDataListener;

import java.util.List;

public class AddProductPopupController {
    private View view;
    private PopupWindow window;
    private AddProductPopupModel model;
    private Context context;
    private UpdateDataListener listener;

    public AddProductPopupController(View view, PopupWindow w, Context c, List<Category> categories,
                                     List<UnitOfMeasure> units, List<Product> products) {
        this.view = view;
        this.model = new AddProductPopupModel(products, categories, units, context);
        this.window = w;
        this.context = c;
        initControlls();
    }

    private void initControlls() {
        ((Spinner) view.findViewById(R.id.spChooseCategory)).setOnItemSelectedListener(new CategoryItemSelectedListener());
        setAdapterCategories();
        ((Spinner) view.findViewById(R.id.spChooseUnit)).setOnItemSelectedListener(new UnitItemSelectedListener());
        setAdapterUnit();
        ((EditText) view.findViewById(R.id.txtProductName)).addTextChangedListener(new NameTextChangeListener());
        ((EditText) view.findViewById(R.id.txtDescriptionProduct)).addTextChangedListener(new  DescriptionTextChangeListener());
        ((Button) view.findViewById(R.id.btnAdditionProduct)).setOnClickListener(new AdditinonProductOnClickListener());
        ((Button) view.findViewById(R.id.btnCancelProduct)).setOnClickListener(new CancelOnClickListener());

    }
    private void setAdapterUnit(){
        ((Spinner)view.findViewById(R.id.spChooseUnit)).setAdapter(
                model.getUnitAdapter(context));
    }

    private void setAdapterCategories(){
        ((Spinner)view.findViewById(R.id.spChooseCategory)).setAdapter(
                model.getCategoriesAdapter(context));
    }


    public void setUpdateDataListener(UpdateDataListener listener) {
        this.listener = listener;
    }


    private class CategoryItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int cid = ((Category)parent.getItemAtPosition(position)).getId();
            model.setCategoryId(cid);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class UnitItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int uni = ((UnitOfMeasure)parent.getItemAtPosition(position)).getId();
            model.setUnitId(uni);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class NameTextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            model.setName(s.toString());
        }
    }

    private class DescriptionTextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            model.setDescription(s.toString());
        }
    }

    private void toastError(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    private class AdditinonProductOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String mode = ((String) ((Button) v).getText()).toLowerCase();
            switch (mode) {
                case "dodaj":
                    if (!model.productExists()) {
                        if (model.isMinimalDataSet()) {
                            model.storeProduct(context);
                            window.dismiss();
                            listener.dataUpdated(PopupWindowType.PRODUCT);
                        } else {
                            toastError("Należy podać nazwę produktu");
                        }
                    } else {
                        toastError("Podana nazwa już istnieje w systemie");
                    }
                    break;
                case "zachowaj":
                    if (model.isMinimalDataSet()) {
                        model.updateProduct(context);
                        window.dismiss();
                        listener.dataUpdated(PopupWindowType.PRODUCT);
                    }
                    break;
            }

        }
    }

    private class CancelOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String mode = ((String) ((Button) v).getText()).toLowerCase();
            switch (mode) {
                case "odrzuć":
                    model.clear();
                    break;
                case "anuluj":
                    window.dismiss();
                    break;
            }

        }
    }
}
