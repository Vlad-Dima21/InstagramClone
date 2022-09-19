package com.vlamima.apinstagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;

public class SharePictureTab extends Fragment implements View.OnClickListener {

    private ImageView imgShare;
    private EditText edtDesc;
    private Button btnShare;

    private Bitmap receivedImageBitmap;

    public SharePictureTab() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        imgShare = view.findViewById(R.id.imgShare);
        edtDesc = view.findViewById(R.id.edtPhotoDesc);
        btnShare = view.findViewById(R.id.btnShare);

        imgShare.setOnClickListener(SharePictureTab.this);
        btnShare.setOnClickListener(SharePictureTab.this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgShare:
                if (Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                } else {
                    getChosenImage();
                }
                break;
            case R.id.btnShare:
                if (receivedImageBitmap != null) {
                    if (edtDesc.getText().toString().equals("")) {
                        FancyToast.makeText(getContext(), "Please enter a description first",
                                FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                    } else {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("img.png", bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_des", edtDesc.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Uploading image");
                        dialog.show();
                        parseObject.saveInBackground(e -> {
                            if (e == null) {
                                FancyToast.makeText(getContext(), "Image uploaded",
                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            } else {
                                FancyToast.makeText(getContext(), "Error: " + e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            }
                            dialog.dismiss();
                        });
                    }
                } else {
                    FancyToast.makeText(getContext(), "Please select an image first",
                            FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                }
                break;
        }
    }

    private void getChosenImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {
            if (Array.getLength(grantResults) > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FancyToast.makeText(getContext(), "Permission granted", FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS, false).show();
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    receivedImageBitmap = BitmapFactory.decodeFile(picturePath);

                    imgShare.setImageBitmap(receivedImageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}