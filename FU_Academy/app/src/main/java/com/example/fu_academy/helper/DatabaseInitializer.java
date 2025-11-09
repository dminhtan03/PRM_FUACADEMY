package com.example.fu_academy.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.fu_academy.dao.UserDao;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.User;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.Class;
import com.example.fu_academy.entity.Enrollment;
import com.example.fu_academy.entity.Schedule;
import com.example.fu_academy.entity.Exam;
import com.example.fu_academy.entity.AttendanceDetail;
import com.example.fu_academy.entity.Notification;
import com.example.fu_academy.entity.Feedback;

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
        
        // Nếu chỉ có users nhưng chưa có enrollments, cần tạo enrollments
        if (existingUsers != null && existingUsers.size() > 0 
            && (existingEnrollments == null || existingEnrollments.size() == 0)) {
            android.util.Log.d("DatabaseInitializer", "Users exist but no enrollments found. Creating enrollments...");
            // Tìm lại các users đã có
            User insertedStudent1 = userDao.getUserByEmail("sonmthe170091@fu.edu.vn");
            User insertedStudent2 = userDao.getUserByEmail("student2@fu.edu.vn");
            User insertedStudent3 = userDao.getUserByEmail("student3@fu.edu.vn");
            User insertedLecturer1 = userDao.getUserByEmail("lecturer1@fu.edu.vn");
            User insertedLecturer2 = userDao.getUserByEmail("lecturer2@fu.edu.vn");
            
            if (insertedStudent1 != null && insertedStudent2 != null && insertedStudent3 != null 
                && insertedLecturer1 != null && insertedLecturer2 != null) {
                // Kiểm tra xem đã có courses chưa
                List<Course> existingCourses = db.courseDao().getAll();
                if (existingCourses == null || existingCourses.isEmpty()) {
                    insertSampleCourses(db, insertedLecturer1.user_id, insertedLecturer2.user_id);
                }
                
                // Kiểm tra xem đã có classes chưa
                List<Class> existingClasses = db.classDao().getAll();
                if (existingClasses == null || existingClasses.isEmpty()) {
                    insertSampleClasses(db, insertedLecturer1.user_id, insertedLecturer2.user_id);
                }
                
                // Tạo enrollments
                insertSampleEnrollmentsForStudent(db, insertedStudent1.user_id, 0);
                insertSampleEnrollmentsForStudent(db, insertedStudent2.user_id, 1);
                insertSampleEnrollmentsForStudent(db, insertedStudent3.user_id, 2);
                
                // Tạo schedules, exams, attendance, notifications, feedback
                // Đảm bảo schedules luôn được tạo cho tuần hiện tại
                ensureSchedulesForCurrentWeek(db);
                
                List<Exam> existingExams = db.examDao().getAll();
                if (existingExams == null || existingExams.isEmpty()) {
                    insertSampleExams(db);
                }
                
                insertSampleAttendanceForStudent(db, insertedStudent1.user_id);
                insertSampleAttendanceForStudent(db, insertedStudent2.user_id);
                insertSampleAttendanceForStudent(db, insertedStudent3.user_id);
                
                List<Notification> existingNotifications = db.notificationDao().getAll();
                if (existingNotifications == null || existingNotifications.isEmpty()) {
                    insertSampleNotifications(db, insertedLecturer1.user_id, insertedLecturer2.user_id);
                }
                
                insertSampleFeedbackForStudent(db, insertedStudent1.user_id, insertedLecturer1.user_id, insertedLecturer2.user_id);
                insertSampleFeedbackForStudent(db, insertedStudent2.user_id, insertedLecturer1.user_id, insertedLecturer2.user_id);
                insertSampleFeedbackForStudent(db, insertedStudent3.user_id, insertedLecturer1.user_id, insertedLecturer2.user_id);
                
                prefs.edit().putBoolean(KEY_DATA_INITIALIZED, true).apply();
                android.util.Log.d("DatabaseInitializer", "Created missing data for existing users");
                return;
            }
        }

        if (!isInitialized) {
            // Tạo tài khoản mẫu cho Student
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

            // Tạo tài khoản mẫu cho Lecturer
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

            // Lấy user_id sau khi insert (vì autoGenerate)
            User insertedStudent1 = userDao.getUserByEmail("sonmthe170091@fu.edu.vn");
            User insertedStudent2 = userDao.getUserByEmail("student2@fu.edu.vn");
            User insertedStudent3 = userDao.getUserByEmail("student3@fu.edu.vn");
            User insertedLecturer1 = userDao.getUserByEmail("lecturer1@fu.edu.vn");
            User insertedLecturer2 = userDao.getUserByEmail("lecturer2@fu.edu.vn");

            // Khởi tạo dữ liệu mẫu khác (courses, enrollments, etc.)
            if (insertedStudent1 != null && insertedStudent2 != null && insertedStudent3 != null 
                && insertedLecturer1 != null && insertedLecturer2 != null) {
                insertSampleCourses(db, insertedLecturer1.user_id, insertedLecturer2.user_id);
                insertSampleClasses(db, insertedLecturer1.user_id, insertedLecturer2.user_id);
                
                // Tạo enrollments riêng cho từng student
                insertSampleEnrollmentsForStudent(db, insertedStudent1.user_id, 0); // Student1: tất cả 3 lớp
                insertSampleEnrollmentsForStudent(db, insertedStudent2.user_id, 1); // Student2: lớp 1, 2
                insertSampleEnrollmentsForStudent(db, insertedStudent3.user_id, 2); // Student3: lớp 2, 3
                
                ensureSchedulesForCurrentWeek(db);
                insertSampleExams(db);
                
                // Tạo attendance riêng cho từng student
                insertSampleAttendanceForStudent(db, insertedStudent1.user_id);
                insertSampleAttendanceForStudent(db, insertedStudent2.user_id);
                insertSampleAttendanceForStudent(db, insertedStudent3.user_id);
                
                insertSampleNotifications(db, insertedLecturer1.user_id, insertedLecturer2.user_id);
                
                // Tạo feedback riêng cho từng student
                insertSampleFeedbackForStudent(db, insertedStudent1.user_id, insertedLecturer1.user_id, insertedLecturer2.user_id);
                insertSampleFeedbackForStudent(db, insertedStudent2.user_id, insertedLecturer1.user_id, insertedLecturer2.user_id);
                insertSampleFeedbackForStudent(db, insertedStudent3.user_id, insertedLecturer1.user_id, insertedLecturer2.user_id);
            }

            // Đánh dấu đã khởi tạo
            prefs.edit().putBoolean(KEY_DATA_INITIALIZED, true).apply();
        }

        // Luôn kiểm tra và thêm user mới nếu chưa tồn tại (cho phép thêm user mới sau khi đã khởi tạo)
        User student4 = addUserIfNotExists(userDao, "tandmhe170536@fpt.edu.vn", () -> {
            User newStudent = new User();
            newStudent.name = "Đinh Minh Tân";
            newStudent.email = "tandmhe170536@fpt.edu.vn";
            newStudent.password = "123456";
            newStudent.role = "student";
            newStudent.phone = "0866458780";
            newStudent.status = "active";
            newStudent.address = "Thường Tín, Hà Nội";
            newStudent.gender = "Nam";
            newStudent.major = "Công Nghệ Thông Tin";
            return newStudent;
        });
        
        // Nếu student4 mới được thêm, tạo dữ liệu mẫu cho họ
        if (student4 != null) {
            User insertedStudent4 = userDao.getUserByEmail("tandmhe170536@fpt.edu.vn");
            if (insertedStudent4 != null) {
                // Tạo enrollments cho student4 (lớp 1 và 3)
                insertSampleEnrollmentsForStudent(db, insertedStudent4.user_id, 3);
                // Tạo attendance cho student4
                insertSampleAttendanceForStudent(db, insertedStudent4.user_id);
                // Tạo feedback cho student4
                User lecturer1 = userDao.getUserByEmail("lecturer1@fu.edu.vn");
                User lecturer2 = userDao.getUserByEmail("lecturer2@fu.edu.vn");
                if (lecturer1 != null && lecturer2 != null) {
                    insertSampleFeedbackForStudent(db, insertedStudent4.user_id, lecturer1.user_id, lecturer2.user_id);
                }
            }
        }
    }

    // Helper method để thêm user nếu chưa tồn tại
    private static User addUserIfNotExists(UserDao userDao, String email, java.util.function.Supplier<User> userSupplier) {
        User existingUser = userDao.getUserByEmail(email);
        if (existingUser == null) {
            User newUser = userSupplier.get();
            userDao.insertUser(newUser);
            return newUser;
        }
        return null; // User đã tồn tại
    }

    // Method để reset và khởi tạo lại database (dùng khi cần thêm user mới)
    public static void resetAndInitialize(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_DATA_INITIALIZED, false).apply();
        initializeData(context);
    }

    // Method để thêm user mới bất cứ lúc nào (không cần reset)
    public static void addNewUser(Context context, User user) {
        EducationDatabase db = EducationDatabase.getInstance(context);
        UserDao userDao = db.userDao();
        userDao.insertUser(user);
    }

    // Khởi tạo dữ liệu mẫu cho courses
    private static void insertSampleCourses(EducationDatabase db, long lecturer1Id, long lecturer2Id) {
        Course course1 = new Course();
        course1.name = "Lập Trình Java";
        course1.lecturer_id = lecturer1Id;
        course1.credit = 3;
        course1.semester = "2024-2025 HK1";
        course1.status = "active";
        db.courseDao().insert(course1);

        Course course2 = new Course();
        course2.name = "Cơ Sở Dữ Liệu";
        course2.lecturer_id = lecturer2Id;
        course2.credit = 3;
        course2.semester = "2024-2025 HK1";
        course2.status = "active";
        db.courseDao().insert(course2);

        Course course3 = new Course();
        course3.name = "Mạng Máy Tính";
        course3.lecturer_id = lecturer1Id;
        course3.credit = 2;
        course3.semester = "2024-2025 HK1";
        course3.status = "active";
        db.courseDao().insert(course3);
    }

    // Khởi tạo dữ liệu mẫu cho classes
    private static void insertSampleClasses(EducationDatabase db, long lecturer1Id, long lecturer2Id) {
        List<Course> courses = db.courseDao().getAll();
        if (courses == null || courses.isEmpty()) return;

        Course course1 = courses.get(0);
        Course course2 = courses.size() > 1 ? courses.get(1) : course1;
        Course course3 = courses.size() > 2 ? courses.get(2) : course1;

        Class class1 = new Class();
        class1.course_id = course1.course_id;
        class1.lecturer_id = lecturer1Id;
        class1.room = "A101";
        class1.schedule = "Thứ 2, 7:00-9:00";
        class1.semester = "2024-2025 HK1";
        db.classDao().insert(class1);

        Class class2 = new Class();
        class2.course_id = course2.course_id;
        class2.lecturer_id = lecturer2Id;
        class2.room = "B202";
        class2.schedule = "Thứ 4, 9:00-11:00";
        class2.semester = "2024-2025 HK1";
        db.classDao().insert(class2);

        Class class3 = new Class();
        class3.course_id = course3.course_id;
        class3.lecturer_id = lecturer1Id;
        class3.room = "C303";
        class3.schedule = "Thứ 6, 13:00-15:00";
        class3.semester = "2024-2025 HK1";
        db.classDao().insert(class3);
    }

    // Khởi tạo dữ liệu mẫu cho enrollments - Student1: tất cả 3 lớp
    private static void insertSampleEnrollmentsForStudent(EducationDatabase db, long studentId, int studentIndex) {
        List<Class> classes = db.classDao().getAll();
        if (classes == null || classes.isEmpty()) return;

        // Student1: đăng ký tất cả 3 lớp với điểm cao
        // Student2: đăng ký lớp 1, 2 với điểm trung bình
        // Student3: đăng ký lớp 2, 3 với điểm khá
        int[] classIndices;
        double[] grades;
        int[] attendances;
        
        if (studentIndex == 0) { // Student1 - Nguyễn Văn An
            classIndices = new int[]{0, 1, 2};
            grades = new double[]{8.5, 8.0, 9.0};
            attendances = new int[]{90, 95, 88};
        } else if (studentIndex == 1) { // Student2 - Trần Thị Bình
            classIndices = new int[]{0, 1};
            grades = new double[]{7.5, 7.8};
            attendances = new int[]{85, 90};
        } else if (studentIndex == 2) { // Student3 - Lê Văn Cường
            classIndices = new int[]{1, 2};
            grades = new double[]{8.2, 8.5};
            attendances = new int[]{92, 87};
        } else { // Student4 - Đinh Minh Tân
            classIndices = new int[]{0, 2};
            grades = new double[]{8.8, 8.3};
            attendances = new int[]{93, 89};
        }

        for (int i = 0; i < classIndices.length && classIndices[i] < classes.size(); i++) {
            Class cls = classes.get(classIndices[i]);
            Enrollment enrollment = new Enrollment();
            enrollment.student_id = studentId;
            enrollment.class_id = cls.class_id;
            enrollment.grade = grades[i];
            enrollment.attendance = attendances[i];
            db.enrollmentDao().insert(enrollment);
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

        // Lấy enrollments của student để biết họ học lớp nào
        List<Enrollment> enrollments = db.enrollmentDao().getByStudent(studentId);
        if (enrollments == null || enrollments.isEmpty()) return;

        // Lấy schedules của các lớp mà student đã đăng ký
        List<Schedule> allSchedules = db.scheduleDao().getAll();
        if (allSchedules == null || allSchedules.isEmpty()) return;

        // Tạo attendance cho các schedule mà student đã đăng ký
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
            // Tìm schedules của lớp này
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
        
        // Lấy thông tin student để tạo feedback phù hợp
        User student = db.userDao().getUserById(studentId);
        String studentName = student != null && student.name != null ? student.name : "Sinh viên";

        // Feedback 1: về môn học
        Feedback feedback1 = new Feedback();
        feedback1.user_id = lecturer1Id;
        feedback1.student_id = studentId;
        
        // Tạo subject và content khác nhau cho từng student
        if (studentName.contains("An")) {
            feedback1.subject = "Phản hồi về môn Lập Trình Java";
            feedback1.content = "Môn học rất hay và bổ ích, giảng viên giảng dạy nhiệt tình. Em rất hài lòng với phương pháp giảng dạy.";
        } else if (studentName.contains("Bình")) {
            feedback1.subject = "Góp ý về môn Cơ Sở Dữ Liệu";
            feedback1.content = "Môn học khá khó nhưng thầy cô giảng rất dễ hiểu. Em mong có thêm bài tập thực hành.";
        } else if (studentName.contains("Cường")) {
            feedback1.subject = "Phản hồi về môn Mạng Máy Tính";
            feedback1.content = "Môn học thú vị, em học được nhiều kiến thức mới. Cảm ơn thầy cô đã nhiệt tình hỗ trợ.";
        } else { // Tân hoặc các student khác
            feedback1.subject = "Phản hồi về môn Lập Trình Java";
            feedback1.content = "Môn học rất bổ ích, em đã học được nhiều kỹ năng lập trình quan trọng. Cảm ơn thầy cô!";
        }
        
        feedback1.category = "course";
        feedback1.date = sdf.format(new Date());
        feedback1.rating = 5;
        feedback1.status = "pending";
        feedback1.response = "";
        db.feedbackDao().insert(feedback1);

        // Feedback 2: về hệ thống
        Feedback feedback2 = new Feedback();
        feedback2.user_id = lecturer2Id;
        feedback2.student_id = studentId;
        
        if (studentName.contains("An")) {
            feedback2.subject = "Góp ý về hệ thống";
            feedback2.content = "Hệ thống cần cải thiện tốc độ tải trang và thêm tính năng thông báo real-time.";
        } else if (studentName.contains("Bình")) {
            feedback2.subject = "Đề xuất cải thiện giao diện";
            feedback2.content = "Giao diện đẹp nhưng em mong có thêm chế độ tối (dark mode) để bảo vệ mắt.";
        } else if (studentName.contains("Cường")) {
            feedback2.subject = "Phản hồi về tính năng";
            feedback2.content = "Hệ thống tốt, em mong có thêm tính năng chat với giảng viên trực tiếp trên app.";
        } else { // Tân hoặc các student khác
            feedback2.subject = "Góp ý về ứng dụng";
            feedback2.content = "Ứng dụng rất tiện lợi, em mong có thêm tính năng xem điểm chi tiết từng môn học.";
        }
        
        feedback2.category = "system";
        feedback2.date = sdf.format(new Date());
        feedback2.rating = 4;
        feedback2.status = "responded";
        feedback2.response = "Cảm ơn bạn đã phản hồi. Chúng tôi sẽ xem xét và cải thiện trong thời gian tới.";
        db.feedbackDao().insert(feedback2);
    }
}

