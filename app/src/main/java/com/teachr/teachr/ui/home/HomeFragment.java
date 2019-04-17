package com.teachr.teachr.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teachr.teachr.models.Entry;
import com.teachr.teachr.models.Subject;
import com.teachr.teachr.models.User;
import com.teachr.teachr.EntryDetailActivity;
import com.teachr.teachr.EntryDetailFragment;
import com.teachr.teachr.R;
import com.teachr.teachr.Utils;
import com.teachr.teachr.offer.MatiereOfferActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private HomeViewModel mViewModel;
    private Spinner spinner;
    Geocoder geocoder;
    private FirebaseAuth mAuth;
    private DatabaseReference  _dbEntry;
    private DatabaseReference _dbSubject;
    private DatabaseReference _dbUser;
    private Entry entry;
    private RelativeLayout layoutFilter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<Entry> listEntry = new ArrayList<>();
    private HashMap<String, String> listSubject = new HashMap<>();
    private ArrayList<Subject> list = new ArrayList<>();
    private HashMap<String, User> listUser = new HashMap<>();
    private ArrayAdapter<Subject> dataAdapter;
    private HomeFragment.SimpleItemRecyclerViewAdapter adapter;

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
        adapter = new HomeFragment.SimpleItemRecyclerViewAdapter(this, listEntry, listUser);
        _dbSubject = database.getReference().child(Utils.getFirebaseSubject());
        _dbEntry = database.getReference().child(Utils.getFirebaseEntry());
        _dbUser = database.getReference().child(Utils.getFirebaseUser());
        _dbUser.addValueEventListener(_userListener);
        //View view = inflater.inflate(R.layout.home_fragment, container, false);
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = getView().findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        layoutFilter = getView().findViewById(R.id.filterRelative);
        layoutFilter.setVisibility(View.GONE);

        RecyclerView listView = view.findViewById(R.id.entry_list);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layout);
        _dbSubject.orderByKey().addValueEventListener(_subjectListener);
        _dbUser.orderByKey().addValueEventListener(_userListener);
        HomeFragment.SimpleItemRecyclerViewAdapter adapter = new HomeFragment.SimpleItemRecyclerViewAdapter(this, listEntry, listUser);
        setupRecyclerView(listView);

        dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

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

        getView().findViewById(R.id.filterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (layoutFilter.getVisibility() == View.GONE){
                    layoutFilter.setVisibility(View.VISIBLE);
                } else {
                    layoutFilter.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initMap(){

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        geocoder = new Geocoder(getContext(), Locale.getDefault());


        // Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), "AIzaSyBy5wzih3SDrAiC2D9SjbbGjrmmE0R32DY");
        }

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getContext());

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment1);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));


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
                    (String)map.get("user"), (long) map.get("type"), (String) map.get("address"));
            if(listUser.get(map.get("user")) != null)
                entry.setUsername(listUser.get(map.get("user")).getFirstname() + " " + listUser.get(map.get("user")).getLastname());
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
            Subject subject = new Subject(currentItem.getKey(), (String) map.get("name"));
            list.add(subject);

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
            (String) map.get("address"), (String) map.get("email"), (long) map.get("type"));

            listUser.put(currentItem.getKey(), user);

        }

        adapter.notifyDataSetChanged();
        RecyclerView listView = getView().findViewById(R.id.entry_list);
//        setupRecyclerView(listView);

        _dbEntry.orderByKey().addValueEventListener(_entryListener);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(this.adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        Log.d("taistoi", "pasok");
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + list.get(position).getName(), Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        Log.d("taistoi", "pasok");
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<HomeFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private HashMap<String, User> listUser;
        private final HomeFragment mParentActivity;
        private final List<Entry> mValues;
        private final FirebaseStorage db  = FirebaseStorage.getInstance();
        private StorageReference storageRef;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Entry item = (Entry) view.getTag();


                Context context = view.getContext();
                Intent intent = new Intent(context, EntryDetailActivity.class);
                intent.putExtra(EntryDetailFragment.ARG_ENTRY, item);
                String username = listUser.get(item.getUser()).getFirstname() + " " + listUser.get(item.getUser()).getLastname();
                intent.putExtra("username", username);

                context.startActivity(intent);
            }
        };

        SimpleItemRecyclerViewAdapter(HomeFragment parent,
                                      List<Entry> items,
                                      HashMap<String, User> listUser) {
            mValues = items;
            mParentActivity = parent;
            this.listUser = listUser;
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
            holder.addressView.setText(mValues.get(position).getAddress());
            holder.durationView.setText(String.format("%d", mValues.get(position).getDuration()) + " heures");
            holder.dateView.setText(mValues.get(position).getDate());
            holder.nameView.setText(mValues.get(position).getUsername());
//            holder.nameView.setText(listUser.get(mValues.get(position).getUser()).getFirstname() + " " + listUser.get(mValues.get(position).getUser()).getLastname());
            holder.priceView.setText(String.format("%d", mValues.get(position).getPrice()) + "$");

            storageRef = db.getReference().child("profile_pictures").child("profile_" + mValues.get(position).getUser());
            final long ONE_MEGABYTE = 1024 * 1024 * 8;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try {
                        holder.avatar.setVisibility(View.VISIBLE);
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        holder.avatar.setImageBitmap(Bitmap.createScaledBitmap(bmp, 120,
                                120, false));
                    }catch(Exception e){}
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    holder.avatar.setImageBitmap(BitmapFactory.decodeResource(holder.itemView.getResources(), R.mipmap.user));
                    holder.avatar.setVisibility(View.VISIBLE);
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {

                }
            });

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
            final ImageView avatar;

            ViewHolder(View view) {
                super(view);

                courseView = view.findViewById(R.id.course);
                durationView = view.findViewById(R.id.duration);
                addressView = view.findViewById(R.id.address);
                dateView = view.findViewById(R.id.date);
                nameView = view.findViewById(R.id.name);
                priceView = view.findViewById(R.id.price);
                avatar = view.findViewById(R.id.avatar);
            }
        }
    }




}
