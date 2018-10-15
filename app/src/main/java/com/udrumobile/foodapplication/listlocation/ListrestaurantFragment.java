package com.udrumobile.foodapplication.listlocation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.user.ShowResActivity;
import com.udrumobile.foodapplication.module.Getdata;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ListrestaurantFragment extends Fragment {




    EditText editText;
    ListView listView;
    ListViewAdapter adapter;
    ArrayAdapter<String> adapter2;
    Getdata getdt=new Getdata();
    View view;

    public ListrestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("classname", this.getClass().getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_listrestaurant, container, false);

        listView = (ListView)view.findViewById(R.id.list);
        editText = (EditText)view.findViewById(R.id.Edt);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String text = (String) adapter.getItem(position).getName();

                for(int i=0;i<getdt.NameresAll.size();i++){
                    if(getdt.NameresAll.get(i).equals(text)){
                        Intent intent=new Intent(getActivity(), ShowResActivity.class);
                        intent.putExtra("numberread",i);
                        startActivity(intent);
                        i=getdt.NameresAll.size();
                    }
                }


                Toast.makeText(getContext(), "เลือก "+text, Toast.LENGTH_SHORT).show();

            }
        });

        new ExecuteRes().execute();

        return view;
    }

    public void returnVal(String idval) {
        Intent val = new Intent();
        val.putExtra("id", idval);
//        setResult(1, val);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        new ExecuteRes().cancel(true);

    }

    private class ExecuteRes extends AsyncTask<Void, Void, Void> {
        int status;
        ArrayList<Locationmodel> nameArrayList = new ArrayList<Locationmodel>();
        String[] Name;
        private String TAG_RESID="res_id";
        private String TAG_USERID="user_id";
        private String TAG_RESNAME="resname";
        private String TAG_ABOUT="about";
        private String TAG_WORKINGTIME="workingtime";
        private String TAG_IMAGE_PATH="image_path";
        private String TAG_LATITUDE="latitude";
        private String TAG_LONGITUDE="longitude";

        JSONObject jsonobject;
        JSONArray jsonArrayRes;

        String URL=getdt.SetIP+"/LoginServer/showrestaurants.php";

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

            getdt.NameresAll.clear();
            getdt.ResIDAll.clear();
            getdt.ResAboutAll.clear();
            getdt.ResWorkingtimeAll.clear();
            getdt.ResImageAll.clear();

            try {

                List<NameValuePair> param = new ArrayList<NameValuePair>();

                jsonobject = JSONParser.makeHttpRequest(URL, "POST", param);

                status = jsonobject.getInt("success");

                if (status != 0) {

                    jsonArrayRes = jsonobject.getJSONArray("restaurants");


                    for (int i = 0; i < jsonArrayRes.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonobject = jsonArrayRes.getJSONObject(i);
                        getdt.NameresAll.add(jsonobject.getString(TAG_RESNAME));
                        getdt.ResIDAll.add(jsonobject.getString(TAG_RESID));
                        getdt.ResAboutAll.add(jsonobject.getString(TAG_ABOUT));
                        getdt.ResWorkingtimeAll.add(jsonobject.getString(TAG_WORKINGTIME));
                        getdt.ResImageAll.add(jsonobject.getString(TAG_IMAGE_PATH));

//                        LatLng points = new LatLng(jsonobject.getDouble(TAG_LONGITUDE), jsonobject.getDouble(TAG_LATITUDE));


//                if (listPoints.size() == 2) {
//                    //Create the URL to get request from first marker to second marker
//                    String url = getRequestUrlandMe(listPoints.get(0), listPoints.get(1));
//                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
//                    taskRequestDirections.execute(url);
//                }


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

            for (int i = 0; i < getdt.NameresAll.size(); i++){
                Locationmodel PN = new Locationmodel(getdt.NameresAll.get(i));
                nameArrayList.add(PN);
            }

            adapter = new ListViewAdapter(getContext(), nameArrayList);
            listView.setAdapter(adapter);
            pDialog.cancel();

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Search or Filter
                    String sc = editText.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.myFilter(sc);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(count==0){
                        nameArrayList.clear();
                        for (int i = 0; i < getdt.NameresAll.size(); i++){
                            Locationmodel PN = new Locationmodel(getdt.NameresAll.get(i));
                            nameArrayList.add(PN);
                        }

                        adapter = new ListViewAdapter(getContext(), nameArrayList);


                        listView.setAdapter(adapter);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }
}
