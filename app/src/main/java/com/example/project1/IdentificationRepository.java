package com.example.project1;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class IdentificationRepository {
    private IdentificationDao mIdentificationDao;
    private LiveData<List<Identification>> mAllIdentityCards;

    IdentificationRepository(Application application){
        IdentificationRoomDatabase db = IdentificationRoomDatabase.getDatabase(application);
        mIdentificationDao = db.identityCardDao();
        mAllIdentityCards = mIdentificationDao.getAllIdentification();
    }

    LiveData<List<Identification>> getmAllIdentityCards(){return mAllIdentityCards;}

    public void insert(Identification identification) {
        new insertAsyncTask(mIdentificationDao).execute(identification);
    }

    public void delete(Identification identification) {
        new deleteAsyncTask(mIdentificationDao).execute(identification);
    }

    public void update(Identification identification) {
        new updateAsyncTask(mIdentificationDao).execute(identification);
    }

    private static class insertAsyncTask extends AsyncTask<Identification, Void, Void> {

        private IdentificationDao mAsyncTaskDao;

        insertAsyncTask(IdentificationDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Identification... identifications) {
            mAsyncTaskDao.insert(identifications[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Identification, Void, Void> {

        private IdentificationDao mAsyncTaskDao;

        deleteAsyncTask(IdentificationDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Identification... identifications) {
            mAsyncTaskDao.deleteIdentification(identifications[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Identification, Void, Void> {

        private IdentificationDao mAsyncTaskDao;

        updateAsyncTask(IdentificationDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(Identification... identifications) {
            mAsyncTaskDao.updateIdentification(identifications[0]);
            return null;
        }
    }

}
