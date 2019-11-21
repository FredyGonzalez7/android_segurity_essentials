package com.example.segurity_essentials_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
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
import com.google.firebase.database.core.Tag;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RegisterActivity extends AppCompatActivity {

    Button register;
    private EditText email;
    private EditText pass;
    private EditText conpass;
    private TextView login;

    private static String clave = "fredy david gonz";
    private SecretKeySpec secretKeySpec;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        email = findViewById(R.id.editTextEmail);
        pass = findViewById(R.id.editTextPass);
        conpass = findViewById(R.id.editTextPassCon);
        register = findViewById(R.id.buttonRegister);
        login = findViewById(R.id.textLogin);

        //findViewById(R.id.buttonRegister).setOnClickListener((View.OnClickListener) this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    //Toast.makeText(getApplicationContext(), "Las claves coinciden", Toast.LENGTH_LONG).show();
                    if (validatePass()){
                        //secretKeySpec = new SecretKeySpec(clave.getBytes(), "AES");
                        secretKeySpec = generateKey();
                        try {
                            byte[] passEncrypted;
                            passEncrypted = encryptMsg(pass.getText().toString(), secretKeySpec);
                            Toast.makeText(getApplicationContext(), Arrays.toString(passEncrypted), Toast.LENGTH_LONG).show();
                            String passDecrypted = decryptMsg(passEncrypted, secretKeySpec);
                            Toast.makeText(getApplicationContext(), passDecrypted, Toast.LENGTH_LONG).show();
                            //createAccount(email.getText().toString() , pass.getText().toString());
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
        });
    }

    private void createAccount(String emailCreate, String passwordCreate) {
        Toast.makeText(RegisterActivity.this, emailCreate+" - "+passwordCreate, Toast.LENGTH_SHORT).show();
        if (!validateForm()) {
            Toast.makeText(getApplicationContext(), "Revise los datos", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(emailCreate, passwordCreate)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "OnComplete", Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "Authentication exist.", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
        });
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
}
