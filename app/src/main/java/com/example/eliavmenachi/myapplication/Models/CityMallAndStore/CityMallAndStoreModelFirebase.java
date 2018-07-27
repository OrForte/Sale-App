package com.example.eliavmenachi.myapplication.Models.CityMallAndStore;

import com.example.eliavmenachi.myapplication.Entities.City;
import com.example.eliavmenachi.myapplication.Entities.CityMallStoreDetails;
import com.example.eliavmenachi.myapplication.Entities.ListData;
import com.example.eliavmenachi.myapplication.Entities.Mall;
import com.example.eliavmenachi.myapplication.Entities.Store;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CityMallAndStoreModelFirebase {
    CityMallStoreDetails details = new CityMallStoreDetails();
    ListData listData = new ListData();


    public void GetListOfCitiesMallsAndStores(final CityMallAndStoreModel.GetListOfCitiesMallsAndStoresListener listener) {
        this.listData = new ListData();

        // step 1: get the store data;
        FirebaseDatabase.getInstance().getReference()
                .child("store").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listData.stores = new ArrayList<>();
                for (DataSnapshot curr : dataSnapshot.getChildren()) {
                    listData.stores.add(curr.getValue(Store.class));
                }
                // step 2: get the mall data;
                FirebaseDatabase.getInstance().getReference()
                        .child("mall").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listData.malls = new ArrayList<>();
                        for (DataSnapshot curr : dataSnapshot.getChildren()) {
                            listData.malls.add(curr.getValue(Mall.class));
                        }

                        // step 3: get the city data;
                        FirebaseDatabase.getInstance().getReference().child("city").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                listData.cities = new ArrayList<>();
                                for (DataSnapshot curr : dataSnapshot.getChildren()) {
                                    listData.cities.add(curr.getValue(City.class));
                                }
                                // send the data to callback
                                listener.onGetListOfCitiesMallsANdStoresResults(listData);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void GetDetailsByStoreId(final int storeId, final CityMallAndStoreModel.GetDetailsByStoreIdListener listener) {
        String storeIdString = storeId + "";

        // step 1: get the store data;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("store").child(storeIdString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (details == null) {
                    details = new CityMallStoreDetails();
                }
                details.store = dataSnapshot.getValue(Store.class);

                // step 2: get the mall data;
                FirebaseDatabase.getInstance().getReference().child("mall")
                        .child(details.store.mallId + "").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        details.mall = dataSnapshot.getValue(Mall.class);

                        // step 3: get the city data;
                        FirebaseDatabase.getInstance().getReference().child("city")
                                .child(details.mall.cityId + "").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                details.city = dataSnapshot.getValue(City.class);
                                listener.onGetDetailsByStoreIdResults(details);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void GetMallsByCityId(final String p_cityId, final CityMallAndStoreModel.GetMallsByCityIdListener listener) {
        // TODO : need to get the collections of malls by city id
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("mall").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                List<Mall> lstMallsToReturn = new ArrayList<>();
                for (DataSnapshot curr : data) {
                    Mall currMall = curr.getValue(Mall.class);
                    if (currMall.cityId == Integer.parseInt(p_cityId)) {
                        lstMallsToReturn.add(currMall);
                    }
                }

                listener.onGetMallsByCityIdResults(lstMallsToReturn);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void GetStoresByMallId(final String mallId, final CityMallAndStoreModel.GetStoreByMallIdListener listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("store").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                List<Store> lstStoresToReturn = new ArrayList<>();

                for (DataSnapshot curr : data) {
                    Store currStore = curr.getValue(Store.class);
                    if (currStore.mallId == Integer.parseInt(mallId)) {
                        lstStoresToReturn.add(currStore);
                    }
                }

                listener.onGetStoresByMallIdResults(lstStoresToReturn);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void GetCities(final CityMallAndStoreModel.GetCitiesListener listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("city").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();

                List<City> lstCities = new ArrayList<>();

                for (DataSnapshot curr : data) {
                    lstCities.add(curr.getValue(City.class));
                }

                listener.onGetCitiesResults(lstCities);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void GetStoreByStoreId(final int storeId, final CityMallAndStoreModel.GetStoreByStoreIdListener listener) {
        String storeIdString = storeId + "";
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("store").child(storeIdString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Store storeData = dataSnapshot.getValue(Store.class);
                listener.onGetStoreByStoreIdResults(storeData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
