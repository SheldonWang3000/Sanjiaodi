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
import java.util.Date;

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
import sheldon.sanjiaodi.Info;
import sheldon.sanjiaodi.ListItem.ItemAdapter;
import sheldon.sanjiaodi.ListItem.ItemData;
import sheldon.sanjiaodi.MyVolley;
import sheldon.sanjiaodi.R;
import sheldon.sanjiaodi.RefreshInterface;
import sheldon.sanjiaodi.SJDLog;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class SecondFragment extends BaseFragment implements View.OnClickListener, RefreshInterface{

    private PtrClassicFrameLayout ptrFrameLayout;
    private ListView listView;
    private ItemAdapter itemAdapter;
    private ArrayList<ItemData> doingList;
    private ArrayList<ItemData> currentList;
    private ArrayList<ItemData> currentMoreList;
    private ArrayList<ItemData> doneList;
    private ArrayList<ItemData> doneMoreList;
    private ArrayList<ItemData> doingMoreList;
    private LoadMoreListViewContainer loadMoreListViewContainer;
    private RelativeLayout process;
    private String current = "doing";

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_second, null);
        ((TextView) view.findViewById(R.id.header_text)).setText("我的收藏");

        ptrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.star_ptr_frame);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setResistance(4.3f);

        listView = (ListView) view.findViewById(R.id.star_list_view);
        process = (RelativeLayout) view.findViewById(R.id.loading);
        process.setVisibility(View.VISIBLE);

        loadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.star_load_more);
        loadMoreListViewContainer.useDefaultHeader();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                closeAll();
                for (int i = 0; i < 3; ++i) {
                    if (currentMoreList.isEmpty()) {
                        loadMoreContainer.loadMoreFinish(false, false);
                        break;
                    }
                    currentList.add(currentMoreList.get(0));
                    currentMoreList.remove(0);
                }
                SJDLog.i("size", currentList.size() + ":" + currentMoreList.size());
                itemAdapter.notifyDataSetChanged();
                loadMoreListViewContainer.loadMoreFinish(!currentMoreList.isEmpty(),
                        !currentMoreList.isEmpty());
            }
        });

        doingList = new ArrayList<>();
        doneList = new ArrayList<>();
        doingMoreList = new ArrayList<>();
        doneMoreList = new ArrayList<>();
        currentList = new ArrayList<>();
        currentMoreList = new ArrayList<>();

        itemAdapter = new ItemAdapter(currentList, getContext(), this);
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
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("item", currentList.get(position));
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
                refresh();
            }
        });

        view.findViewById(R.id.menu_button).setOnClickListener((MainActivity) context);
        view.findViewById(R.id.star_doing_text).setOnClickListener(this);
        view.findViewById(R.id.star_done_text).setOnClickListener(this);

        return view;
    }

    private void closeAll() {
        int openPosition = itemAdapter.getOpenItems().get(0);
        if (openPosition != -1) {
            itemAdapter.closeItem(openPosition);
        }
    }

    @Override
    public String getTitle() {
        return "Second";
    }

    @Override
    public void refresh() {
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
        MyVolley.getCollection(getContext(), uid,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONArray array = (JSONArray) response;
                            SJDLog.i("refreshCollectionList", array.toString());
                            SharedPreferences s = context.getSharedPreferences("sjd", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = s.edit();
                            editor.putString("collection", response.toString());
                            editor.commit();
                            getList(array);
                            process.setVisibility(View.GONE);
                            ptrFrameLayout.refreshComplete();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SJDLog.d("getCollectionError", error);
                        Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                        process.setVisibility(View.GONE);
                        ptrFrameLayout.refreshComplete();
                    }
                });
    }
    private void getList(JSONArray array) throws JSONException {
        doingList.clear();
        doneList.clear();
        doingMoreList.clear();
        doneMoreList.clear();
        long now = new Date().getTime() / 1000;
        for (int i = 0; i < array.length(); ++i) {
            JSONObject tmp = (JSONObject) array.get(i);
            ItemData tmpItem = new ItemData(tmp.getString("id"), tmp.getString("image_big"),
                    tmp.getString("image_big"), tmp.getString("title"), tmp.getString("sTime"),
                    tmp.getString("eTime"), tmp.getString("update_time"),
                    tmp.getString("deadline"), tmp.getString("is_sign"), tmp.getString("explain"),
                    tmp.getBoolean("collect")
            );
            if (Long.valueOf(tmpItem.endTime) >= now) {
                //尚未结束
                if (doingList.size() < 10) {
                    doingList.add(tmpItem);
                }else {
                    doingMoreList.add(tmpItem);
                }
            }else {
                //已结束
                if (doneList.size() < 10) {
                    doneList.add(tmpItem);
                }else {
                    doneMoreList.add(tmpItem);
                }
            }
        }
        currentList.clear();
        currentMoreList.clear();
        if (current.equals("doing")) {
            currentList.addAll(doingList);
            currentMoreList.addAll(doingMoreList);

        }else {
            currentList.addAll(doneList);
            currentMoreList.addAll(doneMoreList);
        }
        itemAdapter.notifyDataSetChanged();

        SJDLog.i("doingSize", doingList.size() + ":" + doingMoreList.size());
        SJDLog.i("doneSize", doneList.size() + ":" + doneMoreList.size());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        SharedPreferences s = context.getSharedPreferences("sjd", Context.MODE_PRIVATE);
        String oldCollection = s.getString("collection", "");
        SJDLog.i("initCollectionData", oldCollection);
        if (!TextUtils.isEmpty(oldCollection)) {
            try {
                JSONArray array = new JSONArray(oldCollection);
                getList(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        refresh();
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
        current = "doing";
        loadMoreListViewContainer.loadMoreFinish(true, true);
        currentList.clear();
        currentMoreList.clear();
        currentList.addAll(doingList);
        currentMoreList.addAll(doingMoreList);

        itemAdapter.notifyDataSetChanged();
    }


    void checkToDone() {
        current = "done";
        loadMoreListViewContainer.loadMoreFinish(true, true);
        currentList.clear();
        currentMoreList.clear();
        currentList.addAll(doneList);
        currentMoreList.addAll(doneMoreList);

        itemAdapter.notifyDataSetChanged();
    }

}
