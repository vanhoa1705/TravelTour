package com.example.yourtour;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateTour extends AppCompatActivity {

    String Token;
    EditText edtName, edtStartDate, edtEndDate, edtSource,edtDestination;
    Button btnDone;
    Switch swPrivate;
    int REQUEST_CODE1=123, REQUEST_CODE2=456;
    double srcLat=0,srcLong=0,dtnLat=0,dtnLong=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);
        Intent intent = this.getIntent();
        Token = intent.getStringExtra("Token");

        btnDone = findViewById(R.id.btnCreateDone);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        edtSource=findViewById(R.id.edtSource);
        edtDestination=findViewById(R.id.edtDestination);

        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickADate(edtStartDate);
            }
        });

        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickADate(edtEndDate);
            }
        });

        edtSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickLocation(edtSource,REQUEST_CODE1);
            }
        });

        edtDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickLocation(edtDestination,REQUEST_CODE2);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get name
                edtName = findViewById(R.id.edtName);
                String Name = edtName.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();

                //get startDate
                String stDate = edtStartDate.getText().toString();
                try {
                    date = sdf.parse(stDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long temp = date.getTime();
                String startDate = Long.toString(temp);

//                edtStartDate.setText(startDate);

                //get endDate
                String eDate = edtEndDate.getText().toString();
                try {
                    date = sdf.parse(eDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                temp = date.getTime();
                String endDate = Long.toString(temp);

                //get sourceLat,sourceLong, des
                String sourceLat = Double.toString(srcLat);
                String sourceLong = Double.toString(srcLong);
                String desLat = Double.toString(dtnLat);
                String desLong = Double.toString(dtnLong);

                //get isPrivate
                swPrivate = findViewById(R.id.switchPrivateTour);
                Boolean isPrivate = swPrivate.isChecked();

                if ((edtName.getText().length() == 0) || (edtStartDate.getText().length()==0) || (edtEndDate.getText().length() == 0) || (srcLat == 0) || (srcLong == 0) || (dtnLat == 0) || (dtnLong == 0)) {
                    Toast.makeText(CreateTour.this, "Please fill all necessary informations!!!", Toast.LENGTH_LONG).show();
                } else {
                    //POST
                    final String URL = "http://35.197.153.192:3000/tour/create";
                    final HashMap<String, String> params = new HashMap<>();
                    params.put("name", Name);
                    params.put("startDate", startDate);
                    params.put("endDate", endDate);
                    params.put("sourceLat", sourceLat);
                    params.put("sourceLong", sourceLong);
                    params.put("desLat", desLat);
                    params.put("desLong", desLong);
                    params.put("isPrivate", isPrivate.toString());

                    JSONObject parameters = new JSONObject(params);
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                            Toast.makeText(CreateTour.this, "You've created a new tour successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        //error giu thong tin tra ve tu server khi send that bai
//                        Toast.makeText(CreateTour.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null) {
                                String statusCode = String.valueOf(networkResponse.statusCode);
                                switch (statusCode) {
                                    case "400":
                                        Toast.makeText(CreateTour.this, "ERROR 400", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "500":
                                        Toast.makeText(CreateTour.this, "ERROR 500", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(CreateTour.this, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            //get Authorization
                            Intent t = getIntent();
                            String Authorization = t.getStringExtra("Token");
                            headers.put("Authorization", Authorization);
                            return headers;
                        }
                    };
                    Volley.newRequestQueue(CreateTour.this).add(jsonRequest);
                }
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
                String temp = dayOfMonth + "/" + (month+1) + "/" + year;
                edtDate.setText(temp);

                //edtDate.setText(simpleDateFormat.format(temp));
            }
        }, year,month,day);
        datePickerDialog.show();
    }

    private void PickLocation(final EditText edtSource, int REQUEST_CODE){
        Intent intent = new Intent(CreateTour.this, map.class);
        intent.putExtra("REQUEST_CODE",REQUEST_CODE);
        startActivityForResult(intent,REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE1) {
            srcLat=data.getDoubleExtra("Lat",0);
            srcLong=data.getDoubleExtra("Long",0);
            if (srcLat!=0&srcLong!=0)
                edtSource.setText(srcLat + " - " + srcLong);
            }
        else if (requestCode==REQUEST_CODE2){
            dtnLat=data.getDoubleExtra("Lat",0);
            dtnLong=data.getDoubleExtra("Long",0);
            if (srcLat!=0&srcLong!=0)
                edtDestination.setText(dtnLat + " - " + dtnLong);
        }
    }
}
