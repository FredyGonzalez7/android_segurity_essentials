package com.example.segurity_essentials_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private EditText compass;
    private EditText name;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference database;

    private static String TAG = "Fredy";

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
    private void registerLocalUser(String email,String name, String uid){
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String responseInsert = dbHelper.insertRow(getApplicationContext(),uid,email, name);
        Log.d(TAG, "onCreate: insert "+responseInsert);
    }

    private void signOut() { firebaseAuth.signOut(); }
    private void goToMainActivity(){ startActivity(new Intent(RegisterActivity.this,MainActivity.class)); }

    public SecretKeySpec generateKey() {
        String key = "fredy david gonz";
        return new SecretKeySpec(key.getBytes(), "AES");
    }
    
    //public String encryptMsg(String message, SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
    public byte[] encryptMsg(String message, SecretKey key) throws Exception {
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
    }

    @SuppressLint("GetInstance")
    public static String decryptMsg(byte[] cipherText, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
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
            if (localPassword.length()<6) {
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
            if (localConfirmPassword.length()<6) {
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

    private boolean validatePass(){
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

    private void registerUser(){
        if (validateForm()) {
            if (validatePass()){
                try {
                    SecretKeySpec secretKeySpec = generateKey();
                    byte[] passEncrypted;
                    passEncrypted = encryptMsg(pass.getText().toString(), secretKeySpec);
                    //Toast.makeText(getApplicationContext(), Arrays.toString(passEncrypted), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "registerUser: passEncryp "+ Arrays.toString(passEncrypted));
                    String passDecrypted = decryptMsg(passEncrypted, secretKeySpec);
                    Log.d(TAG, "registerUser: passDecrecryp "+passDecrypted);
                    //Toast.makeText(getApplicationContext(), passDecrypted, Toast.LENGTH_LONG).show();
                    createAccount(email.getText().toString().trim() , pass.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
            else Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
        }
    }
}
