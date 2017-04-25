package me.kevindevelops.takit.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.kevindevelops.takit.Post;
import me.kevindevelops.takit.R;

/**
 * Created by Kevin on 4/24/2017.
 */

public class PostsAdapter extends ArrayAdapter<Post>{
    public PostsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Post> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.posts_item, parent, false);
        }

        TextView subjectTextView = (TextView)convertView.findViewById(R.id.post_item_subject_textview);
        TextView locationTextView = (TextView)convertView.findViewById(R.id.post_item_location_textview);
        TextView usernameTextView = (TextView)convertView.findViewById(R.id.post_item_username_textview);
        TextView dateTextView = (TextView)convertView.findViewById(R.id.post_item_date_textview);

        Post post = getItem(position);

        String subject = post.getSubject();
        String username = post.getUsername();
        String date = post.getDate();
        String location = post.getLocation();

        subjectTextView.setText(subject);
        locationTextView.setText(location);
        usernameTextView.setText("By: " + username);
        dateTextView.setText("Posted: " + date);

        return convertView;
    }
}
