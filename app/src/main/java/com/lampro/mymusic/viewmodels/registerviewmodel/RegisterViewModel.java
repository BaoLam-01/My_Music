package com.lampro.mymusic.viewmodels.registerviewmodel;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lampro.mymusic.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterViewModel extends AndroidViewModel {


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = firebaseDatabase.getReference("List User");


    public final MutableLiveData<List<User>> listUserLiveData = new MutableLiveData<>();
    public final MutableLiveData<Task<AuthResult>> taskResult = new MutableLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }

    public void getListUser(){
        List<User> listUser = new ArrayList<>();
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    listUser.add(user);
                    listUserLiveData.setValue(listUser);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user == null || listUser == null || listUser.isEmpty()) {
                    return;
                }
                for (int i = 0; i < listUser.size(); i++) {
                    if (Objects.equals(listUser.get(i).getUserId(), user.getUserId())) {
                        listUser.set(i, user);
                        listUserLiveData.setValue(listUser);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null || listUser == null || listUser.isEmpty()) {
                    return;
                }
                for (int i = 0; i < listUser.size(); i++) {
                    if (Objects.equals(listUser.get(i).getUserId(), user.getUserId())) {
                        listUser.remove(i);
                        listUserLiveData.setValue(listUser);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void register(String email, String password){



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        taskResult.setValue(task);
                        if (task.isSuccessful()) {
                            saveUserToFirebase(email,password);
                        }
                    }
                });

    }

    private void saveUserToFirebase(String email, String password) {
         new Thread(() -> {

            DatabaseReference newUserRef = userRef.push();
            String randomId = newUserRef.getKey();
            User newUser = new User(randomId, null, email, password);
            newUserRef.setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }).start();
    }
}
