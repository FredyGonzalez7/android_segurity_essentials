package com.example.segurity_essentials_class;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class HomeActivity extends AppCompatActivity {

    TextView signOut, email, nombres, latLong;
    Button salvar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nombres = findViewById(R.id.textViewNombres);
        email = findViewById(R.id.textViewEmail);
        latLong = findViewById(R.id.textViewLatLong);
        signOut = findViewById(R.id.textViewSignOut);

        firebaseAuth = FirebaseAuth.getInstance();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                nombres.setText("");
                email.setText("");
                latLong.setText("");
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
            }
        });
        // verificar si hay un currentUser
        informationUser();
    }


    private void signOut() {
        firebaseAuth.signOut();
    }
    @SuppressLint("SetTextI18n")
    private void informationUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (Objects.equals(user.getDisplayName(), "") || user.getDisplayName()==null){
                nombres.setText("Not registered");
            }
            else {
                nombres.setText(user.getDisplayName());
            }
            email.setText(user.getEmail());
            latLong.setText(getLatLong());
        }
        else {
            Log.d("user null", "loginUser: null");
        }
    }

    private String getLatLong(){
        return "123";
    }
}
