package cn.anline.www;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 3000);
    }
    class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(),HomeActivity.class));
            MainActivity.this.finish();
        }
    }
}
