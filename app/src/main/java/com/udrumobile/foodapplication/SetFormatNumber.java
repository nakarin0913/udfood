package com.udrumobile.foodapplication;

import java.text.DecimalFormat;

public class SetFormatNumber {

    public static String formatNumber (Double num) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(num);
    }

}
