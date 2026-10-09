package app;

import java.io.IOException;
import account.AccountStorage;

/** จัดการบัญชีปัจจุบัน และอ่านข้อมูลบัญชีจาก users.csv */
public final class UserSession {
    /** ชื่อผู้ใช้ที่กำลังล็อกอิน */
    private static String currentUsername;

    /** อีเมลบัญชีที่กำลังล็อกอิน ใช้แยกข้อมูลของแต่ละคน */
    private static String currentEmail;

    /** คลาสนี้ใช้เมธอด static จึงไม่ต้องสร้าง object */
    private UserSession() {
    }

    /** เพิ่มบัญชีลง users.csv แล้วตั้งบัญชีนี้เป็นผู้ใช้ปัจจุบัน */
    public static boolean register(String username, String email, String password) {
        try {
            boolean wasCreated = AccountStorage.register(username, email, password);
            if (!wasCreated) {
                return false;
            }

            currentUsername = username.trim();
            currentEmail = email.trim().toLowerCase();
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("บันทึกบัญชีลง users.csv ไม่สำเร็จ", e);
        }
    }

    /** ตรวจอีเมลและรหัสผ่านใน users.csv แล้วโหลดชื่อผู้ใช้และอีเมล */
    public static boolean login(String email, String password) {
        try {
            String[] account = AccountStorage.login(email, password);
            if (account == null) {
                return false;
            }

            currentUsername = account[0];
            currentEmail = account[1];
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("อ่านบัญชีจาก users.csv ไม่สำเร็จ", e);
        }
    }

    /** คืนชื่อผู้ใช้ที่กำลังล็อกอิน */
    public static String username() {
        if (currentUsername == null) {
            return "";
        }
        return currentUsername;
    }

    /** คืนอีเมลของผู้ใช้ปัจจุบัน เพื่อใช้ผูกข้อมูลส่วนตัวใน CSV */
    public static String accountKey() {
        if (currentEmail == null) {
            return "";
        }
        return currentEmail;
    }

    /** ล้างชื่อและอีเมลของผู้ใช้ปัจจุบันเมื่อออกจากระบบ */
    public static void logout() {
        currentUsername = null;
        currentEmail = null;
    }
}
