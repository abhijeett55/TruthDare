package com.example.truthdare;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DareActivity extends AppCompatActivity {

    private ArrayList<TruthItem> truthList;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dare);


        sharedPreferences = getSharedPreferences("mySharedPreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        truthList = new ArrayList<>();

        recyclerViewConfig();
        populateDefaultData();
        if(sharedPreferences.contains("UserDares"))
            populateUserData(sharedPreferences.getString("UserDares", null));

    }

    public void populateDefaultData() {
        Values values = new Values();
        for(int i=0; i<values.dares.length; i++)
            truthList.add(new TruthItem(values.dares[i]));
    }

    public void populateUserData(String jsonDares) {
        String[] dares = gson.fromJson(jsonDares, String[].class);
        for(int i=0; i<dares.length; i++)
            truthList.add(new TruthItem(dares[i]));
    }

    private void recyclerViewConfig() {
        recyclerView = findViewById(R.id.recyclerView);

        //performance
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        adapter = new TruthAdapter(truthList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);


    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // setup buttons
        final EditText input = dialog.findViewById(R.id.editText);
        Button dismiss = dialog.findViewById(R.id.dismiss);
        Button add = dialog.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mText = input.getText().toString();
                if(mText.isEmpty())
                    Toast.makeText(getApplicationContext(), "Empty Text", Toast.LENGTH_LONG).show();
                else{
                    updateUserData(mText);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void updateUserData(String string) {
        ArrayList<String> textList = new ArrayList<>();

        if(sharedPreferences.contains("UserDares")) {
            String jsonDares = sharedPreferences.getString("UserDares", null);
            String[] dares = gson.fromJson(jsonDares, String[].class);
            for(int i=0; i<dares.length; i++)
                textList.add(dares[i]);
        }

        textList.add(string);
        editor.putString("UserDares", gson.toJson(textList));
        editor.apply();
        truthList.add(new TruthItem(string));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            showDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}