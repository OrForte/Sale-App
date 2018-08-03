package com.example.eliavmenachi.myapplication.Models.User;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.example.eliavmenachi.myapplication.Entities.UserPreview;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
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



    private class UserPreviewData extends MutableLiveData<List<UserPreview>> {
        @Override
        protected void onActive() {
            super.onActive();

            UserPreviewAsyncDao.GetAllPreviewUsers(new UserPreviewAsyncDao.UserAsynchDaoListener<List<UserPreview>>() {
                @Override
                public void onComplete(final List<UserPreview> data) {
                    setValue(data);
                    userModelFirebase.getAllUsers(new UserModelFirebase.GetAllUsersListener() {
                        @Override
                        public void onSuccess(final List<User> users) {
                            final List<UserPreview> lstUserPreview = new LinkedList<UserPreview>();
                            if (users != null) {
                                for (User currUser : users) {
                                    if (currUser != null) {
                                        UserPreview preview = new UserPreview();
                                        preview.id = currUser.id;
                                        preview.username = currUser.username;
                                        lstUserPreview.add(preview);
                                    }
                                }
                            }
                            setValue(lstUserPreview);

                            List<UserPreview> lstToDelete = new LinkedList<UserPreview>();

                            lstToDelete = GetUsersThatInSqlLiteButNotInFireBase(lstUserPreview, data);
                            if (lstToDelete.size() > 0) {

                                UserPreviewAsyncDao.updateDeletedSales(lstToDelete, new UserPreviewAsyncDao.UserAsynchDaoListener<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean data) {
                                        UserPreviewAsyncDao.insertAll(lstUserPreview, new UserPreviewAsyncDao.UserAsynchDaoListener<Boolean>() {
                                            @Override
                                            public void onComplete(Boolean data) {
                                            }
                                        });
                                    }
                                });
                            }
                            else
                            {
                                UserPreviewAsyncDao.insertAll(lstUserPreview, new UserPreviewAsyncDao.UserAsynchDaoListener<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean data) {
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }

        public List<UserPreview> GetUsersThatInSqlLiteButNotInFireBase(List<UserPreview> dataFireBase, List<UserPreview> dataSqlLite)
        {
            List<UserPreview> returnData = new LinkedList<UserPreview>();

            boolean bIsIn = false;
            for(UserPreview saleSqlLite : dataSqlLite)
            {
                bIsIn = false;
                for(UserPreview saleFireBase : dataFireBase)
                {
                    if (saleSqlLite.id.contains(saleFireBase.id))
                    {
                        bIsIn = true;
                        break;
                    }
                }
                if (bIsIn == false)
                {
                    returnData.add(saleSqlLite);
                }
            }

            return returnData;
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }

        public UserPreviewData() {
            super();
            setValue(new LinkedList<UserPreview>());
        }
    }
    public UserPreviewData userPreviewData = new UserPreviewData();
    public LiveData<List<UserPreview>> getUsersPreviewData() {
        return userPreviewData;
    }



    private class UserPreviewDataByUseId extends MutableLiveData<UserPreview> {
        String m_userId = "";

        @Override
        protected void onActive() {
            super.onActive();

            UserPreviewAsyncDao.GetUserPreviewByUserId(m_userId, new UserPreviewAsyncDao.UserAsynchDaoListener<UserPreview>() {
                @Override
                public void onComplete(UserPreview data) {
                    setValue(data);
                    userModelFirebase.getUserById(m_userId, new UserModelFirebase.GetUserByIdListener() {
                        @Override
                        public void onSuccess(User user) {
                            UserPreview preview = new UserPreview();
                            if (user != null) {
                                preview = new UserPreview();
                                preview.id = user.id;
                                preview.username = user.username;
                                setValue(preview);

                                UserPreviewAsyncDao.insert(preview, new UserPreviewAsyncDao.UserAsynchDaoListener<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean data) {
                                    }
                                });
                            }
                            else
                            {
                                setValue(preview);
                            }
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }
        public UserPreviewDataByUseId() {
            super();
            setValue(new UserPreview());
        }
        public void InitUserPreviewId(String p_userId) {
            m_userId = p_userId;
        }
    }
    public UserPreviewDataByUseId userPreviewDataByUseId = new UserPreviewDataByUseId();
    public LiveData<UserPreview> getUserPreviewByUserId(String p_userId) {
        return userPreviewDataByUseId;
    }
    public void InitUserPreviewId(String p_strStoreId) {
        if (userPreviewDataByUseId == null) {
            userPreviewDataByUseId = new UserPreviewDataByUseId();
        }

        userPreviewDataByUseId.InitUserPreviewId(p_strStoreId);
    }

    public interface CreateUserListener {
        void onSuccess(User user);

        void onFailure(String exceptionMessage);
    }
    public void createUser(User user,
                           String email,
                           String password,
                           final CreateUserListener listener) {
        userAuthModel.createUser(user, email, password, new UserAuthModelFirebase.CreateUserCallback() {
            @Override
            public void onSuccess(User user) {
                userModelFirebase.addUser(user, new UserModelFirebase.AddUserListener() {
                    @Override
                    public void onSuccess(final User userData) {
                        UserPreview preview = new UserPreview();
                        preview.username = userData.username;
                        preview.id = userData.id;

                        UserPreviewAsyncDao.insert(preview, new UserPreviewAsyncDao.UserAsynchDaoListener<Boolean>() {
                            @Override
                            public void onComplete(Boolean data) {
                                listener.onSuccess(userData);
                            }
                        });
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
                UserPreview preview = new UserPreview();
                preview.username = user.username;
                preview.id = user.id;

                UserPreviewAsyncDao.insert(preview, new UserPreviewAsyncDao.UserAsynchDaoListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        listener.onSuccess();
                    }
                });
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
