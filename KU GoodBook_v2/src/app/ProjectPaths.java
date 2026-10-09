package app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** หาตำแหน่งโฟลเดอร์โปรเจ็กต์ เพื่อให้ทุกส่วนบันทึก CSV ไว้ที่เดียวกัน */
public final class ProjectPaths {

    private ProjectPaths() {
        // คลาสนี้มีแต่เมธอดสำหรับหาตำแหน่ง จึงไม่ต้องสร้าง object
    }

    /** คืนตำแหน่ง data ของโปรเจ็กต์ โดยไล่จากโฟลเดอร์ที่ใช้เปิดโปรแกรมขึ้นไป */
    public static Path dataFolder() {
        Path startFolder = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        Path projectFolder = findProjectFolder(startFolder);

        if (projectFolder != null) {
            return projectFolder.resolve("data");
        }

        // ถ้าหาโปรเจ็กต์ไม่เจอ ให้คงวิธีเดิมและใช้ตำแหน่งที่เปิดโปรแกรม
        return startFolder.resolve("data");
    }

    /** หาโฟลเดอร์โปรเจ็กต์ ทั้งโฟลเดอร์ปัจจุบัน โฟลเดอร์แม่ และโฟลเดอร์ลูก */
    private static Path findProjectFolder(Path startFolder) {
        Path folder = startFolder;

        while (folder != null) {
            if (isProjectFolder(folder)) {
                return folder;
            }

            // รองรับกรณีเปิดโปรแกรมจากโฟลเดอร์แม่ที่ครอบโปรเจ็กต์ไว้อีกชั้น
            java.io.File[] children = folder.toFile().listFiles();
            if (children != null) {
                for (java.io.File child : children) {
                    if (child.isDirectory() && isProjectFolder(child.toPath())) {
                        return child.toPath();
                    }
                }
            }

            folder = folder.getParent();
        }

        return null;
    }

    /** โฟลเดอร์โปรเจ็กต์มีทั้ง src และ README.md */
    private static boolean isProjectFolder(Path folder) {
        return Files.isDirectory(folder.resolve("src"))
                && Files.exists(folder.resolve("README.md"));
    }
}
