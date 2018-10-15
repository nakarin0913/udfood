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
import android.widget.ListView;
import android.widget.Toast;

import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.owner.adapter_owner.ListViewAdapterOwn;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListfoodFragment extends Fragment {


    private static JSONObject jsonobject;
    private static JSONArray jsonArrayUsers;

    private static ListView listview;
    private static ListViewAdapterOwn adapterListView;

    private static Context context;

    private static ArrayList<HashMap<String, String>> arraylistFoods;
    private static Getdata getdt=new Getdata();
    //    private static String USERS_URL = "http://www.udfood.xyz/LoginServer/listfood.php";
    private static String FOODS_URL = getdt.SetIP+"/LoginServer/listfood.php?user_id="+getdt.IDUser;

    static final String TAG_FOODS_LIST = "foods";
    static final String TAG_SUCCESS = "success";
    static final String TAG_MESSAGE = "message";
    public static final String TAG_FOODID = "food_id";
    public static final String TAG_FOODNAME = "foodname";
    public static final String TAG_IMGPATH = "imagepath";
    public static final String TAG_ABOUT = "about";
    public static final String TAG_PRICE = "price";
    private View view;


    public ListfoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new DownloadJSON().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_listfood, container,
                false);
        Log.e("classname", this.getClass().getName());


        listview = (ListView) view.findViewById(R.id.listViewfood);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),position+"",Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }


    @Override
    public void onActivityResult(int RC, int RQC, Intent I) {
//        Log.e("RESULT","RESULT");
       new DownloadJSON().execute();

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context=context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




    public static class DownloadJSON extends AsyncTask<Void, Void, Void> {
        int status;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "กำลังโหลดข้อมูล", "กรุณารอสักครู่..", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            arraylistFoods = new ArrayList<HashMap<String, String>>();

            try {

                List<NameValuePair> param = new ArrayList<NameValuePair>();

                Log.d("request!", "Starting");


                jsonobject = JSONParser.makeHttpRequest(FOODS_URL, "POST", param);

                status = jsonobject.getInt(TAG_SUCCESS);

                if (status != 0) {
                    getdt.IdfoodAll.clear();
                    jsonArrayUsers = jsonobject.getJSONArray(TAG_FOODS_LIST);

                    Log.e("FoodsVal", "doInBackground: "+jsonArrayUsers );

                    for (int i = 0; i < jsonArrayUsers.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonobject = jsonArrayUsers.getJSONObject(i);

                        getdt.IdfoodAll.add(jsonobject.getInt(TAG_FOODID));
                        map.put(TAG_FOODNAME, jsonobject.getString(TAG_FOODNAME));
                        map.put(TAG_PRICE, jsonobject.getString(TAG_PRICE));
                        map.put(TAG_ABOUT, jsonobject.getString(TAG_ABOUT));
                        map.put(TAG_IMGPATH, jsonobject.getString(TAG_IMGPATH));


                        arraylistFoods.add(map);
                    }



                } else {

                    Log.d("LodingRestaurantFailure", jsonobject.getString(TAG_MESSAGE));
                }


            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            progressDialog.dismiss();

            if (status != 0) {

                adapterListView = new ListViewAdapterOwn(context, arraylistFoods);

                listview.setAdapter(adapterListView);

            }

        }
    }


}
