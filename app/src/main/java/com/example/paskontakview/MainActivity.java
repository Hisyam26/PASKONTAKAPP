package com.example.paskontakview;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Model> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.r);
        recyclerView.setHasFixedSize(true);
        AndroidNetworking.initialize(getApplicationContext());
        getDataApi();
    }
public void getDataApi(){
    AndroidNetworking.get("https://www.thesportsdb.com/api/v1/json/1/search_all_leagues.php?c=England")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: " + response.toString());
                    {
                        try {
                            data = new ArrayList<>();
                            JSONArray results = response.getJSONArray("countrys");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject object = results.getJSONObject(i);
                                 data.add(new Model(object.getString("strLeague"),
                                         object.getString("strCurrentSeason"),
                                         object.getString("strBadge"),
                                         object.getString("strDescriptionEN")));
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                            adapter adapter = new adapter(data, new com.example.paskontakview.adapter.Callback() {
                                @Override
                                public void halo(int position) {
                                    Model Operator = data.get(position);
                                    Intent move = new Intent(getApplicationContext(), Detail.class);
                                    move.putExtra("desk", Operator.getDeskripsi());
                                    move.putExtra("nama", Operator.getJudul());
                                    move.putExtra("gambar", Operator.getGambar());
                                    startActivity(move);
                                }
                            });

                            recyclerView.setAdapter(adapter);
                           } catch (JSONException e) {
                                 e.printStackTrace();
                         }
                      }
                }@Override
                public void onError(ANError error) {
                    Log.d(TAG, "onError: " + error);
                }
            });
   }
}