package com.example.fu_academy.database;

import android.content.Context;
import com.example.fu_academy.entity.AttendanceDetail;
import com.example.fu_academy.entity.Enrollment;
import com.example.fu_academy.entity.Exam;
import com.example.fu_academy.entity.Feedback;
import com.example.fu_academy.entity.Notification;
import com.example.fu_academy.entity.Schedule;
import com.example.fu_academy.entity.User;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.Class;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatabaseInitializer {
    private static boolean initialized = false;

    public static void initializeDatabase(Context context) {
        if (initialized) {
            return;
        }

        try {
            EducationDatabase db = EducationDatabase.getInstance(context);

            // Check if data already exists
            if (db.userDao().getAllUsers().size() > 0) {
                initialized = true;
                return;
            }

            // Insert sample users
            insertSampleUsers(db);

            // Insert sample courses
            insertSampleCourses(db);

            // Insert sample classes
            insertSampleClasses(db);

            // Insert sample enrollments
            insertSampleEnrollments(db);

            // Insert sample schedules
            insertSampleSchedules(db);

            // Insert sample exams
            insertSampleExams(db);

            // Insert sample attendance (after schedules are created)
            insertSampleAttendance(db);

            // Insert sample notifications
            insertSampleNotifications(db);

            // Insert sample feedback
            insertSampleFeedback(db);

            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertSampleUsers(EducationDatabase db) {
        // Student
        User student = new User();
        student.user_id = 1;
        student.name = "Nguyễn Văn A";
        student.email = "student@fu.edu.vn";
        student.password = "123456";
        student.role = "student";
        student.phone = "0123456789";
        student.status = "active";
        db.userDao().insertUser(student);

        // Lecturers
        User lecturer1 = new User();
        lecturer1.user_id = 2;
        lecturer1.name = "Thầy Nguyễn Văn B";
        lecturer1.email = "lecturer1@fu.edu.vn";
        lecturer1.password = "123456";
        lecturer1.role = "lecturer";
        lecturer1.phone = "0987654321";
        lecturer1.status = "active";
        db.userDao().insertUser(lecturer1);

        User lecturer2 = new User();
        lecturer2.user_id = 3;
        lecturer2.name = "Cô Trần Thị C";
        lecturer2.email = "lecturer2@fu.edu.vn";
        lecturer2.password = "123456";
        lecturer2.role = "lecturer";
        lecturer2.phone = "0912345678";
        lecturer2.status = "active";
        db.userDao().insertUser(lecturer2);
    }

    private static void insertSampleCourses(EducationDatabase db) {
        Course course1 = new Course();
        course1.course_id = 1;
        course1.name = "Lập Trình Java";
        course1.lecturer_id = 2;
        course1.credit = 3;
        course1.semester = "2024-2025 HK1";
        course1.status = "active";
        db.courseDao().insert(course1);

        Course course2 = new Course();
        course2.course_id = 2;
        course2.name = "Cơ Sở Dữ Liệu";
        course2.lecturer_id = 3;
        course2.credit = 3;
        course2.semester = "2024-2025 HK1";
        course2.status = "active";
        db.courseDao().insert(course2);

        Course course3 = new Course();
        course3.course_id = 3;
        course3.name = "Mạng Máy Tính";
        course3.lecturer_id = 2;
        course3.credit = 2;
        course3.semester = "2024-2025 HK1";
        course3.status = "active";
        db.courseDao().insert(course3);
    }

    private static void insertSampleClasses(EducationDatabase db) {
        com.example.fu_academy.entity.Class class1 = new com.example.fu_academy.entity.Class();
        class1.class_id = 1;
        class1.course_id = 1;
        class1.lecturer_id = 2;
        class1.room = "A101";
        class1.schedule = "Thứ 2, 7:00-9:00";
        class1.semester = "2024-2025 HK1";
        db.classDao().insert(class1);

        com.example.fu_academy.entity.Class class2 = new com.example.fu_academy.entity.Class();
        class2.class_id = 2;
        class2.course_id = 2;
        class2.lecturer_id = 3;
        class2.room = "B202";
        class2.schedule = "Thứ 4, 9:00-11:00";
        class2.semester = "2024-2025 HK1";
        db.classDao().insert(class2);

        com.example.fu_academy.entity.Class class3 = new com.example.fu_academy.entity.Class();
        class3.class_id = 3;
        class3.course_id = 3;
        class3.lecturer_id = 2;
        class3.room = "C303";
        class3.schedule = "Thứ 6, 13:00-15:00";
        class3.semester = "2024-2025 HK1";
        db.classDao().insert(class3);
    }

    private static void insertSampleEnrollments(EducationDatabase db) {
        Enrollment enrollment1 = new Enrollment();
        enrollment1.student_id = 1;
        enrollment1.class_id = 1;
        enrollment1.grade = 8.5;
        enrollment1.attendance = 85;
        db.enrollmentDao().insert(enrollment1);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.student_id = 1;
        enrollment2.class_id = 2;
        enrollment2.grade = 7.8;
        enrollment2.attendance = 90;
        db.enrollmentDao().insert(enrollment2);

        Enrollment enrollment3 = new Enrollment();
        enrollment3.student_id = 1;
        enrollment3.class_id = 3;
        enrollment3.grade = 9.0;
        enrollment3.attendance = 95;
        db.enrollmentDao().insert(enrollment3);
    }

    private static void insertSampleSchedules(EducationDatabase db) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        
        // Today's schedule
        Schedule schedule1 = new Schedule();
        schedule1.class_id = 1;
        schedule1.date = sdf.format(cal.getTime());
        schedule1.time = "07:00-09:00";
        schedule1.room = "A101";
        schedule1.type = "lecture";
        schedule1.status = "scheduled";
        db.scheduleDao().insert(schedule1);

        // Tomorrow's schedule
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Schedule schedule2 = new Schedule();
        schedule2.class_id = 2;
        schedule2.date = sdf.format(cal.getTime());
        schedule2.time = "09:00-11:00";
        schedule2.room = "B202";
        schedule2.type = "lecture";
        schedule2.status = "scheduled";
        db.scheduleDao().insert(schedule2);

        // Next week
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Schedule schedule3 = new Schedule();
        schedule3.class_id = 3;
        schedule3.date = sdf.format(cal.getTime());
        schedule3.time = "13:00-15:00";
        schedule3.room = "C303";
        schedule3.type = "lab";
        schedule3.status = "scheduled";
        db.scheduleDao().insert(schedule3);
    }

    private static void insertSampleExams(EducationDatabase db) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);

        Exam exam1 = new Exam();
        exam1.course_id = 1;
        exam1.date = sdf.format(cal.getTime());
        exam1.time = "08:00";
        exam1.room = "A101";
        exam1.seat = "A15";
        exam1.duration = 90;
        exam1.type = "midterm";
        exam1.status = "scheduled";
        db.examDao().insert(exam1);

        cal.add(Calendar.DAY_OF_MONTH, 3);
        Exam exam2 = new Exam();
        exam2.course_id = 2;
        exam2.date = sdf.format(cal.getTime());
        exam2.time = "09:00";
        exam2.room = "B202";
        exam2.seat = "B20";
        exam2.duration = 120;
        exam2.type = "midterm";
        exam2.status = "scheduled";
        db.examDao().insert(exam2);
    }

    private static void insertSampleAttendance(EducationDatabase db) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);

        // Get all schedules and use their IDs
        java.util.List<Schedule> schedules = db.scheduleDao().getAll();
        if (schedules != null && !schedules.isEmpty()) {
            long scheduleId1 = schedules.get(0).id;
            long scheduleId2 = schedules.size() > 1 ? schedules.get(1).id : scheduleId1;

            AttendanceDetail att1 = new AttendanceDetail();
            att1.student_id = 1;
            att1.schedule_id = scheduleId1;
            att1.date = sdf.format(cal.getTime());
            att1.status = "present";
            att1.remark = "Có mặt đầy đủ";
            att1.duration = 120;
            att1.type = "lecture";
            db.attendanceDetailDao().insert(att1);

            cal.add(Calendar.DAY_OF_MONTH, 2);
            AttendanceDetail att2 = new AttendanceDetail();
            att2.student_id = 1;
            att2.schedule_id = scheduleId2;
            att2.date = sdf.format(cal.getTime());
            att2.status = "present";
            att2.remark = "Tham gia tích cực";
            att2.duration = 120;
            att2.type = "lecture";
            db.attendanceDetailDao().insert(att2);

            cal.add(Calendar.DAY_OF_MONTH, 2);
            AttendanceDetail att3 = new AttendanceDetail();
            att3.student_id = 1;
            att3.schedule_id = scheduleId1;
            att3.date = sdf.format(cal.getTime());
            att3.status = "late";
            att3.remark = "Đến muộn 10 phút";
            att3.duration = 110;
            att3.type = "lecture";
            db.attendanceDetailDao().insert(att3);
        }
    }

    private static void insertSampleNotifications(EducationDatabase db) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Notification notif1 = new Notification();
        notif1.title = "Thông báo lịch thi";
        notif1.content = "Lịch thi giữa kỳ sẽ được công bố vào tuần tới";
        notif1.sender_id = 2;
        notif1.role_target = "student";
        notif1.date = sdf.format(new Date());
        db.notificationDao().insert(notif1);

        Notification notif2 = new Notification();
        notif2.title = "Thay đổi lịch học";
        notif2.content = "Lớp Cơ Sở Dữ Liệu sẽ học bù vào thứ 7";
        notif2.sender_id = 3;
        notif2.role_target = "student";
        notif2.date = sdf.format(new Date());
        db.notificationDao().insert(notif2);
    }

    private static void insertSampleFeedback(EducationDatabase db) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Feedback feedback1 = new Feedback();
        feedback1.user_id = 2;
        feedback1.student_id = 1;
        feedback1.subject = "Phản hồi về môn Lập Trình Java";
        feedback1.content = "Môn học rất hay và bổ ích, giảng viên giảng dạy nhiệt tình";
        feedback1.category = "course";
        feedback1.date = sdf.format(new Date());
        feedback1.rating = 5;
        feedback1.status = "pending";
        feedback1.response = "";
        db.feedbackDao().insert(feedback1);

        Feedback feedback2 = new Feedback();
        feedback2.user_id = 3;
        feedback2.student_id = 1;
        feedback2.subject = "Góp ý về hệ thống";
        feedback2.content = "Hệ thống cần cải thiện tốc độ tải trang";
        feedback2.category = "system";
        feedback2.date = sdf.format(new Date());
        feedback2.rating = 4;
        feedback2.status = "responded";
        feedback2.response = "Cảm ơn bạn đã phản hồi. Chúng tôi sẽ cải thiện trong thời gian tới.";
        db.feedbackDao().insert(feedback2);
    }
}

