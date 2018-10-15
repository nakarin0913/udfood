package com.udrumobile.foodapplication.printhtml;

import android.content.Context;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PrintHTMLFile {

    WebView myWebView;
    Context context;

    public void PrintHTMLFile(Context co){
        this.context=co;
    }

    private void Print(){
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

        String content ="";

        String html = "<html><body>"+ content +"</body></html>";


        webView.loadDataWithBaseURL(null, html,
                "text/HTML", "UTF-8", null);

        myWebView = webView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createWebPrintJob(WebView webView) {

        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                webView.createPrintDocumentAdapter("MyDocument");

        String jobName = "Bill Printing";

        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

}
