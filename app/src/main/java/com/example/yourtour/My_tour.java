package com.example.yourtour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class My_tour extends AppCompatActivity {

    String Token;
    ListView lvTourList;
    ArrayList<TourDetail> listTour;
    ListViewAdapter listViewAdapter;

    int mt_id, mt_status, mt_adults, mt_childs, mt_Rating;
    String mt_name, mt_minCost, mt_maxCost, mt_startDate, mt_endDate, mt_avatar, mt_hostId;
    Boolean mt_isPrivate, mt_isHost, mt_isKicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tour);
        final Intent intent = this.getIntent();
        Token = intent.getStringExtra("Token");

        listTour = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(this, R.layout.item_tour, listTour);
        lvTourList = this.findViewById(R.id.lv_mytour);
        lvTourList.setAdapter(listViewAdapter);
        GetTours();

        listViewAdapter.notifyDataSetChanged();

        lvTourList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(My_tour.this, My_Tour_Info.class);
                intent.putExtra("Token",Token);
                intent.putExtra("id",listTour.get(i).getId()+"");
                startActivity(intent);
            }
        });

    }

    private void GetTours() {
        //Truyen parameter va body
        //Cac kieu du lieu deu truyen kieu string
        final TourDetail[] ListTour = null;

        String URL = "http://35.197.153.192:3000/tour/history-user?pageIndex=1&pageSize=20";

        Map<String, String> params = new HashMap<>();
        //params.put("rowPerPage", "1000");
        //params.put("pageNum", "1");


        listViewAdapter.notifyDataSetChanged();

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                Log.e("my tour",response.toString());
                //Toast.makeText(ToursList.this, "" + response.toString(),Toast.LENGTH_LONG).show();

                //lay du lieu tu json
                try {
                    JSONArray jSONArray = response.getJSONArray("tours");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject object = jSONArray.getJSONObject(i);
                        mt_id = object.getInt("id");
                        mt_hostId = object.getString("id");
                        mt_status = object.getInt("status");
                        mt_name = object.getString("name");
                        mt_minCost = object.getString("minCost");
                        mt_maxCost = object.getString("maxCost");
                        mt_startDate = object.getString("startDate");
                        mt_endDate = object.getString("endDate");
                        mt_adults = object.getInt("adults");
                        mt_childs = object.getInt("childs");
                        mt_isPrivate = true;
                        mt_avatar = object.getString("avatar");
                        mt_isHost = object.getBoolean("isHost");
                        mt_isKicked = object.getBoolean("isKicked");

                        if(mt_startDate.equals("null") || mt_endDate.equals("null")){
                            mt_startDate = "0";
                            mt_endDate = "0";
                        } else {
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
                            long StartDate = Long.parseLong(mt_startDate);
                            long EndDate = Long.parseLong(mt_endDate);
                            mt_startDate = simpleDateFormat1.format(new Date(StartDate));
                            mt_endDate = simpleDateFormat2.format(new Date(EndDate));
                        }
                        //GetRating(id);
                        listTour.add(new TourDetail(mt_id, mt_status, mt_name, mt_minCost, mt_maxCost, mt_startDate, mt_endDate, mt_adults, mt_childs, mt_isPrivate, mt_avatar, 0));

                        listViewAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //error giu thong tin tra ve tu server khi send that bai
                Toast.makeText(My_tour.this,"Can not get any Tour !", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Gui header
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Token);
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(jsonRequest);
    }


}


