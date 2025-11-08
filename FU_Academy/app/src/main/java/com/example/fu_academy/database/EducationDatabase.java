package com.example.fu_academy.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.fu_academy.dao.AssignmentDao;
import com.example.fu_academy.dao.ClassDao;
import com.example.fu_academy.dao.CourseDao;
import com.example.fu_academy.dao.EnrollmentDao;
import com.example.fu_academy.dao.FeedbackDao;
import com.example.fu_academy.dao.MaterialDao;
import com.example.fu_academy.dao.NotificationDao;
import com.example.fu_academy.dao.ScheduleDao;
import com.example.fu_academy.dao.SubmissionDao;
import com.example.fu_academy.dao.UserDao;
import com.example.fu_academy.entity.Assignment;
import com.example.fu_academy.entity.Class;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.Enrollment;
import com.example.fu_academy.entity.Feedback;
import com.example.fu_academy.entity.Material;
import com.example.fu_academy.entity.Notification;
import com.example.fu_academy.entity.Schedule;
import com.example.fu_academy.entity.Submission;
import com.example.fu_academy.entity.User;

@Database(
        entities = {
                User.class, Course.class, Class.class, Enrollment.class,
                Material.class, Assignment.class, Submission.class,
                Notification.class, Feedback.class, Schedule.class
        },
        version = 1,
        exportSchema = false
)
public abstract class EducationDatabase extends RoomDatabase {

        private static volatile EducationDatabase INSTANCE;

        // --- DAO Accessors ---
        public abstract UserDao userDao();
        public abstract CourseDao courseDao();
        public abstract ClassDao classDao();
        public abstract EnrollmentDao enrollmentDao();
        public abstract MaterialDao materialDao();
        public abstract AssignmentDao assignmentDao();
        public abstract SubmissionDao submissionDao();
        public abstract NotificationDao notificationDao();
        public abstract FeedbackDao feedbackDao();
        public abstract ScheduleDao scheduleDao();

        // --- Singleton Builder ---
        public static EducationDatabase getInstance(Context context) {
                if (INSTANCE == null) {
                        synchronized (EducationDatabase.class) {
                                if (INSTANCE == null) {
                                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                                        EducationDatabase.class, "education_db")
                                                .fallbackToDestructiveMigration()
                                                .allowMainThreadQueries() // chỉ dùng demo, nên chuyển sang background sau
                                                .build();
                                }
                        }
                }
                return INSTANCE;
        }
}