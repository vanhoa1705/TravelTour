package com.example.yourtour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class map2 extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap2;
    Button btnCreate, btnlocationpin;
    EditText txtSearchMap;
    double mlat, mlong;
    public int RESULT_PIN;
    public float zoomLevel = 16.0f;
    public Marker start;
    Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;

    public String Token, tourID;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        getLocationPermission();

        txtSearchMap = findViewById(R.id.input_SearchMap2);
        btnlocationpin = findViewById(R.id.btnlocationpin2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(map2.this);
        Intent intent=getIntent();
        Token = intent.getStringExtra("Token");
        tourID = intent.getStringExtra("tourID");

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
                        contact=object.getString("contact");
                        spminCost=object.getString("minCost");
                        spmaxCost=object.getString("maxCost");
                        spserviceTypeId=object.getString("serviceTypeId");

                        StopPoint temp = new StopPoint(tourID,spid, spname, "", mlat+"", mlong+"", "0","0", spminCost, spmaxCost,spserviceTypeId);

                        mMap2.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(adLat), Double.parseDouble(adLong)))
                                .title(spname));
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
                            Toast.makeText(map2.this, "Fill Status with number!!!", Toast.LENGTH_SHORT).show();
                            break;
                        case "404":
                            Toast.makeText(map2.this, "Tour is not found", Toast.LENGTH_SHORT).show();
                            break;
                        case "403":
                            Toast.makeText(map2.this, "Not permission to update tour info", Toast.LENGTH_SHORT).show();
                            break;
                        case "500":
                            Toast.makeText(map2.this, "Server error on updating tour info", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(map2.this, "ERROR", Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(map2.this).add(jsonRequest);



        btnCreate=(Button)findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(map2.this,MT_SP_Create.class);
                intent.putExtra("Token", Token);
                intent.putExtra("tourID", tourID);
                intent.putExtra("Lat",mlat+"");
                intent.putExtra("Long",mlong+"");
                //setResult(RESULT_PIN,intent);
                startActivity(intent);
            }
        });

        btnlocationpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlat = currentLocation.getLatitude();
                mlong = currentLocation.getLongitude();
                moveCamera(new LatLng(mlat, mlong),zoomLevel, currentLocation.getProvider());
            }
        });
        innit();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent=new Intent(map2.this,CreateTour.class);
            intent.putExtra("Lat",0);
            intent.putExtra("Long",0);
            setResult(RESULT_PIN,intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    private void getDeviceLocation(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            currentLocation = (Location) task.getResult();
                            mlat = currentLocation.getLatitude();
                            mlong =  currentLocation.getLongitude();

                            moveCamera(new LatLng(mlat, mlong),zoomLevel, currentLocation.getProvider());

                        }else{
                            //Log.d(TAG, "onComplete: current location is null");
                            //Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            //Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap2 = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        mlat=10.763092;
        mlong=106.682333;
        LatLng KHTN = new LatLng(10.763092, 106.682333);
        MarkerOptions temp = new MarkerOptions().position(KHTN);
        start = mMap2.addMarker(temp);
        mMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(KHTN, zoomLevel));
        mMap2.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mlat=latLng.latitude;
                mlong=latLng.longitude;
                LatLng temp = new LatLng(mlat,mlong);
                start.setPosition(temp);
                mMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(temp, zoomLevel));
            }
        });

        mMap2.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                return false;
            }
        });

        if(mLocationPermissionsGranted){
            getDeviceLocation();
            mMap2.setMyLocationEnabled(true);
            mMap2.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void innit(){
        txtSearchMap.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    geoLocate();
                }

                return false;
            }
        });
    }

    private void geoLocate(){
        String searchString = txtSearchMap.getText().toString();
        Geocoder geocoder = new Geocoder(map2.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if( list.size() > 0){
            Address address = list.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), zoomLevel,
                    address.getAddressLine(0));
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        start.setPosition(latLng);
        mMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        1234);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    1234);
        }
    }
}

