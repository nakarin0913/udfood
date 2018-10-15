package com.udrumobile.foodapplication.user.adapter_user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.udrumobile.foodapplication.JSONParser;
import com.udrumobile.foodapplication.NetConnect;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.SetFormatNumber;
import com.udrumobile.foodapplication.URLtoImageview;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.owner.ListOrderFoodFragment;
import com.udrumobile.foodapplication.owner.ListfoodFragment;
import com.udrumobile.foodapplication.owner.dialogstatus.ScrollingActivity;
import com.udrumobile.foodapplication.user.ListOrderFragment;
import com.udrumobile.foodapplication.user.ListfoodActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewAdapterAddfood extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();
	CardView card_status, card_delete;
	Getdata getdt = new Getdata();
	ListfoodActivity listfoodActivity=new ListfoodActivity();

	private static String TAG_SUCCESS="success";

	SetFormatNumber setFormatNumber=new SetFormatNumber();

	public ListViewAdapterAddfood(Context context,
                                  ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;

		data = arraylist;
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

		TextView txt_foodname, txt_price, txt_about, txt_number, txt_status, txt_qty;

		ImageView imageView;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.list_addfoods_layout, parent, false);

		resultp = data.get(position);


		txt_foodname = (TextView) itemView.findViewById(R.id.txt_foodname);
		txt_price = (TextView) itemView.findViewById(R.id.txt_foodprice);
		txt_about = (TextView) itemView.findViewById(R.id.txt_about);
		imageView =(ImageView) itemView.findViewById(R.id.imageViewfood);
		txt_number = (TextView) itemView.findViewById(R.id.txt_number);
		txt_status = (TextView) itemView.findViewById(R.id.txt_status);
        txt_qty = (TextView) itemView.findViewById(R.id.txt_qty);

		txt_foodname.setText(resultp.get(ListfoodActivity.TAG_FOODNAME));
		txt_price.setText(setFormatNumber.formatNumber(Double.parseDouble(resultp.get(ListfoodActivity.TAG_PRICE))*Double.parseDouble(resultp.get(ListfoodActivity.TAG_QTY))  )+" ฿");


		if(getdt.PositionUser.equals("owner")){
			txt_about.setText(resultp.get(ListfoodActivity.TAG_NEEDMORE));
		}
		else{
			txt_about.setText(resultp.get(ListfoodActivity.TAG_ABOUT));
		}




		txt_status.setText(resultp.get(ListfoodActivity.TAG_STATUS));
        txt_qty.setText("จำนวน "+resultp.get(ListfoodActivity.TAG_QTY));
		txt_number.setText((position+1)+"");


		card_status = (CardView) itemView.findViewById(R.id.card_status);

		card_delete = (CardView) itemView.findViewById(R.id.card_delete);



		card_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (resultp.get(ListfoodActivity.TAG_STATUS).equals("รอดำเนินการ")) {

//					Toast.makeText(context,"ลบรายการ "+resultp.get(ListfoodActivity.TAG_FOODID)+" "+getdt.Orb_Select,Toast.LENGTH_LONG).show();
					getdt.OrderId = listfoodActivity.orderId.get(position);
     				new DeleteOrderFood().execute();

				}
				else {
					Toast.makeText(context,"ไม่สามารถลบได้ กำลังทำรายการ",Toast.LENGTH_LONG).show();
				}

			}
		});

		if(getdt.Status_Btn_delete != true){
			card_delete.setVisibility(View.GONE);
		}


		if(getdt.PositionUser.equals("owner")){
			card_delete.setVisibility(View.GONE);
		}

//		if(status.get(position).equals("รอดำเนินการ")){
//			card_status.setCardBackgroundColor(Color.parseColor("#ff7700"));
//		}
//
//		else if(status.get(position).equals("กำลังทำ")){
//			card_status.setCardBackgroundColor(Color.parseColor("#707dc6"));
//		}
//
//		else {
//
//		}

		new URLtoImageview.SetImageview(imageView).execute(resultp.get(ListfoodActivity.TAG_IMGPATH));


		return itemView;
	}


	public class DeleteOrderFood extends AsyncTask<Void, Void, Void> {


		static final String TAG_SUCCESS = "success";
		static final String TAG_MESSAGE = "message";
		JSONObject jsonobject;


		String link = getdt.SetIP + "/LoginServer/allfunc.php?func_name=DeleteOrderFood&orb_id="+getdt.Orb_Select+"&orderId="+getdt.OrderId;

		@Override
		protected void onPreExecute() {
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
				Toast.makeText(context, jsonobject.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
				new ListfoodActivity.DownloadJSON().execute();

//                new ListOrderFoodFragment.DownloadJSON().execute();
			} catch (JSONException e) {
				e.printStackTrace();
			}


			Log.e("URL DELETE ORDER ",link);





		}

	}


}
