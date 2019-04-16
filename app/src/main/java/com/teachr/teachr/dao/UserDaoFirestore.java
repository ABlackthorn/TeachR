package com.teachr.teachr.dao;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.teachr.teachr.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoFirestore implements UserDao {

    private final String collectionName = "users";

    private static UserDaoFirestore single_instance = null;

    public User user = new User();
    private List<User> users;

    FirebaseFirestore db;

    private UserDaoFirestore(){
         db = FirebaseFirestore.getInstance();
    }

    public static UserDaoFirestore getInstance()
    {
        if (single_instance == null)
            single_instance = new UserDaoFirestore();

        return single_instance;
    }

    @Override
    public User getUser(String id) {
        db.collection(collectionName).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        user.setId(doc.getString("id"));
                        user.setAddress(doc.getString("address"));
                        user.setEmail(doc.getString("email"));
                        user.setFirstname(doc.getString("firstname"));
                        user.setLastname(doc.getString("lastname"));
                        user.setType(doc.getLong("type"));
                    } else {
                        user.setId("NOT FOUND");
                        user.setAddress("NOT FOUND");
                        user.setEmail("NOT FOUND");
                        user.setFirstname("NOT FOUND");
                        user.setLastname("NOT FOUND");
                        user.setType(0);
                    }
                }
            }
        });

        return (User)user.clone();

    }

    @Override
    public User findUser(String key, String value) {
        CollectionReference userCollection = db.collection(collectionName);
        Query query = userCollection.whereEqualTo(key, value);
        this.user = new User();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot doc = task.getResult();
                    if(doc.getDocuments() != null){
                        if(doc.getDocuments().size() > 0) {
                            DocumentSnapshot userSnapshot = doc.getDocuments().get(0);
                            user.setId(userSnapshot.getString("id"));
                            user.setAddress(userSnapshot.getString("address"));
                            user.setEmail(userSnapshot.getString("email"));
                            user.setFirstname(userSnapshot.getString("firstname"));
                            user.setLastname(userSnapshot.getString("lastname"));
                            user.setType(userSnapshot.getLong("type"));
                        }
                    }
                }
            }
        });
        return (User)user.clone();
    }

    @Override
    public List<User> findUsers(String key, String value, int comparator) {
        CollectionReference userCollection = db.collection(collectionName);
        Query query;
        switch (comparator){
            case Comparators.COMPARATOR_EQUALS:
                query = userCollection.whereEqualTo(key, value);
                break;
            case Comparators.COMPARATOR_LIKE:
                query = userCollection.whereArrayContains(key, value);
                break;
            case Comparators.COMPARATOR_GREATER:
                query = userCollection.whereGreaterThan(key, value);
                break;
            case Comparators.COMPARATOR_LOWER:
                query = userCollection.whereLessThan(key, value);
                break;
            case Comparators.COMPARATOR_GREATER_EQUALS:
                query = userCollection.whereGreaterThanOrEqualTo(key, value);
                break;
            case Comparators.COMPARATOR_LOWER_EQUALS:
                query = userCollection.whereLessThanOrEqualTo(key, value);
                break;
            default:
                query = userCollection.whereEqualTo(key, value);
                break;
        }
        this.users = new ArrayList<>();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot doc = task.getResult();
                    if(doc.getDocuments() != null){
                        if(doc.getDocuments().size() > 0) {
                            for(DocumentSnapshot userSnapshot : doc.getDocuments()){
                                User tmpUser = new User();
                                tmpUser.setId(userSnapshot.getString("id"));
                                tmpUser.setAddress(userSnapshot.getString("address"));
                                tmpUser.setEmail(userSnapshot.getString("email"));
                                tmpUser.setFirstname(userSnapshot.getString("firstname"));
                                tmpUser.setLastname(userSnapshot.getString("lastname"));
                                tmpUser.setType(userSnapshot.getLong("type"));
                                users.add(tmpUser);
                            }
                        }
                    }
                }
            }
        });
        return new ArrayList<>(users);
    }

    @Override
    public List<User> findUsers(List<SearchCriteria> searchCriteria) {
        CollectionReference userCollection = db.collection(collectionName);
        Query query = userCollection.orderBy("firstname");
        for(SearchCriteria s : searchCriteria){
            switch (s.getComparator()){
                case Comparators.COMPARATOR_EQUALS:
                    query = query.whereEqualTo(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_LIKE:
                    query = userCollection.whereArrayContains(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_GREATER:
                    query = userCollection.whereGreaterThan(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_LOWER:
                    query = userCollection.whereLessThan(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_GREATER_EQUALS:
                    query = userCollection.whereGreaterThanOrEqualTo(s.getKey(), s.getValue());
                    break;
                case Comparators.COMPARATOR_LOWER_EQUALS:
                    query = userCollection.whereLessThanOrEqualTo(s.getKey(), s.getValue());
                    break;
                default:
                    query = userCollection.whereEqualTo(s.getKey(), s.getValue());
                    break;
            }
        }
        this.users = new ArrayList<>();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot doc = task.getResult();
                    if(doc.getDocuments() != null){
                        if(doc.getDocuments().size() > 0) {
                            for(DocumentSnapshot userSnapshot : doc.getDocuments()){
                                User tmpUser = new User();
                                tmpUser.setId(userSnapshot.getString("id"));
                                tmpUser.setAddress(userSnapshot.getString("address"));
                                tmpUser.setEmail(userSnapshot.getString("email"));
                                tmpUser.setFirstname(userSnapshot.getString("firstname"));
                                tmpUser.setLastname(userSnapshot.getString("lastname"));
                                tmpUser.setType(userSnapshot.getLong("type"));
                                users.add(tmpUser);
                            }
                        }
                    }
                }
            }
        });
        return new ArrayList<>(users);
    }

    @Override
    public void addUser(User user){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("address", user.getAddress());
        userMap.put("email", user.getEmail());
        userMap.put("firstname", user.getFirstname());
        userMap.put("lastname", user.getLastname());
        userMap.put("type", user.getType());
        userMap.put("id", user.getId());

        db.collection(collectionName).document(user.getId()).set(userMap);

    }

    @Override
    public void deleteUser(String id){
        db.collection(collectionName).document(id).delete();
    }

    @Override
    public void updateUser(String id, User user){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("address", user.getAddress());
        userMap.put("email", user.getEmail());
        userMap.put("firstname", user.getFirstname());
        userMap.put("lastname", user.getLastname());
        userMap.put("type", user.getType());
        userMap.put("id", user.getId());

        db.collection(collectionName).document(id).set(userMap);
    }

}
