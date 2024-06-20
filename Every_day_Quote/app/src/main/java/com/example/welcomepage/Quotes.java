package com.example.welcomepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Quotes extends AppCompatActivity {
    private static final String TAG = "HERE";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView QuoteView;
    private TextView AuthorView;
    private Button btn;
    private Handler handler = new Handler();
    private Runnable quoteRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);
        QuoteView = findViewById(R.id.Quote);
        AuthorView = findViewById(R.id.Author);
        btn =findViewById(R.id.Another_Quote);
        setTheQuote();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTheQuote();
                           }
        });
        change_every_hour();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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


