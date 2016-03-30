package sheldon.sanjiaodi.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.lang.reflect.Method;

import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.Fragment.FirstFragment;
import sheldon.sanjiaodi.Fragment.ForthFragment;
import sheldon.sanjiaodi.Fragment.MenuFragment;
import sheldon.sanjiaodi.Fragment.SecondFragment;
import sheldon.sanjiaodi.Fragment.ThirdFragment;
import sheldon.sanjiaodi.Info;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

public class MainActivity extends SlidingFragmentActivity implements View.OnClickListener {

    private SlidingMenu slidingMenu;

    private MenuFragment menuFragment;

    private BaseFragment content;

    private RadioGroup radioGroup;

    private int clickedButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        slidingMenu = getSlidingMenu();
        String str = "" + checkDeviceHasNavigationBar(this);
        SJDLog.d("navigationbar", str);

        setContentView(R.layout.fragment_main_content);
        setBehindContentView(R.layout.fragment_sliding_menu);

        final LinearLayout view = (LinearLayout) findViewById(R.id.navigation_bottom);
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (checkDeviceHasNavigationBar(getApplicationContext())) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    float height = getNavigationBarHeight(getApplicationContext());
                    params.height = (int)(height);
                    view.setLayoutParams(params);
                }

                return true;
            }
        });

        clickedButton = R.id.bottom_button_0;
        radioGroup = (RadioGroup) findViewById(R.id.bottom_button_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String uid = null;
                if (checkedId == clickedButton) {
                    return;
                }
                switch (checkedId) {
                    case R.id.bottom_button_0:
                        switchContent(new FirstFragment());
                        clickedButton = R.id.bottom_button_0;
                        break;
                    case R.id.bottom_button_1:
                        try {
                            uid = Info.getUid(MainActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (uid == null || uid.equals("-1")) {
                            askForLogin();
                        } else {
                            switchContent(new SecondFragment());
                            clickedButton = R.id.bottom_button_1;
                        }
                        break;
                    case R.id.bottom_button_2:
                        switchContent(new ThirdFragment());
                        clickedButton = R.id.bottom_button_2;
                        break;
                    case R.id.bottom_button_3:
                        try {
                            uid = Info.getUid(MainActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (uid == null || uid.equals("-1")) {
                            askForLogin();
                        } else {
                            switchContent(new ForthFragment());
                            clickedButton = R.id.bottom_button_3;
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        menuFragment = new MenuFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.sliding_menu, menuFragment, "menu").commit();

        if (savedInstanceState == null) {
            switchContent(new FirstFragment());
        }
        else {
            switchContent((Fragment)savedInstanceState.get("save"));
        }
        slidingMenu.setBehindWidth(750);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setBehindScrollScale(0.0f);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setMode(SlidingMenu.LEFT);
    }

    private void askForLogin() {
        SJDLog.w("MainActivity", "not login");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setTitle("您还没有登录")
                .setMessage("请先登录")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent();
                        i.setClass(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((RadioButton)radioGroup.findViewById(clickedButton)).setChecked(true);
                    }
                });
        builder.create().show();
    }

    public void switchContent(Fragment fragment) {
        content = (BaseFragment) fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, content).commit();
        getSlidingMenu().showContent();
    }

    public void showTab() {
        radioGroup.setVisibility(View.VISIBLE);
        radioGroup.check(R.id.bottom_button_0);
    }

    @Override
    public void onBackPressed() {
        if (!content.getTitle().equals("First")) {
            switchContent(new FirstFragment());
            showTab();
        }
        else {
            super.onBackPressed();
        }
    }

    public void hideTab() {
        radioGroup.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "save", content);
    }

    private boolean checkDeviceHasNavigationBar(Context context) {

        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasNavigationBar;
    }

    private int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_button:
                SJDLog.i("click", "menu");
                slidingMenu.toggle();
                break;
        }
    }
}
