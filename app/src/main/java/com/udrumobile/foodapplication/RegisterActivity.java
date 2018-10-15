package com.udrumobile.foodapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.udrumobile.foodapplication.module.Getdata;

public class RegisterActivity extends AppCompatActivity {
    EditText firstname, lastname, email, pass, confirmpass;
    CardView btnRegister;
    RadioGroup radioRigisterGroup;
    private ProgressDialog pDialog;

    Getdata getdt = new Getdata();
    private String REGISTER_URL = getdt.SetIP + "/LoginServer/register.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_LASTNAME = "lastname";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_POSITION = "position";
    private static final String TAG_STATUS = "status";

    private String typeUser = "user";

    private Boolean isEmail = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        radioRigisterGroup = (RadioGroup) findViewById(R.id.radioGroupRg);

        firstname = (EditText) findViewById(R.id.edtRegisterFirstName);
        lastname = (EditText) findViewById(R.id.edtRegisterLastName);
        email = (EditText) findViewById(R.id.edtRegisterEmail);
        pass = (EditText) findViewById(R.id.edtRegisterPassword);
        confirmpass = (EditText) findViewById(R.id.edtRegisterConfirmPassword);
        btnRegister = (CardView) findViewById(R.id.btnRegister);
        Log.e("classname", this.getClass().getName());
        this.setTitle("หน้าสมัครสมาชิก");

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckNullTextView(firstname) == true && CheckNullTextView(lastname) == true && CheckNullTextView(email) == true && CheckNullTextView(pass) == true && CheckNullTextView(confirmpass) == true && isEmail == true) {

                    if (pass.getText().toString().equals(confirmpass.getText().toString())) {
                        new Register().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "รหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show();
                        pass.requestFocus();
                    }

                }

                if (isEmail != true) {
                    Toast.makeText(getApplicationContext(), "รูปแบบอีเมลล์ไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (emailValidator(email.getText().toString().trim()) == true) {
                    Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });


        radioRigisterGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioRigisterGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);


                if (radioButton.getText().equals("ผู้ใช้ทั่วไป")) {
                    typeUser = "user";
                } else {
                    typeUser = "owner";
                }
            }
        });


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

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        isEmail = matcher.matches();
        return matcher.matches();
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


    class Register extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("กรุณารอสักครู่...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub


            int success;
            String fname = firstname.getText().toString();
            String lname = lastname.getText().toString();
            String txtemail = email.getText().toString();
            String txtpassword = pass.getText().toString();


            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_FIRSTNAME, fname));
                params.add(new BasicNameValuePair(TAG_LASTNAME, lname));
                params.add(new BasicNameValuePair(TAG_EMAIL, txtemail));
                params.add(new BasicNameValuePair(TAG_PASSWORD, txtpassword));
                params.add(new BasicNameValuePair(TAG_POSITION, typeUser));
                params.add(new BasicNameValuePair(TAG_STATUS, "0"));


                String resultServer = NetConnect.getHttpPost(REGISTER_URL, params);

                JSONObject c;
                c = new JSONObject(resultServer);

                try {
                    success = c.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("User Created!", c.toString());
                        finish();
                        return c.getString(TAG_MESSAGE);
                    } else {
                        Log.d("Register Failure!", c.getString(TAG_MESSAGE));
                        return c.getString(TAG_MESSAGE);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                return c.getString(TAG_MESSAGE);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();


            if (file_url != null) {
                Toast.makeText(RegisterActivity.this, file_url,
                        Toast.LENGTH_LONG).show();
            }

        }

    }

}
