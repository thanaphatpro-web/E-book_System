package screens;

import components.BookCard;
import components.InputField;
import components.PillButton;
import components.Sidebar;
import components.SlimScrollBarUI;
import data.BookData;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import theme.Fonts;
import theme.Theme;

/**
 * หน้าแสดงรายการหนังสือโปรดตัวอย่าง
 *
 * <p>รายการที่เลือกเก็บไว้กำหนดด้วยลำดับใน BookData.ALL
 * ปุ่มค้นหา ปุ่มเรียง และปุ่มลบเป็นเพียงหน้าตาตัวอย่าง ยังไม่เปลี่ยนข้อมูลจริง</p>
 */
public class FavoritesScreen extends JFrame {

    /*
     * เลือกหนังสือจากตำแหน่งใน BookData.ALL โดยเริ่มตำแหน่งแรกที่ 0
     * เมื่อต้องการเปลี่ยนรายการโปรดตัวอย่าง ให้แก้ตัวเลขในรายการนี้
     */
    private static final int[] FAVORITES = {0, 1, 5, 2, 6, 7};

    /** สร้างหน้าต่างรายการโปรดและแสดงหนังสือตามตำแหน่งที่กำหนดไว้ */
    public FavoritesScreen() {
        Fonts.install();
        setTitle("KU Goodbook - รายการโปรด");
        setSize(1280, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // บอกแถบเมนูว่ารายการโปรดเป็นหน้าปัจจุบัน
        add(new Sidebar("รายการโปรด"), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout(0, 14));
        main.setBackground(Theme.bg());
        main.setBorder(new EmptyBorder(22, 28, 0, 20));
        add(main, BorderLayout.CENTER);

        // แสดงชื่อหน้าและจำนวนหนังสือ พร้อมช่องค้นหาและตัวเลือกเรียงตัวอย่าง
        JLabel title = new JLabel("รายการโปรดของคุณ");
        title.setFont(Fonts.title(26));
        title.setForeground(Theme.accent());
        JLabel sub = new JLabel("คุณมีหนังสือที่บันทึกไว้ทั้งหมด " + FAVORITES.length + " เรื่อง");
        sub.setFont(Fonts.body(14));
        sub.setForeground(Theme.muted());
        JPanel texts = new JPanel(new GridLayout(2, 1));
        texts.setOpaque(false);
        texts.add(title);
        texts.add(sub);

        InputField search = new InputField("ค้นหาในรายการโปรด", false);
        search.setPreferredSize(new Dimension(320, 44));
        JPanel searchWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        searchWrap.setOpaque(false);
        searchWrap.add(search);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(texts, BorderLayout.WEST);
        topBar.add(searchWrap, BorderLayout.EAST);

        JPanel sortRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sortRow.setOpaque(false);
        sortRow.add(new PillButton("เรียงตาม: เพิ่มล่าสุด", PillButton.OUTLINE));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        top.add(topBar, BorderLayout.NORTH);
        top.add(sortRow, BorderLayout.CENTER);
        main.add(top, BorderLayout.NORTH);

        // สร้างการ์ดสำหรับหนังสือที่อยู่ในรายการโปรดตัวอย่าง
        JPanel grid = new JPanel(new GridLayout(0, 3, 18, 18));
        grid.setOpaque(false);
        for (int index : FAVORITES) {
            grid.add(new BookCard(BookData.ALL.get(index), true));
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

    /**
     * เปิดหน้านี้โดยตรงเพื่อทดลองหน้าจอ โดยไม่ต้องเริ่มจาก Main
     *
     * @param args ค่าจากคำสั่งเปิดโปรแกรม (ยังไม่ได้ใช้)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FavoritesScreen::new);
    }
}
