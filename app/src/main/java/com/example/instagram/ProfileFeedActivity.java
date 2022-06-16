package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileFeedActivity extends AppCompatActivity {

    public static String TAG = "ProfileFeedActivity";
    private ProfilePostAdapter adapter;
    private RecyclerView rvProfilePosts;
    private List<Post> allPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_feed);

        rvProfilePosts = findViewById(R.id.rvProfilePosts);

        allPosts = new ArrayList<>();
        adapter = new ProfilePostAdapter(this, allPosts);

//        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
//        StaggeredGridLayoutManager gridLayoutManager =
//                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        // Attach the layout manager to the recycler view
//        recyclerView.setLayoutManager(gridLayoutManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        rvProfilePosts.setAdapter(adapter);
        rvProfilePosts.setLayoutManager(gridLayoutManager);

        queryProfilePosts();
    }

    private void queryProfilePosts() {
        Post post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        //query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.whereEqualTo(Post.KEY_USER, post.getUser());
        System.out.println("?" + ParseUser.getCurrentUser());
        query.setLimit(20); // change this line later !!!!!!!!!!
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