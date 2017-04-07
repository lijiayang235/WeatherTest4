package com.example.weathertest4;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class AutoUploadService extends Service {
    LocationClient mLocalClient;

    public AutoUploadService() {
        mLocalClient = new LocationClient(this);
        mLocalClient.registerLocationListener(new AutoUploadService.MyLocationListener());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initLocation();
        mLocalClient.start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //每隔3秒调用一下
        option.setScanSpan(5000);
        //定位模式分三种，选取定位模式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //支持具体位置
        option.setIsNeedAddress(true);
        //使用百度坐标
        option.setCoorType("bd09ll");//注意是ll
        mLocalClient.setLocOption(option);

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//首次默认的为北京天安门
//            locationa=location;

            Log.d("myTest","onreceivelocationa");
//            Toast.makeText(AutoUploadService.this, "持续更新中", Toast.LENGTH_SHORT).show();
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度:").append(location.getLatitude()).append("\n");
            currentPosition.append("经度:").append(location.getLongitude()).append("\n");
            currentPosition.append("国家:").append(location.getCountry()).append("\n");
            currentPosition.append("省:").append(location.getProvince()).append("\n");
            currentPosition.append("市:").append(location.getCity()).append("\n");
            currentPosition.append("区:").append(location.getDistrict()).append("\n");
            currentPosition.append("街道:").append(location.getStreet()).append("\n");
            currentPosition.append("地址:").append(location.getAddrStr()).append("\n");
            currentPosition.append("时间:").append(location.getTime()).append("\n");
            currentPosition.append("速度:").append(location.getSpeed()).append("\n");


            currentPosition.append("定位方式:");
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网路");
            }
            Log.d("myTest",currentPosition.toString());

//            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                navigateTo(location);
//            }
//            Mylog.mylog("lbsmytest", "onReceiveLocation-end");
        }

    }
}