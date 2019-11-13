package com.example.project1;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class IdentificationViewModel extends AndroidViewModel {
    private IdentificationRepository mRepository;
    private LiveData<List<Identification>> allIdentityCards;

    public IdentificationViewModel(@NonNull Application application) {
        super(application);
        mRepository = new IdentificationRepository(application);
        allIdentityCards = mRepository.getmAllIdentityCards();
    }

    public LiveData<List<Identification>> getAllIdentityCards() {
        return allIdentityCards;
    }

    public void insert(Identification identification){
        mRepository.insert(identification);
    }

    public void delete(Identification identification){
        mRepository.delete(identification);
    }

    public void update(Identification identification){
        mRepository.update(identification);
    }
}
