package me.binplus.simsimigoogle;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by bjzhou on 13-5-30.
 */
public class MyApi {


    /* Simsimi api, keyword need to encode*/
    public static String simsimi(String keyword) {
        StringBuffer sb = new StringBuffer();
        try {
            URL cookie_url = new URL("http://www.simsimi.com/talk.htm");
            HttpURLConnection cookie_connection = (HttpURLConnection) cookie_url.openConnection();
            cookie_connection.connect();
            String cookie = cookie_connection.getHeaderField("Set-Cookie");
            cookie_connection.disconnect();
            System.out.println(cookie);
            URL url = new URL("http://www.simsimi.com/func/req?msg=" + keyword + "&lc=ch");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.20 Safari/537.36  OPR/15.0.1147.18 (Edition Next)");
            connection.setRequestProperty("Referer", "http://www.simsimi.com/talk.htm");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Cookie", cookie);
            connection.connect();
            Log.d("test", connection.getResponseCode() + "");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }
            reader.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = null;
        try {
            JSONObject jsonObject = new JSONObject(sb.toString());
            result = jsonObject.getString("response");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }

    /* google translate tts api, keyword need to encode */
    public static String googleNiang(Context context,String keyword) {

        String filename = UUID.randomUUID().toString().replace("-","");
        String filepath = context.getExternalCacheDir() + File.separator + filename;
        final File file = new File(filepath);


        try {
            URL url = new URL("http://translate.google.com/translate_tts?ie=UTF-8&q=" + keyword + "&tl=zh-CN&total=1&idx=0");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            Log.d("test", connection.getResponseCode() + "");
            InputStream is = connection.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                fos.write(buffer);
            }
            fos.close();
            is.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filepath;
    }
}
