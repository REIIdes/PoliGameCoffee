package com.example.mysavethebunny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Explosion {
    // Array to store the different frames of the explosion animation (total 4 frames)
    Bitmap explosion[] = new Bitmap[4];
    // Variable to keep track of which frame of the explosion is currently being shown
    int explosionFrame = 0;
    // X and Y coordinates to define the position of the explosion on the screen
    int explosionX, explosionY;

    // Constructor that initializes the explosion frames using the provided context
    public Explosion(Context context) {
        // Load the explosion images from resources and store them in the explosion array
        explosion[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion0);
        explosion[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion1);
        explosion[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion2);
        explosion[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion3);
    }

    // Method to retrieve the current frame of the explosion animation
    public Bitmap getExplosion(int explosionFrame) {
        // Return the bitmap for the specified explosion frame
        return explosion[explosionFrame];
    }
}
