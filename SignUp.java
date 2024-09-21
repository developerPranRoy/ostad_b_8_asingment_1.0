package com.roytech.accountmanagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    Button btnlogin,btnsignup;
    ImageView imageProfile;
    TextInputEditText edName, edEmail, edPassword;
    TextView changePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        changePhoto=findViewById(R.id.changePhoto);
        btnlogin=findViewById(R.id.btnlogin);
        btnsignup=findViewById(R.id.btnsignup);
        imageProfile=findViewById(R.id.imageProfile);
        edName=findViewById(R.id.edName);
        edEmail=findViewById(R.id.edEmail);
        edPassword=findViewById(R.id.edPassword);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,Login.class));
                finish();           }
        });



        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name= edName.getText().toString();
                String password =edPassword.getText().toString();


                BitmapDrawable bitmapDrawable= (BitmapDrawable) imageProfile.getDrawable();
                Bitmap bitmap=bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,50, byteArrayOutputStream);
                byte [] imagebytes=byteArrayOutputStream.toByteArray();

                String image = Base64.encodeToString(imagebytes,Base64.DEFAULT);


                stringRequest(name,image); // volly request




            }
        });


    }


    private  void  stringRequest(  String name,String image64){

        String  email=edEmail.getText().toString();
        String url ="http://10.0.2.2/imageupload_to_xamp/signup.php";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if ( response.contains("Sign up succes")){

                    SharedPreferences sharedPreferences=getSharedPreferences("myApp",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("email",email);
                    editor.apply();

                    startActivity(new Intent(SignUp.this,MainActivity.class));
                    finish();

                }

               /* new  AlertDialog.Builder( SignUp.this)
                        .setTitle("Server response")
                        .setMessage(response)
                        .create().show();
                */

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new  AlertDialog.Builder( SignUp.this)
                        .setTitle("Server response")
                        .setMessage(error.getMessage())
                        .create().show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map myMap= new HashMap();
                try {
                    myMap.put("email",MyMethods.encryptData(edEmail.getText().toString()));
                    myMap.put("pass", MyMethods.encryptData(edPassword.getText().toString()));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                myMap.put("name",name);
                myMap.put("image",image64);
                myMap.put("key",MyMethods.MY_KEY);

                return myMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(SignUp.this);
        requestQueue.add(stringRequest);

    }











}