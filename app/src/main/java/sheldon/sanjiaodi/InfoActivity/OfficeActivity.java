package sheldon.sanjiaodi.InfoActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import sheldon.sanjiaodi.Fragment.OfficeFragment;
import sheldon.sanjiaodi.Fragment.OfficePhoneFragment;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

public class OfficeActivity extends FragmentActivity implements View.OnClickListener{

    private Fragment content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);

        ((TextView)findViewById(R.id.header_text)).setText(R.string.info_office_title);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.pku_red));
        }

        if (savedInstanceState == null) {
            switchContent(new OfficeFragment());
        }
        else {
            switchContent((Fragment) savedInstanceState.get("save"));
        }

        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.info_office).setOnClickListener(this);
        findViewById(R.id.info_office_phone).setOnClickListener(this);

    }

    public void switchContent(Fragment fragment) {
        if (content == null || !content.equals(fragment)) {
            content = fragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.info_office_fragment_content, content).commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.info_office:
                SJDLog.i("click", "office");
                switchContent(new OfficeFragment());
                break;
            case R.id.info_office_phone:
                SJDLog.i("click", "office_phone");
                switchContent(new OfficePhoneFragment());
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
