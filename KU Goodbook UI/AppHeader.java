import javax.swing.*;
import java.awt.*;

/**
 * [Reusable Component]
 * แถบ Header แสดงชื่อแอปและเมนูหลัก ปรากฏอยู่ที่ส่วนบนสุดของทุกหน้าจอ (North Region)
 */
public class AppHeader extends JPanel {

    public AppHeader() {
        // ตั้งค่า Layout และการจัดระยะขอบ
        setLayout(new BorderLayout());
        setBackground(Theme.ACCENT);
        setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));

        // --------------------------------------------------------------------
        // ส่วนซ้าย: โลโก้ / ชื่อแอปพลิเคชัน
        // --------------------------------------------------------------------
        JLabel appTitle = new JLabel("KU Goodbook");
        appTitle.setFont(new Font(Theme.FONT_TITLE, Font.BOLD, 24));
        appTitle.setForeground(Theme.CARD);
        add(appTitle, BorderLayout.WEST);

        // --------------------------------------------------------------------
        // ส่วนขวา: ปุ่มเมนูหลักสำหรับการสลับหน้า
        // --------------------------------------------------------------------
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        menuPanel.setBackground(Theme.ACCENT);

        JButton btnHome     = new JButton("หน้าแรก");
        JButton btnLogin    = new JButton("เข้าสู่ระบบ");
        JButton btnRegister = new JButton("สมัครสมาชิก");
        JButton btnDarkMode = new JButton("โหมดกลางคืน");

        menuPanel.add(btnHome);
        menuPanel.add(btnLogin);
        menuPanel.add(btnRegister);
        menuPanel.add(btnDarkMode);

        add(menuPanel, BorderLayout.EAST);

        // TODO (สำหรับคนทำต่อ): ผูก ActionListener กับปุ่มเมนูเพื่อสลับหน้าจอ (Navigation)
        // btnHome.addActionListener(e -> SwitchToHomeScreen());
    }
}