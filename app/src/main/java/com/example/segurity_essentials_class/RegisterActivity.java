package com.example.segurity_essentials_class;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

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
        FirebaseUser currentUser = mAuth.getCurrentUser();

        email = findViewById(R.id.editTextEmail);
        pass = findViewById(R.id.editTextPass);
        conpass = findViewById(R.id.editTextPassCon);
        register = findViewById(R.id.buttonRegister);
        login = findViewById(R.id.textLogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().toString().equals(conpass.getText().toString())){
                    //Toast.makeText(getApplicationContext(), "Las claves coinciden", Toast.LENGTH_LONG).show();

                    secretKeySpec =  new SecretKeySpec(clave.getBytes(), "AES");

                    try {
                        byte[] claveEncryptada = encryptMsg(pass.getText().toString(),secretKeySpec);
                        //Toast.makeText(getApplicationContext(), claveEncryptada.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), claveEncryptada.toString(), Toast.LENGTH_LONG).show();
                        String claveDesencriptada = decrryptMsg(claveEncryptada,secretKeySpec);
                        Toast.makeText(getApplicationContext(), claveDesencriptada, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage()+"  error", Toast.LENGTH_LONG).show();
                    }
                    /*try {
                        String claveEncryptada = encryptMsg(pass.getText().toString(),secret);
                        Toast.makeText(getApplicationContext(), claveEncryptada.toString()+"123", Toast.LENGTH_LONG).show();
                    } catch (NoSuchAlgorithmException e) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    } catch (NoSuchPaddingException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getApplicationContext(), "dd", Toast.LENGTH_LONG).show();
                    */
                }
                else {
                    Toast.makeText(getApplicationContext(), "Las claves no coinciden", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    /*
    public static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeyException {
        return secret = new SecretKeySpec(clave.getBytes(), "AES");
    }Arrays.toString(byteArr)*/
    //public String encryptMsg(String message, SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
    public byte[] encryptMsg(String message, SecretKey key) throws Exception {
            //String text;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(message.getBytes("UTF-8"));

        /*
        if (key == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (key.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }

        byte[] raw = key.getBytes("UTF-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/Iso10126Padding");//"算法/模式/补码方式"
        IvParameterSpec ivps = new IvParameterSpec(iv.getBytes("UTF-8"));//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivps);
        byte[] encrypted = cipher.doFinal(message.getBytes("UTF-8"));
        return new String(Base64.encodeBase64(encrypted),"UTF-8");//此处使用BAES64做转码功能，同时能起到2次
        //return new String(Base64.encodeToString(cipherText),"UTF-8");
        */
    }

    public static String decrryptMsg(byte[] cipherText, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }
    /*
    public static String registrarUsuario(FirebaseUser currentUser,String email, String password){

        if (currentUser != null){

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });


        }
        else {
            return "error";
        }
    }*/

}
