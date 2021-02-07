package com.jrcw.expensemonitor;

import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    private void toastError(String msg){
        Toast.makeText(view, msg, Toast.LENGTH_LONG).show();
    }

    public void showPopup(View v, Statistics view){
        PopupMenu popup = new PopupMenu(view, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_function, popup.getMenu());
        popup.setOnMenuItemClickListener(new ActivitySwitch());
        popup.getMenu().findItem(R.id.menuStatistics).setEnabled(false);
        popup.show();
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
