package com.example.mysavethebunny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {

    // TextView to display the player's points
    TextView tvPoints;
    // TextView to display the highest score
    TextView tvHighest;
    // SharedPreferences to store and retrieve the highest score
    SharedPreferences sharedPreferences;
    // ImageView to show a new highest score indicator
    ImageView ivNewHighest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the game_over layout
        setContentView(R.layout.game_over);

        // Initialize TextView and ImageView references
        tvPoints = findViewById(R.id.tvPoints);
        tvHighest = findViewById(R.id.tvHighest);
        ivNewHighest = findViewById(R.id.ivNewHighest);

        // Retrieve the points passed from the previous activity
        int points = getIntent().getExtras().getInt("points");
        // Display the points on the screen
        tvPoints.setText("" + points);

        // Initialize SharedPreferences to store the highest score
        sharedPreferences = getSharedPreferences("my_pref", 0);
        // Retrieve the current highest score
        int highest = sharedPreferences.getInt("highest", 0);

        // Check if the current score exceeds the highest score
        if (points > highest) {
            // Show the new highest score indicator
            ivNewHighest.setVisibility(View.VISIBLE);
            // Update the highest score to the current score
            highest = points;
            // Save the new highest score in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highest", highest);
            editor.commit(); // Commit the changes
        }

        // Display the highest score on the screen
        tvHighest.setText("" + highest);
    }

    // Method to restart the game
    public void restart(View view) {
        // Create a new GameView instance and set it as the content view
        GameView gameView = new GameView(this);
        setContentView(gameView);
    }

    // Method to exit to the main menu
    public void exit(View view) {
        // Create an intent to navigate back to the MainActivity
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Clear the activity stack
        startActivity(intent); // Start the MainActivity
        finish();  // End the GameOver activity
    }
}
