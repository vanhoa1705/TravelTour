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

public class My_Tour_Info extends AppCompatActivity {
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
    Button Send, Add;
    RatingBar rating;

    EditText edtName,edtstartDate,edtendDate,edtadults,edtchilds,edtPrivate,edtstatus,edtminCost,edtmaxCost, if_cmt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__tour__info);

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

        Send = findViewById(R.id.mif_send);
        Add = this.findViewById(R.id.mti_add);
        rating = findViewById(R.id.mif_ratingBar);


        edtName=this.findViewById(R.id.mtiName);
        edtstartDate=this.findViewById(R.id.mtiStartDate);
        edtendDate=this.findViewById(R.id.mtiEndDate);
        edtadults=this.findViewById(R.id.mtiadults);
        edtchilds=this.findViewById(R.id.mtiChilds);
        edtPrivate=this.findViewById(R.id.mtiPrivate);
        edtstatus=this.findViewById(R.id.mtistatus);
        edtminCost=this.findViewById(R.id.mtiminCost);
        edtmaxCost=this.findViewById(R.id.mtimaxCost);

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
                    String spid,spname,address,adLat,adLong,arriveAt,leaveAt, spminCost,spmaxCost,spserviceTypeId, serviceId;
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject object = jSONArray.getJSONObject(i);
                        spid=object.getString("id");
                        serviceId=object.getString("serviceId");
                        spname=object.getString("name");
                        address=object.getString("address");
                        adLat=object.getString("lat");
                        adLong=object.getString("long");
                        arriveAt=object.getString("arrivalAt");
                        leaveAt=object.getString("leaveAt");
                        spminCost=object.getString("minCost");
                        spmaxCost=object.getString("maxCost");
                        spserviceTypeId=object.getString("serviceTypeId");

                        if(arriveAt.equals("null") || leaveAt.equals("null")){
                            arriveAt = "0";
                            leaveAt = "0";
                        } else {
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
                            long SpstDate = Long.parseLong(arriveAt);
                            long SpeDate = Long.parseLong(leaveAt);
                            arriveAt = simpleDateFormat1.format(new Date(SpstDate));
                            leaveAt = simpleDateFormat2.format(new Date(SpeDate));
                        }
                        list_sp.add(new StopPoint(spid,serviceId,spname,address,adLat,adLong,arriveAt,leaveAt,spminCost,spmaxCost,spserviceTypeId));
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
                Toast.makeText(My_Tour_Info.this,"Can't get this tour !", Toast.LENGTH_SHORT).show();

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

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(My_Tour_Info.this, map2.class);
                i.putExtra("Token", Token);
                i.putExtra("tourID", tourID);
                startActivity(i);
            }
        });

        stop_point.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(My_Tour_Info.this,MT_SP_Info.class);
                intent.putExtra("Token",Token);
                intent.putExtra("tourID",tourID);
                intent.putExtra("id",list_sp.get(i).getId()+"");
                intent.putExtra("serviceId",list_sp.get(i).getServiceId());
                intent.putExtra("name",list_sp.get(i).getName()+"");
                intent.putExtra("address",list_sp.get(i).getAddress()+"");
                intent.putExtra("lat",list_sp.get(i).getLat()+"");
                intent.putExtra("long",list_sp.get(i).getLong()+"");
                intent.putExtra("arriveAt",list_sp.get(i).getArriveAt()+"");
                intent.putExtra("leaveAt",list_sp.get(i).getLeaveAt()+"");
                intent.putExtra("serviceTypeId",list_sp.get(i).getServiceTypeId()+"");
                intent.putExtra("minCost",list_sp.get(i).getMinCost()+"");
                intent.putExtra("maxCost",list_sp.get(i).getMaxCost()+"");
                startActivity(intent);
            }
        });

        Button btnEdit,btnDelete;
        btnEdit=this.findViewById(R.id.btn_mtiedit);
        btnDelete=this.findViewById(R.id.btn_mtidelete);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(My_Tour_Info.this,UpdateTour.class);
                intent.putExtra("Token",Token);
                intent.putExtra("id",tourID);
                intent.putExtra("name",name);
                intent.putExtra("status",status+"");
                intent.putExtra("minCost",minCost);
                intent.putExtra("maxCost",maxCost);
                intent.putExtra("startDate",startDate);
                intent.putExtra("endDate",endDate);
                intent.putExtra("adults",adults+"");
                intent.putExtra("childs",childs+"");
                intent.putExtra("isPrivate",isPrivate+"");
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //POST
                final String URL = "http://35.197.153.192:3000/tour/update-tour";
                final Map<String, String> params = new HashMap<>();
                params.put("id", tourID);
                params.put("status", "-1");

                JSONObject parameters = new JSONObject(params);
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                        Toast.makeText(My_Tour_Info.this, "Tour is disable", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(My_Tour_Info.this, "Fill Status with number!!!", Toast.LENGTH_SHORT).show();
                                    break;
                                case "404":
                                    Toast.makeText(My_Tour_Info.this, "Tour is not found", Toast.LENGTH_SHORT).show();
                                    break;
                                case "403":
                                    Toast.makeText(My_Tour_Info.this, "Not permission to update tour info", Toast.LENGTH_SHORT).show();
                                    break;
                                case "500":
                                    Toast.makeText(My_Tour_Info.this, "Server error on updating tour info", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(My_Tour_Info.this, "ERROR", Toast.LENGTH_SHORT).show();
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
                Volley.newRequestQueue(My_Tour_Info.this).add(jsonRequest);
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Tour_Info.this, "" + rating.getRating(), Toast.LENGTH_LONG).show();
                String URL = "http://35.197.153.192:3000/tour/add/review";

                if_cmt = findViewById(R.id.mif_cmt);

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
                            Toast.makeText(My_Tour_Info.this, temp1,Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //error giu thong tin tra ve tu server khi send that bai
                        Toast.makeText(My_Tour_Info.this,"Error", Toast.LENGTH_SHORT).show();

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
                Volley.newRequestQueue(My_Tour_Info.this).add(jsonRequest);
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
                    rating = findViewById(R.id.mif_ratingBar);
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
                Toast.makeText(My_Tour_Info.this,"Can not get point", Toast.LENGTH_SHORT).show();

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
