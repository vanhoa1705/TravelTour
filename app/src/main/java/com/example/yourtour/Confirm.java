package com.example.yourtour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Confirm extends AppCompatActivity {
    String type;
    String userID;
    long expiredOn;
    Button cf_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        final Intent intent = getIntent();

        type = intent.getStringExtra("type");
        userID = intent.getStringExtra("userId");
        expiredOn = intent.getLongExtra("expiredOn", 0);


        cf_confirm = findViewById(R.id.cf_confirm);
        cf_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String URL ="http://35.197.153.192:3000/user/verify-otp-recovery";
                EditText temp;

                Map<String, String> params = new HashMap<>();
                params.put("userId", userID);
                temp = (EditText)findViewById(R.id.cf_NewPassword);
                String Email = temp.getText().toString();
                params.put("newPassword", Email);
                temp = (EditText) findViewById(R.id.cf_verifyCode);
                String verifyCode = temp.getText().toString();
                params.put("verifyCode", verifyCode);

                JSONObject parameters = new JSONObject(params);

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                        //Nhan chuoi Token sau khi dang nhap thanh cong
                        Toast.makeText(Confirm.this,"Success",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Confirm.this, MainActivity.class);
                        startActivity(intent);
                        // Them intent chuyen man hinh tai day
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //error giu thong tin tra ve tu server khi send that bai
                        Toast.makeText(Confirm.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });

                //Goi phuong thuc send request de gui toan bo len server
                Volley.newRequestQueue(Confirm.this).add(jsonRequest);

            }
        });


    }
}
