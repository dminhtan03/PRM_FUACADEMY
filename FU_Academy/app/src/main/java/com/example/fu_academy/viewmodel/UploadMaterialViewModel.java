package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Material;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadMaterialViewModel extends AndroidViewModel {
    private EducationDatabase database;
    private ExecutorService executorService;

    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<String> successMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public UploadMaterialViewModel(@NonNull Application application) {
        super(application);
        database = EducationDatabase.getInstance(application);
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void uploadMaterial(Material material) {
        isLoading.postValue(true);

        executorService.execute(() -> {
            try {
                long materialId = database.materialDao().insert(material);

                if (materialId > 0) {
                    successMessage.postValue("Material uploaded successfully");
                } else {
                    errorMessage.postValue("Failed to upload material");
                }

                isLoading.postValue(false);

            } catch (Exception e) {
                errorMessage.postValue("Failed to upload material: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
