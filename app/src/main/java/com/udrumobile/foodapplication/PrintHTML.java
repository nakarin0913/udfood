package com.udrumobile.foodapplication;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PrintHTML {

    Context context;

    public void PrintHTML(Context con){
        this.context=con;
    }

    WebView myWebView;


    private void Print() {


        WebView webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view,
                                                    String url)
            {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageFinished(WebView view, String url)
            {
                createWebPrintJob(view);
                myWebView = null;
            }
        });

        String htmlDocument =
                "<link href=\"https://fonts.googleapis.com/css?family=Kanit\" rel=\"stylesheet\">\n" +
                        "<style>\n" +
                        "    p {\n" +
                        "        font-family: 'Kanit', sans-serif;\n" +
                        "    }\n" +
                        "\n" +
                        "    .num {\n" +
                        "        text-align: left;\n" +
                        "    }\n" +
                        "\n" +
                        "    .price {\n" +
                        "        text-align: left;\n" +
                        "    }\n" +
                        "\n" +
                        "    table {\n" +
                        "        table-layout: fixed;\n" +
                        "        width: 100%;\n" +
                        "    }\n" +
                        "</style>\n" +
                        "<p style=\"font-size: 18px; text-align: center; margin-top: 30px;\">ใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ</p>\n" +
                        "<p style=\"font-size: 14px; text-align: left;\">รหัส : #4221154521 &emsp;วันที่ : 18/07/2561</p>\n" +
                        "<table style=\"table-layout:auto;\">\n" +
                        "    <tr>\n" +
                        "        <td>1</td><td>ทดสdfdf</td><td>price</td>\n" +
                        "    </tr>\n" +
                        "     <tr>\n" +
                        "        <td>1</td><td>ทดสอบ</td><td>price</td>\n" +
                        "    </tr>\n" +
                        "     <tr>\n" +
                        "        <td>1</td><td>ทดสอบ</td><td>price</td>\n" +
                        "    </tr>\n" +
                        "</table>";

        webView.loadDataWithBaseURL(null, htmlDocument,
                "text/HTML", "UTF-8", null);

        myWebView = webView;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createWebPrintJob(WebView webView) {

        PrintManager printManager = (PrintManager) context
                .getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                webView.createPrintDocumentAdapter("MyDocument");

        String jobName = "printHTML";

        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

}
