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
    private EditText email, pass;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.editTextEmail);
        pass = findViewById(R.id.editTextPass);

        TextView register = findViewById(R.id.textRegister);
        Button login = findViewById(R.id.buttonLogin);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        informationUser();
    }

    private void signIn(String emailSignIn, String passwordSignIn){
        firebaseAuth.signInWithEmailAndPassword(emailSignIn, passwordSignIn)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d("signIn", "signInWithEmail:success");
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loginUser(){
        if (validateForm()) {
            try {
                Encrypt encrypt = new Encrypt();
                String encryptMessage = encrypt.encryptMsg(pass.getText().toString().trim(), encrypt.generateKey());
                signIn(email.getText().toString().trim(),encryptMessage);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
        }
    }
    private void informationUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
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
        // No validar el numero de caracteres de la password en la autenticacion
        return valid;
    }
}
