package com.vgaw.rongyundemo.http;

import com.vgaw.rongyundemo.protopojo.FlyCatProto;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/10/25.
 */
public class HttpRequest {
    public void request(FlyCatProto.FlyCat flyCat) {
        String uri = "http://192.168.64.171:7778/";

        URL url = null;
        try {
            url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.setInstanceFollowRedirects(true);
            /*conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);*/
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Connection", "close");

            OutputStream out = conn.getOutputStream();
            out.write(flyCat.toByteArray());
            out.flush();
            out.close();

            InputStream in = null;
            if (conn.getResponseCode() == 200) {
                in = conn.getInputStream();
            } else {
                in = conn.getErrorStream();
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FlyCatProto.FlyCat   requestForResult(FlyCatProto.FlyCat flyCat) {
        String uri = "http://192.168.64.171:7778/";

        URL url;
        try {
            url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.setInstanceFollowRedirects(true);
            /*conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);*/
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Connection", "close");

            OutputStream out = conn.getOutputStream();
            out.write(flyCat.toByteArray());
            out.flush();
            out.close();

            InputStream in = null;
            if (conn.getResponseCode() == 200) {
                in = conn.getInputStream();
            } else {
                in = conn.getErrorStream();
            }
            return FlyCatProto.FlyCat.parseFrom(readInputStream(in));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

}
