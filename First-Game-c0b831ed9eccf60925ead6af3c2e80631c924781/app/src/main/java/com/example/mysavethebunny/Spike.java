package com.example.mysavethebunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Spike {
    // Array to hold spike images for animation
    Bitmap spike[] = new Bitmap[3];
    int spikeFrame = 0; // Current animation frame
    int spikeX, spikeY, spikeVelocity; // Position and speed of the spike
    Random random; // Random number generator

    // Constructor to initialize the spike
    public Spike(Context context) {
        // Load spike images from resources
        spike[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spike0);
        spike[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spike1);
        spike[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spike2);
        random = new Random(); // Initialize random generator
        resetPosition(); // Set initial position and speed
    }

    // Get the current spike image based on the animation frame
    public Bitmap getSpike(int spikeFrame) {
        return spike[spikeFrame]; // Return the spike image
    }

    // Get the width of the spike image
    public int getSpikeWidth() {
        return spike[0].getWidth(); // Return width of the first spike image
    }

    // Get the height of the spike image
    public int getSpikeHeight() {
        return spike[0].getHeight(); // Return height of the first spike image
    }

    // Reset the position and speed of the spike
    public void resetPosition() {
        // Randomly set x position within screen width
        spikeX = random.nextInt(GameView.dWidth - getSpikeWidth());
        // Set y position above the screen
        spikeY = -200 - random.nextInt(600);
        // Randomly set speed of the spike
        spikeVelocity = 35 + random.nextInt(16);
    }
}
