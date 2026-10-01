import javax.swing.*;
import java.awt.*;

/**
 * [View Screen]
 * หน้าแรก (Home Screen) แสดงรายการหนังสือ ตัวกรอง และช่องค้นหา
 */
public class HomeScreen extends JFrame {

    public HomeScreen() {
        setTitle("KU Goodbook - หน้าแรก");
        setSize(1120, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. ใส่ AppHeader ไว้ส่วนบนสุด
        add(new AppHeader(), BorderLayout.NORTH);

        // 2. สร้าง Container หลักของเนื้อหา
        JPanel bodyContainer = new JPanel(new BorderLayout(0, 10));
        bodyContainer.setBackground(Theme.PAPER);
        bodyContainer.setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));

        // ครอบด้วย JScrollPane เพื่อให้หน้าจอเลื่อนเมาส์ขึ้นลงได้
        JScrollPane mainScroll = new JScrollPane(bodyContainer);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16); // สกอร์ลได้ลื่นขึ้น
        add(mainScroll, BorderLayout.CENTER);

        // --------------------------------------------------------------------
        // ส่วนบนของ Body: Hero Title, อ่านค้างไว้, เครื่องมือค้นหา และ หมวดหมู่
        // --------------------------------------------------------------------
        JPanel topSection = new JPanel(new GridLayout(0, 1));
        topSection.setBackground(Theme.PAPER);
        bodyContainer.add(topSection, BorderLayout.NORTH);

        // ข้อความต้อนรับ
        JLabel heroTitle = new JLabel("พลิกหน้ากระดาษ อ่านเรื่องที่ใช่");
        heroTitle.setFont(new Font(Theme.FONT_TITLE, Font.BOLD, 28));
        heroTitle.setForeground(Theme.ACCENT);
        topSection.add(heroTitle);

        // แถบค้นหา และ ตัวเลือกเรียงลำดับ
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.setBackground(Theme.PAPER);
        searchRow.add(new JLabel("ค้นหา (ชื่อเรื่อง/ผู้แต่ง)"));
        searchRow.add(new JTextField(16));
        searchRow.add(new JLabel("เรียง"));
        searchRow.add(new JComboBox<>(new String[]{"ยอดนิยม", "ใหม่ล่าสุด", "ชื่อ A-Z"}));
        searchRow.add(new JToggleButton("รายการโปรด"));
        topSection.add(searchRow);

        // แถบปุ่มเลือกหมวดหมู่ (Category Pills)
        JPanel categoryRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryRow.setBackground(Theme.PAPER);
        String[] categories = {"ทั้งหมด", "วรรณกรรม", "แฟนตาซี", "ปรัชญา", "ทั่วไป", "การศึกษา"};
        ButtonGroup categoryGroup = new ButtonGroup();
        for (int i = 0; i < categories.length; i++) {
            JToggleButton pillBtn = new JToggleButton(categories[i]);
            if (i == 0) pillBtn.setSelected(true);
            categoryGroup.add(pillBtn);
            categoryRow.add(pillBtn);
        }
        topSection.add(categoryRow);

        // --------------------------------------------------------------------
        // ส่วนกลางของ Body: ตารางรายการหนังสือ (8 เล่ม: 2 แถว x 4 คอลัมน์)
        // --------------------------------------------------------------------
        JPanel bookGrid = new JPanel(new GridLayout(2, 4, 20, 20));
        bookGrid.setBackground(Theme.PAPER);

        // ตัวอย่างข้อมูลหนังสือ 8 เรื่อง
        bookGrid.add(new BookCard(new Color(0x7B2CBF), "เจ้าชายน้อย", "The Little Prince", "แอนตวง เดอ ซานเต็กซูเปรี", "วรรณกรรม", "4.9", "จบแล้ว"));
        bookGrid.add(new BookCard(new Color(0x386641), "เดอะลอร์ดออฟเดอะริงส์", "The Lord of the Rings", "เจ. อาร์. อาร์. โทลคีน", "แฟนตาซี", "4.8", "จบแล้ว"));
        bookGrid.add(new BookCard(new Color(0xBC4749), "เดอะ แอลเคมิสต์", "The Alchemist", "เปาโล โกเอลโญ", "ปรัชญา", "4.7", "จบแล้ว"));
        bookGrid.add(new BookCard(new Color(0x023E8A), "Story of 67", "", "sincondee", "ทั่วไป", "4.5", "กำลังอัปเดต"));
        bookGrid.add(new BookCard(new Color(0x582F0E), "Scouting for Boys", "", "โรเบิร์ต เบเดน-โพเอลล์", "การศึกษา", "4.6", "จบแล้ว"));
        bookGrid.add(new BookCard(new Color(0x9A031E), "แฮร์รี่ พอตเตอร์กับศิลาอาถรรพ์", "Harry Potter and...", "เจ. เค. โรว์ลิ่ง", "แฟนตาซี", "4.9", "จบแล้ว"));
        bookGrid.add(new BookCard(new Color(0x283618), "เดอะฮอบบิท", "The Hobbit", "เจ. อาร์. อาร์. โทลคีน", "แฟนตาซี", "4.8", "จบแล้ว"));
        bookGrid.add(new BookCard(new Color(0x0077B6), "อลิซในแดนมหัศจรรย์", "Alice's Adventures...", "ลูวิส แคร์รอลล์", "แฟนตาซี", "4.7", "จบแล้ว"));

        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setBackground(Theme.PAPER);
        gridWrapper.add(bookGrid, BorderLayout.NORTH);
        bodyContainer.add(gridWrapper, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomeScreen::new);
    }
}