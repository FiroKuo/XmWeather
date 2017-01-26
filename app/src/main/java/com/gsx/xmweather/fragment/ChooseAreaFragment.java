package com.gsx.xmweather.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsx.xmweather.MainActivity;
import com.gsx.xmweather.R;
import com.gsx.xmweather.WeatherActivity;
import com.gsx.xmweather.db.City;
import com.gsx.xmweather.db.County;
import com.gsx.xmweather.db.Province;
import com.gsx.xmweather.util.HttpUtil;
import com.gsx.xmweather.util.LogUtil;
import com.gsx.xmweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;



public class ChooseAreaFragment extends Fragment {


    private static final String TAG="ChooseareaFragment";
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province currentProvince;
    private City currentCity;
    private County currentCounty;
    private Button bt_back;
    private TextView tv_title;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    private List<String> data=new ArrayList<>();
    private int currentLevel;

    private ProgressDialog mProgressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_choose, container, false);
        bt_back= (Button) view.findViewById(R.id.bt_back);
        tv_title= (TextView) view.findViewById(R.id.tv_title);
        mListView= (ListView) view.findViewById(R.id.listView);
        mAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,data);
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel==LEVEL_PROVINCE){
                    currentProvince=provinceList.get(position);
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    currentCity=cityList.get(position);
                    queryCounties();
                }else if (currentLevel==LEVEL_COUNTY){

                    currentCounty=countyList.get(position);
                    String weatherId=currentCounty.getWeatherId();

                    if (getActivity() instanceof MainActivity){

                        Intent intent =new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity){

                        WeatherActivity activity= (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.refreshLayout.setRefreshing(true);
                        activity.requestServer(weatherId);
                    }
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel==LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryCounties() {

        tv_title.setText(currentCity.getCityName());
        bt_back.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid=?", String.valueOf(currentCity.getId())).find(County.class);
        if(countyList.size()!=0){
            data.clear();
            for (County county:countyList) {
                data.add(county.getCountyName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }else{

            String adress="http://guolin.tech/api/china"+"/"+currentProvince.getProvinceCode()+
                    "/"+currentCity.getCityCode();
            LogUtil.e(TAG,"county:"+adress);
            queryServer(adress,"county");
        }
    }

    private void queryCities() {

        tv_title.setText(currentProvince.getProvinceName());
        bt_back.setVisibility(View.VISIBLE);
        cityList= DataSupport.where("provinceid=?", String.valueOf(currentProvince.getId())).find(City.class);
        if (cityList.size()>0){
            data.clear();
            for (City city:cityList) {
                data.add(city.getCityName());
            }

            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else {
            String adress = "http://guolin.tech/api/china" + "/" + currentProvince.getProvinceCode();
            LogUtil.e(TAG,"city:"+adress);
            queryServer(adress, "city");
        }
    }

    /**
     * 这个方法用来查询所有的省份
     *
     */
    private void queryProvinces() {

        tv_title.setText("中国");
        bt_back.setVisibility(View.GONE);
        provinceList=DataSupport.findAll(Province.class);
        if (provinceList.size()>0){
            data.clear();
            for (Province province:provinceList) {
                data.add(province.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else{
            String adress="http://guolin.tech/api/china";
            LogUtil.e(TAG,"province:"+adress);
            queryServer(adress,"province");
        }
    }

    /**
     * 这个方法用来联网查找省份/市/县
     * @param adress 查询地址
     * @param type 查询类型
     */
    private void queryServer(String adress,final String type) {

        showProgressDialog();
        HttpUtil.sendRequest(adress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"获取数据失败!",Toast.LENGTH_SHORT).show();
                        dismissDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                boolean flag=false;
                String resStr = response.body().string();
                if ("province".equals(type)){
                    flag=Utility.handleProvinceResponse(resStr);
                }else if ("city".equals(type)){
                    flag=Utility.handleCityResponse(resStr,currentProvince.getId());
                }else if ("county".equals(type)){
                    flag=Utility.handleCountyResponse(resStr,currentCity.getId());
                }

                if (flag){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dismissDialog();

                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });

                }
            }
        });

    }


    private void showProgressDialog() {

        if (mProgressDialog==null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("loading...");
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    private void dismissDialog(){

        if (mProgressDialog!=null){
            mProgressDialog.dismiss();
            mProgressDialog=null;
        }
    }
}
