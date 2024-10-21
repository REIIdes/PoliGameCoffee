package com.example.mysavethebunny;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the superclass's onCreate method
        setContentView(R.layout.activity_main); // Set the content view to the main layout

        // Keep the screen on while the activity is running
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Method to start the game when the button is clicked
    public void startGame(View view) {
        GameView gameView = new GameView(this); // Create a new instance of GameView
        setContentView(gameView); // Replace the current content view with the game view
    }
}
