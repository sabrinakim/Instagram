package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public static String TAG = "PostAdapter";
    Context context;
    List<Post> posts;
    private String likedDescription;
    private String caption;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating each item in the recycling view with this layout.
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        // returning a VieHolder for each item in the recycling view.
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // all positions are iterated through.
        Post post = posts.get(position);
        // each viewholder is the same (created in above function)
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // we will place the proper text and images into these views in the bind function
        TextView tvUsername;
        TextView tvUsername2;
        TextView tvDescription;
        ImageView ivPostPicture;
        ImageView ivProfilePic;
        TextView tvCreatedAt;
        ToggleButton tbLike;
        TextView tvLikes;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // locate the views here
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivPostPicture = itemView.findViewById(R.id.ivPostPicture);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tbLike = itemView.findViewById(R.id.tbLike);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);

            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            tvCreatedAt.setText(post.getCreatedAt().toString());

            likedDescription = "Liked by " + post.getLikes().toString();
            tvLikes.setText(likedDescription);

            // image is saved in our database
            ParseFile image = post.getImage();
            if (image != null) { // image is optional, so its possible that it is null
                Glide.with(context).load(image.getUrl()).into(ivPostPicture);
            }
            ParseFile profilePic = post.getUser().getParseFile("profilePic");
            if (profilePic != null) { // image is optional, so its possible that it is null
                Glide.with(context).load(profilePic.getUrl())
                        .centerCrop()
                        .into(ivProfilePic);
            }

            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ProfileFeedActivity.class);
                    i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                    v.getContext().startActivity(i);
                }
            });



            tbLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // update database
                        post.setLikes(post.getLikes().intValue() + 1);
                        likedDescription = "Liked by " + post.getLikes().toString();
                        tvLikes.setText(likedDescription);
                        ParseUser u = ParseUser.getCurrentUser();
                        post.saveInBackground(new SaveCallback() { // saves in our database?
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "error while saving", e);
                                } else {
                                    Log.i(TAG, "like update success", e);
                                }
                            }
                        });
                    } else {
                        post.setLikes(post.getLikes().intValue() - 1);
                        likedDescription = "Liked by " + post.getLikes().toString();
                        tvLikes.setText(likedDescription);
                        post.saveInBackground(new SaveCallback() { // saves in our database?
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "error while saving", e);
                                } else {
                                    Log.i(TAG, "like update success", e);
                                }
                            }
                        });
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            //System.out.println("hello world");
            // get item position
            int position = getAdapterPosition();
            // makes sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the tweet at the position
                Post post = posts.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, DetailActivity.class);
                // serialize the tweet using parceler
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                // show the activity
                context.startActivity(intent);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

}
