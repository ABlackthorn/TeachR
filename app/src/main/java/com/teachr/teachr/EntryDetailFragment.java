package com.teachr.teachr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teachr.teachr.models.Entry;

/**
 * A fragment representing a single Entry detail screen.
 * This fragment is either contained in a {@link EntryListActivity}
 * in two-pane mode (on tablets) or a {@link EntryDetailActivity}
 * on handsets.
 */
public class EntryDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ENTRY = "entry";
    private final FirebaseStorage db  = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    /**
     * The dummy content this fragment is presenting.
     */
    private Entry entry;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EntryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ENTRY)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            entry = getArguments().getParcelable(ARG_ENTRY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.entry_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (entry != null) {
            String[] dates = entry.getDate().split("2019");
            ((TextView) rootView.findViewById(R.id.name)).setText(entry.getUser());
            ((TextView) rootView.findViewById(R.id.course)).setText(entry.getSubject());
            ((TextView) rootView.findViewById(R.id.date)).setText(dates[0] + "2019");
            ((TextView) rootView.findViewById(R.id.hour)).setText(dates[1]);
            ((TextView) rootView.findViewById(R.id.price)).setText(String.format("%d", entry.getPrice()) + "$");
            ((TextView) rootView.findViewById(R.id.address)).setText(entry.getAddress());
            ((TextView) rootView.findViewById(R.id.duration)).setText(String.format("%d", entry.getDuration()) + " heures");
            final ImageView avatar = ((ImageView) rootView.findViewById(R.id.avatar));

            Log.i("FRAGMENT", "onCreateView: " + getArguments().getString("userId"));
            storageRef = db.getReference().child("profile_pictures").child("profile_" + getArguments().getString("userId"));
            final long ONE_MEGABYTE = 1024 * 1024 * 8;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try {
                        avatar.setVisibility(View.VISIBLE);
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        avatar.setImageBitmap(Bitmap.createScaledBitmap(bmp, 120,
                                120, false));
                    }catch(Exception e){}
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    avatar.setImageBitmap(BitmapFactory.decodeResource(rootView.getResources(), R.mipmap.user));
                    avatar.setVisibility(View.VISIBLE);
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {

                }
            });
        }

        return rootView;
    }

}
