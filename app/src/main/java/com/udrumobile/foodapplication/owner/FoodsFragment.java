package com.udrumobile.foodapplication.owner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.MapsInitializer;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;

public class FoodsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Bitmap bitmap, decoded;
    Button SelectImageGallery, UploadImageServer;
    ImageView imageView;
    EditText edit_foodname, edit_price, edit_about;
    ProgressDialog progressDialog;
    Getdata getdt = new Getdata();
    int bitmap_size = 60; // range 1 - 100
    String UserID = "user_id";
    String Foodname = "foodname";
    String About = "about";
    String ImageName = "image_name";
    String ImagePath = "image_path";
    String Price = "price";
    Bitmap bitmapset;
    //    String ServerUploadPath = "http://www.udfood.xyz/LoginServer/foods.php";
    String ServerUploadPath = getdt.SetIP + "/LoginServer/foods.php";

    public FoodsFragment() {
        // Required empty public constructor
    }

    public static FoodsFragment newInstance(String param1, String param2) {
        FoodsFragment fragment = new FoodsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("classname", this.getClass().getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_foods, container,
                false);


        edit_foodname = (EditText) view.findViewById(R.id.editFoodName);

        edit_price = (EditText) view.findViewById(R.id.editFoodPrice);

        edit_about = (EditText) view.findViewById(R.id.editFoodAbout);

        SelectImageGallery = (Button) view.findViewById(R.id.buttonSelect);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        SelectImageGallery = (Button) view.findViewById(R.id.buttonSelect);

        UploadImageServer = (Button) view.findViewById(R.id.button_update);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        bitmapset = BitmapFactory.decodeResource(getResources(),
                R.drawable.noimage);

        setToImageView(getResizedBitmap(bitmapset, 512));

        SelectImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });

        UploadImageServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( CheckNullTextView(edit_foodname) == true && CheckNullTextView(edit_price) == true && CheckNullTextView(edit_about) == true) {
                    ImageUploadToServerFunction();
                }

            }
        });
        return view;
    }

    private boolean CheckNullTextView(EditText edt) {
        String GetEditText = edt.getText().toString();

        if (TextUtils.isEmpty(GetEditText)) {
            Toast.makeText(getContext(), "กรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
            edt.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);
        Log.e("RESULT","RESULT");
        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                setToImageView(getResizedBitmap(bitmap, 512));
				imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e("RESULT","RESULT");
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    public void ImageUploadToServerFunction() {

        ByteArrayOutputStream byteArrayOutputStreamObject;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        decoded.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(getContext(), "กำลังอัพอาหาร", "กรุณารอสักครู่..", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                Log.e("Excute", string1);

                progressDialog.dismiss();
                // Printing uploading success message coming from server on android app.
                Toast.makeText(getContext(), string1, Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
                setToImageView(getResizedBitmap(bitmapset, 512));


            }

            @Override
            protected String doInBackground(Void... params) {

                Getdata getdt = new Getdata();


                FoodsFragment.ImageProcessClass imageProcessClass = new FoodsFragment.ImageProcessClass();

                HashMap<String, String> HashMapParams = new HashMap<String, String>();

                HashMapParams.put(UserID, getdt.IDUser);

                HashMapParams.put(Foodname, edit_foodname.getText().toString());

                HashMapParams.put(Price, edit_price.getText().toString());

                HashMapParams.put(About, edit_about.getText().toString());

                HashMapParams.put(ImageName, "img");

                HashMapParams.put(ImagePath, ConvertImage);


                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }


    public static class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null) {

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {


                stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }


}
