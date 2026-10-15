package logic.admin;

// ไกด์ไฟล์: หน้าที่: ตรวจสิทธิ์และตรวจข้อมูลก่อนส่งคำสั่งจัดการหนังสือไป AdminStorage
// addBook/updateBook/deleteBook เป็นทางหลักของการเพิ่ม แก้ไข และลบหนังสือ
// เมธอด validate คุมข้อมูลที่จำเป็นและปีพิมพ์; การเขียนข้อมูลจริงอยู่ใน AdminStorage

import app.AdminSession;
import app.ProjectPaths;
import storage.admin.AdminStorage;
import books.Book;
import books.Chapter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;

/** ตรวจข้อมูลหนังสือจากฟอร์ม แล้วส่งให้ AdminStorage บันทึก */
public final class AdminBookLogic {

    private AdminBookLogic() {
    }

    /** ตรวจข้อมูลฟอร์ม แล้วให้ AdminStorage บันทึกหนังสือและตอน */
    public static Book addBook(String title, String titleEn, String author, String category,
                               String yearText, String status, String blurb, Path cover,
                               List<Chapter> chapters) throws IOException {
        int year = validate(title, author, category, yearText, status, blurb, chapters);
        validateCover(cover);
        return AdminStorage.addBook(title, titleEn, author, category, status, year,
                blurb, cover, chapters);
    }

    /** ตรวจข้อมูลแบบเดียวกับการเพิ่ม ก่อนส่งข้อมูลไปแก้ไขหนังสือ */
    public static Book updateBook(Book oldBook, String title, String titleEn, String author,
                                  String category, String yearText, String status, String blurb,
                                  Path cover, List<Chapter> chapters) throws IOException {
        int year = validate(title, author, category, yearText, status, blurb, chapters);
        Path imagePath = cover;
        if (imagePath == null && oldBook.coverPath != null && !oldBook.coverPath.isEmpty()) {
            imagePath = ProjectPaths.dataFolder().resolve(oldBook.coverPath);
        }
        validateCover(imagePath);
        return AdminStorage.updateBook(oldBook, title, titleEn, author, category, status,
                year, blurb, cover, chapters);
    }

    /** ลบหนังสือได้เฉพาะตอนแอดมินเข้าสู่ระบบแล้ว */
    public static void deleteBook(Book book) throws IOException {
        if (!AdminSession.isLoggedIn()) {
            throw new IOException("ต้องเข้าสู่ระบบแอดมินก่อนลบหนังสือ");
        }
        AdminStorage.deleteBook(book);
    }

    /** ตรวจรูปให้ผ่านก่อนเริ่มเขียนปก ข้อมูลหนังสือ หรือตอนลงไฟล์ */
    private static void validateCover(Path cover) throws IOException {
        if (cover == null) {
            throw new IOException("กรุณาเลือกรูปปกก่อนบันทึกหนังสือ");
        }
        if (!Files.isRegularFile(cover) || !Files.isReadable(cover)) {
            throw new IOException("ไม่พบรูปปกหรืออ่านไฟล์ไม่ได้ กรุณาเลือกรูปปกใหม่");
        }
        String name = cover.getFileName().toString().toLowerCase(Locale.ROOT);
        if (!name.endsWith(".png") && !name.endsWith(".jpg")
                && !name.endsWith(".jpeg") && !name.endsWith(".gif")) {
            throw new IOException("เลือกรูป .png, .jpg, .jpeg หรือ .gif เท่านั้น");
        }
        try (InputStream input = Files.newInputStream(cover)) {
            if (ImageIO.read(input) == null) {
                throw new IOException("ไฟล์รูปปกไม่ใช่ภาพที่ใช้งานได้ กรุณาเลือกรูปใหม่");
            }
        } catch (IOException error) {
            throw new IOException("เปิดรูปปกไม่ได้หรือไฟล์เสีย กรุณาเลือกรูปใหม่", error);
        }
    }

    /** ตรวจว่าผู้ใช้เป็นแอดมินและตรวจช่องกรอกที่จำเป็น */
    private static int validate(String title, String author, String category, String yearText,
                                String status, String blurb, List<Chapter> chapters)
            throws IOException {
        if (!AdminSession.isLoggedIn()) {
            throw new IOException("ต้องเข้าสู่ระบบแอดมินก่อนจัดการหนังสือ");
        }
        if (title.trim().isEmpty()) {
            throw new IOException("กรุณากรอกชื่อหนังสือภาษาไทย");
        }
        if (author.trim().isEmpty()) {
            throw new IOException("กรุณากรอกชื่อผู้แต่ง");
        }
        if (category.trim().isEmpty()) {
            throw new IOException("กรุณากรอกหมวดหมู่หนังสือ");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IOException("กรุณาเลือกสถานะหนังสือ");
        }
        if (blurb.trim().isEmpty()) {
            throw new IOException("กรุณากรอกเรื่องย่อ");
        }
        if (chapters.isEmpty()) {
            throw new IOException("เพิ่มเนื้อหาอย่างน้อยหนึ่งตอนก่อนบันทึก");
        }
        for (int i = 0; i < chapters.size(); i++) {
            Chapter chapter = chapters.get(i);
            if (chapter.title.trim().isEmpty() || chapter.content.trim().isEmpty()) {
                throw new IOException("ตอนที่ " + (i + 1) + " มีชื่อหรือเนื้อหาไม่ครบ");
            }
        }

        int year;
        try {
            year = Integer.parseInt(yearText.trim());
        } catch (NumberFormatException error) {
            throw new IOException("กรุณากรอกปีที่พิมพ์เป็นตัวเลข");
        }
        if (year < 1 || year > 9999) {
            throw new IOException("ปีที่พิมพ์ต้องอยู่ระหว่าง 1 ถึง 9999");
        }
        if (!"กำลังอัปเดต".equals(status) && !"จบแล้ว".equals(status)) {
            throw new IOException("สถานะหนังสือไม่ถูกต้อง");
        }
        return year;
    }
}
