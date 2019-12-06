package com.example.segurity_essentials_class;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {

    private final static String alg = "AES";
    // Definición del modo de cifrado a utilizar
    private final static String cI = "AES/CBC/PKCS5Padding";

    /**
     * https://bit502.wordpress.com/2014/06/27/codigo-java-encriptar-y-desencriptar-texto-usando-el-algoritmo-aes-con-cifrado-por-bloques-cbc-de-128-bits/
     * Función de tipo String que recibe el texto que se desea cifrar
     * param cleartext el texto sin cifrar a encriptar
     * return el texto cifrado en modo String
     * throws Exception puede devolver excepciones de los siguientes tipos: NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException
     */

    private static String key = "92AE31A79FEEB2A3"; //llave en tipo String a utilizar
    private static String iv = "0123456789ABCDEF"; // vector de inicialización a utilizar

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encryptMessage(String cleartext) throws Exception {
        Cipher cipher = Cipher.getInstance(cI);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), alg);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        //byte[] encrypted = cipher.doFinal(cleartext.getBytes());
        return Base64.getEncoder().encodeToString(cipher.doFinal(cleartext.getBytes()));
    }

    /**
     * Función de tipo String que recibe el texto que se desea descifrar
     * @param encrypted el texto cifrado en modo String
     * @return el texto desencriptado en modo String
     * @throws Exception puede devolver excepciones de los siguientes tipos: NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decryptMessage(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(cI);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), alg);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        byte[] enc = Base64.getDecoder().decode(encrypted.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(enc);
        return new String(decrypted);
    }
}