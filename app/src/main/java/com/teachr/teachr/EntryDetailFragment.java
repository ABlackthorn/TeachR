package com.teachr.teachr;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        View rootView = inflater.inflate(R.layout.entry_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (entry != null) {
            ((TextView) rootView.findViewById(R.id.name)).setText(entry.getUser());
            ((TextView) rootView.findViewById(R.id.course)).setText(entry.getSubject());
            ((TextView) rootView.findViewById(R.id.date)).setText(entry.getDate().toString());
            ((TextView) rootView.findViewById(R.id.price)).setText(String.format("%d", entry.getPrice()));
            ((TextView) rootView.findViewById(R.id.address)).setText(entry.getAddress());
            ((TextView) rootView.findViewById(R.id.duration)).setText(String.format("%d", entry.getDuration()));
        }

        return rootView;
    }
}
