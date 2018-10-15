/***
 * @author Ahmed Saleh
 * Created by ahmadssb on 2014-12-13
 * Website: http://www.ahmadssb.com
 * Email: ahmad@ahmadssb.com
 * Facebook: https://www.facebook.com/ahmadssbblog
 * Twitter: https://twitter.com/ahmadssb
 * YouTube: http://www.youtube.com/user/ahmadssbblog
 */

package com.udrumobile.foodapplication.owner.adapter_owner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.owner.ReccommentFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapterReccom extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();

	public ListViewAdapterReccom(Context context,
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

		TextView txt_Firstname;

		ImageView imageView;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		View itemView = inflater.inflate(R.layout.list_reccom_layout, parent, false);


		resultp = data.get(position);

		txt_Firstname = (TextView) itemView.findViewById(R.id.txt_firstname);
		imageView =(ImageView) itemView.findViewById(R.id.iv_check_box) ;

		txt_Firstname.setText(resultp.get(ReccommentFragment.TAG_FOODNAME));

		if (resultp.get(ReccommentFragment.TAG_RECOM).equals("0")) {
			imageView.setBackgroundResource(R.drawable.uncheck);
		} else {
			imageView.setBackgroundResource(R.drawable.checked_rec);
		}


		return itemView;
	}


}
