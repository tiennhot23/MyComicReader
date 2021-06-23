package com.e.mycomicreader.views;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.e.mycomicreader.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startSplashScreen();



    }

    private void startSplashScreen() {
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
    }
}