package kashyap.anurag.notesapp.ViewModel;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import kashyap.anurag.notesapp.Model.Notes;
import kashyap.anurag.notesapp.Repository.NotesRepository;

public class NotesViewModel extends AndroidViewModel {

    public NotesRepository repository;
    public List<Notes> getallNotes;
    public List<Notes> highToLow;
    public List<Notes> lowToHigh;

    public NotesViewModel(Application application) {
        super(application);

        repository = new NotesRepository(application);
        getallNotes = repository.getallNotes;
        highToLow = repository.highToLow;
        lowToHigh = repository.lowToHigh;

    }
    public void insertNote(Notes notes){
        repository.insertNotes(notes);
    }

    public void deleteNote(int id){
        repository.deleteNotes(id);
    }

    public void updateNote(Notes notes){
        repository.updateNotes(notes);
    }
}
