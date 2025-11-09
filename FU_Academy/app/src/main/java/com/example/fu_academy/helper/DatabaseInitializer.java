package com.example.fu_academy.helper;

import android.content.Context;

import com.example.fu_academy.dao.*;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Assignment;
import com.example.fu_academy.entity.Course;
import com.example.fu_academy.entity.Enrollment;
import com.example.fu_academy.entity.Material;
import com.example.fu_academy.entity.Submission;
import com.example.fu_academy.entity.User;

import java.util.List;

public class DatabaseInitializer {

    public static void initializeData(Context context) {
        EducationDatabase db = EducationDatabase.getInstance(context);
        UserDao userDao = db.userDao();
        CourseDao courseDao = db.courseDao();
        ClassDao classDao = db.classDao();
        EnrollmentDao enrollmentDao = db.enrollmentDao();
        AssignmentDao assignmentDao = db.assignmentDao();
        MaterialDao materialDao = db.materialDao();
        SubmissionDao submissionDao = db.submissionDao();

        // Kiểm tra và tạo từng user nếu chưa tồn tại
        if (userDao.getUserByEmail("student1@fu.edu.vn") == null) {
            User student1 = new User();
            student1.name = "Nguyễn Văn An";
            student1.email = "student1@fu.edu.vn";
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
        initializeCourses(db, userDao, courseDao, classDao, enrollmentDao, assignmentDao, materialDao, submissionDao);
    }

    private static void initializeCourses(EducationDatabase db, UserDao userDao, CourseDao courseDao, 
                                         ClassDao classDao, EnrollmentDao enrollmentDao,
                                         AssignmentDao assignmentDao, MaterialDao materialDao,
                                         SubmissionDao submissionDao) {
        try {
            // Get users
            User student1 = userDao.getUserByEmail("student1@fu.edu.vn");
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
            // This ensures all 48 courses are added even if some already exist
            if (totalCourses >= 48) {
                android.util.Log.d("DatabaseInitializer", "Already have " + totalCourses + " courses, skipping initialization");
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
            com.example.fu_academy.entity.Class class1 = new com.example.fu_academy.entity.Class();
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
            com.example.fu_academy.entity.Class class2 = new com.example.fu_academy.entity.Class();
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
            com.example.fu_academy.entity.Class class3 = new com.example.fu_academy.entity.Class();
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

        } catch (Exception e) {
            android.util.Log.e("DatabaseInitializer", "Error initializing courses: " + e.getMessage(), e);
        }
    }

    private static void addMoreCourses(CourseDao courseDao, ClassDao classDao, 
                                       EnrollmentDao enrollmentDao, AssignmentDao assignmentDao,
                                       MaterialDao materialDao, User lecturer1, User lecturer2, User student1) {
        // Get current year and determine current semester
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int currentYear = cal.get(cal.YEAR);
        int currentMonth = cal.get(cal.MONTH) + 1; // 1-12
        
        // Determine current semester based on month
        // Fall: Sep-Dec (9-12), Spring: Jan-Apr (1-4), Summer: May-Aug (5-8)
        String currentSemesterType = "Fall";
        if (currentMonth >= 1 && currentMonth <= 4) {
            currentSemesterType = "Spring";
        } else if (currentMonth >= 5 && currentMonth <= 8) {
            currentSemesterType = "Summer";
        }
        String currentSemester = currentSemesterType + " " + currentYear;
        
        // Get all existing courses once to avoid multiple queries
        List<Course> allExistingCourses = courseDao.getAll();
        java.util.Set<String> existingCourseCodes = new java.util.HashSet<>();
        if (allExistingCourses != null) {
            for (Course c : allExistingCourses) {
                if (c.course_code != null) {
                    existingCourseCodes.add(c.course_code);
                }
            }
        }
        
        // Array of 45 courses with full information - spanning from 2022 to current year (2025)
        // Note: PRJ301, DBI202, NWC301 are already added in initializeCourses, so we skip them here
        String[][] courses = {
            // Code, Name, Credit, Semester, Status, Type, Room, Lecturer (1 or 2)
            // Current semester 2025 - Đang học, no grades
            {"CUR001", "Phát triển ứng dụng di động nâng cao", "3", currentSemester, "Đang học", "Bắt buộc", "P101", "1"},
            {"CUR002", "An toàn thông tin và bảo mật", "3", currentSemester, "Đang học", "Bắt buộc", "P102", "2"},
            {"CUR003", "Đồ án tốt nghiệp", "4", currentSemester, "Đang học", "Bắt buộc", "P103", "1"},
            {"CUR004", "Blockchain và ứng dụng", "2", currentSemester, "Đang học", "Tự chọn", "P104", "2"},
            {"CUR005", "Cloud Computing", "3", currentSemester, "Đang học", "Bắt buộc", "P105", "1"},
            {"CUR006", "DevOps và CI/CD", "3", currentSemester, "Đang học", "Bắt buộc", "P106", "2"},
            
            // Add other 2025 semesters - all should be "Đã học" if not current semester
            // Spring 2025
            {"SPR2501", "Lập trình Frontend nâng cao", "3", "Spring 2025", currentSemester.equals("Spring 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P201", "1"},
            {"SPR2502", "Kiến trúc phần mềm", "3", "Spring 2025", currentSemester.equals("Spring 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P202", "2"},
            {"SPR2503", "Hệ thống phân tán", "3", "Spring 2025", currentSemester.equals("Spring 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P203", "1"},
            {"SPR2504", "Deep Learning", "2", "Spring 2025", currentSemester.equals("Spring 2025") ? "Đang học" : "Đã học", "Tự chọn", "P204", "2"},
            {"SPR2505", "Big Data Analytics", "3", "Spring 2025", currentSemester.equals("Spring 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P205", "1"},
            {"SPR2506", "Internet of Things nâng cao", "3", "Spring 2025", currentSemester.equals("Spring 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P206", "2"},
            
            // Summer 2025
            {"SUM2501", "Lập trình Backend với Node.js", "3", "Summer 2025", currentSemester.equals("Summer 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P301", "1"},
            {"SUM2502", "Microservices Architecture", "3", "Summer 2025", currentSemester.equals("Summer 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P302", "2"},
            {"SUM2503", "Docker và Kubernetes", "3", "Summer 2025", currentSemester.equals("Summer 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P303", "1"},
            {"SUM2504", "Computer Vision", "2", "Summer 2025", currentSemester.equals("Summer 2025") ? "Đang học" : "Đã học", "Tự chọn", "P304", "2"},
            {"SUM2505", "Natural Language Processing", "3", "Summer 2025", currentSemester.equals("Summer 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P305", "1"},
            {"SUM2506", "Cybersecurity", "3", "Summer 2025", currentSemester.equals("Summer 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P306", "2"},
            
            // Fall 2025
            {"FAL2501", "React Native Development", "3", "Fall 2025", currentSemester.equals("Fall 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P401", "1"},
            {"FAL2502", "GraphQL và RESTful API", "3", "Fall 2025", currentSemester.equals("Fall 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P402", "2"},
            {"FAL2503", "System Design", "3", "Fall 2025", currentSemester.equals("Fall 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P403", "1"},
            {"FAL2504", "Augmented Reality", "2", "Fall 2025", currentSemester.equals("Fall 2025") ? "Đang học" : "Đã học", "Tự chọn", "P404", "2"},
            {"FAL2505", "Quantum Computing cơ bản", "3", "Fall 2025", currentSemester.equals("Fall 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P405", "1"},
            {"FAL2506", "Ethical Hacking", "3", "Fall 2025", currentSemester.equals("Fall 2025") ? "Đang học" : "Đã học", "Bắt buộc", "P406", "2"},
            
            // Previous semester (Fall 2024)
            {"GDQP", "Giáo dục quốc phòng", "2", "Fall 2024", "Đã học", "Bắt buộc", "P501", "1"},
            {"VOV124", "Vovinam 2", "2", "Fall 2024", "Đã học", "Tự chọn", "P502", "1"},
            {"TRS601", "Tiếng Anh 6", "3", "Fall 2024", "Đã học", "Bắt buộc", "P503", "2"},
            {"MAE101", "Toán cho kỹ sư", "4", "Fall 2024", "Đã học", "Bắt buộc", "P504", "1"},
            {"CSI104", "Tổ chức và kiến trúc máy tính", "3", "Fall 2024", "Đã học", "Bắt buộc", "P505", "2"},
            {"PRO192", "Nhập môn lập trình", "3", "Fall 2024", "Đã học", "Bắt buộc", "P506", "1"},
            
            // Spring 2024
            {"CEA201", "Cấu trúc dữ liệu và giải thuật", "3", "Spring 2024", "Đã học", "Bắt buộc", "P601", "1"},
            {"LAB211", "Lập trình Java nâng cao", "3", "Spring 2024", "Đã học", "Bắt buộc", "P602", "1"},
            {"IOT102", "Internet of Things", "3", "Spring 2024", "Đã học", "Tự chọn", "P603", "2"},
            {"SWE201", "Công nghệ phần mềm", "3", "Spring 2024", "Đã học", "Bắt buộc", "P604", "1"},
            {"SWR302", "Phát triển ứng dụng Web", "3", "Spring 2024", "Đã học", "Bắt buộc", "P605", "2"},
            {"SWT301", "Kiểm thử phần mềm", "3", "Spring 2024", "Đã học", "Bắt buộc", "P606", "1"},
            
            // Summer 2024
            {"PRU211", "Lập trình Python", "3", "Summer 2024", "Đã học", "Tự chọn", "P701", "1"},
            {"SWP391", "Thực tập tốt nghiệp", "4", "Summer 2024", "Đã học", "Bắt buộc", "P702", "2"},
            {"AIE201", "Trí tuệ nhân tạo", "3", "Summer 2024", "Đã học", "Tự chọn", "P703", "2"},
            {"MLN122", "Machine Learning", "3", "Summer 2024", "Đã học", "Tự chọn", "P704", "1"},
            {"BDA201", "Phân tích dữ liệu lớn", "3", "Summer 2024", "Đã học", "Tự chọn", "P705", "2"},
            {"WED201", "Thiết kế Web", "3", "Summer 2024", "Đã học", "Bắt buộc", "P706", "1"},
            
            // Fall 2023
            {"PRM392", "Quản lý dự án phần mềm", "3", "Fall 2023", "Đã học", "Bắt buộc", "P801", "1"},
            {"ENT301", "Khởi nghiệp công nghệ", "2", "Fall 2023", "Đã học", "Tự chọn", "P802", "2"},
            {"ACC101", "Nguyên lý kế toán", "3", "Fall 2023", "Đã học", "Tự chọn", "P803", "1"},
            {"ECO101", "Kinh tế học đại cương", "3", "Fall 2023", "Đã học", "Tự chọn", "P804", "2"},
            {"MKT101", "Marketing căn bản", "3", "Fall 2023", "Đã học", "Tự chọn", "P805", "1"},
            {"MAD101", "Lập trình hướng đối tượng", "3", "Fall 2023", "Đã học", "Bắt buộc", "P806", "1"},
            
            // Spring 2023
            {"FIN201", "Tài chính doanh nghiệp", "3", "Spring 2023", "Đã học", "Tự chọn", "P901", "2"},
            {"HRM201", "Quản trị nhân sự", "3", "Spring 2023", "Đã học", "Tự chọn", "P902", "1"},
            {"LAW101", "Pháp luật đại cương", "2", "Spring 2023", "Đã học", "Bắt buộc", "P903", "2"},
            {"PHI101", "Triết học Mác-Lênin", "2", "Spring 2023", "Đã học", "Bắt buộc", "P904", "1"},
            {"HIS101", "Lịch sử Đảng", "2", "Spring 2023", "Đã học", "Bắt buộc", "P905", "2"},
            {"OSG202", "Toán rời rạc", "3", "Spring 2023", "Đã học", "Bắt buộc", "P906", "1"},
            
            // Summer 2023
            {"POL101", "Chủ nghĩa xã hội khoa học", "2", "Summer 2023", "Đã học", "Bắt buộc", "P1001", "1"},
            {"PSY101", "Tâm lý học đại cương", "2", "Summer 2023", "Đã học", "Tự chọn", "P1002", "2"},
            {"SOC101", "Xã hội học đại cương", "2", "Summer 2023", "Đã học", "Tự chọn", "P1003", "1"},
            {"NWC203c", "Hệ điều hành", "3", "Summer 2023", "Đã học", "Bắt buộc", "P1004", "1"},
            {"SSG104", "Mạng máy tính", "3", "Summer 2023", "Đã học", "Bắt buộc", "P1005", "2"},
            {"NWC302", "Mạng máy tính nâng cao", "3", "Summer 2023", "Đã học", "Bắt buộc", "P1006", "1"},
            
            // Fall 2022
            {"ENG101", "Tiếng Anh 1", "3", "Fall 2022", "Đã học", "Bắt buộc", "P1101", "2"},
            {"ENG102", "Tiếng Anh 2", "3", "Fall 2022", "Đã học", "Bắt buộc", "P1102", "1"},
            {"ENG103", "Tiếng Anh 3", "3", "Fall 2022", "Đã học", "Bắt buộc", "P1103", "2"},
            {"JPD113", "Kỹ năng giao tiếp và làm việc nhóm", "2", "Fall 2022", "Đã học", "Tự chọn", "P1104", "1"},
            {"CSD201", "Tiếng Nhật sơ cấp 1", "3", "Fall 2022", "Đã học", "Tự chọn", "P1105", "2"},
            {"MOB201", "Lập trình di động", "3", "Fall 2022", "Đã học", "Bắt buộc", "P1106", "2"},
            
            // Spring 2022
            {"MAS291", "Xác suất thống kê", "3", "Spring 2022", "Đã học", "Bắt buộc", "P1201", "1"},
            {"JPD123", "Tiếng Nhật sơ cấp 2", "3", "Spring 2022", "Đã học", "Tự chọn", "P1202", "2"},
            {"VOV134", "Vovinam 3", "2", "Spring 2022", "Đã học", "Tự chọn", "P1203", "1"},
            {"PRF192", "Lập trình C cơ bản", "3", "Spring 2022", "Đã học", "Bắt buộc", "P1204", "2"},
            {"DBI203", "Cơ sở dữ liệu nâng cao", "3", "Spring 2022", "Đã học", "Bắt buộc", "P1205", "2"},
            {"NWC303", "Mạng máy tính chuyên sâu", "3", "Spring 2022", "Đã học", "Bắt buộc", "P1206", "1"}
        };

        for (String[] courseData : courses) {
            try {
                // Check if course already exists using the pre-loaded set
                if (existingCourseCodes.contains(courseData[0])) {
                    continue; // Skip if already exists
                }

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
                com.example.fu_academy.entity.Class classObj = new com.example.fu_academy.entity.Class();
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
                // Check if this is the current semester (newest, đang học)
                boolean isCurrentSemester = courseData[3].equals(currentSemester) && "Đang học".equals(courseData[4]);
                
                if ("Đã học".equals(courseData[4]) || (!isCurrentSemester && "Đang học".equals(courseData[4]))) {
                    // Past semesters or courses that are "Đã học" - have grades
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
                    // Current semester (newest) - no grades yet, đang học
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
                android.util.Log.e("DatabaseInitializer", "Error adding course " + courseData[0] + ": " + e.getMessage());
            }
        }
    }
}
