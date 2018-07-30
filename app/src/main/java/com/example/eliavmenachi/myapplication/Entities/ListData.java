package com.example.eliavmenachi.myapplication.Entities;

import java.util.ArrayList;
import java.util.List;

public class ListData {
    public List<Store> stores;
    public List<Mall> malls;
    public List<City> cities;

    public ListData() {
        this.stores = new ArrayList<Store>();
        this.malls = new ArrayList<Mall>();
        this.cities = new ArrayList<City>();
    }

    @Override
    public boolean equals(Object obj) {
        //return super.equals(obj);
        ListData listData = (ListData) obj;

        if (identicalLists(this.stores, listData.stores)
                && identicalLists(this.malls, listData.malls)
                && identicalLists(this.cities, listData.cities)) {
            return true;
        }

        return false;
    }

    private static boolean identicalLists(List list1, List list2) {
        if (list1.size() == list2.size()) {
            for (int i = 0; i < list1.size(); i++) {
                if (!list1.get(i).equals(list2.get(i)))
                    return false;
            }
        }

        return true;
    }
}
