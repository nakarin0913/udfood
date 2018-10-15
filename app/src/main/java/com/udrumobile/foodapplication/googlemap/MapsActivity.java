package com.udrumobile.foodapplication.googlemap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.R;
import org.w3c.dom.Element;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;




public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;

    public String destination;
    public double Latme, Lonme, Latdes, Londes;
    String namelocation;
    private CheckBox show_alllocation;

    ArrayList<String> Locationlist = new ArrayList<String>();
    ArrayList<String> LatLon = new ArrayList<String>();
    ArrayList<String> UrlImage = new ArrayList<String>();
    ImageButton btnlist,btnmovetoudru;
    Getdata getdt=new Getdata();
    boolean addmark=false;

    List<LatLng> listPoint;
    Element element2;

    Getdata Getdata = new Getdata();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.e("classname", this.getClass().getName());
        listPoints = new ArrayList<>();
        listPoint = new ArrayList<LatLng>();


    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        ReadXMLFile("markerUDRU.xml");

        LatLng point = new LatLng(17.3972472, 102.7942013);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                //Reset marker when already 2
                if (listPoints.size() == 1) {
                    listPoints.clear();
                    mMap.clear();
                }
                //Save first point select
                listPoints.add(latLng);
                getdt.LatLon=listPoints.get(0).latitude+","+listPoints.get(0).longitude;

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("สถานที่ของคุณ")
                        .snippet(new DecimalFormat("##.00000").format(listPoints.get(0).latitude)+" "+new DecimalFormat("##.00000").format(listPoints.get(0).longitude))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    //Add first marker to the map
//
//                if (listPoints.size() == 2) {
//                    //Create the URL to get request from first marker to second marker
//                    String url = getRequestUrlandMe(listPoints.get(0), listPoints.get(1));
//                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
//                    taskRequestDirections.execute(url);
//                }
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                Lonme = mMap.getMyLocation().getLongitude();
                Latme = mMap.getMyLocation().getLatitude();
                return false;
            }
        });


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {

                for(int i=0;i<Locationlist.size();i++){
                    if(arg0.getTitle().equals(Locationlist.get(i).toString())){

                        getdt.LocationDetial=Locationlist.get(i);
                        getdt.URL=UrlImage.get(i).toString();
                        getdt.LocationName=Locationlist.get(i);
                        getdt.LatLon=arg0.getPosition().latitude+","+arg0.getPosition().longitude;


//                        Intent menu=new Intent(getApplicationContext(), MenuActivity.class);
//                        startActivity(menu);


//                        Intent showdetail=new Intent(getApplicationContext(), DetailLocation.class);
//                        startActivity(showdetail);
                    }
                }

            }

        });


        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);


    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data != null) {
                show_alllocation.setChecked(false);
                Lonme = mMap.getMyLocation().getLongitude();
                Latme = mMap.getMyLocation().getLatitude();

                namelocation = data.getStringExtra("name");
                destination = data.getStringExtra("latlon");



                String full_latlon = Getdata.LatLon;
                String[] parts = full_latlon.split(",");
                Latdes = Double.valueOf(parts[1]);
                Londes = Double.valueOf(parts[0]);

                LatLng Location_me = new LatLng(Latme, Lonme);
                LatLng Location_dest = new LatLng(Latdes, Londes);

                try {
//                    String url = getRequestUrlandMe(Location_me, Location_dest);

//                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
//                    taskRequestDirections.execute(url);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }






    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent main = new Intent(MapsActivity.this, LoginActivity.class);
//        startActivity(main);
        finish();
    }







}
