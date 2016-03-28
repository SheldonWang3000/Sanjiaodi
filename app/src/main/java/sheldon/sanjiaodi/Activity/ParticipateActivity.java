package sheldon.sanjiaodi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import sheldon.sanjiaodi.ListItem.ItemAdapter;
import sheldon.sanjiaodi.ListItem.ItemData;
import sheldon.sanjiaodi.R;

public class ParticipateActivity extends Activity implements View.OnClickListener{

    private PtrClassicFrameLayout ptrFrameLayout;
    private ListView listView;
    private ItemAdapter itemAdapter;
    private ArrayList<ItemData> stringList;
    private RelativeLayout process;
    private LoadMoreListViewContainer loadMoreListViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate);
        ((TextView) findViewById(R.id.header_text)).setText("参与的活动");

        process = (RelativeLayout) findViewById(R.id.loading);
        ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.part_ptr_frame);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setResistance(2.8f);

        listView = (ListView) findViewById(R.id.part_list_view);

        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.part_load_more);
        loadMoreListViewContainer.useDefaultHeader();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        stringList.add("100");
                        loadMoreListViewContainer.loadMoreFinish(true, true);
                        itemAdapter.notifyDataSetChanged();
                    }
                }, 1000);

            }
        });

        stringList = new ArrayList<>();
        itemAdapter = new ItemAdapter(stringList, this);
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
                    i.setClass(ParticipateActivity.this, ContentActivity.class);
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

        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.part_doing_text).setOnClickListener(this);
        findViewById(R.id.part_done_text).setOnClickListener(this);

        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; ++i)
        {
//            stringList.add(String.valueOf(i));
        }

        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.part_doing_text:
                checkToDoing();
                break;
            case R.id.part_done_text:
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
//            stringList.add(String.valueOf(i));
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
//                        stringList.add("100");
                        loadMoreListViewContainer.loadMoreFinish(true, true);
                        itemAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        stringList.clear();
        for (int i = 0; i < 3; ++i)
        {
//            stringList.add(String.valueOf(i));
        }

        itemAdapter.notifyDataSetChanged();
    }
}
