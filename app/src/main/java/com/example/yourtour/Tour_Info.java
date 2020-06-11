package com.example.yourtour;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
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

import static com.example.yourtour.R.id.if_ratingBar;
import static com.example.yourtour.R.id.lvstoppoint;
import static com.example.yourtour.R.id.msiArriveAt;

public class Tour_Info extends AppCompatActivity {
    String Token,tourID,name,minCost,maxCost,startDate,endDate;
    ListView stop_point, tour_comment, tour_member;
    int Rating =0;

    ArrayList<StopPoint> list_sp;
    ListStopPointAdapter listStopPointAdapter;

    ArrayList<Comment> list_cm;
    ListCommentAdapter listCommentAdapter;

    ArrayList<Member> list_mb;
    ListMemberAdapter listMemberAdapter;

    int id,status,adults,childs;
    Boolean isPrivate;
    Button Send;
    RatingBar rating;

    EditText edtName,edtstartDate,edtendDate,edtadults,edtchilds,edtPrivate,edtstatus,edtminCost,edtmaxCost, if_cmt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_info);

        NonScrollListView non_scroll_list = (NonScrollListView) findViewById (R.id.lvstoppoint);
        list_sp=new ArrayList<>();
        listStopPointAdapter=new ListStopPointAdapter(this,R.layout.stop_point,list_sp);
        stop_point=this.findViewById(lvstoppoint);
        stop_point.setAdapter(listStopPointAdapter);

        NonScrollListView non_scroll_list2 = (NonScrollListView) findViewById (R.id.lvcomment);
        list_cm=new ArrayList<>();
        listCommentAdapter=new ListCommentAdapter(this,R.layout.tour_comment,list_cm);
        tour_comment=this.findViewById(R.id.lvcomment);
        tour_comment.setAdapter(listCommentAdapter);

        NonScrollListView non_scroll_list3 = (NonScrollListView) findViewById (R.id.lvmember);
        list_mb=new ArrayList<>();
        listMemberAdapter=new ListMemberAdapter(this,R.layout.tour_member,list_mb);
        tour_member=this.findViewById(R.id.lvmember);
        tour_member.setAdapter(listMemberAdapter);

        Send = findViewById(R.id.if_send);
        rating = findViewById(R.id.if_ratingBar);


        edtName=this.findViewById(R.id.tiName);
        edtstartDate=this.findViewById(R.id.tiStartDate);
        edtendDate=this.findViewById(R.id.tiEndDate);
        edtadults=this.findViewById(R.id.tiadults);
        edtchilds=this.findViewById(R.id.tiChilds);
        edtPrivate=this.findViewById(R.id.tiPrivate);
        edtstatus=this.findViewById(R.id.tistatus);
        edtminCost=this.findViewById(R.id.timinCost);
        edtmaxCost=this.findViewById(R.id.timaxCost);

        edtName.setFocusableInTouchMode(false);
        edtstartDate.setFocusableInTouchMode(false);
        edtendDate.setFocusableInTouchMode(false);
        edtadults.setFocusableInTouchMode(false);
        edtchilds.setFocusableInTouchMode(false);
        edtPrivate.setFocusableInTouchMode(false);
        edtstatus.setFocusableInTouchMode(false);
        edtminCost.setFocusableInTouchMode(false);
        edtmaxCost.setFocusableInTouchMode(false);

        Intent intent=this.getIntent();
        Token=intent.getStringExtra("Token");
        tourID=intent.getStringExtra("id");
        GetRating();


//        edtName.setText(Token);
//        edtstartDate.setText(tourID);
//        GetInf(tourID, Token);

        String URL = "http://35.197.153.192:3000/tour/info?tourId="+tourID;

        Map<String, String> params = new HashMap<>();
        params.put("tourId",tourID);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //lay du lieu tu json
                try {
                    JSONArray jSONArray = response.getJSONArray("stopPoints");
                    String spid,spserviceId,spname,address,spstDate,speDate, spminCost,spmaxCost, adLat,adLong,serviceID;
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject object = jSONArray.getJSONObject(i);
                        spid=object.getString("id");
                        spserviceId=object.getString("serviceId");
                        spname=object.getString("name");
                        address=object.getString("address");
                        adLat=object.getString("Lat");
                        adLong=object.getString("Long");
                        spstDate=object.getString("arrivalAt");
                        speDate=object.getString("leaveAt");
                        spminCost=object.getString("minCost");
                        spmaxCost=object.getString("maxCost");
                        serviceID=object.getString("serviceTypeId");

                        if(spstDate.equals("null") || speDate.equals("null")){
                            spstDate = "0";
                            speDate = "0";
                        } else {
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
                            long SpstDate = Long.parseLong(spstDate);
                            long SpeDate = Long.parseLong(speDate);
                            spstDate = simpleDateFormat1.format(new Date(SpstDate));
                            speDate = simpleDateFormat2.format(new Date(SpeDate));
                        }
                        list_sp.add(new StopPoint(spid,serviceID,spname,address,adLat,adLong,spstDate,speDate,spminCost,spmaxCost,serviceID));
                        listStopPointAdapter.notifyDataSetChanged();
                    }

                    JSONArray jSONArray2 = response.getJSONArray("comments");
                    String cmid,cmname,cmcomment;
                    for (int i=0;i<jSONArray2.length();i++)
                    {
                        JSONObject object2 = jSONArray2.getJSONObject(i);
                        cmid=object2.getString("id");
                        cmname=object2.getString("name");
                        cmcomment=object2.getString("comment");
                        list_cm.add(new Comment(cmid,cmname,cmcomment));
                        listCommentAdapter.notifyDataSetChanged();
                    }

                    JSONArray jSONArray3 = response.getJSONArray("members");
                    String mbid,mbname,mbphone;
                    for (int i=0;i<jSONArray3.length();i++)
                    {
                        JSONObject object3 = jSONArray3.getJSONObject(i);
                        mbid=object3.getString("id");
                        mbname=object3.getString("name");
                        mbphone=object3.getString("phone");
                        list_mb.add(new Member(mbid,mbname,mbphone));
                        listMemberAdapter.notifyDataSetChanged();
                    }


                    name=response.getString("name");
                    minCost=response.getString("minCost");
                    maxCost=response.getString("maxCost");
                    startDate=response.getString("startDate");
                    endDate=response.getString("endDate");
                    adults=response.getInt("adults");
                    childs=response.getInt("childs");
                    status=response.getInt("status");
                    isPrivate=response.getBoolean("isPrivate");

                    if(startDate.equals("null")){
                        startDate = "0";
                    } else {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        long StartDate = Long.parseLong(startDate);
                        startDate = simpleDateFormat.format(new Date(StartDate));
                    }

                    if(endDate.equals("null")){
                        endDate = "0";
                    } else {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        long EndDate = Long.parseLong(endDate);
                        endDate = simpleDateFormat.format(new Date(EndDate));
                    }

                    edtName.setText(name);
                    edtstartDate.setText(startDate);
                    edtendDate.setText(endDate);
                    edtadults.setText(adults+"");
                    edtchilds.setText(childs+"");
                    edtminCost.setText(minCost);
                    edtmaxCost.setText(maxCost);
                    edtPrivate.setText(isPrivate+"");
                    if (status==-1)
                        edtstatus.setText("Canceled");
                    else if (status==0)
                        edtstatus.setText("Open");
                    else if (status==1)
                        edtstatus.setText("Started");
                    else if (status==2)
                        edtstatus.setText("Closed");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //error giu thong tin tra ve tu server khi send that bai
                Toast.makeText(Tour_Info.this,"Can't get this tour !", Toast.LENGTH_SHORT).show();

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

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Tour_Info.this, "" + rating.getRating(), Toast.LENGTH_LONG).show();
                String URL = "http://35.197.153.192:3000/tour/add/review";

                if_cmt = findViewById(R.id.if_cmt);

                String point = Integer.toString((int)rating.getRating());


                Map<String, String> params = new HashMap<>();
                params.put("tourId", tourID);

                params.put("point", point);
                String temp = if_cmt.getText().toString();

                params.put("review", temp);

                JSONObject parameters = new JSONObject(params);

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                        Log.e("Add review ",response.toString());

                        //lay du lieu tu json
                        try {
                            JSONObject object = response;

                            String temp1 = object.getString("message");
                            Toast.makeText(Tour_Info.this, temp1,Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //error giu thong tin tra ve tu server khi send that bai
                        Toast.makeText(Tour_Info.this,"Error", Toast.LENGTH_SHORT).show();

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
                Volley.newRequestQueue(Tour_Info.this).add(jsonRequest);
            }
        });
    }
    private void GetRating() {
        //Truyen parameter va body
        //Cac kieu du lieu deu truyen kieu string

        String URL = "http://35.197.153.192:3000/tour/get/review-point-stats?tourId=" + tourID;

        Map<String, String> params = new HashMap<>();
        //params.put("rowPerPage", "1000");
        //params.put("pageNum", "1");

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                //Toast.makeText(ToursList.this, "" + response.toString(),Toast.LENGTH_LONG).show();
                int sum =0, temp =0;

                //lay du lieu tu json
                try {
                    JSONArray jSONArray = response.getJSONArray("pointStats");

                    for (int i = 0; i < jSONArray.length(); i++) {

                        JSONObject object = jSONArray.getJSONObject(i);
                        temp = temp + Integer.parseInt(object.getString("total"));
                        sum = sum + Integer.parseInt(object.getString("total"))*(i+1);
                    }
                    if(temp == 0) Rating = 0;
                    else Rating = sum/temp;
                    rating = findViewById(if_ratingBar);
                    rating.setRating(Rating);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //error giu thong tin tra ve tu server khi send that bai
                Toast.makeText(Tour_Info.this,"Can not get point", Toast.LENGTH_SHORT).show();

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
