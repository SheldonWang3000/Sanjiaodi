package sheldon.sanjiaodi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import sheldon.sanjiaodi.Activity.ContentActivity;
import sheldon.sanjiaodi.Activity.MainActivity;
import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.ListItem.ItemAdapter;
import sheldon.sanjiaodi.R;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class SecondFragment extends BaseFragment implements View.OnClickListener{

    private PtrClassicFrameLayout ptrFrameLayout;
    private ListView listView;
    private ItemAdapter itemAdapter;
    private ArrayList<String> stringList;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private RelativeLayout process;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_second, null);
        ((TextView) view.findViewById(R.id.header_text)).setText("我的收藏");

        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.star_ptr_frame);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setResistance(2.8f);

        listView = (ListView) view.findViewById(R.id.star_list_view);
        process = (RelativeLayout) view.findViewById(R.id.loading);

        loadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.star_load_more);
        loadMoreListViewContainer.useDefaultHeader();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stringList.add("100");
                        loadMoreListViewContainer.loadMoreFinish(true, true);
                        itemAdapter.notifyDataSetChanged();
                    }
                }, 1000);

            }
        });

        stringList = new ArrayList<>();
        itemAdapter = new ItemAdapter(stringList, getContext());
        listView.setAdapter(itemAdapter);
        itemAdapter.setMode(Attributes.Mode.Single);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int openPosition = itemAdapter.getOpenItems().get(0);
                if (openPosition != -1) {
                    itemAdapter.closeItem(openPosition);
                } else {
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

        view.findViewById(R.id.menu_button).setOnClickListener((MainActivity) getActivity());
        view.findViewById(R.id.star_doing_text).setOnClickListener(this);
        view.findViewById(R.id.star_done_text).setOnClickListener(this);

        return view;
    }

    @Override
    public String getTitle() {
        return "Second";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        for (int i = 0; i < 20; ++i)
        {
            stringList.add(String.valueOf(i));
        }

        itemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setListener() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.star_doing_text:
                checkToDoing();
                break;
            case R.id.star_done_text:
                checkToDone();
                break;
            default:
                break;
        }
    }
    void checkToDoing() {
        loadMoreListViewContainer.loadMoreFinish(true, true);
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreListViewContainer.loadMoreFinish(false, false);
                        itemAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
        stringList.clear();
        for (int i = 0; i < 15; ++i)
        {
            stringList.add(String.valueOf(i));
        }

        itemAdapter.notifyDataSetChanged();
    }


    void checkToDone() {
        loadMoreListViewContainer.loadMoreFinish(true, true);
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stringList.add("100");
                        loadMoreListViewContainer.loadMoreFinish(true, true);
                        itemAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        stringList.clear();
        for (int i = 0; i < 3; ++i)
        {
            stringList.add(String.valueOf(i));
        }

        itemAdapter.notifyDataSetChanged();
    }
}
