package kashyap.anurag.notesapp.Adapters;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kashyap.anurag.notesapp.Activities.MainActivity;
import kashyap.anurag.notesapp.Activities.UpdateNotesActivity;
import kashyap.anurag.notesapp.Model.Notes;
import kashyap.anurag.notesapp.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.notesViewHolder> {

    MainActivity mainActivity;
    List<Notes> notes;

    public NotesAdapter(MainActivity mainActivity, List<Notes> notes) {
        this.mainActivity = mainActivity;
        this.notes = notes;

    }

    public void searchNotes(List<Notes> filteredName){
        this.notes = filteredName;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public notesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item_notes, parent, false);
        return new notesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(notesViewHolder holder, int position) {

        Notes note = notes.get(position);

        holder.notesTitle.setText(note.notesTitle);
        holder.notesData.setText(note.notes);
        holder.date.setText(note.notesDate);

        switch (note.notesPriority) {
            case "1":
                holder.notesPriority.setBackgroundResource(R.drawable.green_shape);
                break;
            case "2":
                holder.notesPriority.setBackgroundResource(R.drawable.yellow_shape);
                break;
            case "3":
                holder.notesPriority.setBackgroundResource(R.drawable.red_shape);
                break;
        }
        if (note.getImagePath() != null){
            holder.imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
            holder.imageNote.setVisibility(View.VISIBLE);
        }else {
            holder.imageNote.setVisibility(View.GONE);

        }
        if (note.getWebLink() != null){
            holder.webURL.setVisibility(View.VISIBLE);
            holder.webURL.setText(note.webLink);
        }else {
            holder.webURL.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, UpdateNotesActivity.class);
                intent.putExtra("id", note.id);
                intent.putExtra("title", note.notesTitle);
                intent.putExtra("subTitle", note.notesSubtitle);
                intent.putExtra("priority", note.notesPriority);
                intent.putExtra("notes", note.notes);
                intent.putExtra("webURL", note.webLink);
                intent.putExtra("image", note.imagePath);
                intent.putExtra("note", note);
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class notesViewHolder extends RecyclerView.ViewHolder {

        TextView date, notesData, notesTitle, webURL;
        View notesPriority;
        ImageView imageNote;

        public notesViewHolder(@NonNull View itemView) {
            super(itemView);

            notesTitle = itemView.findViewById(R.id.updateTitle);
            notesData = itemView.findViewById(R.id.updateNotes);
            date = itemView.findViewById(R.id.date);
            notesPriority = itemView.findViewById(R.id.notesPriority);
            imageNote = itemView.findViewById(R.id.imageNote);
            webURL = itemView.findViewById(R.id.webURL);
        }
    }
}
