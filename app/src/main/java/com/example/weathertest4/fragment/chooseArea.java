package com.example.weathertest4.fragment;

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

import com.example.weathertest4.MainActivity;
import com.example.weathertest4.R;
import com.example.weathertest4.WeatherActicity;
import com.example.weathertest4.db.City;
import com.example.weathertest4.db.County;
import com.example.weathertest4.db.Province;
import com.example.weathertest4.gson.Weather;
import com.example.weathertest4.util.HttpSendUrl;
import com.example.weathertest4.util.Utility;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yf on 2017/3/29.
 */

public class chooseArea extends Fragment {
    public static int PROVINCELEVEL=0;
    public static int CITYLEVEL=1;
    public static int COUNTYLEVEL=2;
    private Button backButton;
    private TextView titleText;
    private ListView listView;
    private List<String>datelist=new ArrayList<>();
    private ArrayAdapter<String>adapter;
    private int currentLevel;
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_are,container);
        backButton= (Button) view.findViewById(R.id.back_button);
        titleText= (TextView) view.findViewById(R.id.title_text);
        listView= (ListView) view.findViewById(R.id.listview);
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,datelist);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLevel==COUNTYLEVEL){
                    queryCity();
                }else if(currentLevel==CITYLEVEL){
                    queryProvince();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(currentLevel==PROVINCELEVEL){
                    selectedProvince=provinceList.get(position);
                    queryCity();
                }else if(currentLevel==CITYLEVEL){
                    selectedCity=cityList.get(position);
                    queryCounty();
                }else  if(currentLevel==COUNTYLEVEL){
                    selectedCounty=countyList.get(position);
                    if(getActivity() instanceof MainActivity){
                    Intent intent=new Intent(getContext(), WeatherActicity.class);
                    intent.putExtra("weatherId",selectedCounty.getWeatherId());
                    startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity() instanceof  WeatherActicity){
                        WeatherActicity weatherActicity= (WeatherActicity) getActivity();
                        weatherActicity.requestWeather(selectedCounty.getWeatherId());
                        weatherActicity.swipLayout.setRefreshing(true);
                        weatherActicity.drawerLayout.closeDrawers();
                    }
                }

            }
        });
        queryProvince();

    }



    private void queryProvince() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList=DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            datelist.clear();
            for(Province p:provinceList){
                datelist.add(p.getProvinceName());

            }
            adapter.notifyDataSetChanged();
            currentLevel=PROVINCELEVEL;

        }else {
            String address="http://guolin.tech/api/china";
            getDateFromServer(address,"province");
        }

    }



    private void queryCity() {
        backButton.setVisibility(View.VISIBLE);
        titleText.setText(selectedProvince.getProvinceName());
        cityList=DataSupport.where("provinceCode=?",selectedProvince.getProvinceCode()).find(City.class);
        if(cityList.size()>0){
            datelist.clear();
            for(City city:cityList){
                datelist.add(city.getCityName());

            }
            adapter.notifyDataSetChanged();
            currentLevel=CITYLEVEL;

        }else {
            String address="http://guolin.tech/api/china/"+selectedProvince.getProvinceCode()+"/";
            getDateFromServer(address,"city");
        }


    }
    private void queryCounty() {
        backButton.setVisibility(View.VISIBLE);
        titleText.setText(selectedCity.getCityName());
        countyList=DataSupport.where("cityCode=?",selectedCity.getCityCode()).find(County.class);
        if(countyList.size()>0){
            datelist.clear();
            for(County county:countyList){
                datelist.add(county.getCountyName());

            }
            adapter.notifyDataSetChanged();
            currentLevel=COUNTYLEVEL;

        }else {
            String address="http://guolin.tech/api/china/"+selectedProvince.getProvinceCode()+"/"+selectedCity.getCityCode();
            getDateFromServer(address,"county");
        }
    }
    private void getDateFromServer(String address, final String type) {
        showProgressDialog();
        HttpSendUrl.sendOkhttp3(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"获取城市失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseText=response.body().string();
               ;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        boolean access=false;
                        if(type.equals("province")){
                            access= Utility.handProvince(responseText);
                        }else if(type.equals("city")){
                            access= Utility.handCity(responseText,selectedProvince.getProvinceCode());
                        }else if(type.equals("county")){
                            access= Utility.handCouty(responseText,selectedCity.getCityCode());
                        }
                        if(access){
                            if(type.equals("province")){
                                queryProvince();
                            }else if(type.equals("city")){
                                queryCity();
                            }else if(type.equals("county")){
                                queryCounty();
                            }
                        }
                    }
                });


            }
        });
    }
    public void showProgressDialog(){
        if(progressDialog==null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("加载中");
            progressDialog.setCanceledOnTouchOutside(false);

        }
        progressDialog.show();


    }
    public void closeProgressDialog(){

        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

}
