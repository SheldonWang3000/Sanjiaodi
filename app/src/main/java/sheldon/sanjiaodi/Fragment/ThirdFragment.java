package sheldon.sanjiaodi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.R;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class ThirdFragment extends BaseFragment {


    @Override
    protected View initView(LayoutInflater inflater) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_third, null);
    }

    @Override
    protected String getTitle() {
        return "Third";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub

    }

}
