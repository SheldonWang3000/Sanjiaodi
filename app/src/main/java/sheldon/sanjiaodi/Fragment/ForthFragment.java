package sheldon.sanjiaodi.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.LoginActivity;
import sheldon.sanjiaodi.MainActivity;
import sheldon.sanjiaodi.R;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class ForthFragment extends BaseFragment implements View.OnClickListener{

    private TextView userName;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_forth, null);
        ((TextView) view.findViewById(R.id.header_text)).setText("我的活动");
        userName = (TextView) view.findViewById(R.id.username_text);
        view.findViewById(R.id.menu_button).setOnClickListener((MainActivity) getActivity());
        view.findViewById(R.id.btn_password).setOnClickListener(this);
        view.findViewById(R.id.btn_activity).setOnClickListener(this);
        view.findViewById(R.id.btn_phone).setOnClickListener(this);
        view.findViewById(R.id.btn_myself).setOnClickListener(this);
        view.findViewById(R.id.btn_logout).setOnClickListener(this);
        return view;
    }

    @Override
    protected String getTitle() {
        return "Forth";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity:
                break;
            case R.id.btn_myself:
                break;
            case R.id.btn_password:
                break;
            case R.id.btn_phone:
                break;
            case R.id.btn_logout:
                SharedPreferences s = getActivity().getSharedPreferences("sjd", Context.MODE_PRIVATE);
                SharedPreferences.Editor e = s.edit();
                e.putString("token", "");
                e.commit();
                Intent i = new Intent();
                i.setClass(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
