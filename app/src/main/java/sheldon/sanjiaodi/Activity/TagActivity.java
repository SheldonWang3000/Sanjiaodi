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
import android.widget.Toast;

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
import sheldon.sanjiaodi.ListItem.ItemAdapter;
import sheldon.sanjiaodi.R;

public class TagActivity extends Activity{

    private ListView listView;
    private PtrClassicFrameLayout ptrFrameLayout;
    private ItemAdapter itemAdapter;
    private List<String> stringList;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private RelativeLayout process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        int id = i.getIntExtra("id", -1);

        ((TextView) findViewById(R.id.header_text)).setText(title);
        ((TextView) findViewById(R.id.tag_title)).setText(title);

        process = (RelativeLayout) findViewById(R.id.loading);
        ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.tag_ptr_frame);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setResistance(2.8f);
        listView = (ListView) findViewById(R.id.tag_list_view);

        loadMoreListViewContainer =
                (LoadMoreListViewContainer) findViewById(R.id.tag_load_more);
        loadMoreListViewContainer.useDefaultHeader();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                loadMore();
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
                    i.setClass(TagActivity.this, ContentActivity.class);
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
                refresh();
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1800);
            }
        });

        initData(id);

    }

    private void initData(int id) {

        Toast toast = Toast.makeText(this, "" + id, Toast.LENGTH_SHORT);
        toast.show();

        for (int i = 10; i < 19; ++i)
        {
            stringList.add(String.valueOf(i));
        }

        itemAdapter.notifyDataSetChanged();
    }

    private void refresh() {
    }

    private void loadMore() {
        new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        stringList.add("100");
                        itemAdapter.notifyDataSetChanged();
                        loadMoreListViewContainer.loadMoreFinish(true, true);
                    }
                }, 1000);
    }


}
