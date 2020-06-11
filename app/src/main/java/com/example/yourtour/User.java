package com.example.yourtour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class User extends AppCompatActivity {
    String token;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        token = intent.getStringExtra("Token");
        GetInf();

        btn = findViewById(R.id.user_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(User.this, Edit_Profile.class);
                newIntent.putExtra("Token",token);
                TextView temp = findViewById(R.id.user_Name);
                newIntent.putExtra("fullName",temp.getText());

                TextView temp1 = findViewById(R.id.user_Email);
                newIntent.putExtra("email",temp1.getText());

                TextView temp2 = findViewById(R.id.user_Phone);
                newIntent.putExtra("phone",temp2.getText());

                TextView temp3 = findViewById(R.id.user_Address);
                newIntent.putExtra("address",temp3.getText());

                TextView temp4 = findViewById(R.id.user_dob);
                newIntent.putExtra("dob",temp4.getText());

                TextView temp5 = findViewById(R.id.user_gender);
                newIntent.putExtra("gender",temp5.getText());

                startActivity(newIntent);
            }
        });

    }

    //Tao Menu Add
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_user,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Bat su kien Add chuyen qua Create
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuMyTour: {
                Intent newIntent = new Intent(User.this, My_tour.class);
                newIntent.putExtra("Token",token);
                startActivity(newIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void GetInf() {
        //Truyen parameter va body
        //Cac kieu du lieu deu truyen kieu string

        String URL = "http://35.197.153.192:3000/user/info";

        Map<String, String> params = new HashMap<>();

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                Log.e("Infor: ",response.toString());
                //Toast.makeText(ToursList.this, "" + response.toString(),Toast.LENGTH_LONG).show();

                //lay du lieu tu json
                try {
                    JSONObject object = response;
//                    String temp = object.getString("id");
//                    TextView textView = findViewById(R.id.user_Name);
//                    textView.setText(temp);

                    String temp1 = object.getString("fullName");
                    TextView textView1 = findViewById(R.id.user_Name);
                    textView1.setText(temp1);

                    String temp7 = object.getString("id");
                    TextView textView7 = findViewById(R.id.user_ID);
                    textView7.setText(temp7);

                    String temp2 = object.getString("email");
                    TextView textView2 = findViewById(R.id.user_Email);
                    textView2.setText(temp2);

                    String temp3 = object.getString("phone");
                    TextView textView3 = findViewById(R.id.user_Phone);
                    textView3.setText(temp3);

                    String temp4 = object.getString("address");
                    TextView textView4 = findViewById(R.id.user_Address);
                    textView4.setText(temp4);

                    String temp5 = object.getString("dob");
                    TextView textView5 = findViewById(R.id.user_dob);
                    textView5.setText(temp5);

                    String temp6 = object.getString("gender");
                    TextView textView6 = findViewById(R.id.user_gender);
                    //if(temp6.equals(null))temp6 = "0";
                    if(temp6.equals("0")) textView6.setText("Nam");
                    else textView6.setText("Nữ");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //error giu thong tin tra ve tu server khi send that bai
                Toast.makeText(User.this,"Can not get any Tour !", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Gui header
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(jsonRequest);
    }
}


