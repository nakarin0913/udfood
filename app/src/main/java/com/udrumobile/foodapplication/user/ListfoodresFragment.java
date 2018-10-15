package com.udrumobile.foodapplication.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.owner.adapter_owner.ListViewAdapterOwn;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListfoodresFragment extends Fragment {


    JSONObject jsonobject;
    JSONArray jsonArrayUsers;

    ListView listview;
    ListViewAdapterOwn adapterListView;

    ArrayList<HashMap<String, String>> arraylistFoods;
    Getdata getdt=new Getdata();
    //    private static String USERS_URL = "http://www.udfood.xyz/LoginServer/listfood.php";
    private String FOODRES_URL = getdt.SetIP+"/LoginServer/listfoodres.php?res_id="+getdt.IDUser;

    static final String TAG_FOODS_LIST = "foods";
    static final String TAG_SUCCESS = "success";
    static final String TAG_MESSAGE = "message";
    public static final String TAG_FOODNAME = "foodname";
    public static final String TAG_IMGPATH = "imagepath";
    public static final String TAG_ABOUT = "about";
    public static final String TAG_PRICE = "price";
    private View view;




    public ListfoodresFragment() {
        // Required empty public constructor
    }


    public static ListfoodresFragment newInstance(String param1, String param2) {
        ListfoodresFragment fragment = new ListfoodresFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("classname", this.getClass().getName());
        new DownloadJSON().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         view =inflater.inflate(R.layout.fragment_listfoodres, container, false);

        listview = (ListView) view.findViewById(R.id.listViewfood);

         return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

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


    private class DownloadJSON extends AsyncTask<Void, Void, Void> {
        int status;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("กรุณารอสักครู่...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            arraylistFoods = new ArrayList<HashMap<String, String>>();

            try {

                List<NameValuePair> param = new ArrayList<NameValuePair>();

                Log.d("request!", "Starting");

                jsonobject = JSONParser.makeHttpRequest(FOODRES_URL, "POST", param);

                status = jsonobject.getInt(TAG_SUCCESS);

                if (status != 0) {

                    jsonArrayUsers = jsonobject.getJSONArray(TAG_FOODS_LIST);

                    for (int i = 0; i < jsonArrayUsers.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonobject = jsonArrayUsers.getJSONObject(i);


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

            if (status != 0) {

                pDialog.cancel();
                adapterListView = new ListViewAdapterOwn(getContext(), arraylistFoods);
//
                listview.setAdapter(adapterListView);


                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

//                        TextView tvUserID = (TextView) (arg1.findViewById(R.id.txtID));
//                        String sUserID = tvUserID.getText().toString();
//                        TextView tvUsername = (TextView) (arg1.findViewById(R.id.txtUsername));
//                        String sUsername = tvUsername.getText().toString();
//                        TextView tvDisplayName = (TextView) (arg1.findViewById(R.id.txtDisplayName));
//                        String sDisplayName = tvDisplayName.getText().toString();
//
//                        Toast.makeText(getContext(), sUserID + " - " + sUsername + " - " + sDisplayName, Toast.LENGTH_LONG).show();

                    }
                });

            }

        }
    }

}
