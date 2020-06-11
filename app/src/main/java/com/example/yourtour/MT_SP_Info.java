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

public class MT_SP_Info extends AppCompatActivity {
    Button btnEdit, btnDelete,btnSend;
    ListView tour_comment;
    RatingBar bar;
    EditText Feedback;
    int rating=0;

    ArrayList<Comment> list_cm;
    ListCommentAdapter listCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mt__sp__info);

        NonScrollListView non_scroll_list2 = (NonScrollListView) findViewById (R.id.msilvcomment);
        list_cm=new ArrayList<>();
        listCommentAdapter=new ListCommentAdapter(this,R.layout.tour_comment,list_cm);
        tour_comment=this.findViewById(R.id.msilvcomment);
        tour_comment.setAdapter(listCommentAdapter);

        EditText edtName,edtLocation,edtArrive,edtLeave,edtService,edtMin,edtMax,edtIndex;
        edtName=this.findViewById(R.id.msiName);
        edtLocation=this.findViewById(R.id.msiLocation);
        edtArrive=this.findViewById(R.id.msiArriveAt);
        edtLeave=this.findViewById(R.id.msiLeaveAt);
        edtService=this.findViewById(R.id.msiSTID);
        edtMin=this.findViewById(R.id.msiminCost);
        edtMax=this.findViewById(R.id.msimaxCost);
        bar=this.findViewById(R.id.msi_ratingBar);
        btnSend=this.findViewById(R.id.msi_send);
        Feedback=this.findViewById(R.id.msi_cmt);

        edtName.setFocusableInTouchMode(false);
        edtLocation.setFocusableInTouchMode(false);
        edtArrive.setFocusableInTouchMode(false);
        edtLeave.setFocusableInTouchMode(false);
        edtService.setFocusableInTouchMode(false);
        edtMin.setFocusableInTouchMode(false);
        edtMax.setFocusableInTouchMode(false);

        final String Token,tourID, id,serviceId,name,address,splat,splong,arrive,leave,serviceTypeID,minCost,maxCost;
        final Intent intent=getIntent();
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

        edtName.setText(name);
        edtLocation.setText(splat + " , " + splong);
        edtArrive.setText(arrive);
        edtLeave.setText(leave);
        edtService.setText(serviceTypeID);
        edtMin.setText(minCost);
        edtMax.setText(maxCost);

        String URL = "http://35.197.153.192:3000/tour/get/feedback-service?serviceId="+serviceId+"&pageIndex=1&pageSize=1000";

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
                    if (jSONArray.length()!=0)
                        rating=sum/(jSONArray.length());
                    else rating=0;
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
                Toast.makeText(MT_SP_Info.this,"Can't get this Stop Point !", Toast.LENGTH_SHORT).show();

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

        btnEdit = findViewById(R.id.btn_mtiedit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MT_SP_Info.this, MT_SP_Update.class);
                intent1.putExtra("Token", Token);
                intent1.putExtra("tourID", tourID);
                intent1.putExtra("id", id);
                intent.putExtra("serviceId",serviceId);
                intent1.putExtra("name",name);
                intent1.putExtra("address", address);
                intent1.putExtra("lat", splat);
                intent1.putExtra("long", splong);
                intent1.putExtra("arriveAt", arrive);
                intent1.putExtra("leaveAt", leave);
                intent1.putExtra("serviceTypeId", serviceTypeID);
                intent1.putExtra("minCost", minCost);
                intent1.putExtra("maxCost", maxCost);
                startActivity(intent1);
            }
        });
        btnDelete = findViewById(R.id.btn_mtidelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = "http://35.197.153.192:3000/tour/remove-stop-point?stopPointId="+id;

                Map<String, String> params = new HashMap<>();
                //params.put("stopPointId", id);

                JSONObject parameters = new JSONObject(params);

                final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(MT_SP_Info.this, "Delete Stop Point successfully!",Toast.LENGTH_LONG).show();
                            }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //error giu thong tin tra ve tu server khi send that bai
                        Toast.makeText(MT_SP_Info.this,"Can not delete this Stop Point!", Toast.LENGTH_SHORT).show();
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
                Volley.newRequestQueue(MT_SP_Info.this).add(jsonRequest);
                finish();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = "http://35.197.153.192:3000/tour/add/feedback-service";

                String point = Integer.toString((int)bar.getRating());


                Map<String, String> params = new HashMap<>();
                params.put("serviceId", serviceId);

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
                        Toast.makeText(MT_SP_Info.this, "Send feedback successfully!!!",Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //error giu thong tin tra ve tu server khi send that bai
                        Toast.makeText(MT_SP_Info.this,"Error", Toast.LENGTH_SHORT).show();

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
                Volley.newRequestQueue(MT_SP_Info.this).add(jsonRequest);
            }
        });
    }
}
