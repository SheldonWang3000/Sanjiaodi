package sheldon.sanjiaodi;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import sheldon.sanjiaodi.Fragment.FirstFragment;
import sheldon.sanjiaodi.Fragment.ForthFragment;
import sheldon.sanjiaodi.Fragment.MenuFragment;
import sheldon.sanjiaodi.Fragment.SearchFragment;
import sheldon.sanjiaodi.Fragment.SecondFragment;
import sheldon.sanjiaodi.Fragment.ThirdFragment;

public class MainActivity extends SlidingFragmentActivity implements View.OnClickListener {

    private SlidingMenu slidingMenu;

    private MenuFragment menuFragment;

    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private ForthFragment forthFragment;
    private SearchFragment searchFragment;

    private Fragment content;

    private RadioGroup radioGroup;

    private Map<String, Fragment> fragmentMap;


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

        radioGroup = (RadioGroup) findViewById(R.id.bottom_button_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bottom_button_0:
                        switchContent("firstFragment");
                        break;
                    case R.id.bottom_button_1:
                        switchContent("secondFragment");
                        break;
                    case R.id.bottom_button_2:
                        switchContent("thirdFragment");
                        break;
                    case R.id.bottom_button_3:
                        switchContent("forthFragment");
                        break;
                    default:
                        break;
                }
            }
        });
        fragmentMap = new HashMap<>();

        menuFragment = new MenuFragment();

        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();
        forthFragment = new ForthFragment();
        searchFragment = new SearchFragment();

        fragmentMap.put("firstFragment", firstFragment);
        fragmentMap.put("secondFragment", secondFragment);
        fragmentMap.put("thirdFragment", thirdFragment);
        fragmentMap.put("forthFragment", forthFragment);
        fragmentMap.put("searchFragment", searchFragment);

        getSupportFragmentManager().beginTransaction().replace(R.id.sliding_menu, menuFragment, "menu").commit();

        if (savedInstanceState == null) {
            switchContent("firstFragment");
        }
        else {
            switchContent((Fragment)savedInstanceState.get("save"));
        }
        slidingMenu.setBehindWidth(800);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setBehindScrollScale(0.0f);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setMode(SlidingMenu.LEFT);
    }

    public void switchContent(String fragmentName) {
        content = fragmentMap.get(fragmentName);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, content).commit();
        getSlidingMenu().showContent();
    }

    public void switchContent(Fragment fragment) {
        content = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, content).commit();
        getSlidingMenu().showContent();
    }

    public void showTab() {
        radioGroup.setVisibility(View.VISIBLE);
        radioGroup.check(R.id.bottom_button_0);
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
