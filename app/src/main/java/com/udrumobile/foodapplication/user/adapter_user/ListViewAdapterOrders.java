package com.udrumobile.foodapplication.user.adapter_user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.URLtoImageview;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.owner.ListfoodFragment;
import com.udrumobile.foodapplication.owner.UpdatefoodActivity;
import com.udrumobile.foodapplication.owner.adapter_owner.OrderAdapterFoods;
import com.udrumobile.foodapplication.user.ListbillorderFragment;
import com.udrumobile.foodapplication.user.ListfoodActivity;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ListViewAdapterOrders extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();
	public ImageView imageView;

	TextView txt_order_id, txt_date, txt_status, txt_number, txt_name;

	Getdata getdt=new Getdata();

	public static ArrayList<HashMap<String, String>> arraylistOrder;
	public static JSONObject jsonobject;
	public static JSONArray jsonArrayUsers;
	public static final String TAG_FULLNAME = "fullname";

	static final String TAG_USERNAME_LIST = "user";

	String fullname="";

	public ListViewAdapterOrders(Context context,
                                 ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;


		getdt.BitmapFoodAll_Owner.clear();
		data = arraylist;
	}

	public ListViewAdapterOrders() {

	}


	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}



	public View getView(final int position, View convertView, ViewGroup parent) {


		Log.e("classname", this.getClass().getName());


		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.listview_order_bill, parent, false);

		txt_order_id =(TextView)view.findViewById(R.id.txt_order_id);
		txt_status = (TextView)view.findViewById(R.id.txt_status);
		txt_date = (TextView)view.findViewById(R.id.txt_date);
		txt_number = (TextView)view.findViewById(R.id.txt_number);
		txt_name = (TextView)view.findViewById(R.id.txt_name);

		resultp = data.get(position);

		txt_number.setText((position+1)+"");

		txt_order_id.setText(resultp.get(ListbillorderFragment.TAG_ORDER_ID));
		txt_status.setText(resultp.get(ListbillorderFragment.TAG_STATUS));
		txt_date.setText(ConvertTime(resultp.get(ListbillorderFragment.TAG_DATETIME)));
		txt_name.setText(data.get(position).get(TAG_FULLNAME));

		return view;
	}

	private String ConvertTime(String datetime){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String $month="";
			Date d1 = df.parse(datetime);
			int Yearval=Integer.parseInt(d1.getYear()+"");

			int num_month =d1.getMonth()+1;
			switch (num_month){
				case 1 : $month="มกราคม"; break;
				case 2 : $month="กุมภาพันธ์"; break;
				case 3 : $month="มีนาคม"; break;
				case 4 : $month="เมษายน"; break;
				case 5 : $month="พฤษภาคม"; break;
				case 6 : $month="มิถุนายน"; break;
				case 7 : $month="กรกฎาคม"; break;
				case 8 : $month="สิงหาคม"; break;
				case 9 : $month="กันยายน"; break;
				case 10 : $month="ตุลาคม"; break;
				case 11 : $month="พฤศจิกายน"; break;
				case 12 : $month="ธันวาคม"; break;
			}

			return "สั่งเมื่อ "+d1.getHours()+":"+d1.getMinutes()+"/"+d1.getDate()+" "+$month+" "+(Yearval+1900);

		} catch (ParseException e) {
			return null;
		}
	}



}

