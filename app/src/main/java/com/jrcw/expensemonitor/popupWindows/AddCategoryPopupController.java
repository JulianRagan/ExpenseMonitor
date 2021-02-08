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

import com.jrcw.expensemonitor.PopupWindowType;
import com.jrcw.expensemonitor.R;
import com.jrcw.expensemonitor.containers.Category;
import com.jrcw.expensemonitor.containers.UpdateDataListener;

import java.util.List;

public class AddCategoryPopupController {
    private View view;
    private PopupWindow window;
    private AddCategoryPopupModel model;
    private Context context;
    private UpdateDataListener listener;


    public AddCategoryPopupController(View view, PopupWindow w, Context c, List<Category> categories){
        this.view = view;
        this.model = new AddCategoryPopupModel(categories);
        this.window = w;
        this.context = c;
        initControls();
    }

    public void setUpdateDataListener(UpdateDataListener listener){
        this.listener = listener;
    }

    private void initControls(){
        ((EditText)view.findViewById(R.id.txtNameCategory)).addTextChangedListener(new NameTextWatcher());
        ((EditText)view.findViewById(R.id.txtDescriptionCategory)).addTextChangedListener(new DescriptionTextWatcher());
        ((Button)view.findViewById(R.id.btnAdditionNewCategory)).setOnClickListener(new AddCategoryOnClickListener());
        ((Button)view.findViewById(R.id.btnCancelAddCategory)).setOnClickListener(new CancelOnClickListener());
        ((ListView)view.findViewById(R.id.lvAddCategory)).setAdapter(model.getCategoriesAdapter(context));
        ((ListView)view.findViewById(R.id.lvAddCategory)).setOnItemClickListener(new ListOnItemClickListener());
    }

    private void importCategory(Category c){
        model.populateFromCategory(c);
        ((EditText)view.findViewById(R.id.txtNameCategory)).setText(c.getName());
        ((EditText)view.findViewById(R.id.txtDescriptionCategory)).setText(c.getDescription());
        ((Button)view.findViewById(R.id.btnAdditionNewCategory)).setText("Zachowaj");
        ((Button)view.findViewById(R.id.btnCancelAddCategory)).setText("Odrzuć");
    }

    private void clear(){
        model.clear();
        ((EditText)view.findViewById(R.id.txtNameCategory)).setText("");
        ((EditText)view.findViewById(R.id.txtDescriptionCategory)).setText("");
        ((Button)view.findViewById(R.id.btnAdditionNewCategory)).setText("Dodaj");
        ((Button)view.findViewById(R.id.btnCancelAddCategory)).setText("Anuluj");
    }

    private void toastError(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    private class AddCategoryOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String mode = ((String)((Button)v).getText()).toLowerCase();
            switch(mode){
                case "dodaj":
                    if(!model.categoryExists()){
                        if(model.isMinimalDataSet()){
                            model.storeCategory(context);
                            listener.dataUpdated(PopupWindowType.CATEGORY);
                            window.dismiss();
                        }else{
                            toastError("Należy podać nazwę kategorii");
                        }
                    }else{
                        toastError("Podana nazwa już istnieje w systemie");
                    }
                    break;
                case "zachowaj":
                    if(model.isMinimalDataSet()){
                        model.updateCategory(context);
                        listener.dataUpdated(PopupWindowType.CATEGORY);
                        window.dismiss();
                    }
                    break;
            }

        }
    }

    private class ListOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Category c = (Category)parent.getItemAtPosition(position);
            importCategory(c);
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
}
