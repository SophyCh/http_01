package com.yuqi.http_01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.Handler;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/3/31.
 */
public class HttpThread extends Thread{
    private ImageView imageView;
    private String url;
    private WebView webView;
    private Handler handler;

    public HttpThread(String url,WebView webView,Handler handler){
        this.url=url;
        this.webView=webView;
        this.handler=handler;
    }

    public HttpThread(String url,ImageView imageView,Handler handler){
        this.url=url;
        this.imageView=imageView;
        this.handler=handler;

    }

    @Override
    public void run() {
        try {
            URL httpUrl=new URL(url);
            try {
                HttpURLConnection connc= (HttpURLConnection) httpUrl.openConnection();
                connc.setReadTimeout(5000);
                connc.setRequestMethod("GET");
                connc.setDoInput(true);
                InputStream in=connc.getInputStream();

                FileOutputStream out=null;
                String fileName=String.valueOf(System.currentTimeMillis());
                File downloadFile=null;
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED
                )){
                    File parent = Environment.getExternalStorageDirectory();
                    downloadFile=new File(parent,fileName);
                    out =new FileOutputStream(downloadFile);
                }

                byte[] buffer=new byte[1024*2];
                int len=0;
                if(out!=null){
                    while((len=in.read(buffer)) !=-1){
                        out.write(buffer,0,len);
                    }
                }

                final Bitmap bitmap= BitmapFactory.decodeFile(downloadFile.getAbsolutePath());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });

//                final StringBuffer stringBuffer=new StringBuffer();
//                BufferedReader reader= new BufferedReader(new InputStreamReader(connc.getInputStream()));
//                String str;
//                while ((str=reader.readLine())!=null){
//                    stringBuffer.append(str);
//                }
//
//                handler.post(new Runnable(){
//                    @Override
//                    public void run() {
//                        webView.loadData(stringBuffer.toString(),"text/html;charset=utf-8",null);
//                    }
//                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
