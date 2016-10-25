package com.example.notebook.onlineshop.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Gamatechno Developer on 14/07/2016.
 */
public class HttpRequestData extends AsyncTask<String, String, String> {

    String URL;
    MultipartCallback multipartCallback;

    public HttpRequestData(String URL, MultipartCallback multipartCallback) {
        this.URL = URL;
        this.multipartCallback = multipartCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        multipartCallback.onPreExecuted();
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "";
        try {
            MultipartUtility multipartUtility = multipartCallback.parameters(URL);
            response = multipartUtility.finish().get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Multipart Response", response);
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        multipartCallback.onResponseServer(s);

    }

    public interface MultipartCallback {
        void onPreExecuted();
        MultipartUtility parameters(String url);
        void onResponseServer(String string);
    }
}
