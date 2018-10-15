package com.udrumobile.foodapplication.module;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by CTZC on 3/24/2017.
 */

public class Getdata {

    //    public static String SetIP="http://192.168.1.23";
    public static String SetIP = "http://www.udfood.xyz";
//    public static String SetIP = "http://10.0.2.2";

    public static ArrayList<Boolean> SetUserCheck = new ArrayList<Boolean>();
    public static ArrayList<Integer> SetIdOwnerAll = new ArrayList<Integer>();

    public static ArrayList<Integer> SetIdFoodRec = new ArrayList<Integer>();
    public static ArrayList<Boolean> SetRecCheck = new ArrayList<Boolean>();

    public static String URL = "";

    public static String LatLon = "17.397605,102.794518";

    public static String LocationName = "";

    public static String LocationDetial = "";

    public static String NameUser = "";

    public static String IDUser = "";

    public static String IDRes = "";

    public static Boolean StatusUser;

    public static String PositionUser = "";

    public static Integer Distance = 0;

    public static String ResID_Select = "";


    public static String Timeuse = "";

    public static LatLng endLocation;
    public static LatLng startLocation;


    //ข้อมูลร้านอาหาร
    public static ArrayList<String> NameresAll = new ArrayList<>();
    public static ArrayList<String> ResIDAll = new ArrayList<>();
    public static ArrayList<String> ResAboutAll = new ArrayList<>();
    public static ArrayList<String> ResWorkingtimeAll = new ArrayList<>();
    public static ArrayList<String> ResImageAll = new ArrayList<>();


    //food
    public static ArrayList<Integer> IdfoodAll = new ArrayList<>();
    public static ArrayList<String> NamefoodAll = new ArrayList<>();
    public static ArrayList<String> ImagefoodAll = new ArrayList<>();
    public static ArrayList<String> PricefoodAll = new ArrayList<>();
    public static ArrayList<String> AboutfoodAll = new ArrayList<>();
    public static ArrayList<String> LatLon_Res = new ArrayList<>();
    public static LatLng loca_user;
    public static ArrayList<Bitmap> BitmapFoodAll = new ArrayList<>();

    //food_rec
    public static ArrayList<Integer> IdfoodAll_Rec = new ArrayList<>();
    public static ArrayList<String> NamefoodAll_Rec = new ArrayList<>();
    public static ArrayList<String> ImagefoodAll_Rec = new ArrayList<>();
    public static ArrayList<Bitmap> BitmapFoodAll_Rec = new ArrayList<>();
    public static ArrayList<String> AboutfoodAll_Rec = new ArrayList<>();
    public static ArrayList<String> PricefoodAll_Rec = new ArrayList<>();
    public static ArrayList<String> LatLon_Res_Rec = new ArrayList<>();



    //food_owner
    public static ArrayList<Bitmap> BitmapFoodAll_Owner = new ArrayList<>();
    public static Integer ID_Status = 0;
//    public static Integer Order_ID;
    public static Integer MAX_Mater = 500;


    //orb_seletc
    public static Integer Orb_Select=0;
    public static Integer OrderId=0;

    //
    public static Boolean Status_Btn_delete=false;

}
