package com.udrumobile.foodapplication.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.owner.adapter_owner.ListViewAdapterOwn;
import com.udrumobile.foodapplication.user.adapter_user.ListViewAdapterOrders;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListbillorderFragment extends Fragment {


    private View view;

    public static JSONObject jsonobject;
    public static JSONArray jsonArrayOrders;

    public static ListView listview;
    public static ListViewAdapterOrders adapterListView;

    public static ArrayList<HashMap<String, String>> arraylistOrders;
    public static Getdata getdt=new Getdata();
    //    private static String USERS_URL = "http://www.udfood.xyz/LoginServer/listfood.php";
    public static String FOODRES_URL = getdt.SetIP+"/LoginServer/allfunc.php?func_name=SelectOrderBuy&user_id="+getdt.IDUser;

    static final String TAG_ORDERBUY_LIST = "order_buy";
    static final String TAG_SUCCESS = "success";
    static final String TAG_MESSAGE = "message";
    public static final String TAG_ORDER_ID = "orb_id";
    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_STATUS = "orb_status";
    public static final String TAG_DATETIME = "datetime";

    public static Context context;

    public ListbillorderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =getContext();
        new DownloadJSON().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("classname", this.getClass().getName());
        view= inflater.inflate(R.layout.fragment_listbillorder, container, false);
        listview = (ListView) view.findViewById(R.id.listVieworderbill);

        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        context =getContext();
    }



    public static class DownloadJSON extends AsyncTask<Void, Void, Void> {

        int status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            arraylistOrders = new ArrayList<HashMap<String, String>>();

            try {

                List<NameValuePair> param = new ArrayList<NameValuePair>();

                Log.d("URLLLLL !", FOODRES_URL);

                jsonobject = JSONParser.makeHttpRequest(FOODRES_URL, "POST", param);

                status = jsonobject.getInt(TAG_SUCCESS);

                if (status != 0) {

                    jsonArrayOrders = jsonobject.getJSONArray(TAG_ORDERBUY_LIST);

                    for (int i = 0; i < jsonArrayOrders.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonobject = jsonArrayOrders.getJSONObject(i);


                        map.put(TAG_ORDER_ID, jsonobject.getString(TAG_ORDER_ID));
                        map.put(TAG_USER_ID, jsonobject.getString(TAG_USER_ID));
                        map.put(TAG_STATUS, jsonobject.getString(TAG_STATUS));
                        map.put(TAG_DATETIME, jsonobject.getString(TAG_DATETIME));

                        arraylistOrders.add(map);
                    }



                } else {

                    Log.d("ListbillorderFragment", jsonobject.getString(TAG_MESSAGE));
                }


            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            if (status != 0) {

                adapterListView = new ListViewAdapterOrders(context, arraylistOrders);
//
                listview.setAdapter(adapterListView);


                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent =new Intent(context, ListfoodActivity.class);
                        intent.putExtra("orb_id", arraylistOrders.get(position).get(TAG_ORDER_ID));
                        intent.putExtra("status_btn",arraylistOrders.get(position).get(TAG_STATUS));

                        Log.e("Status listview",arraylistOrders.get(position).get(TAG_STATUS).toString());

                        if(arraylistOrders.get(position).get(TAG_STATUS).toString().equals("success" ) || arraylistOrders.get(position).get(TAG_STATUS).toString().equals("doing")){
                            getdt.Status_Btn_delete=false;
                        }else {
                            getdt.Status_Btn_delete=true;
                        }

                        context.startActivity(intent);
                    }
                });

            }

        }
    }
}
