package com.example.eliavmenachi.myapplication.Models.Sale;

import com.example.eliavmenachi.myapplication.Entities.Sequence;
import com.example.eliavmenachi.myapplication.Entities.Sale;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SaleModelFirebase {

    public void getPostsByStoreId(final String p_strStoreId, final SaleModel.GetPostsByStoreIdListener listener)
    {
        // TODO : need to get the collections if posts by store id
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void addPost(Sale p_postToSave)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("sale").child(p_postToSave.id).setValue(p_postToSave);
    }
    public void GetNextSequenceSale(final String SeqName, final SaleModel.GetNextSequenceListener listener)
    {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("sequences").child(SeqName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Sequence sequence = dataSnapshot.getValue(Sequence.class);
                int value = (Integer.parseInt(sequence.value) + 1);
                sequence.value = value+"";
                mDatabase.child("sequences").child(sequence.name).setValue(sequence);
                listener.onGetNextSeq(sequence.value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
