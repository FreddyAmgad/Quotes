package com.example.welcomepage;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adaptor.Adaptor_Fav;
import Utils.DataBase;

public class Favourite extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> favoriteQuotesList;
    private DataBase database;
    private Adaptor_Fav adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourite);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        favoriteQuotesList = new ArrayList<>();
        database = new DataBase(this);

        // Fetch all favorite quotes from the database
        favoriteQuotesList = database.getAllTasks();

        // Initialize and set adapter for RecyclerView
        adapt = new Adaptor_Fav(favoriteQuotesList);
        recyclerView.setAdapter(adapt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Fav_act), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}