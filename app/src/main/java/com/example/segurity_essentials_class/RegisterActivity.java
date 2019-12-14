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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private EditText compass;
    private EditText name;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        email = findViewById(R.id.editTextEmail);
        pass = findViewById(R.id.editTextPass);
        compass = findViewById(R.id.editTextPassCon);
        name = findViewById(R.id.editTextName);

        Button register = findViewById(R.id.buttonRegister);
        TextView login = findViewById(R.id.textLogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMainActivity();
            }
        });
    }

    private void registerUser() {
        if (validateForm()) {
            if (validatePass()) {
                try {
                    Encrypt encrypt = new Encrypt();
                    String encryptMessage;
                    encryptMessage = encrypt.encryptMsg(pass.getText().toString().trim(), encrypt.generateKey());
                    createAccount(email.getText().toString().trim(), encryptMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            } else
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
        }
    }

    private void createAccount(final String emailCreate, String passwordCreate) {
        firebaseAuth.createUserWithEmailAndPassword(emailCreate, passwordCreate)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // registrar en la DBRealTime y en la DBLocal
                            onAuthSuccess(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()));
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "Register exist.", Toast.LENGTH_SHORT).show();
                            signOut();
                            goToMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            // verificar si el usuario existe
                            Toast.makeText(RegisterActivity.this, "Register failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //registrar en la DBRealTime y en DBLocal
    private void onAuthSuccess(FirebaseUser firebaseUser) {
        String localName = name.getText().toString();
        // Write new user
        writeNewUser(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.getUid(), localName);
        registerLocalUser(firebaseUser.getEmail(), localName, firebaseUser.getUid());
    }

    // registrar en la DBRealTime
    private void writeNewUser(String userId, String email, String uid, String name) {
        User user = new User(email, uid, name);
        //DatabaseReference databaseReference = database.getReference();
        database.child("user").child(userId).setValue(user);
    }

    // registrar en la DBLocal
    private void registerLocalUser(String email, String name, String uid) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String responseInsert = dbHelper.insertRow(getApplicationContext(), uid, email, name);
        Log.d("Fredy", "onCreate: insert " + responseInsert);
    }

    private void signOut() {
        firebaseAuth.signOut();
    }

    private void goToMainActivity() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
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

        String localPassword = pass.getText().toString().trim();
        if (TextUtils.isEmpty(localPassword)) {
            pass.setError("Required.");
            valid = false;
        } else {
            if (localPassword.length() < 6) {
                pass.setError("Minimum 6 digits required.");
                valid = false;
            } else {
                pass.setError(null);
            }
        }

        String localConfirmPassword = compass.getText().toString().trim();
        if (TextUtils.isEmpty(localConfirmPassword)) {
            compass.setError("Required.");
            valid = false;
        } else {
            if (localConfirmPassword.length() < 6) {
                compass.setError("Minimum 6 digits required.");
                valid = false;
            } else {
                compass.setError(null);
            }
        }

        String localName = name.getText().toString();
        if (TextUtils.isEmpty(localName)) {
            name.setError("Required.");
            valid = false;
        } else {
            name.setError(null);
        }
        return valid;
    }

    private boolean validatePass() {
        boolean valid = true;
        String password = pass.getText().toString();
        String confirmPassword = compass.getText().toString();
        if (!password.equals(confirmPassword)) {
            pass.setError("Required.");
            compass.setError("Required.");
            valid = false;
        } else {
            pass.setError(null);
            compass.setError(null);
        }
        return valid;
    }
}
