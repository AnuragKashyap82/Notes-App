package kashyap.anurag.notesapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import kashyap.anurag.notesapp.Model.Notes;
import kashyap.anurag.notesapp.R;
import kashyap.anurag.notesapp.ViewModel.NotesViewModel;
import kashyap.anurag.notesapp.databinding.ActivityInsertNotesBinding;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import java.util.Date;


public class InsertNotesActivity extends AppCompatActivity {

    ActivityInsertNotesBinding binding;
    String title, subtitle, note;
    NotesViewModel notesViewModel;
    String priority = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

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

        binding.doneNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
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
        notes1.notesTitle = title;
        notes1.notesSubtitle = subtitle;
        notes1.notes = note;
        notes1.notesPriority = priority;
        notes1.notesDate = sequence.toString();

        notesViewModel.insertNote(notes1);

        Toast.makeText(this, "Notes Created Successfully!!!", Toast.LENGTH_SHORT).show();

        finish();
    }

}