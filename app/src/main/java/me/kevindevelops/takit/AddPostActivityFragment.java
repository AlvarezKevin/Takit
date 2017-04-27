package me.kevindevelops.takit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddPostActivityFragment extends Fragment {

    private static final String LOG_TAG = AddPostActivityFragment.class.getSimpleName();
    private static final int SUBJECT_TEXT_LIMIT = 140;

    private static final int RC_PHOTO_PICKER = 2;

    private String mUsername = null;
    private String mPhotoUrl = null;
    private String mEmail = null;

    private EditText mSubjectEditText;
    private EditText mPostTextEditText;
    private EditText mLocationEditText;
    private ImageButton mPhotoImageButton;
    private ImageView mImageView;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    public AddPostActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_post, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("posts");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("post_photos");

        mSubjectEditText = (EditText) rootView.findViewById(R.id.edit_text_subject);
        mPostTextEditText = (EditText) rootView.findViewById(R.id.edit_text_text);
        mLocationEditText = (EditText) rootView.findViewById(R.id.edit_text_location);
        mPhotoImageButton = (ImageButton) rootView.findViewById(R.id.post_image_button);
        mImageView = (ImageView) rootView.findViewById(R.id.add_post_imageview);

        mSubjectEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(SUBJECT_TEXT_LIMIT)});
        mPhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Insert image using"), RC_PHOTO_PICKER);
            }
        });

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        mEmail = user.getEmail();
        mUsername = user.getDisplayName();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            final Uri selectedImageUri = data.getData();
            StorageReference photoRef = mStorageReference.child(selectedImageUri.getLastPathSegment());

            photoRef.putFile(selectedImageUri).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(getActivity(), "Photo saved", Toast.LENGTH_SHORT).show();
                    mPhotoUrl = downloadUrl.toString();

                    Glide.with(getActivity()).load(mPhotoUrl).into(mImageView);
                }
            });
        }
    }

    private boolean validatePost() {
        String subject = mSubjectEditText.getText().toString().trim();
        String text = mPostTextEditText.getText().toString().trim();
        String location = mLocationEditText.getText().toString().trim();

        if (location.length() < 1 || text.length() < 1 || subject.length() < 1) {
            Toast.makeText(getActivity(), "Make sure all fields are filled in!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mPhotoUrl == null || mUsername == null) {
            return false;
        }
        return true;
    }


    private void pushPostToDatabase() {
        String subject = mSubjectEditText.getText().toString().trim();
        String text = mPostTextEditText.getText().toString().trim();
        String location = mLocationEditText.getText().toString().trim();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd,yyyy");
        String date = format.format(cal.getTime());

        if (validatePost()) {
            Post post = new Post(mUsername, subject, text, location, mEmail, mPhotoUrl, date);
            mDatabaseReference.push().setValue(post);
            Log.v(LOG_TAG, "On database");
            getActivity().finish();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_add_post, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_push_post:
                pushPostToDatabase();
                return true;
            case R.id.action_sign_out:
                mFirebaseAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }


}
