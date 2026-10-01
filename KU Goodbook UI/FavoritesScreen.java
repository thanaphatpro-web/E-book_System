import javax.swing.*;
import java.awt.*;

/**
 * [View Screen]
 * หน้ารายการโปรด (Favorites Screen) แสดงรายการหนังสือที่ผู้ใช้กดบันทึกหัวใจ/รายการโปรดเอาไว้
 */
public class FavoritesScreen extends JFrame {

    public FavoritesScreen() {
        setTitle("รายการโปรด");
        setSize(1120, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. ใส่ AppHeader ด้านบนสุด
        add(new AppHeader(), BorderLayout.NORTH);

        // 2. สร้าง Container หลักของเนื้อหา
        JPanel bodyContainer = new JPanel(new BorderLayout(0, 16));
        bodyContainer.setBackground(Theme.PAPER);
        bodyContainer.setBorder(BorderFactory.createEmptyBorder(18, 28, 18, 28));

        // ครอบด้วย JScrollPane เพื่อให้เลื่อนเมาส์ขึ้นลงได้
        JScrollPane mainScroll = new JScrollPane(bodyContainer);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);

        // --------------------------------------------------------------------
        // ส่วนบนของ Body: หัวข้อรายการโปรด, จำนวนรายการ และ แถบเครื่องมือ
        // --------------------------------------------------------------------
        JPanel topSection = new JPanel(new GridLayout(0, 1, 0, 8));
        topSection.setBackground(Theme.PAPER);
        bodyContainer.add(topSection, BorderLayout.NORTH);

        // หัวข้อหน้า
        JLabel heroTitle = new JLabel("รายการโปรดของคุณ");
        heroTitle.setFont(new Font(Theme.FONT_TITLE, Font.BOLD, 28));
        heroTitle.setForeground(Theme.ACCENT);
        topSection.add(heroTitle);

        // ข้อความสรุปจำนวนหนังสือที่บันทึกไว้
        JLabel subtitle = new JLabel("คุณมีหนังสือที่บันทึกไว้ทั้งหมด 4 เรื่อง");
        subtitle.setFont(new Font(Theme.FONT_BODY, Font.PLAIN, 14));
        subtitle.setForeground(Theme.INK);
        topSection.add(subtitle);

        // แถบเครื่องมือ (ค้นหาในรายการโปรด / เรียงลำดับ / ปุ่มล้างทั้งหมด)
        JPanel toolRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolRow.setBackground(Theme.PAPER);
        
        toolRow.add(new JLabel("ค้นหาในรายการโปรด:"));
        toolRow.add(new JTextField(14));
        
        toolRow.add(new JLabel("เรียงตาม:"));
        toolRow.add(new JComboBox<>(new String[]{"เพิ่มล่าสุด", "คะแนนสูงสุด"}));
        
        JButton btnClearAll = new JButton("ลบรายการโปรดทั้งหมด");
        toolRow.add(btnClearAll);
        topSection.add(toolRow);

        // --------------------------------------------------------------------
        // ส่วนกลางของ Body: ตารางหนังสือโปรด (Grid แสดงรายการที่บันทึกไว้)
        // --------------------------------------------------------------------
        JPanel bookGrid = new JPanel(new GridLayout(0, 4, 20, 20));
        bookGrid.setBackground(Theme.PAPER);

        // ตัวอย่างหนังสือ 4 เรื่องที่ถูกบันทึกไว้ในรายการโปรด
        bookGrid.add(new BookCard(new Color(0x7B2CBF), "เจ้าชายน้อย", "The Little Prince", "แอนตวง เดอ ซานเต็กซูเปรี", "วรรณกรรม", "4.9", "จบแล้ว"));
        bookGrid.add(new BookCard(new Color(0x386641), "เดอะลอร์ดออฟเดอะริงส์", "The Lord of the Rings", "เจ. อาร์. อาร์. โทลคีน", "แฟนตาซี", "4.8", "จบแล้ว"));
        bookGrid.add(new BookCard(new Color(0x9A031E), "แฮร์รี่ พอตเตอร์กับศิลาอาถรรพ์", "Harry Potter ", "เจ. เค. โรว์ลิ่ง", "แฟนตาซี", "4.9", "จบแล้ว"));
        bookGrid.add(new BookCard(new Color(0xBC4749), "เดอะ แอลเคมิสต์", "The Alchemist", "เปาโล โกเอลโญ", "ปรัชญา", "4.7", "จบแล้ว"));

        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setBackground(Theme.PAPER);
        gridWrapper.add(bookGrid, BorderLayout.NORTH);
        bodyContainer.add(gridWrapper, BorderLayout.CENTER);

        // TODO (สำหรับคนทำต่อ): 
        // 1. ดึงรายการ ID หนังสือโปรดจาก Database/User Session มาวนลูปสร้าง BookCard
        // 2. หากไม่มีรายการโปรดเลย สามารถสลับแสดงผล Panel ข้อความ "ยังไม่มีรายการโปรด" (Empty State) แทนได้

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FavoritesScreen::new);
    }
}