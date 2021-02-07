package com.jrcw.expensemonitor;

import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.PopupMenu;

public class StatisticsController {
    private Statistics view;
    private StatisticsModel model;


    public StatisticsController(Statistics view, StatisticsModel model) {
        this.view = view;
        this.model = model;
        initControls();
    }

    private void initControls() {
        ((Button)view.findViewById(R.id.btnChooseFunction)).setOnClickListener(new ChooseFunctionOnClickListener());
        ((Button)view.findViewById(R.id.btnDevisionExpense)).setOnClickListener(new DevisionExpenseOnClickListener());
        ((Button)view.findViewById(R.id.btnChangePrice)).setOnClickListener(new  ChangePriceOnClickListener());
        ((Button)view.findViewById(R.id.btnOverrun)).setOnClickListener(new OverrunOnClickListener());
    }
    public void showPopup(View v, Statistics view){
        PopupMenu popup = new PopupMenu(view, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_function, popup.getMenu());
        popup.show();
    }

    private class ChooseFunctionOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v ) {


        }
    }
    private class DevisionExpenseOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }
    private class ChangePriceOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }
    private class OverrunOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }
}
