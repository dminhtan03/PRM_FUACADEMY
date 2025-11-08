package com.example.fu_academy.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Feedback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedbackFormViewModel extends AndroidViewModel {
    private final EducationDatabase db;
    private final MutableLiveData<List<Feedback>> feedbackList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> submitResult = new MutableLiveData<>();

    public FeedbackFormViewModel(Application application) {
        super(application);
        db = EducationDatabase.getInstance(application);
    }

    public void submitFeedback(long userId, long studentId, String subject, 
                               String content, String category, int rating) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(new Date());
        
        Feedback feedback = new Feedback();
        feedback.user_id = userId;
        feedback.student_id = studentId;
        feedback.subject = subject;
        feedback.content = content;
        feedback.category = category;
        feedback.date = date;
        feedback.rating = rating;
        feedback.status = "pending";
        feedback.response = "";
        
        long id = db.feedbackDao().insert(feedback);
        submitResult.postValue(id > 0);
        
        // Reload feedback list
        loadFeedbackList(studentId);
    }

    public void loadFeedbackList(long studentId) {
        List<Feedback> feedbacks = db.feedbackDao().getByStudent(studentId);
        feedbackList.postValue(feedbacks);
    }

    public LiveData<List<Feedback>> getFeedbackList() {
        return feedbackList;
    }

    public LiveData<Boolean> getSubmitResult() {
        return submitResult;
    }
}

