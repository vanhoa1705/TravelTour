package com.example.yourtour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Tour_SP_Info extends AppCompatActivity {
    Button btnSend;
    EditText Feedback;
    ListView tour_comment;
    RatingBar bar;
    int rating=0;

    ArrayList<Comment> list_cm;
    ListCommentAdapter listCommentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour__sp__info);

        NonScrollListView non_scroll_list2 = (NonScrollListView) findViewById (R.id.msilvcomment);
        list_cm=new ArrayList<>();
        listCommentAdapter=new ListCommentAdapter(this,R.layout.tour_comment,list_cm);
        tour_comment=this.findViewById(R.id.silvcomment);
        tour_comment.setAdapter(listCommentAdapter);

        EditText edtName,edtLocation,edtArrive,edtLeave,edtService,edtMin,edtMax,edtIndex;
        edtName=this.findViewById(R.id.siName);
        edtLocation=this.findViewById(R.id.siLocation);
        edtService=this.findViewById(R.id.siSTID);
        edtMin=this.findViewById(R.id.siminCost);
        edtMax=this.findViewById(R.id.simaxCost);
        tour_comment=this.findViewById(R.id.silvcomment);
        btnSend=this.findViewById(R.id.si_send);
        bar=this.findViewById(R.id.si_ratingBar);
        Feedback=this.findViewById(R.id.si_cmt);

        edtName.setFocusableInTouchMode(false);
        edtLocation.setFocusableInTouchMode(false);
        edtService.setFocusableInTouchMode(false);
        edtMin.setFocusableInTouchMode(false);
        edtMax.setFocusableInTouchMode(false);

        final String Token,tourID, id,name,splat,splong,serviceTypeID,minCost,maxCost;
        Intent intent=getIntent();
        Token=intent.getStringExtra("Token");
        tourID=intent.getStringExtra("tourID");
        id=intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        splat=intent.getStringExtra("lat");
        splong=intent.getStringExtra("long");
        serviceTypeID=intent.getStringExtra("serviceTypeId");
        minCost=intent.getStringExtra("minCost");
        maxCost=intent.getStringExtra("maxCost");

        edtName.setText(name);
        edtLocation.setText(splat + " , " + splong);
        edtService.setText(serviceTypeID);
        edtMin.setText(minCost);
        edtMax.setText(maxCost);

        String URL = "http://35.197.153.192:3000/tour/get/feedback-service?serviceId="+id+"&pageIndex=1&pageSize=1000";

        Map<String, String> params = new HashMap<>();

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //lay du lieu tu json
                try {
                    JSONArray jSONArray = response.getJSONArray("feedbackList");
                    String cmtid,cmtname,cmtfeedback, cmtpoint;
                    int sum=0;
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject object = jSONArray.getJSONObject(i);
                        cmtid=object.getString("id");
                        cmtname=object.getString("name");
                        cmtfeedback=object.getString("feedback");
                        cmtpoint=object.getString("point");
                        sum+=Integer.parseInt(cmtpoint);
                        list_cm.add(new Comment(cmtid,cmtname,cmtfeedback));
                        listCommentAdapter.notifyDataSetChanged();
                    }
                    rating=sum/(jSONArray.length());
                    bar.setRating(rating);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //error giu thong tin tra ve tu server khi send that bai
                Toast.makeText(Tour_SP_Info.this,"Can't get this Stop Point !", Toast.LENGTH_SHORT).show();

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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = "http://35.197.153.192:3000/tour/add/feedback-service";

                String point = Integer.toString((int)bar.getRating());


                Map<String, String> params = new HashMap<>();
                params.put("serviceId", id);

                params.put("feedback", Feedback.getText().toString());

                params.put("point", point);

                JSONObject parameters = new JSONObject(params);

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi
                        Log.e("Add review ",response.toString());

                        //lay du lieu tu json
                        JSONObject object = response;
                        Toast.makeText(Tour_SP_Info.this, "Send feedback successfully!!!",Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //error giu thong tin tra ve tu server khi send that bai
                        Toast.makeText(Tour_SP_Info.this,"Error", Toast.LENGTH_SHORT).show();

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
                Volley.newRequestQueue(Tour_SP_Info.this).add(jsonRequest);
            }
        });
    }

}
