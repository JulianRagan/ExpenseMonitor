package com.jrcw.expensemonitor.db;

import java.util.ArrayList;
import java.util.List;

public class DBQueries {

    public static String qryGetAllFrom(String tableName){
        return "SELECT * FROM " + tableName + ";";
    }

    public static List<String> populateDatabase(){
        List<String> list = new ArrayList<>();
        list.add("INSERT INTO Category (id, Name, Description) VALUES (1, 'Żywność', 'Artykuły spo" +
                "żywcze'), (2, 'Kosmetyki', 'Kosmetyki'), (3, 'Higieniczne', 'Artykuły do higien" +
                "y osobistej');\n");
        list.add("INSERT INTO UnitOfMeasure (id, Description, Abbreviation) VALUES (1, 'Metr', 'm" +
                "'), (2, 'Metr kwadratowy', 'm2'), (3, 'Metr sześcienny', 'm3'), (4, 'Sztuka', '" +
                "szt.'), (5, 'Litr', 'l'), (6, 'Kilogram', 'kg');\n");
        list.add("INSERT INTO Currency (id, Name, ISOCode, ExchangeRate) VALUES (1, 'Polski złoty" +
                "', 'PLN', '1.000');\n");
        list.add("INSERT INTO LimitMode (id, Type) VALUES (1, 'Sztukowy'), (2, 'Kwotowy');\n");
        list.add("INSERT INTO Preferences (DefaultEntryDelay, DefaultCurrency_id, DefaultExchange" +
                "Rate, DefaultMaxForQuotaSlider, DefaultMaxForQuantitySlider) VALUES ('00:15:00'" +
                ", 1, '1.000', 5000, 100);");
        return list;
    }
    public static List<String> createDataBase(){
        List<String> list = new ArrayList<>();
        list.add("CREATE TABLE Category (\n" +
                "    id INTEGER NOT NULL,\n" +
                "    Name VARCHAR(70) NOT NULL,\n" +
                "    Description VARCHAR(255),\n" +
                "    PRIMARY KEY (id),\n" +
                "    UNIQUE (Name)\n" +
                ");");
        list.add("CREATE TABLE Product (\n" +
                "    id INTEGER NOT NULL,\n" +
                "    Name VARCHAR(70) NOT NULL,\n" +
                "    Description VARCHAR(255),\n" +
                "    DefaultUnit_id INTEGER NOT NULL,\n" +
                "    Category_id INTEGER NOT NULL,\n" +
                "    PRIMARY KEY (id),\n" +
                "    UNIQUE (id, Name)\n" +
                ");\n");
        list.add("CREATE TABLE Place (\n" +
                "    id INTEGER NOT NULL,\n" +
                "    Name VARCHAR(70) NOT NULL,\n" +
                "    Description VARCHAR(255),\n" +
                "    StreetName VARCHAR(255),\n" +
                "    StreetNumber VARCHAR(10),\n" +
                "    CityName VARCHAR(255),\n" +
                "    CountryName VARCHAR(255),\n" +
                "    PRIMARY KEY (id)\n" +
                ");\n");
        list.add("CREATE TABLE UnitOfMeasure (\n" +
                "    id INTEGER NOT NULL,\n" +
                "    Description VARCHAR(255),\n" +
                "    Abbreviation VARCHAR(10) NOT NULL,\n" +
                "    PRIMARY KEY (id),\n" +
                "    UNIQUE (Abbreviation)\n" +
                ");\n");
        list.add("CREATE TABLE Currency (\n" +
                "    id INTEGER NOT NULL,\n" +
                "    Name VARCHAR(255) NOT NULL,\n" +
                "    ISOCode VARCHAR(3) NOT NULL,\n" +
                "    ExchangeRate DECIMAL(10,3) NOT NULL,\n" +
                "    PRIMARY KEY (id),\n" +
                "    UNIQUE (Name, ISOCode)\n" +
                ");\n");
        list.add("CREATE TABLE LimitMode (\n" +
                "    id INTEGER NOT NULL,\n" +
                "    Type VARCHAR(20) NOT NULL,\n" +
                "    PRIMARY KEY (id),\n" +
                "    UNIQUE (Type)\n" +
                ");\n");
        list.add("CREATE TABLE Overspend (\n" +
                "    Limit_id INTEGER NOT NULL,\n" +
                "    Reason VARCHAR(1000),\n" +
                "    TimeStamp TIMESTAMP NOT NULL,\n" +
                "    PRIMARY KEY (Limit_id)\n" +
                ");\n");
        list.add("CREATE TABLE Expense (\n" +
                "    time DATETIME NOT NULL,\n" +
                "    Description VARCHAR(255),\n" +
                "    LaterEntry BOOLEAN,\n" +
                "    Place_id INTEGER,\n" +
                "    PRIMARY KEY (time)\n" +
                "    FOREIGN KEY(Place_id) REFERENCES Place(id)\n" +
                ");\n");
        list.add("CREATE TABLE Preferences (\n" +
                "    DefaultEntryDelay TIME NOT NULL,\n" +
                "    DefaultCurrency_id INTEGER NOT NULL,\n" +
                "    DefaultExchangeRate DECIMAL(10,3) NOT NULL,\n" +
                "    DefaultMaxForQuotaSlider INTEGER NOT NULL,\n" +
                "    DefaultMaxForQuantitySlider INTEGER NOT NULL,\n" +
                "    FOREIGN KEY(DefaultCurrency_id) REFERENCES Currency(id)\n" +
                ");\n");
        list.add("CREATE TABLE ExpenseDetail (\n" +
                "    time DATETIME NOT NULL,\n" +
                "    id INTEGER NOT NULL,\n" +
                "    Product_id INTEGER,\n" +
                "    Category_id INTEGER,\n" +
                "    Quantity DECIMAL(10,3),\n" +
                "    UnitOfMeasure_id INTEGER,\n" +
                "    Amount DECIMAL(10,2) NOT NULL,\n" +
                "    Currency_id INTEGER,\n" +
                "    ExchangeRate DECIMAL(10,2) NOT NULL,\n" +
                "    PRIMARY KEY (time, id)\n" +
                "    FOREIGN KEY(time) REFERENCES Expense(time),\n" +
                "    FOREIGN KEY(Product_id) REFERENCES Product(id),\n" +
                "    FOREIGN KEY(Category_id) REFERENCES Category(id),\n" +
                "    FOREIGN KEY(UnitOfMeasure_id) REFERENCES UnitOfMeasure(id),\n" +
                "    FOREIGN KEY(Currency_id) REFERENCES Currency(id)\n" +
                ");\n");
        list.add("CREATE TABLE MonitorDetails (\n" +
                "    time DATETIME NOT NULL,\n" +
                "    NextMonit DATETIME NOT NULL,\n" +
                "    EntryDelay TIME,\n" +
                "    Note VARCHAR(1000),\n" +
                "    PRIMARY KEY (time),\n" +
                "    FOREIGN KEY(time) REFERENCES Expense(time)\n" +
                ");\n");
        list.add("CREATE TABLE Limits (\n" +
                "    id INTEGER NOT NULL,\n" +
                "    StartDate DATE NOT NULL,\n" +
                "    EndDate DATE NOT NULL,\n" +
                "    Product_id INTEGER,\n" +
                "    Category_id INTEGER,\n" +
                "    LimitMode_id INTEGER NOT NULL,\n" +
                "    Quota DECIMAL(10,2) NOT NULL,\n" +
                "    Currency_id INTEGER NOT NULL,\n" +
                "    Quantity DECIMAL(10,3) NOT NULL,\n" +
                "    PRIMARY KEY (id, StartDate, EndDate),\n" +
                "    UNIQUE (Product_id, Category_id),\n" +
                "    FOREIGN KEY(Product_id) REFERENCES Product(id),\n" +
                "    FOREIGN KEY(Category_id) REFERENCES Category(id),\n" +
                "    FOREIGN KEY(LimitMode_id) REFERENCES LimitMode(id),\n" +
                "    FOREIGN KEY(Currency_id) REFERENCES Currency(id)\n" +
                ");\n");
        return list;
    }
}
