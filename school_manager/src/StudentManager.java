// File: StudentManager.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

// Lớp này phải là một JPanel để có thể thêm vào JTabbedPane
public class StudentManager extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public StudentManager() {
        setLayout(new BorderLayout());

        // Bảng hiển thị
        model = new DefaultTableModel(new String[]{"Mã SV", "Họ tên", "Ngành học/Lớp", "Ngày sinh"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Nút chức năng
        JPanel panel = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        panel.add(btnAdd);
        panel.add(btnDelete);
        panel.add(btnRefresh);
        add(panel, BorderLayout.SOUTH);

        // Tải dữ liệu ban đầu
        loadStudents();

        // Sự kiện thêm sinh viên (ví dụ đơn giản)
        btnAdd.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Vui lòng sử dụng tab 'Tạo Tài Khoản Mới' để thêm sinh viên một cách đầy đủ.");
        });

        // Sự kiện xóa sinh viên
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên để xóa.");
                return;
            }

            // Lấy mã sinh viên từ cột đầu tiên của hàng được chọn
            String studentId = (String) model.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sinh viên '" + studentId + "'?\nHành động này cũng sẽ xóa tài khoản đăng nhập tương ứng.", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Thêm logic để xóa sinh viên khỏi cả bảng `students` và `users`
                // (Cần viết thêm một phương thức trong Database.java để xử lý việc này)
            }
        });

        // Sự kiện cho nút Làm mới
        btnRefresh.addActionListener(e -> loadStudents());
    }

    private void loadStudents() {
        // Xóa dữ liệu cũ trên bảng
        model.setRowCount(0);

        // Câu lệnh SQL để lấy tất cả sinh viên
        String sql = "SELECT * FROM students";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Duyệt qua từng kết quả và thêm vào bảng
            while (rs.next()) {
                // SỬA ĐỔI QUAN TRỌNG: Sử dụng đúng tên cột từ CSDL
                String id = rs.getString("id");
                String name = rs.getString("name");
                String major = rs.getString("major");
                java.sql.Date birthDate = rs.getDate("birth_date");

                model.addRow(new Object[]{id, name, major, birthDate});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu sinh viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}