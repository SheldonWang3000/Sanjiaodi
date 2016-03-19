package sheldon.sanjiaodi.InfoActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import sheldon.sanjiaodi.Fragment.ShoppingCashFragment;
import sheldon.sanjiaodi.Fragment.ShoppingEatFragment;
import sheldon.sanjiaodi.Fragment.ShoppingExpressFragment;
import sheldon.sanjiaodi.Fragment.ShoppingShopFragment;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

public class ShoppingActivity extends FragmentActivity implements View.OnClickListener{

    private Fragment content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        ((TextView)findViewById(R.id.header_text)).setText(R.string.info_shopping_title);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.pku_red));
        }

        if (savedInstanceState == null) {
            switchContent(new ShoppingEatFragment());
        }
        else {
            switchContent((Fragment)savedInstanceState.get("save"));
        }

        findViewById(R.id.info_shopping_eat).setOnClickListener(this);
        findViewById(R.id.info_shopping_shop).setOnClickListener(this);
        findViewById(R.id.info_shopping_express).setOnClickListener(this);
        findViewById(R.id.info_shopping_cash).setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);

    }

    public void switchContent(Fragment fragment) {
        if (content == null || !content.equals(fragment)) {
            content = fragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.info_shopping_fragment_content, content).commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.info_shopping_eat:
                SJDLog.i("click", "eat");
                switchContent(new ShoppingEatFragment());
                break;
            case R.id.info_shopping_shop:
                SJDLog.i("click", "shop");
                switchContent(new ShoppingShopFragment());
                break;
            case R.id.info_shopping_express:
                SJDLog.i("click", "express");
                switchContent(new ShoppingExpressFragment());
                break;
            case R.id.info_shopping_cash:
                SJDLog.i("click", "cash");
                switchContent(new ShoppingCashFragment());
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
