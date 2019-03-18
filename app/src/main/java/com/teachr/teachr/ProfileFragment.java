package com.teachr.teachr;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teachr.teachr.models.User;

import java.io.ByteArrayOutputStream;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private ImageView profilImageView;


    private static final int GALLERY_REQUEST_CODE = 1;

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilImageView = (ImageView)getActivity().findViewById(R.id.profilImageView);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        FirebaseUser Fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users");

        ValueEventListener userEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null) {
//                    Log.d("INFOS", "RECUPERE " + user.getEmail() + " " + user.getFirstname() + " " + user.getLastname() + " " + user.getAddress());
                    setTextProfil(user.getEmail(), user.getFirstname(), user.getLastname(), user.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        database.child(""+ Fuser.getUid()).addValueEventListener(userEventListener);

        ((Button)getActivity().findViewById(R.id.saveModificationButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText)getActivity().findViewById(R.id.emailProfilEditText)).getText().toString();
                String firstname = ((EditText)getActivity().findViewById(R.id.firstnameProfilEditText)).getText().toString();
                String lastname = ((EditText)getActivity().findViewById(R.id.lastnameProfilEditText)).getText().toString();
                String address = ((EditText)getActivity().findViewById(R.id.addressProfilEditText)).getText().toString();

                if(email.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || address.isEmpty()){
                    Toast.makeText(getContext(), getString(R.string.enter_details_error), Toast.LENGTH_LONG).show();
                } else {
                    ModifyAccount(email, firstname, lastname, address);
                }

            }
        });

        Button profilButton = getActivity().findViewById(R.id.photoButton);
        profilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        getView().findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        ((ToggleButton)getView().findViewById(R.id.toggleButton)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Log.d("BUTTON", "ACTIVATED");
                    ((EditText)getView().findViewById(R.id.emailProfilEditText)).setFocusable(true);
                    ((EditText)getView().findViewById(R.id.emailProfilEditText)).setTextIsSelectable(true);

                    ((EditText)getView().findViewById(R.id.firstnameProfilEditText)).setFocusable(true);
                    ((EditText)getView().findViewById(R.id.firstnameProfilEditText)).setTextIsSelectable(true);

                    ((EditText)getView().findViewById(R.id.lastnameProfilEditText)).setFocusable(true);
                    ((EditText)getView().findViewById(R.id.lastnameProfilEditText)).setTextIsSelectable(true);

                    ((EditText)getView().findViewById(R.id.addressProfilEditText)).setFocusable(true);
                    ((EditText)getView().findViewById(R.id.addressProfilEditText)).setTextIsSelectable(true);

                    getView().findViewById(R.id.logoutButton).setVisibility(View.GONE);
                    getView().findViewById(R.id.saveModificationButton).setVisibility(View.VISIBLE);

                } else {
                    Log.d("BUTTON", "DEACTIVATED");
                    ((EditText)getView().findViewById(R.id.emailProfilEditText)).setFocusable(false);
                    ((EditText)getView().findViewById(R.id.emailProfilEditText)).setTextIsSelectable(false);

                    ((EditText)getView().findViewById(R.id.firstnameProfilEditText)).setFocusable(false);
                    ((EditText)getView().findViewById(R.id.firstnameProfilEditText)).setTextIsSelectable(false);

                    ((EditText)getView().findViewById(R.id.lastnameProfilEditText)).setFocusable(false);
                    ((EditText)getView().findViewById(R.id.lastnameProfilEditText)).setTextIsSelectable(false);

                    ((EditText)getView().findViewById(R.id.addressProfilEditText)).setFocusable(false);
                    ((EditText)getView().findViewById(R.id.addressProfilEditText)).setTextIsSelectable(false);

                    getView().findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.saveModificationButton).setVisibility(View.GONE);
                }
            }
        });

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_pictures").child("profile_" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        final long ONE_MEGABYTE = 1024 * 1024 * 8;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    getView().findViewById(R.id.profilImageView).setVisibility(View.VISIBLE);
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ImageView image = (ImageView) getView().findViewById(R.id.profilImageView);

                    image.setImageBitmap(Bitmap.createScaledBitmap(bmp, 120,
                            120, false));
                    getView().findViewById(R.id.profilImageViewProgressBar).setVisibility(View.GONE);
                }catch(Exception e){}
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                ImageView image = (ImageView) getView().findViewById(R.id.profilImageView);
                image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.teachr_logo));
                getView().findViewById(R.id.profilImageViewProgressBar).setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == ProfileFragment.GALLERY_REQUEST_CODE) {
                Uri selectedImage = data.getData();
                profilImageView.setImageURI(selectedImage);
            }
        }

        StorageReference profilRef = mStorageRef.child("profile_pictures").child("profile_" + FirebaseAuth.getInstance().getCurrentUser().getUid());


        // Get the data from an ImageView as bytes
        profilImageView.setDrawingCacheEnabled(true);
        profilImageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable)profilImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteData = baos.toByteArray();

        UploadTask uploadTask = profilRef.putBytes(byteData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    private void ModifyAccount (String email, String firstname, String lastname, String address) {
        FirebaseUser Fuser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User();
        user.setId(Fuser.getUid());
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setAddress(address);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(Fuser.getUid()).setValue(user);

        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
    }

    private void setTextProfil (String email, String firstname, String lastname, String address) {

        ((EditText)getView().findViewById(R.id.emailProfilEditText)).setText(email);
        ((EditText)getView().findViewById(R.id.firstnameProfilEditText)).setText(firstname);
        ((EditText)getView().findViewById(R.id.lastnameProfilEditText)).setText(lastname);
        ((EditText)getView().findViewById(R.id.addressProfilEditText)).setText(address);

    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

}
