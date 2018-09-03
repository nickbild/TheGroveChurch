package com.grove.church.thegrovechurch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList al_details = new ArrayList();
    ListView sermonList;
    String url = "http://18.218.241.166/test.json";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sermonList = (ListView) findViewById(R.id.sermonList);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

        sermonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Selected item.
                String details = al_details.get(position).toString();

                // Launch new activity.
                Intent i = new Intent(getApplicationContext(), SermonDetails.class);
                // Send data to new activity.
                i.putExtra("details", details);
                startActivity(i);

            }
        });
    }

    void parseJsonData(String jsonString) {
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray sermonArray = jsonObj.getJSONArray("sermons");
            ArrayList al = new ArrayList();

            for(int i = 0; i < sermonArray.length(); ++i) {
                al.add(sermonArray.getJSONObject(i).getString("date"));
                // Lord, forgive me for my poor understanding of Android development and this horrible hack.
                al_details.add(sermonArray.getJSONObject(i));
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al);
            sermonList.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }

    public void givingActivity(View view) {
        Intent intent = new Intent(this, Giving.class);
        startActivity(intent);
    }
}
