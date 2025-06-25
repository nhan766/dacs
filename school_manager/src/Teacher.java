// File: Teacher.java
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Teacher extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private final String SCHEDULE_PANEL = "Card with Schedule";
    private final String STUDENT_LIST_PANEL = "Card with Student List";

    public Teacher(String teacherUsername) {
        setTitle("Teacher Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Panel Menu bên trái ---
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(45, 62, 80));
        menuPanel.setPreferredSize(new Dimension(200, 0));

        JLabel teacherNameLabel = new JLabel("GV: " + teacherUsername);
        teacherNameLabel.setForeground(Color.WHITE);
        teacherNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        teacherNameLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        teacherNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton scheduleButton = new JButton("Lịch giảng dạy");
        JButton studentListButton = new JButton("Danh sách sinh viên");
        JButton logoutButton = new JButton("Đăng xuất");

        styleMenuButton(scheduleButton);
        styleMenuButton(studentListButton);
        styleMenuButton(logoutButton);

        menuPanel.add(teacherNameLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(scheduleButton);
        menuPanel.add(studentListButton);
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(logoutButton);

        // --- Panel Nội dung bên phải ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        JPanel scheduleCard = createSchedulePanel(teacherUsername);
        JPanel studentListCard = createStudentListPanel(); // Panel xem danh sách SV

        contentPanel.add(scheduleCard, SCHEDULE_PANEL);
        contentPanel.add(studentListCard, STUDENT_LIST_PANEL);
        
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // --- Xử lý sự kiện ---
        scheduleButton.addActionListener(e -> cardLayout.show(contentPanel, SCHEDULE_PANEL));
        studentListButton.addActionListener(e -> cardLayout.show(contentPanel, STUDENT_LIST_PANEL));

        // SỬA LỖI: Sửa lại ActionListener của nút Đăng xuất cho đúng
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    Teacher.this, // Sửa từ Admin.this thành Teacher.this
                    "Bạn có chắc chắn muốn đăng xuất không?",
                    "Xác nhận Đăng xuất",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new Login();
            }
        });

        setVisible(true);
    }

    // BỔ SUNG: Hoàn thiện phương thức style cho nút
    private void styleMenuButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
    }
    
    // Panel hiển thị Lịch giảng dạy
    private JPanel createSchedulePanel(String teacherId) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        String[] columnNames = {"Lớp học", "Môn học Giảng dạy", "Sĩ số", "Lịch dạy (Thứ, Tiết, Phòng)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable scheduleTable = new JTable(tableModel);
        
        List<Object[]> scheduleData = Database.getTeachingScheduleForTeacher(teacherId);
        for (Object[] row : scheduleData) {
            tableModel.addRow(row);
        }
        
        panel.add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        return panel;
    }

    // BỔ SUNG: Hoàn thiện Panel hiển thị Danh sách sinh viên
    private JPanel createStudentListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] columnNames = {"Mã SV", "Họ tên", "Ngành học/Lớp", "Ngày Sinh"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable studentTable = new JTable(tableModel);
        studentTable.setEnabled(false); // Chỉ cho phép xem

        // Lấy dữ liệu từ CSDL
        List<Object[]> studentData = Database.getAllStudents();
        for (Object[] row : studentData) {
            tableModel.addRow(row);
        }

        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        return panel;
    }
}