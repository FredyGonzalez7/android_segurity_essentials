package com.example.segurity_essentials_class;

import android.annotation.SuppressLint;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class Encrypt {
    /*
    private final static String alg = "AES";
    // Definición del modo de cifrado a utilizar
    private final static String cI = "AES/CBC/PKCS5Padding";

    /**
     * https://bit502.wordpress.com/2014/06/27/codigo-java-encriptar-y-desencriptar-texto-usando-el-algoritmo-aes-con-cifrado-por-bloques-cbc-de-128-bits/
     * Función de tipo String que recibe el texto que se desea cifrar
     * param cleartext el texto sin cifrar a encriptar
     * return el texto cifrado en modo String
     * throws Exception puede devolver excepciones de los siguientes tipos: NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException
     *

    private static String key = "92AE31A79FEEB2A3"; //llave en tipo String a utilizar
    private static String iv = "0123456789ABCDEF"; // vector de inicialización a utilizar

    String encryptMessage(String cleartext) throws Exception {
        Cipher cipher = Cipher.getInstance(cI);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), alg);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        //byte[] encrypted = cipher.doFinal(cleartext.getBytes());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(cipher.doFinal(cleartext.getBytes()));
        }
        else {return null; }
    }

    /**
     * Función de tipo String que recibe el texto que se desea descifrar
     * @param encrypted el texto cifrado en modo String
     * @return el texto desencriptado en modo String
     * @throws Exception puede devolver excepciones de los siguientes tipos: NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException
     *
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
    */

    SecretKeySpec generateKey() {
        String key = "fredy david gonz";
        return new SecretKeySpec(key.getBytes(), "AES");
    }

    //public String encryptMsg(String message, SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
    String encryptMsg(String message, SecretKey key) throws Exception {
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        String messageEncrypt;
        messageEncrypt = android.util.Base64.encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)), 32);
        return messageEncrypt;
    }
}