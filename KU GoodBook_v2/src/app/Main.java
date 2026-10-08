package app;

import screens.LoginScreen;

import javax.swing.*;

/**
 * จุดเริ่มต้นของแอป KU GoodBook
 *
 * <p>เมื่อเปิดโปรแกรม คลาสนี้จะแสดงหน้าเข้าสู่ระบบก่อน
 * จากนั้นปุ่มต่าง ๆ จะพาผู้ใช้ไปหน้าอื่นผ่านคลาส Nav</p>
 */
public class Main {

    /**
     * ไม่ต้องสร้าง Main เพราะเราใช้แค่เมธอด main เพื่อเริ่มแอป
     */
    private Main() {
    }

    /**
     * เริ่มแอปโดยเปิดหน้าเข้าสู่ระบบ
     *
     * Swing มีเธรดสำหรับดูแลหน้าต่างและรับการกดปุ่มโดยเฉพาะ
     * จึงใช้ invokeLater เพื่อให้สร้างหน้าจอบนเธรดที่ถูกต้อง
     *
     * @param args ข้อความที่ส่งมาตอนเปิดโปรแกรม ปัจจุบันยังไม่ได้ใช้
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}
