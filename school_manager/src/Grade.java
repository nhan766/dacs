// File: GradeManager.java
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Grade extends JPanel {
    private JComboBox<String> studentComboBox;
    private JComboBox<String> subjectNameComboBox; // Đổi tên để rõ nghĩa
    private JTextField scoreField;
    // THAY ĐỔI 1: Tạo một Map để lưu trữ cặp Tên môn học -> Mã môn học
    private Map<String, String> subjectMap;

    public Grade() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        studentComboBox = new JComboBox<>();
        subjectNameComboBox = new JComboBox<>(); // ComboBox này sẽ hiển thị Tên
        scoreField = new JTextField(15);
        JButton saveButton = new JButton("Lưu Điểm");

        // Thêm các thành phần vào panel
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Chọn Sinh viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; add(studentComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Chọn Môn học:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(subjectNameComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Nhập điểm:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(scoreField, gbc);
        gbc.gridy = 3; gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST; add(saveButton, gbc);
        
        refreshData();

        saveButton.addActionListener(e -> {
            String studentId = (String) studentComboBox.getSelectedItem();
            // THAY ĐỔI 2: Lấy Tên môn học được chọn từ ComboBox
            String selectedSubjectName = (String) subjectNameComboBox.getSelectedItem();
            
            if (studentId == null || selectedSubjectName == null) {
                 JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên và môn học.");
                 return;
            }

            // THAY ĐỔI 3: Dùng Tên để tra cứu ra Mã môn học tương ứng từ Map
            String subjectId = subjectMap.get(selectedSubjectName);

            try {
                float score = Float.parseFloat(scoreField.getText());
                // Lưu Mã môn học vào CSDL
                Database.saveGrade(studentId, subjectId, score);
                JOptionPane.showMessageDialog(this, "Đã lưu điểm thành công!");
                scoreField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Điểm phải là một con số (ví dụ: 8.5).");
            }
        });
    }

    public void refreshData() {
        // Làm mới danh sách sinh viên
        studentComboBox.removeAllItems();
        Database.getAllStudentIDs().forEach(studentComboBox::addItem);

        // THAY ĐỔI 4: Làm mới danh sách môn học
        subjectNameComboBox.removeAllItems();
        // Gọi phương thức mới để lấy cả mã và tên
        subjectMap = Database.getAllSubjectsForComboBox();
        // Chỉ thêm Tên môn học (keys của Map) vào ComboBox để hiển thị
        for (String subjectName : subjectMap.keySet()) {
            subjectNameComboBox.addItem(subjectName);
        }
    }
}