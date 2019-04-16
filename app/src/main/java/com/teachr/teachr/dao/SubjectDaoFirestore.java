package com.teachr.teachr.dao;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.teachr.teachr.models.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectDaoFirestore implements SubjectDao {

    private final String collectionName = "subjects";

    private static SubjectDaoFirestore single_instance = null;

    private FirebaseFirestore db;
    private Subject subject;
    private List<Subject> subjects;

    private SubjectDaoFirestore() {db = FirebaseFirestore.getInstance();}

    public static SubjectDaoFirestore getInstance(){
        if(single_instance == null)
            single_instance = new SubjectDaoFirestore();
        return single_instance;
    }

    @Override
    public Subject getSubject(String id) {
        subject = new Subject();
        subject.setId(id);
        db.collection(collectionName).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        subject.setName(doc.getString("name"));
                    } else {
                        subject.setName(doc.getString("NOT FOUND"));
                    }
                }
            }
        });
        return (Subject)subject.clone();
    }

    @Override
    public Subject findSubject(String key, String value) {
        CollectionReference subjectCollection = db.collection(collectionName);
        Query query = subjectCollection.whereEqualTo(key, value);
        subject = new Subject();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot doc = task.getResult();
                    if(doc.getDocuments() != null){
                        if(doc.getDocuments().size() > 0) {
                            DocumentSnapshot subjectSnapshot = doc.getDocuments().get(0);
                            subject.setId(subjectSnapshot.getId());
                            subject.setName(subjectSnapshot.getString("name"));
                        }
                    }
                }
            }
        });
        return (Subject)subject.clone();
    }

    @Override
    public List<Subject> findSubjects(String key, String value, int comparator) {
        CollectionReference subjectCollection = db.collection(collectionName);
        Query query;
        switch (comparator){
            case Comparators.COMPARATOR_EQUALS:
                query = subjectCollection.whereEqualTo(key, value);
                break;
            case Comparators.COMPARATOR_LIKE:
                query = subjectCollection.whereArrayContains(key, value);
                break;
            case Comparators.COMPARATOR_GREATER:
                query = subjectCollection.whereGreaterThan(key, value);
                break;
            case Comparators.COMPARATOR_LOWER:
                query = subjectCollection.whereLessThan(key, value);
                break;
            case Comparators.COMPARATOR_GREATER_EQUALS:
                query = subjectCollection.whereGreaterThanOrEqualTo(key, value);
                break;
            case Comparators.COMPARATOR_LOWER_EQUALS:
                query = subjectCollection.whereLessThanOrEqualTo(key, value);
                break;
            default:
                query = subjectCollection.whereEqualTo(key, value);
                break;
        }
        this.subjects = new ArrayList<>();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot doc = task.getResult();
                    if(doc.getDocuments() != null){
                        if(doc.getDocuments().size() > 0) {
                            for(DocumentSnapshot subjectSnapshot : doc.getDocuments()){
                                Subject tmpSubject= new Subject();
                                tmpSubject.setId(subjectSnapshot.getId());
                                tmpSubject.setName(subjectSnapshot.getString("name"));
                                subjects.add(tmpSubject);
                            }
                        }
                    }
                }
            }
        });
        return new ArrayList<>(subjects);
    }

    @Override
    public List<Subject> findSubjects(List<SearchCriteria> searchCriteria) {
        CollectionReference subjectCollection = db.collection(collectionName);
        Query query = subjectCollection.orderBy("name");
        for(SearchCriteria s : searchCriteria){
            switch (s.getComparator()){
                case Comparators.COMPARATOR_EQUALS:
                    query = query.whereEqualTo(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_LIKE:
                    query = subjectCollection.whereArrayContains(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_GREATER:
                    query = subjectCollection.whereGreaterThan(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_LOWER:
                    query = subjectCollection.whereLessThan(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_GREATER_EQUALS:
                    query = subjectCollection.whereGreaterThanOrEqualTo(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_LOWER_EQUALS:
                    query = subjectCollection.whereLessThanOrEqualTo(s.getKey(), s.getValue());
                    break;
                default:
                    query = subjectCollection.whereEqualTo(s.getKey(), s.getValue());
                    break;
            }
        }
        this.subjects = new ArrayList<>();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot doc = task.getResult();
                    if(doc.getDocuments() != null){
                        if(doc.getDocuments().size() > 0) {
                            for(DocumentSnapshot subjectSnapshot : doc.getDocuments()){
                                Subject tmpSubject = new Subject();
                                tmpSubject.setId(subjectSnapshot.getId());
                                tmpSubject.setName(subjectSnapshot.getString("name"));
                                subjects.add(tmpSubject);
                            }
                        }
                    }
                }
            }
        });
        return new ArrayList<>(subjects);
    }

    @Override
    public void addSubject(Subject subject) {
        Map<String, Object> subjectMap = new HashMap<>();
        subjectMap.put("name", subject.getName());


        db.collection(collectionName).document().set(subjectMap);
    }

    @Override
    public void deleteSubject(String id) {
        db.collection(collectionName).document(id).delete();
    }

    @Override
    public void updateSubject(String id, Subject subject) {
        Map<String, Object> subjectMap = new HashMap<>();
        subjectMap.put("name", subject.getName());


        db.collection(collectionName).document(id).set(subjectMap);
    }
}
