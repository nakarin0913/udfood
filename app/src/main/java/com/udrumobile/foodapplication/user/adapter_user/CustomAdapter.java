package com.udrumobile.foodapplication.user.adapter_user;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.URLtoImageview;
import com.udrumobile.foodapplication.module.Getdata;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context mContext;
    ArrayList <String> strName=new ArrayList<>();
    ArrayList <String> img=new ArrayList<>();
    ImageView imageView;


    Getdata getdt=new Getdata();
    //    private static String USERS_URL = "http://www.udfood.xyz/LoginServer/listfood.php";


    public CustomAdapter(Context context, ArrayList<String> strName,  ArrayList<String> img) {
        this.mContext= context;
        this.strName = strName;
        this.img = img;
    }

    public CustomAdapter(Context context,  ArrayList<String> strName) {
        this.mContext= context;
        this.strName = strName;
    }

    public int getCount() {
        return strName.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.e("classname", this.getClass().getName());

        if(view == null){
            view = mInflater.inflate(R.layout.listview_recomment, parent, false);
        }


        TextView textView = (TextView)view.findViewById(R.id.textView1);
        textView.setText(strName.get(position));

        imageView = (ImageView)view.findViewById(R.id.imageView1);

        if(img.size()>0){
            new URLtoImageview.SetImageview(imageView).execute(getdt.ImagefoodAll.get(position).toString());
        }


        return view;
    }


}