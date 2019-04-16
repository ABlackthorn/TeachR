package com.teachr.teachr.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.teachr.teachr.models.Entry;
import com.teachr.teachr.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryDaoFirestore implements EntryDao {

    private static EntryDaoFirestore single_instance = null;

    private final String collectionName = "entries";

    private FirebaseFirestore db;
    private Entry entry;
    private List<Entry> entries;


    private EntryDaoFirestore(){
        db = FirebaseFirestore.getInstance();
    }

    public static EntryDaoFirestore getInstance(){
        if (single_instance == null)
            single_instance = new EntryDaoFirestore();

        return single_instance;
    }


    @Override
    public Entry getEntry(String id) {

        entry = new Entry();

        db.collection(collectionName).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {

                        entry.setAddress(doc.getString("address"));
                        entry.setDate(doc.getDate("date").toString());
                        entry.setDuration(doc.getLong("duration"));
                        entry.setLatitude(doc.getLong("latitude"));
                        entry.setLongitude(doc.getLong("longitude"));
                        entry.setPrice(doc.getLong("price"));
                        entry.setSubject(doc.getString("subject"));
                        entry.setType(doc.getLong("type"));
                        entry.setUser(doc.getString("user"));
                        entry.setId(doc.getId());

                    } else {
                        entry.setAddress("NOT FOUND");
                        entry.setDate("NOT FOUND");
                        entry.setDuration(0);
                        entry.setLatitude(0);
                        entry.setLongitude(0);
                        entry.setPrice(0);
                        entry.setSubject("NOT FOUND");
                        entry.setType(0);
                        entry.setUser("NOT FOUND");
                        entry.setId("NOT FOUND");
                    }
                }
            }
        });

        return (Entry)entry.clone();
    }

    @Override
    public Entry findEntry(String key, String value) {
        CollectionReference entryCollection = db.collection(collectionName);
        Query query = entryCollection.whereEqualTo(key, value);
        entry = new Entry();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot doc = task.getResult();
                    if(doc.getDocuments() != null){
                        if(doc.getDocuments().size() > 0) {
                            DocumentSnapshot entrySnapshot = doc.getDocuments().get(0);
                            entry.setAddress(entrySnapshot.getString("address"));
                            entry.setDate(entrySnapshot.getDate("date").toString());
                            entry.setDuration(entrySnapshot.getLong("duration"));
                            entry.setLatitude(entrySnapshot.getLong("latitude"));
                            entry.setLongitude(entrySnapshot.getLong("longitude"));
                            entry.setPrice(entrySnapshot.getLong("price"));
                            entry.setSubject(entrySnapshot.getString("subject"));
                            entry.setType(entrySnapshot.getLong("type"));
                            entry.setUser(entrySnapshot.getString("user"));
                            entry.setId(entrySnapshot.getId());
                        }
                    }
                }
            }
        });
        return (Entry)entry.clone();
    }

    public List<Entry> getAllEntries(){
        entries = new ArrayList<>();
        CollectionReference entryCollection = db.collection(collectionName);
        entryCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot doc = task.getResult();
                    if(doc.getDocuments() != null){
                        if(doc.getDocuments().size() > 0) {
                            for(DocumentSnapshot entrySnapshot : doc.getDocuments()){
                                Entry tmpEntry= new Entry();
                                tmpEntry.setId(entrySnapshot.getId());
                                tmpEntry.setAddress(entrySnapshot.getString("address"));
                                tmpEntry.setDate(entrySnapshot.getDate("date").toString());
                                tmpEntry.setDuration(entrySnapshot.getLong("duration"));
                                tmpEntry.setLatitude(entrySnapshot.getLong("latitude"));
                                tmpEntry.setLongitude(entrySnapshot.getLong("longitude"));
                                tmpEntry.setPrice(entrySnapshot.getLong("price"));
                                tmpEntry.setSubject(entrySnapshot.getString("subject"));
                                tmpEntry.setType(entrySnapshot.getLong("type"));
                                tmpEntry.setUser(entrySnapshot.getString("user"));
                                Log.i("GETALLENTRIES", "Adding user : " + tmpEntry.getSubject());
                                EntryDaoFirestore.this.entries.add(tmpEntry);
                            }
                        }
                    }
                }
            }
        });
        Log.i("GETALLENTRIES", "getAllEntries : " + entries.size());
        return new ArrayList<>(entries);
    }

    @Override
    public List<Entry> findEntries(String key, String value, int comparator) {
        CollectionReference entryCollection = db.collection(collectionName);
        Query query;
        switch (comparator){
            case Comparators.COMPARATOR_EQUALS:
                query = entryCollection.whereEqualTo(key, value);
                break;
            case Comparators.COMPARATOR_LIKE:
                query = entryCollection.whereArrayContains(key, value);
                break;
            case Comparators.COMPARATOR_GREATER:
                query = entryCollection.whereGreaterThan(key, value);
                break;
            case Comparators.COMPARATOR_LOWER:
                query = entryCollection.whereLessThan(key, value);
                break;
            case Comparators.COMPARATOR_GREATER_EQUALS:
                query = entryCollection.whereGreaterThanOrEqualTo(key, value);
                break;
            case Comparators.COMPARATOR_LOWER_EQUALS:
                query = entryCollection.whereLessThanOrEqualTo(key, value);
                break;
            default:
                query = entryCollection.whereEqualTo(key, value);
                break;
        }
        entries = new ArrayList<>();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot doc = task.getResult();
                    if(doc.getDocuments() != null){
                        if(doc.getDocuments().size() > 0) {
                            for(DocumentSnapshot entrySnapshot : doc.getDocuments()){
                                Entry tmpEntry= new Entry();
                                tmpEntry.setId(entrySnapshot.getId());
                                tmpEntry.setAddress(entrySnapshot.getString("address"));
                                tmpEntry.setDate(entrySnapshot.getDate("date").toString());
                                tmpEntry.setDuration(entrySnapshot.getLong("duration"));
                                tmpEntry.setLatitude(entrySnapshot.getLong("latitude"));
                                tmpEntry.setLongitude(entrySnapshot.getLong("longitude"));
                                tmpEntry.setPrice(entrySnapshot.getLong("price"));
                                tmpEntry.setSubject(entrySnapshot.getString("subject"));
                                tmpEntry.setType(entrySnapshot.getLong("type"));
                                tmpEntry.setUser(entrySnapshot.getString("user"));
                                entries.add(tmpEntry);
                            }
                        }
                    }
                }
            }
        });
        Log.i("FINDENTRIES", "findEntries : " + entries.size());
        return new ArrayList<>(entries);
    }

    @Override
    public List<Entry> findEntries(List<SearchCriteria> searchCriteria) {
        CollectionReference entryCollection = db.collection(collectionName);
        Query query = entryCollection.orderBy("date");
        for(SearchCriteria s : searchCriteria){
            switch (s.getComparator()){
                case Comparators.COMPARATOR_EQUALS:
                    query = query.whereEqualTo(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_LIKE:
                    query = entryCollection.whereArrayContains(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_GREATER:
                    query = entryCollection.whereGreaterThan(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_LOWER:
                    query = entryCollection.whereLessThan(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_GREATER_EQUALS:
                    query = entryCollection.whereGreaterThanOrEqualTo(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_LOWER_EQUALS:
                    query = entryCollection.whereLessThanOrEqualTo(s.getKey(), s.getValue());
                    break;
                default:
                    query = entryCollection.whereEqualTo(s.getKey(), s.getValue());
                    break;
            }
        }
        this.entries = new ArrayList<>();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot doc = task.getResult();
                    if(doc.getDocuments() != null){
                        if(doc.getDocuments().size() > 0) {
                            for(DocumentSnapshot entrySnapshot : doc.getDocuments()){
                                Entry tmpEntry= new Entry();
                                tmpEntry.setId(entrySnapshot.getId());
                                tmpEntry.setAddress(entrySnapshot.getString("address"));
                                tmpEntry.setDate(entrySnapshot.getDate("date").toString());
                                tmpEntry.setDuration(entrySnapshot.getLong("duration"));
                                tmpEntry.setLatitude(entrySnapshot.getLong("latitude"));
                                tmpEntry.setLongitude(entrySnapshot.getLong("longitude"));
                                tmpEntry.setPrice(entrySnapshot.getLong("price"));
                                tmpEntry.setSubject(entrySnapshot.getString("subject"));
                                tmpEntry.setType(entrySnapshot.getLong("type"));
                                tmpEntry.setUser(entrySnapshot.getString("user"));
                                entries.add(tmpEntry);
                            }
                        }
                    }
                }
            }
        });
        return new ArrayList<>(entries);
    }

    @Override
    public void addEntry(Entry entry) {
        Map<String, Object> entryMap = new HashMap<>();
        entryMap.put("address", entry.getAddress());
        entryMap.put("date", entry.getDate());
        entryMap.put("duration", entry.getDuration());
        entryMap.put("latitude", entry.getLatitude());
        entryMap.put("longitude", entry.getLongitude());
        entryMap.put("price", entry.getPrice());
        entryMap.put("subject", entry.getSubject());
        entryMap.put("type", entry.getType());
        entryMap.put("user", entry.getUser());

        db.collection(collectionName).document().set(entryMap);
    }

    @Override
    public void deleteEntry(String id) {
        db.collection(collectionName).document(id).delete();
    }

    @Override
    public void updateEntry(String id, Entry entry) {
        Map<String, Object> entryMap = new HashMap<>();
        entryMap.put("address", entry.getAddress());
        entryMap.put("date", entry.getDate());
        entryMap.put("duration", entry.getDuration());
        entryMap.put("latitude", entry.getLatitude());
        entryMap.put("longitude", entry.getLongitude());
        entryMap.put("price", entry.getPrice());
        entryMap.put("subject", entry.getSubject());
        entryMap.put("type", entry.getType());
        entryMap.put("user", entry.getUser());

        db.collection(collectionName).document(id).set(entryMap);
    }
}
