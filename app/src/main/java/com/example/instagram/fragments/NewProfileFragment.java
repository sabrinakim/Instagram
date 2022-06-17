package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.instagram.Post;
import com.example.instagram.PostAdapter;
import com.example.instagram.ProfilePostAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class NewProfileFragment extends Fragment {

    public static final int LIMIT = 20;
    public static final String TAG = "NewProfileFragment";
    private ProfilePostAdapter adapter;
    private RecyclerView rvProfilePostsFrag;
    private ImageView ivProfilePicFrag;
    private List<Post> allPosts;


    public NewProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //rvProfilePostsFrag = view.findViewById(R.id.rvProfilePostsFrag);
        rvProfilePostsFrag = view.findViewById(R.id.rvProfilePostsFrag);
        ivProfilePicFrag = view.findViewById(R.id.ivProfilePicFrag);

        allPosts = new ArrayList<>();
        adapter = new ProfilePostAdapter(getContext(), allPosts);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);


        // steps to use the recycler view:
        // 0. create layout for one row in the list
        // 1. create the adapter
        // 2. create the data source
        // 3. set the adapter on the recycler view
        rvProfilePostsFrag.setAdapter(adapter);
        // 4. set the layout manager on the recycler view
        rvProfilePostsFrag.setLayoutManager(gridLayoutManager);
        queryPosts();

        ParseUser u = ParseUser.getCurrentUser();
        ParseFile image = u.getParseFile("profilePic");
        if (image != null) { // image is optional, so its possible that it is null
            Glide.with(this).load(image.getUrl()).into(ivProfilePicFrag);
        }
    }

    // take the latest LIMIT # of posts from our database
    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(LIMIT);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}