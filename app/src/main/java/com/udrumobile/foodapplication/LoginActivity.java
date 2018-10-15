/***
 * @author Ahmed Saleh
 * Created by ahmadssb on 2014-12-13
 * Website: http://www.ahmadssb.com
 * Email: ahmad@ahmadssb.com
 * Facebook: https://www.facebook.com/ahmadssbblog
 * Twitter: https://twitter.com/ahmadssb
 * YouTube: http://www.youtube.com/user/ahmadssbblog
 */

package com.udrumobile.foodapplication;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.udrumobile.foodapplication.admin.MainActivityAdmin;
import com.udrumobile.foodapplication.databaselocal.SQLiteHelper;
import com.udrumobile.foodapplication.user.MainActivityUser;
import com.udrumobile.foodapplication.module.Getdata;
import com.udrumobile.foodapplication.owner.MainActivityOwner;

public class LoginActivity extends Activity {
    private static EditText edit_user, edit_pass;

    private ProgressDialog pDialog;

    CardView card_login, card_register;

    JSONParser jsonParser = new JSONParser();
    Getdata getdt = new Getdata();

    //	private static String LOGIN_URL = "http://www.udfood.xyz/LoginServer/login.php";
    String LOGIN_URL = getdt.SetIP + "/LoginServer/login.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_GETUSERID = "user_id";
    private static final String TAG_POSITION = "position";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_GETNAME = "nameuser";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_STATUS = "status";

    public static SQLiteHelper sqLiteHelper;
    public static String DBName = "DBMEMLOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e("classname", this.getClass().getName());

        edit_user = (EditText) findViewById(R.id.edtUsername);
        edit_pass = (EditText) findViewById(R.id.edtPassword);

        card_login = (CardView) findViewById(R.id.card_login);

        card_register = (CardView) findViewById(R.id.card_register);

        card_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNullTextView(edit_user) == true && CheckNullTextView(edit_pass) == true) {


                    Cursor cursor = LoginActivity.sqLiteHelper.getData("SELECT * FROM " + DBName);

                    if(cursor.getCount()>0){

                        sqLiteHelper.queryData("UPDATE "+DBName+" SET username='"+edit_user.getText().toString().trim()+"', password='"+edit_pass.getText().toString().trim()+"' WHERE id=1");

                        new AttemptLogin().execute();
                    }

                    else {
                        sqLiteHelper.insertData(edit_user.getText().toString().trim(), edit_pass.getText().toString().trim());
                        new AttemptLogin().execute();
                    }


                }

            }
        });

        card_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });


        SQLITE_LOGIN();
    }


    public void SQLITE_LOGIN() {

        sqLiteHelper = new SQLiteHelper(LoginActivity.this, DBName + ".sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS " + DBName + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, password VARCHAR)");

    }


    private boolean CheckNullTextView(EditText edt) {
        String GetEditText = edt.getText().toString();

        if (TextUtils.isEmpty(GetEditText)) {
            Toast.makeText(getApplicationContext(), "กรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
            edt.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("ออกจากแอปพลิเคชัน")
                    .setMessage("คุณแน่ใจว่าจะออกจากแอปพลิเคชัน ?")
                    .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }

                    })
                    .setNegativeButton("ไม่ใช่", null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }


    class AttemptLogin extends AsyncTask<String, String, String> {

        String email, password;
        int id;

        private AttemptLogin (String email, String password){
            this.email =email;
            this.password =password;
        }

        private AttemptLogin (){

        }


        @Override
        protected void onCancelled(String s) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("กรุณารอสักครู่ ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            new CountDownTimer(7000, 7000) {
                public void onTick(long millisUntilFinished) {
                    // You can monitor the progress here as well by changing the onTick() time
                }

                public void onFinish() {
                    // stop async task if not in progress
                    pDialog.dismiss();


                }
            }.start();


        }

        @Override
        protected String doInBackground(String... args) {

            int success;

            email = edit_user.getText().toString();
            password = edit_pass.getText().toString();

            try {


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_EMAIL, email));
                params.add(new BasicNameValuePair(TAG_PASSWORD, password));


                JSONObject json = JSONParser.makeHttpRequest(LOGIN_URL, "POST", params);


                success = json.getInt(TAG_SUCCESS);


                getdt.IDUser = json.getString(TAG_GETUSERID);


                getdt.PositionUser = json.getString(TAG_POSITION);

                if (json.getString(TAG_STATUS).equals("0")) {
                    getdt.StatusUser = false;
                } else {
                    getdt.StatusUser = true;
                }


                if (success == 1) {

                    if (getdt.PositionUser.equals("admin")) {

                        Intent i = new Intent(LoginActivity.this,
                                MainActivityAdmin.class);
                        startActivity(i);
                    } else if (getdt.PositionUser.equals("user")) {

                        Intent i = new Intent(LoginActivity.this,
                                MainActivityUser.class);
                        startActivity(i);

                    } else if (getdt.PositionUser.equals("owner")) {

                        if (json.getString(TAG_STATUS).equals("1")) {
                            Intent i = new Intent(LoginActivity.this,
                                    MainActivityOwner.class);
                            startActivity(i);
                        } else {
                            return "ขออภัยระบบยังไม่ยืนยันตัวตน";

                        }


                    } else {

                        return "ไม่พบข้อมูล กรุณาลองใหม่อีกครั้ง";

                    }
                    Log.d("Login Successful!", json.toString());

                    getdt.NameUser = json.getString(TAG_GETNAME);


                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {

            }

            return "ไม่พบข้อมูล กรุณาลองใหม่อีกครั้ง";


        }


        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), file_url, Toast.LENGTH_LONG).show();


        }

    }

}