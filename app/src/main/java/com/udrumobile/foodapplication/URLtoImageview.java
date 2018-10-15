package com.udrumobile.foodapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.udrumobile.foodapplication.module.Getdata;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class URLtoImageview {

    public static class SetImageview extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;
        String name="";
        Getdata getdt=new Getdata();


        public SetImageview(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        public SetImageview(String name) {
            this.name=name;
        }

        public SetImageview(ImageView bmImage, String name) {
            this.bmImage = bmImage;
            this.name=name;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = getdt.SetIP+urls[0];
            Log.e("URL IMAGE", urldisplay );
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {

                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if(name.equals("food")){
                getdt.BitmapFoodAll.add(result);
            }
            else if(name.equals("food_rec")){
                getdt.BitmapFoodAll_Rec.add(result);
            }
            else if(name.equals("food_owner")){
                getdt.BitmapFoodAll_Owner.add(result);
                bmImage.setImageBitmap(result);
            }
            else {
                bmImage.setImageBitmap(result);
            }
        }
    }
}
