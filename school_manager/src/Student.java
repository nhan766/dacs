// File: Student.java (Đây là lớp GIAO DIỆN - CỬA SỔ)
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class Student extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private final String INFO_PANEL = "Card with Student Info";
    private final String GRADES_PANEL = "Card with Student Grades";

    // SỬA ĐỔI QUAN TRỌNG: Hàm khởi tạo phải nhận vào đối tượng StudentProfile (lớp dữ liệu)
    public Student(StudentProfile profile) {
        setTitle("Student Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Panel Menu bên trái ---
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(45, 62, 80));
        menuPanel.setPreferredSize(new Dimension(200, 0));

        // SỬA ĐỔI: Lấy tên từ đối tượng 'profile'
        JLabel studentNameLabel = new JLabel(profile.getName());
        studentNameLabel.setForeground(Color.WHITE);
        studentNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        studentNameLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        studentNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton infoButton = new JButton("Thông tin cá nhân");
        JButton gradesButton = new JButton("Bảng điểm");
        JButton logoutButton = new JButton("Đăng xuất");

        styleMenuButton(infoButton);
        styleMenuButton(gradesButton);
        styleMenuButton(logoutButton);

        menuPanel.add(studentNameLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(infoButton);
        menuPanel.add(gradesButton);
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(logoutButton);

        // --- Panel Nội dung bên phải (sử dụng CardLayout) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Tạo các "card" (màn hình chức năng)
        JPanel infoCard = createInfoPanel(profile); // Truyền đối tượng profile
        JPanel gradesCard = createGradesPanel(profile.getId()); // Truyền mã sinh viên từ profile

        // THÊM LẠI PHẦN BỊ THIẾU: Thêm gradesCard vào contentPanel
        contentPanel.add(infoCard, INFO_PANEL);
        contentPanel.add(gradesCard, GRADES_PANEL);
        
        // Thêm các panel vào Frame
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // --- Xử lý sự kiện ---
        infoButton.addActionListener(e -> cardLayout.show(contentPanel, INFO_PANEL));
        gradesButton.addActionListener(e -> cardLayout.show(contentPanel, GRADES_PANEL));
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new Login();
            }
        });

        setVisible(true);
    }

    private void styleMenuButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
    }
    
    // SỬA ĐỔI: Phương thức này nhận vào StudentProfile và hoàn thiện lại
    private JPanel createInfoPanel(StudentProfile profile) {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 10, 5);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Mã sinh viên
        gbc.gridx = 0; gbc.gridy = 0; infoPanel.add(new JLabel("Mã sinh viên:"), gbc);
        gbc.gridx = 1; infoPanel.add(new JLabel(profile.getId()), gbc);

        // Họ và tên
        gbc.gridy = 1; gbc.gridx = 0; infoPanel.add(new JLabel("Họ và tên:"), gbc);
        gbc.gridx = 1; infoPanel.add(new JLabel(profile.getName()), gbc);

        // Ngành học/Lớp
        gbc.gridy = 2; gbc.gridx = 0; infoPanel.add(new JLabel("Ngành học/Lớp:"), gbc);
        gbc.gridx = 1; infoPanel.add(new JLabel(profile.getMajor()), gbc);

        // Ngày sinh
        gbc.gridy = 3; gbc.gridx = 0; infoPanel.add(new JLabel("Ngày sinh:"), gbc);
        gbc.gridx = 1;
        String formattedDate = profile.getDob() != null ? new SimpleDateFormat("dd/MM/yyyy").format(profile.getDob()) : "N/A";
        infoPanel.add(new JLabel(formattedDate), gbc);
        
        return infoPanel;
    }

    // Panel hiển thị bảng điểm
    private JPanel createGradesPanel(String studentId) {
        JPanel gradesPanel = new JPanel(new BorderLayout());
        gradesPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        String[] columnNames = {"Mã Môn học", "Tên Môn học", "Số tín chỉ", "Điểm"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable gradesTable = new JTable(tableModel);
        
        List<Object[]> gradesData = Database.getStudentGrades(studentId);
        for (Object[] row : gradesData) {
            tableModel.addRow(row);
        }
        
        gradesPanel.add(new JScrollPane(gradesTable), BorderLayout.CENTER);
        return gradesPanel;
    }
}