import javax.swing.*;
import java.awt.*;

/**
 * [View Screen]
 * หน้ารายละเอียดหนังสือ (Detail Screen)
 */
public class DetailScreen extends JFrame {

    public DetailScreen() {
        setTitle("KU Goodbook - รายละเอียดเรื่อง");
        setSize(1120, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new AppHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Theme.PAPER);
        body.setBorder(BorderFactory.createEmptyBorder(16, 40, 16, 40));
        add(body, BorderLayout.CENTER);

        // แผ่นกระดาษหลัก แบ่งพื้นที่ ซ้าย (ปก) และ ขวา (ข้อมูล + สารบัญ)
        JPanel pageCard = new JPanel(new BorderLayout(28, 0));
        pageCard.setBackground(Theme.CARD);
        pageCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 6, 6, Theme.SHADOW),
                BorderFactory.createEmptyBorder(26, 28, 26, 28)));
        body.add(pageCard, BorderLayout.CENTER);

        // --------------------------------------------------------------------
        // ฝั่งซ้าย: ปกเรื่อง
        // --------------------------------------------------------------------
        CoverPanel cover = new CoverPanel(new Color(0x6b2d2d), "ดาบแห่งเถ้าถ่าน", "Ashen Blade", "ธีรภัทร ศรีสุวรรณ");
        cover.setPreferredSize(new Dimension(230, 330));
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Theme.CARD);
        leftPanel.add(cover, BorderLayout.NORTH);
        pageCard.add(leftPanel, BorderLayout.WEST);

        // --------------------------------------------------------------------
        // ฝั่งขวา: 3 ส่วนเรียงลงมา (1. ข้อมูลเรื่อง / 2. สารบัญ / 3. ฟอร์มรีวิว)
        // --------------------------------------------------------------------
        JPanel rightPanel = new JPanel(new BorderLayout(0, 12));
        rightPanel.setBackground(Theme.CARD);
        pageCard.add(rightPanel, BorderLayout.CENTER);

        // ---- ส่วนที่ 1: ข้อมูลเรื่องย่อ ----
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.setBackground(Theme.CARD);

        JLabel lblTitleTh = new JLabel("ดาบแห่งเถ้าถ่าน");
        lblTitleTh.setFont(new Font(Theme.FONT_TITLE, Font.BOLD, 26));
        lblTitleTh.setForeground(Theme.ACCENT);
        infoPanel.add(lblTitleTh);

        infoPanel.add(new JLabel("Ashen Blade"));
        infoPanel.add(new JLabel("ผู้แต่ง ธีรภัทร ศรีสุวรรณ  ·  ปี 2021  ·  หมวด แอ็คชัน"));

        JLabel lblRating = new JLabel("★ 4.7 (1 รีวิว)  ·  จบแล้ว");
        lblRating.setForeground(Theme.ACCENT);
        infoPanel.add(lblRating);

        infoPanel.add(new JLabel("ชายหนุ่มผู้รอดจากหมู่บ้านที่มอดไหม้ ออกเดินทางกับดาบปริศนา"));
        rightPanel.add(infoPanel, BorderLayout.NORTH);

        // ---- ส่วนที่ 2: ตารางสารบัญ 10 ตอน ----
        JPanel chapterPanel = new JPanel(new BorderLayout(0, 6));
        chapterPanel.setBackground(Theme.CARD);

        JLabel lblChapterHeader = new JLabel("สารบัญ");
        lblChapterHeader.setFont(new Font(Theme.FONT_TITLE, Font.BOLD, 18));
        lblChapterHeader.setForeground(Theme.ACCENT);
        chapterPanel.add(lblChapterHeader, BorderLayout.NORTH);

        String[] chapters = {"หมู่บ้านที่มอดไหม้", "นักเดินทางไร้ชื่อ", "เส้นทางสู่เมืองท่า",
                             "โจรสามพี่น้อง", "ตลาดมืด", "ความจริงของเถ้าถ่าน",
                             "การไล่ล่า", "ดวลบนหน้าผา", "ราคาของพลัง", "เถ้าที่ปลูกต้นไม้"};
        
        JPanel chapterGrid = new JPanel(new GridLayout(5, 2, 8, 6));
        chapterGrid.setBackground(Theme.CARD);
        for (int i = 0; i < chapters.length; i++) {
            JButton chapterBtn = new JButton((i + 1) + ". " + chapters[i]);
            chapterGrid.add(chapterBtn);
            // TODO: ผูก Event เมื่อกดปุ่มตอน ให้เปิดไป ReaderScreen
        }
        chapterPanel.add(chapterGrid, BorderLayout.CENTER);
        rightPanel.add(chapterPanel, BorderLayout.CENTER);

        // ---- ส่วนที่ 3: ระบบให้คะแนนและส่งรีวิว ----
        JPanel reviewPanel = new JPanel(new BorderLayout(0, 6));
        reviewPanel.setBackground(Theme.CARD);

        JPanel reviewHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reviewHeader.setBackground(Theme.CARD);
        JLabel lblReviewHeader = new JLabel("ให้คะแนนและรีวิว");
        lblReviewHeader.setFont(new Font(Theme.FONT_TITLE, Font.BOLD, 18));
        lblReviewHeader.setForeground(Theme.ACCENT);
        
        reviewHeader.add(lblReviewHeader);
        reviewHeader.add(new JComboBox<>(new String[]{"5 ดาว", "4 ดาว", "3 ดาว", "2 ดาว", "1 ดาว"}));
        reviewHeader.add(new JButton("ส่งรีวิว"));
        reviewPanel.add(reviewHeader, BorderLayout.NORTH);

        reviewPanel.add(new JScrollPane(new JTextArea(3, 30)), BorderLayout.CENTER);
        reviewPanel.add(new JLabel("★★★★★  ฉากดวลบนหน้าผาสนุกมาก อยากให้ตอนยาวกว่านี้"), BorderLayout.SOUTH);
        
        rightPanel.add(reviewPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DetailScreen::new);
    }
}