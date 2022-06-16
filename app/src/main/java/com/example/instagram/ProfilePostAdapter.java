package com.example.instagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder> {

    Context context;
    List<Post> posts;

    public ProfilePostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating each item in the recycling view with this layout.
        View view = LayoutInflater.from(context).inflate(R.layout.profile_post_item, parent, false);
        // returning a VieHolder for each item in the recycling view.
        return new ProfilePostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfilePost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePost = itemView.findViewById(R.id.ivProfilePost);
        }

        public void bind(Post post) {
            ParseFile image = post.getImage();
            if (image != null) { // image is optional, so its possible that it is null
                Glide.with(context).load(image.getUrl()).into(ivProfilePost);
            }
        }
    }
}
