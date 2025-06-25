// File: Database.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;


public class Database {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/school_manager";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String checkLogin(String username, String password) {
        String sql = "SELECT role FROM users WHERE username=? AND password=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean registerUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error when registering user: " + e.getMessage());
            return false;
        }
    }

   public static boolean addStudentProfile(String studentId, String name, String major, String birthDate) {
        // SỬA ĐỔI 1: Đổi tên cột "class" thành "major" và "dob" thành "birth_date"
        String sql = "INSERT INTO students (id, name, major, birth_date) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            stmt.setString(2, name);
            stmt.setString(3, major); // Gán cho cột "major"
            stmt.setDate(4, java.sql.Date.valueOf(birthDate)); // Gán cho cột "birth_date"

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static StudentProfile getStudentDetails(String studentId) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String major = rs.getString("major");
                Date birthDate = rs.getDate("birth_date");
                
                // Trả về một đối tượng StudentProfile chứa đầy đủ thông tin
                return new StudentProfile(id, name, major, birthDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public static List<Object[]> getStudentGrades(String studentId) {
    List<Object[]> gradesList = new ArrayList<>();
    // Câu lệnh JOIN để lấy tên môn học và số tín chỉ cùng với điểm
    String sql = "SELECT s.subject_id, s.subject_name, s.credits, g.score " +
                 "FROM grades g " +
                 "JOIN subjects s ON g.subject_id = s.subject_id " +
                 "WHERE g.student_id = ?";
    
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, studentId);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Object[] row = new Object[] {
                rs.getString("subject_id"),
                rs.getString("subject_name"),
                rs.getInt("credits"),
                rs.getFloat("score")
            };
            gradesList.add(row);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return gradesList;
}
    
    public static List<Object[]> getAllSubjects() {
    List<Object[]> list = new ArrayList<>();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM subjects")) {
        while (rs.next()) {
            list.add(new Object[]{rs.getString("subject_id"), rs.getString("subject_name"), rs.getInt("credits")});
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return list;
}

public static void addSubject(String id, String name, int credits) {
    String sql = "INSERT INTO subjects(subject_id, subject_name, credits) VALUES(?, ?, ?)";
    try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, id);
        stmt.setString(2, name);
        stmt.setInt(3, credits);
        stmt.executeUpdate();
    } catch (SQLException e) { e.printStackTrace(); }
}

public static void deleteSubject(String id) {
    String sql = "DELETE FROM subjects WHERE subject_id = ?";
    try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, id);
        stmt.executeUpdate();
    } catch (SQLException e) { e.printStackTrace(); }
}
    
// Thêm các phương thức này vào file Database.java

public static List<String> getAllStudentIDs() {
    List<String> ids = new ArrayList<>();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT id FROM students")) {
        while (rs.next()) {
            ids.add(rs.getString("id"));
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return ids;
}

public static List<String> getAllSubjectIDs() {
    List<String> ids = new ArrayList<>();
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT subject_id FROM subjects")) {
        while (rs.next()) {
            ids.add(rs.getString("subject_id"));
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return ids;
}

// Phương thức này sẽ thêm mới nếu chưa có, hoặc cập nhật nếu đã có điểm
public static void saveGrade(String studentId, String subjectId, float score) {
    String sql = "INSERT INTO grades (student_id, subject_id, score) VALUES (?, ?, ?) " +
                 "ON DUPLICATE KEY UPDATE score = VALUES(score)";
    try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, studentId);
        stmt.setString(2, subjectId);
        stmt.setFloat(3, score);
        stmt.executeUpdate();
    } catch (SQLException e) { e.printStackTrace(); }
}    

public static Map<String, String> getAllSubjectsForComboBox() {
    // Sử dụng LinkedHashMap để duy trì thứ tự chèn
    Map<String, String> subjects = new LinkedHashMap<>();
    String sql = "SELECT subject_id, subject_name FROM subjects ORDER BY subject_name";
    
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            // Key là Tên môn học (để hiển thị), Value là Mã môn học (để lưu)
            subjects.put(rs.getString("subject_name"), rs.getString("subject_id"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return subjects;
}

public static List<Object[]> getTeachingScheduleForTeacher(String teacherId) {
    List<Object[]> scheduleList = new ArrayList<>();
    // Câu lệnh JOIN phức tạp để lấy thông tin từ nhiều bảng
    String sql = "SELECT c.class_name, s.subject_name, ta.schedule_info, " +
                 "(SELECT COUNT(*) FROM class_enrollments ce WHERE ce.class_id = c.class_id) AS class_size " +
                 "FROM teaching_assignments ta " +
                 "JOIN classes c ON ta.class_id = c.class_id " +
                 "JOIN subjects s ON ta.subject_id = s.subject_id " +
                 "WHERE ta.teacher_id = ?";
    
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, teacherId);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Object[] row = new Object[]{
                rs.getString("class_name"),
                rs.getString("subject_name"),
                rs.getInt("class_size"),
                rs.getString("schedule_info")
            };
            scheduleList.add(row);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return scheduleList;
}

// Thêm phương thức này vào file Database.java
public static List<Object[]> getAllStudents() {
    List<Object[]> list = new ArrayList<>();
    String sql = "SELECT id, name, major, birth_date FROM students ORDER BY name";
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            list.add(new Object[]{
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("major"),
                rs.getDate("birth_date")
            });
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

// Thêm các phương thức này vào file Database.java

public static List<String> getAllTeachers() {
    List<String> teachers = new ArrayList<>();
    // Lấy username của các user có vai trò là 'teacher'
    String sql = "SELECT username FROM users WHERE role = 'teacher'";
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            teachers.add(rs.getString("username"));
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return teachers;
}

public static List<String> getAllClassIDs() {
    List<String> classIDs = new ArrayList<>();
    String sql = "SELECT class_id FROM classes";
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            classIDs.add(rs.getString("class_id"));
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return classIDs;
}

public static boolean addTeachingAssignment(String teacherId, String subjectId, String classId, String scheduleInfo) {
    String sql = "INSERT INTO teaching_assignments (teacher_id, subject_id, class_id, schedule_info) VALUES (?, ?, ?, ?)";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, teacherId);
        stmt.setString(2, subjectId);
        stmt.setString(3, classId);
        stmt.setString(4, scheduleInfo);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

// Lấy tất cả các phân công để admin xem
public static List<Object[]> getAllAssignments() {
    List<Object[]> assignments = new ArrayList<>();
    String sql = "SELECT ta.teacher_id, s.subject_name, c.class_name, ta.schedule_info " +
                 "FROM teaching_assignments ta " +
                 "JOIN subjects s ON ta.subject_id = s.subject_id " +
                 "JOIN classes c ON ta.class_id = c.class_id";
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            assignments.add(new Object[]{
                rs.getString("teacher_id"),
                rs.getString("subject_name"),
                rs.getString("class_name"),
                rs.getString("schedule_info")
            });
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return assignments;
}

}