package com.example.jome17wave.jome_group;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.R;

import java.io.IOException;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class TestCreateFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private static final int PER_EXTERNAL_STORAGE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private MainActivity activity;
    private ImageView ivPicture;
    private ImageButton btPickPicture;
    private ImageView Title;
    private EditText editTitle;
    private ImageView Date;
    private EditText editDate;
    private ImageView Time;
    private EditText editTime;
    private ImageView Location;
    private TextView tvLocation;
    private Spinner LocationChoice;
    private ImageView Gender;
    private TextView tvGender;
    private Spinner GenderChoice;
    private TextView tvPersonnum;
    private TextView edMemo;
    private Button bt_add;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        editTitle = activity.findViewById(R.id.title);
        editDate = activity.findViewById(R.id.Date);
        editDate =activity.findViewById(R.id.Date);
        editDate = activity.findViewById(R.id.Date);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivPicture = view.findViewById(R.id.ivPicture);
        btPickPicture = view.findViewById(R.id.btPickPicture);
        btPickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(intent, REQ_PICK_PICTURE);
                } else {
                    Toast.makeText(activity, "no image pick found",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            Uri uri = intent.getData();
            if (uri != null) {
                switch (requestCode) {
                    case REQ_PICK_PICTURE:

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                            try {
                                Bitmap bitmap = BitmapFactory.decodeStream(
                                        activity.getContentResolver().openInputStream(uri));
                                ivPicture.setImageBitmap(bitmap);
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                String text = String.format(Locale.getDefault(),
                                        "%nsource image size = %d x %d", width, height);

                            } catch (IOException e) {
                                Log.e(TAG, e.toString());
                            }
                        } else {
                            ImageDecoder.OnHeaderDecodedListener listener = new ImageDecoder.OnHeaderDecodedListener() {
                                public void onHeaderDecoded(ImageDecoder decoder, ImageDecoder.ImageInfo info, ImageDecoder.Source source) {
                                    String mimeType = info.getMimeType();
                                    int width = info.getSize().getWidth();
                                    int height = info.getSize().getHeight();
                                    String text = String.format(Locale.getDefault(),
                                            "%nmime type: %s; source image size = %d x %d",
                                            mimeType, width, height);

                                }
                            };
                            ImageDecoder.Source source = ImageDecoder.createSource(activity.getContentResolver(), uri);
                            try {
                                Bitmap bitmap = ImageDecoder.decodeBitmap(source, listener);
                                ivPicture.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        askExternalStoragePermission();
    }

    private void askExternalStoragePermission() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        int result = ContextCompat.checkSelfPermission(activity, permissions[0]);
        if (result == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, PER_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @androidx.annotation.NonNull String[] permissions,
                                           @androidx.annotation.NonNull int[] grantResults) {
        if (requestCode == PER_EXTERNAL_STORAGE) {
            // 如果user不同意將資料儲存至外部儲存體的公開檔案，就將儲存按鈕設為disable
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                Toast.makeText(activity, R.string.textShouldGrant, Toast.LENGTH_SHORT).show();
                btPickPicture.setEnabled(false);
            } else {
                btPickPicture.setEnabled(true);
            }
        }
    }
}



