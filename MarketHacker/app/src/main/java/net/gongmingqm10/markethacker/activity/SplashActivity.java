package net.gongmingqm10.markethacker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import net.gongmingqm10.markethacker.R;

import butterknife.Bind;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.splash_image)
    protected ImageView splashImage;

    private final static String IS_FIRST_INIT = "first_init";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        if (sharedPreferences.getBoolean(IS_FIRST_INIT, false)) {
            splashImage.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }
            }, 3000);
        } else {
            sharedPreferences.edit().putBoolean(IS_FIRST_INIT, false).apply();
            startMainActivity();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
