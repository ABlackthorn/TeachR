package com.teachr.teachr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import values.Statics;

/**
 * An activity representing a list of Entries. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EntryDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class EntryListActivity extends Activity implements View.OnClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private DatabaseReference  _db;
    private ArrayList<Entry> list = new ArrayList<>();
    FirebaseAuth mAuth;
    private SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(this, list, mTwoPane);

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

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                //action
                mAuth.signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.filterButton:
                //action
                //var intent : Intent = Intent(this, ListActivity::class.java);
                //startActivity(intent,
                //ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                break;
            case R.id.fab:
                this.addEntry();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);

        mAuth = FirebaseAuth.getInstance();

        Button button = findViewById(R.id.button);
        Button filterButton = findViewById(R.id.filterButton);
        FloatingActionButton fab = findViewById(R.id.fab);

        button.setOnClickListener(this);
        filterButton.setOnClickListener(this);
        fab.setOnClickListener(this);

        if (findViewById(R.id.entry_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        _db = FirebaseDatabase.getInstance().getReference().child("entry");

        View recyclerView = findViewById(R.id.entry_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        _db.orderByKey().addValueEventListener(_entryListener);
    }

    private void addEntry() {
        // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        Entry entry = new Entry("13/02", 2, 45.494354, -73.561818, 24,
                "11R", "Wolfgang Amadeus Mozart", 1);
        //Get the object id for the new task from the Firebase Database

        DatabaseReference newEntry = _db.child(Utils.getFirebaseEntry()).push();
        entry.setId(newEntry.getKey());

        //Set the values for new task in the firebase using the footer form
        newEntry.setValue(entry);
        list.add(entry);
        Toast.makeText(this, "Task added to the list successfully" + entry.getId(), Toast.LENGTH_SHORT).show();
    }

    private void loadEntryList(DataSnapshot dataSnapshot) {
        Log.d("MainActivity", "loadEntryList");

        Iterator<DataSnapshot> entries = dataSnapshot.getChildren().iterator();
        //Check if current database contains any collection
        if (entries.hasNext()) {

            list.clear();


            DataSnapshot listIndex = entries.next();
            Iterator<DataSnapshot> itemsIterator = listIndex.getChildren().iterator();

            //check if the collection has any task or not
            while (itemsIterator.hasNext()) {

                //get current task
                DataSnapshot currentItem = itemsIterator.next();

                //get current data in a map
                HashMap<String, Object> map = (HashMap<String, Object>) currentItem.getValue();
                //key will return the Firebase ID
                Entry entry = new Entry(currentItem.getKey(),
                        (String) map.get("date"), (long) map.get("duration"),
                        (double) map.get("latitude"), (double) map.get("longitude"), (long) map.get("price"), (String) map.get("subject"),
                        (String) map.get("user"), (long) map.get("type"));
                list.add(entry);
            }
        }

        //alert adapter that has changed
        this.adapter.notifyDataSetChanged();

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(this.adapter);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final EntryListActivity mParentActivity;
        private final List<Entry> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Entry item = (Entry) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(EntryDetailFragment.ARG_ITEM_ID, item.getId());
                    EntryDetailFragment fragment = new EntryDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getFragmentManager().beginTransaction()
                            .replace(R.id.entry_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, EntryDetailActivity.class);
                    intent.putExtra(EntryDetailFragment.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(EntryListActivity parent,
                                      List<Entry> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.entry_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
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
