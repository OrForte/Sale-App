package com.example.eliavmenachi.myapplication.Models.User;

import com.example.eliavmenachi.myapplication.Entities.User;

public class UserModel {

    //region DataMembers
    public static UserModel instance = new UserModel();
    UserModelFirebase userModelFirebase;
    //endregion

    //region C'Tors

    private UserModel()
    {
        userModelFirebase = new UserModelFirebase();
    }

    //endregion

    //region methods

    public interface IsUserVisibleListener{
        void onDone(boolean p_bIsValid);
    }

    public void IsUserVisible(final String p_strUserName, final String p_strPassword ,  final IsUserVisibleListener listener ){
        boolean bIsValid = true;

        userModelFirebase.IsUserVisible(p_strUserName, p_strPassword, new IsUserVisibleListener() {
            @Override
            public void onDone(boolean p_bIsValid) {
                // Its happen when we get response from firebase
                listener.onDone(p_bIsValid);
            }
        });

        //listener.onDone(bIsValid);
    }

    public void addUser(User userToAdd)
    {
        userModelFirebase.addUser(userToAdd);
    }


    //endregion
}
