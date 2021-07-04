package com.e.mycomicreader.views;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.e.mycomicreader.R;

public class SplashScreen extends AppCompatActivity {

    Animation animSplashScreen;
    ImageView imgSplashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        animSplashScreen = AnimationUtils.loadAnimation(this, R.anim.anim_splashscreen);
        imgSplashScreen = findViewById(R.id.img_splash_screen);
//        animSplashScreen.setDuration(2000);
//        imgSplashScreen.setAnimation(animSplashScreen);
        Glide.with(this).asGif()
                .load(R.drawable.splash)
                .placeholder(R.drawable.splash)
                .into(imgSplashScreen);

        Handler handler = new Handler();
        handler.postDelayed(() -> finish(), 3000);


    }
}