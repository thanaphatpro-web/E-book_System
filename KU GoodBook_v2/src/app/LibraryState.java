package app;

import data.Book;
import java.io.IOException;
import java.util.List;
import storage.CsvStorage;

/** เป็นจุดกลางที่หน้าจอใช้จัดการรายการโปรดและประวัติอ่านผ่าน CSV */
public final class LibraryState {
    /** คลาสนี้ใช้เมธอด static จึงไม่ต้องสร้าง object */
    private LibraryState() {
    }

    /** ตรวจว่าหนังสืออยู่ในรายการโปรดของผู้ใช้ปัจจุบัน */
    public static boolean isFavorite(Book book) {
        try {
            return CsvStorage.isFavorite(UserSession.accountKey(), book);
        } catch (IOException e) {
            throw new IllegalStateException("อ่าน favorites.csv ไม่สำเร็จ", e);
        }
    }

    /** เพิ่มหรือลบหนังสือในรายการโปรดของผู้ใช้ปัจจุบัน */
    public static void toggleFavorite(Book book) {
        try {
            CsvStorage.toggleFavorite(UserSession.accountKey(), book);
        } catch (IOException e) {
            throw new IllegalStateException("บันทึก favorites.csv ไม่สำเร็จ", e);
        }
    }

    /** โหลดรายการโปรดของผู้ใช้ปัจจุบันจาก CSV */
    public static List<Book> favorites() {
        try {
            return CsvStorage.loadFavorites(UserSession.accountKey());
        } catch (IOException e) {
            throw new IllegalStateException("อ่าน favorites.csv ไม่สำเร็จ", e);
        }
    }

    /** ตรวจว่าตอนที่ระบุเคยถูกทำเครื่องหมายว่าอ่านแล้วหรือไม่ */
    public static boolean isChapterRead(Book book, int chapterIndex) {
        try {
            return CsvStorage.isChapterRead(UserSession.accountKey(), book, chapterIndex);
        } catch (IOException e) {
            throw new IllegalStateException("อ่าน read_chapters.csv ไม่สำเร็จ", e);
        }
    }

    /** คืนลำดับตอนแรกที่ผู้ใช้ยังไม่ได้ทำเครื่องหมายว่าอ่านแล้ว */
    public static int nextUnreadChapter(Book book) {
        try {
            return CsvStorage.nextUnreadChapter(UserSession.accountKey(), book);
        } catch (IOException e) {
            throw new IllegalStateException("อ่าน read_chapters.csv ไม่สำเร็จ", e);
        }
    }

    /** บันทึกตอนที่อ่านแล้วลง CSV */
    public static void markChapterRead(Book book, int chapterIndex) {
        try {
            CsvStorage.markChapterRead(UserSession.accountKey(), book, chapterIndex);
        } catch (IOException e) {
            throw new IllegalStateException("บันทึก read_chapters.csv ไม่สำเร็จ", e);
        }
    }
}
