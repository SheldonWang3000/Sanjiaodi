package sheldon.sanjiaodi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.HashMap;
import java.util.Map;

import sheldon.sanjiaodi.Fragment.FirstFragment;
import sheldon.sanjiaodi.Fragment.ForthFragment;
import sheldon.sanjiaodi.Fragment.MenuFragment;
import sheldon.sanjiaodi.Fragment.SearchFragment;
import sheldon.sanjiaodi.Fragment.SecondFragment;
import sheldon.sanjiaodi.Fragment.ThirdFragment;

public class MainActivity extends SlidingFragmentActivity {

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
        setContentView(R.layout.fragment_main_content);
        setBehindContentView(R.layout.fragment_sliding_menu);

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

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.sliding_menu, menuFragment, "menu").commit();
            content = firstFragment;
        }
        else {
            content = (Fragment)savedInstanceState.get("save");
        }
        switchContent(content);
        slidingMenu.setBehindWidth(600);
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

}
