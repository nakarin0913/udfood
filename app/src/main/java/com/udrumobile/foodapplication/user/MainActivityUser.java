package com.udrumobile.foodapplication.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.udrumobile.foodapplication.listlocation.ListrestaurantFragment;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.owner.ListfoodFragment;

public class MainActivityUser extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView txt_title,txt_username;
    Getdata getdt=new Getdata();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e("classname", this.getClass().getName());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        TextView Head = (TextView) headerView.findViewById(R.id.textHead);
        TextView Title = (TextView) headerView.findViewById(R.id.textTitle);

        Head.setText("ระบบสั่งอาหาร");
        Title.setText(getdt.NameUser);

        setTitle("แผนที่ร้านอาหาร");
        ShowRestaurants fr_secound=new ShowRestaurants();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fr_secound,"Fragment 02");
        fragmentTransaction.commit();
        navigationView.getMenu().getItem(0).setChecked(true);


    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("ออกจากระบบ")
                .setMessage("คุณแน่ใจว่าจะออกจากระบบ ?")
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }

                })
                .setNegativeButton("ไม่ใช่", null)
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.mapres) {
            setTitle("แผนที่ร้านอาหาร");
            ShowRestaurants fr_secound=new ShowRestaurants();
            FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fr_secound,"Fragment 02");
            fragmentTransaction.commit();
            // Handle the camera action
        } else if (id == R.id.listres) {
            setTitle("รายการร้านอาหาร");
            ListrestaurantFragment fragment=new ListrestaurantFragment();
            FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"Fragment 01");
            fragmentTransaction.commit();

        } else if (id == R.id.food_order) {
            setTitle("รายการใบสั่งอาหาร");
            ListbillorderFragment fragorder=new ListbillorderFragment();
            FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragorder,"Fragment 01");
            fragmentTransaction.commit();

        }
//        else if (id == R.id.history_order) {
//            setTitle("ประวัติการสั่งอาหาร");
//            ListOrderHistoryFragment fragorder=new ListOrderHistoryFragment();
//            FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.frame,fragorder,"Fragment 01");
//            fragmentTransaction.commit();
//        }

        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
