package screens;

import app.Nav;
import components.PillButton;
import components.SlimScrollBarUI;
import data.Book;
import data.BookData;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import theme.Fonts;
import theme.Theme;

/**
 * หน้าอ่านเนื้อหาของตอนที่เลือก
 *
 * <p>ตอนในข้อมูลเริ่มนับตำแหน่งจาก 0 แต่ข้อความบนหน้าจอเริ่มนับจาก 1
 * ตัวอย่างเช่น chapterIndex 0 จะแสดงเป็น "ตอนที่ 1" ปุ่มเปลี่ยนขนาดตัวอักษร
 * ปุ่มโหมดกลางคืน และปุ่มทำเครื่องหมายอ่านจบยังเป็นเพียงตัวอย่างหน้าตา</p>
 */
public class ReaderScreen extends JFrame {

    /** ระยะขอบซ้ายและขวาของแถบเปลี่ยนตอนด้านล่าง */
    private static final int SIDE_MARGIN = 130;

    /** หนังสือที่กำลังอ่าน */
    private final Book book;

    /** ลำดับตอนปัจจุบัน โดยเริ่มจากศูนย์ */
    private final int chapterIndex;

    /**
     * สร้างหน้าต่างอ่านและแสดงชื่อเรื่อง เนื้อหา และปุ่มเปลี่ยนตอน
     *
     * @param book หนังสือที่ต้องการอ่าน
     * @param chapterIndex ลำดับตอน โดยเริ่มจากศูนย์
     */
    public ReaderScreen(Book book, int chapterIndex) {
        Fonts.install();
        this.book = book;
        this.chapterIndex = chapterIndex;
        setTitle("KU Goodbook - " + book.titleTh + " ตอนที่ " + (chapterIndex + 1));
        setSize(1280, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(Theme.bg());
        root.setBorder(new EmptyBorder(18, 40, 18, 40));
        setContentPane(root);

        // แถบชื่อเรื่องและเครื่องมืออยู่ด้านบน
        root.add(buildTopBar(), BorderLayout.NORTH);
        // หน้ากระดาษอยู่กลางหน้าต่าง ส่วนปุ่มเปลี่ยนตอนอยู่ด้านล่าง
        root.add(buildPaper(), BorderLayout.CENTER);
        root.add(buildBottomBar(), BorderLayout.SOUTH);

        setVisible(true);
    }

    /*
     * สร้างแถบด้านบนที่มีปุ่มกลับ ชื่อหนังสือและตอน
     * ปุ่มปรับตัวอักษรและโหมดกลางคืนยังไม่มีคำสั่งเปลี่ยนการแสดงผล
     */
    private JPanel buildTopBar() {
        JLabel title = new JLabel(book.titleTh + " · ตอนที่ " + (chapterIndex + 1) + ": "
                + book.chapterTitles.get(chapterIndex), SwingConstants.CENTER);
        title.setFont(Fonts.bold(15));
        title.setForeground(Theme.text());

        JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        tools.setOpaque(false);
        tools.add(new PillButton("A-", PillButton.OUTLINE));
        tools.add(new PillButton("A+", PillButton.OUTLINE));
        tools.add(new PillButton("โหมดกลางคืน", PillButton.OUTLINE));

        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        PillButton back = new PillButton("กลับ", PillButton.OUTLINE);
        back.addActionListener(e -> Nav.openDetail(this, book));
        bar.add(back, BorderLayout.WEST);
        bar.add(title, BorderLayout.CENTER);
        bar.add(tools, BorderLayout.EAST);
        return bar;
    }

    /*
     * สร้างหน้ากระดาษสำหรับอ่านเนื้อหาของตอนที่เลือก
     * ข้อความอยู่ในช่องที่เลื่อนได้ เผื่อเนื้อหายาวกว่าพื้นที่บนหน้าจอ
     */
    private JComponent buildPaper() {
        JPanel paper = new JPanel(new BorderLayout(0, 18)) {
            {
                setPreferredSize(new Dimension(900, 600));
            }
            @Override
            protected void paintChildren(Graphics g) {
                super.paintChildren(g);
                // วาดริบบิ้นหลังวาดส่วนประกอบภายใน เพื่อให้ลายอยู่มุมบนของกระดาษ
                paintRibbon((Graphics2D) g, getWidth() - 5 - 90);
            }
        };
        paper.setBackground(Theme.card());
        // เว้นขอบด้านในให้ข้อความไม่ชิดกรอบกระดาษ
        paper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.line()), new EmptyBorder(28, 48, 34, 48)));

        // จัดเลขตอน ชื่อตอน และลายเส้นไว้เหนือเนื้อหา
        JLabel eyebrow = new JLabel("ตอนที่ " + (chapterIndex + 1), SwingConstants.CENTER);
        eyebrow.setFont(Fonts.bold(13));
        eyebrow.setForeground(Theme.gold());
        JLabel chapterTitle = new JLabel(book.chapterTitles.get(chapterIndex), SwingConstants.CENTER);
        chapterTitle.setFont(Fonts.title(30));
        chapterTitle.setForeground(Theme.accent());
        JPanel head = new JPanel(new BorderLayout(0, 4));
        head.setOpaque(false);
        head.add(eyebrow, BorderLayout.NORTH);
        head.add(chapterTitle, BorderLayout.CENTER);
        head.add(makeOrnament(), BorderLayout.SOUTH);
        paper.add(head, BorderLayout.NORTH);

        // แสดงเนื้อหาจากข้อมูลหนังสือ พร้อมรูปแบบตัวอักษรและย่อหน้า
        JTextPane text = new JTextPane();
        text.setEditable(false);
        text.setOpaque(false);
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setFontFamily(style, Fonts.read(20).getFamily());
        StyleConstants.setFontSize(style, 22);
        StyleConstants.setForeground(style, Theme.text());
        StyleConstants.setLineSpacing(style, 0.8f);
        StyleConstants.setFirstLineIndent(style, 32);
        StyleConstants.setLeftIndent(style, 0);
        StyleConstants.setRightIndent(style, 0);
        try {
            StyledDocument doc = text.getStyledDocument();
            doc.insertString(0, book.chapterTexts.get(chapterIndex), style);
            // ใช้รูปแบบย่อหน้ากับข้อความทั้งหมดที่เพิ่งใส่ลงในเอกสาร
            doc.setParagraphAttributes(0, doc.getLength(), style, false);
        } catch (BadLocationException e) {
            throw new IllegalStateException("ไม่สามารถแสดงเนื้อหาตอนได้", e);
        }
        JScrollPane scroll = new JScrollPane(text);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUI(new SlimScrollBarUI());
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        paper.add(scroll, BorderLayout.CENTER);

        // วางปุ่มไว้นอกกรอบตามแบบหน้าจอ ปัจจุบันปุ่มยังไม่บันทึกสถานะอ่าน
        JPanel buttonWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonWrap.setOpaque(false);
        buttonWrap.add(new PillButton("ทำเครื่องหมายว่าอ่านจบตอนนี้", PillButton.PRIMARY));

        JPanel paperCenter = new JPanel(new GridBagLayout());
        paperCenter.setOpaque(false);
        paperCenter.add(paper);

        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, 24, 0, 24));
        wrapper.add(paperCenter, BorderLayout.CENTER);
        wrapper.add(buttonWrap, BorderLayout.SOUTH);
        return wrapper;
    }

    /*
     * วาดริบบิ้นสีแดงที่มุมบนของหน้ากระดาษ
     * วาดเงาก่อน แล้ววาดริบบิ้นจริงทับเพื่อให้ดูนูนขึ้น
     */
    private void paintRibbon(Graphics2D g, int x) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = 26;
        int h = 70;
        int[] xs = {x, x + w, x + w, x + w / 2, x};
        int[] ys = {1, 1, h, h - 14, h};
        g2.setColor(new Color(0, 0, 0, 50));
        g2.translate(2, 3);
        g2.fillPolygon(xs, ys, 5);
        g2.translate(-2, -3);
        g2.setColor(Theme.accentFill());
        g2.fillPolygon(xs, ys, 5);
        g2.dispose();
    }

    // สร้างเส้นสีทองกับรูปข้าวหลามตัดเล็ก ๆ คั่นหัวตอนกับเนื้อหา
    private JComponent makeOrnament() {
        return new JComponent() {
            {
                setPreferredSize(new Dimension(300, 26));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                Color gold = Theme.gold();
                Color clear = new Color(gold.getRed(), gold.getGreen(), gold.getBlue(), 0);
                g2.setPaint(new GradientPaint(cx - 120, 0, clear, cx - 14, 0, gold));
                g2.drawLine(cx - 120, cy, cx - 14, cy);
                g2.setPaint(new GradientPaint(cx + 14, 0, gold, cx + 120, 0, clear));
                g2.drawLine(cx + 14, cy, cx + 120, cy);
                g2.setColor(gold);
                g2.fillPolygon(new int[]{cx, cx + 6, cx, cx - 6}, new int[]{cy - 6, cy, cy + 6, cy}, 4);
                g2.dispose();
            }
        };
    }

    /*
     * สร้างปุ่มไปตอนก่อนหน้าและตอนถัดไป พร้อมเลขบอกตำแหน่งตอน
     * ปิดปุ่มก่อนหน้าเมื่ออยู่ตอนแรก และปิดปุ่มถัดไปเมื่ออยู่ตอนสุดท้าย
     */
    private JPanel buildBottomBar() {
        PillButton prev = new PillButton("ตอนก่อนหน้า", PillButton.OUTLINE);
        // ตอนแรกไม่มีตอนก่อนหน้า จึงปิดปุ่มนั้นไว้
        prev.setEnabled(chapterIndex > 0);
        PillButton next = new PillButton("ตอนถัดไป", PillButton.OUTLINE);
        next.setEnabled(chapterIndex < book.chapterTitles.size() - 1);
        prev.addActionListener(e -> Nav.openReader(this, book, chapterIndex - 1));
        next.addActionListener(e -> Nav.openReader(this, book, chapterIndex + 1));

        JComponent dots = makeDots();
        JLabel number = new JLabel("ตอนที่ " + (chapterIndex + 1) + " / " + book.chapterTitles.size(), SwingConstants.CENTER);
        number.setFont(Fonts.body(13));
        number.setForeground(Theme.muted());
        JPanel middle = new JPanel(new BorderLayout(0, 4));
        middle.setOpaque(false);
        middle.add(dots, BorderLayout.NORTH);
        middle.add(number, BorderLayout.CENTER);

        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(0, SIDE_MARGIN - 40, 0, SIDE_MARGIN - 40));
        bar.add(prev, BorderLayout.WEST);
        bar.add(middle, BorderLayout.CENTER);
        bar.add(next, BorderLayout.EAST);
        return bar;
    }

    /*
     * วาดจุดแทนทุกตอน จุดทึบคือตอนก่อนหน้า วงกลมเด่นคือตอนปัจจุบัน
     * และวงกลมสีอ่อนคือตอนที่ยังอยู่ถัดไป
     */
    private JComponent makeDots() {
        final int total = book.chapterTitles.size();
        return new JComponent() {
            {
                setPreferredSize(new Dimension(total * 18, 14));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int startX = (getWidth() - total * 18) / 2 + 4;
                for (int i = 0; i < total; i++) {
                    int x = startX + i * 18;
                    if (i < chapterIndex) {
                        g2.setColor(Theme.accent());
                        g2.fillOval(x, 2, 10, 10);
                    } else if (i == chapterIndex) {
                        g2.setColor(Theme.accent());
                        g2.setStroke(new BasicStroke(2f));
                        g2.drawOval(x, 2, 10, 10);
                    } else {
                        g2.setColor(Theme.line());
                        g2.setStroke(new BasicStroke(1.5f));
                        g2.drawOval(x, 2, 10, 10);
                    }
                }
                g2.dispose();
            }
        };
    }

    /**
     * เปิดหน้าอ่านโดยระบุเลขหนังสือและเลขตอนจากคำสั่ง
     * ค่าที่รับเริ่มนับจาก 1 เพื่อให้ง่ายต่อการพิมพ์ในหน้าต่างคำสั่ง
     * ก่อนส่งไปยัง ReaderScreen จะเปลี่ยนเป็นตำแหน่งเริ่มจาก 0
     *
     * @param args เลขหนังสือและเลขตอน ถ้าไม่ระบุจะเปิดเรื่องแรกตอนแรก
     */
    public static void main(String[] args) {
        int bookNumber = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        int chapterNumber = args.length > 1 ? Integer.parseInt(args[1]) : 1;
        Book book = BookData.ALL.get(Math.max(1, Math.min(BookData.ALL.size(), bookNumber)) - 1);
        int index = Math.max(1, Math.min(book.chapterTitles.size(), chapterNumber)) - 1;
        SwingUtilities.invokeLater(() -> new ReaderScreen(book, index));
    }
}
