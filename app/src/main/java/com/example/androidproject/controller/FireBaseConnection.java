package com.example.androidproject.controller;

import androidx.annotation.Nullable;

import com.example.androidproject.FireBaseListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class FireBaseConnection {
    FirebaseFirestore db;
    FireBaseListener listener;
    private FireBaseConnection(FireBaseListener listener){
        db = FirebaseFirestore.getInstance();
        this.listener=listener;
    }
    public void changeListener( FireBaseListener listener){
        this.listener=listener;
    }
    public void getData(){
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
                    switch (dc.getType()) {
                        case ADDED:
                            listener.onDataAdded();
                           // Log.d(TAG, "New city: " + dc.getDocument().getData());
                            break;
                        case MODIFIED:
                            listener.onDataChanged();
                           // Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            listener.onDataRemoved();
                           // Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                            break;
                    }
                }
            }
        });

    }

}
