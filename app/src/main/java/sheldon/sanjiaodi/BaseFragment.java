package sheldon.sanjiaodi;

/**
 * Created by Sheldon on 2016/3/3.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    protected Context context;
    public View rootView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = initView(inflater);
        setListener();
        return rootView;
    }

    public View getRootView() {
        return rootView;
    }

    /**
     * 初始化UI
     *
     * @param inflater
     * @return
     */
    protected abstract View initView(LayoutInflater inflater);

    public abstract String getTitle();

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 设置监听
     */
    protected abstract void setListener();

}
