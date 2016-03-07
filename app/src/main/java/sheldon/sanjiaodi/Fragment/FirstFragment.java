package sheldon.sanjiaodi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.DataAdapter;
import sheldon.sanjiaodi.R;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class FirstFragment extends BaseFragment {

    private ListView listView;
    private PtrClassicFrameLayout ptrFrameLayout;
    private DataAdapter dataAdapter;
    private List<String> stringList;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_first, null);

        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_frame);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        listView = (ListView) view.findViewById(R.id.list_view);

        final LoadMoreListViewContainer loadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.load_more_list_view_container);
        loadMoreListViewContainer.useDefaultHeader();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stringList.add("100");
                        dataAdapter.notifyDataSetChanged();
                        loadMoreListViewContainer.loadMoreFinish(true, true);
                    }
                }, 1000);

            }
        });

        stringList = new ArrayList<>();
        dataAdapter = new DataAdapter(stringList, getContext());
        listView.setAdapter(dataAdapter);
        dataAdapter.setMode(Attributes.Mode.Single);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int openPosition = dataAdapter.getOpenItems().get(0);
                if (openPosition != -1) {
                    dataAdapter.closeItem(openPosition);
                }else {
                    Intent i = new Intent();
                    i.setClass(getContext(), ContentActivity.class);
                    startActivity(i);
                }

            }
        });
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, listView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1800);
            }
        });
        return view;
    }

    @Override
    protected String getTitle() {
        return "First";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        for (int i = 0; i < 20; ++i)
        {
            stringList.add(String.valueOf(i));
        }

        dataAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub

    }



}
