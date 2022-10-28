package kashyap.anurag.notesapp.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import kashyap.anurag.notesapp.Database.NotesDatabase;
import kashyap.anurag.notesapp.Model.Notes;
import kashyap.anurag.notesapp.R;
import kashyap.anurag.notesapp.ViewModel.NotesViewModel;
import kashyap.anurag.notesapp.databinding.ActivityInsertNotesBinding;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;


public class InsertNotesActivity extends AppCompatActivity {

    private ActivityInsertNotesBinding binding;
    private String title, subtitle, note;
    private String priorityColor;
    private String priority = "1";
    private ImageView greenPriority, yellowPriority, redPriority;
    private static final int STORAGE_REQUEST_CODE = 300;

    private String[] storagePermission;

    private Uri image_uri = null;
    private String selectedImagePath;
    private AlertDialog dialogAddURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        greenPriority = findViewById(R.id.greenPriority);
        yellowPriority = findViewById(R.id.yellowPriority);
        redPriority = findViewById(R.id.redPriority);

        Date date = new Date();
        CharSequence sequence = DateFormat.format("MMMM d, yyyy", date.getTime());
        binding.textDateTime.setText(sequence);

        priorityColor = "#11D99B";
        priority = "1";
        greenPriority.setImageResource(R.drawable.ic_done_black);
        yellowPriority.setImageResource(0);
        redPriority.setImageResource(0);
        setSubtitleIndicator();

        greenPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priorityColor = "#11D99B";
                priority = "1";
                greenPriority.setImageResource(R.drawable.ic_done_black);
                yellowPriority.setImageResource(0);
                redPriority.setImageResource(0);
                setSubtitleIndicator();
                Toast.makeText(InsertNotesActivity.this, "green", Toast.LENGTH_SHORT).show();
            }
        });
        yellowPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priorityColor = "#FDBE3B";
                priority = "2";
                yellowPriority.setImageResource(R.drawable.ic_done_black);
                greenPriority.setImageResource(0);
                redPriority.setImageResource(0);
                setSubtitleIndicator();
                Toast.makeText(InsertNotesActivity.this, "yellow", Toast.LENGTH_SHORT).show();
            }
        });
        redPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priorityColor = "#FF5151";
                priority = "3";
                redPriority.setImageResource(R.drawable.ic_done_black);
                yellowPriority.setImageResource(0);
                greenPriority.setImageResource(0);
                setSubtitleIndicator();
                Toast.makeText(InsertNotesActivity.this, "red", Toast.LENGTH_SHORT).show();
            }
        });

        binding.doneNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        selectedImagePath = "";
        initMiscellaneous();
        binding.notesTitle.requestFocus();

        binding.removeUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textWebURL.setText(null);
                binding.layoutWebUrl.setVisibility(View.GONE);
            }
        });
        binding.removeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageNote.setImageBitmap(null);
                binding.imageNote.setVisibility(View.GONE);
                binding.removeImageBtn.setVisibility(View.GONE);
                selectedImagePath = "";
            }
        });
    }

    private void validateData() {
        title = binding.notesTitle.getText().toString();
        subtitle = binding.notedSubTitle.getText().toString();
        note = binding.notesData.getText().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter all the fields!!!", Toast.LENGTH_SHORT).show();
        } else if (subtitle.isEmpty()) {
            Toast.makeText(this, "Enter all the fields!!!", Toast.LENGTH_SHORT).show();
        } else if (note.isEmpty()) {
            Toast.makeText(this, "Enter all the fields!!!", Toast.LENGTH_SHORT).show();
        } else {
            createNotes(title, subtitle, note);
        }
    }

    private void createNotes(String title, String subtitle, String note) {

        Date date = new Date();
        CharSequence sequence = DateFormat.format("MMMM d, yyyy", date.getTime());

        Notes notes1 = new Notes();
        notes1.setNotesTitle(title);
        notes1.setNotesSubtitle(subtitle);
        notes1.setNotes(note);
        notes1.setNotesPriority(priority);
        notes1.setNotesDate(sequence.toString());
        notes1.setImagePath(selectedImagePath);

        if (binding.layoutWebUrl.getVisibility() == View.VISIBLE){
            notes1.setWebLink(binding.textWebURL.getText().toString().trim());
        }

        @SuppressLint("StaticFieldLeak")
        class saveNoteTask extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabaseInstance(getApplicationContext()).notesDao().insertNotes(notes1);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Toast.makeText(InsertNotesActivity.this, "Notes Created Successfully!!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(InsertNotesActivity.this, MainActivity.class));
                finishAffinity();
                Toast.makeText(InsertNotesActivity.this, ""+selectedImagePath, Toast.LENGTH_SHORT).show();
            }
        }

        new saveNoteTask().execute();

    }

    private void initMiscellaneous() {
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (checkStoragePermission()) {
                    pickImageGallery();
                } else {
                    requestStoragePermission();
                }
            }
        });
        layoutMiscellaneous.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showAddURLDialog();
            }
        });
    }

    private void pickImageGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        image_uri = data.getData();
                        if (image_uri != null) {

                            try {
                                InputStream inputStream = getContentResolver().openInputStream(image_uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                binding.imageNote.setImageBitmap(bitmap);
                                binding.imageNote.setVisibility(View.VISIBLE);
                                binding.removeImageBtn.setVisibility(View.VISIBLE);

                                selectedImagePath = getPathFromUrl(image_uri);
                                Toast.makeText(InsertNotesActivity.this, ""+selectedImagePath, Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                Toast.makeText(InsertNotesActivity.this, "Exception"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }


                    } else {
                        Toast.makeText(InsertNotesActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private String getPathFromUrl(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        Toast.makeText(this, "Storage Permission granted", Toast.LENGTH_SHORT).show();
                        pickImageGallery();
                    } else {
                        Toast.makeText(this, "Storage permission is necessary...!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void setSubtitleIndicator() {
        GradientDrawable gradientDrawable = (GradientDrawable) binding.indicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(priorityColor));
    }

    private void showAddURLDialog() {

        if (dialogAddURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(InsertNotesActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_url, (ViewGroup) findViewById(R.id.layoutAddUrlContainer));

            builder.setView(view);

            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null) {
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputURL = view.findViewById(R.id.inputURL);
            inputURL.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputURL.getText().toString().trim().isEmpty()) {
                        Toast.makeText(InsertNotesActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                    } else if (!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()) {
                        Toast.makeText(InsertNotesActivity.this, "Enter Valid URL", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.textWebURL.setText(inputURL.getText().toString().trim());
                        binding.layoutWebUrl.setVisibility(View.VISIBLE);
                        dialogAddURL.dismiss();
                    }
                }
            });
            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL.dismiss();
                }
            });
        }
        dialogAddURL.show();
    }
}