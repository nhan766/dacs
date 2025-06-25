// File: AssignmentManager.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Assignment extends JPanel {
    private JTable assignmentTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> teacherComboBox, subjectComboBox, classComboBox;

    public Assignment() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Panel Form để nhập liệu ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        teacherComboBox = new JComboBox<>();
        subjectComboBox = new JComboBox<>();
        classComboBox = new JComboBox<>();
        JTextField scheduleField = new JTextField(20);
        JButton assignButton = new JButton("Lưu Phân Công");

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Chọn Giáo viên:"), gbc);
        gbc.gridx = 1; formPanel.add(teacherComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Chọn Môn học:"), gbc);
        gbc.gridx = 1; formPanel.add(subjectComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Chọn Lớp:"), gbc);
        gbc.gridx = 1; formPanel.add(classComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Thông tin Lịch dạy:"), gbc);
        gbc.gridx = 1; formPanel.add(scheduleField, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST; formPanel.add(assignButton, gbc);
        
        add(formPanel, BorderLayout.NORTH);

        // --- Bảng hiển thị các phân công đã có ---
        tableModel = new DefaultTableModel(new String[]{"Giáo viên", "Môn học", "Lớp", "Lịch dạy"}, 0);
        assignmentTable = new JTable(tableModel);
        add(new JScrollPane(assignmentTable), BorderLayout.CENTER);

        // Xử lý sự kiện cho nút
        assignButton.addActionListener(e -> {
            String teacherId = (String) teacherComboBox.getSelectedItem();
            String subjectId = (String) subjectComboBox.getSelectedItem(); // Giả sử ComboBox này lưu mã môn
            String classId = (String) classComboBox.getSelectedItem();
            String scheduleInfo = scheduleField.getText();

            if (Database.addTeachingAssignment(teacherId, subjectId, classId, scheduleInfo)) {
                JOptionPane.showMessageDialog(this, "Phân công giảng dạy thành công!");
                refreshData(); // Tải lại bảng sau khi thêm
            } else {
                JOptionPane.showMessageDialog(this, "Phân công thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Tải dữ liệu ban đầu
        refreshData();
    }

    // Phương thức để làm mới tất cả dữ liệu trên panel
    public void refreshData() {
        // Làm mới ComboBox giáo viên
        teacherComboBox.removeAllItems();
        Database.getAllTeachers().forEach(teacherComboBox::addItem);

        // Làm mới ComboBox môn học
        subjectComboBox.removeAllItems();
        Database.getAllSubjectIDs().forEach(subjectComboBox::addItem); // Giả sử đã có phương thức này

        // Làm mới ComboBox lớp học
        classComboBox.removeAllItems();
        Database.getAllClassIDs().forEach(classComboBox::addItem);

        // Làm mới bảng phân công
        tableModel.setRowCount(0);
        List<Object[]> assignments = Database.getAllAssignments();
        for (Object[] row : assignments) {
            tableModel.addRow(row);
        }
    }
}