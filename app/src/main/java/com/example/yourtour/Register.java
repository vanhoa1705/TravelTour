package com.example.yourtour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    Button btnRegisterLogin, btnRegisterRgister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegisterLogin=(Button)findViewById(R.id.btnRegisterLogin);
        btnRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnRegisterRgister=(Button)findViewById(R.id.btnRegisterRegister);
        btnRegisterRgister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //RequestQueue queue;
                //queue = Volley.newRequestQueue(activity_register.this);
                final String URL ="http://35.197.153.192:3000/user/register";
                EditText temp;
                temp=(EditText)findViewById(R.id.etEmail);
                String Email=temp.getText().toString();
                temp=(EditText)findViewById(R.id.etPhone);
                String Phone=temp.getText().toString();
                temp=(EditText)findViewById(R.id.etPassword);
                String Password=temp.getText().toString();
                temp=(EditText)findViewById(R.id.etConfirmPassword);
                String ComfirmPassword=temp.getText().toString();

                //Truyen parameter va body
                //Cac kieu du lieu deu truyen kieu string
                Map<String, String> params = new HashMap<>();
                params.put("password", Password);
                params.put("email", Email);
                params.put("phone", Phone);

                JSONObject parameters = new JSONObject(params);
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                        Toast.makeText(Register.this,"You've been registed successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //error giu thong tin tra ve tu server khi send that bai
                        Toast.makeText(Register.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
                Volley.newRequestQueue(Register.this).add(jsonRequest);
            }
        });
    }
}
