package com.teachr.teachr;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MyCoursesFragment extends Fragment {

    private MyCoursesViewModel mViewModel;
    private FirebaseAuth mAuth;
    private DatabaseReference _db;
    private DatabaseReference _dbSubject;
    private DatabaseReference _dbUser;
    private ArrayList<Entry> list = new ArrayList<Entry>();
    private ArrayList<Entry> listEntry = new ArrayList<>();
    private HashMap<String, String> listSubject = new HashMap<>();
    private User user = new User();

//    private MyCoursesFragment.SimpleItemRecyclerViewAdapter adapter = new MyCoursesFragment.SimpleItemRecyclerViewAdapter(this, list, mAuth.getUid());
    private MyCoursesFragment.SimpleItemRecyclerViewAdapter adapter;

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
            loadUser(dataSnapshot);
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
        adapter = new MyCoursesFragment.SimpleItemRecyclerViewAdapter(this, list, mAuth.getUid());
        _db = FirebaseDatabase.getInstance().getReference().child(Utils.getFirebaseEntry());
        _dbSubject = FirebaseDatabase.getInstance().getReference().child(Utils.getFirebaseSubject());
        _dbUser = FirebaseDatabase.getInstance().getReference().child(Utils.getFirebaseUser()).child(mAuth.getUid());
        return inflater.inflate(R.layout.my_courses_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.entry_list);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        _dbSubject.orderByKey().addValueEventListener(_subjectListener);
        _dbUser.orderByKey().addValueEventListener(_userListener);
        listView.setLayoutManager(layout);

        Log.d("entry?", this.mAuth.getUid());
        //Query myTopPostsQuery = _db.child("entry").orderByChild("user").equalTo(this.mAuth.getUid());
        // myTopPostsQuery.addChildEventListener(this._childListener);

        _db.addValueEventListener(this._entryListener);
        MyCoursesFragment.SimpleItemRecyclerViewAdapter adapter = new MyCoursesFragment.SimpleItemRecyclerViewAdapter(this, list, mAuth.getUid());
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
                        user.getFirstname() + ' ' + user.getLastname(), (long) map.get("type"), (String) map.get("address"));
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
            //list.add(subject);

        }
        _dbUser.orderByKey().addValueEventListener(_userListener);
    }

    private void loadUser(DataSnapshot dataSnapshot) {

        user = dataSnapshot.getValue(User.class);
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(this.adapter);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<MyCoursesFragment.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final String userId;
        private final MyCoursesFragment mParentActivity;
        private final List<Entry> mValues;
        private StorageReference storageRef;
        private final FirebaseStorage db  = FirebaseStorage.getInstance();
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
                                      List<Entry> items, String userId) {
            mValues = items;
            mParentActivity = parent;
            this.userId = userId;
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
            holder.addressView.setText(mValues.get(position).getAddress());
            holder.durationView.setText(String.format("%d", mValues.get(position).getDuration()) + " heures");
            holder.dateView.setText(mValues.get(position).getDate());
            holder.nameView.setText(mValues.get(position).getUser());
            holder.priceView.setText(String.format("%d", mValues.get(position).getPrice()) + "$");
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);

            storageRef = db.getReference().child("profile_pictures").child("profile_" + userId);
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
