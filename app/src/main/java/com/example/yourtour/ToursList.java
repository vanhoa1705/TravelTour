package com.example.yourtour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToursList extends AppCompatActivity implements TextWatcher {

    String Token;
    ListView lvTourList;
    ArrayList<TourDetail> listTour;
    ListViewAdapter listViewAdapter;
    EditText searchView;

    int id, status, adults, childs, Rating;
    String name, minCost, maxCost, startDate, endDate, avatar;
    Boolean isPrivate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours_list);
        final Intent intent = this.getIntent();
        Token = intent.getStringExtra("Token");

        listTour = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(this, R.layout.item_tour, listTour);
        lvTourList = this.findViewById(R.id.lvTourList);
        lvTourList.setAdapter(listViewAdapter);
        GetTours();
        searchView = (EditText) findViewById(R.id.search);
        searchView.addTextChangedListener(this);

        listViewAdapter.notifyDataSetChanged();

        lvTourList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ToursList.this, Tour_Info.class);
                intent.putExtra("Token",Token);
                intent.putExtra("id",listTour.get(i).getId()+"");
                startActivity(intent);
            }
        });


        this.setTitle("List Tours");
    }
    //Tao Menu Add
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_list,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Bat su kien Add chuyen qua Create
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAdd: {
                Intent newIntent = new Intent(ToursList.this,CreateTour.class);
                newIntent.putExtra("Token",Token);
                startActivity(newIntent);
                return true;
            }
            case  R.id.menuSettings: {
                Intent newIntent = new Intent(ToursList.this,Settings.class);
                startActivity(newIntent);
                return true;
            }
            case R.id.menuStopPoint: {
                Intent intent=new Intent(ToursList.this,StopPoint_System.class);
                intent.putExtra("Token",Token);
                startActivity(intent);
                return true;
            }
            case  R.id.menuUser: {
                Intent newIntent = new Intent(ToursList.this, User.class);
                newIntent.putExtra("Token",Token);
                startActivity(newIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void GetTours() {
        //Truyen parameter va body
        //Cac kieu du lieu deu truyen kieu string
        final TourDetail[] ListTour = null;

        String URL = "http://35.197.153.192:3000/tour/list?rowPerPage=3000&pageNum=1";

        Map<String, String> params = new HashMap<>();
        //params.put("rowPerPage", "1000");
        //params.put("pageNum", "1");

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                Log.e("List Tour",response.toString());
                //Toast.makeText(ToursList.this, "" + response.toString(),Toast.LENGTH_LONG).show();

                //lay du lieu tu json
                try {
                    JSONArray jSONArray = response.getJSONArray("tours");

                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject object = jSONArray.getJSONObject(i);
                        id = object.getInt("id");
                        status = object.getInt("status");
                        name = object.getString("name");
                        minCost = object.getString("minCost");
                        maxCost = object.getString("maxCost");
                        startDate = object.getString("startDate");
                        endDate = object.getString("endDate");
                        adults = object.getInt("adults");
                        childs = object.getInt("childs");
                        isPrivate = object.getBoolean("isPrivate");
                        avatar = object.getString("avatar");

                        if(startDate.equals("null") || endDate.equals("null")){
                            startDate = "0";
                            endDate = "0";
                        } else {
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
                            long StartDate = Long.parseLong(startDate);
                            long EndDate = Long.parseLong(endDate);
                            startDate = simpleDateFormat1.format(new Date(StartDate));
                            endDate = simpleDateFormat2.format(new Date(EndDate));
                        }
                        //GetRating(id);
                        listTour.add(new TourDetail(id, status, name, minCost, maxCost, startDate, endDate, adults, childs, isPrivate, avatar, Rating));

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
                Toast.makeText(ToursList.this,"Can not get any Tour !", Toast.LENGTH_SHORT).show();

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
    //search
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        listViewAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
