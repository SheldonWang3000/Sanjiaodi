package sheldon.sanjiaodi.InfoActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import sheldon.sanjiaodi.Fragment.MetroLineFragment;
import sheldon.sanjiaodi.Fragment.MetroStationFragment;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

public class MetroActivity extends FragmentActivity implements View.OnClickListener{

    private Fragment content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro);

        ((TextView)findViewById(R.id.header_text)).setText(R.string.info_metro_title);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.pku_red));
        }

        if (savedInstanceState == null) {
            switchContent(new MetroLineFragment());
        }
        else {
            switchContent((Fragment) savedInstanceState.get("save"));
        }

        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.info_metro_line).setOnClickListener(this);
        findViewById(R.id.info_metro_station).setOnClickListener(this);

    }

    public void switchContent(Fragment fragment) {
        if (content == null || !content.equals(fragment)) {
            content = fragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.info_metro_fragment, content).commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.info_metro_line:
                SJDLog.i("click", "metro_line");
                switchContent(new MetroLineFragment());
                break;
            case R.id.info_metro_station:
                SJDLog.i("click", "metro_station");
                switchContent(new MetroStationFragment());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "save", content);
    }
}
