package com.udrumobile.foodapplication.owner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.owner.adapter_owner.OrderAdapterFoods;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListOrderFoodActivity extends Activity {

    public static GridView list_order;
    public static ArrayList<HashMap<String, String>> arraylistOrder;
    public static JSONObject jsonobject;
    public static JSONArray jsonArrayUsers;
    public static Getdata getdt=new Getdata();


    static final String TAG_ORDER_LIST = "order";
    static final String TAG_SUCCESS = "success";
    static final String TAG_MESSAGE = "message";
    public static final String TAG_ORDERID = "orderId";
    public static final String TAG_FOODID = "food_id";
    public static final String TAG_FOODNAME = "foodname";
    public static final String TAG_RESID = "res_id";
    public static final String TAG_USERID = "user_id";
    public static final String TAG_QTY = "qty";
    public static final String TAG_SUMPRICE = "sumprice";
    public static final String TAG_STATUS = "status";
    public static final String TAG_DATETIME = "datetime";
    public static final String TAG_IMAGE = "image_path";
    public static final String TAG_FULLNAME_USER = "fullname";
    public static final ArrayList<Integer> order_id=new ArrayList();
    public static final ArrayList<String> namefood=new ArrayList();
    public static final ArrayList<String> food_status=new ArrayList();
    public static final ArrayList<String> img=new ArrayList();
    public static final ArrayList<String> qty=new ArrayList();
    public static final ArrayList<String> price=new ArrayList();
    public static final ArrayList<String> date=new ArrayList();
    public static final ArrayList<String> fullname_us=new ArrayList();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list_order_history);


        new DownloadJSON().execute();
    }

    public class DownloadJSON extends AsyncTask<Void, Void, Void> {
        int status;
        String FOODRES_URL = getdt.SetIP+"/LoginServer/listorderHistory.php?res_id="+getdt.IDRes;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage("กรุณารอสักครู่...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            arraylistOrder = new ArrayList<HashMap<String, String>>();

            try {


                List<NameValuePair> param = new ArrayList<NameValuePair>();


                jsonobject = JSONParser.makeHttpRequest(FOODRES_URL, "POST", param);

                status = jsonobject.getInt(TAG_SUCCESS);


                Log.e("URL",FOODRES_URL);

                jsonArrayUsers = jsonobject.getJSONArray(TAG_ORDER_LIST);


                for (int i = 0; i < jsonArrayUsers.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonobject = jsonArrayUsers.getJSONObject(i);

                    Log.d("JSONFILES", jsonobject+"");

                    map.put(TAG_ORDERID, jsonobject.getString(TAG_ORDERID));
                    map.put(TAG_FOODNAME, jsonobject.getString(TAG_FOODNAME));
                    map.put(TAG_RESID, jsonobject.getString(TAG_RESID));
                    map.put(TAG_USERID, jsonobject.getString(TAG_USERID));
                    map.put(TAG_IMAGE,jsonobject.getString(TAG_IMAGE));
                    map.put(TAG_QTY, jsonobject.getString(TAG_QTY));
                    map.put(TAG_SUMPRICE, jsonobject.getString(TAG_SUMPRICE));
                    map.put(TAG_STATUS, jsonobject.getString(TAG_STATUS));
                    map.put(TAG_DATETIME, jsonobject.getString(TAG_DATETIME));
                    map.put(TAG_FULLNAME_USER, jsonobject.getString(TAG_FULLNAME_USER));
                    arraylistOrder.add(map);

                }



            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            namefood.clear();
            order_id.clear();
            food_status.clear();
            img.clear();
            qty.clear();
            price.clear();
            pDialog.cancel();

            for (int i = 0; i < arraylistOrder.size() ; i++) {
                order_id.add(Integer.parseInt(arraylistOrder.get(i).get(TAG_ORDERID)));
                namefood.add(arraylistOrder.get(i).get(TAG_FOODNAME));
                food_status.add(arraylistOrder.get(i).get(TAG_STATUS));
                img.add(arraylistOrder.get(i).get(TAG_IMAGE));
                qty.add(arraylistOrder.get(i).get(TAG_QTY));
                price.add(arraylistOrder.get(i).get(TAG_SUMPRICE));
                date.add(arraylistOrder.get(i).get(TAG_DATETIME));
                fullname_us.add(arraylistOrder.get(i).get(TAG_FULLNAME_USER));

            }



            OrderAdapterFoods adapter = new OrderAdapterFoods(getApplicationContext(), order_id, namefood, food_status, img, qty, price, date, fullname_us);
            list_order.setAdapter(adapter);

        }
    }
}
