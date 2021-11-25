package com.example.androidproject.controller;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidproject.FireBaseListener;
import com.example.androidproject.data.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireBaseConnection {
    FirebaseFirestore db;
    FireBaseListener listener;
    public static FireBaseConnection instance=new FireBaseConnection(null);
    private FireBaseConnection(FireBaseListener listener){
        db = FirebaseFirestore.getInstance();
        this.listener=listener;
        instance=this;
    }


    public void changeListener(FireBaseListener listener){
        this.listener=listener;
    }
    public  void connectData(String id){
        final CollectionReference docRef = db.collection("Restaurants");
        docRef.document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    listener.onFailed();
                    return;
                }
                if (snapshot != null && snapshot.exists()){
                    listener.onDataChanged(snapshot.getData());
                }
            }
        });
    }
    public void connectCollectionInDocument(String id,String collection,String client_id,HashMap data){
        final CollectionReference docRef = db.collection("Restaurants");
        docRef.document(id).collection(collection).document(client_id).set(data);
        docRef.document(id).collection(collection).document(client_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    listener.onFailed();
                    return;
                }
                if (snapshot != null && snapshot.exists()){
                    listener.onDataChanged(snapshot.getData());
                }
            }
        });
    }
    public void connectData(){
        ArrayList<Restaurant> temp=new ArrayList<Restaurant>();
        //Restaurants
        final CollectionReference docRef = db.collection("Restaurants");
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>()  {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    listener.onFailed();
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    QueryDocumentSnapshot doc = null;
                    switch (dc.getType()) {
                        case ADDED:
                            doc=dc.getDocument();
                            listener.onDataAdded(doc.getData());
                            doc.getData().put("id",doc.getId());
                           // Log.d(TAG, "New city: " + dc.getDocument().getData());
                            //((HashMap)doc.get("position")).get("latitude"),((HashMap)doc.get("position")).get("longitude"),
                            break;
                        case MODIFIED:
                           doc = dc.getDocument();
                           doc.getData().put("id",doc.getId());
                            listener.onDataChanged(doc.getData());
                           // Log.d(TAG, "Modified city: " + dc.getDocument().getData());

                            break;
                        case REMOVED:
                            doc = dc.getDocument();
                            doc.getData().put("id",doc.getId());
                            listener.onDataRemoved(doc.getData());
                           // Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                            break;

                    }
                }
            }
        });

    }

}
