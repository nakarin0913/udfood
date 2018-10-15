package com.udrumobile.foodapplication.admin;

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
import android.widget.TextView;
import android.widget.Toast;

import com.udrumobile.foodapplication.admin.adapter_admin.ListViewAdapterAdm;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;

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


public class ListUserFragment extends Fragment {


    JSONObject jsonobject;
    JSONArray jsonArrayUsers;

    ListView listview;
    ListViewAdapterAdm adapterListView;

    ArrayList<HashMap<String, String>> arraylistUser;
    Getdata getdt = new Getdata();

    private String USERS_URL = getdt.SetIP + "/LoginServer/users.php";

    static final String TAG_USERS_LIST = "users";
    static final String TAG_SUCCESS = "success";
    static final String TAG_MESSAGE = "message";
    public static final String TAG_IDOwner = "id";
    public static final String TAG_FIRSTNAME = "firstname";
    public static final String TAG_LASTNAME = "lastname";
    public static final String TAG_STATUS = "status";
    public static final String TAG_EMAIL = "email";
    private View view;


    public int listPosition;
    static boolean[] tickMarkVisibileListPosition = new boolean[20];

    public ListUserFragment() {
        // Required empty public constructor
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


        view = inflater.inflate(R.layout.fragment_manage_user, container,
                false);


        listview = (ListView) view.findViewById(R.id.listViewuser);


        return view;
    }


    @Override
    public void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

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

            arraylistUser = new ArrayList<HashMap<String, String>>();

            try {

                List<NameValuePair> param = new ArrayList<NameValuePair>();

                Log.d("request!", "Starting");


                jsonobject = JSONParser.makeHttpRequest(USERS_URL, "POST", param);


                status = jsonobject.getInt(TAG_SUCCESS);

                getdt.SetUserCheck.clear();
                getdt.SetIdOwnerAll.clear();

                if (status != 0) {

                    jsonArrayUsers = jsonobject.getJSONArray(TAG_USERS_LIST);


                    for (int i = 0; i < jsonArrayUsers.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonobject = jsonArrayUsers.getJSONObject(i);


                        map.put(TAG_FIRSTNAME, jsonobject.getString(TAG_FIRSTNAME));
                        map.put(TAG_LASTNAME, jsonobject.getString(TAG_LASTNAME));
                        map.put(TAG_EMAIL, jsonobject.getString(TAG_EMAIL));
                        map.put(TAG_STATUS, jsonobject.getString(TAG_STATUS));


                        if (jsonobject.getString(TAG_STATUS).equals("0")) {
                            getdt.SetUserCheck.add(false);
                        } else {
                            getdt.SetUserCheck.add(true);
                        }

                        getdt.SetIdOwnerAll.add(Integer.parseInt(jsonobject.getString(TAG_IDOwner)));

                        arraylistUser.add(map);
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
                adapterListView = new ListViewAdapterAdm(getContext(), arraylistUser);

                listview.setAdapter(adapterListView);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                        TextView f_name = (TextView) (arg1.findViewById(R.id.txt_firstname));
                        String sUserID = f_name.getText().toString();
                        TextView l_name = (TextView) (arg1.findViewById(R.id.txt_lastname));
                        String sUsername = l_name.getText().toString();
                        TextView email = (TextView) (arg1.findViewById(R.id.txt_email));
                        String sDisplayName = email.getText().toString();

                        listPosition = position - listview.getFirstVisiblePosition();

                        if (getdt.SetUserCheck.get(position).toString().equals("true")) {

                            Log.e("Click", "onItemClickTRUE: "+ getdt.SetUserCheck.get(position));
                            listview.getChildAt(listPosition).findViewById(R.id.iv_check_box).setBackgroundResource(R.drawable.uncheck);
                            tickMarkVisibileListPosition[position] = Boolean.FALSE;

                            getdt.SetUserCheck.set(position,false);

                            MyTaskParams params = new MyTaskParams(getdt.SetIdOwnerAll.get(position).toString(), false);
                            SetStatusOwner myTask = new SetStatusOwner();
                            myTask.execute(params);

                        }

                        else if(getdt.SetUserCheck.get(position).toString().equals("false")) {

                            Log.e("Click", "onItemClickFalse: "+ getdt.SetUserCheck.get(position));
                            listview.getChildAt(listPosition).findViewById(R.id.iv_check_box).setBackgroundResource(R.drawable.checked);
                            tickMarkVisibileListPosition[position] = Boolean.TRUE;



                            getdt.SetUserCheck.set(position,true);

                            MyTaskParams params = new MyTaskParams(getdt.SetIdOwnerAll.get(position).toString(), true);
                            SetStatusOwner myTask = new SetStatusOwner();
                            myTask.execute(params);


                        }


                    }
                });

                Toast.makeText(getContext(),"สภานะอัพเดทแล้ว",Toast.LENGTH_LONG).show();

            }

        }


        private class SetStatusOwner extends AsyncTask<MyTaskParams, Void, String> {


            @Override
            protected String doInBackground(MyTaskParams... myTaskParams) {
                try {

                    String link = getdt.SetIP + "/LoginServer/updatestatusUs.php?user_id=" + myTaskParams[0].id + "&status=" + myTaskParams[0].status;

                    Log.e("URL", link );

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
