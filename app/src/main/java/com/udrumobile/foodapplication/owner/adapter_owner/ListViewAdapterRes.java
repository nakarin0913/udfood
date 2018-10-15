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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.owner.ListfoodFragment;

public class ListViewAdapterRes extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> resultp = new HashMap<String, String>();


	public ListViewAdapterRes(Context context,
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


		TextView tvUserID, tvUsername, tvDisplayName;
		ImageView imageView;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		View itemView = inflater.inflate(R.layout.fragment_listrestaurant, parent, false);


		resultp = data.get(position);


		tvUserID = (TextView) itemView.findViewById(R.id.txtID);
		tvUsername = (TextView) itemView.findViewById(R.id.txtUsername);
		tvDisplayName = (TextView) itemView.findViewById(R.id.txtDisplayName);
		imageView =(ImageView) itemView.findViewById(R.id.imageView1);

		tvUserID.setText(resultp.get(ListfoodFragment.TAG_FOODNAME));
		tvUsername.setText(resultp.get(ListfoodFragment.TAG_PRICE));
		tvDisplayName.setText(resultp.get(ListfoodFragment.TAG_ABOUT));

		new LoadImagefromUrl( ).execute( imageView, resultp.get(ListfoodFragment.TAG_IMGPATH) );

		return itemView;
	}

	private class LoadImagefromUrl extends AsyncTask< Object, Void, Bitmap > {
		ImageView ivPreview = null;

		@Override
		protected Bitmap doInBackground(Object... params ) {
			this.ivPreview = (ImageView) params[0];
			String url = (String) params[1];
			System.out.println(url);
			return loadBitmap( url );
		}

		@Override
		protected void onPostExecute( Bitmap result ) {
			super.onPostExecute( result );
			ivPreview.setImageBitmap( result );
		}
	}

	public Bitmap loadBitmap( String url ) {
		URL newurl = null;
		Bitmap bitmap = null;
		try {
			newurl = new URL( url );
			bitmap = BitmapFactory.decodeStream( newurl.openConnection( ).getInputStream( ) );
		} catch ( MalformedURLException e ) {
			e.printStackTrace( );
		} catch ( IOException e ) {

			e.printStackTrace( );
		}
		return bitmap;
	}

}
