package com.example.fu_academy.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.fu_academy.dao.*;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.*;
import com.example.fu_academy.entity.Class;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseInitializer {
    private static final String PREFS_NAME = "FU_Academy_Prefs";
    private static final String KEY_DATA_INITIALIZED = "data_initialized";

    public static void initializeData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isInitialized = prefs.getBoolean(KEY_DATA_INITIALIZED, false);

        EducationDatabase db = EducationDatabase.getInstance(context);
        UserDao userDao = db.userDao();

        // Migration: Cập nhật email cũ thành email mới nếu user đã tồn tại với email cũ
        User oldStudent1 = userDao.getUserByEmail("student1@fu.edu.vn");
        if (oldStudent1 != null) {
            // Kiểm tra xem email mới đã tồn tại chưa
            User newStudent1 = userDao.getUserByEmail("sonmthe170091@fu.edu.vn");
            if (newStudent1 == null) {
                // Cập nhật email cũ thành email mới
                oldStudent1.email = "sonmthe170091@fu.edu.vn";
                oldStudent1.name = "Mạc Tuấn Sơn";
                userDao.updateUser(oldStudent1);
                android.util.Log.d("DatabaseInitializer", "Updated email from student1@fu.edu.vn to sonmthe170091@fu.edu.vn");
            }
        }

        // Kiểm tra xem đã có dữ liệu chưa (kiểm tra số lượng users và enrollments)
        List<User> existingUsers = userDao.getAllUsers();
        
        // Tìm student với email mới để kiểm tra enrollments
        User existingStudent1 = userDao.getUserByEmail("sonmthe170091@fu.edu.vn");
        List<Enrollment> existingEnrollments = null;
        if (existingStudent1 != null) {
            existingEnrollments = db.enrollmentDao().getByStudent(existingStudent1.user_id);
        }
        
        // Nếu đã có users và enrollments, đảm bảo schedules được cập nhật cho tuần hiện tại
        if (existingUsers != null && existingUsers.size() > 0 
            && existingEnrollments != null && existingEnrollments.size() > 0) {
            // Đã có dữ liệu đầy đủ, nhưng vẫn cần đảm bảo schedules cho tuần hiện tại
            ensureSchedulesForCurrentWeek(db);
            if (!isInitialized) {
                prefs.edit().putBoolean(KEY_DATA_INITIALIZED, true).apply();
            }
            android.util.Log.d("DatabaseInitializer", "Database already initialized with " + existingUsers.size() + " users");
            return;
        }

        // Kiểm tra và tạo từng user nếu chưa tồn tại
        if (userDao.getUserByEmail("sonmthe170091@fu.edu.vn") == null) {
            User student1 = new User();
            student1.name = "Mạc Tuấn Sơn";
            student1.email = "sonmthe170091@fu.edu.vn";
            student1.password = "123456";
            student1.role = "student";
            student1.phone = "0901234567";
            student1.status = "active";
            student1.address = "123 Đường ABC, Quận 1, TP.HCM";
            student1.gender = "Nam";
            student1.major = "Công nghệ thông tin";
            userDao.insertUser(student1);
        }

        if (userDao.getUserByEmail("student2@fu.edu.vn") == null) {
            User student2 = new User();
            student2.name = "Trần Thị Bình";
            student2.email = "student2@fu.edu.vn";
            student2.password = "123456";
            student2.role = "student";
            student2.phone = "0902345678";
            student2.status = "active";
            student2.address = "456 Đường XYZ, Quận 2, TP.HCM";
            student2.gender = "Nữ";
            student2.major = "Kinh tế";
            userDao.insertUser(student2);
        }

        if (userDao.getUserByEmail("student3@fu.edu.vn") == null) {
            User student3 = new User();
            student3.name = "Lê Văn Cường";
            student3.email = "student3@fu.edu.vn";
            student3.password = "123456";
            student3.role = "student";
            student3.phone = "0903456789";
            student3.status = "active";
            student3.address = "789 Đường DEF, Quận 3, TP.HCM";
            student3.gender = "Nam";
            student3.major = "Quản trị kinh doanh";
            userDao.insertUser(student3);
        }

        if (userDao.getUserByEmail("tandmhe170536@fpt.edu.vn") == null) {
            User student4 = new User();
            student4.name = "Đinh Minh Tân";
            student4.email = "tandmhe170536@fpt.edu.vn";
            student4.password = "123456";
            student4.role = "student";
            student4.phone = "0866458780";
            student4.status = "active";
            student4.address = "Thường Tín, Hà Nội";
            student4.gender = "Nam";
            student4.major = "Công Nghệ Thông Tin";
            userDao.insertUser(student4);
        }

        if (userDao.getUserByEmail("lecturer1@fu.edu.vn") == null) {
            User lecturer1 = new User();
            lecturer1.name = "Phạm Thị Dung";
            lecturer1.email = "lecturer1@fu.edu.vn";
            lecturer1.password = "123456";
            lecturer1.role = "lecturer";
            lecturer1.phone = "0912345678";
            lecturer1.status = "active";
            lecturer1.address = "321 Đường GHI, Quận 5, TP.HCM";
            lecturer1.gender = "Nữ";
            lecturer1.major = "Công nghệ thông tin";
            userDao.insertUser(lecturer1);
        }

        if (userDao.getUserByEmail("lecturer2@fu.edu.vn") == null) {
            User lecturer2 = new User();
            lecturer2.name = "Hoàng Văn Em";
            lecturer2.email = "lecturer2@fu.edu.vn";
            lecturer2.password = "123456";
            lecturer2.role = "lecturer";
            lecturer2.phone = "0913456789";
            lecturer2.status = "active";
            lecturer2.address = "654 Đường JKL, Quận 7, TP.HCM";
            lecturer2.gender = "Nam";
            lecturer2.major = "Kinh tế";
            userDao.insertUser(lecturer2);
        }

        // Initialize Courses
        initializeCourses(db, userDao, db.courseDao(), db.classDao(), db.enrollmentDao(), 
                         db.assignmentDao(), db.materialDao(), db.submissionDao());

        // Đánh dấu đã khởi tạo
        prefs.edit().putBoolean(KEY_DATA_INITIALIZED, true).apply();
    }

    private static void initializeCourses(EducationDatabase db, UserDao userDao, CourseDao courseDao,
                                         ClassDao classDao, EnrollmentDao enrollmentDao,
                                         AssignmentDao assignmentDao, MaterialDao materialDao,
                                         SubmissionDao submissionDao) {
        try {
            // Get users
            User student1 = userDao.getUserByEmail("sonmthe170091@fu.edu.vn");
            User student2 = userDao.getUserByEmail("student2@fu.edu.vn");
            User student3 = userDao.getUserByEmail("student3@fu.edu.vn");
            User student4 = userDao.getUserByEmail("tandmhe170536@fpt.edu.vn");
            User lecturer1 = userDao.getUserByEmail("lecturer1@fu.edu.vn");
            User lecturer2 = userDao.getUserByEmail("lecturer2@fu.edu.vn");

            if (lecturer1 == null || lecturer2 == null || student1 == null) {
                return; // Users not initialized yet
            }

            // Check total courses count
            List<Course> existingCourses = courseDao.getAll();
            int totalCourses = (existingCourses != null) ? existingCourses.size() : 0;

            // Only skip if we already have 48 or more courses
            if (totalCourses >= 48) {
                android.util.Log.d("DatabaseInitializer", "Already have " + totalCourses + " courses, skipping initialization");
                // Đảm bảo schedules được tạo cho tháng hiện tại
                ensureSchedulesForCurrentWeek(db);
                return;
            }

            // Course 1: Lập trình Java (Fall)
            Course course1 = new Course();
            course1.name = "Lập trình Java";
            course1.course_code = "PRJ301";
            course1.lecturer_id = lecturer1.user_id;
            course1.credit = 3;
            course1.semester = "Fall 2024";
            course1.status = "Đang học";
            course1.type = "Bắt buộc";
            long course1Id = courseDao.insert(course1);

            // Class 1 for Course 1
            Class class1 = new Class();
            class1.course_id = course1Id;
            class1.lecturer_id = lecturer1.user_id;
            class1.room = "P201";
            class1.schedule = "Thứ 2, 8:00-11:00";
            class1.semester = "Fall 2024";
            long class1Id = classDao.insert(class1);

            // Enrollment for student1 in class1
            Enrollment enrollment1 = new Enrollment();
            enrollment1.student_id = student1.user_id;
            enrollment1.class_id = class1Id;
            enrollment1.attendance_score = 8.5;
            enrollment1.assignment_score = 7.5;
            enrollment1.midterm_score = 8.0;
            enrollment1.final_score = 9.0;
            enrollment1.average_score = 8.25;
            enrollment1.status = "Pass";
            enrollment1.attendance = 85;
            enrollmentDao.insert(enrollment1);

            // Assignment 1 for Course 1
            Assignment assignment1 = new Assignment();
            assignment1.course_id = course1Id;
            assignment1.title = "Bài tập Java - OOP";
            assignment1.deadline = "2024-12-15";
            assignment1.description = "Viết chương trình quản lý sinh viên sử dụng OOP";
            assignment1.status = "Đã nộp";
            assignment1.grade = 8.5;
            assignment1.type = "Bài tập";
            assignment1.file_required = true;
            assignment1.max_score = 10.0;
            assignment1.feedback = "Tốt, cần cải thiện phần exception handling";
            long assignment1Id = assignmentDao.insert(assignment1);

            // Submission for assignment1
            Submission submission1 = new Submission();
            submission1.assignment_id = assignment1Id;
            submission1.student_id = student1.user_id;
            submission1.file_url = "/files/submission1.pdf";
            submission1.file_name = "BaiTapJava_OOP.pdf";
            submission1.file_size = "2.5 MB";
            submission1.submit_date = "2024-12-14";
            submission1.status = "On-time";
            submission1.grade = 8.5;
            submission1.feedback = "Tốt, cần cải thiện phần exception handling";
            submissionDao.insert(submission1);

            // Material 1 for Course 1
            Material material1 = new Material();
            material1.course_id = course1Id;
            material1.title = "Slide bài giảng Java OOP";
            material1.file_url = "/materials/java_oop_slide.pdf";
            material1.upload_date = "2024-09-01";
            material1.type = "PDF";
            material1.file_size = "5.2 MB";
            material1.owner_id = lecturer1.user_id;
            material1.description = "Slide bài giảng về lập trình hướng đối tượng trong Java";
            materialDao.insert(material1);

            // Course 2: Cơ sở dữ liệu (Spring)
            Course course2 = new Course();
            course2.name = "Cơ sở dữ liệu";
            course2.course_code = "DBI202";
            course2.lecturer_id = lecturer2.user_id;
            course2.credit = 3;
            course2.semester = "Spring 2024";
            course2.status = "Đã học";
            course2.type = "Bắt buộc";
            long course2Id = courseDao.insert(course2);

            // Class 2 for Course 2
            Class class2 = new Class();
            class2.course_id = course2Id;
            class2.lecturer_id = lecturer2.user_id;
            class2.room = "P302";
            class2.schedule = "Thứ 4, 13:00-16:00";
            class2.semester = "Spring 2024";
            long class2Id = classDao.insert(class2);

            // Enrollment for student1 in class2
            Enrollment enrollment2 = new Enrollment();
            enrollment2.student_id = student1.user_id;
            enrollment2.class_id = class2Id;
            enrollment2.attendance_score = 9.0;
            enrollment2.assignment_score = 8.0;
            enrollment2.midterm_score = 8.5;
            enrollment2.final_score = 9.5;
            enrollment2.average_score = 8.75;
            enrollment2.status = "Pass";
            enrollment2.attendance = 90;
            enrollmentDao.insert(enrollment2);

            // Assignment 2 for Course 2
            Assignment assignment2 = new Assignment();
            assignment2.course_id = course2Id;
            assignment2.title = "Thiết kế Database cho hệ thống quản lý";
            assignment2.deadline = "2024-12-20";
            assignment2.description = "Thiết kế database và viết các câu query SQL";
            assignment2.status = "Chưa nộp";
            assignment2.grade = null;
            assignment2.type = "Dự án";
            assignment2.file_required = true;
            assignment2.max_score = 10.0;
            assignment2.feedback = null;
            assignmentDao.insert(assignment2);

            // Material 2 for Course 2
            Material material2 = new Material();
            material2.course_id = course2Id;
            material2.title = "Video bài giảng SQL cơ bản";
            material2.file_url = "/materials/sql_basic_video.mp4";
            material2.upload_date = "2024-09-05";
            material2.type = "Video";
            material2.file_size = "125 MB";
            material2.owner_id = lecturer2.user_id;
            material2.description = "Video hướng dẫn SQL cơ bản";
            materialDao.insert(material2);

            // Course 3: Mạng máy tính (Summer)
            Course course3 = new Course();
            course3.name = "Mạng máy tính";
            course3.course_code = "NWC301";
            course3.lecturer_id = lecturer1.user_id;
            course3.credit = 2;
            course3.semester = "Summer 2024";
            course3.status = "Đã học";
            course3.type = "Tự chọn";
            long course3Id = courseDao.insert(course3);

            // Class 3 for Course 3
            Class class3 = new Class();
            class3.course_id = course3Id;
            class3.lecturer_id = lecturer1.user_id;
            class3.room = "P401";
            class3.schedule = "Thứ 6, 8:00-10:00";
            class3.semester = "Summer 2024";
            long class3Id = classDao.insert(class3);

            // Enrollment for student1 in class3
            Enrollment enrollment3 = new Enrollment();
            enrollment3.student_id = student1.user_id;
            enrollment3.class_id = class3Id;
            enrollment3.attendance_score = 7.5;
            enrollment3.assignment_score = 7.0;
            enrollment3.midterm_score = 7.5;
            enrollment3.final_score = 8.0;
            enrollment3.average_score = 7.5;
            enrollment3.status = "Pass";
            enrollment3.attendance = 75;
            enrollmentDao.insert(enrollment3);

            // Assignment 3 for Course 3
            Assignment assignment3 = new Assignment();
            assignment3.course_id = course3Id;
            assignment3.title = "Bài tập về mô hình OSI";
            assignment3.deadline = "2024-11-30";
            assignment3.description = "Phân tích và giải thích mô hình OSI";
            assignment3.status = "Đã nộp";
            assignment3.grade = 7.5;
            assignment3.type = "Bài tập";
            assignment3.file_required = false;
            assignment3.max_score = 10.0;
            assignment3.feedback = "Đầy đủ, cần giải thích rõ hơn về tầng Network";
            long assignment3Id = assignmentDao.insert(assignment3);

            // Submission for assignment3
            Submission submission3 = new Submission();
            submission3.assignment_id = assignment3Id;
            submission3.student_id = student1.user_id;
            submission3.file_url = "/files/submission3.docx";
            submission3.file_name = "BaiTap_OSI.docx";
            submission3.file_size = "1.8 MB";
            submission3.submit_date = "2024-11-29";
            submission3.status = "On-time";
            submission3.grade = 7.5;
            submission3.feedback = "Đầy đủ, cần giải thích rõ hơn về tầng Network";
            submissionDao.insert(submission3);

            // Material 3 for Course 3
            Material material3 = new Material();
            material3.course_id = course3Id;
            material3.title = "Tài liệu tham khảo về TCP/IP";
            material3.file_url = "/materials/tcpip_reference.pdf";
            material3.upload_date = "2024-09-10";
            material3.type = "PDF";
            material3.file_size = "8.5 MB";
            material3.owner_id = lecturer1.user_id;
            material3.description = "Tài liệu tham khảo chi tiết về giao thức TCP/IP";
            materialDao.insert(material3);

            // Add enrollments for other students if they exist
            if (student2 != null) {
                Enrollment enrollment4 = new Enrollment();
                enrollment4.student_id = student2.user_id;
                enrollment4.class_id = class1Id;
                enrollment4.attendance_score = 8.0;
                enrollment4.assignment_score = 7.0;
                enrollment4.midterm_score = 7.5;
                enrollment4.final_score = 8.5;
                enrollment4.average_score = 7.75;
                enrollment4.status = "Pass";
                enrollment4.attendance = 80;
                enrollmentDao.insert(enrollment4);
            }

            if (student3 != null) {
                Enrollment enrollment5 = new Enrollment();
                enrollment5.student_id = student3.user_id;
                enrollment5.class_id = class2Id;
                enrollment5.attendance_score = 9.5;
                enrollment5.assignment_score = 9.0;
                enrollment5.midterm_score = 9.0;
                enrollment5.final_score = 9.5;
                enrollment5.average_score = 9.25;
                enrollment5.status = "Pass";
                enrollment5.attendance = 95;
                enrollmentDao.insert(enrollment5);
            }

            // Add 45 more courses to reach 48 total
            addMoreCourses(courseDao, classDao, enrollmentDao, assignmentDao, materialDao,
                          lecturer1, lecturer2, student1);

            // Tạo schedules, exams, attendance, notifications, feedback
            ensureSchedulesForCurrentWeek(db);
            insertSampleExams(db);
            insertSampleAttendanceForStudent(db, student1.user_id);
            if (student2 != null) insertSampleAttendanceForStudent(db, student2.user_id);
            if (student3 != null) insertSampleAttendanceForStudent(db, student3.user_id);
            insertSampleNotifications(db, lecturer1.user_id, lecturer2.user_id);
            insertSampleFeedbackForStudent(db, student1.user_id, lecturer1.user_id, lecturer2.user_id);
            if (student2 != null) insertSampleFeedbackForStudent(db, student2.user_id, lecturer1.user_id, lecturer2.user_id);
            if (student3 != null) insertSampleFeedbackForStudent(db, student3.user_id, lecturer1.user_id, lecturer2.user_id);

        } catch (Exception e) {
            android.util.Log.e("DatabaseInitializer", "Error initializing courses: " + e.getMessage(), e);
        }
    }

    private static void addMoreCourses(CourseDao courseDao, ClassDao classDao, EnrollmentDao enrollmentDao,
                                      AssignmentDao assignmentDao, MaterialDao materialDao,
                                      User lecturer1, User lecturer2, User student1) {
        String currentSemester = "Fall 2024";
        String[][] coursesData = {
            // Format: [course_code, name, credit, semester, status, type, room, lecturer]
            {"PRJ302", "Lập trình Web", "3", currentSemester, "Đang học", "Bắt buộc", "P202", "1"},
            {"PRJ303", "Lập trình Mobile", "3", currentSemester, "Đang học", "Bắt buộc", "P203", "1"},
            {"DBI203", "Phân tích thiết kế hệ thống", "3", "Spring 2024", "Đã học", "Bắt buộc", "P303", "2"},
            {"NWC302", "Bảo mật mạng", "2", "Summer 2024", "Đã học", "Tự chọn", "P402", "1"},
            // Thêm 41 courses nữa để đạt 48 total (3 courses đầu + 45 courses này)
        };

        // Chỉ thêm đủ để đạt 48 courses
        int coursesToAdd = 45;
        for (int i = 0; i < Math.min(coursesToAdd, coursesData.length); i++) {
            try {
                String[] courseData = coursesData[i];
                Course course = new Course();
                course.course_code = courseData[0];
                course.name = courseData[1];
                course.credit = Integer.parseInt(courseData[2]);
                course.semester = courseData[3];
                course.status = courseData[4];
                course.type = courseData[5];
                course.lecturer_id = "1".equals(courseData[7]) ? lecturer1.user_id : lecturer2.user_id;

                long courseId = courseDao.insert(course);

                // Create class for this course
                Class classObj = new Class();
                classObj.course_id = courseId;
                classObj.lecturer_id = course.lecturer_id;
                classObj.room = courseData[6];
                classObj.schedule = "Thứ 2, 8:00-11:00";
                classObj.semester = courseData[3];
                long classId = classDao.insert(classObj);

                // Create enrollment for student1
                Enrollment enrollment = new Enrollment();
                enrollment.student_id = student1.user_id;
                enrollment.class_id = classId;

                // Set scores based on status
                boolean isCurrentSemester = courseData[3].equals(currentSemester) && "Đang học".equals(courseData[4]);

                if ("Đã học".equals(courseData[4]) || (!isCurrentSemester && "Đang học".equals(courseData[4]))) {
                    enrollment.attendance_score = 7.0 + Math.random() * 2.5;
                    enrollment.assignment_score = 7.0 + Math.random() * 2.5;
                    enrollment.midterm_score = 7.0 + Math.random() * 2.5;
                    enrollment.final_score = 7.0 + Math.random() * 2.5;
                    enrollment.average_score = (enrollment.attendance_score * 0.1 +
                                               enrollment.assignment_score * 0.2 +
                                               enrollment.midterm_score * 0.3 +
                                               enrollment.final_score * 0.4);
                    enrollment.status = enrollment.average_score >= 5.0 ? "Pass" : "Fail";
                    enrollment.attendance = 70 + (int)(Math.random() * 25);
                } else {
                    enrollment.attendance_score = null;
                    enrollment.assignment_score = null;
                    enrollment.midterm_score = null;
                    enrollment.final_score = null;
                    enrollment.average_score = null;
                    enrollment.status = null;
                    enrollment.attendance = 0;
                }
                enrollmentDao.insert(enrollment);

                // Create assignment for some courses
                if (Math.random() > 0.3) {
                    Assignment assignment = new Assignment();
                    assignment.course_id = courseId;
                    assignment.title = "Bài tập " + courseData[1];
                    assignment.deadline = "2024-12-25";
                    assignment.description = "Bài tập về " + courseData[1];
                    assignment.status = Math.random() > 0.5 ? "Đã nộp" : "Chưa nộp";
                    assignment.grade = assignment.status.equals("Đã nộp") ? 7.0 + Math.random() * 2.5 : null;
                    assignment.type = Math.random() > 0.5 ? "Bài tập" : "Dự án";
                    assignment.file_required = Math.random() > 0.5;
                    assignment.max_score = 10.0;
                    assignment.feedback = assignment.status.equals("Đã nộp") ? "Tốt" : null;
                    assignmentDao.insert(assignment);
                }

                // Create material for some courses
                if (Math.random() > 0.4) {
                    Material material = new Material();
                    material.course_id = courseId;
                    material.title = "Tài liệu " + courseData[1];
                    material.file_url = "/materials/" + courseData[0].toLowerCase() + ".pdf";
                    material.upload_date = "2024-09-01";
                    material.type = Math.random() > 0.5 ? "PDF" : "Video";
                    material.file_size = String.format("%.1f MB", 1.0 + Math.random() * 10);
                    material.owner_id = course.lecturer_id;
                    material.description = "Tài liệu tham khảo cho " + courseData[1];
                    materialDao.insert(material);
                }

            } catch (Exception e) {
                android.util.Log.e("DatabaseInitializer", "Error adding course " + (i < coursesData.length ? coursesData[i][0] : "unknown") + ": " + e.getMessage());
            }
        }
    }

    // Đảm bảo schedules luôn được tạo cho tháng hiện tại (đồng bộ với Monthly Calendar)
    public static void ensureSchedulesForCurrentWeek(EducationDatabase db) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        
        String currentMonth = monthFormat.format(cal.getTime());
        
        // Kiểm tra xem đã có schedules cho tháng hiện tại chưa
        List<Schedule> allSchedules = db.scheduleDao().getAll();
        boolean hasCurrentMonthSchedules = false;
        if (allSchedules != null && !allSchedules.isEmpty()) {
            for (Schedule s : allSchedules) {
                if (s.date != null && s.date.startsWith(currentMonth)) {
                    hasCurrentMonthSchedules = true;
                    break;
                }
            }
        }
        
        // Nếu chưa có schedules cho tháng hiện tại, tạo mới cho toàn bộ tháng
        if (!hasCurrentMonthSchedules) {
            insertSampleSchedulesForMonth(db, currentMonth);
        }
    }

    // Khởi tạo dữ liệu mẫu cho schedules - tạo cho toàn bộ tháng (đồng bộ với Monthly Calendar)
    private static void insertSampleSchedulesForMonth(EducationDatabase db, String month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        List<Class> classes = db.classDao().getAll();
        
        if (classes == null || classes.isEmpty()) return;

        try {
            // Parse tháng (yyyy-MM) để lấy năm và tháng
            String[] parts = month.split("-");
            int year = Integer.parseInt(parts[0]);
            int monthInt = Integer.parseInt(parts[1]) - 1; // Calendar.MONTH is 0-based
            
            // Tạo schedules cho các ngày trong tháng (tương tự như Monthly Calendar hiển thị)
            // Ngày 3 - Lớp 1 (Lecture)
            if (classes.size() > 0) {
                cal.set(year, monthInt, 3);
                Schedule schedule1 = new Schedule();
                schedule1.class_id = classes.get(0).class_id;
                schedule1.date = sdf.format(cal.getTime());
                schedule1.time = "07:00-09:00";
                schedule1.room = "A101";
                schedule1.type = "lecture";
                schedule1.status = "scheduled";
                db.scheduleDao().insert(schedule1);

                // Ngày 4 - Lớp 1 (Lab)
                cal.set(year, monthInt, 4);
                Schedule schedule4 = new Schedule();
                schedule4.class_id = classes.get(0).class_id;
                schedule4.date = sdf.format(cal.getTime());
                schedule4.time = "14:00-16:00";
                schedule4.room = "A101";
                schedule4.type = "lab";
                schedule4.status = "scheduled";
                db.scheduleDao().insert(schedule4);
            }

            // Ngày 6 - Lớp 2 (Lecture)
            if (classes.size() > 1) {
                cal.set(year, monthInt, 6);
                Schedule schedule2 = new Schedule();
                schedule2.class_id = classes.get(1).class_id;
                schedule2.date = sdf.format(cal.getTime());
                schedule2.time = "09:00-11:00";
                schedule2.room = "B202";
                schedule2.type = "lecture";
                schedule2.status = "scheduled";
                db.scheduleDao().insert(schedule2);
            }

            // Ngày 8 - Lớp 3 (Lab)
            if (classes.size() > 2) {
                cal.set(year, monthInt, 8);
                Schedule schedule3 = new Schedule();
                schedule3.class_id = classes.get(2).class_id;
                schedule3.date = sdf.format(cal.getTime());
                schedule3.time = "13:00-15:00";
                schedule3.room = "C303";
                schedule3.type = "lab";
                schedule3.status = "scheduled";
                db.scheduleDao().insert(schedule3);
            }

            // Thêm schedules cho tuần hiện tại (để đảm bảo có dữ liệu cho Weekly Schedule)
            insertSampleSchedules(db);
        } catch (Exception e) {
            android.util.Log.e("DatabaseInitializer", "Error creating schedules for month: " + month, e);
            // Fallback: tạo schedules cho tuần hiện tại
            insertSampleSchedules(db);
        }
    }

    // Khởi tạo dữ liệu mẫu cho schedules - tạo trong tuần hiện tại
    private static void insertSampleSchedules(EducationDatabase db) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        List<Class> classes = db.classDao().getAll();
        
        if (classes == null || classes.isEmpty()) return;

        // Lấy thứ 2 của tuần hiện tại
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysFromMonday = (dayOfWeek == Calendar.SUNDAY) ? 6 : dayOfWeek - Calendar.MONDAY;
        cal.add(Calendar.DAY_OF_MONTH, -daysFromMonday);
        Calendar mondayCal = (Calendar) cal.clone();
        
        // Tạo schedules cho tuần hiện tại
        // Thứ 2 - Lớp 1
        if (classes.size() > 0) {
            Schedule schedule1 = new Schedule();
            schedule1.class_id = classes.get(0).class_id;
            schedule1.date = sdf.format(cal.getTime());
            schedule1.time = "07:00-09:00";
            schedule1.room = "A101";
            schedule1.type = "lecture";
            schedule1.status = "scheduled";
            db.scheduleDao().insert(schedule1);

            // Thứ 3 - Lớp 1 (thêm)
            cal = (Calendar) mondayCal.clone();
            cal.add(Calendar.DAY_OF_MONTH, 1); // Thứ 3
            Schedule schedule4 = new Schedule();
            schedule4.class_id = classes.get(0).class_id;
            schedule4.date = sdf.format(cal.getTime());
            schedule4.time = "14:00-16:00";
            schedule4.room = "A101";
            schedule4.type = "lab";
            schedule4.status = "scheduled";
            db.scheduleDao().insert(schedule4);
        }

        // Thứ 4 - Lớp 2
        if (classes.size() > 1) {
            cal = (Calendar) mondayCal.clone();
            cal.add(Calendar.DAY_OF_MONTH, 3); // Thứ 4
            Schedule schedule2 = new Schedule();
            schedule2.class_id = classes.get(1).class_id;
            schedule2.date = sdf.format(cal.getTime());
            schedule2.time = "09:00-11:00";
            schedule2.room = "B202";
            schedule2.type = "lecture";
            schedule2.status = "scheduled";
            db.scheduleDao().insert(schedule2);
        }

        // Thứ 6 - Lớp 3
        if (classes.size() > 2) {
            cal = (Calendar) mondayCal.clone();
            cal.add(Calendar.DAY_OF_MONTH, 5); // Thứ 6
            Schedule schedule3 = new Schedule();
            schedule3.class_id = classes.get(2).class_id;
            schedule3.date = sdf.format(cal.getTime());
            schedule3.time = "13:00-15:00";
            schedule3.room = "C303";
            schedule3.type = "lab";
            schedule3.status = "scheduled";
            db.scheduleDao().insert(schedule3);
        }
    }

    // Khởi tạo dữ liệu mẫu cho exams
    private static void insertSampleExams(EducationDatabase db) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        List<Course> courses = db.courseDao().getAll();

        if (courses == null || courses.isEmpty()) return;

        if (courses.size() > 0) {
            Exam exam1 = new Exam();
            exam1.course_id = courses.get(0).course_id;
            exam1.date = sdf.format(cal.getTime());
            exam1.time = "08:00";
            exam1.room = "A101";
            exam1.seat = "A15";
            exam1.duration = 90;
            exam1.type = "midterm";
            exam1.status = "scheduled";
            db.examDao().insert(exam1);
        }

        if (courses.size() > 1) {
            cal.add(Calendar.DAY_OF_MONTH, 3);
            Exam exam2 = new Exam();
            exam2.course_id = courses.get(1).course_id;
            exam2.date = sdf.format(cal.getTime());
            exam2.time = "09:00";
            exam2.room = "B202";
            exam2.seat = "B20";
            exam2.duration = 120;
            exam2.type = "midterm";
            exam2.status = "scheduled";
            db.examDao().insert(exam2);
        }
    }

    // Khởi tạo dữ liệu mẫu cho attendance riêng cho từng student
    private static void insertSampleAttendanceForStudent(EducationDatabase db, long studentId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -14);

        List<Enrollment> enrollments = db.enrollmentDao().getByStudent(studentId);
        if (enrollments == null || enrollments.isEmpty()) return;

        List<Schedule> allSchedules = db.scheduleDao().getAll();
        if (allSchedules == null || allSchedules.isEmpty()) return;

        int attendanceCount = 0;
        String[] statuses = {"present", "present", "late", "present", "absent", "present"};
        String[] remarks = {
            "Có mặt đầy đủ",
            "Tham gia tích cực",
            "Đến muộn 10 phút",
            "Có mặt đầy đủ",
            "Vắng mặt có phép",
            "Có mặt đầy đủ"
        };

        for (Enrollment enrollment : enrollments) {
            for (Schedule schedule : allSchedules) {
                if (schedule.class_id == enrollment.class_id && attendanceCount < 6) {
                    AttendanceDetail att = new AttendanceDetail();
                    att.student_id = studentId;
                    att.schedule_id = schedule.id;
                    att.date = sdf.format(cal.getTime());
                    att.status = statuses[attendanceCount % statuses.length];
                    att.remark = remarks[attendanceCount % remarks.length];
                    att.duration = 120;
                    att.type = "lecture";
                    db.attendanceDetailDao().insert(att);
                    
                    cal.add(Calendar.DAY_OF_MONTH, 2);
                    attendanceCount++;
                    
                    if (attendanceCount >= 6) break;
                }
            }
            if (attendanceCount >= 6) break;
        }
    }

    // Khởi tạo dữ liệu mẫu cho notifications
    private static void insertSampleNotifications(EducationDatabase db, long lecturer1Id, long lecturer2Id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Notification notif1 = new Notification();
        notif1.title = "Thông báo lịch thi";
        notif1.content = "Lịch thi giữa kỳ sẽ được công bố vào tuần tới";
        notif1.sender_id = lecturer1Id;
        notif1.role_target = "student";
        notif1.date = sdf.format(new Date());
        db.notificationDao().insert(notif1);

        Notification notif2 = new Notification();
        notif2.title = "Thay đổi lịch học";
        notif2.content = "Lớp Cơ Sở Dữ Liệu sẽ học bù vào thứ 7";
        notif2.sender_id = lecturer2Id;
        notif2.role_target = "student";
        notif2.date = sdf.format(new Date());
        db.notificationDao().insert(notif2);
    }

    // Khởi tạo dữ liệu mẫu cho feedback riêng cho từng student
    private static void insertSampleFeedbackForStudent(EducationDatabase db, long studentId, long lecturer1Id, long lecturer2Id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        
        User student = db.userDao().getUserById(studentId);
        String studentName = student != null && student.name != null ? student.name : "Sinh viên";

        Feedback feedback1 = new Feedback();
        feedback1.user_id = lecturer1Id;
        feedback1.student_id = studentId;
        feedback1.subject = "Phản hồi về môn Lập Trình Java";
        feedback1.content = "Môn học rất hay và bổ ích, giảng viên giảng dạy nhiệt tình. Em rất hài lòng với phương pháp giảng dạy.";
        feedback1.category = "course";
        feedback1.date = sdf.format(new Date());
        feedback1.rating = 5;
        feedback1.status = "pending";
        feedback1.response = "";
        db.feedbackDao().insert(feedback1);

        Feedback feedback2 = new Feedback();
        feedback2.user_id = lecturer2Id;
        feedback2.student_id = studentId;
        feedback2.subject = "Góp ý về hệ thống";
        feedback2.content = "Hệ thống cần cải thiện tốc độ tải trang và thêm tính năng thông báo real-time.";
        feedback2.category = "system";
        feedback2.date = sdf.format(new Date());
        feedback2.rating = 4;
        feedback2.status = "responded";
        feedback2.response = "Cảm ơn bạn đã phản hồi. Chúng tôi sẽ xem xét và cải thiện trong thời gian tới.";
        db.feedbackDao().insert(feedback2);
    }
}
