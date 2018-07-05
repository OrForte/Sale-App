package com.example.eliavmenachi.myapplication.Entities;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListData {
    public List<Store> stores;
    public List<Mall> malls;
    public List<City> cities;

    public ListData()
    {
        this.stores = new ArrayList<Store>();
        this.malls = new ArrayList<Mall>();
        this.cities = new ArrayList<City>();
    }
}
