package screens;

import components.InputField;
import components.Sidebar;
import components.SlimScrollBarUI;
import data.BookData;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import theme.Fonts;
import theme.Theme;

/**
 * หน้าแสดงชื่อหมวดหมู่และจำนวนหนังสือในแต่ละหมวด
 *
 * <p>จำนวนหนังสือดึงจาก BookData ตอนสร้างหน้า ส่วนช่องค้นหาเป็นเพียง
 * ตัวอย่างหน้าตาและยังไม่กรองรายการ</p>
 */
public class CategoriesScreen extends JFrame {

    /** ชื่อหมวดที่ต้องการแสดงบนหน้านี้ */
    private static final String[] NAMES = {"ทั้งหมด", "วรรณกรรม", "แฟนตาซี", "ปรัชญา", "ทั่วไป", "การศึกษา"};

    /** สร้างหน้าต่างหมวดหมู่และนับจำนวนหนังสือจากข้อมูลตัวอย่าง */
    public CategoriesScreen() {
        Fonts.install();
        setTitle("KU Goodbook - หมวดหมู่");
        setSize(1280, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // บอก Sidebar ว่าหมวดหมู่เป็นหน้าที่กำลังเปิดอยู่
        add(new Sidebar("หมวดหมู่"), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout(0, 18));
        main.setBackground(Theme.bg());
        main.setBorder(new EmptyBorder(22, 28, 0, 20));
        add(main, BorderLayout.CENTER);

        // วางชื่อและคำอธิบายไว้ซ้าย ส่วนช่องค้นหาตัวอย่างอยู่ทางขวา
        JLabel title = new JLabel("หมวดหมู่");
        title.setFont(Fonts.title(26));
        title.setForeground(Theme.accent());
        JLabel sub = new JLabel("เลือกหมวดที่อยากอ่าน");
        sub.setFont(Fonts.body(14));
        sub.setForeground(Theme.muted());
        JPanel texts = new JPanel(new GridLayout(2, 1));
        texts.setOpaque(false);
        texts.add(title);
        texts.add(sub);

        InputField search = new InputField("ค้นหาชื่อเรื่อง ผู้แต่ง", false);
        search.setPreferredSize(new Dimension(320, 44));
        JPanel searchWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        searchWrap.setOpaque(false);
        searchWrap.add(search);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(texts, BorderLayout.WEST);
        topBar.add(searchWrap, BorderLayout.EAST);
        main.add(topBar, BorderLayout.NORTH);

        // สร้างการ์ดตามรายชื่อหมวดที่กำหนดไว้ด้านบน
        JPanel grid = new JPanel(new GridLayout(0, 3, 18, 18));
        grid.setOpaque(false);
        for (String name : NAMES) {
            // byCategory("ทั้งหมด") คืนหนังสือทุกเรื่อง ส่วนหมวดอื่นคืนเฉพาะที่ตรงกัน
            int count = BookData.byCategory(name).size();
            grid.add(new CategoryTile(name, count + " เรื่อง", colorOf(name)));
        }
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, 0, 24, 10));
        wrapper.add(grid, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.getVerticalScrollBar().setUI(new SlimScrollBarUI());
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        main.add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    /*
     * ใช้สีปกของหนังสือเล่มแรกในหมวดเพื่อให้การ์ดสื่อถึงเรื่องในหมวดนั้น
     * หมวด "ทั้งหมด" หรือหมวดที่ยังไม่มีหนังสือจะใช้สีแดงหลักของแอป
     */
    private static Color colorOf(String category) {
        if (category.equals("ทั้งหมด") || BookData.byCategory(category).isEmpty()) {
            return new Color(0x8B1E24);
        }
        return BookData.byCategory(category).get(0).color;
    }

    /**
     * วาดการ์ดหนึ่งใบที่แสดงชื่อหมวดและจำนวนหนังสือ
     * ใช้สีที่ส่งเข้ามาเป็นพื้นหลังและวาดลวดลายสันหนังสือประกอบ
     */
    private static class CategoryTile extends JComponent {
        private final String name;
        private final String count;
        private final Color color;

        CategoryTile(String name, String count, Color color) {
            this.name = name;
            this.count = count;
            this.color = color;
            setPreferredSize(new Dimension(300, 150));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // ลดพื้นที่วาดเล็กน้อยเพื่อเหลือที่ให้เงาโดยไม่ชนขอบ
            int w = getWidth() - 6;
            int h = getHeight() - 6;

            // เพิ่มความเข้มของเงาในโหมดกลางคืนเพื่อให้เห็นชัด
            g2.setColor(new Color(60, 35, 15, Theme.dark ? 90 : 45));
            g2.fillRoundRect(5, 5, w, h, 26, 26);

            Color dark = new Color((int) (color.getRed() * 0.4), (int) (color.getGreen() * 0.4), (int) (color.getBlue() * 0.4));
            // ใช้สีหมวดเป็นสีเริ่มต้น แล้วไล่ไปยังเฉดที่เข้มกว่า
            g2.setPaint(new GradientPaint(0, 0, color, w, h, dark));
            g2.fillRoundRect(0, 0, w, h, 26, 26);

            // กรอบสีทองและสันหนังสือเป็นลวดลายตกแต่งของการ์ด
            g2.setColor(new Color(217, 178, 111, 140));
            g2.drawRoundRect(9, 9, w - 18, h - 18, 18, 18);

            int[] spineHeights = {64, 92, 54};
            for (int i = 0; i < 3; i++) {
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(w - 110 + i * 26, h - 22 - spineHeights[i], 20, spineHeights[i], 4, 4);
            }

            // ใช้สีอ่อนเพื่อให้อ่านชื่อหมวดบนพื้นเข้มได้ชัด
            g2.setColor(new Color(0xF6ECD6));
            g2.setFont(Fonts.title(24));
            g2.drawString(name, 28, h - 52);
            g2.setFont(Fonts.body(14));
            g2.drawString(count, 28, h - 28);
            g2.dispose();
        }
    }

    /**
     * เปิดหน้านี้โดยตรงเพื่อทดลองหน้าจอ โดยไม่ต้องเริ่มจาก Main
     *
     * @param args ค่าจากคำสั่งเปิดโปรแกรม (ยังไม่ได้ใช้)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CategoriesScreen::new);
    }
}
