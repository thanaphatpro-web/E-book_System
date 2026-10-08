package screens;

import app.Nav;
import components.BrandPanel;
import components.InputField;
import components.PillButton;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import theme.Fonts;
import theme.Theme;

/**
 * หน้าเข้าสู่ระบบตัวอย่างของแอป
 *
 * <p>หน้านี้แสดงช่องชื่อผู้ใช้และรหัสผ่าน แต่ยังไม่ตรวจข้อมูลจริง
 * กดปุ่มเข้าสู่ระบบแล้วจะไปหน้าแรกทันที จึงใช้สำหรับทดลองหน้าตาและการเปลี่ยนหน้า</p>
 */
public class LoginScreen extends JFrame {

    /**
     * สร้างหน้าต่าง ฟอร์มเข้าสู่ระบบ และลิงก์ไปหน้าสมัครสมาชิก
     * หลังจัดวางส่วนต่าง ๆ แล้วจึงแสดงหน้าต่างให้ผู้ใช้เห็น
     */
    public LoginScreen() {
        Fonts.install();
        setTitle("KU Goodbook - เข้าสู่ระบบ");
        setSize(1280, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ใช้ GridBagLayout เพื่อวางการ์ดให้อยู่กลางพื้นที่หน้าต่าง
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Theme.bg());
        setContentPane(body);

        // การ์ดหลักแบ่งเป็นแผงแบรนด์ด้านซ้ายและฟอร์มด้านขวา
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.card());
        card.setBorder(BorderFactory.createLineBorder(Theme.line()));
        card.setBorder(new EmptyBorder(0, 0, 5, 5)); // เว้นที่ให้เงา
        card.setPreferredSize(new Dimension(820, 480));
        body.add(card);

        card.add(new BrandPanel(), BorderLayout.WEST);

        // เรียงหัวข้อ คำอธิบาย ช่องกรอก และปุ่มจากบนลงล่าง
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(40, 44, 30, 44));
        card.add(form, BorderLayout.CENTER);

        JLabel title = new JLabel("เข้าสู่ระบบ");
        title.setFont(Fonts.title(28));
        title.setForeground(Theme.accent());
        addRow(form, title, 4);

        JLabel welcome = new JLabel("ยินดีต้อนรับกลับมา ท่านผู้เจริญ");
        welcome.setFont(Fonts.body(14));
        welcome.setForeground(Theme.muted());
        addRow(form, welcome, 22);

        addRow(form, makeLabel("ชื่อผู้ใช้ หรืออีเมล"), 6);
        addRow(form, makeField("name@example.com", false), 16);
        addRow(form, makeLabel("รหัสผ่าน"), 6);
        addRow(form, makeField("••••••••", true), 26);

        PillButton loginButton = new PillButton("เข้าสู่ระบบ", PillButton.PRIMARY);
        loginButton.setFont(Fonts.bold(15));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        // ตัวอย่างนี้ยังไม่ตรวจชื่อหรือรหัสผ่าน กดแล้วเปิดหน้าแรกทันที
        loginButton.addActionListener(e -> Nav.openHome(this));
        addRow(form, loginButton, 18);

        // แสดงคำถามและข้อความที่คลิกได้เพื่อไปหน้าสมัครสมาชิก
        JPanel link = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        link.setOpaque(false);
        JLabel ask = new JLabel("ยังไม่มีบัญชี?");
        ask.setFont(Fonts.body(13));
        ask.setForeground(Theme.muted());
        JLabel go = new JLabel("สมัครสมาชิก");
        go.setFont(Fonts.bold(13));
        go.setForeground(Theme.accent());
        Nav.onClick(go, () -> Nav.openRegister(this));
        link.add(ask);
        link.add(go);
        addRow(form, link, 0);

        setVisible(true);
    }

    /**
     * สร้างข้อความกำกับช่องกรอกให้ใช้รูปแบบเดียวกัน
     *
     * @param text ข้อความที่ต้องการแสดง
     * @return ป้ายข้อความที่ตั้งฟอนต์และสีไว้แล้ว
     */
    private static JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Fonts.bold(13));
        label.setForeground(Theme.text());
        return label;
    }

    /**
     * สร้างช่องกรอกและกำหนดความกว้างให้ขยายตามฟอร์ม
     *
     * @param hint ข้อความแนะนำก่อนเริ่มพิมพ์
     * @param password ระบุว่าเป็นช่องรหัสผ่านหรือไม่
     * @return ช่องกรอกที่พร้อมนำไปวางในฟอร์ม
     */
    private static InputField makeField(String hint, boolean password) {
        InputField field = new InputField(hint, password);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return field;
    }

    /*
     * เพิ่มส่วนประกอบหนึ่งชิ้นในฟอร์ม และเว้นช่องว่างด้านล่างตามที่กำหนด
     * การจัดชิดซ้ายช่วยให้หัวข้อ ช่องกรอก และปุ่มเริ่มตรงแนวเดียวกัน
     */
    private static void addRow(JPanel form, JComponent c, int gap) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(c);
        form.add(Box.createVerticalStrut(gap));
    }

    /**
     * เปิดหน้านี้โดยตรงเพื่อทดลองหน้าจอ โดยไม่ต้องเริ่มจาก Main
     *
     * @param args ค่าจากคำสั่งเปิดโปรแกรม (ยังไม่ได้ใช้)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}
