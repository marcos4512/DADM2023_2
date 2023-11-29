package com.unal.consumidor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unal.consumidor.adapters.BornsAdapter;
import com.unal.consumidor.model.BornRegistry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView bornList;
    private ArrayList<BornRegistry> bornRegistriesArrayList;
    private BornsAdapter bornsAdapter;
    private EditText momAgeEditView, dadAgeEditView;
    private AutoCompleteTextView autoCompleteTextViewGemder;
    private ArrayAdapter<String> adapterGender;
    private String[] item_gender = {"FEMENINO", "MASCULINO"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteTextViewGemder = findViewById(R.id.auto_complete_genero);
        adapterGender = new ArrayAdapter<>(this, R.layout.list_item, item_gender);
        autoCompleteTextViewGemder.setAdapter(adapterGender);

        momAgeEditView = findViewById(R.id.editTextMom);
        dadAgeEditView = findViewById(R.id.editTextDad);

        bornList = findViewById(R.id.dataList);
    }


    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.search_button) {

            String url = getURLQuery(autoCompleteTextViewGemder.getText().toString(), momAgeEditView.getText().toString(), dadAgeEditView.getText().toString());
            processDataResponse(url);
            //bornList.setLayoutManager(new LinearLayoutManager(this));
            //bornRegistriesArrayList  =
            //bornsAdapter = new BornsAdapter(bornRegistriesArrayList);
            //bornList.setAdapter(bornsAdapter);

        } else if (id == R.id.clean_button) {

            autoCompleteTextViewGemder.setText("");
            autoCompleteTextViewGemder.setHint(R.string.hint_gender);
            momAgeEditView.setText("");
            dadAgeEditView.setText("");

        }
    }

    private String getURLQuery(String gender, String momAge, String dadAge){
        String urlQuery = "https://datos.gov.co/resource/26g4-ekt3.json?";
        String searchQuery = "$where=";
        String andOption = "%20and%20";
        if (gender.isEmpty() && momAge.isEmpty() && dadAge.isEmpty()) {
            Log.d("MainActivity", "url = " + urlQuery);
            return  urlQuery;
        } else
            urlQuery = urlQuery.concat(searchQuery);

        if (!gender.isEmpty()){
            String focusedQuery = "genero%20=%20%27"+gender+"%27";
            urlQuery = urlQuery.concat(focusedQuery);
        }
        if (!momAge.isEmpty()){
            if (!gender.isEmpty()){
                urlQuery = urlQuery.concat(andOption);
            }
            String focusedQuery = "edad_madre="+momAge;
            urlQuery = urlQuery.concat(focusedQuery);
        }
        if (!dadAge.isEmpty()){
            if (!gender.isEmpty() || !momAge.isEmpty()){
                urlQuery = urlQuery.concat(andOption);
            }
            String focusedQuery = "edad_padre="+dadAge;
            urlQuery = urlQuery.concat(focusedQuery);
        }
        Log.d("MainActivity", "url = " + urlQuery);
        return urlQuery;
    }

    private ArrayList<BornRegistry> processDataResponse(String url) {
        ArrayList<BornRegistry> responseBornList = new ArrayList<>();
        StringRequest getRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //JSONObject jsonOResponse = new JSON(response);
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BornRegistry bornRegistry = new BornRegistry();

                        bornRegistry.setGender(jsonObject.getString("genero").toLowerCase());
                        bornRegistry.setWeight(jsonObject.getString("peso"));
                        bornRegistry.setSize(jsonObject.getString("talla"));
                        bornRegistry.setDate(jsonObject.getString("hora"));
                        bornRegistry.setMomAge(jsonObject.getString("edad_madre"));
                        bornRegistry.setDadAge(jsonObject.getString("edad_padre"));
                        responseBornList.add(bornRegistry);
                    }
                    bornList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    bornRegistriesArrayList  = responseBornList;
                    bornsAdapter = new BornsAdapter(bornRegistriesArrayList);
                    bornList.setAdapter(bornsAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Se presentó un error en la lectura de la respuesta de la API", Toast.LENGTH_SHORT);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error consumiendo API: ", error.getMessage());
                Toast.makeText(MainActivity.this, "Se presentó un error en el consumo de la API", Toast.LENGTH_SHORT);
            }
        });

        Volley.newRequestQueue(this).add(getRequest);
        return responseBornList;
    }

}