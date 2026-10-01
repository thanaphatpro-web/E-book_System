import javax.swing.*;
import java.awt.*;

/**
 * [Reusable Component]
 * แสดงผลรูปปกหนังสือแบบจำลอง (ใช้สีพื้นหลัง + ข้อความชื่อเรื่อง/ผู้แต่ง)
 */
public class CoverPanel extends JPanel {

    /**
     * @param coverColor สีพื้นหลังของปก
     * @param titleTh    ชื่อเรื่องภาษาไทย
     * @param titleEn    ชื่อเรื่องภาษาอังกฤษ
     * @param author     ชื่อผู้แต่ง
     */
    public CoverPanel(Color coverColor, String titleTh, String titleEn, String author) {
        setLayout(new BorderLayout());
        setBackground(coverColor);
        setBorder(BorderFactory.createLineBorder(Theme.CARD, 3)); // กรอบเส้นสีครีมรอบปก
        setPreferredSize(new Dimension(180, 250));

        // --------------------------------------------------------------------
        // ส่วนกลางปก: ชื่อเรื่องภาษาไทย และ ชื่อเรื่องภาษาอังกฤษ
        // --------------------------------------------------------------------
        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setBackground(coverColor);

        JLabel lblTitleTh = new JLabel(titleTh, SwingConstants.CENTER);
        lblTitleTh.setVerticalAlignment(SwingConstants.BOTTOM);
        lblTitleTh.setFont(new Font(Theme.FONT_TITLE, Font.BOLD, 15));
        lblTitleTh.setForeground(Theme.CARD);
        titleBox.add(lblTitleTh);

        JLabel lblTitleEn = new JLabel(titleEn, SwingConstants.CENTER);
        lblTitleEn.setVerticalAlignment(SwingConstants.TOP);
        lblTitleEn.setFont(new Font(Theme.FONT_BODY, Font.PLAIN, 11));
        lblTitleEn.setForeground(Theme.CARD);
        titleBox.add(lblTitleEn);

        add(titleBox, BorderLayout.CENTER);

        // --------------------------------------------------------------------
        // ส่วนล่างปก: ชื่อผู้แต่ง
        // --------------------------------------------------------------------
        JLabel lblAuthor = new JLabel(author, SwingConstants.CENTER);
        lblAuthor.setFont(new Font(Theme.FONT_BODY, Font.PLAIN, 12));
        lblAuthor.setForeground(Theme.CARD);
        lblAuthor.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        add(lblAuthor, BorderLayout.SOUTH);
    }
}