import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Input extends JFrame {
    private final JTextField nameField  = new JTextField(40);
    private final JTextField ageField   = new JTextField(6);
    private final JTextField emailField = new JTextField(200);
    private final JButton saveButt      = new JButton("Save");

    public Input() {
        super("Add User");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridLayout(0, 1, 6, 6)); // rows x 1

        panel.add(new JLabel("Name"));
        panel.add(nameField);
        panel.add(new JLabel("Age"));
        panel.add(ageField);
        panel.add(new JLabel("Email"));
        panel.add(emailField);

        saveButt.addActionListener(this::onSave);     // <-- hook the button
        panel.add(saveButt);

        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(saveButt);     // Enter key triggers Save
    }

    private void onSave(ActionEvent e){
        String nameVal  = nameField.getText().trim();
        String ageText  = ageField.getText().trim();
        String emailVal = emailField.getText().trim();

        // validations
        if (nameVal.isEmpty() || ageText.isEmpty() || emailVal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ageValue;
        // age vlaidation
        try{
            ageValue = Integer.parseInt(ageText);
            if (ageValue < 0 || ageValue > 150) throw new NumberFormatException();
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number (0–150).", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //email validation
        if (!emailVal.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //DB section
        
        String sql = "INSERT INTO persons(name, age, email) VALUES (?, ?, ?)";

        try (Connection conn = mysqldemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nameVal);
            ps.setInt(2, ageValue);
            ps.setString(3, emailVal);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "User saved!");
            nameField.setText("");
            ageField.setText("");
            emailField.setText("");
        } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Duplicate or constraint error:\n" + ex.getMessage(),
                "DB Error", JOptionPane.ERROR_MESSAGE);
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "SQL Error: " + ex.getMessage() +
                "\nSQLState: " + ex.getSQLState() +
                "\nErrorCode: " + ex.getErrorCode(),
                "DB Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Input().setVisible(true));
    }
}
