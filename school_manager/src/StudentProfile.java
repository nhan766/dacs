// File: StudentProfile.java
import java.sql.Date;

public class StudentProfile {
    // Các trường dữ liệu private
    private String id;
    private String name;
    private String major;
    private Date dob;

    // Hàm khởi tạo để tạo đối tượng từ dữ liệu
    public StudentProfile(String id, String name, String major, Date dob) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.dob = dob;
    }

    // Các hàm public "getter" để các lớp khác có thể đọc được thông tin
    public String getId() { return id; }
    public String getName() { return name; }
    public String getMajor() { return major; }
    public Date getDob() { return dob; }
}