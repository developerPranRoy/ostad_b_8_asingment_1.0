package com.roytech.accountmanagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Button logOut;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logOut = findViewById(R.id.logOut);

        sharedPreferences = getSharedPreferences("myApp", MODE_PRIVATE);
        textView = findViewById(R.id.textView);
        imageView=findViewById(R.id.imageView);



        try {
            MyMethods.MY_KEY = MyMethods.encryptData("pranto112233");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String email = sharedPreferences.getString("email", "");

        if (email.length() <= 0) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();


        }else {

            objRequest();
        }




        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("email","");
                editor.apply();

                startActivity( new Intent(MainActivity.this,Login.class));
                finish();




            }
        });

    }

//====================================================================================================


   private void objRequest() {

        String url = "http://10.0.2.2/imageupload_to_xamp/home.php";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("key", MyMethods.MY_KEY);
            jsonObject.put("email", sharedPreferences.getString("email", ""));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String result = response.getString("result");
                    String image = response.getString("image");

                    textView.setText(result);
                    Picasso.get().load(image).into(imageView);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Server Response")
                        .setMessage(error.getMessage())
                        .create().show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(objectRequest);
    }



}