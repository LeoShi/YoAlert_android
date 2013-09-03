package com.yoalert.cops;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.*;

public class RequestWrapper {

    public static Response post(String url, JSONObject data) {
        return getJsonResponse(data, new HttpPost(url));
    }

    public static Response put(String url, JSONObject data) {
        return getJsonResponse(data, new HttpPut(url));
    }

    private static Response getJsonResponse(JSONObject data, HttpEntityEnclosingRequestBase request) {
        request.addHeader("content-type", "application/json");
        try {
            request.setEntity(new StringEntity(data.toString()));
            return request(request);

        } catch (UnsupportedEncodingException ignored) {
        }
        return null;
    }

    public static Response get(String url)
    {
        return request(new HttpGet(url));
    }

    private static Response request(HttpUriRequest request) {
        HttpResponse response;
        String content = null;
        try {
            response = new DefaultHttpClient().execute(request);
            int http_status_code = response.getStatusLine().getStatusCode();
            Log.i("Http Status Code", Integer.toString(http_status_code));
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream input_stream = entity.getContent();
                content = convertStreamToString(input_stream);
                Log.i("Content", content);
                input_stream.close();
            }
            return new Response(http_status_code, content);

        } catch (Exception e) {
            Log.e("HttpRequest Error:", e.toString());
        }
        return null;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
