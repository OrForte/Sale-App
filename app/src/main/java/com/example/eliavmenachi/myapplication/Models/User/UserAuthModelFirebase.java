package com.example.eliavmenachi.myapplication.Models.User;

import android.support.annotation.NonNull;

import com.example.eliavmenachi.myapplication.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserAuthModelFirebase {

//    User totalUserDetailsToSave = new User();

//    public interface getAllUsersCallback {
//        void onCompleted(List<User> users);
//
//        void onCanceled();
//    }

//    public void getUsers(final String userID, final getAllUsersCallback callback) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference myRef1 = database.getReference("users");
//        final ValueEventListener listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<User> list = new LinkedList<>();
//                for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                    User user = snap.getValue(User.class);
//                    user.id = snap.getKey();
//                    if (!user.id.equals(userID)) {
//                        list.add(user);
//                    }
//                }
//
//                callback.onCompleted(list);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                callback.onCanceled();
//            }
//        };
//
//        myRef1.addListenerForSingleValueEvent(listener);
//    }

    public FirebaseUser getCurrentUser() {
        //FirebaseUser firebaseUser =
//        User user = null;
//
//        if (firebaseUser != null) {
//            user = new User();
//            user.id = firebaseUser.getUid();
//            user.username = firebaseUser.getDisplayName();
//            user.email = firebaseUser.getEmail();
//        }

        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public interface SignInCallback {
        void onSuccess(String userID, String userName);

        void onFailure(String exceptionMessage);
    }

    public void signInWithEmailAndPassword(String email, String password, final SignInCallback callback) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            callback.onFailure("Signing in has failed: " + task.getException().getMessage());
                        } else {
                            FirebaseUser user = task.getResult().getUser();
                            callback.onSuccess(user.getUid(), user.getDisplayName());
                        }
                    }
                });
    }

    public interface CreateUserCallback {
        void onSuccess(User user);

        void onFailure(String exceptionMessage);
    }

    public void createUser(final User user,
                           final String password,
                           final CreateUserCallback callback) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    String exception = task.getException().getMessage();
                    callback.onFailure("Registration has failed: " + exception);
                    //callback.onFailure("Registration has failed: " + exception);
                } else {
                    final FirebaseUser firebaseUser = task.getResult().getUser();

                    updateUserProfileAfterCreate(firebaseUser, user, callback);
                }
            }
        });
    }

    private void updateUserProfileAfterCreate(final FirebaseUser firebaseUser,
                                              final User user,
                                              final CreateUserCallback callback) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.username)
                .build();

        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    String exception = task.getException().getMessage();
                    callback.onFailure("Updating the user's profile has failed: " + exception);
                } else {
                    user.id = firebaseUser.getUid();
                    user.email = firebaseUser.getEmail();
                    user.username = user.username;
                    user.firstName = user.firstName;
                    user.lastName = user.lastName;
                    user.city = user.city;
                    user.birthDate = user.birthDate;

                    callback.onSuccess(user);
                    // TODO: CALL from user model to firebase
                    //UserModelFirebase.addUser()
                }
            }
        });
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
}
