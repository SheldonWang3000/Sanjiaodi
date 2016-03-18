package sheldon.sanjiaodi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import sheldon.sanjiaodi.BaseFragment;
import sheldon.sanjiaodi.InfoActivity.ShoppingActivity;
import sheldon.sanjiaodi.MainActivity;
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
        view.findViewById(R.id.info_phone).setOnClickListener(this);
        view.findViewById(R.id.info_hospital).setOnClickListener(this);
        view.findViewById(R.id.info_taxi).setOnClickListener(this);

        return view;
    }

    @Override
    protected String getTitle() {
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
    protected void setListener() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_shopping:
                Intent i = new Intent();
                i.setClass(getContext(), ShoppingActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
