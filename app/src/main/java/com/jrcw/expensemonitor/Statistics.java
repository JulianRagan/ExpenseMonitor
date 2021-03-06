package com.jrcw.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Statistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        StatisticsModel model = new StatisticsModel(this);
        new StatisticsController(this, model);
    }
}