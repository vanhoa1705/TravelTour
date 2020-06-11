package com.example.yourtour;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdateTour extends AppCompatActivity {

    String Token,tourID,name,startDate,endDate,adults,childs,minCost,maxCost,status;
     String isPrivate;
    EditText edtName, edtStartDate, edtEndDate, edtSource,edtDestination,edtadults,edtchilds,edtminCost,edtmaxCost;
    Button btnDone;
    Switch swPrivate;
    EditText edtstatus;
    int REQUEST_CODE1=123,REQUEST_CODE2=456;
    double srcLat=0,srcLong=0,dtnLat=0,dtnLong=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tour);

        Intent intent = this.getIntent();
        Token = intent.getStringExtra("Token");
        status=intent.getStringExtra("status");
        tourID=intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        startDate=intent.getStringExtra("startDate");
        endDate=intent.getStringExtra("endDate");
        minCost=intent.getStringExtra("minCost");
        maxCost=intent.getStringExtra("maxCost");
        adults=intent.getStringExtra("adults");
        childs=intent.getStringExtra("childs");
        isPrivate=intent.getStringExtra("isPrivate");


        btnDone = findViewById(R.id.btnCreateDone);
        edtName=this.findViewById(R.id.utName);
        edtStartDate = findViewById(R.id.utStartDate);
        edtEndDate = findViewById(R.id.utEndDate);
        edtSource=findViewById(R.id.utSource);
        edtDestination=findViewById(R.id.utDestination);
        edtadults=this.findViewById(R.id.utadults);
        edtchilds=this.findViewById(R.id.utchilds);
        edtminCost=this.findViewById(R.id.utminCost);
        edtmaxCost=this.findViewById(R.id.utmaxCost);
        edtstatus=this.findViewById(R.id.utstatus);
        swPrivate=this.findViewById(R.id.utPravate);

        edtName.setText(name);
        edtStartDate.setText(startDate);
        edtEndDate.setText(endDate);
        if (adults!=null)
            edtadults.setText(adults);
        if (childs!=null)
            edtchilds.setText(childs);
        if (minCost!=null)
            edtminCost.setText(minCost);
        if (maxCost!=null)
            edtmaxCost.setText(maxCost);
        if (isPrivate.equals("true"))
            swPrivate.setChecked(true);
        else swPrivate.setChecked(false);

        if (status.equals("-1"))
            edtstatus.setText("Canceled");
        else if (status.equals("0"))
            edtstatus.setText("Open");
        else if (status.equals("1"))
            edtstatus.setText("Started");
        else if (status.equals("2"))
            edtstatus.setText("Closed");

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
                String ad=edtadults.getText().toString();
                String ch=edtchilds.getText().toString();
                String min=edtminCost.getText().toString();
                String max=edtmaxCost.getText().toString();
                String st=edtstatus.getText().toString();
                String iP;
                if (swPrivate.isChecked())
                    iP="true";
                else iP="false";

                    //POST
                    final String URL = "http://35.197.153.192:3000/tour/update-tour";
                    final Map<String, String> params = new HashMap<>();
                    params.put("id",tourID);
                    params.put("name", Name);
                    params.put("startDate", startDate);
                    params.put("endDate", endDate);
                    params.put("sourceLat", sourceLat);
                    params.put("sourceLong", sourceLong);
                    params.put("desLat", desLat);
                    params.put("desLong", desLong);
                    params.put("adults",ad);
                    params.put("childs",ch);
                    params.put("minCost",min);
                    params.put("maxCost",max);
                    params.put("status",st);
                    params.put("isPrivate",iP);

                    JSONObject parameters = new JSONObject(params);
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("abcd:", response.toString());
                            //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                            Toast.makeText(UpdateTour.this, "Update tour successfully", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(UpdateTour.this, "Fill Status with number!!!", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "404":
                                        Toast.makeText(UpdateTour.this, "Tour is not found", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "403":
                                        Toast.makeText(UpdateTour.this, "Not permission to update tour info", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "500":
                                        Toast.makeText(UpdateTour.this, "Server error on updating tour info", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(UpdateTour.this, "ERROR", Toast.LENGTH_SHORT).show();
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
                    Volley.newRequestQueue(UpdateTour.this).add(jsonRequest);
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
        Intent intent = new Intent(UpdateTour.this, map.class);
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
