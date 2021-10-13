package com.example.androidproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.example.androidproject.ExitDialogFrag;
import com.example.androidproject.MultiSpinner;
import com.example.androidproject.R;
import com.example.androidproject.StorageData;
import com.example.androidproject.TableDialog;
import com.example.androidproject.data.Restaurant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity implements OnMapReadyCallback , MultiSpinner.multispinnerListener {
    int ok = R.drawable.fork_green;
    int notOk = R.drawable.fork_red;
    LatLng haifa = new LatLng(32.81970682272472, 34.99887516473304);
    Restaurant resPos[] = {new Restaurant("Pizza Lina","Ofek","05012345678","haifa",32.819586013022246, 34.99745082196883, true), new Restaurant("Kabareet","Ofek2","050123456782","haifa2",32.81895966739854, 34.996908342877035, true), new Restaurant("Falafel Michel","Ofek1","050123456781","haifa1",32.819909326661175, 34.99678333060842, false)};
    ArrayList<Marker> resMarkers = new ArrayList<Marker>();
    GoogleMap map;
    Restaurant selectedRestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        checkBoxSpinner();
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
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

            @Override
            public boolean onMarkerClick(Marker marker) {
                //TableDialog.errorOrderDialog(null,marker.getId()).show(getSupportFragmentManager(), "dialog");
                for (Restaurant pos : resPos) {
                    if (pos.getPos().equals(marker.getPosition()) && pos.getName().contentEquals(marker.getTitle()))
                        selectedRestaurant=pos;
                }

                return false;
            }
        });
        MarkerOptions marker;
        for (Restaurant pos : resPos) {
            marker = new MarkerOptions().position(pos.getPos()).title(pos.getName());
            marker.icon(BitmapDescriptorFactory.fromResource(pos.isAvailable() ? ok : notOk));
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
    public void goToRestaurant(View view){
        Intent intent = new Intent(this,RestMain.class);
        StorageData.saveSP(StorageData.SP_STRING_REST,selectedRestaurant,this);
        startActivity(intent);
    }
}