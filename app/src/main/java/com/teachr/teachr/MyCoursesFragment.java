package com.teachr.teachr;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teachr.teachr.models.Entry;
import com.teachr.teachr.models.Subject;
import com.teachr.teachr.models.User;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MyCoursesFragment extends Fragment {

    private MyCoursesViewModel mViewModel;
    private FirebaseAuth mAuth;
    private DatabaseReference _db;
    private ArrayList<Entry> list = new ArrayList<Entry>();
    private HashMap<String, String> listSubject = new HashMap<>();
    private HashMap<String, User> listUser = new HashMap<>();
    private DatabaseReference _dbSubject;
    private DatabaseReference _dbUser;

    private MyCoursesFragment.SimpleItemRecyclerViewAdapter adapter = new MyCoursesFragment.SimpleItemRecyclerViewAdapter(this, list);

    ValueEventListener _entryListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            loadEntryList(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException());
        }
    };

    ValueEventListener _subjectListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            loadSubjectList(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException());
        }
    };

    ValueEventListener _userListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            loadUserList(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException());
        }
    };

    ChildEventListener _childListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d("Test1", "onChildAdded:" + dataSnapshot.getKey());

            // A new comment has been added, add it to the displayed list
            loadEntryList(dataSnapshot);
            // ...
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d("Test1", "onChildChanged:" + dataSnapshot.getKey());

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so displayed the changed comment.
            Comment newComment = dataSnapshot.getValue(Comment.class);
            String commentKey = dataSnapshot.getKey();

            // ...
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d("Test1", "onChildRemoved:" + dataSnapshot.getKey());

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so remove it.
            String commentKey = dataSnapshot.getKey();

            // ...
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d("Test1", "onChildMoved:" + dataSnapshot.getKey());

            // A comment has changed position, use the key to determine if we are
            // displaying this comment and if so move it.
            Comment movedComment = dataSnapshot.getValue(Comment.class);
            String commentKey = dataSnapshot.getKey();

            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("Test1", "postComments:onCancelled", databaseError.toException());
        }
    };

    public static MyCoursesFragment newInstance() {
        return new MyCoursesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        _db = FirebaseDatabase.getInstance().getReference().child(Utils.getFirebaseEntry());
        _dbSubject = FirebaseDatabase.getInstance().getReference().child(Utils.getFirebaseSubject());
        _dbUser = FirebaseDatabase.getInstance().getReference().child(Utils.getFirebaseUser());
        return inflater.inflate(R.layout.my_courses_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.entry_list);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layout);

        Log.d("entry?", this.mAuth.getUid());
        //Query myTopPostsQuery = _db.child("entry").orderByChild("user").equalTo(this.mAuth.getUid());
        // myTopPostsQuery.addChildEventListener(this._childListener);

        _dbSubject.orderByKey().addValueEventListener(_subjectListener);
        _dbUser.orderByKey().addValueEventListener(_userListener);
        MyCoursesFragment.SimpleItemRecyclerViewAdapter adapter = new MyCoursesFragment.SimpleItemRecyclerViewAdapter(this, list);
        setupRecyclerView(listView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyCoursesViewModel.class);
        // TODO: Use the ViewModel
    }

    private void loadEntryList(DataSnapshot dataSnapshot) {

        Iterator<DataSnapshot> entries = dataSnapshot.getChildren().iterator();
        //Check if current database contains any collection

        list.clear();

        //check if the collection has any task or not
        while (entries.hasNext()) {

            //get current task
            DataSnapshot currentItem = entries.next();
            //get current data in a map
            HashMap<String, Object> map = (HashMap<String, Object>) currentItem.getValue();
            Log.d("ok", (String) map.get("user"));

            //key will return the Firebase ID
            if ( map.get("user") != null && ( ( (String) map.get("user")).equals(this.mAuth.getUid()))){
                Entry entry = new Entry(currentItem.getKey(),
                        (String) map.get("date"), (long) map.get("duration"),
                        (double) map.get("latitude"), (double) map.get("longitude"), (long) map.get("price"), listSubject.get(map.get("subject")),
                        "Amine" + ' ' + "Baidada", (long) map.get("type"), (String) map.get("address"));
                list.add(entry);
            }
        }

        //alert adapter that has changed
        this.adapter.notifyDataSetChanged();
    }


    private void loadSubjectList(DataSnapshot dataSnapshot) {
        Log.d("MainActivity", "loadSubjectList");

        Iterator<DataSnapshot> subjects = dataSnapshot.getChildren().iterator();
        //Check if current database contains any collection

        listSubject.clear();

        //check if the collection has any task or not
        while (subjects.hasNext()) {

            //get current task
            DataSnapshot currentItem = subjects.next();

            //get current data in a map
            HashMap<String, Object> map = (HashMap<String, Object>) currentItem.getValue();
            //key will return the Firebase ID

            listSubject.put(currentItem.getKey(), (String) map.get("name"));
            Subject subject = new Subject(currentItem.getKey(), (String) map.get("name"));

        }
        _dbUser.orderByKey().addValueEventListener(_userListener);
    }

    private void loadUserList(DataSnapshot dataSnapshot) {
        Log.d("MainActivity", "loadEntryList");

        Iterator<DataSnapshot> subjects = dataSnapshot.getChildren().iterator();
        //Check if current database contains any collection

        listUser.clear();

        //check if the collection has any task or not
        while (subjects.hasNext()) {

            //get current task
            DataSnapshot currentItem = subjects.next();

            //get current data in a map
            HashMap<String, Object> map = (HashMap<String, Object>) currentItem.getValue();
            //key will return the Firebase ID

            User user = new User(currentItem.getKey(), (String) map.get("firstname"), (String) map.get("lastname"),
                    (String) map.get("address"), (String) map.get("email"), (String) map.get("password"), (long) map.get("type"), (String) map.get("avatar") );

            listUser.put(currentItem.getKey(), user);
        }
        _db.orderByKey().addValueEventListener(_entryListener);
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(this.adapter);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<MyCoursesFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final MyCoursesFragment mParentActivity;
        private final List<Entry> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Entry item = (Entry) view.getTag();



                Context context = view.getContext();
                Intent intent = new Intent(context, EntryDetailActivity.class);
                intent.putExtra(EntryDetailFragment.ARG_ENTRY, item);

                context.startActivity(intent);
            }
        };

        SimpleItemRecyclerViewAdapter(MyCoursesFragment parent,
                                      List<Entry> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public MyCoursesFragment.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.entry_list_content, parent, false);
            return new MyCoursesFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyCoursesFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.courseView.setText(mValues.get(position).getSubject());
            holder.addressView.setText(String.format("%1$,.2f", mValues.get(position).getLatitude()));
            holder.durationView.setText(String.format("%d", mValues.get(position).getDuration()));
            holder.dateView.setText(mValues.get(position).getDate());
            holder.nameView.setText(mValues.get(position).getUser());
            holder.priceView.setText(String.format("%d", mValues.get(position).getPrice()));
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView courseView;
            final TextView addressView;
            final TextView durationView;
            final TextView dateView;
            final TextView nameView;
            final TextView priceView;

            ViewHolder(View view) {
                super(view);

                courseView = view.findViewById(R.id.course);
                durationView = view.findViewById(R.id.duration);
                addressView = view.findViewById(R.id.address);
                dateView = view.findViewById(R.id.date);
                nameView = view.findViewById(R.id.name);
                priceView = view.findViewById(R.id.price);
            }
        }
    }

}
