package com.udrumobile.foodapplication.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.SetFormatNumber;
import com.udrumobile.foodapplication.admin.ListUserFragment;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.owner.ListOrderFoodFragmentAll;
import com.udrumobile.foodapplication.owner.ReccommentFragment;
import com.udrumobile.foodapplication.owner.dialogstatus.ScrollingActivity;
import com.udrumobile.foodapplication.user.adapter_user.ListViewAdapterAddfood;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListfoodActivity extends AppCompatActivity {

    public static JSONObject jsonobject;
    public static JSONArray jsonArrayOrder;

    public static ListView listview;
    public static ListViewAdapterAddfood adapterListView;

    public static ArrayList<HashMap<String, String>> arraylistFoods;
    public static Getdata getdt = new Getdata();

    static final String TAG_FOODS_LIST = "order_food";
    static final String TAG_SUCCESS = "success";
    static final String TAG_MESSAGE = "message";
    public static final String TAG_ORDERID = "orderId";
    public static final String TAG_FOODID = "food_id";
    public static final String TAG_FOODNAME = "foodname";
    public static final String TAG_IMGPATH = "image_path";
    public static final String TAG_ABOUT = "about";
    public static final String TAG_QTY = "qty";
    public static final String TAG_PRICE = "price";
    public static final String TAG_STATUS = "status";
    public static final String TAG_NEEDMORE = "needmore";
    public static final ArrayList<Integer> Idfood = new ArrayList<>();
    public static final ArrayList<Integer> orderId = new ArrayList<>();

    public static String intent_orbId = "";

    public static final ArrayList<String> price = new ArrayList();
    public static double Sumprice = 0;
    public static TextView txt_sumprice, txt_status;

    public static Context context;

    String status_btn="";

    private CardView btn_checkout;

    public static SetFormatNumber setFormatNumber = new SetFormatNumber();

    String link="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfood);

        Log.e("classname", this.getClass().getName());

        listview = (ListView) findViewById(R.id.listViewfood);
        txt_sumprice = (TextView) findViewById(R.id.txt_sumprice);
        txt_status =(TextView) findViewById(R.id.txt_status);

        btn_checkout = (CardView) findViewById(R.id.btn_checkout);

        this.setTitle("เมนูอาหาร");
        BackButton();

        Intent getIntent = getIntent();
        intent_orbId = getIntent.getStringExtra("orb_id");

        context = getApplicationContext();


        getdt.Orb_Select = Integer.parseInt(intent_orbId);

        status_btn=getIntent.getStringExtra("status_btn");

        if (getdt.PositionUser.equals("owner")) {
            link = getdt.SetIP + "/LoginServer/allfunc.php?func_name=UpdateStatusOrder&orb_id=" + intent_orbId+"&status=success";
            txt_status.setText("สำเร็จ");

        } else {

            if(status_btn.equals("success") || status_btn.equals("doing")){
                btn_checkout.setVisibility(View.GONE);
            }

            link = getdt.SetIP + "/LoginServer/allfunc.php?func_name=UpdateStatusOrder&orb_id=" + intent_orbId;
        }

        Log.e("LINK UPDATE",link);

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SetStatusOrder().execute();

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(getdt.PositionUser.equals("owner")){

                    getdt.OrderId = orderId.get(position);

                    Intent scrolling =new Intent(ListfoodActivity.this, ScrollingActivity.class);
                    startActivity(scrolling);
                }

            }
        });

        new DownloadJSON().execute();

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


    public static class DownloadJSON extends AsyncTask<Void, Void, Void> {
        int status;
        private String FOODRES_URL = getdt.SetIP + "/LoginServer/allfunc.php?func_name=SelectOrderFoods&orb_id=" + intent_orbId;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            arraylistFoods = new ArrayList<HashMap<String, String>>();

            arraylistFoods.clear();

            try {

                Idfood.clear();
                List<NameValuePair> param = new ArrayList<NameValuePair>();

                jsonobject = JSONParser.makeHttpRequest(FOODRES_URL, "POST", param);

                status = jsonobject.getInt(TAG_SUCCESS);

                jsonArrayOrder = jsonobject.getJSONArray(TAG_FOODS_LIST);

                Log.d("JSONFILES", jsonArrayOrder + "");

                orderId.clear();

                for (int i = 0; i < jsonArrayOrder.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonobject = jsonArrayOrder.getJSONObject(i);

                    orderId.add(Integer.parseInt(jsonobject.getString(TAG_ORDERID)));
                    Idfood.add(Integer.parseInt(jsonobject.getString(TAG_FOODID)));

                    map.put(TAG_ORDERID, jsonobject.getString(TAG_ORDERID));
                    map.put(TAG_FOODID, jsonobject.getString(TAG_FOODID));
                    map.put(TAG_FOODNAME, jsonobject.getString(TAG_FOODNAME));
                    map.put(TAG_QTY, jsonobject.getString(TAG_QTY));
                    map.put(TAG_PRICE, jsonobject.getString(TAG_PRICE));
                    map.put(TAG_ABOUT, jsonobject.getString(TAG_ABOUT));
                    map.put(TAG_IMGPATH, jsonobject.getString(TAG_IMGPATH));
                    map.put(TAG_STATUS, jsonobject.getString(TAG_STATUS));
                    map.put(TAG_NEEDMORE, jsonobject.getString(TAG_NEEDMORE));

                    arraylistFoods.add(map);

                }

                Log.e("IDList", Idfood + "");


            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            if (status != 0) {

                adapterListView = new ListViewAdapterAddfood(context, arraylistFoods);
                listview.setAdapter(adapterListView);

            }

            price.clear();

            Sumprice = 0;
            for (int i = 0; i < arraylistFoods.size(); i++) {

                price.add(arraylistFoods.get(i).get(TAG_PRICE));

                Sumprice += Double.parseDouble(arraylistFoods.get(i).get(TAG_PRICE))*Double.parseDouble(arraylistFoods.get(i).get(TAG_QTY));
            }

            txt_sumprice.setText(setFormatNumber.formatNumber(Double.parseDouble(Sumprice + "")));



        }
    }


    private class SetStatusOrder extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... id) {
            try {

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                in.close();

                new DownloadJSON().execute();

                return sb.toString();

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), "ส่งรายการแล้ว", Toast.LENGTH_LONG).show();
            getdt.Orb_Select=0;

            if (getdt.PositionUser.equals("owner")) {
                new ListOrderFoodFragmentAll.DownloadJSON().execute();
            } else {
                new ListbillorderFragment.DownloadJSON().execute();
            }

            finish();
        }
    }

}