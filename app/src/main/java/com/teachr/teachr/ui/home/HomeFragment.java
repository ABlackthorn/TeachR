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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teachr.teachr.models.Entry;
import com.teachr.teachr.EntryDetailActivity;
import com.teachr.teachr.EntryDetailFragment;
import com.teachr.teachr.R;
import com.teachr.teachr.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private FirebaseAuth mAuth;
    private DatabaseReference _db;
    private ArrayList<Entry> list = new ArrayList<Entry>();

    private HomeFragment.SimpleItemRecyclerViewAdapter adapter = new HomeFragment.SimpleItemRecyclerViewAdapter(this, list);

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

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        _db = FirebaseDatabase.getInstance().getReference().child(Utils.getFirebaseEntry());

        //View view = inflater.inflate(R.layout.home_fragment, container, false);

        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.entry_list);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layout);

        _db.addValueEventListener(_entryListener);

        HomeFragment.SimpleItemRecyclerViewAdapter adapter = new HomeFragment.SimpleItemRecyclerViewAdapter(this, list);
        setupRecyclerView(listView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
            //key will return the Firebase ID
            Entry entry = new Entry(currentItem.getKey(),
                    (String) map.get("date"), (long) map.get("duration"),
                    (double) map.get("latitude"), (double) map.get("longitude"), (long) map.get("price"), (String) map.get("subject"),
                    (String) map.get("user"), (long) map.get("type"));
            list.add(entry);
        }

        //alert adapter that has changed
        this.adapter.notifyDataSetChanged();

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
