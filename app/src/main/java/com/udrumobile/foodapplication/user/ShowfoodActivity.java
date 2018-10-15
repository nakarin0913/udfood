package com.udrumobile.foodapplication.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.travijuu.numberpicker.library.NumberPicker;
import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.NetConnect;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.module.Getdata;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ShowfoodActivity extends AppCompatActivity {


    FloatingActionButton btn_add;
    TextView txt_name, txt_about, txt_price;
    ImageView imageView;
    Integer id_food_all, id_food_rec;
    NumberPicker number_picker;
    EditText edit_needmore;
    Getdata getdt = new Getdata();

    String TAG_UserID = "user_id";

    String TAG_FoodID = "food_id";

    String TAG_ResID = "res_id";

    String TAG_Orb_id = "orb_id";

    String TAG_Qty = "qty";

    String TAG_Sumprice = "sumprice";

    String TAG_Status = "status";

    String TAG_Needmore = "needmore";

    private String TAG_SUCCESS = "success";

    private String AddOrder_URL = getdt.SetIP + "/LoginServer/allfunc.php?func_name=addFoodInOrder";

    private String BuyOrder_URL = getdt.SetIP + "/LoginServer/allfunc.php?func_name=CreateOrderBuy";


    private static final String TAG_MESSAGE = "message";

    LocationManager locationManager;
    LocationListener locationListener;

    int id_get = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfood);

        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_about = (TextView) findViewById(R.id.txt_about);
        txt_price = (TextView) findViewById(R.id.txt_price);
        imageView = (ImageView) findViewById(R.id.imageView);
        number_picker = (NumberPicker) findViewById(R.id.number_picker_default);
        edit_needmore = (EditText) findViewById(R.id.edit_needmore);

        CallGPS();

        Log.e("classname", this.getClass().getName());
        this.setTitle("หน้าสั่งอาหาร");

        Intent getintent = getIntent();


        id_food_all = getintent.getIntExtra("id_food_all", 999999);

        id_food_rec = getintent.getIntExtra("id_food_rec", 999999);

        String full_latlon = "";

        if (id_food_all != 999999) {
            id_get = id_food_all;
            txt_name.setText(getdt.NamefoodAll.get(id_food_all));
            txt_about.setText(getdt.AboutfoodAll.get(id_food_all));
            txt_price.setText(getdt.PricefoodAll.get(id_food_all));
            full_latlon = getdt.LatLon_Res.get(id_food_all);
            imageView.setImageBitmap(getdt.BitmapFoodAll.get(id_food_all));
        }

        else if (id_food_rec != 999999) {
            id_get = id_food_rec;
            txt_name.setText(getdt.NamefoodAll_Rec.get(id_food_rec));
            txt_about.setText(getdt.AboutfoodAll_Rec.get(id_food_rec));
            txt_price.setText(getdt.PricefoodAll_Rec.get(id_food_rec));
            full_latlon = getdt.LatLon_Res_Rec.get(id_food_rec);
            imageView.setImageBitmap(getdt.BitmapFoodAll_Rec.get(id_food_rec));
        }

        String[] parts = full_latlon.split(",");
        Double lat_des = Double.valueOf(parts[0]);
        Double lon_des = Double.valueOf(parts[1]);


        BackButton();

        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.number_picker_default);
        numberPicker.setMax(10);
        numberPicker.setMin(1);
        numberPicker.setUnit(1);
        numberPicker.setValue(1);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getdt.loca_user != null) {
                    double distance = CalculateDistance(getdt.loca_user.latitude, lat_des, getdt.loca_user.longitude, lon_des, 0, 0);

                    if (distance <= getdt.MAX_Mater) {
                        if(getdt.Orb_Select != 0){
                            Log.e("Exec", "Exec().execute()");
                            new Exec().execute();
                        }
                        else{
                            Log.e("ExecOrderBuy_ID", "ExecOrderBuy_ID");
                            new ExecOrderBuy_ID().execute();
                        }

                    } else {
                        Toast.makeText(ShowfoodActivity.this, "อยู่นอกระยะที่ระบบกำหนดคือ " + getdt.MAX_Mater + " ม.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ShowfoodActivity.this, "กำลังค้นหาตำแหน่งของคุณ...", Toast.LENGTH_SHORT).show();
                    CallGPS();
                }

            }
        });



    }

    @SuppressLint("MissingPermission")
    public void CallGPS() {
//        locationManager = (LocationManager)  getSystemService(Context.LOCATION_SERVICE);
//
//        locationListener = new MyLocationListener();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getApplicationContext(),"ERror",Toast.LENGTH_LONG).show();
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Keep track of user location.
        // Use callback/listener since requesting immediately may return null location.
        // IMPORTANT: TO GET GPS TO WORK, MAKE SURE THE LOCATION SERVICES ON YOUR PHONE ARE ON.
        // FOR ME, THIS WAS LOCATED IN SETTINGS > LOCATION.
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, new Listener());
        // Have another for GPS provider just in case.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new Listener());
        // Try to request the location immediately
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null){
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location != null){
            handleLatLng(location.getLatitude(), location.getLongitude());
        }
        Toast.makeText(getApplicationContext(),
                "Trying to obtain GPS coordinates. Make sure you have location services on.",
                Toast.LENGTH_SHORT).show();
    }

    private void BackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static double CalculateDistance(double lat1, double lat2, double lon1,
                                           double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            Log.e("LocationVal",location.getLatitude()+""+location.getLongitude());

            if(location!=null){
                getdt.loca_user = new LatLng(location.getLatitude(), location.getLongitude());

                Toast.makeText(getApplicationContext(),"พบตำแหน่งของคุณแล้ว สั่งอาหารได้", Toast.LENGTH_LONG).show();
            }

        }


        @Override
        public void onProviderDisabled(String provider) {
            Log.e("onProviderDisabled",provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e("onProviderEnabled",provider);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("onStatusChanged",provider);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getdt.loca_user=null;
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private double Sumpricev(int qty, double price) {
        Double sum = qty * price;
        return sum;
    }

    //new
    private class Listener implements LocationListener {
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            handleLatLng(latitude, longitude);
        }

        public void onProviderDisabled(String provider){}
        public void onProviderEnabled(String provider){}
        public void onStatusChanged(String provider, int status, Bundle extras){}
    }

    private void handleLatLng(double latitude, double longitude){
        Log.v("TAG", "(" + latitude + "," + longitude + ")");
    }


    class ExecOrderBuy_ID extends AsyncTask<String, String, String> {

        String message;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... args) {



            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("parameter['user_id']", getdt.IDUser));


                JSONObject jsonobject = JSONParser.makeHttpRequest(BuyOrder_URL, "POST", params);

                JSONArray jsonArray = jsonobject.getJSONArray("orderbuys").getJSONArray(0);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonfile = jsonArray.getJSONObject(i);
                    getdt.Orb_Select = jsonfile.getInt("orb_id");

                    Log.e("ShowfoodActivity", jsonfile.getString("orb_id"));
                }


                message = jsonobject.getString(TAG_MESSAGE);

                return message+"";


            } catch (JSONException e) {
                return e.toString();
            }



        }


        protected void onPostExecute(String file_url) {
            Toast.makeText(getApplicationContext(),file_url,Toast.LENGTH_LONG).show();
            if(file_url.equals("Available")){
                new Exec().execute();
            }
        }

    }


    class Exec extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            int success;

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_UserID, getdt.IDUser));
                params.add(new BasicNameValuePair(TAG_FoodID, getdt.IdfoodAll.get(id_get).toString()));
                params.add(new BasicNameValuePair(TAG_ResID, getdt.ResID_Select));
                params.add(new BasicNameValuePair(TAG_Orb_id, getdt.Orb_Select.toString()));
                params.add(new BasicNameValuePair(TAG_Qty, number_picker.getValue() + ""));
                params.add(new BasicNameValuePair(TAG_Sumprice, Sumpricev(Integer.parseInt(number_picker.getValue() + ""), Double.parseDouble(txt_price.getText().toString())) + ""));
                params.add(new BasicNameValuePair(TAG_Status, "รอดำเนินการ"));
                params.add(new BasicNameValuePair(TAG_Needmore, edit_needmore.getText().toString()));


                String resultServer = NetConnect.getHttpPost(AddOrder_URL, params);


                Log.e("Exec Result",resultServer);

                JSONObject c;
                c = new JSONObject(resultServer);

                try {
                    success = c.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("User Created!", c.toString());
                        finish();
                        return c.getString(TAG_MESSAGE);
                    } else {
                        Log.d("Register Failure!", c.getString(TAG_MESSAGE));
                        return c.getString(TAG_MESSAGE);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                return c.getString(TAG_MESSAGE);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {

            if (file_url != null) {
                Toast.makeText(ShowfoodActivity.this, file_url,
                        Toast.LENGTH_LONG).show();
                finish();
            }

        }

    }
}
