package com.udrumobile.foodapplication.owner;

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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.owner.adapter_owner.OrderAdapterFoods;
import com.udrumobile.foodapplication.owner.adapter_owner.OrderAdapterFoodsAll;
import com.udrumobile.foodapplication.user.ListfoodActivity;
import com.udrumobile.foodapplication.user.adapter_user.ListViewAdapterOrders;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListOrderFoodFragmentAll extends Fragment {


    private View view;

    public static Context context;

    public static JSONObject jsonobject;
    public static JSONArray jsonArrayOrders;

    public static ListView listview;
    public static ListViewAdapterOrders adapterListView;

    public static ArrayList<HashMap<String, String>> arraylistOrders;
    public static Getdata getdt = new Getdata();
    //    private static String USERS_URL = "http://www.udfood.xyz/LoginServer/listfood.php";
    public static String FOODRES_URL = getdt.SetIP + "/LoginServer/allfunc.php?func_name=SelectOrderBuy&res_id="+getdt.IDRes;


    static final String TAG_ORDERBUY_LIST = "order_buy";
    static final String TAG_USERNAME_LIST = "user";
    static final String TAG_SUCCESS = "success";
    static final String TAG_MESSAGE = "message";
    public static final String TAG_ORDER_ID = "orb_id";
    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_STATUS = "orb_status";
    public static final String TAG_DATETIME = "datetime";
    public static final String TAG_FULLNAME = "fullname";

    public static String User_id="";




    public ListOrderFoodFragmentAll() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_order_history, container, false);
        listview = (ListView) view.findViewById(R.id.list_order);


        Log.e("classname", this.getClass().getName());

        Log.e("URLLL",FOODRES_URL);
        context = getContext();

        new DownloadJSON().execute();

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        context = getContext();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    public static class DownloadJSON extends AsyncTask<Void, Void, Void> {

        int status;

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

            arraylistOrders = new ArrayList<HashMap<String, String>>();

            arraylistOrders.clear();

            try {

                List<NameValuePair> param = new ArrayList<NameValuePair>();

                Log.d("request!", "Starting");

                jsonobject = JSONParser.makeHttpRequest(FOODRES_URL, "POST", param);

                status = jsonobject.getInt(TAG_SUCCESS);


                jsonArrayOrders = jsonobject.getJSONArray(TAG_ORDERBUY_LIST);

                Log.e("JSON FILE", jsonArrayOrders.toString() );

                for (int i = 0; i < jsonArrayOrders.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonobject = jsonArrayOrders.getJSONObject(i);


                    map.put(TAG_ORDER_ID, jsonobject.getString(TAG_ORDER_ID));
                    map.put(TAG_USER_ID, jsonobject.getString(TAG_USER_ID));
                    User_id=jsonobject.getString(TAG_USER_ID);
                    map.put(TAG_STATUS, jsonobject.getString(TAG_STATUS));
                    map.put(TAG_DATETIME, jsonobject.getString(TAG_DATETIME));
                    map.put(TAG_FULLNAME, jsonobject.getString(TAG_FULLNAME));


                    arraylistOrders.add(map);
                }


            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            pDialog.cancel();



            if (status != 0) {

                adapterListView = new ListViewAdapterOrders(context, arraylistOrders);
//
                listview.setAdapter(adapterListView);


                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, ListfoodActivity.class);
//                        getdt.Status_Btn_delete=arraylistOrders.get(position).get(TAG_ORDER_ID);
                        intent.putExtra("orb_id", arraylistOrders.get(position).get(TAG_ORDER_ID));
                        context.startActivity(intent);
                    }
                });

            }

        }
    }






}
