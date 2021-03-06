package com.tacademy.miniproject.content;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tacademy.miniproject.R;
import com.tacademy.miniproject.autodata.UserResult;
import com.tacademy.miniproject.data.ContentData;
import com.tacademy.miniproject.manager.NetworkManager;
import com.tacademy.miniproject.manager.NetworkRequest;
import com.tacademy.miniproject.request.UploadRequest;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContentAddActivity extends AppCompatActivity {

    @BindView(R.id.edit_content)
    EditText textView;

    @BindView(R.id.image_content)
    ImageView imageView;

    File savedFile = null;
    File uploadFile = null;

    private static final int RC_GET_IMAGE = 1;
    private static final int RC_CAPTURE_IMAGE = 2;

    private static final String SAVED_FILE = "savedfile";
    private static final String UPLOAD_FILE = "uploadfile";

    private static final int INDEX_GALLERY = 0;
    private static final int INDEX_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_add);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            String path = savedInstanceState.getString(SAVED_FILE);

            if (!TextUtils.isEmpty(path))
                savedFile = new File(path);

            path = savedInstanceState.getString(UPLOAD_FILE);

            if (!TextUtils.isEmpty(path)) {
                uploadFile = new File(path);
                Glide.with(this).load(uploadFile).into(imageView);
            }
        }

        checkPermission();
    }

    private static final int RC_PERMISSION = 100;
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // dialog...
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION);
                return false;
            }
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_PERMISSION && permissions != null) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @OnClick(R.id.btn_get_image)
    public void onGetImageClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setItems(R.array.select_image, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case INDEX_GALLERY :
                        getGalleryImage();
                        break;
                    case INDEX_CAMERA :
                        getCapturedImage();
                        break;
                }
            }
        });

        builder.create().show();
    }

    private void getGalleryImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, RC_GET_IMAGE);
    }

    private void getCapturedImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getSaveFile());
        startActivityForResult(intent, RC_CAPTURE_IMAGE);
    }

    private Uri getSaveFile() {
        File dir = getExternalFilesDir("capture");

        if (!dir.exists()) dir.mkdirs();

        savedFile = new File(dir, "my_image_" + System.currentTimeMillis() + ".jpeg");
        return Uri.fromFile(savedFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                Cursor c = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);

                if (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    uploadFile = new File(path);
                    Glide.with(this).load(uploadFile).into(imageView);
                }
            }
        } else if (requestCode == RC_CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uploadFile = savedFile;
                Glide.with(this).load(uploadFile).into(imageView);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (savedFile != null)
            outState.putString(SAVED_FILE, savedFile.getAbsolutePath());

        if (uploadFile != null)
            outState.putString(UPLOAD_FILE, uploadFile.getAbsolutePath());
    }

    @OnClick(R.id.btn_upload)
    public void onUploadClick(View view) {
        String text = textView.getText().toString();

        if (!TextUtils.isEmpty(text)) {
            UploadRequest request = new UploadRequest(this, text, uploadFile);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<UserResult<ContentData>>() {
                @Override
                public void onSuccess(NetworkRequest<UserResult<ContentData>> request, UserResult<ContentData> result) {
                    Toast.makeText(ContentAddActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFail(NetworkRequest<UserResult<ContentData>> request, int errorCode, String errorMessage, Throwable e) {
                    Toast.makeText(ContentAddActivity.this, errorCode + " : " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
