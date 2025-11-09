package com.example.fu_academy.helper;

import android.content.Context;

import com.example.fu_academy.dao.UserDao;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.User;

public class DatabaseInitializer {

    public static void initializeData(Context context) {
        EducationDatabase db = EducationDatabase.getInstance(context);
        UserDao userDao = db.userDao();

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
    }
}
