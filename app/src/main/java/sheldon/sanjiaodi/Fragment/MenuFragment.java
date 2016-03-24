package sheldon.sanjiaodi.Fragment;

/**
 * Created by Sheldon on 2016/3/3.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import sheldon.sanjiaodi.Activity.MainActivity;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

public class MenuFragment extends Fragment implements OnClickListener {

    private MainActivity mainActivity;
    private Toast toast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sliding_menu, null);

        LinearLayout toastView = (LinearLayout) inflater.inflate(R.layout.toast, null);
        TextView textView = ((TextView) toastView.findViewById(R.id.toast_text));
        textView.setText("功能暂未开放，敬请期待");
        toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastView);

        mainActivity = (MainActivity) getActivity();
        view.findViewById(R.id.sliding_menu1).setOnClickListener(this);
        view.findViewById(R.id.sliding_menu2).setOnClickListener(this);
        view.findViewById(R.id.sliding_menu3).setOnClickListener(this);
        view.findViewById(R.id.sliding_menu4).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sliding_menu1:
                SJDLog.i("menu_click", "menu1");
                mainActivity.showTab();
                mainActivity.switchContent(new FirstFragment());
                break;
            case R.id.sliding_menu2:
                SJDLog.i("menu_click", "menu2");
                mainActivity.hideTab();
                mainActivity.switchContent(new SearchFragment());
                break;
            case R.id.sliding_menu3:
                SJDLog.i("menu_click", "menu3");
                toast.show();
                break;
            case R.id.sliding_menu4:
                SJDLog.i("menu_click", "menu4");
                toast.show();
                break;
            default:
                break;
        }
    }

}

