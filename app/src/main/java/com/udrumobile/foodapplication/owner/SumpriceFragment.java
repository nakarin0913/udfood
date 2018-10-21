package com.udrumobile.foodapplication.owner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.LoginActivity;
import com.udrumobile.foodapplication.NetConnect;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.user.ListfoodActivity;
import com.udrumobile.foodapplication.user.adapter_user.ListViewAdapterOrders;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SumpriceFragment extends Fragment {

    View view;
    Button btn_calc;
    CalendarView calendarView;
    String check_select;
    EditText edit_St, edit_Ed;
    TextView txt_sum;

    int date_start, month_start;


    Getdata getdt=new Getdata();

    String URL_GET;


    ListView listview;
    ListViewAdapterOrders adapterListView;

    ArrayList<HashMap<String, String>> arraylistOrders;


    public SumpriceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_sumprice, container, false);
        btn_calc = (Button) view.findViewById(R.id.btn_calc);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        edit_St = (EditText)view.findViewById(R.id.edit_St);
        edit_Ed = (EditText)view.findViewById(R.id.edit_Ed);
        txt_sum = (TextView)view.findViewById(R.id.txt_sum);

        btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNullTextView(edit_St) && CheckNullTextView(edit_Ed)) {
                    String start_dt=edit_St.getText().toString();
                    String start_en=edit_Ed.getText().toString();
                    URL_GET = getdt.SetIP+"/LoginServer/allfunc.php?func_name=CalcSum&res_id="+getdt.IDRes+"&date_st="+start_dt+"&date_end="+start_en;
                }else{
                    URL_GET = getdt.SetIP+"/LoginServer/allfunc.php?func_name=CalcSum&res_id="+getdt.IDRes;
                }
                new SumPrices().execute();
            }
        });


        edit_St.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                check_select="edit_st";
            }
        });

        edit_Ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                check_select="edit_ed";
            }
        });



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date_full="";
                date_full+=year;
                if(month<10){
                    date_full+="-0"+month;
                }else{
                    date_full+="-"+month;
                }

                if(dayOfMonth<10){
                    date_full+="-0"+dayOfMonth;
                }else{
                    date_full+="-"+dayOfMonth;
                }

                if(check_select=="edit_st"){
                    date_start = dayOfMonth;
                    month_start = month;
                    edit_St.setText(date_full);
                }else if(check_select=="edit_ed"){

                    if(month<month_start){
                        Toast.makeText(getContext(),"เลือกเดือนที่เท่ากับหรือมากกว่าเดือนเริ่มต้น",Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(month_start==month){
                        if(dayOfMonth<=date_start){
                            Toast.makeText(getContext(),"เลือกวันที่มากกว่าวันเริ่มต้น",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    edit_Ed.setText(date_full);
                }else{
                    edit_St.setText("");
                    edit_Ed.setText("");
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private boolean CheckNullTextView(EditText edt) {
        String GetEditText = edt.getText().toString();

        if (TextUtils.isEmpty(GetEditText)) {
//            edt.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private class SumPrices extends AsyncTask<Void, Void, Void> {
        int status;
        int sum;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("กรุณารอสักครู่ ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String resultServer = NetConnect.getHttpGet(URL_GET);

                Log.e("URL_GET", URL_GET );

                JSONObject c;
                JSONArray sumJsonval;


                c = new JSONObject(resultServer);

                sumJsonval = c.getJSONArray("sumval");


                if(sumJsonval.getJSONObject(0).getString("sumprice_calc")!="null"){
                    sum = Integer.parseInt(sumJsonval.getJSONObject(0).getString("sumprice_calc"));
                }else{
                    sum=0;
                }

                Log.e("val",sum+"");

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            txt_sum.setText(sum+"");
            pDialog.cancel();

        }

    }

}
