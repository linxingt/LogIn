package com.example.login;

import android.graphics.Bitmap;
import android.os.Handler;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class GetCustomerPosition implements Runnable {
    ArrayList<LatLng> allPosition;
    private JSONParser parser;
    private final Handler myHandler;
    private final BaiduMap map;
    private BitmapDescriptor icon;
    private final BitmapDescriptor resizedIcon;
    private int numPosition;
    private Marker previousMaker;

    public GetCustomerPosition(BaiduMap m) {
        this.map = m;
        this.myHandler = new Handler();

        this.allPosition = new ArrayList<>(Arrays.asList(
                new LatLng(48.865285969008525, 2.3350311397520684),
                new LatLng(48.86619697265034, 2.3354606250061862),
                new LatLng(48.866491027247335, 2.3356183950995355),
                new LatLng(48.86703965200232, 2.3358749278058983),
                new LatLng(48.86771423391688, 2.3361904680011953),
                new LatLng(48.868671317449305, 2.3366024232508025),
                new LatLng(48.86892499916941, 2.3359538128670323),
                new LatLng(48.869609986218826, 2.335886163215369)
        ));

        this.icon = BitmapDescriptorFactory.fromResource(R.drawable.bleutotoro);
        this.resizedIcon = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(icon.getBitmap(), 100, 100, false));
        this.numPosition = 0;
    }

    public void start() {
        this.myHandler.post(this);
    }

    public void stop() {
        this.myHandler.removeCallbacks(this);
    }

    @Override
    public void run() {
        if (this.previousMaker != null) {
            this.previousMaker.remove();
        }
        if (this.numPosition == this.allPosition.size()) {
            this.numPosition = 0;
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .position(this.allPosition.get(this.numPosition))
                .icon(resizedIcon);
        this.previousMaker = (Marker) this.map.addOverlay(markerOptions);
        ++this.numPosition;

        this.myHandler.postDelayed(this, 2000);
    }
}