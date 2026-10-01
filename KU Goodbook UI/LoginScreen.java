import javax.swing.*;
import java.awt.*;

/**
 * [View Screen]
 * หน้าเข้าสู่ระบบ (Login Screen)
 */
public class LoginScreen extends JFrame {

    public LoginScreen() {
        setTitle("KU Goodbook - เข้าสู่ระบบ");
        setSize(1120, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new AppHeader(), BorderLayout.NORTH);

        // จัดตำแหน่งการ์ดให้อยู่กลางจอด้วย GridBagLayout
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Theme.PAPER);
        add(body, BorderLayout.CENTER);

        // การ์ดฟอร์มเข้าสู่ระบบ
        JPanel loginCard = new JPanel(new GridLayout(0, 1, 0, 6));
        loginCard.setBackground(Theme.CARD);
        loginCard.setPreferredSize(new Dimension(440, 440));
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 6, 6, Theme.SHADOW),
                BorderFactory.createEmptyBorder(30, 36, 30, 36)));
        body.add(loginCard);

        // องค์ประกอบฟอร์ม
        JLabel title = new JLabel("เข้าสู่ระบบ");
        title.setFont(new Font(Theme.FONT_TITLE, Font.BOLD, 28));
        title.setForeground(Theme.ACCENT);
        loginCard.add(title);

        loginCard.add(new JLabel("ยินดีต้อนรับกลับมา ท่านผู้เจริญ"));
        loginCard.add(new JLabel("ชื่อผู้ใช้ หรืออีเมล"));
        
        JTextField txtUsername = new JTextField();
        loginCard.add(txtUsername);

        loginCard.add(new JLabel("รหัสผ่าน"));
        JPasswordField txtPassword = new JPasswordField();
        loginCard.add(txtPassword);

        JCheckBox chkRemember = new JCheckBox("จำฉันไว้");
        chkRemember.setBackground(Theme.CARD);
        loginCard.add(chkRemember);

        JButton btnLogin = new JButton("เข้าสู่ระบบ");
        btnLogin.setBackground(Theme.ACCENT);
        btnLogin.setForeground(Theme.CARD);
        loginCard.add(btnLogin);

        JLabel lblToRegister = new JLabel("ยังไม่มีบัญชี? สมัครสมาชิก");
        lblToRegister.setForeground(Theme.ACCENT);
        loginCard.add(lblToRegister);

        // TODO (สำหรับคนทำต่อ): ตรวจสอบการ Login
        // btnLogin.addActionListener(e -> performLogin(txtUsername.getText(), String.valueOf(txtPassword.getPassword())));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}