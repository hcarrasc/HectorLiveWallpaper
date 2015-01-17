package com.example.hector.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {
    int x1,y1;
    int x2,y2;
    int x3,y3;

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public Engine onCreateEngine(){
        return new MyWallpaperEngine();
    }

    class MyWallpaperEngine extends Engine {

        private boolean visible = true;
        public Bitmap image1;
        public Bitmap image2;
        public Bitmap image3;
        public Bitmap backgroundImage;

        MyWallpaperEngine() {
            // get the fish and background image references
            image1 = BitmapFactory.decodeResource(getResources(), R.drawable.fish);
            image2 = BitmapFactory.decodeResource(getResources(), R.drawable.fish);
            image3 = BitmapFactory.decodeResource(getResources(), R.drawable.fish);

            backgroundImage = BitmapFactory.decodeResource(getResources(),R.drawable.background);
            x1=-130; // initialize x position
            y1=200;  // initialize y position

            x2=50; // initialize x position
            y2=500;  // initialize y position

            x3=200; // initialize x position
            y3=870;  // initialize y position
        }

        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            // if screen wallpaper is visible then draw the image otherwise do not draw
            if (visible) {
                handler.post(drawRunner);
            }
            else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            draw();
        }

        void draw() {

            final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                // clear the canvas
                c.drawColor(Color.BLACK);
                if (c != null) {
                    // draw the background image
                    c.drawBitmap(backgroundImage, 0, 0, null);
                    // draw the fish
                    c.drawBitmap(image1, x1,y1, null);
                    c.drawBitmap(image2, x2,y2, null);
                    c.drawBitmap(image3, x3,y3, null);
                    // get the width of canvas
                    int width=c.getWidth();

                    // if x crosses the width means  x has reached to right edge
                    if(x1>width+100 ) {
                        // assign initial value to start with
                        x1=-130;
                    }
                    // change the x position/value by 1 pixel
                    x1=x1+1;


                    if(x3>width+100 ) {
                        // assign initial value to start with
                        x3=0;
                    }
                    // change the x position/value by 1 pixel
                    x3=x3+1;


                    if(x2>width+100 ) {
                        // assign initial value to start with
                        x2=200;
                    }
                    // change the x position/value by 1 pixel
                    x2=x2+1;
                }
            }
            finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }

            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 10); // delay 10
            }
        }
    }
}

