package me.kevindevelops.takit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A placeholder fragment containing a simple view.
 */
public class ViewPostActivityFragment extends Fragment {

    private static final String LOG_TAG = ViewPostActivityFragment.class.getSimpleName();
    private Post mPost;

    private TextView mSubjectTextView;
    private TextView mLocationTextView;
    private TextView mEmailTextView;
    private TextView mUsernameTextView;
    private TextView mDateTextView;
    private TextView mPostTextView;
    private ImageView mImageView;


    public ViewPostActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_post, container, false);
        //TODO: Get post info and display it in activity
        Intent intent = getActivity().getIntent();
        mPost = (Post) intent.getSerializableExtra("POST_EXTRA");

        mSubjectTextView = (TextView)rootView.findViewById(R.id.view_subject_text_view);
        mLocationTextView = (TextView)rootView.findViewById(R.id.view_location_text_view);
        mEmailTextView = (TextView)rootView.findViewById(R.id.view_post_email_text_view);
        mUsernameTextView = (TextView)rootView.findViewById(R.id.view_post_username_text_view);
        mDateTextView = (TextView)rootView.findViewById(R.id.view_post_date_text_view);
        mPostTextView = (TextView)rootView.findViewById(R.id.view_post_text_view);
        mImageView = (ImageView)rootView.findViewById(R.id.view_post_imageview);

        mSubjectTextView.setText(mPost.getSubject());
        mLocationTextView.setText(mPost.getLocation());
        mEmailTextView.setText(mPost.getEmail());
        mUsernameTextView.setText(mPost.getUsername());
        mDateTextView.setText(mPost.getDate());
        mPostTextView.setText(mPost.getText());
        Glide.with(getActivity()).load(mPost.getPhotoUrl()).into(mImageView);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_view_post,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                return true;
            case R.id.action_email:
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",mPost.getEmail(),null));
                intent.putExtra(Intent.EXTRA_SUBJECT,"Response to Takit Post (" + mPost.getSubject() + ")");
                startActivity(Intent.createChooser(intent,"Send email..."));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
