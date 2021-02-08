package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.github.mikephil.charting.charts.PieChart;
import com.jrcw.expensemonitor.R;
import com.jrcw.expensemonitor.StatisticsModel;

public class SEDPopupController {
    private StatisticsModel model;
    private View view;
    private PopupWindow window;
    private Context context;

    public SEDPopupController(View view, PopupWindow w, Context c, StatisticsModel model){
        this.view = view;
        this.model = model;
        this.window = w;
        this.context = c;
        initControls();
    }

    private void initControls(){
        ((Button)view.findViewById(R.id.btnSEDShow)).setOnClickListener(new ShowOnClickListener());
        ((Button)view.findViewById(R.id.btnSEDClose)).setOnClickListener(new CloseOnClickListener());
        ((EditText)view.findViewById(R.id.etSEDFrom)).addTextChangedListener(new FromTextWatcher());
        ((EditText)view.findViewById(R.id.etSEDTo)).addTextChangedListener(new ToTextWatcher());
        //((PieChart)view.findViewById(R.id.chart)).set
    }

    private class ShowOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }

    private class CloseOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            window.dismiss();
        }
    }

    private class FromTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class ToTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
