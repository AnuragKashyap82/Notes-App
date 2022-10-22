package kashyap.anurag.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import kashyap.anurag.notesapp.Model.Notes;
import kashyap.anurag.notesapp.R;
import kashyap.anurag.notesapp.ViewModel.NotesViewModel;
import kashyap.anurag.notesapp.databinding.ActivityUpdateNotesBinding;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;

public class UpdateNotesActivity extends AppCompatActivity {

    private ActivityUpdateNotesBinding binding;
    NotesViewModel notesViewModel;
    String priority = "1";
    String sTitle, sSubTitle, sPriority, sNotes;
    int iid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iid = getIntent().getIntExtra("id", 0);
        sTitle = getIntent().getStringExtra("title");
        sSubTitle = getIntent().getStringExtra("subTitle");
        sPriority = getIntent().getStringExtra("priority");
        sNotes = getIntent().getStringExtra("notes");

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        binding.updateTitle.setText(sTitle);
        binding.updateSubTitle.setText(sSubTitle);
        binding.updateNotes.setText(sNotes);

        if (sPriority.equals("1")) {
            binding.greenPriority.setImageResource(R.drawable.ic_done_black);
        } else if (sPriority.equals("2")) {
            binding.yellowPriority.setImageResource(R.drawable.ic_done_black);
        } else if (sPriority.equals("3")) {
            binding.redPriority.setImageResource(R.drawable.ic_done_black);
        }

        binding.greenPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = "1";
                binding.greenPriority.setImageResource(R.drawable.ic_done_black);
                binding.yellowPriority.setImageResource(0);
                binding.redPriority.setImageResource(0);
            }
        });
        binding.yellowPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = "2";
                binding.yellowPriority.setImageResource(R.drawable.ic_done_black);
                binding.greenPriority.setImageResource(0);
                binding.redPriority.setImageResource(0);
            }
        });
        binding.redPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = "3";
                binding.redPriority.setImageResource(R.drawable.ic_done_black);
                binding.yellowPriority.setImageResource(0);
                binding.greenPriority.setImageResource(0);
            }
        });
        binding.updateNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        String title = binding.updateTitle.getText().toString();
        String subtitle = binding.updateSubTitle.getText().toString();
        String note = binding.updateNotes.getText().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter all the fields!!!", Toast.LENGTH_SHORT).show();
        } else if (subtitle.isEmpty()) {
            Toast.makeText(this, "Enter all the fields!!!", Toast.LENGTH_SHORT).show();
        } else if (note.isEmpty()) {
            Toast.makeText(this, "Enter all the fields!!!", Toast.LENGTH_SHORT).show();
        } else {
            updateNotes(title, subtitle, note);
        }
    }

    private void updateNotes(String title, String subtitle, String note) {

        Date date = new Date();
        CharSequence sequence = DateFormat.format("MMMM d, yyyy", date.getTime());

        Notes updateNotes = new Notes();

        updateNotes.id = iid;
        updateNotes.notesTitle = title;
        updateNotes.notesSubtitle = subtitle;
        updateNotes.notes = note;
        updateNotes.notesPriority = priority;
        updateNotes.notesDate = sequence.toString();

        notesViewModel.updateNote(updateNotes);

        Toast.makeText(this, "Notes Created Successfully!!!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()== R.id.deleteBtn){
            BottomSheetDialog sheetDialog = new BottomSheetDialog(UpdateNotesActivity.this, R.style.BottomSheetStyle);

            View view = LayoutInflater.from(UpdateNotesActivity.this).inflate(R.layout.delete_bottom_sheet, (LinearLayout) findViewById(R.id.bottomSheet));
            sheetDialog.setContentView(view);

            TextView yes, no;

            yes = view.findViewById(R.id.yesBtn);
            no = view.findViewById(R.id.noBtn);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notesViewModel.deleteNote(iid);
                    finish();
                    sheetDialog.dismiss();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sheetDialog.dismiss();
                }
            });

            sheetDialog.show();
        }
        return true;
    }
}