package theme;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * จัดการฟอนต์ที่ใช้ในแอปไว้ที่เดียว
 *
 * <p>หัวข้อใช้ Chonburi, ข้อความทั่วไปใช้ Sarabun และเนื้อหาหนังสือใช้ Pridi
 * ถ้าไฟล์ฟอนต์ไม่มีหรือเปิดไม่ได้ แอปจะแจ้งข้อความในหน้าต่างคำสั่ง
 * แล้วเปลี่ยนไปใช้ Leelawadee UI แทน</p>
 */
public class Fonts {

    private static final Font TITLE = load("fonts/Chonburi-Regular.ttf", Font.PLAIN);
    private static final Font BODY = load("fonts/Sarabun-Regular.ttf", Font.PLAIN);
    private static final Font BOLD  = load("fonts/Sarabun-Bold.ttf", Font.BOLD);
    private static final Font READ  = load("fonts/Pridi-Regular.ttf", Font.PLAIN);

    /** ฟอนต์ถูกเก็บเป็นค่ากลาง จึงไม่ต้องสร้าง object ของ Fonts */
    private Fonts() {
    }

    /**
     * เลือกฟอนต์ Chonburi สำหรับหัวข้อ
     *
     * @param size ขนาดตัวอักษร
     * @return ฟอนต์หัวข้อตามขนาดที่เลือก
     */
    public static Font title(float size) {
        return TITLE.deriveFont(size);
    }

    /**
     * เลือกฟอนต์ Sarabun สำหรับข้อความทั่วไป
     *
     * @param size ขนาดตัวอักษร
     * @return ฟอนต์ข้อความตามขนาดที่เลือก
     */
    public static Font body(float size) {
        return BODY.deriveFont(size);
    }

    /**
     * เลือกฟอนต์ Sarabun ตัวหนาสำหรับป้ายและปุ่ม
     *
     * @param size ขนาดตัวอักษร
     * @return ฟอนต์ตัวหนาตามขนาดที่เลือก
     */
    public static Font bold(float size) {
        return BOLD.deriveFont(size);
    }

    /**
     * เลือกฟอนต์ Pridi สำหรับเนื้อหาหนังสือ
     *
     * @param size ขนาดตัวอักษร
     * @return ฟอนต์เนื้อหาตามขนาดที่เลือก
     */
    public static Font read(float size) {
        return READ.deriveFont(size);
    }

    /**
     * ตั้งฟอนต์เริ่มต้นของส่วนประกอบ Swing ให้เป็น Sarabun
     *
     * <p>วิธีนี้ช่วยให้ปุ่มและช่องกรอกที่ยังไม่ได้กำหนดฟอนต์เอง
     * ใช้รูปแบบเดียวกัน ควรเรียกก่อนสร้างหน้าจอ</p>
     */
    public static void install() {
        FontUIResource base = new FontUIResource(body(14));
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        // ไล่ดูส่วนประกอบมาตรฐาน แล้วเปลี่ยนเฉพาะค่าที่เป็นฟอนต์
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (UIManager.get(key) instanceof FontUIResource) {
                UIManager.put(key, base);
            }
        }
    }

    /**
     * อ่านไฟล์ฟอนต์และเพิ่มฟอนต์นั้นให้ Java ใช้งาน
     * หากเกิดปัญหา จะใช้ฟอนต์สำรองเพื่อให้แอปยังแสดงข้อความได้
     *
     * @param path ที่อยู่ของไฟล์ฟอนต์
     * @param fallbackStyle รูปแบบของฟอนต์สำรอง
     * @return ฟอนต์ที่โหลดได้ หรือฟอนต์สำรอง
     */
    private static Font load(String path, int fallbackStyle) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (FontFormatException | IOException e) {
            System.err.println("โหลดฟอนต์ " + path + " ไม่สำเร็จ ใช้ Leelawadee UI แทน: " + e.getMessage());
            return new Font("Leelawadee UI", fallbackStyle, 12);
        }
    }
}
