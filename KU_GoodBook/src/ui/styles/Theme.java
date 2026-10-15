package ui.styles;

// ไกด์ไฟล์: หน้าที่: รวมค่าสีของพื้นหลัง แผง การ์ด ตัวอักษร และสีเน้น
// เมธอด bg/sidebar/card/text/accent คืนสีที่ส่วน UI เรียกใช้
// เปลี่ยนค่าสีในคลาสนี้เพื่อปรับธีมทั้งแอป โดยไม่ต้องไล่แก้ทุกหน้าทีละไฟล์

import java.awt.*;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import ui.components.PillButton;

/**
 * รวมสีที่ใช้ในแอปไว้ที่เดียว เพื่อให้หน้าจอต่าง ๆ ใช้สีชุดเดียวกัน
 *
 * <p>ปุ่มสลับโหมดเปลี่ยนสีบนหน้าต่างเดิม จึงไม่ทำให้ข้อมูลฟอร์มหรือตำแหน่งอ่านหาย
 * หน้าที่เปิดภายหลังใช้ค่าสีจากโหมดเดียวกันระหว่างเปิดโปรแกรม</p>
 */
public class Theme {

    /** false ใช้สีสว่าง ส่วน true เลือกชุดสีสำหรับโหมดกลางคืน */
    public static boolean dark = false;

    private static final String TOGGLE_BUTTON = "kuGoodbook.themeToggle";

    /** คลาสนี้เก็บค่าสีและเมธอด static จึงไม่ต้องสร้าง Theme */
    private Theme() {
    }

    /** สร้างปุ่มร่วมสำหรับหน้าที่รองรับการสลับโหมด */
    public static JButton createToggleButton() {
        PillButton button = new PillButton(dark ? "โหมดกลางวัน" : "โหมดกลางคืน", PillButton.OUTLINE);
        button.putClientProperty(TOGGLE_BUTTON, Boolean.TRUE);
        button.setFocusable(false);
        button.addActionListener(event -> toggle(button));
        return button;
    }

    /** เปลี่ยนธีมในหน้าต่างเดิม โดยไม่สร้างหน้าใหม่หรือแก้ข้อมูลที่ผู้ใช้กรอก */
    public static void toggle(Component source) {
        Color[] previous = palette();
        dark = !dark;
        Color[] next = palette();
        Map<JViewport, Point> positions = new IdentityHashMap<>();
        boolean updated = false;
        for (Window window : Window.getWindows()) {
            if (window.isDisplayable()) {
                rememberPositions(window, positions);
                apply(window, previous, next);
                window.repaint();
                updated = true;
            }
        }
        if (!updated && source != null) {
            rememberPositions(source, positions);
            apply(source, previous, next);
            source.repaint();
        }
        // การปรับสีข้อความในเอกสารไม่ควรทำให้ caret ดึงหน้าจอไปตำแหน่งอื่น
        positions.forEach(JViewport::setViewPosition);
        SwingUtilities.invokeLater(() -> positions.forEach(JViewport::setViewPosition));
    }

    private static Color[] palette() {
        return new Color[]{bg(), sidebar(), card(), tile(), line(), text(), muted(),
                accent(), accentFill(), gold(), readBg()};
    }

    private static Color replace(Color color, Color[] previous, Color[] next) {
        if (color != null) {
            for (int i = 0; i < previous.length; i++) {
                if (color.equals(previous[i])) {
                    return next[i];
                }
            }
        }
        return color;
    }

    private static void rememberPositions(Component component, Map<JViewport, Point> positions) {
        if (component instanceof JViewport) {
            positions.put((JViewport) component, ((JViewport) component).getViewPosition());
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                rememberPositions(child, positions);
            }
        }
    }

    private static void apply(Component component, Color[] previous, Color[] next) {
        component.setForeground(replace(component.getForeground(), previous, next));
        // สีแดงของตัวอักษรกับพื้นปุ่มเหมือนกันในโหมดสว่าง แต่ใช้คนละสีในโหมดมืด
        Color background = component.getBackground();
        component.setBackground(previous[8].equals(background) ? next[8]
                : replace(background, previous, next));
        if (component instanceof JComponent) {
            JComponent view = (JComponent) component;
            Border border = view.getBorder();
            Border changed = replaceBorder(border, previous, next);
            if (changed != border) {
                view.setBorder(changed);
            }
            if (Boolean.TRUE.equals(view.getClientProperty(TOGGLE_BUTTON))) {
                ((AbstractButton) view).setText(dark ? "โหมดกลางวัน" : "โหมดกลางคืน");
            }
        }
        if (component instanceof JTextComponent) {
            JTextComponent text = (JTextComponent) component;
            text.setCaretColor(replace(text.getCaretColor(), previous, next));
            text.setSelectionColor(replace(text.getSelectionColor(), previous, next));
            text.setSelectedTextColor(replace(text.getSelectedTextColor(), previous, next));
            if (text instanceof JTextPane && !text.isEditable()) {
                StyledDocument document = ((JTextPane) text).getStyledDocument();
                DefaultCaret caret = text.getCaret() instanceof DefaultCaret
                        ? (DefaultCaret) text.getCaret() : null;
                int policy = caret == null ? 0 : caret.getUpdatePolicy();
                if (caret != null) caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
                try {
                    for (int pos = 0; pos < document.getLength();) {
                        Element run = document.getCharacterElement(pos);
                        int end = Math.min(run.getEndOffset(), document.getLength());
                        Color color = StyleConstants.getForeground(run.getAttributes());
                        Color changed = replace(color, previous, next);
                        if (!color.equals(changed)) {
                            SimpleAttributeSet style = new SimpleAttributeSet();
                            StyleConstants.setForeground(style, changed);
                            document.setCharacterAttributes(pos, end - pos, style, false);
                        }
                        pos = end;
                    }
                } finally {
                    if (caret != null) caret.setUpdatePolicy(policy);
                }
            }
        }
        if (component instanceof JList) {
            JList<?> list = (JList<?>) component;
            list.setSelectionBackground(replace(list.getSelectionBackground(), previous, next));
            list.setSelectionForeground(replace(list.getSelectionForeground(), previous, next));
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                apply(child, previous, next);
            }
        }
    }

    private static Border replaceBorder(Border border, Color[] previous, Color[] next) {
        if (border instanceof CompoundBorder) {
            CompoundBorder compound = (CompoundBorder) border;
            return BorderFactory.createCompoundBorder(
                    replaceBorder(compound.getOutsideBorder(), previous, next),
                    replaceBorder(compound.getInsideBorder(), previous, next));
        }
        if (border instanceof LineBorder) {
            LineBorder line = (LineBorder) border;
            return new LineBorder(replace(line.getLineColor(), previous, next),
                    line.getThickness(), line.getRoundedCorners());
        }
        if (border instanceof MatteBorder && ((MatteBorder) border).getMatteColor() != null) {
            MatteBorder matte = (MatteBorder) border;
            return new MatteBorder(matte.getBorderInsets(),
                    replace(matte.getMatteColor(), previous, next));
        }
        return border;
    }

    // คืนค่าสีสำหรับโหมดปัจจุบัน โดยแปลงตัวเลขสีเป็นชนิด Color ของ Java
    private static Color pick(int light, int night) {
        return new Color(dark ? night : light);
    }

    /**
     * เลือกสีพื้นหลังตามโหมด
     *
     * @return สีพื้นหลัง
     */
    public static Color bg() {
        return pick(0xEEE3CD, 0x1C1711);
    }

    /**
     * เลือกสีพื้นแถบเมนูตามโหมด
     *
     * @return สีแถบเมนู
     */
    public static Color sidebar() {
        return pick(0xE5D6BA, 0x241D15);
    }

    /**
     * เลือกสีพื้นการ์ดตามโหมด
     *
     * @return สีพื้นการ์ด
     */
    public static Color card() {
        return pick(0xFBF6EA, 0x2C241B);
    }

    /**
     * เลือกสีพื้นช่องกรอกและช่องรายการตามโหมด
     *
     * @return สีพื้นช่อง
     */
    public static Color tile() {
        return pick(0xF2E8D2, 0x372E23);
    }

    /**
     * เลือกสีเส้นขอบตามโหมด
     *
     * @return สีเส้นขอบ
     */
    public static Color line() {
        return pick(0xDFD0B2, 0x41372A);
    }

    /**
     * เลือกสีข้อความหลักตามโหมด
     *
     * @return สีข้อความ
     */
    public static Color text() {
        return pick(0x2B1D14, 0xEFE4D0);
    }

    /**
     * เลือกสีข้อความรองตามโหมด
     *
     * @return สีข้อความรอง
     */
    public static Color muted() {
        return pick(0x8A7A66, 0xA89A86);
    }

    /**
     * เลือกสีเน้นสำหรับข้อความและเส้นตามโหมด
     *
     * @return สีเน้น
     */
    public static Color accent() {
        return pick(0x8B1E24, 0xD2676C);
    }

    /**
     * เลือกสีพื้นปุ่มตามโหมด
     *
     * @return สีปุ่ม
     */
    public static Color accentFill() {
        return pick(0x8B1E24, 0xA8343A);
    }

    /**
     * เลือกสีทองสำหรับลวดลายตามโหมด
     *
     * @return สีทอง
     */
    public static Color gold() {
        return pick(0xB8893B, 0xD9B26F);
    }

    /**
     * เลือกสีพื้นของตอนที่อ่านแล้วตามโหมด
     *
     * @return สีพื้นตอนที่อ่านแล้ว
     */
    public static Color readBg() {
        return pick(0xF0D9D3, 0x4A2A2C);
    }
}
