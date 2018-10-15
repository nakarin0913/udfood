package com.udrumobile.foodapplication.user.adapter_user;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.URLtoImageview;
import com.udrumobile.foodapplication.module.Getdata;

import java.util.ArrayList;

public class CustomAdapterRec extends BaseAdapter {
    Context mContext;
    ArrayList <String> strName=new ArrayList<>();
    ArrayList <String> img=new ArrayList<>();
    ImageView imageView;


    Getdata getdt=new Getdata();
    //    private static String USERS_URL = "http://www.udfood.xyz/LoginServer/listfood.php";


    public CustomAdapterRec(Context context, ArrayList<String> strName, ArrayList<String> img) {
        this.mContext= context;
        this.strName = strName;
        this.img = img;
    }

    public CustomAdapterRec(Context context, ArrayList<String> strName) {
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

        if(view == null){
            view = mInflater.inflate(R.layout.listview_recomment, parent, false);
        }


        TextView textView = (TextView)view.findViewById(R.id.textView1);
        textView.setText(strName.get(position));

        imageView = (ImageView)view.findViewById(R.id.imageView1);

        if(img.size()>0){
            new URLtoImageview.SetImageview(imageView).execute(getdt.ImagefoodAll_Rec.get(position).toString());
        }


        return view;
    }


}