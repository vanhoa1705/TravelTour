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
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MT_SP_Create extends AppCompatActivity {
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mt__sp__create);
        done = findViewById(R.id.cr_done);

        final String Token,tourID, id,serviceId,name,address,splat,splong,arrive,leave,serviceTypeID,minCost,maxCost, index;
        Intent intent=getIntent();
        Token=intent.getStringExtra("Token");
        tourID=intent.getStringExtra("tourID");
        splat=intent.getStringExtra("Lat");
        splong=intent.getStringExtra("Long");





        final EditText edtName, edtAddress, edtProvinceID, edtLocation,edtArrive,edtLeave,edtService,edtMin,edtMax,edtIndex;
        edtName=this.findViewById(R.id.cr_Name);
        edtLocation=this.findViewById(R.id.cr_location);
        edtArrive=this.findViewById(R.id.cr_arriveAt);
        edtLeave=this.findViewById(R.id.cr_leaveAt);
        edtService=this.findViewById(R.id.cr_service);
        edtMin=this.findViewById(R.id.cr_minCost);
        edtMax=this.findViewById(R.id.cr_maxCost);

        edtLocation.setText(splat + " , " + splong);


        edtArrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickADate(edtArrive);
            }
        });

        edtLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickADate(edtLeave);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get name
                final String serviceId,name,address, provinceID,arrive,leave,serviceTypeID,minCost,maxCost, index;

                String Name = edtName.getText().toString(); ;
                serviceTypeID = edtService.getText().toString();
                minCost = edtMin.getText().toString();
                maxCost = edtMax.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();

                //get startDate
                String stDate = edtArrive.getText().toString();
                try {
                    date = sdf.parse(stDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long temp = date.getTime();
                String startDate = Long.toString(temp);

//                edtStartDate.setText(startDate);

                //get endDate
                String eDate = edtLeave.getText().toString();
                try {
                    date = sdf.parse(eDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                temp = date.getTime();
                String endDate = Long.toString(temp);


                //POST
                final String URL = "http://35.197.153.192:3000/tour/set-stop-points";
                final Map<String, String> params = new HashMap<>();

                JSONObject parameters = new JSONObject(params);

                try {
                    parameters = new JSONObject("{\"tourId\": "+tourID+",\"stopPoints\": [{\"arrivalAt\": "+startDate+",\"lat\": "+splat+",\"leaveAt\": "+endDate+",\"long\": "+splong+",\"maxCost\": "+maxCost+",\"minCost\": "+minCost+",\"name\": \""+Name+"\",\"serviceTypeId\": "+serviceTypeID+"}],\"deleteIds\": [0]}");
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("abcd:", response.toString());
                        //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                        Toast.makeText(MT_SP_Create.this, "Update tour successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
//                        //error giu thong tin tra ve tu server khi send that bai
//                        Toast.makeText(UpdateTour.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            String statusCode = String.valueOf(networkResponse.statusCode);
                            switch (statusCode) {
                                case "400":
                                    Toast.makeText(MT_SP_Create.this, "Bad request!!!", Toast.LENGTH_SHORT).show();
                                    break;
                                case "404":
                                    Toast.makeText(MT_SP_Create.this, "Tour is not found", Toast.LENGTH_SHORT).show();
                                    break;
                                case "403":
                                    Toast.makeText(MT_SP_Create.this, "Not permission to update tour info", Toast.LENGTH_SHORT).show();
                                    break;
                                case "500":
                                    Toast.makeText(MT_SP_Create.this, "Server error on updating tour info", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(MT_SP_Create.this, "ERROR", Toast.LENGTH_SHORT).show();
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
                Volley.newRequestQueue(MT_SP_Create.this).add(jsonRequest);
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
}
