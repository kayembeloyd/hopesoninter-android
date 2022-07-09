package com.loycompany.hopesoninter.fragments.adds;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.loycompany.hopesoninter.AddActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.adapters.recyclerview.AddPostMediaRecyclerViewAdapter;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.Post;
import com.loycompany.hopesoninter.classes.PostMedia;
import com.loycompany.hopesoninter.classes.User;
import com.loycompany.hopesoninter.network.ServerRequest;
import com.loycompany.hopesoninter.network.ServerVolleyMultipartRequest;
import com.loycompany.hopesoninter.network.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddPostFragment extends Fragment {
    LinearLayout addPhotoLinearLayout;

    private Bitmap bitmap;

    private static final int PICK_IMAGE_MULTIPLE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    List<PostMedia> postMedia = new ArrayList<>();
    RecyclerView postMediaRecyclerView;
    AddPostMediaRecyclerViewAdapter addPostMediaRecyclerViewAdapter;

    EditText postTitleEditText, postDescriptionEditText, postDetailedDescriptionEditText;
    Button addPostButton;

    int recentlyPostedPostID;
    private boolean imageUploadServerHasResponded = false;
    private Handler handler;

    public AddPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        handler = new Handler();

        postMediaRecyclerView = view.findViewById(R.id.post_media_recycler_view);

        addPostMediaRecyclerViewAdapter = new AddPostMediaRecyclerViewAdapter(requireContext(), postMedia);

        RecyclerView.LayoutManager communityMediaRecyclerViewLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        postMediaRecyclerView.setLayoutManager(communityMediaRecyclerViewLayoutManager);
        postMediaRecyclerView.setItemAnimator(new DefaultItemAnimator());
        postMediaRecyclerView.setAdapter(addPostMediaRecyclerViewAdapter);

        addPhotoLinearLayout = view.findViewById(R.id.add_photo_linear_layout);

        addPhotoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStorageAccessPermission();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(Intent.createChooser(intent, "Select photos"), PICK_IMAGE_MULTIPLE);
            }
        });

        postTitleEditText = view.findViewById(R.id.post_title_edit_text);
        postDescriptionEditText = view.findViewById(R.id.post_description_edit_text);
        postDetailedDescriptionEditText = view.findViewById(R.id.post_detailed_description_edit_text);
        addPostButton = view.findViewById(R.id.add_post_button);

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postTitle, postDescription, postDetailedDescription;

                postTitle = postTitleEditText.getText().toString();

                if (postTitle.equals("")){
                    postTitleEditText.setError("Please enter title");
                    return;
                }

                postDescription = postDescriptionEditText.getText().toString();
                postDetailedDescription = postDetailedDescriptionEditText.getText().toString();

                ServerRequest serverRequest = new ServerRequest(requireContext()) {
                    @Override
                    public void onServerResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("status")){
                                if (jsonObject.getString("status").equals("success")){
                                    // Well done

                                    Post post = new Post(jsonObject.getJSONObject("post"));

                                    // Start image upload
                                    recentlyPostedPostID = post.id;

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i = 0; i < postMedia.size(); i++){
                                                int finalI = i;

                                                // NEW
                                                ServerVolleyMultipartRequest serverRequestTemp = new ServerVolleyMultipartRequest(requireContext()) {
                                                    @Override
                                                    public void onServerResponse(String response) {
                                                        imageUploadServerHasResponded = true;
                                                        handler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(requireContext(), "What is happening?!!!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onServerError(String error) {
                                                        imageUploadServerHasResponded = true;
                                                        handler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(requireContext(), "What is happening?!!! - error = " + error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                };


                                                serverRequestTemp.addHeaderParam("Accept", "application/json");
                                                serverRequestTemp.addHeaderParam("Authorization",
                                                        "Bearer " + Objects.requireNonNull(
                                                                Authenticator.getAuthUser(requireContext())).getToken());

                                                try {
                                                    bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(),
                                                            postMedia.get(i).getUri());

                                                    Bitmap scaledBitmap = scaleDownBitmap(bitmap);
                                                    bitmap = scaledBitmap;

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                serverRequestTemp.addParam("media", getStringImage(bitmap));
                                                serverRequestTemp.addParam("position", String.valueOf(i));
                                                serverRequestTemp.addParam("name", "post_media");

                                                serverRequestTemp.sendRequest(URLs.getApiAddress() +
                                                        "/posts/" + String.valueOf(recentlyPostedPostID) + "/media");
                                                // NEW

                                                while(!imageUploadServerHasResponded){
                                                    // loop basi
                                                    try {
                                                        Thread.sleep(1000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                imageUploadServerHasResponded = false;

                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(requireContext(), "Image " + String.valueOf(finalI) + " has been uploaded", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ((AddActivity) requireContext()).onBackPressed();
                                                }
                                            });
                                        }
                                    }).start();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onServerError(String error) {
                        String errCopy = error;

                        int f = 0;
                        f++;
                    }
                };

                serverRequest.addHeaderParam("Accept", "application/json");
                User user = Authenticator.getAuthUser(requireContext());
                serverRequest.addHeaderParam("Authorization", "Bearer " + user.getToken());

                serverRequest.addParam("title", postTitle);
                serverRequest.addParam("short_description", postDescription);
                serverRequest.addParam("long_description", postDetailedDescription);
                serverRequest.addParam("community_id", String.valueOf(((AddActivity) requireContext()).getSelectedCommunityID()));

                serverRequest.sendRequest(URLs.getApiAddress() + "/posts", Request.Method.POST);
            }
        });


        return view;
    }


    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        String encodedString = Base64.encodeToString(byte_arr, 0);
        return encodedString;
    }

    public byte[] getStringImage2(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        String encodedString = Base64.encodeToString(byte_arr, 0);
        return encodedString.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        System.out.println("Bitmap");
        System.out.println("Size : " + String.valueOf(bitmap.getHeight() * bitmap.getWidth()));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public Bitmap scaleDownBitmap(Bitmap bitmap){
        final int REQUIRED_SIZE = 1024;

        int iWidth = bitmap.getWidth(), iHeight = bitmap.getHeight();
        int width = iWidth, height = iHeight;
        int scale = 1;

        while(true){
            if (iWidth <= REQUIRED_SIZE && iHeight <= REQUIRED_SIZE){
                break;
            }

            iWidth /= 2;
            iHeight /= 2;
            scale *= 2;
        }

        Matrix matrix = new Matrix();

        matrix.postScale(1.0f/scale, 1.0f/scale);

        // bitmap.recycle();

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }

    public Bitmap decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap image = BitmapFactory.decodeFile(filePath, o2);

        ExifInterface exif;
        try
        {
            exif = new ExifInterface(filePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int rotate = 0;
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            if (rotate != 0) {
                int w = image.getWidth();
                int h = image.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap & convert to ARGB_8888, required by tess // who is tess?
                image = Bitmap.createBitmap(image, 0, 0, w, h, mtx, false);

            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return image.copy(Bitmap.Config.ARGB_8888, true);
    }

    private void verifyStorageAccessPermission() {
        int permission = ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data){
            if (data.getClipData() != null){
                ClipData clipData = data.getClipData();
                int count = clipData.getItemCount();
                for(int i = 0; i < count; i++){

                    PostMedia postMedia = new PostMedia();
                    Uri d = clipData.getItemAt(i).getUri();
                    postMedia.setUri(clipData.getItemAt(i).getUri());
                    this.postMedia.add(postMedia);
                    addPostMediaRecyclerViewAdapter.notifyItemInserted(this.postMedia.size() - 1);
                    // Image Setting
                }
            } else {
                PostMedia postMedia = new PostMedia();
                postMedia.setUri(data.getData());
                this.postMedia.add(postMedia);
                addPostMediaRecyclerViewAdapter.notifyItemInserted(this.postMedia.size() - 1);
            }

            // Last Resort
            // postImagesRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(requireContext(), "You haven't selected any image", Toast.LENGTH_SHORT).show();
        }
    }
}