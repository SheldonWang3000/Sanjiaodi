package sheldon.sanjiaodi.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.swipe.util.Attributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import sheldon.sanjiaodi.Activity.ContentActivity;
import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.Info;
import sheldon.sanjiaodi.ListItem.ItemAdapter;
import sheldon.sanjiaodi.ListItem.ItemData;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.SJDLog;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class FirstFragment extends BaseFragment {

    private ListView listView;
    private PtrClassicFrameLayout ptrFrameLayout;
    private ItemAdapter itemAdapter;
    private List<ItemData> itemList;
    private List<ItemData> moreItemList;
    private RelativeLayout process;
    private LoadMoreListViewContainer loadMoreListViewContainer;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_first, null);

        ((TextView) view.findViewById(R.id.header_text)).setText("三角地");

        process = (RelativeLayout) view.findViewById(R.id.loading);
        process.setVisibility(View.VISIBLE);
        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_frame);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setResistance(4.5f);
        listView = (ListView) view.findViewById(R.id.list_view);

        loadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.load_more_list_view_container);
        loadMoreListViewContainer.useDefaultHeader();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                closeAll();
                for (int i = 0; i < 3; ++i) {
                    if (moreItemList.isEmpty()) {
                        loadMoreContainer.loadMoreFinish(false, false);
                        break;
                    }
                    itemList.add(moreItemList.get(0));
                    moreItemList.remove(0);
                }
                SJDLog.i("size", itemList.size() + ":" + moreItemList.size());
                itemAdapter.notifyDataSetChanged();
                loadMoreListViewContainer.loadMoreFinish(!moreItemList.isEmpty(), !moreItemList.isEmpty());
            }
        });

        itemList = new ArrayList<>();
        moreItemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList, getContext());
        listView.setAdapter(itemAdapter);
        itemAdapter.setMode(Attributes.Mode.Single);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int openPosition = itemAdapter.getOpenItems().get(0);
                if (openPosition != -1) {
                    itemAdapter.closeItem(openPosition);
                } else {
                    SJDLog.i("onListClick", position);
                    Intent i = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("item", itemList.get(position));
                    i.putExtras(bundle);
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
                refreshList();
            }
        });
        return view;
    }

    @Override
    public String getTitle() {
        return "First";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        SharedPreferences s = getActivity().getSharedPreferences("sjd", Context.MODE_PRIVATE);
        String oldContent = s.getString("content", "");
        SJDLog.i("initData", oldContent);
        if (!TextUtils.isEmpty(oldContent)) {
            try {
                JSONArray array = new JSONArray(oldContent);
                getList(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        refreshList();
    }

    private void refreshList() {
        closeAll();
        loadMoreListViewContainer.loadMoreFinish(true, true);
        String uid = null;
        try {
            uid = Info.getUid(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(uid)) {
            //TODO 登录异常
        }
        MyVolley.getContent(getContext(), uid,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONArray array = (JSONArray) response;
                            SJDLog.i("refreshList", array.toString());
                            getList(array);
                            SharedPreferences s = getActivity().getSharedPreferences("sjd", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = s.edit();
                            editor.putString("content", response.toString());
                            editor.commit();
                            process.setVisibility(View.GONE);
                            ptrFrameLayout.refreshComplete();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SJDLog.d("getContentError", error);
                        Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                        process.setVisibility(View.GONE);
                        ptrFrameLayout.refreshComplete();
                    }
                });
    }
    private void closeAll() {
        int openPosition = itemAdapter.getOpenItems().get(0);
        if (openPosition != -1) {
            itemAdapter.closeItem(openPosition);
        }
    }

    private void getList(JSONArray array) throws JSONException{
        itemList.clear();
        moreItemList.clear();
        List<ItemData> list = itemList;
        for (int i = 0; i < array.length(); ++i) {
            JSONObject tmp = (JSONObject) array.get(i);
            ItemData tmpItem = new ItemData(tmp.getString("id"), tmp.getString("image_big"),
                    tmp.getString("image_big"), tmp.getString("title"), tmp.getString("sTime"),
                    tmp.getString("eTime"), tmp.getString("update_time"),
                    tmp.getString("deadline"), tmp.getString("is_sign"), tmp.getString("explain"),
                    tmp.getBoolean("collect")
            );
            list.add(tmpItem);
            if (i == 9) {
                list = moreItemList;
            }
        }
        itemAdapter.notifyDataSetChanged();

        SJDLog.i("size", itemList.size() + ":" + moreItemList.size());
    }

    @Override
    protected void setListener() {
    }



}
