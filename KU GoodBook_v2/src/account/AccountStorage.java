package account;

import app.ProjectPaths;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/** จัดการข้อมูลสมัครสมาชิกและเข้าสู่ระบบ แยกจากส่วนหนังสือ */
public final class AccountStorage {

    // ไฟล์บัญชีอยู่ในโฟลเดอร์ data ของโปรเจ็กต์ ไม่ขึ้นกับตำแหน่งที่เปิดโปรแกรม
    private static final Path USER_FILE = ProjectPaths.dataFolder().resolve("users.csv");
    private static final String[] HEADER = {"username", "email", "password"};

    private AccountStorage() {
        // ไม่ต้องสร้าง object เพราะเรียกใช้เมธอดแบบ static
    }

    /** สร้างไฟล์ users.csv และหัวตารางเมื่อยังไม่มีไฟล์ */
    public static void initialize() throws IOException {
        Files.createDirectories(USER_FILE.getParent());

        if (!Files.exists(USER_FILE) || Files.size(USER_FILE) == 0) {
            BufferedWriter writer = Files.newBufferedWriter(
                    USER_FILE, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            writeRow(writer, HEADER);
            writer.close();
        }
    }

    /** คืนตำแหน่งเต็มของไฟล์บัญชี เพื่อให้ตรวจดูไฟล์ที่บันทึกได้ */
    public static Path userFilePath() {
        return USER_FILE.toAbsolutePath();
    }

    /** สมัครบัญชีใหม่ ถ้าอีเมลนี้มีคนใช้แล้วจะคืนค่า false */
    public static boolean register(String username, String email, String password)
            throws IOException {
        List<String[]> users = readUsers();
        String cleanEmail = normalizeEmail(email);

        for (String[] user : users) {
            if (user.length >= 2 && normalizeEmail(user[1]).equals(cleanEmail)) {
                return false;
            }
        }

        BufferedWriter writer = Files.newBufferedWriter(
                USER_FILE, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        String[] newUser = {username.trim(), cleanEmail, password};
        writeRow(writer, newUser);
        writer.close();
        return true;
    }

    /** ตรวจอีเมลกับรหัสผ่าน ถ้าถูกต้องคืนชื่อผู้ใช้และอีเมล */
    public static String[] login(String email, String password) throws IOException {
        List<String[]> users = readUsers();
        String cleanEmail = normalizeEmail(email);

        for (String[] user : users) {
            if (user.length >= 3
                    && normalizeEmail(user[1]).equals(cleanEmail)
                    && user[2].equals(password)) {
                return new String[]{user[0], user[1]};
            }
        }
        return null;
    }

    /** อ่านทุกแถวในไฟล์ โดยข้ามแถวแรกที่เป็นหัวตาราง */
    private static List<String[]> readUsers() throws IOException {
        initialize();
        List<String[]> users = new ArrayList<String[]>();
        BufferedReader reader = Files.newBufferedReader(USER_FILE, StandardCharsets.UTF_8);
        String line;
        boolean firstLine = true;

        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
            } else if (!line.trim().isEmpty()) {
                users.add(parseRow(line));
            }
        }
        reader.close();
        return users;
    }

    /** แบ่ง CSV เป็นคอลัมน์ โดยรองรับ comma ภายในเครื่องหมายคำพูด */
    private static String[] parseRow(String line) {
        List<String> values = new ArrayList<String>();
        StringBuilder value = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);

            if (current == '"') {
                if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    value.append('"');
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (current == ',' && !insideQuotes) {
                values.add(value.toString());
                value.setLength(0);
            } else {
                value.append(current);
            }
        }

        values.add(value.toString());
        return values.toArray(new String[values.size()]);
    }

    /** เขียนชื่อ อีเมล หรือรหัสผ่านลง CSV หนึ่งแถว */
    private static void writeRow(BufferedWriter writer, String[] values) throws IOException {
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                writer.write(',');
            }

            String value = values[i];
            if (value == null) {
                value = "";
            }
            value = value.replace("\r", " ").replace("\n", " ");

            if (value.contains(",") || value.contains("\"")) {
                value = value.replace("\"", "\"\"");
                writer.write('"');
                writer.write(value);
                writer.write('"');
            } else {
                writer.write(value);
            }
        }
        writer.newLine();
    }

    /** เอาช่องว่างหัวท้ายออกและเปลี่ยนอีเมลเป็นตัวพิมพ์เล็ก */
    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
