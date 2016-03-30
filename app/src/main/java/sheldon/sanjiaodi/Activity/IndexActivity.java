package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import sheldon.sanjiaodi.R;

/**
 * Created by Sheldon on 2016/3/21.
 */
public class IndexActivity extends Activity{

    final long SPLASH_DISPLAY_LENGTH = 500;
    static boolean login = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_index);

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("sjd", Context.MODE_PRIVATE);
                String uid = sharedPreferences.getString("uid", "");
                if (TextUtils.isEmpty(uid) || uid.equals("-1")) {
                    login = false;
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!login) {
                    Intent i = new Intent(IndexActivity.this, LoginActivity.class);
                    IndexActivity.this.startActivity(i);
                    IndexActivity.this.finish();
                } else {
                    Intent i = new Intent(IndexActivity.this, MainActivity.class);
                    IndexActivity.this.startActivity(i);
                    IndexActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
