package com.example.eliavmenachi.myapplication.Models.User;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserModel {

    //public static UserModel.UserModelOld instance = new UserModel.UserModelOld();
    public static UserModel instance = new UserModel();
    private UserAuthModelFirebase userAuthModel;
    private UserModelFirebase userModelFirebase;
    private FirebaseUser currentFirebaseUser;
    UserData userData; //= new UserData();

    private UserModel() {
        userAuthModel = new UserAuthModelFirebase();
        userModelFirebase = new UserModelFirebase();
    }

    public interface SignInListener {
        void onSuccess();

        void onFailure(String exceptionMessage);
    }
    public void signIn(final String email, final String password, final SignInListener listener) {
        userAuthModel.signInWithEmailAndPassword(email, password, new UserAuthModelFirebase.SignInCallback() {
            @Override
            public void onSuccess(String userID, String userName) {
                userData = new UserData();
                listener.onSuccess();
            }

            @Override
            public void onFailure(String exceptionMessage) {
                listener.onFailure(exceptionMessage);
            }
        });
    }



    public class UserByUserIdData extends  MutableLiveData<User> {
        String m_userId = "";
        @Override
        protected void onActive() {
            super.onActive();
            UserAsynchDao.GetUserByUserId(m_userId, new UserAsynchDao.UserAsynchDaoListener<User>() {
                @Override
                public void onComplete(User data) {
                    setValue(data);
                    userModelFirebase.getUserById(m_userId, new UserModelFirebase.GetUserByIdListener() {
                        @Override
                        public void onSuccess(User user) {
                            setValue(user);
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }

        public UserByUserIdData() {
            super();
            setValue(new User());
        }

        public void InitUserId(String p_userId) {
            m_userId = p_userId;
        }
    }
    public UserByUserIdData userByUserIdData = new UserByUserIdData();
    public LiveData<User> getUserByUserId(String p_userId) {
        return userByUserIdData;
    }
    public void InitUserId(String p_strStoreId) {
        if (userByUserIdData == null) {
            userByUserIdData = new UserByUserIdData();
        }

        userByUserIdData.InitUserId(p_strStoreId);
    }





    private class UserData extends MutableLiveData<User> {
        @Override
        protected void onActive() {
            super.onActive();

            currentFirebaseUser = userAuthModel.getCurrentUser();

            if (currentFirebaseUser == null) {
                setValue(null);
            } else {
                UserAsynchDao.GetUserByUserId(currentFirebaseUser.getUid(), new UserAsynchDao.UserAsynchDaoListener<User>() {
                    @Override
                    public void onComplete(User data) {
                        if (data != null) {
                            setValue(data);
                        }

                        userModelFirebase.getUserById(currentFirebaseUser.getUid(), new UserModelFirebase.GetUserByIdListener() {
                            @Override
                            public void onSuccess(User user) {
                                setValue(user);

                                UserAsynchDao.insert(user, new UserAsynchDao.UserAsynchDaoListener<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean data) {
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
        @Override
        protected void onInactive() {
            super.onInactive();
        }

        public UserData() {
            super();
            setValue(null);
        }
    }

    public interface CreateUserListener {
        void onSuccess(User user);

        void onFailure(String exceptionMessage);
    }
    public void createUser(User user,
                           String password,
                           final CreateUserListener listener) {
        userAuthModel.createUser(user, password, new UserAuthModelFirebase.CreateUserCallback() {
            @Override
            public void onSuccess(User user) {
                userModelFirebase.addUser(user, new UserModelFirebase.AddUserListener() {
                    @Override
                    public void onSuccess(User user) {
                        listener.onSuccess(user);
                    }

                    @Override
                    public void onFailure(String exceptionMessage) {
                        listener.onFailure(exceptionMessage);
                    }
                });
            }

            @Override
            public void onFailure(String exceptionMessage) {
                listener.onFailure(exceptionMessage);
            }
        });
    }



    public interface SignOutListener {
        void onSuccess();

        void onFailure(String exceptionMessage);
    }
    public void signOut(final SignOutListener listener) {
        userAuthModel.signOut();

        UserAsynchDao.removeAll(new UserAsynchDao.UserAsynchDaoListener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                if (data) {
                    userData.setValue(null);
                    listener.onSuccess();
                } else {
                    listener.onFailure("Signing out has faild.");
                }
            }
        });
    }



    public interface UpdateUserListener {
        void onSuccess();

        void onFailure(String exceptionMessage);
    }
    public void updateUser(final User user, final UpdateUserListener listener) {
        userModelFirebase.setUser(user, new UserModelFirebase.SetUserListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String exceptionMessage) {
                listener.onFailure(exceptionMessage);
            }
        });
    }

    public LiveData<User> getCurrentUser() {
        if (this.userData == null) {
            this.userData = new UserData();
        }
        //return this.userData;
        //return user;
        return this.userData;
    }
}
