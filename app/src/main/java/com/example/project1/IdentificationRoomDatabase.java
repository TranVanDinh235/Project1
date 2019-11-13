package com.example.project1;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Identification.class}, version = 1, exportSchema = false)
public abstract class IdentificationRoomDatabase extends RoomDatabase {
    public abstract IdentificationDao identityCardDao();

    private static IdentificationRoomDatabase INSTANCE;

    public static IdentificationRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (IdentificationRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            IdentificationRoomDatabase.class, "identification")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    // This callback is called when the database has opened.
    // In this case, use PopulateDbAsync to populate the database
    // with the initial data set if the database has no entries.
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    // Populate the database with the initial data set
    // only if the database has no entries.
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final IdentificationDao mDao;

        // Initial data set
        private static Identification identification = new Identification("Chứng minh nhân dân", "012344566", "TRAN VAN DINH", "1/1/1998", "Nam", "Hà Nội", "1/1/2014", "Ha Noi", null, null);

        PopulateDbAsync(IdentificationRoomDatabase db) {
            mDao = db.identityCardDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // If we have no words, then create the initial list of words
            if (mDao.getAnyIdentification().length < 1) {
                mDao.insert(identification);
            }
            return null;
        }
    }
}
