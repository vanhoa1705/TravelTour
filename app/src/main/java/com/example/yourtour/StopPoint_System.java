package com.example.yourtour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.yourtour.R.id.lvstoppoint;

public class StopPoint_System extends AppCompatActivity implements TextWatcher {
    ListView stop_point;
    ArrayList<StopPoint> list_stoppoint;
    ListStopPointAdapter listStopPointAdapter;
    String Token,tourID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_point__system);

        Intent intent=getIntent();
        Token=intent.getStringExtra("Token");
        tourID=intent.getStringExtra("tourID");

        list_stoppoint=new ArrayList<>();
        listStopPointAdapter=new ListStopPointAdapter(this,R.layout.stop_point,list_stoppoint);
        stop_point=this.findViewById(R.id.lvSuggestSP);
        stop_point.setAdapter(listStopPointAdapter);

        listStopPointAdapter.notifyDataSetChanged();

        EditText Search=findViewById(R.id.sgSPsearch);
        Search.addTextChangedListener(StopPoint_System.this);

        listStopPointAdapter.notifyDataSetChanged();

        final String URL ="http://35.197.153.192:3000/tour/suggested-destination-list";

        Map<String, String> params = new HashMap<>();

        JSONObject parameters = new JSONObject(params);

        try {
            parameters = new JSONObject("{\"hasOneCoordinate\": false,\"coordList\": [{\"coordinateSet\": [{\"lat\": 23.457796,\"long\": 101.802655},{\"lat\": 8.553419,\"long\": 109.097577\n}]\n}\n]}");
        }catch (JSONException err){
            Log.d("Error", err.toString());
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("suggest:", response.toString());
                //response giu thong tin tra ve tu server khi thanh cong, them toString() de chuyen ve chuoi

                try {
                    JSONArray jSONArray = response.getJSONArray("stopPoints");
                    String spid,spname,address,adLat,adLong,arriveAt,leaveAt, spminCost,spmaxCost,spserviceTypeId, contact;
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject object = jSONArray.getJSONObject(i);
                        spid=object.getString("id");
                        spname=object.getString("name");
                        address=object.getString("address");
                        adLat=object.getString("lat");
                        adLong=object.getString("long");
                        //arriveAt=object.getString("arrivalAt");
                        //leaveAt=object.getString("leaveAt");
                        contact=object.getString("contact");
                        spminCost=object.getString("minCost");
                        spmaxCost=object.getString("maxCost");
                        spserviceTypeId=object.getString("serviceTypeId");

                        list_stoppoint.add(new StopPoint(spid,"0",spname,address,adLat,adLong,"0","0",spminCost,spmaxCost,spserviceTypeId));
                        listStopPointAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                            Toast.makeText(StopPoint_System.this, "Fill Status with number!!!", Toast.LENGTH_SHORT).show();
                            break;
                        case "404":
                            Toast.makeText(StopPoint_System.this, "Tour is not found", Toast.LENGTH_SHORT).show();
                            break;
                        case "403":
                            Toast.makeText(StopPoint_System.this, "Not permission to update tour info", Toast.LENGTH_SHORT).show();
                            break;
                        case "500":
                            Toast.makeText(StopPoint_System.this, "Server error on updating tour info", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(StopPoint_System.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //get Authorization
                headers.put("Authorization", Token);
                return headers;
            }
        };
        Volley.newRequestQueue(StopPoint_System.this).add(jsonRequest);

        stop_point.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(StopPoint_System.this,Tour_SP_Info.class);
                intent.putExtra("Token",Token);
                intent.putExtra("tourID",tourID);
                intent.putExtra("id",list_stoppoint.get(i).getId()+"");
                intent.putExtra("name",list_stoppoint.get(i).getName()+"");
                intent.putExtra("address",list_stoppoint.get(i).getAddress()+"");
                intent.putExtra("lat",list_stoppoint.get(i).getLat()+"");
                intent.putExtra("long",list_stoppoint.get(i).getLong()+"");
                intent.putExtra("arriveAt",list_stoppoint.get(i).getArriveAt()+"");
                intent.putExtra("leaveAt",list_stoppoint.get(i).getLeaveAt()+"");
                intent.putExtra("serviceTypeId",list_stoppoint.get(i).getServiceTypeId()+"");
                intent.putExtra("minCost",list_stoppoint.get(i).getMinCost()+"");
                intent.putExtra("maxCost",list_stoppoint.get(i).getMaxCost()+"");
                startActivity(intent);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        listStopPointAdapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
