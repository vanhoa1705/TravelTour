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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class forgot_password extends AppCompatActivity {
    Button fp_btnConfirm, fp_btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        fp_btnLogin = findViewById(R.id.fp_btnLogin);
        fp_btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fp_btnConfirm = findViewById(R.id.fp_btnConfirm);
        fp_btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String URL ="http://35.197.153.192:3000/user/request-otp-recovery";
                EditText temp;
                temp = (EditText)findViewById(R.id.fp_Email);
                String Email = temp.getText().toString();

                Map<String, String> params = new HashMap<>();
                params.put("type", "email");
                params.put("value", Email);

                JSONObject parameters = new JSONObject(params);
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject object = response;
                            String userID = object.getString("userId");
                            String type = object.getString("type");
                            int expiredOn = object.getInt("expiredOn");
                            Intent intent = new Intent(forgot_password.this, Confirm.class);
                            intent.putExtra("userId", userID);
                            intent.putExtra("type", type);
                            intent.putExtra("expiredOn", expiredOn);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //error giu thong tin tra ve tu server khi send that bai
                        Toast.makeText(forgot_password.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
                Volley.newRequestQueue(forgot_password.this).add(jsonRequest);
            }
        });

    }
}
