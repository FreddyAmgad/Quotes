package com.example.welcomepage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "HERE";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static List<String> quotesList = new ArrayList<>();
    public static List<String> authorList = new ArrayList<>();
    private Button Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        Btn = findViewById(R.id.Start);

        // Disable button until quotes are loaded
        Btn.setEnabled(false);

        fetchQuotes();

        gotonewact();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void gotonewact() {
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ensure lists are populated before starting Quotes activity
                if (!quotesList.isEmpty() && !authorList.isEmpty()) {
                    startActivity(new Intent(MainActivity.this, Quotes.class));
                } else {
                    Log.d(TAG, "Lists are empty, cannot start Quotes activity");
                }
            }
        });
    }

    private void fetchQuotes() {
        Log.d(TAG, "Starting fetchQuotes method");
        db.collection("Q")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "Entered onComplete");

                        if (task.isSuccessful()) {
                            Log.d(TAG, "Query successful");
                            Log.d(TAG, "Task Size: " + task.getResult().size());

                            quotesList.clear();
                            authorList.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "Document ID: " + document.getId() + " => Data: " + document.getData());
                                String quote = document.getString("Q");
                                String author = document.getString("A");
                                Log.d(TAG, "Quote: " + quote + ", Author: " + author);
                                if (quote != null && author != null) {
                                    quotesList.add(quote);
                                    authorList.add(author);
                                } else {
                                    Log.d(TAG, "Missing Quote or Author in document ID: " + document.getId());
                                }
                            }
                            Log.d(TAG, "Quotes List Size: " + quotesList.size() + ", Authors List Size: " + authorList.size());

                            // Enable button after quotes are loaded
                            Btn.setEnabled(true);

                        } else {
                            Log.d(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
