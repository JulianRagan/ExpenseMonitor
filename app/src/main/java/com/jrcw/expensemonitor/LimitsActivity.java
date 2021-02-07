package com.jrcw.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class LimitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limits);
        new LimitsActivityController(this, new LimitsActivityModel(this));
    }


    public EditText getEditTextByName(String name) throws Exception {
        EditText txt = null;
        switch (name) {
            case "Fundusze":
                txt = (EditText) this.findViewById(R.id.etFunds);
                break;
            case "Sztuki":
                txt = (EditText) this.findViewById(R.id.etQuantity);
                break;
            case "DataOd":
                txt = (EditText) this.findViewById(R.id.etFrom);
            case "DateDo":
                txt = (EditText) this.findViewById(R.id.etTo);
        }
        if (txt != null) {
            return txt;
        } else {
            throw new Exception();
        }
    }

    public SeekBar getSeekBarByName(String name) throws Exception {
        SeekBar sb = null;
        switch (name) {
            case "Fundusze":
                sb = (SeekBar) this.findViewById(R.id.seekFundusze);
                break;

            case "Sztuki":
                sb = (SeekBar) this.findViewById(R.id.seekSztuki);
                break;
        }
        if (sb != null) {
            return sb;
        } else {
            throw new Exception();
        }

    }

}



