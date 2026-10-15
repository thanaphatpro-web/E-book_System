package ui.components;

// แสดงรูปปกจริงโดยรักษาสัดส่วน ถ้าไม่มีรูปที่ใช้งานได้จะแสดงเพียงกรอบว่าง
// อ่านข้อมูลไฟล์ใหม่เมื่อสร้างส่วนแสดงผล เพื่อไม่ให้ติดภาพเก่าจากแคชตามชื่อไฟล์

import app.ProjectPaths;
import books.Book;
import ui.styles.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** แสดงรูปปกหนังสือจากไฟล์ หรือกรอบว่างสำหรับหนังสือเก่าที่ยังไม่มีรูป */
public class BookCover extends JComponent {

    private final ImageIcon coverImage;

    /**
     * @param book หนังสือที่จะแสดง
     * @param width ความกว้างพื้นที่ปก
     * @param height ความสูงพื้นที่ปก
     */
    public BookCover(Book book, int width, int height) {
        ImageIcon image = null;
        if (book.coverPath != null && !book.coverPath.isEmpty()) {
            Path imagePath = ProjectPaths.dataFolder().resolve(book.coverPath);
            try {
                if (Files.isRegularFile(imagePath)) {
                    // การโหลดจาก bytes ไม่ใช้แคชรูปตามที่อยู่ไฟล์ของ ImageIcon
                    ImageIcon candidate = new ImageIcon(Files.readAllBytes(imagePath));
                    if (candidate.getIconWidth() > 0 && candidate.getIconHeight() > 0) {
                        image = candidate;
                    }
                }
            } catch (IOException error) {
                // หนังสือเดิมที่รูปหายหรืออ่านไม่ได้ยังเปิดดูได้ โดยแสดงกรอบว่าง
            }
        }
        coverImage = image;
        if (image == null) {
            setToolTipText("ยังไม่มีรูปปกที่ใช้งานได้ กรุณาเพิ่มรูปผ่านหน้าแอดมิน");
        }
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth() - 8;
        int h = getHeight() - 8;
        if (w <= 0 || h <= 0) {
            g2.dispose();
            return;
        }
        Shape cover = new RoundRectangle2D.Float(0, 0, w, h, 10, 10);

        if (coverImage != null) {
            double scale = Math.min((double) w / coverImage.getIconWidth(),
                    (double) h / coverImage.getIconHeight());
            int imageWidth = Math.max(1, Math.min(w,
                    (int) Math.round(coverImage.getIconWidth() * scale)));
            int imageHeight = Math.max(1, Math.min(h,
                    (int) Math.round(coverImage.getIconHeight() * scale)));
            int x = (w - imageWidth) / 2;
            int y = (h - imageHeight) / 2;
            cover = new RoundRectangle2D.Float(x, y, imageWidth, imageHeight, 10, 10);
            Shape oldClip = g2.getClip();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.clip(cover);
            g2.drawImage(coverImage.getImage(), x, y, imageWidth, imageHeight, this);
            g2.setClip(oldClip);
        }

        g2.setColor(Theme.gold());
        g2.draw(cover);
        g2.dispose();
    }
}
