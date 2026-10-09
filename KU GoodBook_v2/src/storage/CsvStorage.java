package storage;

import app.ProjectPaths;
import data.Book;
import data.BookData;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** อ่านและเขียน CSV ของบัญชี รายการโปรด ประวัติอ่าน และรีวิว */
public final class CsvStorage {
    /** ใช้โฟลเดอร์ data เดียวกับส่วนบัญชี */
    private static final Path DATA_FOLDER = ProjectPaths.dataFolder();

    /** ชื่อคอลัมน์ในไฟล์รีวิว */
    private static final String[] REVIEW_HEADER = {
        "bookId", "bookTitle", "username", "email", "rating", "comment"
    };

    /** ชื่อคอลัมน์ในไฟล์รายการโปรด */
    private static final String[] FAVORITE_HEADER = {"email", "bookId", "bookTitle"};

    /** ชื่อคอลัมน์ในไฟล์ประวัติอ่านรายตอน */
    private static final String[] READ_HEADER = {
        "email", "bookId", "bookTitle", "chapterIndex", "chapterTitle"
    };

    /** ป้องกันไม่ให้สร้างคลาสนี้ เพราะใช้เมธอด static */
    private CsvStorage() {
    }

    /** สร้างโฟลเดอร์และไฟล์ CSV ที่จำเป็น แล้วโหลดรีวิวจาก CSV */
    public static void initialize() throws IOException {
        Files.createDirectories(DATA_FOLDER);
        ensureFile("reviews.csv", REVIEW_HEADER);
        ensureFile("favorites.csv", FAVORITE_HEADER);
        ensureFile("read_chapters.csv", READ_HEADER);
        reloadReviews();
    }

    /** สร้างไฟล์ใหม่พร้อมแถวหัวตาราง ถ้าไฟล์ยังไม่มีอยู่ */
    private static void ensureFile(String fileName, String[] header) throws IOException {
        Path file = DATA_FOLDER.resolve(fileName);
        if (!Files.exists(file) || Files.size(file) == 0) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    file, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                writeRow(writer, header);
            }
        }
    }

    /** อ่านข้อมูลทุกแถวโดยข้ามแถวหัวตาราง */
    private static List<String[]> readRows(String fileName, String[] header) throws IOException {
        ensureFile(fileName, header);
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                DATA_FOLDER.resolve(fileName), StandardCharsets.UTF_8)) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (!line.trim().isEmpty()) {
                    rows.add(parseRow(line));
                }
            }
        }
        return rows;
    }

    /** แปลงข้อความ CSV หนึ่งบรรทัดให้เป็นรายการคอลัมน์ */
    private static String[] parseRow(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);

            if (current == '"') {
                // เครื่องหมายคำพูดคู่ใน CSV หมายถึงเครื่องหมายคำพูดหนึ่งตัวในข้อมูล
                if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    value.append('"');
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (current == ',' && !insideQuotes) {
                columns.add(value.toString());
                value.setLength(0);
            } else {
                value.append(current);
            }
        }

        columns.add(value.toString());
        return columns.toArray(new String[0]);
    }

    /** เขียนรายการคอลัมน์เป็น CSV หนึ่งบรรทัด */
    private static void writeRow(BufferedWriter writer, String[] columns) throws IOException {
        for (int i = 0; i < columns.length; i++) {
            if (i > 0) {
                writer.write(',');
            }
            writer.write(escape(columns[i]));
        }
        writer.newLine();
    }

    /** ใส่เครื่องหมายคำพูดเมื่อข้อมูลมี comma หรือ quote เพื่อไม่ให้คอลัมน์เลื่อน */
    private static String escape(String value) {
        String safeValue = value == null ? "" : value;
        safeValue = safeValue.replace("\r", " ").replace("\n", " ");

        if (safeValue.contains(",") || safeValue.contains("\"")) {
            safeValue = safeValue.replace("\"", "\"\"");
            return "\"" + safeValue + "\"";
        }
        return safeValue;
    }

    /** เพิ่มข้อมูลหนึ่งแถวต่อท้ายไฟล์ CSV */
    private static void appendRow(String fileName, String[] header, String[] row) throws IOException {
        ensureFile(fileName, header);
        try (BufferedWriter writer = Files.newBufferedWriter(
                DATA_FOLDER.resolve(fileName),
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND)) {
            writeRow(writer, row);
        }
    }

    /** คืนอีเมลรูปแบบเดียวกันเพื่อใช้เปรียบเทียบและผูกข้อมูล */
    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    /** ตรวจว่าหนังสืออยู่ในรายการโปรดของอีเมลนี้หรือไม่ */
    public static boolean isFavorite(String email, Book book) throws IOException {
        List<String[]> rows = readRows("favorites.csv", FAVORITE_HEADER);
        for (String[] row : rows) {
            if (row.length >= 2
                    && normalizeEmail(row[0]).equals(normalizeEmail(email))
                    && row[1].equals(book.id)) {
                return true;
            }
        }
        return false;
    }

    /** เพิ่มหนังสือในรายการโปรด หรือลบแถวเดิมถ้ามีอยู่แล้ว */
    public static void toggleFavorite(String email, Book book) throws IOException {
        List<String[]> rows = readRows("favorites.csv", FAVORITE_HEADER);
        String emailKey = normalizeEmail(email);
        boolean found = false;
        List<String[]> updatedRows = new ArrayList<>();

        for (String[] row : rows) {
            boolean isSameFavorite = row.length >= 2
                    && normalizeEmail(row[0]).equals(emailKey)
                    && row[1].equals(book.id);
            if (isSameFavorite) {
                found = true;
            } else {
                updatedRows.add(row);
            }
        }

        if (!found) {
            updatedRows.add(new String[]{emailKey, book.id, book.titleTh});
        }
        writeRows("favorites.csv", FAVORITE_HEADER, updatedRows);
    }

    /** คืนรายการหนังสือโปรดของผู้ใช้ เรียงตามลำดับใน BookData */
    public static List<Book> loadFavorites(String email) throws IOException {
        List<String[]> rows = readRows("favorites.csv", FAVORITE_HEADER);
        List<Book> result = new ArrayList<>();

        for (Book book : BookData.ALL) {
            for (String[] row : rows) {
                if (row.length >= 2
                        && normalizeEmail(row[0]).equals(normalizeEmail(email))
                        && row[1].equals(book.id)) {
                    result.add(book);
                    break;
                }
            }
        }
        return result;
    }

    /** ตรวจว่าผู้ใช้เคยทำเครื่องหมายอ่านตอนนี้แล้วหรือไม่ */
    public static boolean isChapterRead(String email, Book book, int chapterIndex) throws IOException {
        List<String[]> rows = readRows("read_chapters.csv", READ_HEADER);
        String emailKey = normalizeEmail(email);

        for (String[] row : rows) {
            if (row.length >= 4
                    && normalizeEmail(row[0]).equals(emailKey)
                    && row[1].equals(book.id)
                    && row[3].equals(String.valueOf(chapterIndex))) {
                return true;
            }
        }
        return false;
    }

    /** คืนเลขตอนถัดไปที่ยังไม่ถูกทำเครื่องหมายว่าอ่านแล้ว */
    public static int nextUnreadChapter(String email, Book book) throws IOException {
        for (int i = 0; i < book.chapterTitles.size(); i++) {
            if (!isChapterRead(email, book, i)) {
                return i;
            }
        }
        return -1;
    }

    /** บันทึกตอนที่อ่านแล้ว ถ้ายังไม่มีแถวนี้ในไฟล์ */
    public static void markChapterRead(String email, Book book, int chapterIndex) throws IOException {
        if (isChapterRead(email, book, chapterIndex)) {
            return;
        }

        String chapterTitle = book.chapterTitles.get(chapterIndex);
        appendRow("read_chapters.csv", READ_HEADER, new String[]{
            normalizeEmail(email), book.id, book.titleTh,
            String.valueOf(chapterIndex), chapterTitle
        });
    }

    /** เพิ่มรีวิวลงไฟล์ แล้วโหลดข้อมูลจากไฟล์กลับมาใช้แสดงผล */
    public static void addReview(Book book, String username, String email,
                                 int rating, String comment) throws IOException {
        appendRow("reviews.csv", REVIEW_HEADER, new String[]{
            book.id, book.titleTh, username, normalizeEmail(email),
            String.valueOf(rating), comment
        });
        reloadReviews();
    }

    /** อ่านรีวิวจาก CSV ใหม่ และแทนข้อมูลรีวิวเดิมที่หน้าจอใช้อยู่ */
    private static void reloadReviews() throws IOException {
        List<String[]> rows = readRows("reviews.csv", REVIEW_HEADER);

        // ล้างข้อมูลเดิมก่อน เพื่อไม่ให้รีวิวซ้ำในหน้าจอเมื่อโหลดไฟล์อีกครั้ง
        for (Book book : BookData.ALL) {
            book.reviews.clear();
        }

        for (String[] row : rows) {
            if (row.length < 6) {
                continue;
            }

            for (Book book : BookData.ALL) {
                if (book.id.equals(row[0])) {
                    try {
                        int rating = Integer.parseInt(row[4]);
                        book.addReview(row[2], rating, row[5]);
                    } catch (NumberFormatException e) {
                        // ข้ามแถวที่คะแนนใน CSV ไม่ใช่ตัวเลข
                    }
                    break;
                }
            }
        }
    }

    /** เขียนหัวตารางและข้อมูลทั้งหมดทับไฟล์เดิม ใช้เมื่อลบรายการโปรด */
    private static void writeRows(String fileName, String[] header,
                                  List<String[]> rows) throws IOException {
        Path file = DATA_FOLDER.resolve(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(
                file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writeRow(writer, header);
            for (String[] row : rows) {
                writeRow(writer, row);
            }
        }
    }
}
