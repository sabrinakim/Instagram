package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    TextView tvUsernameD;
    TextView tvCaptionD;
    TextView tvTimestampD;
    ImageView ivImageD;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        tvUsernameD = findViewById(R.id.tvUsernameD);
        tvCaptionD = findViewById(R.id.tvCaptionD);
        tvTimestampD = findViewById(R.id.tvTimestampD);
        ivImageD = findViewById(R.id.ivImageD);

        tvUsernameD.setText(post.getUser().getUsername());
        tvCaptionD.setText(post.getDescription());
        tvTimestampD.setText(Post.calculateTimeAgo(post.getCreatedAt()));

        ParseFile image = post.getImage();
        if (image != null) { // image is optional, so its possible that it is null
            Glide.with(this).load(image.getUrl()).into(ivImageD);
        }
    }
}