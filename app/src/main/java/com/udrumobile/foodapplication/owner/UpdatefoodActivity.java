package com.udrumobile.foodapplication.owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.travijuu.numberpicker.library.NumberPicker;
import com.udrumobile.foodapplication.NetConnect;
import com.udrumobile.foodapplication.R;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.user.ListOrderFragment;
import com.udrumobile.foodapplication.user.ShowResActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdatefoodActivity extends AppCompatActivity {


    Getdata getdt = new Getdata();

    Intent getIntent;

    int food_id;
    String food_name, food_about, food_price;
    EditText edit_name, edit_price, edit_about;
    ImageView imageView;
    Button btn_openimg,btn_update, btn_delete;
    Bitmap bitmap, decoded;
    int bitmap_size = 60;

    Bitmap bitmapset;

    String ServerUpdatePath = getdt.SetIP+"/LoginServer/foodupdate.php";


    public static final String TAG_FOODID = "food_id";
    public static final String TAG_FOODNAME = "foodname";
    public static final String TAG_IMGPATH = "image_path";
    public static final String TAG_ABOUT = "about";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_PRICE = "price";
    public static final String ImageName = "image_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods_update);


        edit_name = (EditText) findViewById(R.id.editFoodName);
        edit_price = (EditText) findViewById(R.id.editFoodPrice);
        edit_about = (EditText) findViewById(R.id.editFoodAbout);
        imageView =(ImageView) findViewById(R.id.imageView) ;
        btn_openimg= (Button) findViewById(R.id.buttonOptimg);
        btn_update = (Button) findViewById(R.id.button_update);
        btn_delete = (Button) findViewById(R.id.button_delete);

        btn_openimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(CheckNullTextView(edit_name)==true && CheckNullTextView(edit_about)==true && CheckNullTextView(edit_price)==true){
                        UpdateToServerFunction();
                    }

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteOrder().execute();
            }
        });


        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmapset = drawable.getBitmap();

        setToImageView(getResizedBitmap(bitmapset, 512));

        Log.e("classname", this.getClass().getName());

        getIntent=getIntent();

        food_id = getIntent.getIntExtra("food_id",0);
        food_name = getIntent.getStringExtra("food_name");
        food_about = getIntent.getStringExtra("food_about");
        food_price = getIntent.getStringExtra("food_price");



        edit_name.setText(food_name);
        edit_price.setText(food_price);
        edit_about.setText(food_about);
        imageView.setImageBitmap(getdt.BitmapFoodAll_Owner.get(food_id));

        this.setTitle("หน้าแก้ไขอาหาร");

        BackButton();

    }

    private void BackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                setToImageView(getResizedBitmap(bitmap, 512));
//				imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private boolean CheckNullTextView(EditText edt) {
        try {
            String GetEditText = edt.getText().toString();

            if (TextUtils.isEmpty(GetEditText)) {
                Toast.makeText(getApplicationContext(), "กรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                edt.requestFocus();
                return false;
            } else {
                return true;
            }
        }
        catch (Exception e){
            return false;
        }

    }


    public void UpdateToServerFunction() {

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        final String ConvertImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {

                progressDialog = ProgressDialog.show(UpdatefoodActivity.this, "กำลังอัพโหลดอาหาร", "กรุณารอสักครู่..", false, false);
                super.onPreExecute();
            }



            @Override
            protected String doInBackground(Void... params) {

                Getdata getdt = new Getdata();


                FoodsFragment.ImageProcessClass imageProcessClass = new FoodsFragment.ImageProcessClass();

                HashMap<String, String> HashMapParams = new HashMap<String, String>();

                HashMapParams.put(TAG_FOODID, getdt.IdfoodAll.get(food_id).toString());

                HashMapParams.put(TAG_FOODNAME, edit_name.getText().toString());

                HashMapParams.put(TAG_PRICE, edit_price.getText().toString());

                HashMapParams.put(TAG_ABOUT, edit_about.getText().toString());

                HashMapParams.put(ImageName, "img");

                HashMapParams.put(TAG_IMGPATH, ConvertImage);


                String FinalData = imageProcessClass.ImageHttpRequest(ServerUpdatePath, HashMapParams);

                return FinalData;
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                Log.e("Excute", string1);

                progressDialog.dismiss();
                // Printing uploading success message coming from server on android app.
                Toast.makeText(getApplicationContext(), string1, Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
//                setToImageView(getResizedBitmap(bitmapset, 512));

                new ListfoodFragment.DownloadJSON().execute();
                finish();


            }

        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    private class DeleteOrder extends AsyncTask<Void, Void, Void> {
        int status;
        String DeleteField = getdt.SetIP+"/LoginServer/allfunc.php?func_name=DeleteField&food_id="+getdt.IdfoodAll.get(food_id).toString();

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String resultServer = NetConnect.getHttpGet(DeleteField);

                Log.e("DELETE ID", DeleteField );

                JSONObject c;

                c = new JSONObject(resultServer);

                try {
                    status = c.getInt(TAG_SUCCESS);
                    if (status == 1) {
                        Log.d("Delete Order OK", c.toString());
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
                Toast.makeText(getApplicationContext(), "ลบเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                new ListfoodFragment.DownloadJSON().execute();
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "ไม่สามารถลบได้ เพราะรายการถูกใช้", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

}
