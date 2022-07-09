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

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.loycompany.hopesoninter.AddActivity;
import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.adapters.recyclerview.AddCommunityMediaRecyclerViewAdapter;
import com.loycompany.hopesoninter.authentication.Authenticator;
import com.loycompany.hopesoninter.classes.CommunityMedia;
import com.loycompany.hopesoninter.classes.User;
import com.loycompany.hopesoninter.network.ServerRequest;
import com.loycompany.hopesoninter.network.ServerVolleyMultipartRequest;
import com.loycompany.hopesoninter.network.URLs;
import com.loycompany.hopesoninter.network.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddCommunityFragment extends Fragment {

    LinearLayout addPhotoLinearLayout;

    Button addCommunityButton;
    EditText communityNameEditText, locationEditText;

    Handler handler;

    int recentlyPostedCommunityID;
    private Bitmap bitmap;

    private static final int PICK_IMAGE_MULTIPLE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    List<CommunityMedia> communityMedia = new ArrayList<>();
    RecyclerView communityMediaRecyclerView;
    AddCommunityMediaRecyclerViewAdapter addCommunityMediaRecyclerViewAdapter;

    private boolean imageUploadServerHasResponded = false;

    public AddCommunityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_community, container, false);

        handler = new Handler();

        communityMediaRecyclerView = view.findViewById(R.id.community_media_recycler_view);

        addCommunityMediaRecyclerViewAdapter = new AddCommunityMediaRecyclerViewAdapter(requireContext(), communityMedia);

        RecyclerView.LayoutManager communityMediaRecyclerViewLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false);

        communityMediaRecyclerView.setLayoutManager(communityMediaRecyclerViewLayoutManager);
        communityMediaRecyclerView.setItemAnimator(new DefaultItemAnimator());
        communityMediaRecyclerView.setAdapter(addCommunityMediaRecyclerViewAdapter);

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

        communityNameEditText = view.findViewById(R.id.community_name_edit_text);
        locationEditText = view.findViewById(R.id.location_edit_text);

        addCommunityButton = view.findViewById(R.id.add_community_button);

        addCommunityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String communityName, location;

                communityName = communityNameEditText.getText().toString();

                if (communityName.equals("")) {
                    communityNameEditText.setError(getString(R.string.enter_community_name));
                    return;
                }

                location = locationEditText.getText().toString();
                if (location.equals("")){
                    locationEditText.setError(getString(R.string.enter_community_location));
                    return;
                }

                // Start upload
                ServerRequest serverRequest = new ServerRequest(requireContext()) {
                    @Override
                    public void onServerResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("status")){
                                if (jsonObject.getString("status").equals("success")){
                                    // Creation successful
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(requireContext(), "Community Created successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    // Start image upload
                                    recentlyPostedCommunityID = jsonObject.getJSONObject("community").getInt("id");

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i = 0; i < communityMedia.size(); i++){
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
                                                            communityMedia.get(i).getUri());

                                                    Bitmap scaledBitmap = scaleDownBitmap(bitmap);
                                                    bitmap = scaledBitmap;

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                serverRequestTemp.addParam("media", getStringImage(bitmap));
                                                serverRequestTemp.addParam("position", String.valueOf(i));
                                                serverRequestTemp.addParam("name", "community_media");

                                                serverRequestTemp.sendRequest(URLs.getApiAddress() +
                                                        "/communities/" + String.valueOf(recentlyPostedCommunityID) + "/media");
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
                            Toast.makeText(requireContext(), "JSON Error :" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onServerError(String error) {
                        Toast.makeText(requireContext(), "Error" + error, Toast.LENGTH_SHORT).show();
                    }
                };

                serverRequest.addHeaderParam("Accept", "application/json");
                User user = Authenticator.getAuthUser(requireContext());
                serverRequest.addHeaderParam("Authorization", "Bearer " + user.getToken());

                serverRequest.addParam("name", communityName);
                serverRequest.addParam("location", location);
                serverRequest.sendRequest(URLs.getApiAddress() + "/communities", Request.Method.POST);
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

                    CommunityMedia communityMedia = new CommunityMedia();
                    Uri d = clipData.getItemAt(i).getUri();
                    communityMedia.setUri(clipData.getItemAt(i).getUri());
                    this.communityMedia.add(communityMedia);
                    addCommunityMediaRecyclerViewAdapter.notifyItemInserted(this.communityMedia.size() - 1);
                    // Image Setting
                }
            } else {
                CommunityMedia communityMedia = new CommunityMedia();
                communityMedia.setUri(data.getData());
                this.communityMedia.add(communityMedia);
                addCommunityMediaRecyclerViewAdapter.notifyItemInserted(this.communityMedia.size() - 1);
            }

            // Last Resort
            // postImagesRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(requireContext(), "You haven't selected any image", Toast.LENGTH_SHORT).show();
        }
    }
}