package com.example.androidproject;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity implements OnMapReadyCallback , MultiSpinner.multispinnerListener {
  // int ok = R.drawable.fork_green;
   int ok = 0; // @@ TODO: need to fix and add the image
   // int notOk = R.drawable.fork_red;
    int notOk = 1; // @@ TODO: need to fix and add the image
    LatLng haifa = new LatLng(32.81970682272472, 34.99887516473304);
    resDetails resPos[] = {getRes(32.819586013022246, 34.99745082196883, "Pizza Lina", ok), getRes(32.81895966739854, 34.996908342877035, "Kabareet", ok), getRes(32.819909326661175, 34.99678333060842, "Falafel Michel", notOk)};
    ArrayList<Marker> resMarkers = new ArrayList<Marker>();
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        checkBoxSpinner();
    }

    private resDetails getRes(double x, double y, String name, int drawable) {
        return new resDetails(x, y, name, drawable);
    }


    private class resDetails {
        LatLng pos;
        String name;
        int drawable;

        resDetails(double x, double y, String name, int drawable) {
            pos = new LatLng(x, y);
            this.name = name;
            this.drawable = drawable;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (map.getCameraPosition().zoom < 15) {
            for (Marker mark : resMarkers) {
                mark.setVisible(false);
            }
        } else {
            for (Marker mark : resMarkers) {
                mark.setVisible(true);
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(haifa, 15));
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        MarkerOptions marker;
        for (resDetails pos : resPos) {
            marker = new MarkerOptions().position(pos.pos).title(pos.name);
            marker.icon(BitmapDescriptorFactory.fromResource(pos.drawable));
            resMarkers.add(map.addMarker(marker));
        }


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(haifa, 15));
    }

    private void checkBoxSpinner() {
        MultiSpinner ms = (MultiSpinner) findViewById(R.id.multi_spinner);
        List<String> list = new ArrayList<String>();
        list.add("Kosher");
        list.add("Pizza");
        list.add("Free Parking");
        ms.setItems(list, "Filter by tags:", this);
    }

    @Override
    public void onItemschecked(boolean[] checked) {
            // TODO Auto-generated method stub
    }
}