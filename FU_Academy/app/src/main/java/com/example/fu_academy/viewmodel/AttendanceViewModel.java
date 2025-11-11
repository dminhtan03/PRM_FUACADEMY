package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.AttendanceDetail;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AttendanceViewModel extends AndroidViewModel {
    private EducationDatabase database;
    private ExecutorService executorService;

    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<String> successMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<AttendanceDetail>> attendanceList = new MutableLiveData<>();

    public AttendanceViewModel(@NonNull Application application) {
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

    public LiveData<List<AttendanceDetail>> getAttendanceList() {
        return attendanceList;
    }

    public void saveAttendance(List<AttendanceDetail> attendanceDetails) {
        isLoading.postValue(true);

        executorService.execute(() -> {
            try {
                for (AttendanceDetail attendance : attendanceDetails) {
                    // Check if attendance already exists for this student and date
                    List<AttendanceDetail> existing = database.attendanceDetailDao()
                        .getByStudentAndDate(attendance.student_id, attendance.date);

                    if (existing.isEmpty()) {
                        database.attendanceDetailDao().insert(attendance);
                    } else {
                        // Update existing attendance
                        attendance.attendance_id = existing.get(0).attendance_id;
                        database.attendanceDetailDao().update(attendance);
                    }
                }

                successMessage.postValue("Attendance saved successfully");
                isLoading.postValue(false);

            } catch (Exception e) {
                errorMessage.postValue("Failed to save attendance: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void loadAttendanceByClassAndDate(long classId, String date) {
        isLoading.postValue(true);

        executorService.execute(() -> {
            try {
                List<AttendanceDetail> attendance = database.attendanceDetailDao()
                    .getByClassAndDate(classId, date);

                attendanceList.postValue(attendance);
                isLoading.postValue(false);

            } catch (Exception e) {
                errorMessage.postValue("Failed to load attendance: " + e.getMessage());
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
