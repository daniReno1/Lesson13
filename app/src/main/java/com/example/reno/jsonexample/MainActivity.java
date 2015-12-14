package com.example.reno.jsonexample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends Activity
{
    JSONAdapter mJSONAdapter;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();

    EditText searchText;

    private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        searchText = new EditText(this);
        searchText.setWidth(200);

        Button searchButton = new Button(this);
        searchButton.setText("Search");
        searchButton.setOnClickListener(searchButtonListener);

        mainListView = new ListView(this);

        mArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                mNameList);

        mainListView.setAdapter(mArrayAdapter);

        mJSONAdapter = new JSONAdapter(this, getLayoutInflater());

        mainListView.setAdapter(mJSONAdapter);

        layout.addView(searchText);
        layout.addView(searchButton);
        layout.addView(mainListView);

        setContentView(layout);


    }

    View.OnClickListener searchButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            doQuery(searchText.getText().toString());
        }
    };



    private void doQuery(String searchString)
    {

        String urlString = "";

        try
        {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AsyncHttpClient client = new AsyncHttpClient();

        // AsyncHttpClient.get(String url, ResponseHandlerInterface responseHandler)
        client.get(QUERY_URL + urlString,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        mJSONAdapter.updateData(jsonObject.optJSONArray("docs"));
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error)
                    {
                        Log.e("Query Failure", statusCode + " " + throwable.getMessage());
                    }
                });
    }
}