package sheldon.sanjiaodi.Fragment;

/**
 * Created by Sheldon on 2016/3/3.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import sheldon.sanjiaodi.MainActivity;
import sheldon.sanjiaodi.R;

public class MenuFragment extends Fragment implements OnClickListener {

    private MainActivity mainActivity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sliding_menu, null);
        mainActivity = (MainActivity) getActivity();
        view.findViewById(R.id.sliding_menu1).setOnClickListener(this);
        view.findViewById(R.id.sliding_menu2).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sliding_menu1:
                mainActivity.showTab();
                mainActivity.switchContent("firstFragment");
                break;
            case R.id.sliding_menu2:
                mainActivity.hideTab();
                mainActivity.switchContent("searchFragment");
                break;
            default:
                break;
        }
    }

}

