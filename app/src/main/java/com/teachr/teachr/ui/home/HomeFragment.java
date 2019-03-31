package com.teachr.teachr.ui.home;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teachr.teachr.models.Entry;
import com.teachr.teachr.models.User;
import com.teachr.teachr.EntryDetailActivity;
import com.teachr.teachr.EntryDetailFragment;
import com.teachr.teachr.R;
import com.teachr.teachr.Utils;
import com.teachr.teachr.offer.MatiereOfferActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private FirebaseAuth mAuth;
    private DatabaseReference  _dbEntry;
    private DatabaseReference _dbSubject;
    private DatabaseReference _dbUser;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<Entry> listEntry = new ArrayList<>();
    private HashMap<String, String> listSubject = new HashMap<>();
    private HashMap<String, User> listUser = new HashMap<>();

    private HomeFragment.SimpleItemRecyclerViewAdapter adapter = new HomeFragment.SimpleItemRecyclerViewAdapter(this, listEntry);

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

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        _dbSubject = database.getReference().child(Utils.getFirebaseSubject());
        _dbEntry = database.getReference().child(Utils.getFirebaseEntry());
        _dbUser = database.getReference().child(Utils.getFirebaseUser());
        //View view = inflater.inflate(R.layout.home_fragment, container, false);

        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.entry_list);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layout);

        _dbSubject.orderByKey().addValueEventListener(_subjectListener);
        _dbUser.orderByKey().addValueEventListener(_userListener);
        HomeFragment.SimpleItemRecyclerViewAdapter adapter = new HomeFragment.SimpleItemRecyclerViewAdapter(this, listEntry);
        setupRecyclerView(listView);

        getView().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth mAuth = null;
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null) {
                    Intent intent = new Intent(getContext(), MatiereOfferActivity.class);
                    Entry entry = new Entry();
                    entry.setUser(currentUser.getUid());
                    intent.putExtra("entry", entry);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void loadEntryList(DataSnapshot dataSnapshot) {
        Log.d("MainActivity", "loadEntryList");

        Iterator<DataSnapshot> entries = dataSnapshot.getChildren().iterator();
        //Check if current database contains any collection

        listEntry.clear();

        //check if the collection has any task or not
        while (entries.hasNext()) {

            //get current task
            DataSnapshot currentItem = entries.next();

            //get current data in a map
            HashMap<String, Object> map = (HashMap<String, Object>) currentItem.getValue();
            //key will return the Firebase ID

            Entry entry = new Entry(currentItem.getKey(),
                    (String) map.get("date"), (long) map.get("duration"),
                    (double) map.get("latitude"), (double) map.get("longitude"), (long) map.get("price"), listSubject.get(map.get("subject")),
                    listUser.get(map.get("user")).getFirstname() + ' ' + listUser.get(map.get("user")).getLastname(), (long) map.get("type"), (String) map.get("address"));
            listEntry.add(entry);
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
        _dbEntry.orderByKey().addValueEventListener(_entryListener);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(this.adapter);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<HomeFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final HomeFragment mParentActivity;
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

        SimpleItemRecyclerViewAdapter(HomeFragment parent,
                                      List<Entry> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public HomeFragment.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.entry_list_content, parent, false);
            return new HomeFragment.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final HomeFragment.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.courseView.setText(mValues.get(position).getSubject());
            Log.d("adamo", "test" + mValues.get(position));
            holder.addressView.setText(mValues.get(position).getAddress());
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
