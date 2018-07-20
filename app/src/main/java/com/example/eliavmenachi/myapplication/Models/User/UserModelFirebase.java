package com.example.eliavmenachi.myapplication.Models.User;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserModelFirebase {
    String userName;
    String password;
    public void addUser(User userToAdd)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // TODO: ADD FUNCTION TO ADD REAL ADDITION OF USER
        mDatabase.child("users").child(userToAdd.userName).setValue(userToAdd);
    }

    public void IsUserVisible(final String p_strUserName, final String p_strPassword, final UserModel.IsUserVisibleListener listener)
    {
        userName = p_strUserName;
        password = p_strPassword;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(p_strUserName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean bIsValid = false;
                User userData = dataSnapshot.getValue(User.class);

                if (userData != null)
                {
                    if (userData.userName != null &&
                            userData.password !=null &&
                            userData.userName.equals(userName) &&
                            userData.password.equals(password))
                    {
                        bIsValid = true;
                    }
                    else
                    {
                        bIsValid = false;
                    }
                }

                listener.onDone(bIsValid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onDone(false);
            }
        });
    }

}
