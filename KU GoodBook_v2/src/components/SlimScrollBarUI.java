package components;

import theme.Theme;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * วาดแถบเลื่อนให้บางและใช้สีของแอป
 *
 * <p>ใช้กับ JScrollPane เพื่อให้แถบเลื่อนกลมกลืนกับหน้าจอ
 * ปุ่มลูกศรด้านบนและด้านล่างถูกซ่อนไว้ เหลือรางกับตัวเลื่อนเท่านั้น</p>
 */
public class SlimScrollBarUI extends BasicScrollBarUI {

    /** ใช้การตั้งค่าของคลาสนี้ตอน JScrollPane สร้างแถบเลื่อน */
    public SlimScrollBarUI() {
    }

    @Override
    protected void configureScrollBarColors() {
        thumbColor = Theme.muted();
        trackColor = Theme.bg();
    }

    // ส่งปุ่มว่างกลับไปแทนปุ่มลูกศร เพื่อไม่ให้กินพื้นที่แถบเลื่อน
    @Override protected JButton createDecreaseButton(int orientation) { return emptyButton(); }
    @Override protected JButton createIncreaseButton(int orientation) { return emptyButton(); }

    private JButton emptyButton() {
        // กำหนดขนาดเป็นศูนย์เพื่อซ่อนปุ่มไว้ แต่ยังคงรูปแบบการทำงานของ Swing
        JButton b = new JButton();
        b.setPreferredSize(new Dimension(0, 0));
        b.setMinimumSize(new Dimension(0, 0));
        b.setMaximumSize(new Dimension(0, 0));
        return b;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        // วาดพื้นรางด้วยสีพื้นหลังของแอป
        g.setColor(Theme.bg());
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        // วาดตัวเลื่อนแบบมนและโปร่งเล็กน้อย เพื่อให้เห็นรางด้านหลัง
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color m = Theme.muted();
        g2.setColor(new Color(m.getRed(), m.getGreen(), m.getBlue(), 120));
        g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
        g2.dispose();
    }
}
