package com.jrcw.expensemonitor;

import android.app.SearchManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SeekBar;

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
            view.getSeekBarByName("Fundusze").setMax(1000);
            view.getSeekBarByName("Sztuki").setMax(100);
            view.getEditTextByName("Fundusze").setText("0");
            view.getEditTextByName("Sztuki").setText("0");
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

        }
    }
    private class FunduszeSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

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

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
