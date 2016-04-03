package com.bitjini.my_twitter_app;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by bitjini on 1/4/16.
 */
public class HttpUtil {
    public String getHttpResponse(HttpRequestBase request) {
        String result = null;
        try {


            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpResponse httpResponse = httpClient.execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String reason = httpResponse.getStatusLine().getReasonPhrase();
            StringBuilder sb = new StringBuilder();
            if (statusCode == 200) {

                HttpEntity entity = httpResponse.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader bReader = new BufferedReader(
                        new InputStreamReader(inputStream, "UTF-8"), 8);
                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sb.append(line);
                }
            } else {
                sb.append(reason);
            }
            result = sb.toString();
            Log.e("response :",""+result);
        } catch (UnsupportedEncodingException ex) {
        } catch (ClientProtocolException ex1) {
        } catch (IOException ex2) {
        }
        return result;
    }
}
