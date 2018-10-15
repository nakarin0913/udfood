/***
 * @author Ahmed Saleh
 * Created by ahmadssb on 2014-12-13
 * Website: http://www.ahmadssb.com
 * Email: ahmad@ahmadssb.com
 * Facebook: https://www.facebook.com/ahmadssbblog
 * Twitter: https://twitter.com/ahmadssb
 * YouTube: http://www.youtube.com/user/ahmadssbblog
 */

package com.udrumobile.foodapplication.admin.adapter_admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udrumobile.foodapplication.admin.ListUserFragment;
import com.udrumobile.foodapplication.admin.UpdateResActivity;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapterAdm extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();
	Getdata getdt = new Getdata();

	public ListViewAdapterAdm(Context context,
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


		Log.e("classname", this.getClass().getName());
		TextView txt_Firstname, txt_Lastname, txt_Email;
		CardView card_edit;
		ImageView imageView;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		View itemView = inflater.inflate(R.layout.list_user_layout, parent, false);


		resultp = data.get(position);

		txt_Firstname = (TextView) itemView.findViewById(R.id.txt_firstname);
		txt_Lastname = (TextView) itemView.findViewById(R.id.txt_lastname);
		txt_Email = (TextView) itemView.findViewById(R.id.txt_email);
		imageView =(ImageView) itemView.findViewById(R.id.iv_check_box) ;
		card_edit =(CardView) itemView.findViewById(R.id.card_edit);

		txt_Firstname.setText(resultp.get(ListUserFragment.TAG_FIRSTNAME));
		txt_Lastname.setText(resultp.get(ListUserFragment.TAG_LASTNAME));
		txt_Email.setText(resultp.get(ListUserFragment.TAG_EMAIL));

		if (resultp.get(ListUserFragment.TAG_STATUS).equals("0")) {
			imageView.setBackgroundResource(R.drawable.uncheck);
		} else {
			imageView.setBackgroundResource(R.drawable.checked);
		}

		card_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UpdateResActivity.class);
				intent.putExtra("id", position);
				context.startActivity(intent);
			}
		});

		return itemView;
	}


}
