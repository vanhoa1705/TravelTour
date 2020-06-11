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

public class MT_SP_Update extends AppCompatActivity {
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mt__sp__update);
        done = findViewById(R.id.usp_done);

        final String Token,tourID, id,serviceId,name,address,splat,splong,arrive,leave,serviceTypeID,minCost,maxCost, index;
        Intent intent=getIntent();
        Token=intent.getStringExtra("Token");
        tourID=intent.getStringExtra("tourID");
        id=intent.getStringExtra("id");
        serviceId=intent.getStringExtra("serviceId");
        name=intent.getStringExtra("name");
        address=intent.getStringExtra("address");
        splat=intent.getStringExtra("lat");
        splong=intent.getStringExtra("long");
        arrive=intent.getStringExtra("arriveAt");
        leave=intent.getStringExtra("leaveAt");
        serviceTypeID=intent.getStringExtra("serviceTypeId");
        minCost=intent.getStringExtra("minCost");
        maxCost=intent.getStringExtra("maxCost");


        final EditText edtName,edtLocation,edtArrive,edtLeave,edtService,edtMin,edtMax,edtIndex;
        edtName=this.findViewById(R.id.usp_Name);
        edtLocation=this.findViewById(R.id.usp_location);
        edtArrive=this.findViewById(R.id.usp_arriveAt);
        edtLeave=this.findViewById(R.id.usp_leaveAt);
        edtService=this.findViewById(R.id.usp_service);
        edtMin=this.findViewById(R.id.usp_minCost);
        edtMax=this.findViewById(R.id.usp_maxCost);
        edtIndex=this.findViewById(R.id.usp_index);


        edtName.setText(name);
        edtLocation.setText(splat + " , " + splong);
        edtArrive.setText(arrive);
        edtLeave.setText(leave);
        edtService.setText(serviceTypeID);
        edtMin.setText(minCost);
        edtMax.setText(maxCost);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get name
                String Name = edtName.getText().toString(); ;

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

//                //get sourceLat,sourceLong, des
//                String sourceLat = Double.toString(splat);
//                String sourceLong = Double.toString(srcLong);
//                String desLat = Double.toString(dtnLat);
//                String desLong = Double.toString(dtnLong);
//                String ad=edtadults.getText().toString();
//                String ch=edtchilds.getText().toString();
//                String min=edtminCost.getText().toString();
//                String max=edtmaxCost.getText().toString();
//                String st=edtstatus.getText().toString();
//                String iP;
//                if (swPrivate.isChecked())
//                    iP="true";
//                else iP="false";

                //POST
                final String URL = "http://35.197.153.192:3000/tour/set-stop-points";
                final Map<String, String> params = new HashMap<>();
                ArrayList<StopPoint> stopP = new ArrayList<>();
                stopP.add(new StopPoint(id, serviceId,name, address, splat, splong, arrive, leave, minCost, maxCost, serviceTypeID));
                params.put("id",tourID);

//                params.put("name", Name);
//                params.put("startDate", startDate);
//                params.put("endDate", endDate);
//                params.put("sourceLat", sourceLat);
//                params.put("sourceLong", sourceLong);
//                params.put("desLat", desLat);
//                params.put("desLong", desLong);
//                params.put("adults",ad);
//                params.put("childs",ch);
//                params.put("minCost",min);
//                params.put("maxCost",max);
//                params.put("status",st);
//                params.put("isPrivate",iP);


                JSONObject parameters = new JSONObject(params);

                try {
                    parameters = new JSONObject("{\"tourId\": "+tourID+",\"stopPoints\": [{\"id\": "+id+",\"arrivalAt\": "+startDate+",\"lat\": "+splat+",\"leaveAt\": "+endDate+",\"long\": "+splong+",\"maxCost\": "+maxCost+",\"minCost\": "+minCost+",\"name\": \""+Name+"\",\"serviceTypeId\": "+serviceTypeID+"}],\"deleteIds\": [0]}");
                }catch (JSONException err){
                    Log.d("Error", err.toString());
                }
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("abcd:", response.toString());
                        //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                        Toast.makeText(MT_SP_Update.this, "Update tour successfully", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(MT_SP_Update.this, "Bad request!!!", Toast.LENGTH_SHORT).show();
                                    break;
                                case "404":
                                    Toast.makeText(MT_SP_Update.this, "Tour is not found", Toast.LENGTH_SHORT).show();
                                    break;
                                case "403":
                                    Toast.makeText(MT_SP_Update.this, "Not permission to update tour info", Toast.LENGTH_SHORT).show();
                                    break;
                                case "500":
                                    Toast.makeText(MT_SP_Update.this, "Server error on updating tour info", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(MT_SP_Update.this, "ERROR", Toast.LENGTH_SHORT).show();
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
                Volley.newRequestQueue(MT_SP_Update.this).add(jsonRequest);
            }

        });

    }


}
