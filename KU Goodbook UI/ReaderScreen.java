import javax.swing.*;
import java.awt.*;

/**
 * [View Screen]
 * หน้าสำหรับอ่านเนื้อหาหนังสือ (Reader Screen)
 */
public class ReaderScreen extends JFrame {

    private JTextArea storyTextArea;
    private int currentFontSize = 20; // ขนาดฟอนต์เริ่มต้นสำหรับอ่าน

    public ReaderScreen() {
        setTitle("KU Goodbook - หน้าอ่าน");
        setSize(1120, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new AppHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 8));
        body.setBackground(Theme.PAPER);
        body.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        add(body, BorderLayout.CENTER);

        // --------------------------------------------------------------------
        // 1. แถบเครื่องมือด้านบน (Toolbar: ปุ่มย้อนกลับ, ชื่อตอน, ปุ่มปรับขนาดฟอนต์)
        // --------------------------------------------------------------------
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(Theme.PAPER);
        toolbar.setBorder(BorderFactory.createEmptyBorder(12, 28, 12, 28));
        body.add(toolbar, BorderLayout.NORTH);

        toolbar.add(new JButton("‹ กลับ"), BorderLayout.WEST);

        JLabel chapterTitle = new JLabel("เจ้าชายน้อย · ตอนที่ 1: รูปวาดงูเหลือม", SwingConstants.CENTER);
        chapterTitle.setFont(new Font(Theme.FONT_TITLE, Font.BOLD, 17));
        chapterTitle.setForeground(Theme.ACCENT);
        toolbar.add(chapterTitle, BorderLayout.CENTER);

        // ปุ่มปรับขนาดอักษร A- และ A+
        JPanel fontSizePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        fontSizePanel.setBackground(Theme.PAPER);
        JButton btnZoomOut = new JButton("A-");
        JButton btnZoomIn  = new JButton("A+");

        btnZoomOut.addActionListener(e -> changeFontSize(-2));
        btnZoomIn.addActionListener(e -> changeFontSize(2));

        fontSizePanel.add(btnZoomOut);
        fontSizePanel.add(btnZoomIn);
        toolbar.add(fontSizePanel, BorderLayout.EAST);

        // --------------------------------------------------------------------
        // 2. ส่วนกลาง: แผ่นกระดาษเนื้อหาเรื่อง
        // --------------------------------------------------------------------
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.PAPER);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 110, 0, 110));
        body.add(wrapper, BorderLayout.CENTER);

        JPanel page = new JPanel(new BorderLayout(12, 0));
        page.setBackground(Theme.CARD);
        page.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 6, 6, Theme.SHADOW),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)));
        wrapper.add(page, BorderLayout.CENTER);

        // ข้อความตัวอย่างเรื่อง
        String storyContent = 
                "          เมื่อครั้งที่ผมมีอายุได้หกขวบ ผมเคยเห็นรูปภาพอันงดงามรูปหนึ่งในหนังสือเกี่ยวกับป่าดงดิบ "
                + "มันเป็นภาพงูเหลือมยักษ์กำลังกลืนสัตว์ร้ายลงไปในท้อง ในหนังสือกล่าวไว้ว่า "
                + "'งูเหลือมจะกลืนเหยื่อลงไปทั้งตัวโดยไม่เคี้ยว จากนั้นมันจะไม่สามารถเคลื่อนไหวได้เลย และจะนอนหลับไปตลอดหกเดือนเพื่อย่อยอาหาร'\n\n"
                
                + "          เรื่องราวนั้นทำให้ผมจินตนาการถึงการผจญภัยในป่าลึกอย่างมากมาย ผมจึงลองใช้ดินสอสีวาดรูปรูปแรกของผมขึ้นมา "
                + "มันคือภาพงูเหลือมกำลังย่อยช้างทั้งตัว แต่เมื่อผมนำภาพนี้ไปถามพวกผู้ใหญ่ว่า 'ภาพนี้ทำให้คุณกลัวไหม?' "
                + "พวกเขากลับตอบว่า 'ทำไมต้องกลัวหมวกธรรมดาๆ ใบหนึ่งด้วยล่ะ?'\n\n"
                
                + "          ภาพของผมไม่ได้เป็นรูปหมวก แต่มันเป็นรูปงูเหลือมกำลังย่อยช้าง ผมจึงต้องวาดผ่าซีกด้านในของงูเหลือมเพื่อให้ผู้ใหญ่เข้าใจ "
                + "เพราะพวกผู้ใหญ่มักต้องการคำอธิบายเสมอ...";

        storyTextArea = new JTextArea(storyContent);
        storyTextArea.setFont(new Font(Theme.FONT_BODY, Font.PLAIN, currentFontSize));
        storyTextArea.setBackground(Theme.CARD);
        storyTextArea.setForeground(Theme.INK);
        storyTextArea.setLineWrap(true);
        storyTextArea.setWrapStyleWord(true);
        storyTextArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(storyTextArea);
        scrollPane.setBorder(null);
        page.add(scrollPane, BorderLayout.CENTER);

        // --------------------------------------------------------------------
        // 3. ส่วนล่าง: ปุ่มนำทางไปยังตอนก่อนหน้า/ตอนถัดไป
        // --------------------------------------------------------------------
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Theme.PAPER);
        navPanel.setBorder(BorderFactory.createEmptyBorder(12, 110, 0, 110));
        
        JButton btnPrev = new JButton("‹ ตอนก่อนหน้า");
        JButton btnNext = new JButton("ตอนถัดไป ›");
        
        navPanel.add(btnPrev, BorderLayout.WEST);
        navPanel.add(btnNext, BorderLayout.EAST);
        body.add(navPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ฟังก์ชันช่วยย่อ/ขยายฟอนต์
    private void changeFontSize(int delta) {
        currentFontSize = Math.max(12, Math.min(36, currentFontSize + delta));
        storyTextArea.setFont(new Font(Theme.FONT_BODY, Font.PLAIN, currentFontSize));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReaderScreen::new);
    }
}