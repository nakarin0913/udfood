package com.udrumobile.foodapplication.user;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;

import com.udrumobile.foodapplication.module.Getdata;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowRestaurants extends Fragment implements OnMapReadyCallback {


    private View view;

    MapView mMapView;
    GoogleMap googleMap;
    public double Latme = 17, Lonme = 102;
    ArrayList<LatLng> listPoints;
    private static final int LOCATION_REQUEST = 500;
    Getdata getdt = new Getdata();
    ArrayList<LatLng> listlocation = new ArrayList<>();
    ArrayList<String> nameresall = new ArrayList<>();

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    public ShowRestaurants() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_show_restaurants, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        mMapView.getMapAsync(this);

        Log.e("classname", this.getClass().getName());
        // latitude and longitude

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
            }
        });

//        getImages();


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        new ExecuteRes().cancel(true);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {


//        ReadXMLFile("markerUDRU.xml");

        LatLng point = new LatLng(17.3972472, 102.7942013);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));


        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        new ExecuteRes().execute();


        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                Lonme = googleMap.getMyLocation().getLongitude();
                Latme = googleMap.getMyLocation().getLatitude();
                return false;
            }
        });


        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {

                for (int i = 0; i < getdt.NameresAll.size(); i++) {
                    if (arg0.getTitle().equals(getdt.NameresAll.get(i).toString())) {
                        Intent intent = new Intent(getActivity(), ShowResActivity.class);
                        intent.putExtra("numberread", i);

                        Toast.makeText(getContext(), getdt.NameresAll.get(i).toString(), Toast.LENGTH_SHORT);

                        startActivity(intent);
                        i = getdt.NameresAll.size();


                    }
                }


            }

        });


        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
    }


    private class ExecuteRes extends AsyncTask<Void, Void, Void> {
        int status;

        private String TAG_RESID = "res_id";
        private String TAG_USERID = "user_id";
        private String TAG_RESNAME = "resname";
        private String TAG_ABOUT = "about";
        private String TAG_WORKINGTIME = "workingtime";
        private String TAG_IMAGE_PATH = "image_path";
        private String TAG_LATITUDE = "latitude";
        private String TAG_LONGITUDE = "longitude";

        JSONObject jsonobject;
        JSONArray jsonArrayRes;

        String URL = getdt.SetIP + "/LoginServer/showrestaurants.php";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            listlocation.clear();
            nameresall.clear();
            getdt.NameresAll.clear();
            getdt.ResIDAll.clear();
            getdt.ResAboutAll.clear();
            getdt.ResWorkingtimeAll.clear();
            getdt.ResImageAll.clear();

            try {

                List<NameValuePair> param = new ArrayList<NameValuePair>();

                Log.d("request!", "Starting");


                jsonobject = JSONParser.makeHttpRequest(URL, "POST", param);


                Log.d("Loding Restaurant", jsonobject.toString());


                status = jsonobject.getInt("success");

                if (status != 0) {

                    jsonArrayRes = jsonobject.getJSONArray("restaurants");


                    for (int i = 0; i < jsonArrayRes.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonobject = jsonArrayRes.getJSONObject(i);
                        LatLng points = new LatLng(jsonobject.getDouble(TAG_LATITUDE), jsonobject.getDouble(TAG_LONGITUDE));
                        listlocation.add(points);
                        nameresall.add(jsonobject.getString(TAG_RESNAME));
                        getdt.NameresAll.add(jsonobject.getString(TAG_RESNAME));
                        getdt.ResIDAll.add(jsonobject.getString(TAG_RESID));
                        getdt.ResAboutAll.add(jsonobject.getString(TAG_ABOUT));
                        getdt.ResWorkingtimeAll.add(jsonobject.getString(TAG_WORKINGTIME));
                        getdt.ResImageAll.add(jsonobject.getString(TAG_IMAGE_PATH));

                        Log.e("values", jsonobject.getString(TAG_RESNAME));

                    }


                } else {


                }


            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {


            for (int i = 0; i < listlocation.size(); i++) {

                Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.resicon);
                googleMap.addMarker(new MarkerOptions()
                        .position(listlocation.get(i))
                        .title(nameresall.get(i))
                        .snippet("กดเพื่อดูรายละเอียด")
                        .icon(BitmapDescriptorFactory.fromBitmap(myBitmap))
                );
            }


        }
    }


}

