// File: SubjectManager.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Subject extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public Subject() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Bảng hiển thị
        model = new DefaultTableModel(new String[]{"Mã Môn học", "Tên Môn học", "Số tín chỉ"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Panel chức năng
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnAdd = new JButton("Thêm Môn học");
        JButton btnDelete = new JButton("Xóa Môn học");
        JButton btnRefresh = new JButton("Làm mới");
        
        panel.add(btnAdd);
        panel.add(btnDelete);
        panel.add(btnRefresh);
        add(panel, BorderLayout.SOUTH);

        loadSubjects();

        // Sự kiện
        btnRefresh.addActionListener(e -> loadSubjects());
        
        btnAdd.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "Nhập Mã môn học (ví dụ: IT601):");
            if (id == null || id.trim().isEmpty()) return;
            String name = JOptionPane.showInputDialog(this, "Nhập Tên môn học:");
            if (name == null || name.trim().isEmpty()) return;
            String creditsStr = JOptionPane.showInputDialog(this, "Nhập Số tín chỉ:");
             if (creditsStr == null || creditsStr.trim().isEmpty()) return;

            try {
                int credits = Integer.parseInt(creditsStr);
                Database.addSubject(id, name, credits);
                loadSubjects();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số tín chỉ phải là một con số nguyên.");
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String subjectId = (String) model.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa môn học '" + subjectId + "' không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Database.deleteSubject(subjectId);
                    loadSubjects();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một môn học để xóa.");
            }
        });
    }
    
    private void loadSubjects() {
        model.setRowCount(0);
        List<Object[]> subjects = Database.getAllSubjects();
        for (Object[] subject : subjects) {
            model.addRow(subject);
        }
    }
}