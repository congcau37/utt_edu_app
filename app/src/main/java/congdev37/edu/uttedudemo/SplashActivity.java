package congdev37.edu.uttedudemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import congdev37.edu.uttedudemo.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        new Timer().schedule(new TimerTask() {
                                 public void run() {
                                     SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                     SplashActivity.this.finish();
                                 }
                             }
                , 3000L);
        return;
    }
}
