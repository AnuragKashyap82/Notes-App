package kashyap.anurag.notesapp.Dao;

import java.util.List;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import kashyap.anurag.notesapp.Model.Notes;

@androidx.room.Dao
public interface NotesDao {

    @Query("SELECT * FROM Notes_Database")
    List<Notes> getallNotes();

    @Query("SELECT * FROM Notes_Database ORDER BY notes_priority DESC")
    List<Notes> highToLow();

    @Query("SELECT * FROM Notes_Database ORDER BY notes_priority ASC")
    List<Notes> lowToHigh();

    @Insert
    void insertNotes(Notes notes);

    @Query("DELETE FROM Notes_Database WHERE id=:id")
    void deleteNotes(int id);

    @Update
    void updateNotes(Notes notes);
}
