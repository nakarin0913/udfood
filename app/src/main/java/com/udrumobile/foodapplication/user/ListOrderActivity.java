package com.udrumobile.foodapplication.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.module.Getdata;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ListOrderActivity extends AppCompatActivity {

    public static GridView list_order;
    public static TextView txt_sumprice;


    public static ArrayList<HashMap<String, String>> arraylistOrder;
    public static Getdata getdt=new Getdata();
    public static final String TAG_FOODNAME = "foodname";
    public static final ArrayList<String> order_id=new ArrayList();
    public static final ArrayList<String> namefood=new ArrayList();
    public static final ArrayList<String> food_status=new ArrayList();
    public static final ArrayList<String> img=new ArrayList();
    public static final ArrayList<String> qty=new ArrayList();
    public static final ArrayList<String> price=new ArrayList();

    public static JSONObject jsonobject;
    public static JSONArray jsonArrayUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);

        this.setTitle("สร้างรายการสั่งอาหาร");

        BackButton();
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
}
