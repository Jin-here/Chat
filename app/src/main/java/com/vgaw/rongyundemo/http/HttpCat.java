package com.vgaw.rongyundemo.http;

import android.os.AsyncTask;

import com.vgaw.rongyundemo.protopojo.FlyCatProto;
import com.vgaw.rongyundemo.view.Loading;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/10/25.
 */
public class HttpCat {
    private static String uri = "http://192.168.1.110:7778/";

    public static void setUri(String uri1){
        uri = "http://" + uri1 + ":7778/";
    }

    public static void fly(FlyCatProto.FlyCat request, AbstractResponseListener listener){
        new MyAsnyTask(listener).execute(request);
    }

    public interface OnResponseListener{
        void onPreExecute();
        void onSuccess(FlyCatProto.FlyCat flyCat);
        void onException(FlyCatProto.FlyCat flyCat);
    }

    public static class AbstractResponseListener implements OnResponseListener{
        @Override
        public void onPreExecute() {}
        @Override
        public void onSuccess(FlyCatProto.FlyCat flyCat) {}
        @Override
        public void onException(FlyCatProto.FlyCat flyCat) {}
    }

    private static class MyAsnyTask extends AsyncTask<FlyCatProto.FlyCat, Void, FlyCatProto.FlyCat>{
        private AbstractResponseListener listener;

        public MyAsnyTask(AbstractResponseListener listener){
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            listener.onPreExecute();
        }

        @Override
        protected FlyCatProto.FlyCat doInBackground(FlyCatProto.FlyCat... params) {
            try {
                return requestForResult(params[0]);
            } catch (Exception e) {
                return null;
            }
            /*if (isCancelled()){
                break;
            }*/
        }

        @Override
        protected void onPostExecute(FlyCatProto.FlyCat flyCat) {
            if (flyCat != null){
                listener.onSuccess(flyCat);
            }else {
                listener.onException(flyCat);
            }
        }

        @Override
        protected void onCancelled(FlyCatProto.FlyCat flyCat) {
            super.onCancelled(flyCat);
        }
    }

    public void request(FlyCatProto.FlyCat flyCat) {
        URL url = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Connection", "close");

            out = conn.getOutputStream();
            out.write(flyCat.toByteArray());
            out.flush();

            if (conn.getResponseCode() == 200) {
                in = conn.getInputStream();
            } else {
                in = conn.getErrorStream();
            }
        } catch (Exception e) {
        }finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static FlyCatProto.FlyCat requestForResult(FlyCatProto.FlyCat flyCat) throws IOException {
        URL url = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Connection", "close");

            out = conn.getOutputStream();
            out.write(flyCat.toByteArray());
            out.flush();

            if (conn.getResponseCode() == 200) {
                in = conn.getInputStream();
            } else {
                in = conn.getErrorStream();
            }
            return FlyCatProto.FlyCat.parseFrom(readInputStream(in));
        }catch (Exception e){
            throw e;
        }finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    throw e;
                }
            }
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }

    public static byte[] readInputStream(InputStream inStream)  {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] data = outStream.toByteArray();
            return data;
        } catch (IOException e) {
        }finally {
            if (outStream != null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
