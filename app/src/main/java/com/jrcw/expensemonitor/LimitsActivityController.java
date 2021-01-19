package com.jrcw.expensemonitor;

import android.app.SearchManager;
import android.icu.util.LocaleData;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;

import java.sql.Date;
import java.time.LocalDate;

public class LimitsActivityController {

    private LimitsActivity view;
    private LimitsActivityModel model;


    public LimitsActivityController(LimitsActivity view, LimitsActivityModel model) {
        this.view = view;
        this.model = model;
        initView();
    }

    private void initView() {
        try {
            view.getEditTextByName("DateOd").setOnClickListener(new OdEditTextListener());
            view.getEditTextByName("DateDo").setOnClickListener(new DoEditTextListener());
            view.getSeekBarByName("Fundusze").setMax(1000);
            view.getSeekBarByName("Fundusze").setOnSeekBarChangeListener(new FunduszeSeekBarListener());
            view.getSeekBarByName("Sztuki").setMax(100);
            view.getSeekBarByName("Sztuki").setOnSeekBarChangeListener(new SztukiSeekBarListener());
            view.getEditTextByName("Fundusze").setText("0");
            view.getEditTextByName("Fundusze").addTextChangedListener(new FunduszeTextChangedListener());
            view.getEditTextByName("Sztuki").setText("0");
            view.getEditTextByName("Sztuki").addTextChangedListener(new SztukiTextChangedListener());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private class KategorieOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }
    private class ProduktyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

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
                SeekBar seekbar= view.getSeekBarByName("Sztuki");

                Integer Wal = Integer.parseInt(s.toString());
                if (Wal > seekbar.getMax()) {
                    seekbar.setMax(Wal);
                }
                if (Wal >= 0) {
                    seekbar.setProgress(Wal);
                }
                model.setQuantity(Wal);
            }catch (NumberFormatException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    private class FunduszeTextChangedListener implements  TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
             SeekBar seekbar= view.getSeekBarByName("Fundusze");

            Integer Wal = Integer.parseInt(s.toString());
            if (Wal > seekbar.getMax()) {
                seekbar.setMax(Wal);
            }
            if (Wal >= 0) {
                seekbar.setProgress(Wal);
            }
            model.setFunds(Wal);
            }catch (NumberFormatException e) {
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
            CompoundButton btn = (CompoundButton)view.findViewById(R.id.ckbCategory);
            if (btn.isChecked()){

            }
        }
    }
}
