package com.example.segurity_essentials_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText email, pass;
    TextView register;
    Button login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.editTextEmail);
        pass = findViewById(R.id.editTextPass);
        register = findViewById(R.id.textRegister);
        login = findViewById(R.id.buttonLogin);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this,HomeActivity.class));
                loginUser();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        //signOut();
        informationUser();
    }

    private void signIn(String emailSignIn, String passwordSignIn){
        firebaseAuth.signInWithEmailAndPassword(emailSignIn, passwordSignIn)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signIn", "signInWithEmail:success");
                            //FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void loginUser(){
        //Log.d("void", "loginUser: pre-validate");
        if (validateForm()) {
            //Log.d("if", "loginUser: validate");
            signIn(email.getText().toString().trim(),pass.getText().toString().trim());

        } else {
            Toast.makeText(MainActivity.this, "Rellene todos los campos", Toast.LENGTH_LONG).show();
        }
    }
    private void signOut() {
        firebaseAuth.signOut();
        //updateUI(null);
    }
    private void informationUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            // Name, email address, and profile photo Url
            /*String name = user.getDisplayName();
            String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();
            Toast.makeText(MainActivity.this, email ,Toast.LENGTH_SHORT).show();
            // Check if user's email is verified
            //boolean emailVerified = user.isEmailVerified();
            if (user.isEmailVerified()){
                Log.d("user", "informationUser: if");
            }
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            //String uid = user.getUid();
            */
        }
        else {
            Log.d("user null", "loginUser: null");
        }
    }
    private boolean validateForm() {
        boolean valid = true;

        String localEmail = email.getText().toString();
        if (TextUtils.isEmpty(localEmail)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String password = pass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pass.setError("Required.");
            valid = false;
        } else {
            pass.setError(null);
        }
        //validar que las password tenga por lo menos 6 caracteres
        return valid;
    }
}
