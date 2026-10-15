package app;

// ไกด์ไฟล์: หน้าที่: คำนวณตำแหน่งโฟลเดอร์ข้อมูลของแอปจากตำแหน่งโปรเจกต์
// เมธอด dataFolder เป็นจุดหลักที่ Storage ทุกส่วนใช้หาไฟล์ CSV และโฟลเดอร์ข้อมูล
// หากย้ายที่เก็บข้อมูล ให้แก้กฎในไฟล์นี้และตรวจสอบคลาสใน package storage ด้วย

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** รวมวิธีหาโฟลเดอร์ data ของโปรเจกต์ไว้ที่เดียว */
public final class ProjectPaths {

    private ProjectPaths() {
    }

    /**
     * ใช้ data ที่อยู่คู่กับ src/app/Main.java ภายในโปรเจกต์ KU_GoodBook
     * ค้นจากตำแหน่งคลาสและตำแหน่งเริ่มรัน รวมถึงโครงสร้างโฟลเดอร์ชั้นนอก
     * ไม่สร้างหรือเลือก data ชั้นนอกเป็นแหล่งข้อมูลสำรอง
     */
    public static Path dataFolder() {
        Path currentFolder = Paths.get(System.getProperty("user.dir"))
                .toAbsolutePath().normalize();
        Path classFolder = null;
        try {
            java.security.CodeSource source = ProjectPaths.class.getProtectionDomain().getCodeSource();
            if (source != null && "file".equalsIgnoreCase(source.getLocation().getProtocol())) {
                classFolder = Paths.get(source.getLocation().toURI()).toAbsolutePath().normalize();
                if (Files.isRegularFile(classFolder)) {
                    classFolder = classFolder.getParent();
                }
            }
        } catch (java.net.URISyntaxException | SecurityException error) {
            // หากอ่านตำแหน่งคลาสไม่ได้ ยังค้นจากตำแหน่งเริ่มรันได้
        }

        for (Path startFolder : new Path[]{classFolder, currentFolder}) {
            for (Path folder = startFolder; folder != null; folder = folder.getParent()) {
                Path[] candidates = {
                    folder,
                    folder.resolve("KU_GoodBook"),
                    folder.resolve("E-book_System-main").resolve("KU_GoodBook")
                };
                for (Path projectFolder : candidates) {
                    Path mainFile = projectFolder.resolve("src/app/Main.java");
                    Path dataFolder = projectFolder.resolve("data");
                    if (Files.isRegularFile(mainFile) && Files.isDirectory(dataFolder)) {
                        return dataFolder.toAbsolutePath().normalize();
                    }
                }
            }
        }

        throw new IllegalStateException("ไม่พบโฟลเดอร์ data ภายในโปรเจกต์ KU_GoodBook "
                + "กรุณาตรวจสอบ E-book_System-main/KU_GoodBook/data");
    }
}
