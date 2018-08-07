package com.example.eliavmenachi.myapplication.Models.Sale;

import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.example.eliavmenachi.myapplication.Entities.Sequence;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class SaleModelFirebase {

    public void getPostsByStoreId(final String p_strStoreId, final SaleModel.GetPostsByStoreIdListener listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void deleteLogicSale(Sale p_saleToDelete, final SaleModel.deleteLogicSaleListener listener) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("sale").child(p_saleToDelete.id).setValue(p_saleToDelete);
        listener.onDeleteLogicSale(true);
    }

    public void addPost(Sale p_postToSave) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("sale").child(p_postToSave.id).setValue(p_postToSave);
    }

    public void addOrUpdateNewSale(final Sale p_saleToAdd, final SaleModel.addOrUpdateNewSaleListener listener)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("sale").child(p_saleToAdd.id).setValue(p_saleToAdd);
        listener.onAddOrUpdateNewSaleResults(p_saleToAdd);
    }

    public void GetNextSequenceSale(final String SeqName, final SaleModel.GetNextSequenceListener listener) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("sequences").child(SeqName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Sequence sequence = dataSnapshot.getValue(Sequence.class);
                int value = (Integer.parseInt(sequence.value) + 1);
                sequence.value = value + "";
                mDatabase.child("sequences").child(sequence.name).setValue(sequence);
                listener.onGetNextSeq(sequence.value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    //region sales's methods

    interface GetAllSalesListener {
        public void onSuccess(List<Sale> studentslist);
    }

    ValueEventListener eventListener;

    public void getAllSales(final GetAllSalesListener listener) {
        Query stRef = FirebaseDatabase.getInstance().getReference().child("sale");
        eventListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Sale> stSales = new LinkedList<>();
                for (DataSnapshot stSnapshot : dataSnapshot.getChildren()) {
                    Sale currSale = stSnapshot.getValue(Sale.class);
                    if (currSale.active == true) {
                        stSales.add(currSale);
                    }
                }

                listener.onSuccess(stSales);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void cancellGetAllSales() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("sale");
        stRef.removeEventListener(eventListener);
    }


    interface GetAllSalesByStoreListener {
        public void onSuccess(List<Sale> results);
    }

    ValueEventListener eventListener2;

    public void GetAllSalesByStoreId(final String storeId, final GetAllSalesByStoreListener listener) {
        Query stRef = FirebaseDatabase.getInstance().getReference().child("sale").orderByChild("storeId").equalTo(Integer.parseInt(storeId));
        eventListener2 = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Sale> stSales = new LinkedList<>();
                for (DataSnapshot stSnapshot : dataSnapshot.getChildren()) {
                    Sale currSale = stSnapshot.getValue(Sale.class);
                    if (currSale.active == true) {
                        stSales.add(currSale);
                    }
                }

                listener.onSuccess(stSales);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void cancellGetAllSalesByStoreId() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("sale");
        stRef.removeEventListener(eventListener);
    }


    interface GetSaleBySaleId {
        public void onGetData(Sale data);
    }

    ValueEventListener eventListener3;

    public void GetSaleBySaleId(final String saleId, final GetSaleBySaleId listener) {
        Query stRef = FirebaseDatabase.getInstance().getReference().child("sale").orderByChild("id").equalTo(saleId);
        eventListener3 = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Sale currSale = new Sale();
                for (DataSnapshot stSnapshot : dataSnapshot.getChildren()) {
                    Sale sale = stSnapshot.getValue(Sale.class);
                    if (sale.active == true) {
                        currSale = sale;
                    }
                }
                listener.onGetData(currSale);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void cancellGetSalesBySaleId() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("sale");
        stRef.removeEventListener(eventListener);
    }

    interface GetSaleByUserName {
        public void onGetData(List<Sale> data);
    }

    ValueEventListener eventListener4;

    public void GetSaleByUserName(final String UserName, final GetSaleByUserName listener) {
        Query stRef = FirebaseDatabase.getInstance().getReference().child("sale").orderByChild("userId").equalTo(UserName);
        eventListener4 = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Sale> stSales = new LinkedList<>();
                for (DataSnapshot stSnapshot : dataSnapshot.getChildren()) {
                    Sale currSale = stSnapshot.getValue(Sale.class);
                    if (currSale.active) {
                        stSales.add(currSale);
                    }
                }

                listener.onGetData(stSales);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //endregion
}
