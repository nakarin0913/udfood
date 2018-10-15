package com.udrumobile.foodapplication.owner.dialogstatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.owner.ListOrderFoodFragment;
import com.udrumobile.foodapplication.user.ListfoodActivity;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class ScrollingActivity extends Activity {


    CardView card_update;

    PagerAdapter pagerAdapter;

    Getdata getdt = new Getdata();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        card_update = (CardView) findViewById(R.id.card_update);


        this.setTitle("");

        getdt.ID_Status = 0;

        // Setup ViewPager with indicator
        ViewPager pager = findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(4);
        pager.setAdapter(pagerAdapter);

        ScrollingPagerIndicator pagerIndicator = findViewById(R.id.pager_indicator);
        pagerIndicator.attachToPager(pager);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getdt.ID_Status = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        card_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateStOrderFood().execute();

            }
        });

    }

    private int getScreenWidth() {
        @SuppressWarnings("ConstantConditions")
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        return screenSize.x;
    }


    private class UpdateStOrderFood extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        static final String TAG_SUCCESS = "success";
        static final String TAG_MESSAGE = "message";
        JSONObject jsonobject;

        String link = getdt.SetIP + "/LoginServer/allfunc.php?func_name=UpdateStOrderFood&orb_id="+getdt.Orb_Select+"&orderId="+getdt.OrderId+"&status="+pagerAdapter.txt_val[getdt.ID_Status];

        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(ScrollingActivity.this, "กำลังอัพเดทสถานะ", "กรุณารอสักครู่..", false, false);
            Log.e("UPDATE_ORDER", "LINK: "+link);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            jsonobject = JSONParser.makeHttpRequest(link, "POST", param);

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            try {
                Toast.makeText(getApplicationContext(), jsonobject.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
//                new ListOrderFoodFragment.DownloadJSON().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pDialog.cancel();

            new ListfoodActivity.DownloadJSON().execute();

            finish();

        }

    }
}
