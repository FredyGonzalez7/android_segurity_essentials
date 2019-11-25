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

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RegisterActivity extends AppCompatActivity {

    Button register;
    private EditText email;
    private EditText pass;
    private EditText conpass;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editTextEmail);
        pass = findViewById(R.id.editTextPass);
        conpass = findViewById(R.id.editTextPassCon);
        register = findViewById(R.id.buttonRegister);
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

    private void createAccount(String emailCreate, String passwordCreate) {

        firebaseAuth.createUserWithEmailAndPassword(emailCreate, passwordCreate)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "OnComplete", Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "Authentication exist.", Toast.LENGTH_SHORT).show();
                            signOut();
                            goToMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            // verificar si el usuario existe
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }

    private void signOut() {
        firebaseAuth.signOut();
    }
    private void goToMainActivity(){
        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
    }
    /*
    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);

            findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }
    }
    */

    public SecretKeySpec generateKey() {
        String clave = "fredy david gonz";
        return new SecretKeySpec(clave.getBytes(), "AES");
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

        String password = pass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pass.setError("Required.");
            valid = false;
        } else {
            pass.setError(null);
        }

        String confirmPassword = conpass.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            conpass.setError("Required.");
            valid = false;
        } else {
            conpass.setError(null);
        }

        return valid;
    }

    private boolean validatePass(){
        boolean valid = true;
        String password = pass.getText().toString();
        String confirmPassword = conpass.getText().toString();
        if (!password.equals(confirmPassword)) {
            pass.setError("Required.");
            conpass.setError("Required.");
            valid = false;
        } else {
            pass.setError(null);
            conpass.setError(null);
        }
        return valid;
    }

    private void registerUser(){
        // String emailText = email.getText().toString().trim();

        if (validateForm()) {
            if (validatePass()){
                //secretKeySpec = new SecretKeySpec(clave.getBytes(), "AES");
                SecretKeySpec secretKeySpec = generateKey();
                try {
                    byte[] passEncrypted;
                    passEncrypted = encryptMsg(pass.getText().toString(), secretKeySpec);
                    //Toast.makeText(getApplicationContext(), Arrays.toString(passEncrypted), Toast.LENGTH_LONG).show();
                    Log.d("passEncryp", "registerUser: "+ Arrays.toString(passEncrypted));
                    String passDecrypted = decryptMsg(passEncrypted, secretKeySpec);
                    Log.d("passDecrecryp", "registerUser: "+passDecrypted);
                    //Toast.makeText(getApplicationContext(), passDecrypted, Toast.LENGTH_LONG).show();
                    createAccount(email.getText().toString() , pass.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage() + "  error", Toast.LENGTH_LONG).show();
                }
            }
            else Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_LONG).show();
        }
    }
}
