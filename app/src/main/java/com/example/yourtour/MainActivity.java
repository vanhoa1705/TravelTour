package com.example.yourtour;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText edtUser,edtPass;
    TextView forgotPassword;
    Button  btnLogin,btnRegister;
    LoginButton btnLoginFB;
    String Token;

    private static final String EMAIL = "email";
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Add code to print out the key hash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.example.yourtour",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPassWord);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginFB = (LoginButton) findViewById(R.id.btnFB);
        btnRegister=findViewById(R.id.btnRegister);
        forgotPassword = findViewById(R.id.tvForgot);

        callbackManager = CallbackManager.Factory.create();

        btnLoginFB.setReadPermissions("email");
        btnLoginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String User = edtUser.getText().toString();
                final String PassWord = edtPass.getText().toString();
                SendRequestLogin(User,PassWord);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, forgot_password.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    //Get token facebook
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            final String URL = "http://35.197.153.192:3000/user/login/by-facebook";

            //Truyen parameter va body
            //Cac kieu du lieu deu truyen kieu string
            Map<String, String> params = new HashMap<>();
            params.put("accessToken", currentAccessToken.getToken());

            JSONObject parameters = new JSONObject(params);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                    //Nhan chuoi Token sau khi dang nhap thanh cong
                    try {
                        Token = response.getString("token");
                        Intent intent = new Intent(MainActivity.this, ToursList.class);
                        intent.putExtra("Token",Token);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Them intent chuyen man hinh tai day
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //error giu thong tin tra ve tu server khi send that bai
                    Toast.makeText(MainActivity.this,"Login by facebook failed",Toast.LENGTH_LONG).show();
                }
            });

            //Goi phuong thuc send request de gui toan bo len server
            Volley.newRequestQueue(MainActivity.this).add(jsonRequest);
        }
    };


    public void SendRequestLogin(String User, String PassWord)
    {
        final String URL = "http://35.197.153.192:3000/user/login";

        //Truyen parameter va body
        //Cac kieu du lieu deu truyen kieu string
        Map<String, String> params = new HashMap<>();
        params.put("emailPhone", User);
        params.put("password", PassWord);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                //Nhan chuoi Token sau khi dang nhap thanh cong
                try {
                    Token = response.getString("token");
                    Intent intent = new Intent(MainActivity.this, ToursList.class);
                    intent.putExtra("Token",Token);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Them intent chuyen man hinh tai day
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //error giu thong tin tra ve tu server khi send that bai
                Toast.makeText(MainActivity.this,"Wrong User or Password",Toast.LENGTH_LONG).show();
            }
        });

        //Goi phuong thuc send request de gui toan bo len server
        Volley.newRequestQueue(this).add(jsonRequest);
    }
}
