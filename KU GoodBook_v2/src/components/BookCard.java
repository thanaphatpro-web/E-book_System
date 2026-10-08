package components;

import app.Nav;
import data.Book;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import theme.Fonts;
import theme.Theme;

/**
 * การ์ดสรุปข้อมูลหนังสือที่ใช้ซ้ำได้ในหน้าแรกและหน้ารายการโปรด
 *
 * <p>การ์ดแสดงปก ชื่อ ผู้แต่ง คะแนน และสถานะ เมื่อคลิกที่การ์ด
 * จะเปิดหน้ารายละเอียดของหนังสือเล่มนั้น ส่วนปุ่มลบจะแสดงเฉพาะ
 * เมื่อสร้างการ์ดสำหรับรายการโปรด</p>
 */
public class BookCard extends JPanel {

    /**
     * สร้างการ์ดจากข้อมูลหนังสือ
     *
     * @param book หนังสือที่ต้องการแสดง
     * @param showDelete กำหนดว่าจะให้แสดงปุ่มลบรายการโปรดหรือไม่
     */
    public BookCard(Book book, boolean showDelete) {
        super(new BorderLayout(0, 10));
        setBackground(Theme.card());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.line()),
                new EmptyBorder(14, 14, 19, 19)));

        // ส่วนกลางของ BorderLayout ยืดตามพื้นที่ที่เหลือ จึงใช้วางปกหนังสือ
        add(new BookCover(book, 80, 350), BorderLayout.CENTER);

        // รวมข้อความที่เกี่ยวกับหนังสือไว้ในแผงเดียวกันใต้ปก
        JPanel info = new JPanel(new BorderLayout(0, 4));
        info.setOpaque(false);

        // จองพื้นที่ชื่อเรื่องเท่ากันทุกใบ ทำให้ตำแหน่งข้อมูลด้านล่างเรียงตรงกัน
        JLabel title = new JLabel("<html><div style='width:190px'><b>" + book.titleTh + "</b></div></html>");
        title.setFont(Fonts.bold(15));
        title.setForeground(Theme.text());
        title.setVerticalAlignment(SwingConstants.TOP);
        title.setPreferredSize(new Dimension(10, 44));
        info.add(title, BorderLayout.NORTH);

        JPanel details = new JPanel(new GridLayout(0, 1, 0, 2));
        details.setOpaque(false);

        JLabel author = new JLabel(book.author);
        author.setFont(Fonts.body(12));
        author.setForeground(Theme.muted());
        details.add(author);

        // วางคะแนนกับสถานะคนละด้านเพื่อให้อ่านเทียบข้อมูลได้สะดวก
        JLabel rating = new JLabel("★ " + book.rating);
        rating.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        rating.setForeground(Theme.accent());
        JLabel status = new JLabel(book.status);
        status.setFont(Fonts.body(12));
        status.setForeground(Theme.muted());
        JPanel ratingRow = new JPanel(new BorderLayout());
        ratingRow.setOpaque(false);
        ratingRow.add(rating, BorderLayout.WEST);
        ratingRow.add(status, BorderLayout.EAST);
        details.add(ratingRow);
        info.add(details, BorderLayout.CENTER);

        // หน้าทั่วไปไม่แสดงปุ่มนี้ แต่หน้ารายการโปรดแสดงไว้ใต้รายละเอียด
        if (showDelete) {
            JPanel deleteRow = new JPanel(new BorderLayout());
            deleteRow.setOpaque(false);
            deleteRow.setBorder(new EmptyBorder(6, 0, 0, 0));
            deleteRow.add(new PillButton("ลบรายการโปรด", PillButton.DANGER));
            info.add(deleteRow, BorderLayout.SOUTH);
        }
        add(info, BorderLayout.SOUTH);

        // คลิกการ์ดตรงไหนก็ได้เพื่อดูรายละเอียดของหนังสือที่การ์ดนี้แทน
        Nav.onClick(this, () -> Nav.openDetail(this, book));
    }
}
