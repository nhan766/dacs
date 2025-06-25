import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

public class Admin extends JFrame {

    public Admin() {
        setTitle("Admin Control Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // --- PHẦN MỚI: TẠO TAB "TRANG CHỦ" ---
        JPanel homePanel = new JPanel(new GridBagLayout());
        JLabel welcomeLabel = new JLabel("Chào mừng Admin đến với Bảng điều khiển!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(60, 63, 65));
        homePanel.add(welcomeLabel); // Thêm nhãn chào mừng vào panel

        // Thêm tab Trang chủ vào vị trí ĐẦU TIÊN để nó hiển thị mặc định
        tabbedPane.addTab("Trang chủ", homePanel);


        // Tab 2: Quản lý sinh viên (Sử dụng lại StudentManager.java)
        // Lưu ý: Để đoạn mã này chạy, bạn cần đảm bảo lớp StudentManager của bạn là một JPanel
        StudentManager studentManagerPanel = new StudentManager();
        tabbedPane.addTab("Quản Lý Sinh Viên", studentManagerPanel);

        // THÊM MỚI: Tab 3: Quản lý Môn học
        Subject subjectManagerPanel = new Subject();
        tabbedPane.addTab("Quản Lý Môn Học", subjectManagerPanel);

        // THÊM MỚI: Tab 4: Nhập Điểm
        Grade gradeManagerPanel = new Grade();
        tabbedPane.addTab("Nhập Điểm", gradeManagerPanel);
        
        // THÊM MỚI: Tab Phân Công Dạy
        Assignment assignmentPanel = new Assignment();
        tabbedPane.addTab("Phân Công Dạy", assignmentPanel);
        
       
    

        // Tab 3: Tạo tài khoản mới cho Giáo viên/Sinh viên
        JPanel createUserPanel = createCreateUserPanel();
        tabbedPane.addTab("Tạo Tài Khoản Mới", createUserPanel);
        
        
        tabbedPane.addChangeListener(e -> {
        // Lấy panel của tab vừa được chọn
        Component selectedComponent = tabbedPane.getSelectedComponent();
        
         if (selectedComponent instanceof Assignment) {
            ((Assignment) selectedComponent).refreshData();
        }

        // Nếu người dùng vừa chọn tab "Nhập Điểm", gọi phương thức làm mới của nó
        if (selectedComponent instanceof Grade) {
            ((Grade) selectedComponent).refreshData();
        }
        // Tương tự, nếu tab "Quản lý Môn học" cũng cần làm mới
        if (selectedComponent instanceof Subject) {
        }
    });


        // Thêm JTabbedPane vào vị trí trung tâm của JFrame
        add(tabbedPane, BorderLayout.CENTER);


        // --- Panel chứa nút Đăng xuất ---
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        southPanel.add(logoutButton);
        add(southPanel, BorderLayout.SOUTH);

        // Xử lý sự kiện cho nút Đăng xuất
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        Admin.this,
                        "Bạn có chắc chắn muốn đăng xuất không?",
                        "Xác nhận Đăng xuất",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    new Login();
                }
            }
        });

        setVisible(true);
    }

    private JPanel createCreateUserPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // --- Các thành phần chung ---
    JTextField usernameField = new JTextField(15); // Rút ngắn ô để có chỗ cho nút
    JTextField passwordField = new JTextField(15);
    JButton generatePasswordButton = new JButton("Tạo MK");
    String[] roles = { "student", "teacher" };
    JComboBox<String> roleComboBox = new JComboBox<>(roles);
    
    // --- Các thành phần chỉ dành cho sinh viên ---
    JLabel studentNameLabel = new JLabel("Họ tên sinh viên:");
    JTextField studentNameField = new JTextField(20);
    JLabel studentClassLabel = new JLabel("Lớp:");
    JTextField studentClassField = new JTextField(20);
    JLabel studentDobLabel = new JLabel("Ngày sinh (yyyy-mm-dd):");
    JTextField studentDobField = new JTextField(20);
    
    // Đưa các thành phần của sinh viên vào một panel riêng để dễ ẩn/hiện
    JPanel studentDetailsPanel = new JPanel(new GridBagLayout());
    GridBagConstraints sgbc = new GridBagConstraints();
    sgbc.insets = new Insets(5, 5, 5, 5);
    sgbc.fill = GridBagConstraints.HORIZONTAL;

    sgbc.gridx = 0; sgbc.gridy = 0; studentDetailsPanel.add(studentNameLabel, sgbc);
    sgbc.gridx = 1; sgbc.gridy = 0; studentDetailsPanel.add(studentNameField, sgbc);
    sgbc.gridx = 0; sgbc.gridy = 1; studentDetailsPanel.add(studentClassLabel, sgbc);
    sgbc.gridx = 1; sgbc.gridy = 1; studentDetailsPanel.add(studentClassField, sgbc);
    sgbc.gridx = 0; sgbc.gridy = 2; studentDetailsPanel.add(studentDobLabel, sgbc);
    sgbc.gridx = 1; sgbc.gridy = 2; studentDetailsPanel.add(studentDobField, sgbc);

    // --- Sắp xếp các thành phần trên panel chính ---
    gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Mã SV / Tên đăng nhập:"), gbc);
    gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; // Kéo dài 2 cột
    panel.add(usernameField, gbc);
    gbc.gridwidth = 1; // Reset lại

    // Mật khẩu
    gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Mật khẩu:"), gbc);
    gbc.gridx = 1; gbc.gridy = 1; panel.add(passwordField, gbc);
    gbc.gridx = 2; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; // Nút không cần co giãn
    panel.add(generatePasswordButton, gbc);
    gbc.fill = GridBagConstraints.HORIZONTAL; // Đặt lại cho các thành phần sau

    // Vai trò
    gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Vai trò:"), gbc);
    gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
    panel.add(roleComboBox, gbc);
    gbc.gridwidth = 1;
    
    // Thêm studentDetailsPanel vào panel chính
    gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
    panel.add(studentDetailsPanel, gbc);
    gbc.gridwidth = 1;

    // Nút Tạo tài khoản
    JButton createButton = new JButton("Tạo tài khoản");
    gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.EAST;
    panel.add(createButton, gbc);

    // --- Logic Ẩn/Hiện Panel ---
    // Mặc định ban đầu, panel thông tin sinh viên sẽ hiển thị
    
    // --- Xử lý sự kiện cho nút mới ---
    generatePasswordButton.addActionListener(e -> {
        String generatedPassword = generateRandomPassword();
        passwordField.setText(generatedPassword);
    });
    
    studentDetailsPanel.setVisible(true);

    roleComboBox.addActionListener(e -> {
        String selectedRole = (String) roleComboBox.getSelectedItem();
        studentDetailsPanel.setVisible("student".equals(selectedRole));
    });

    // --- Logic cho nút Tạo tài khoản ---
    createButton.addActionListener(e -> {
        String username = usernameField.getText(); // Đây cũng là student_id
        String password = passwordField.getText();
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Tên đăng nhập và mật khẩu không được để trống.");
            return;
        }

        // Bước 1: Tạo tài khoản đăng nhập trong bảng 'users'
        boolean userCreated = Database.registerUser(username, password, role);

        if (userCreated) {
            // Nếu vai trò là sinh viên, tiếp tục thêm hồ sơ vào bảng 'students'
            if ("student".equals(role)) {
                String fullName = studentNameField.getText();
                String className = studentClassField.getText();
                String dob = studentDobField.getText();

                if(fullName.isEmpty() || className.isEmpty() || dob.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Vui lòng nhập đầy đủ thông tin chi tiết cho sinh viên.");
                    // Cân nhắc xóa user vừa tạo nếu thông tin chi tiết không hợp lệ
                    return;
                }

                // Bước 2: Tạo hồ sơ sinh viên trong bảng 'students'
                boolean profileCreated = Database.addStudentProfile(username, fullName, className, dob);
                if (profileCreated) {
                    JOptionPane.showMessageDialog(panel, "Tạo tài khoản và hồ sơ sinh viên thành công!");
                } else {
                    JOptionPane.showMessageDialog(panel, "Tạo tài khoản đăng nhập thành công, nhưng tạo hồ sơ sinh viên thất bại.");
                }
            } else { // Nếu là giáo viên
                JOptionPane.showMessageDialog(panel, "Tạo tài khoản giáo viên thành công!");
            }
            // Xóa trống các trường
            usernameField.setText("");
            passwordField.setText("");
            studentNameField.setText("");
            studentClassField.setText("");
            studentDobField.setText("");
        } else {
            JOptionPane.showMessageDialog(panel, "Tạo tài khoản thất bại! Tên đăng nhập có thể đã tồn tại.");
        }
    });

    
    return panel;
}
    
    private String generateRandomPassword() {
    // Các ký tự sẽ được sử dụng trong mật khẩu
    String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    // Sử dụng SecureRandom để đảm bảo tính ngẫu nhiên an toàn
    SecureRandom random = new SecureRandom();
    StringBuilder password = new StringBuilder(10); // Đặt độ dài mật khẩu là 10

    for (int i = 0; i < 10; i++) {
        int randomIndex = random.nextInt(CHARS.length());
        password.append(CHARS.charAt(randomIndex));
    }

    return password.toString();
}
    
}