package com.udrumobile.foodapplication.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.URLtoImageview;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.user.adapter_user.CustomAdapter;
import com.udrumobile.foodapplication.user.adapter_user.CustomAdapterRec;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowResActivity extends AppCompatActivity {

    Intent getintent;
    Integer Readnum;
    TextView txt_resname,txt_about;
    ImageView imageView;
    Getdata getdata=new Getdata();
    GridView gridView_rec,gridView_all;

    JSONObject jsonobject;
    JSONObject jsonobject_rec;

    JSONArray jsonArrayUsers;
    JSONArray jsonArrayRec;

    ArrayList<HashMap<String, String>> arraylistFoods;
    ArrayList<HashMap<String, String>> arraylistFoods_rec;
    Getdata getdt=new Getdata();

    static final String TAG_FOODS_LIST = "foods";
    static final String TAG_SUCCESS = "success";
    static final String TAG_MESSAGE = "message";
    public static final String TAG_FOODID = "food_id";
    public static final String TAG_FOODNAME = "foodname";
    public static final String TAG_IMGPATH = "imagepath";
    public static final String TAG_ABOUT = "about";
    public static final String TAG_PRICE = "price";
    public static final String TAG_LAT = "latitude";
    public static final String TAG_LON = "longitude";


    public static ArrayList<Bitmap> bitmapsfoods=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_res);
        txt_resname=(TextView)findViewById(R.id.txt_resname);
        txt_about=(TextView)findViewById(R.id.txt_about);
        imageView=(ImageView)findViewById(R.id.imageRes);
        gridView_rec = (GridView)findViewById(R.id.listview_recommend);
        gridView_all = (GridView)findViewById(R.id.listview_all);


        Log.e("classname", this.getClass().getName());


        bitmapsfoods.clear();
        getintent=getIntent();
        Readnum=getintent.getIntExtra("numberread",0);

        this.setTitle("ข้อมูลร้าน"+getdata.NameresAll.get(Readnum));

        txt_resname.setText("ร้าน "+getdata.NameresAll.get(Readnum));

        txt_about.setText("เกี่ยวกับ "+getdata.ResAboutAll.get(Readnum));

        getdata.ResID_Select = getdata.ResIDAll.get(Readnum);


        Log.e("IMAGE_RES", getdata.ResImageAll.get(Readnum) );


        new URLtoImageview.SetImageview(imageView).execute(getdata.ResImageAll.get(Readnum));

        BackButton();

        gridView_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getApplicationContext(), ShowfoodActivity.class);

                intent.putExtra("id_food_all",position);
                startActivity(intent);
            }
        });


        gridView_rec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getApplicationContext(), ShowfoodActivity.class);

                intent.putExtra("id_food_rec",position);
                startActivity(intent);
            }
        });

        getdt.BitmapFoodAll.clear();
        getdt.BitmapFoodAll_Rec.clear();

        new DownloadJSON().execute();

    }

    private void BackButton(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }



    private class DownloadJSON extends AsyncTask<Void, Void, Void> {


        ArrayList<String> list=new ArrayList();


        int status;
        String FOODRES_URL = getdt.SetIP+"/LoginServer/allfunc.php?func_name=ListFoodRes&res_id="+getdt.ResID_Select;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            arraylistFoods = new ArrayList<HashMap<String, String>>();

            try {


                List<NameValuePair> param = new ArrayList<NameValuePair>();

                jsonobject = JSONParser.makeHttpRequest(FOODRES_URL, "POST", param);

                status = jsonobject.getInt(TAG_SUCCESS);


                Log.e("URL",FOODRES_URL);

                jsonArrayUsers = jsonobject.getJSONArray(TAG_FOODS_LIST);

                getdata.IdfoodAll.clear();
                getdata.NamefoodAll.clear();
                getdata.ImagefoodAll.clear();
                getdata.PricefoodAll.clear();
                getdata.AboutfoodAll.clear();
                getdata.LatLon_Res.clear();

                for (int i = 0; i < jsonArrayUsers.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonobject = jsonArrayUsers.getJSONObject(i);

                    Log.d("JSONFILES", jsonobject+"");

                    getdata.IdfoodAll.add(Integer.parseInt(jsonobject.getString(TAG_FOODID)));
                    getdata.NamefoodAll.add(jsonobject.getString(TAG_FOODNAME));
                    getdata.ImagefoodAll.add(jsonobject.getString(TAG_IMGPATH));
                    getdata.PricefoodAll.add(jsonobject.getString(TAG_PRICE));
                    getdata.AboutfoodAll.add(jsonobject.getString(TAG_ABOUT));
                    getdata.LatLon_Res.add(jsonobject.getString(TAG_LAT)+","+jsonobject.getString(TAG_LON));

                    map.put(TAG_FOODNAME, jsonobject.getString(TAG_FOODNAME));
                    map.put(TAG_PRICE, jsonobject.getString(TAG_PRICE));
                    map.put(TAG_ABOUT, jsonobject.getString(TAG_ABOUT));
                    map.put(TAG_IMGPATH, jsonobject.getString(TAG_IMGPATH));

                    arraylistFoods.add(map);

                }



            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            list.clear();
            for (int i = 0; i < getdata.NamefoodAll.size() ; i++) {
                list.add(getdata.NamefoodAll.get(i));
                getdata.ImagefoodAll.get(i);
            }

            for (int i = 0; i < getdt.ImagefoodAll.size(); i++) {
                new URLtoImageview.SetImageview("food").execute(getdt.ImagefoodAll.get(i).toString());

            }

            CustomAdapter adapter = new CustomAdapter(getApplicationContext(), list, getdt.ImagefoodAll);


            gridView_all.setAdapter(adapter);

            new DownloadJSON_REC().execute();


        }
    }


    private class DownloadJSON_REC extends AsyncTask<Void, Void, Void> {

        ArrayList<String> list=new ArrayList();

        int status;

        String FOODRES_REC_URL = getdt.SetIP+"/LoginServer/allfunc.php?func_name=ListFoodResRec&res_id="+getdt.ResID_Select;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            arraylistFoods_rec = new ArrayList<HashMap<String, String>>();

            try {


                List<NameValuePair> param = new ArrayList<NameValuePair>();

                jsonobject_rec = JSONParser.makeHttpRequest(FOODRES_REC_URL, "POST", param);

                status = jsonobject_rec.getInt(TAG_SUCCESS);


                getdata.IdfoodAll_Rec.clear();
                getdata.NamefoodAll_Rec.clear();
                getdata.ImagefoodAll_Rec.clear();
                getdata.AboutfoodAll_Rec.clear();
                getdata.PricefoodAll_Rec.clear();
                getdata.LatLon_Res_Rec.clear();

                jsonArrayRec = jsonobject_rec.getJSONArray(TAG_FOODS_LIST);


                for (int i = 0; i < jsonArrayRec.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonobject = jsonArrayRec.getJSONObject(i);

                    getdata.IdfoodAll_Rec.add(Integer.parseInt(jsonobject.getString(TAG_FOODID)));
                    getdata.NamefoodAll_Rec.add(jsonobject.getString(TAG_FOODNAME));
                    getdata.ImagefoodAll_Rec.add(jsonobject.getString(TAG_IMGPATH));
                    getdata.PricefoodAll_Rec.add(jsonobject.getString(TAG_PRICE));
                    getdata.AboutfoodAll_Rec.add(jsonobject.getString(TAG_ABOUT));
                    getdata.LatLon_Res_Rec.add(jsonobject.getString(TAG_LAT)+","+jsonobject.getString(TAG_LON));

                    map.put(TAG_FOODNAME, jsonobject.getString(TAG_FOODNAME));
                    map.put(TAG_PRICE, jsonobject.getString(TAG_PRICE));
                    map.put(TAG_ABOUT, jsonobject.getString(TAG_ABOUT));
                    map.put(TAG_IMGPATH, jsonobject.getString(TAG_IMGPATH));

                    arraylistFoods_rec.add(map);

                }



            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            list.clear();
            for (int i = 0; i < getdata.NamefoodAll_Rec.size() ; i++) {
                list.add(getdata.NamefoodAll_Rec.get(i));
                getdata.ImagefoodAll_Rec.get(i);
            }

            for (int i = 0; i < getdt.ImagefoodAll_Rec.size(); i++) {

                Log.e("IMAGE_RECOM",  getdt.ImagefoodAll_Rec.get(i).toString()+"\n");
                new URLtoImageview.SetImageview("food_rec").execute(getdt.ImagefoodAll_Rec.get(i).toString());

            }


            CustomAdapterRec adapter = new CustomAdapterRec(getApplicationContext(), list, getdt.ImagefoodAll_Rec);

            gridView_rec.setAdapter(adapter);

        }
    }


}
