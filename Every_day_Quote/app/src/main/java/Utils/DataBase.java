package Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "FavouriteQuote";
    private static final String TODO_TABLE = "fav";
    private static final String QUOTE = "task";

    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + QUOTE + " TEXT)";

    public DataBase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade as needed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public void insertTask(String task) {
        if (!isTaskExists(task)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(QUOTE, task);
            db.insert(TODO_TABLE, null, cv);
            db.close(); // Closing the database after the operation
        }
    }
//understand Gin
    private boolean isTaskExists(String task) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;
        try {
            String query = "SELECT 1 FROM " + TODO_TABLE + " WHERE " + QUOTE + " = ?";
            cursor = db.rawQuery(query, new String[]{task});
            exists = (cursor.getCount() > 0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close(); // Closing the database after the operation
        }
        return exists;
    }
    public List<String> getAllTasks() {
        List<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = null;
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null);
            if (cur != null && cur.moveToFirst()) {
                do {
                    String task = cur.getString(cur.getColumnIndexOrThrow(QUOTE));
                    taskList.add(task);
                } while (cur.moveToNext());
            }
        } finally {
            if (cur != null) {
                cur.close();
            }
            db.close(); // Closing the database after the operation
        }
        return taskList;
    }
    public void deleteTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODO_TABLE, QUOTE + "=?", new String[]{task});
        db.close();
    }
}
