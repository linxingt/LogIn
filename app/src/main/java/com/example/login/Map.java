package com.example.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class Map extends AppCompatActivity {
    private Button but_Loc, mapback;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;
    private GetCustomerPosition getCustomerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //检查权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        } else {
            initSDK(true);
            setContentView(R.layout.activity_map);

            getSupportActionBar().hide();// title bar
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));//color status bar

            try {
                initmap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "have not droit d ACCESS_FINE_LOCATION!", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case 2:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "have not droit d ACCESS_COARSE_LOCATION!", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case 3:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "have not droit de WRITE_EXTERNAL_STORAGE!", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
        initSDK(true);
        setContentView(R.layout.activity_map);

        getSupportActionBar().hide();// title bar
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));//color status bar

        try {
            initmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSDK(boolean status) {
        LocationClient.setAgreePrivacy(status);
        SDKInitializer.setAgreePrivacy(getApplicationContext(), status);
        try {
            //在使用SDK各组件之前初始化context信息，传入ApplicationContext
            SDKInitializer.initialize(getApplicationContext());
            //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
            //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
            SDKInitializer.setCoordType(CoordType.BD09LL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initmap() throws Exception {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        LatLng cenpt = new LatLng(48.865545884093244, 2.34702785023729);

        this.getCustomerPosition = new GetCustomerPosition(this.mBaiduMap);
        this.getCustomerPosition.start();

        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(13.5f)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        mBaiduMap.setMyLocationEnabled(true);

        mMapView.showScaleControl(false);
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);

        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        mLocationClient.start();
        this.mapback = findViewById(R.id.mapback);
        this.mapback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ShowContact.class);
                intent.putExtra("unom", getIntent().getExtras().getString("unom"));
                intent.putExtra("unum", getIntent().getExtras().getString("unum"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mMapView.onDestroy();
        this.getCustomerPosition.stop();
    }

    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null) {
                return;
            }
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                    true, BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bleutotoro), 150, 160, true)));
            mBaiduMap.setMyLocationConfiguration(configuration);
            mBaiduMap.setMyLocationData(locData);

            but_Loc = findViewById(R.id.but_Loc);
            but_Loc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
                    mBaiduMap.animateMapStatus(update);
                }
            });
            BaiduMap.OnMapClickListener listener = new BaiduMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    LatLng start = new LatLng(location.getLatitude(), location.getLongitude());
                    LatLng end = new LatLng(point.latitude, point.longitude);
                    double double_distance = getDistance(start, end);
                    Toast.makeText(getApplicationContext(), "The distance between you and where you clicked is : " + Math.round(double_distance) + "km", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onMapPoiClick(MapPoi mapPoi) {

                }
            };
            mBaiduMap.setOnMapClickListener(listener);

        }
    }

    public double getDistance(LatLng start, LatLng end) {

        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;
        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;

        double R = 6371;

        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1))
                * R;

        return d;
    }
}
