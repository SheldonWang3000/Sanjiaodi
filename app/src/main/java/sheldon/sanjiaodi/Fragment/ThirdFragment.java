package sheldon.sanjiaodi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.Activity.InfoSampleActivity;
import sheldon.sanjiaodi.Activity.MetroActivity;
import sheldon.sanjiaodi.Activity.OfficeActivity;
import sheldon.sanjiaodi.Activity.ShoppingActivity;
import sheldon.sanjiaodi.Activity.MainActivity;
import sheldon.sanjiaodi.R;

/**
 * Created by Sheldon on 2016/3/3.
 */
public class ThirdFragment extends BaseFragment implements View.OnClickListener{

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_third, null);

        ((TextView)view.findViewById(R.id.header_text)).setText(R.string.third_title);

        view.findViewById(R.id.menu_button).setOnClickListener((MainActivity) getActivity());
        view.findViewById(R.id.info_shopping).setOnClickListener(this);
        view.findViewById(R.id.info_house).setOnClickListener(this);
        view.findViewById(R.id.info_sports).setOnClickListener(this);
        view.findViewById(R.id.info_education).setOnClickListener(this);
        view.findViewById(R.id.info_bank).setOnClickListener(this);
        view.findViewById(R.id.info_bus).setOnClickListener(this);
        view.findViewById(R.id.info_station).setOnClickListener(this);
        view.findViewById(R.id.info_metro).setOnClickListener(this);
        view.findViewById(R.id.info_metro_hk).setOnClickListener(this);
        view.findViewById(R.id.info_metro_sz).setOnClickListener(this);
        view.findViewById(R.id.info_pku).setOnClickListener(this);
        view.findViewById(R.id.info_education_center).setOnClickListener(this);
        view.findViewById(R.id.info_office).setOnClickListener(this);
        view.findViewById(R.id.info_hospital).setOnClickListener(this);
        view.findViewById(R.id.info_taxi).setOnClickListener(this);

        return view;
    }

    @Override
    public String getTitle() {
        return "Third";
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
//        btn_get.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                process.setVisibility(View.VISIBLE);
//                MyVolley.getInstance(getContext()).get(
//                        "http://192.168.1.102/get.php?id=123",
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                SJDLog.i("onResponse", response.toString());
//                                try {
//                                    JSONArray array = response.getJSONArray("array");
//                                    process.setVisibility(View.GONE);
//                                    btn_get.setText(array.getJSONObject(0).getString("name"));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                SJDLog.d("third get error", error);
//                                btn_get.setText("error volley");
//                            }
//                        });
//
//            }
//        });
//
//        btn_post.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", "908");
//                JSONObject jsonObject = new JSONObject(params);
//                MyVolley.getInstance(getContext()).post(
//                        "http://192.168.1.102/post.php",
//                        jsonObject,
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                SJDLog.i("onResponse", response.toString());
//                                try {
//                                    btn_post.setText(response.getString("idd"));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                SJDLog.d("third get error post", error.getMessage());
//                                btn_post.setText("error volley post");
//                            }
//                        });
//
//            }
//        });
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.info_shopping:
                i.setClass(getContext(), ShoppingActivity.class);
                break;
            case R.id.info_house:
                i.putExtra("title", "住宿服务");
                i.putExtra("sample", R.layout.info_house);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            case R.id.info_education:
                i.putExtra("title", "教学楼与院系");
                i.putExtra("sample", R.layout.info_education);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            case R.id.info_sports:
                i.putExtra("title", "运动设施");
                i.putExtra("sample", R.layout.info_sports);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            case R.id.info_bus:
                i.putExtra("title", "公交信息");
                i.putExtra("sample", R.layout.info_bus);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            case R.id.info_station:
                i.putExtra("title", "公交站");
                i.putExtra("sample", R.layout.info_station);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            case R.id.info_taxi:
                i.putExtra("title", "出租车");
                i.putExtra("sample", R.layout.info_taxi);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            case R.id.info_metro:
                i.setClass(getContext(), MetroActivity.class);
                break;
            case R.id.info_bank:
                i.putExtra("title", "银行服务");
                i.putExtra("sample", R.layout.info_bank);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            case R.id.info_hospital:
                i.putExtra("title", "医疗服务");
                i.putExtra("sample", R.layout.info_hopital);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            case R.id.info_office:
                i.setClass(getContext(), OfficeActivity.class);
                break;
            case R.id.info_pku:
                i.putExtra("title", "校园地图");
                i.putExtra("resource", R.mipmap.pku_map);
                i.putExtra("subtitle", "校园平面图");
                i.putExtra("sample", R.layout.info_image_sample);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            case R.id.info_education_center:
                i.putExtra("title", "深圳大学城总平面图");
                i.putExtra("resource", R.mipmap.education_center_map);
                i.putExtra("subtitle", "大学城平面图");
                i.putExtra("sample", R.layout.info_image_sample);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            case R.id.info_metro_sz:
                i.putExtra("title", "深圳地铁路线图");
                i.putExtra("resource", R.mipmap.metro_sz_map);
                i.putExtra("subtitle", "深圳地铁路线图");
                i.putExtra("sample", R.layout.info_image_sample);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            case R.id.info_metro_hk:
                i.putExtra("title", "香港地铁路线图");
                i.putExtra("resource", R.mipmap.metro_hk_map);
                i.putExtra("subtitle", "香港地铁路线图");
                i.putExtra("sample", R.layout.info_image_sample);
                i.setClass(getContext(), InfoSampleActivity.class);
                break;
            default:
                break;
        }
        startActivity(i);
    }
}
