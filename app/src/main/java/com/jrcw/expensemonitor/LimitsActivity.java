package com.jrcw.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class LimitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limits);
    }

    public Button getButtonByName(String name) throws Exception {
        Button btn = null;
        switch (name) {
            case "Szczegoly":
                btn = (Button) this.findViewById(R.id.button);
                break;
            case "Edytuj":
                btn = (Button) this.findViewById(R.id.btnEdycja);
                break;
            case "Ustaw":
                btn = (Button) this.findViewById(R.id.btnUstaw);
                break;
            case "Kategorie":
                btn = (Button) this.findViewById(R.id.btnKategorie);
                break;
            case "Produkty":
                btn = (Button) this.findViewById(R.id.btnProdukty);
                break;
        }
        if (btn != null) {
            return btn;
        } else {
            throw new Exception();
        }
    }

    public EditText getEditTextByName(String name) throws Exception {
        EditText txt = null;
        switch (name) {
            case "Fundusze":
                txt = (EditText) this.findViewById(R.id.editTextFundusze);
                break;
            case "Sztuki":
                txt = (EditText) this.findViewById(R.id.editTextSztuki);
                break;
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



