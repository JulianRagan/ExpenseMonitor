package com.jrcw.expensemonitor.popupWindows;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
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

    private void toastError(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
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

    private void handleEvaluationExceptions(Exception e) {
        switch (e.getMessage()){
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
                toastError("Nieprawdidłowa data");
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

            default:
                toastError("Nieznany błąd");
                e.printStackTrace();
        }
    }

    private class ShowOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            try {
                PieData pd = model.evaluateExpenseDistribution();
                PieChart pc = ((PieChart)view.findViewById(R.id.chart));
                pc.setData(pd);
                pc.invalidate();
            }catch (Exception e){
                handleEvaluationExceptions(e);
            }
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
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            checkDateString(s);
            model.setFromField(s.toString());
        }
    }

    private class ToTextWatcher implements TextWatcher{

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
}
