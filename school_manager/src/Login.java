import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    // ĐÃ XÓA: private JButton registerButton;

    public Login() {
        // --- Cài đặt cơ bản cho JFrame ---
        setTitle("Hệ thống đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Panel chính ---
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // --- Form Panel chứa các thành phần đăng nhập ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // ... (Các thành phần tiêu đề, username, password giữ nguyên) ...
        JLabel titleLabel = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Mật khẩu:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(passwordField, gbc);
        
        // --- Các nút bấm ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Chỉ còn 1 nút nên căn phải
        buttonPanel.setBackground(Color.WHITE);

        loginButton = new JButton("Đăng nhập");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(59, 89, 182));
        loginButton.setForeground(Color.WHITE);

        // ĐÃ XÓA: Khởi tạo và thêm nút Đăng ký
        buttonPanel.add(loginButton);

        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel);

        // --- Xử lý sự kiện (chỉ còn sự kiện cho nút Đăng nhập) ---
        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            String userRole = Database.checkLogin(user, pass);

            if (userRole != null) {
                dispose();
                switch (userRole) {
    case "admin":
        new Admin();
        break;
    case "teacher":
        new Teacher(user);
        break;
    case "student":
        // 1. Lấy đối tượng StudentProfile chứa đầy đủ thông tin từ CSDL
        StudentProfile profile = Database.getStudentDetails(user);

        if (profile != null) {
            // 2. Tạo một cửa sổ GIAO DIỆN MỚI (lớp Student) và truyền đối tượng profile vào
            new Student(profile);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin chi tiết.");
            new Login();
        }
        break;
    default:
                        JOptionPane.showMessageDialog(this, "Vai trò không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        new Login();
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu.", "Lỗi Đăng Nhập", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ĐÃ XÓA: Khối xử lý sự kiện cho registerButton

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF.");
        }
        
        SwingUtilities.invokeLater(() -> new Login());
    }
}