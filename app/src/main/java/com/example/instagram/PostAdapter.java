package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    List<Post> posts;

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
        TextView tvDescription;
        ImageView ivPostPicture;
        ImageView ivProfilePic;
        TextView tvCreatedAt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // locate the views here
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivPostPicture = itemView.findViewById(R.id.ivPostPicture);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);

            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            tvCreatedAt.setText(post.getCreatedAt().toString());

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
