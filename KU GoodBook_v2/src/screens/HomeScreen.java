package screens;

import components.BookCard;
import components.InputField;
import components.PillButton;
import components.Sidebar;
import components.SlimScrollBarUI;
import app.UserSession;
import data.Book;
import data.BookData;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;
import theme.Fonts;
import theme.Theme;

/**
 * หน้าแรกที่แสดงรายการหนังสือจาก BookData
 *
 * <p>การ์ดแต่ละใบเปิดหน้ารายละเอียดของหนังสือที่เลือกได้
 * ช่องค้นหาและตัวเลือกเรียงกรองข้อมูลในรายการที่แสดงบนหน้าจอ</p>
 */
public class HomeScreen extends JFrame {
    /** ช่องค้นหาหนังสือบนแถบบน */
    private final InputField search = new InputField("ค้นหาชื่อเรื่อง ผู้แต่ง", false);

    /** ตารางการ์ดหนังสือที่ถูกกรองแล้ว */
    private final JPanel grid = new JPanel(new GridLayout(0, 3, 18, 18));

    /** วิธีเรียงรายการปัจจุบัน */
    private String sort = "ลำดับเดิม";

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
     * ช่องค้นหาใช้กรองชื่อเรื่อง ผู้แต่ง และหมวดหมู่แบบทันที
     */
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);

        JLabel hello = new JLabel("สวัสดี, " + UserSession.username());
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
        JLabel heading = new JLabel("หนังสือทั้งหมด");
        heading.setFont(Fonts.title(20));
        heading.setForeground(Theme.accent());
        JPanel headingRow = new JPanel(new BorderLayout());
        headingRow.setOpaque(false);
        headingRow.add(heading, BorderLayout.WEST);
        JComboBox<String> sortBox = new JComboBox<>(new String[]{"ลำดับเดิม", "ชื่อเรื่อง A-Z", "คะแนนรีวิวสูงสุด"});
        sortBox.setFont(Fonts.body(13));
        sortBox.addActionListener(e -> {
            sort = (String) sortBox.getSelectedItem();
            refreshBooks();
        });
        headingRow.add(sortBox, BorderLayout.EAST);

        // จำนวนแถวจะเพิ่มตามจำนวนหนังสือ โดยคงไว้สามคอลัมน์
        grid.setOpaque(false);
        refreshBooks();
        search.getTextComponent().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { refreshBooks(); }
            @Override
            public void removeUpdate(DocumentEvent e) { refreshBooks(); }
            @Override
            public void changedUpdate(DocumentEvent e) { refreshBooks(); }
        });

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

    /** กรอง เรียง และวาดการ์ดหนังสือใหม่ตามคำค้นหากับตัวเลือกเรียง */
    private void refreshBooks() {
        String query = search.getTextComponent().getText().trim().toLowerCase();
        List<Book> books = new ArrayList<>();

        // เก็บเฉพาะเล่มที่มีชื่อ ผู้แต่ง หรือหมวดตรงกับคำค้นหา
        for (Book book : BookData.ALL) {
            String searchableText = book.titleTh + " " + book.titleEn + " " + book.author + " " + book.category;
            if (searchableText.toLowerCase().contains(query)) {
                books.add(book);
            }
        }

        sortBooks(books);
        grid.removeAll();
        for (Book book : books) {
            grid.add(new BookCard(book, false));
        }
        if (books.isEmpty()) {
            grid.add(new JLabel("ไม่พบหนังสือที่ค้นหา"));
        }
        grid.revalidate();
        grid.repaint();
    }

    /** เรียงหนังสือด้วยการสลับรายการทีละคู่ เพื่อให้เห็นขั้นตอนการทำงานชัดเจน */
    private void sortBooks(List<Book> books) {
        for (int i = 0; i < books.size(); i++) {
            for (int j = i + 1; j < books.size(); j++) {
                boolean shouldSwap = false;

                if ("ชื่อเรื่อง A-Z".equals(sort)) {
                    shouldSwap = books.get(i).titleTh.compareTo(books.get(j).titleTh) > 0;
                } else if ("คะแนนรีวิวสูงสุด".equals(sort)) {
                    shouldSwap = books.get(i).averageReviewRating() < books.get(j).averageReviewRating();
                }

                if (shouldSwap) {
                    Book firstBook = books.get(i);
                    books.set(i, books.get(j));
                    books.set(j, firstBook);
                }
            }
        }
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
