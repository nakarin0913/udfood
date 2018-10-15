package com.udrumobile.foodapplication.owner.adapter_owner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udrumobile.foodapplication.URLtoImageview;
import com.udrumobile.foodapplication.listlocation.ListViewAdapter;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.owner.ListfoodFragment;
import com.udrumobile.foodapplication.owner.UpdatefoodActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapterOwn extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();
	public ImageView imageView;

	Getdata getdt=new Getdata();

	public ListViewAdapterOwn(Context context,
							  ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;


		getdt.BitmapFoodAll_Owner.clear();
		data = arraylist;
	}

	public ListViewAdapterOwn() {

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
		final TextView txt_name, txt_price, txt_about;
		CardView card_edit;


		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		View itemView = inflater.inflate(R.layout.list_foods_layout, parent, false);


		resultp = data.get(position);

		txt_name = (TextView) itemView.findViewById(R.id.txt_foodname);
		txt_price = (TextView) itemView.findViewById(R.id.txt_foodprice);
		txt_about = (TextView) itemView.findViewById(R.id.txt_about);
		imageView =(ImageView) itemView.findViewById(R.id.imageViewfood) ;
		card_edit =(CardView) itemView.findViewById(R.id.card_edit);

		new Thread(new Runnable() {
			public void run() {

				new URLtoImageview.SetImageview(imageView,"food_owner").execute(resultp.get(ListfoodFragment.TAG_IMGPATH));

			}
		}).start();

		txt_name.setText(resultp.get(ListfoodFragment.TAG_FOODNAME));
		txt_price.setText(resultp.get(ListfoodFragment.TAG_PRICE));
		txt_about.setText(resultp.get(ListfoodFragment.TAG_ABOUT));

		card_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context,UpdatefoodActivity.class);


				intent.putExtra("food_id", position);
				intent.putExtra("food_name", txt_name.getText());
				intent.putExtra("food_about", txt_about.getText());
				intent.putExtra("food_price", txt_price.getText());
				context.startActivity(intent);

			}
		});

		return itemView;
	}





}
