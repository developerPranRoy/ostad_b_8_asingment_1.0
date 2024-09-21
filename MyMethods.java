package com.roytech.accountmanagement;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MyMethods {


    public  static String MY_KEY="";

    public  static  String encryptData( String text ) throws  Exception{

        String plainText = text;
        byte [] plaintextByte= plainText.getBytes("UTF-8");

        String password ="A\\OM3J2:1XO+*=2%";
        byte [] passwordBytes=password.getBytes("UTF-8");

        //encrypt

        SecretKeySpec keySpec=new SecretKeySpec(passwordBytes, "AES");
        Cipher cipher= Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,keySpec);
        byte [] secretBytes= cipher.doFinal(plaintextByte);

        //encode

        String encodedString = Base64.encodeToString(secretBytes,Base64.DEFAULT);

        return encodedString ;
    }
















}
