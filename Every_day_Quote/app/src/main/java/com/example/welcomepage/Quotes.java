package com.example.welcomepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.welcomepage.R;

import Utils.DataBase;

public class Quotes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HERE";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView QuoteView;
    private TextView AuthorView;
    private Button btn;
    private Handler handler = new Handler();
    private Runnable quoteRunnable;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Button fav;
    private DataBase database;
    private Button Share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        database = new DataBase(this);
        QuoteView = findViewById(R.id.Quote);
        AuthorView = findViewById(R.id.Author);
        btn =findViewById(R.id.Another_Quote);
        fav = findViewById(R.id.fav);
        Share =findViewById(R.id.share);
        drawerLayout =findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.BAR);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toogle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        setTheQuote();
        change_Quote();
        change_every_hour();
        Fav_Quotes();
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, MainActivity.quotesList.get(0).toString());
                intent.setType("text/plain");
                intent= Intent.createChooser(intent,"Share via:");
                startActivity(intent);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.Home) {
            // Handle Home item click
        } else if (itemId == R.id.Favourite) {
            startActivity(new Intent(Quotes.this, Favourite.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void displayQuotes() {
        List<String> quotes = database.getAllTasks();
        StringBuilder quotesText = new StringBuilder();
        for (String quote : quotes) {
            Log.d("QuotesActivity", "Here is the quote: " + quote);
        }
    }
    private void Fav_Quotes()
    {
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.insertTask(QuoteView.getText().toString());
                displayQuotes();
            }
        });
    }
    private void change_Quote()
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTheQuote();
            }
        });
    }
    private void change_every_hour()
    {
        quoteRunnable = new Runnable() {
            @Override
            public void run() {
                setTheQuote();
                // Schedule the Runnable to run again after 1 minute (60000 milliseconds)
                handler.postDelayed(this, 600000);
            }
        };

        // Start the periodic update
        handler.postDelayed(quoteRunnable, 600000); // Delay before first run (1 minute)

    }
    private int getRandomQuote(int size ) {
        Random random = new Random();
        int index = random.nextInt(size);
        return index;
    }
    private void setTheQuote()
    {
        if (!MainActivity.quotesList.isEmpty() && !MainActivity.authorList.isEmpty()) {
            int index = getRandomQuote(MainActivity.quotesList.size());
            QuoteView.setText(MainActivity.quotesList.get(index));
            AuthorView.setText("الكاتب : " + MainActivity.authorList.get(index));
        } else {
            QuoteView.setText("No quotes available.");
            AuthorView.setText("No authors available.");
        }
    }


}


