package com.example.mysavethebunny;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    // Bitmaps for background, ground, and rabbit images
    Bitmap background, ground, rabbit;
    // Rectangles for positioning the background and ground
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30; // Time interval for updates
    Runnable runnable; // Runnable for the game loop
    Paint textPaint = new Paint(); // Paint for drawing text
    Paint healthPaint = new Paint(); // Paint for drawing health bar
    float TEXT_SIZE = 120; // Size of the text to be drawn
    int points = 0; // Player's current points
    int life = 3; // Player's lives
    static int dWidth, dHeight; // Screen dimensions
    Random random; // Random number generator
    float rabbitX, rabbitY; // Rabbit's current position
    float oldX; // Previous touch X position
    float oldRabbitX; // Previous rabbit X position
    ArrayList<Spike> spikes; // List of spikes in the game
    ArrayList<Explosion> explosions; // List of explosions in the game

    // Rabbit hitbox dimensions (47x80)
    int rabbitHitboxWidth = 47;
    int rabbitHitboxHeight = 80;

    // Constructor initializes the game view
    public GameView(Context context) {
        super(context);
        this.context = context;

        // Load bitmap resources
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        rabbit = BitmapFactory.decodeResource(getResources(), R.drawable.rabbit);

        // Get the screen size for positioning
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x; // Store the screen width
        dHeight = size.y; // Store the screen height

        // Create rectangles for the background and ground
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);

        // Initialize handler and runnable for the game loop
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate(); // Redraw the view
            }
        };

        // Set up text paint properties
        textPaint.setColor(Color.rgb(255, 165, 0)); // Set text color to orange
        textPaint.setTextSize(TEXT_SIZE); // Set text size
        textPaint.setTextAlign(Paint.Align.LEFT); // Align text to the left
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.kenney_blocks)); // Set font

        // Set up health paint properties
        healthPaint.setColor(Color.GREEN); // Set health bar color to green

        random = new Random(); // Initialize random number generator
        rabbitX = dWidth / 2 - rabbit.getWidth() / 2; // Center the rabbit horizontally
        rabbitY = dHeight - ground.getHeight() - rabbit.getHeight(); // Position the rabbit above the ground

        spikes = new ArrayList<>(); // Initialize spike list
        explosions = new ArrayList<>(); // Initialize explosion list

        // Create and add initial spikes to the game
        for (int i = 0; i < 3; i++) {
            Spike spike = new Spike(context);
            spikes.add(spike);
        }
    }

    // Method to draw the game elements
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        // Draw background, ground, and rabbit
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);
        canvas.drawBitmap(rabbit, rabbitX, rabbitY, null);

        // Draw spikes and handle their movement and collision
        for (int i = 0; i < spikes.size(); i++) {
            Spike spike = spikes.get(i);
            canvas.drawBitmap(spike.getSpike(spike.spikeFrame), spike.spikeX, spike.spikeY, null);
            spike.spikeFrame++; // Update the spike frame for animation
            if (spike.spikeFrame > 2) {
                spike.spikeFrame = 0; // Reset to the first frame
            }
            spike.spikeY += spike.spikeVelocity; // Move the spike down

            // Check if the spike has reached the ground
            if (spike.spikeY + spike.getSpikeHeight() >= dHeight - ground.getHeight()) {
                points += 10; // Increase points for avoiding the spike
                Explosion explosion = new Explosion(context);
                explosion.explosionX = spike.spikeX; // Set explosion position
                explosion.explosionY = spike.spikeY; // Set explosion position
                explosions.add(explosion); // Add explosion to the list
                spike.resetPosition(); // Reset spike position
            }
        }

        // Collision detection with rabbit's smaller hitbox (47x80)
        for (int i = 0; i < spikes.size(); i++) {
            Spike spike = spikes.get(i);
            // Calculate the rabbit's hitbox position
            float rabbitHitboxX = rabbitX + (rabbit.getWidth() - rabbitHitboxWidth) / 2;
            float rabbitHitboxY = rabbitY;

            // Check for collision with spike
            if (spike.spikeX + spike.getSpikeWidth() >= rabbitHitboxX
                    && spike.spikeX <= rabbitHitboxX + rabbitHitboxWidth
                    && spike.spikeY + spike.getSpikeWidth() >= rabbitHitboxY
                    && spike.spikeY + spike.getSpikeWidth() <= rabbitHitboxY + rabbitHitboxHeight) {
                life--; // Decrease life
                spike.resetPosition(); // Reset spike position
                // Check if the player has lost all lives
                if (life == 0) {
                    // Start GameOver activity with the current points
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish(); // End the current activity
                }
            }
        }

        // Draw explosions safely
        for (int i = explosions.size() - 1; i >= 0; i--) {
            Explosion explosion = explosions.get(i);
            canvas.drawBitmap(explosion.getExplosion(explosion.explosionFrame), explosion.explosionX,
                    explosion.explosionY, null);
            explosion.explosionFrame++; // Update explosion frame
            // Remove explosion if it has finished animating
            if (explosion.explosionFrame > 3) {
                explosions.remove(i);
            }
        }

        // Update health bar color based on remaining lives
        if (life == 2) {
            healthPaint.setColor(Color.YELLOW); // Set to yellow if 2 lives left
        } else if (life == 1) {
            healthPaint.setColor(Color.RED); // Set to red if 1 life left
        }
        // Draw the health bar on the screen
        canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 60 * life, 80, healthPaint);

        // Draw the current points on the screen
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);

        // Schedule the next update
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    // Method to handle touch events on the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX(); // Get the X position of the touch
        float touchY = event.getY(); // Get the Y position of the touch

        // Allow movement only if the touch is on or above the rabbit
        if (touchY >= rabbitY) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.getX(); // Store the initial touch position
                oldRabbitX = rabbitX; // Store the rabbit's initial position
            }
            if (action == MotionEvent.ACTION_MOVE) {
                // Calculate the shift based on touch movement
                float shift = oldX - touchX;
                float newRabbitX = oldRabbitX - shift; // Update rabbit's X position

                // Ensure the rabbit stays within the screen boundaries
                if (newRabbitX <= 0)
                    rabbitX = 0; // Prevent moving off the left edge
                else if (newRabbitX >= dWidth - rabbit.getWidth())
                    rabbitX = dWidth - rabbit.getWidth(); // Prevent moving off the right edge
                else
                    rabbitX = newRabbitX; // Update rabbit position if within bounds
            }
        }
        return true; // Indicate that the touch event was handled
    }
}
