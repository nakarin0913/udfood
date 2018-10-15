package com.udrumobile.foodapplication.owner;

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
import android.widget.TextView;
import android.widget.Toast;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.admin.ListUserFragment;
import com.udrumobile.foodapplication.admin.adapter_admin.ListViewAdapterAdm;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.owner.adapter_owner.ListViewAdapterReccom;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReccommentFragment extends Fragment {


    private View view;
    ListView listview;

    JSONObject jsonobject;
    JSONArray jsonArrayReccom;

    ListViewAdapterReccom adapterListView;

    ArrayList<HashMap<String, String>> arraylistFood;
    Getdata getdt = new Getdata();

    private String RECCOM_URL = getdt.SetIP + "/LoginServer/allfunc.php?func_name=Listfoods&user_id="+getdt.IDUser;

    static final String TAG_USERS_LIST = "foods";
    static final String TAG_SUCCESS = "success";
    static final String TAG_MESSAGE = "message";
    public static final String TAG_FoodID = "food_id";
    public static final String TAG_FOODNAME = "foodname";
    public static final String TAG_RECOM = "recom";

    public int listPosition;
    static boolean[] tickMarkVisibileListPosition = new boolean[10000];

    public ReccommentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=  inflater.inflate(R.layout.fragment_reccomment_fragment2, container, false);

        listview = (ListView) view.findViewById(R.id.listViewreccom);

        new DownloadJSON().execute();

        return  view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
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

            arraylistFood = new ArrayList<HashMap<String, String>>();

            try {

                List<NameValuePair> param = new ArrayList<NameValuePair>();

                jsonobject = JSONParser.makeHttpRequest(RECCOM_URL, "GET", param);

                status = jsonobject.getInt(TAG_SUCCESS);

                getdt.SetRecCheck.clear();

                if (status != 0) {

                    jsonArrayReccom = jsonobject.getJSONArray(TAG_USERS_LIST);

                    Log.e("VALJSON",jsonArrayReccom.toString());


                    for (int i = 0; i < jsonArrayReccom.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonobject = jsonArrayReccom.getJSONObject(i);


                        map.put(TAG_FOODNAME, jsonobject.getString(TAG_FOODNAME));
                        map.put(TAG_RECOM, jsonobject.getString(TAG_RECOM));


                        if (jsonobject.getString(TAG_RECOM).equals("0")) {
                            getdt.SetRecCheck.add(false);
                        } else {
                            getdt.SetRecCheck.add(true);
                        }

                        getdt.SetIdFoodRec.add(Integer.parseInt(jsonobject.getString(TAG_FoodID)));

                        arraylistFood.add(map);
                    }

                } else {


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
                adapterListView = new ListViewAdapterReccom(getContext(), arraylistFood);

                listview.setAdapter(adapterListView);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                        listPosition = position - listview.getFirstVisiblePosition();

                        if (getdt.SetRecCheck.get(position).toString().equals("true")) {

                            listview.getChildAt(listPosition).findViewById(R.id.iv_check_box).setBackgroundResource(R.drawable.uncheck);
                            tickMarkVisibileListPosition[position] = Boolean.FALSE;

                            getdt.SetRecCheck.set(position,false);

                           DownloadJSON.MyTaskParams params = new DownloadJSON.MyTaskParams(getdt.SetIdFoodRec.get(position).toString(), false);
                           DownloadJSON.SetStatusRecom myTask = new DownloadJSON.SetStatusRecom();
                           myTask.execute(params);

                        }

                        else if(getdt.SetRecCheck.get(position).toString().equals("false")) {

                            listview.getChildAt(listPosition).findViewById(R.id.iv_check_box).setBackgroundResource(R.drawable.checked_rec);
                            tickMarkVisibileListPosition[position] = Boolean.TRUE;

                            getdt.SetRecCheck.set(position,true);

                            DownloadJSON.MyTaskParams params = new DownloadJSON.MyTaskParams(getdt.SetIdFoodRec.get(position).toString(), true);
                            DownloadJSON.SetStatusRecom myTask = new DownloadJSON.SetStatusRecom();
                            myTask.execute(params);

                        }

                    }
                });



            }

        }


        private class SetStatusRecom extends AsyncTask<DownloadJSON.MyTaskParams, Void, String> {


            @Override
            protected String doInBackground(DownloadJSON.MyTaskParams... myTaskParams) {
                try {

                    String link = getdt.SetIP + "/LoginServer/allfunc.php?func_name=UpdatestatusRecom&food_id=" + myTaskParams[0].id + "&status_rec=" + myTaskParams[0].status;

                    Log.e("UPDATE STATUS",link);

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
                Toast.makeText(getContext(),"แก้ไขเมนูแนะนำแล้ว",Toast.LENGTH_LONG).show();
            }
        }

        public class MyTaskParams {

            String id;
            Boolean status;

            public MyTaskParams(String id, Boolean sta) {
                this.id = id;
                this.status = sta;
            }

        }

    }


}
