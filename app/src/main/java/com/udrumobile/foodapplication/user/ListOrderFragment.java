package com.udrumobile.foodapplication.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.user.adapter_user.OrderAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListOrderFragment extends Fragment {


    View view;
    public static GridView list_order;
    public static TextView txt_sumprice;


    public static ArrayList<HashMap<String, String>> arraylistOrder;
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
    public static double Sumprice = 0;
    public static final ArrayList<String> order_id=new ArrayList();
    public static final ArrayList<String> namefood=new ArrayList();
    public static final ArrayList<String> food_status=new ArrayList();
    public static final ArrayList<String> img=new ArrayList();
    public static final ArrayList<String> qty=new ArrayList();
    public static final ArrayList<String> price=new ArrayList();

    public static JSONObject jsonobject;
    public static JSONArray jsonArrayUsers;

    public static Context context;

    public ListOrderFragment() {
        // Required empty public constructor
    }


    public static ListOrderFragment newInstance(String param1, String param2) {
        ListOrderFragment fragment = new ListOrderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_order, container, false);
        list_order=(GridView)view.findViewById(R.id.list_order);
        txt_sumprice=(TextView)view.findViewById(R.id.txt_sumprice);


        Log.e("classname", this.getClass().getName());
        context =getContext();


        new DownloadJSON().execute();

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        context =getContext();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        new DownloadJSON().cancel(true);
    }

    public static class DownloadJSON extends AsyncTask<Void, Void, Void> {
        int status;
        String FOODRES_URL = getdt.SetIP+"/LoginServer/listorder.php?user_id="+getdt.IDUser;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
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

            Sumprice=0;
            for (int i = 0; i < arraylistOrder.size() ; i++) {
                order_id.add(arraylistOrder.get(i).get(TAG_ORDERID));
                namefood.add(arraylistOrder.get(i).get(TAG_FOODNAME));
                food_status.add(arraylistOrder.get(i).get(TAG_STATUS));
                img.add(arraylistOrder.get(i).get(TAG_IMAGE));
                qty.add(arraylistOrder.get(i).get(TAG_QTY));
                price.add(arraylistOrder.get(i).get(TAG_SUMPRICE));

                Sumprice+=Double.parseDouble(arraylistOrder.get(i).get(TAG_SUMPRICE));
            }

            pDialog.cancel();
            txt_sumprice.setText(Sumprice+"");
            OrderAdapter adapter = new OrderAdapter(context, order_id, namefood, food_status, img, qty, price);
            list_order.setAdapter(adapter);

        }
    }


}
