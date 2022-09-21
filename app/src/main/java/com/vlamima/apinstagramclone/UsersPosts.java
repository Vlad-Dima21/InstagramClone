package com.vlamima.apinstagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        linearLayout = findViewById(R.id.linearLayout);

        Intent receivedIntentObject = getIntent();
        final String receivedUserName = receivedIntentObject.getStringExtra("username");
        setTitle(receivedUserName + "'s images");

        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username", receivedUserName);
        parseQuery.orderByDescending("createdAt");

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading posts");
        dialog.show();

        parseQuery.findInBackground((objects, e) -> {
            if (objects.size() > 0 && e == null) {
                for (ParseObject post : objects) {
                    TextView imageDes = new TextView(UsersPosts.this);
                    Object getImageDes = post.get("image_des");
                    if (getImageDes != null) {
                        imageDes.setText(getImageDes.toString());
                        imageDes.setTextColor(Color.WHITE);
                    } else {
                        imageDes.setText("No description");
                        imageDes.setTextColor(Color.GRAY);
                    }

                    ParseFile postPicture = (ParseFile) post.get("picture");
                    postPicture.getDataInBackground((data, e1) -> {
                        if (data != null && e1 == null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            ImageView postImageView = new ImageView(UsersPosts.this);
                            LinearLayout.LayoutParams imageViewParams =
                                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);

                            imageViewParams.setMargins(5, 5,5, 5);
                            postImageView.setLayoutParams(imageViewParams);
                            postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            postImageView.setImageBitmap(bitmap);

                            LinearLayout.LayoutParams imageDesParams =
                                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                            imageDesParams.setMargins(5, 5, 5, 5);
                            imageDes.setLayoutParams(imageDesParams);
                            imageDes.setGravity(Gravity.CENTER);
                            imageDes.setBackgroundColor(Color.parseColor("lime"));
                            imageDes.setTextSize(30f);

                            linearLayout.addView(postImageView);
                            linearLayout.addView(imageDes);
                        }
                    });
                }
                dialog.dismiss();
            } else {
                dialog.dismiss();
                FancyToast.makeText(UsersPosts.this,
                        receivedUserName + " has no posts",
                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                finish();
            }
        });
    }
}