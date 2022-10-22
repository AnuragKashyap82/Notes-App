package kashyap.anurag.notesapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import kashyap.anurag.notesapp.Dao.NotesDao;
import kashyap.anurag.notesapp.Model.Notes;

@Database(entities = {Notes.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {

    public abstract NotesDao notesDao();
    public static NotesDatabase INSTANCE;

    public static NotesDatabase getDatabaseInstance(Context context){

        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NotesDatabase.class,
                    "Notes_Database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
