package com.udrumobile.foodapplication.admin;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.URLtoImageview;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.owner.RestaurantsFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class UpdateResActivity extends AppCompatActivity implements OnMapReadyCallback {


    Bitmap bitmap, decoded;

    Button SelectImageGallery, Btn_Update;

    ImageView imageV;

    EditText editResName,editResAbout,editResWorkingtime;



    private static final int LOCATION_REQUEST = 500;


    public double Latme=17, Lonme=102;

    ArrayList<LatLng> listPoints;

    Getdata getdt = new Getdata();



    int bitmap_size = 60; // range 1 - 100

    String UserID = "user_id";

    String ResID = "res_id";

    String Resname = "resname";

    String About = "about";

    String Workingtime = "workingtime";

    String Latlocation = "latitude";

    String Lonlocation = "longitude";

    String ImageName = "image_name";

    String ImagePath = "image_path";

    public Double Latdes, Londes;

    MapView mMapView;
    GoogleMap googleMapme;


    Bitmap bitmapset;

    String ServerUpdatePath = getdt.SetIP+"/LoginServer/restaurantsupdate.php";
//    String ServerUploadPath = "http://192.168.69.235/LoginServer/restaurants.php";

    Integer position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_res);

        this.setTitle("อัพเดทข้อมูลร้าน");

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        SelectImageGallery = (Button) findViewById(R.id.buttonSelect);
        imageV = (ImageView) findViewById(R.id.imageView);

        editResName=(EditText)findViewById(R.id.editResName);

        editResAbout=(EditText)findViewById(R.id.editResAbout);

        editResWorkingtime=(EditText)findViewById(R.id.editResWorkingtime);

        SelectImageGallery = (Button) findViewById(R.id.buttonSelect);

        Btn_Update = (Button) findViewById(R.id.button_update);

        Intent intent=getIntent();
        position=intent.getIntExtra("id", 0);

        try {
            MapsInitializer.initialize(getApplicationContext().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }



        mMapView.getMapAsync(this);
        // latitude and longitude


        SelectImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });

        Btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if(CheckNullTextView(editResName)==true && CheckNullTextView(editResAbout)==true && CheckNullTextView(editResWorkingtime)==true){
                        UpdateToServerFunction();
                    }


            }
        });


        listPoints = new ArrayList<>();


        BackButton();
        new CheckStore().execute();
    }


    private void BackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean CheckNullTextView(EditText edt) {
        try {
            String GetEditText = edt.getText().toString();

            if (TextUtils.isEmpty(GetEditText)) {
                Toast.makeText(getApplicationContext(), "กรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                edt.requestFocus();
                return false;
            } else {
                return true;
            }
        }
        catch (Exception e){
            return false;
        }

    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {
        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                Log.e("IMG",uri.toString());

                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                setToImageView(getResizedBitmap(bitmap, 512));

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageV.setImageBitmap(decoded);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }



    public void UpdateToServerFunction(){

        Bitmap bitmap = ((BitmapDrawable) imageV.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        final String ConvertImageup = Base64.encodeToString(imageInByte, Base64.DEFAULT);


        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(UpdateResActivity.this,"กำลังอัพเดทร้าน","กรุณารอสักครู่..",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                Log.e("Excute",string1);

                progressDialog.dismiss();
                // Printing uploading success message coming from server on android app.

                Toast.makeText(getApplicationContext(),string1,Toast.LENGTH_LONG).show();



            }

            @Override
            protected String doInBackground(Void... params) {

                Getdata getdt=new Getdata();


                String full_latlon = getdt.LatLon;
                String[] parts = full_latlon.split(",");
                Latdes = Double.valueOf(parts[0]);
                Londes = Double.valueOf(parts[1]);

                RestaurantsFragment.ImageProcessClass imageProcessClass = new RestaurantsFragment.ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ResID, getdt.IDRes);

                HashMapParams.put(Resname, editResName.getText().toString());

                HashMapParams.put(About, editResAbout.getText().toString());

                HashMapParams.put(Workingtime, editResWorkingtime.getText().toString());

                HashMapParams.put(Latlocation, Latdes.toString());

                HashMapParams.put(Lonlocation, Londes.toString());

                HashMapParams.put(ImageName, "img");

                HashMapParams.put(ImagePath, ConvertImageup);



                String FinalData = imageProcessClass.ImageHttpRequest(ServerUpdatePath, HashMapParams);

                return FinalData;
            }
        }

        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMapme=googleMap;

//        ReadXMLFile("markerUDRU.xml");

        LatLng point = new LatLng(17.3972472, 102.7942013);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        googleMap.setMyLocationEnabled(true);


        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {


                if (listPoints.size() == 1) {
                    listPoints.clear();
                    googleMap.clear();
                }

                listPoints.add(latLng);

                getdt.LatLon=listPoints.get(0).latitude + "," + listPoints.get(0).longitude;


                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("สถานที่ของคุณ")
                        .snippet(new DecimalFormat("##.00000").format(listPoints.get(0).latitude) + " " + new DecimalFormat("##.00000").format(listPoints.get(0).longitude))
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



            }

        });


        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
    }

    private class CheckStore extends AsyncTask {

        JSONObject json;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(UpdateResActivity.this, "กำลังโหลดข้อมูล", "กรุณารอสักครู่..", false, false);
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                String link = getdt.SetIP + "/LoginServer/restaurants.php";


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", getdt.SetIdOwnerAll.get(position).toString()));
                json = JSONParser.makeHttpRequest(link, "GET", params);

                return "have";
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(Object o) {

            progressDialog.dismiss();
            try {
                if (json.getString("have").toString().equals("yes")) {
                    Log.e("HAVVVV","val: "+json.toString());
                    editResName.setText(json.getString("resname"));
                    editResAbout.setText(json.getString("about").toString());
                    editResWorkingtime.setText(json.getString("workingtime").toString());
                    LatLng latLngme=new LatLng(Double.parseDouble(json.getString("latitude").toString()) ,Double.parseDouble(json.getString("longitude").toString()));
                    listPoints.clear();
                    listPoints.add(latLngme);
                    googleMapme.clear();
                    googleMapme.addMarker(new MarkerOptions()
                            .position(latLngme)
                            .title("สถานที่ของคุณ")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    getdt.IDRes=json.getString("res_id").toString();

                    new URLtoImageview.SetImageview(imageV).execute(json.getString("image_path").toString());

                } else {

                    bitmapset= BitmapFactory.decodeResource(getResources(),
                            R.drawable.noimage);

                    setToImageView(getResizedBitmap(bitmapset, 512));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
