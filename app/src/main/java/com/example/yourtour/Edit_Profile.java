package com.example.yourtour;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Edit_Profile extends AppCompatActivity {
    String token;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);

        Intent intent = getIntent();
        token = intent.getStringExtra("Token");
        String Name = intent.getStringExtra("fullName");
        String Email = intent.getStringExtra("email");
        String Phone = intent.getStringExtra("phone");
        String Address = intent.getStringExtra("address");
        String dob = intent.getStringExtra("dob");
        String Gender = intent.getStringExtra("gender");

        EditText editText1 = findViewById(R.id.ep_Name);
        editText1.setText(Name);
        EditText editText2 = findViewById(R.id.ep_Email);
        editText2.setText(Email);
        EditText editText3 = findViewById(R.id.ep_Phone);
        editText3.setText(Phone);
//        EditText editText4 = findViewById(R.id.ep_Address);
//        editText4.setText(Address);

        final EditText editText5 = findViewById(R.id.ep_dob);
        editText5.setText(dob);
        editText5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickADate(editText5);
            }
        });

        EditText editText6 = findViewById(R.id.ep_gender);
        if (Gender.equals("Nam"))
            editText6.setText("0");
        else editText6.setText("1");


        btn = findViewById(R.id.ep_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText1 = findViewById(R.id.ep_Name);
                String Name = editText1.getText().toString();
                EditText editText2 = findViewById(R.id.ep_Email);
                final String Email = editText2.getText().toString();
                EditText editText3 = findViewById(R.id.ep_Phone);
                String Phone = editText3.getText().toString();
//                EditText editText4 = findViewById(R.id.ep_Address);
//                String Address = editText4.getText().toString();
                EditText editText5 = findViewById(R.id.ep_dob);
                String dob =editText5.getText().toString();

                EditText editText6 = findViewById(R.id.ep_gender);
                String Gender=editText6.getText().toString();

                String URL = "http://35.197.153.192:3000/user/edit-info";

                Map<String, String> params = new HashMap<>();
                params.put("fullName", Name);
                params.put("email", Email);
                params.put("phone", Phone);
                //params.put("address", Address);
                params.put("gender", Gender);
                params.put("dob", dob);

                JSONObject parameters = new JSONObject(params);

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                        Log.e("Infor: ",response.toString());
                        //Toast.makeText(ToursList.this, "" + response.toString(),Toast.LENGTH_LONG).show();

                        //lay du lieu tu json
                        try {
                            JSONObject object = response;

                            String temp1 = object.getString("message");
                            Toast.makeText(Edit_Profile.this, temp1,Toast.LENGTH_LONG).show();
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //error giu thong tin tra ve tu server khi send that bai
                        Toast.makeText(Edit_Profile.this,"Error", Toast.LENGTH_SHORT).show();

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
                Volley.newRequestQueue(Edit_Profile.this).add(jsonRequest);
            }
        });
    }
    private void PickADate(final EditText edtDate){

        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //@SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String temp = year + "-" + (month+1) + "-" + dayOfMonth;
                edtDate.setText(temp);

                //edtDate.setText(simpleDateFormat.format(temp));
            }
        }, year,month,day);
        datePickerDialog.show();
    }
}
