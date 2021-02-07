package com.jrcw.expensemonitor.containers;

import java.util.Date;

public class Expense {
    private Date expenditureTime;
    private String description;
    private int placeId;

    public Expense(Date expenditureTime, String description, int placeId) {
        this.expenditureTime = expenditureTime;
        this.description = description;
        this.placeId = placeId;
    }

    public Date getExpenditureTime() {
        return expenditureTime;
    }

    public String getDescription() {
        return description;
    }

    public int getPlaceId() {
        return placeId;
    }
}
