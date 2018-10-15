package com.udrumobile.foodapplication.owner;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.udrumobile.foodapplication.URLtoImageview;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;

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

import static android.app.Activity.RESULT_OK;

public class RestaurantsFragment extends Fragment implements OnMapReadyCallback {



    Bitmap bitmap, decoded;

    Button SelectImageGallery, UploadImageServer;

    ImageView imageV;

    EditText editResName,editResAbout,editResWorkingtime;

    ProgressDialog progressDialog;

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

    Boolean statushaveStore=false;

    String ServerUploadPath = getdt.SetIP+"/LoginServer/restaurants.php";
    String ServerUpdatePath = getdt.SetIP+"/LoginServer/restaurantsupdate.php";
//    String ServerUploadPath = "http://192.168.69.235/LoginServer/restaurants.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Log.e("classname", this.getClass().getName());
        View view = inflater.inflate(R.layout.fragment_restaurants, container,
                false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        SelectImageGallery = (Button) view.findViewById(R.id.buttonSelect);
        imageV = (ImageView) view.findViewById(R.id.imageView);

        editResName=(EditText)view.findViewById(R.id.editResName);

        editResAbout=(EditText)view.findViewById(R.id.editResAbout);

        editResWorkingtime=(EditText)view.findViewById(R.id.editResWorkingtime);

        SelectImageGallery = (Button) view.findViewById(R.id.buttonSelect);

        UploadImageServer = (Button) view.findViewById(R.id.button_update);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
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

        UploadImageServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(statushaveStore==true){
                    if(CheckNullTextView(editResName)==true && CheckNullTextView(editResAbout)==true && CheckNullTextView(editResWorkingtime)==true){
                        UpdateToServerFunction();
                    }
                }
                else {

                    if(CheckNullTextView(editResName)==true && CheckNullTextView(editResAbout)==true && CheckNullTextView(editResWorkingtime)==true){
                        UploadToServerFunction();
                    }

                }


            }
        });


        listPoints = new ArrayList<>();
        new  CheckStore().execute();
        return view;
    }

    private boolean CheckNullTextView(EditText edt) {
        try {
            String GetEditText = edt.getText().toString();

            if (TextUtils.isEmpty(GetEditText)) {
                Toast.makeText(getContext(), "กรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
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


    public RestaurantsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        new  CheckStore().execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int RC, int RQC, Intent I) {


//        new  CheckStore().execute("");
        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                Log.e("IMG",uri.toString());

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                setToImageView(getResizedBitmap(bitmap, 512));

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMapme=googleMap;

//        ReadXMLFile("markerUDRU.xml");

        LatLng point = new LatLng(17.3972472, 102.7942013);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
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

//				for(int i=0;i<Locationlist.size();i++){
//					if(arg0.getTitle().equals(Locationlist.get(i).toString())){
//
//						getdt.setLocationDetial(Locationlist.get(i));
//						getdt.setURL(UrlImage.get(i).toString());
//						getdt.setLocationName(Locationlist.get(i));
//						getdt.setLatLon(arg0.getPosition().latitude+","+arg0.getPosition().longitude);
//
//
////                        Intent menu=new Intent(getApplicationContext(), MenuActivity.class);
////                        startActivity(menu);
//
//
////                        Intent showdetail=new Intent(getApplicationContext(), DetailLocation.class);
////                        startActivity(showdetail);
//					}
//				}

            }

        });


        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);


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


    public void UploadToServerFunction(){

        Bitmap bitmap = ((BitmapDrawable) imageV.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        final String ConvertImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(getContext(),"กำลังอัพโหลดร้าน","กรุณารอสักครู่..",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();
                // Printing uploading success message coming from server on android app.

                statushaveStore=true;
                UploadImageServer.setText("อัพเดท");

                // Setting image as transparent after done uploading.


                new  CheckStore().execute();



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

                HashMapParams.put(UserID, getdt.IDUser);

                HashMapParams.put(Resname, editResName.getText().toString());

                HashMapParams.put(About, editResAbout.getText().toString());

                HashMapParams.put(Workingtime, editResWorkingtime.getText().toString());

                HashMapParams.put(Latlocation, Latdes.toString());

                HashMapParams.put(Lonlocation, Londes.toString());

                HashMapParams.put(ImageName, "img");

                HashMapParams.put(ImagePath, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;

            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }


    public void UpdateToServerFunction(){

        Bitmap bitmap = ((BitmapDrawable) imageV.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        final String ConvertImageup = Base64.encodeToString(imageInByte, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(getContext(),"กำลังอัพเดทร้าน","กรุณารอสักครู่..",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                Log.e("Excute",string1);

                progressDialog.dismiss();
                // Printing uploading success message coming from server on android app.

                statushaveStore=true;
                UploadImageServer.setText("อัพเดท");
                Toast.makeText(getContext(),string1,Toast.LENGTH_LONG).show();

                new CheckStore().execute();


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


    public static class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {


                stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }



    private class CheckStore extends AsyncTask {

        JSONObject json;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), "กำลังโหลดข้อมูล", "กรุณารอสักครู่..", false, false);
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
                params.add(new BasicNameValuePair("id", getdt.IDUser));
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
                    UploadImageServer.setText("อัพเดท");
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
                    statushaveStore=true;

                } else {
                    Log.e("HAVVVV","dont");
                    UploadImageServer.setText("บันทัก");
                    statushaveStore=false;

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
