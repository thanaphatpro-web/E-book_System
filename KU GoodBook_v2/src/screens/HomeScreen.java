package screens;

import components.BookCard;
import components.InputField;
import components.PillButton;
import components.Sidebar;
import components.SlimScrollBarUI;
import data.Book;
import data.BookData;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import theme.Fonts;
import theme.Theme;

/**
 * หน้าแรกที่แสดงรายการหนังสือจาก BookData
 *
 * <p>การ์ดแต่ละใบเปิดหน้ารายละเอียดของหนังสือที่เลือกได้
 * ช่องค้นหาและปุ่มเรียงเป็นส่วนแสดงตัวอย่าง ยังไม่ได้กรองหรือเรียงข้อมูลจริง</p>
 */
public class HomeScreen extends JFrame {

    /** สร้างหน้าต่างหน้าแรก พร้อมแถบเมนู แถบด้านบน และรายการหนังสือ */
    public HomeScreen() {
        Fonts.install();
        setTitle("KU Goodbook - หน้าแรก");
        setSize(1280, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // สร้างแถบเมนู และบอก Sidebar ว่าหน้าแรกเป็นหน้าปัจจุบัน
        add(new Sidebar("หน้าแรก"), BorderLayout.WEST);

        // เว้นระยะระหว่างคำทักทายด้านบนกับรายการหนังสือ
        JPanel main = new JPanel(new BorderLayout(0, 18));
        main.setBackground(Theme.bg());
        main.setBorder(new EmptyBorder(22, 28, 0, 20));
        add(main, BorderLayout.CENTER);
        main.add(buildTopBar(), BorderLayout.NORTH);
        main.add(buildBookList(), BorderLayout.CENTER);

        setVisible(true);
    }

    /*
     * สร้างแถบด้านบนที่มีคำทักทายและช่องค้นหา
     * ช่องค้นหาตอนนี้เป็นเพียงหน้าตา ยังไม่มีคำสั่งค้นหาหนังสือ
     */
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        JLabel hello = new JLabel("สวัสดี, ผู้มัวหมอง");
        hello.setFont(Fonts.title(22));
        hello.setForeground(Theme.text());
        JLabel sub = new JLabel("พร้อมหาเรื่องใหม่อ่านหรือยัง?");
        sub.setFont(Fonts.body(14));
        sub.setForeground(Theme.muted());
        JPanel texts = new JPanel(new GridLayout(2, 1));
        texts.setOpaque(false);
        texts.add(hello);
        texts.add(sub);

        JPanel left = new JPanel(new BorderLayout(14, 0));
        left.setOpaque(false);
        left.add(texts, BorderLayout.CENTER);
        bar.add(left, BorderLayout.WEST);

        InputField search = new InputField("ค้นหาชื่อเรื่อง ผู้แต่ง", false);
        search.setPreferredSize(new Dimension(320, 44));
        JPanel searchWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        searchWrap.setOpaque(false);
        searchWrap.add(search);
        bar.add(searchWrap, BorderLayout.EAST);
        return bar;
    }

    /*
     * สร้างรายการหนังสือแบบสามคอลัมน์และใส่ไว้ใน JScrollPane
     * เมื่อมีหนังสือมากกว่าพื้นที่ที่เห็น ผู้ใช้จึงเลื่อนลงไปดูรายการต่อได้
     */
    private JScrollPane buildBookList() {
        JLabel heading = new JLabel("เรื่องแนะนำ");
        heading.setFont(Fonts.title(20));
        heading.setForeground(Theme.accent());
        JPanel headingRow = new JPanel(new BorderLayout());
        headingRow.setOpaque(false);
        headingRow.add(heading, BorderLayout.WEST);
        headingRow.add(new PillButton("เรียง: ยอดนิยม", PillButton.OUTLINE), BorderLayout.EAST);

        // จำนวนแถวจะเพิ่มตามจำนวนหนังสือ โดยคงไว้สามคอลัมน์
        JPanel grid = new JPanel(new GridLayout(0, 3, 18, 18));
        grid.setOpaque(false);
        for (Book book : BookData.ALL) {
            grid.add(new BookCard(book, false));
        }

        JPanel section = new JPanel(new BorderLayout(0, 14));
        section.setOpaque(false);
        section.add(headingRow, BorderLayout.NORTH);
        section.add(grid, BorderLayout.CENTER);

        // วางเนื้อหาไว้ด้านบนของพื้นที่เลื่อน ไม่ยืดการ์ดเมื่อมีรายการน้อย
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, 0, 24, 10));
        wrapper.add(section, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.getVerticalScrollBar().setUI(new SlimScrollBarUI());
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        return scroll;
    }

    /**
     * เปิดหน้านี้โดยตรงเพื่อทดลองหน้าจอ โดยไม่ต้องเริ่มจาก Main
     *
     * @param args ค่าจากคำสั่งเปิดโปรแกรม (ยังไม่ได้ใช้)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomeScreen::new);
    }
}
