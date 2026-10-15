package app;

// ไกด์ไฟล์: หน้าที่: รวมคำสั่งเปิดหน้าจอใหม่และปิดหน้าจอเดิมไว้ที่เดียว
// เมธอด openHome/openFavorites/openCategories/openLogin/openRegister ใช้เปลี่ยนหน้า
// เมธอด openDetail/openReader ส่งข้อมูลหนังสือไปหน้าถัดไป; เพิ่มทางนำทางใหม่ในคลาสนี้

import books.Book;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;
import javax.swing.*;
import ui.pages.AdminScreen;
import ui.pages.CategoriesScreen;
import ui.pages.DetailScreen;
import ui.pages.FavoritesScreen;
import ui.pages.HomeScreen;
import ui.pages.LoginScreen;
import ui.pages.ReaderScreen;
import ui.pages.RegisterScreen;

/**
 * รวมคำสั่งเปิดและปิดหน้าจอไว้ในคลาสเดียว
 *
 * <p>เมื่อเปลี่ยนหน้า จะเปิดหน้าต่างใหม่ก่อนแล้วค่อยปิดหน้าต่างเดิม
 * วิธีนี้ช่วยไม่ให้โปรแกรมปิดตัวลงระหว่างเปลี่ยนหน้า</p>
 */
public class Nav {

    /** คลาสนี้มีแต่คำสั่งเปลี่ยนหน้า จึงเรียกใช้ได้เลยโดยไม่ต้องสร้าง Nav */
    private Nav() {
    }

    /**
     * เปิดหน้าแรก แล้วปิดหน้าต่างที่ผู้ใช้กดปุ่มมาจาก
     *
     * @param from ส่วนบนหน้าจอเดิมที่ผู้ใช้กด เช่น ปุ่มหรือเมนู
     */
    public static void openHome(Component from) {
        switchWindow(from, HomeScreen::new);
    }

    /**
     * เปิดหน้ารายการโปรด แล้วปิดหน้าต่างเดิม
     *
     * @param from ส่วนบนหน้าจอเดิมที่ผู้ใช้กด เช่น ปุ่มหรือเมนู
     */
    public static void openFavorites(Component from) {
        switchWindow(from, FavoritesScreen::new);
    }

    /**
     * เปิดหน้าหมวดหมู่ แล้วปิดหน้าต่างเดิม
     *
     * @param from ส่วนบนหน้าจอเดิมที่ผู้ใช้กด เช่น ปุ่มหรือเมนู
     */
    public static void openCategories(Component from) {
        switchWindow(from, CategoriesScreen::new);
    }

    /**
     * เปิดหน้าเข้าสู่ระบบ แล้วปิดหน้าต่างเดิม
     *
     * @param from ส่วนบนหน้าจอเดิมที่ผู้ใช้กด เช่น ปุ่มหรือลิงก์
     */
    public static void openLogin(Component from) {
        switchWindow(from, LoginScreen::new);
    }

    /**
     * เปิดหน้าสมัครสมาชิก แล้วปิดหน้าต่างเดิม
     *
     * @param from ส่วนบนหน้าจอเดิมที่ผู้ใช้กด เช่น ปุ่มหรือลิงก์
     */
    public static void openRegister(Component from) {
        switchWindow(from, RegisterScreen::new);
    }

    /** เปิดหน้าจัดการหนังสือ โดยรักษาสถานะและขนาดหน้าต่างเดิม */
    public static void openAdmin(Component from) {
        if (!AdminSession.isLoggedIn()) {
            openLogin(from);
            return;
        }
        switchWindow(from, AdminScreen::new);
    }

    /**
     * เปิดหน้ารายละเอียดของหนังสือที่เลือก แล้วปิดหน้าต่างเดิม
     *
     * @param from ส่วนบนหน้าจอเดิมที่ผู้ใช้กด
     * @param book ข้อมูลหนังสือที่จะส่งไปแสดง
     */
    public static void openDetail(Component from, Book book) {
        switchWindow(from, () -> new DetailScreen(book));
    }

    /**
     * เปิดหน้าอ่านตอนที่เลือก แล้วปิดหน้าต่างเดิม
     *
     * @param from ส่วนบนหน้าจอเดิมที่ผู้ใช้กด
     * @param book หนังสือที่ต้องการอ่าน
     * @param chapterIndex ตำแหน่งของตอนในรายการ โดยตำแหน่งแรกคือ 0
     */
    public static void openReader(Component from, Book book, int chapterIndex) {
        switchWindow(from, () -> new ReaderScreen(book, chapterIndex));
    }

    /**
     * ดูชื่อเมนูที่ผู้ใช้กด แล้วเปิดหน้าให้ตรงกับชื่อนั้น
     *
     * @param from ส่วนของหน้าจอที่ผู้ใช้กดเมนู
     * @param menuName ข้อความบนเมนู เช่น "หน้าแรก" หรือ "รายการโปรด"
     */
    public static void openMenu(Component from, String menuName) {
        if (menuName.equals("หน้าแรก")) {
            openHome(from);
        } else if (menuName.equals("รายการโปรด")) {
            openFavorites(from);
        } else if (menuName.equals("หมวดหมู่")) {
            openCategories(from);
        } else if (menuName.equals("ออกจากระบบ")) {
            UserSession.logout();
            AdminSession.logout();
            openLogin(from);
        }
    }

    /**
     * ทำให้ส่วนที่กำหนดตอบสนองเมื่อคลิก เช่น การ์ดหนังสือหรือข้อความลิงก์
     * เมื่อเอาเมาส์ไปชี้ จะเปลี่ยนเป็นรูปมือเพื่อบอกว่ากดได้
     *
     * @param component ส่วนที่ต้องการให้คลิกได้
     * @param action คำสั่งที่จะทำหลังจากคลิก
     */
    public static void onClick(JComponent component, Runnable action) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
    }

    /** ส่งต่อสถานะขยาย หรือขนาดและตำแหน่งของหน้าต่างปกติ ก่อนปิดหน้าเดิม */
    private static void switchWindow(Component from, Supplier<? extends JFrame> factory) {
        Window source = from instanceof Window ? (Window) from
                : (from == null ? null : SwingUtilities.getWindowAncestor(from));
        boolean inherit = source instanceof Frame && source.isDisplayable();
        int state = inherit ? ((Frame) source).getExtendedState() & ~Frame.ICONIFIED
                : Frame.NORMAL;
        Rectangle bounds = inherit ? source.getBounds() : null;

        JFrame target = factory.get();
        if (inherit) {
            if ((state & Frame.MAXIMIZED_BOTH) == 0) {
                target.setBounds(bounds);
            } else {
                // วางบนจอเดียวกับหน้าเดิมก่อนขยาย โดยไม่ใช้ขนาดเต็มจอเป็นขนาดปกติ
                target.setLocation(bounds.x, bounds.y);
            }
            target.setExtendedState(state);
        }
        close(from);
    }

    /**
     * หาหน้าต่างที่ครอบส่วนต้นทาง แล้วปิดหน้าต่างนั้น
     * ถ้าหาหน้าต่างไม่พบ จะข้ามการปิดเพื่อไม่ให้เกิดข้อผิดพลาด
     */
    private static void close(Component from) {
        Window window;
        if (from instanceof Window) {
            window = (Window) from;
        } else if (from != null) {
            window = SwingUtilities.getWindowAncestor(from);
        } else {
            window = null;
        }
        if (window != null) {
            window.dispose();
        }
    }
}
