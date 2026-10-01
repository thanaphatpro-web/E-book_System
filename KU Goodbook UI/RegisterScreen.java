import javax.swing.*;
import java.awt.*;

/**
 * [View Screen]
 * หน้าสมัครสมาชิก (Register Screen)
 */
public class RegisterScreen extends JFrame {

    public RegisterScreen() {
        setTitle("KU Goodbook - สมัครสมาชิก");
        setSize(1120, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new AppHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Theme.PAPER);
        add(body, BorderLayout.CENTER);

        // การ์ดฟอร์มสมัครสมาชิก
        JPanel registerCard = new JPanel(new GridLayout(0, 1, 0, 4));
        registerCard.setBackground(Theme.CARD);
        registerCard.setPreferredSize(new Dimension(440, 620));
        registerCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 6, 6, Theme.SHADOW),
                BorderFactory.createEmptyBorder(26, 36, 26, 36)));
        body.add(registerCard);

        // องค์ประกอบฟอร์ม
        JLabel title = new JLabel("สมัครสมาชิก");
        title.setFont(new Font(Theme.FONT_TITLE, Font.BOLD, 28));
        title.setForeground(Theme.ACCENT);
        registerCard.add(title);

        registerCard.add(new JLabel("สร้างบัญชีเพื่อเก็บรายการโปรดและรีวิวของคุณ"));

        registerCard.add(new JLabel("ชื่อผู้ใช้"));
        JTextField txtUsername = new JTextField();
        registerCard.add(txtUsername);

        registerCard.add(new JLabel("อีเมล"));
        JTextField txtEmail = new JTextField();
        registerCard.add(txtEmail);

        registerCard.add(new JLabel("รหัสผ่าน"));
        JPasswordField txtPassword = new JPasswordField();
        registerCard.add(txtPassword);

        registerCard.add(new JLabel("ยืนยันรหัสผ่าน"));
        JPasswordField txtConfirmPassword = new JPasswordField();
        registerCard.add(txtConfirmPassword);

        JCheckBox chkAccept = new JCheckBox("ฉันยอมรับเงื่อนไขการใช้งาน");
        chkAccept.setBackground(Theme.CARD);
        registerCard.add(chkAccept);

        JButton btnRegister = new JButton("สมัครสมาชิก");
        btnRegister.setBackground(Theme.ACCENT);
        btnRegister.setForeground(Theme.CARD);
        registerCard.add(btnRegister);

        JLabel lblToLogin = new JLabel("มีบัญชีอยู่แล้ว? เข้าสู่ระบบ");
        lblToLogin.setForeground(Theme.ACCENT);
        registerCard.add(lblToLogin);

        // TODO (สำหรับคนทำต่อ): บันทึกข้อมูลสมัครสมาชิก
        // btnRegister.addActionListener(e -> performRegister(...));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterScreen::new);
    }
}