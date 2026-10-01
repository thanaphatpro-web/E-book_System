import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * [Theme Component]
 * ศูนย์รวมการตั้งค่าธีม โทนสี และฟอนต์สำหรับทั้งแอปพลิเคชัน
 * หากต้องการเปลี่ยนสไตล์หรือฟอนต์ของระบบ ให้แก้ไขที่ไฟล์นี้จุดเดียว
 */
public class Theme {
    // ------------------------------------------------------------------------
    // 1. โทนสีหลักของแอปพลิเคชัน (Color Palette)
    // ------------------------------------------------------------------------
    public static final Color PAPER  = new Color(233, 220, 192); // สีพื้นหลังใหญ่ (สีกระดาษถนอมสายตา)
    public static final Color CARD   = new Color(246, 236, 214); // สีพื้นหลังการ์ด / หน้ากระดาษอ่าน
    public static final Color INK    = new Color(42, 26, 16);    // สีตัวอักษรหลัก (สีหมึกน้ำตาลเข้ม)
    public static final Color ACCENT = new Color(123, 21, 35);   // สีเน้น / ไฮไลต์ (สีแดงเบอร์กันดี)
    public static final Color SHADOW = new Color(150, 130, 100); // สีเส้นขอบเงาของการ์ด

    // ------------------------------------------------------------------------
    // 2. การกำหนดฟอนต์ (Font Settings)
    // ------------------------------------------------------------------------
    // เปลี่ยนชื่อฟอนต์ที่ตัวแปรนี้จุดเดียว (เช่น "Leelawadee UI", "Sarabun", "Tahoma")
    public static final String FONT_NAME  = "Leelawadee UI"; 
    public static final String FONT_TITLE = FONT_NAME;
    public static final String FONT_BODY  = FONT_NAME;

    // ------------------------------------------------------------------------
    // 3. ระบบตั้งค่า Global Font ให้ Swing Components ทุกชิ้น
    // ------------------------------------------------------------------------
    static {
        // สร้าง FontUIResource สำหรับการใช้งานในระบบ UIManager
        FontUIResource globalFont = new FontUIResource(new Font(FONT_NAME, Font.PLAIN, 14));
        
        // วนลูปเปลี่ยนค่า Default Font ของ Swing Component ทั้งหมดเป็น FONT_NAME
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, globalFont);
            }
        }
    }
}