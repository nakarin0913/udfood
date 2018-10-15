package com.udrumobile.foodapplication.user.adapter_user;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.udrumobile.foodapplication.NetConnect;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.SetFormatNumber;
import com.udrumobile.foodapplication.URLtoImageview;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.user.ListOrderFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends BaseAdapter {
    Context mContext;

    ArrayList<String> order_id = new ArrayList<>();
    ArrayList<String> strName = new ArrayList<>();
    ArrayList<String> status = new ArrayList<>();
    ArrayList<String> img = new ArrayList<>();
    ArrayList<String> qty = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();
    ImageView imageView;
    CardView card_delete;
    private int position;
    public static JSONObject jsonobject;
    static final String TAG_SUCCESS = "success";
    SetFormatNumber setFormatNumber=new SetFormatNumber();

    Getdata getdt = new Getdata();
    //    private static String USERS_URL = "http://www.udfood.xyz/LoginServer/listfood.php";


//    public OrderAdapter(Context context, ArrayList<String> strName, ArrayList<String> img) {
//        this.mContext= context;
//        this.strName = strName;
//        this.img = img;
//    }

//    public OrderAdapter(Context context, ArrayList<String> food_id, ArrayList<String> namefood, ArrayList<String> food_status, ArrayList<String> img, ArrayList<String> name, ArrayList<String> status) {
//        this.mContext= context;
//        this.strName = name;
//        this.status = status;
//    }

    public OrderAdapter(Context context, ArrayList<String> order_id, ArrayList<String> name, ArrayList<String> status, ArrayList<String> img, ArrayList<String> qty, ArrayList<String> price) {
        this.mContext = context;
        this.order_id = order_id;
        this.strName = name;
        this.status = status;
        this.img = img;
        this.qty = qty;
        this.price = price;
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

    public View getView(final int position, View view, ViewGroup parent) {

        this.position = position;
        LayoutInflater mInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.e("classname", this.getClass().getName());

        if (view == null) {
            view = mInflater.inflate(R.layout.listview_order, parent, false);
        }


        TextView txt_number = (TextView) view.findViewById(R.id.txt_number);
        TextView txt_status = (TextView) view.findViewById(R.id.txt_status);
        TextView txt_foodname = (TextView) view.findViewById(R.id.txt_foodname);
        TextView txt_qty = (TextView) view.findViewById(R.id.txt_qty);
        TextView txt_price = (TextView) view.findViewById(R.id.txt_price);

        card_delete = (CardView) view.findViewById(R.id.card_delete);

        card_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("ORDERID", "OrderID=" + order_id.get(position));

                if (status.get(position).equals("รอดำเนินการ")) {
                    new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("ลบรายการ")
                            .setMessage("คุณแน่ใจว่าจะลบ" + strName.get(position))
                            .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new DownloadJSON(Integer.parseInt(order_id.get(position).toString())).execute();
                                }

                            })
                            .setNegativeButton("ไม่ใช่", null)
                            .show();
                }
                else {
                    new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("อยู่ในกระบวนการ")
                            .setMessage("ไม่สามารถลบ" + strName.get(position)+"ได้")
                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }

                            }).show();
                }


            }
        });

        txt_number.setText((position + 1) + "");
        txt_qty.setText(qty.get(position));
        txt_price.setText(setFormatNumber.formatNumber(Double.parseDouble(price.get(position))));


        if (status.get(position).equals("เสร็จแล้ว")) {
            txt_status.setTextColor(Color.parseColor("#00B95D"));
            card_delete.setVisibility(View.GONE);
        }
        else if (status.get(position).equals("กำลังทำ")) {
            txt_status.setTextColor(Color.parseColor("#009EFF"));
            card_delete.setVisibility(View.GONE);
        }

        else {
            txt_status.setTextColor(Color.parseColor("#ff7700"));
        }
        txt_status.setText(status.get(position));
        txt_foodname.setText(strName.get(position));

        imageView = (ImageView) view.findViewById(R.id.imageView1);

        if (img.size() > 0) {
            new URLtoImageview.SetImageview(imageView).execute(img.get(position).toString());
        }


        return view;
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {
        int status;
        String URL = getdt.SetIP + "/LoginServer/deleteOrder.php";
        Integer delete_id = 0;


        public DownloadJSON(int id) {
            this.delete_id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("orderId", delete_id + ""));


                String resultServer = NetConnect.getHttpPost(URL, params);

                JSONObject c;

                c = new JSONObject(resultServer);

                try {
                    status = c.getInt(TAG_SUCCESS);
                    if (status == 1) {
                        Log.d("User Created!", c.toString());
                    } else {

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void args) {

            if (status != 0) {
                Toast.makeText(mContext, "ลบรายการเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                new ListOrderFragment.DownloadJSON().execute();
            }
        }

    }

}
        
