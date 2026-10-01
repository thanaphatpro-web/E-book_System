import javax.swing.*;
import java.awt.*;

/**
 * [Reusable Component]
 * การ์ดหนังสือ 1 เล่ม นำไปใช้จัดวางใน Grid View หน้าแรก
 */
public class BookCard extends JPanel {

    public BookCard(Color coverColor, String titleTh, String titleEn, String author,
                    String category, String rating, String status) {
        
        setLayout(new BorderLayout(0, 8));
        setBackground(Theme.CARD);
        setPreferredSize(new Dimension(220, 360));
        
        // สร้างมิติด้วยเงาด้านขวาและล่าง
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 6, 6, Theme.SHADOW),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // 1. ส่วนกลาง: ภาพปกหนังสือ
        add(new CoverPanel(coverColor, titleTh, titleEn, author), BorderLayout.CENTER);

        // 2. ส่วนล่าง: ข้อความรายละเอียดหนังสือ 4 บรรทัด
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.setBackground(Theme.CARD);

        JLabel lblTh = new JLabel(titleTh);
        lblTh.setFont(new Font(Theme.FONT_TITLE, Font.BOLD, 14));
        infoPanel.add(lblTh);

        JLabel lblEn = new JLabel(titleEn);
        lblEn.setFont(new Font(Theme.FONT_BODY, Font.PLAIN, 11));
        infoPanel.add(lblEn);

        JLabel lblAuthorCat = new JLabel(author + " · " + category);
        lblAuthorCat.setFont(new Font(Theme.FONT_BODY, Font.PLAIN, 11));
        infoPanel.add(lblAuthorCat);

        JLabel lblRatingStatus = new JLabel("★ " + rating + " · " + status);
        lblRatingStatus.setFont(new Font(Theme.FONT_BODY, Font.PLAIN, 12));
        lblRatingStatus.setForeground(Theme.ACCENT);
        infoPanel.add(lblRatingStatus);

        add(infoPanel, BorderLayout.SOUTH);

        // TODO (สำหรับคนทำต่อ): ผูก MouseListener เมื่อคลิกการ์ด เพื่อเปิดไปยัง DetailScreen
        // this.addMouseListener(new MouseAdapter() { ... });
    }
}