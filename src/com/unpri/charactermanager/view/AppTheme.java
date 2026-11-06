package com.unpri.charactermanager.view;

import com.unpri.charactermanager.model.CharacterType;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/** Pusat gaya tampilan Character Manager. */
public final class AppTheme {

    public static final int LEVEL_HIGHLIGHT_THRESHOLD = 5;

    public static final Color BG_MAIN = Color.decode("#13131f");
    public static final Color TEXT_MAIN = Color.decode("#dfe6e9");
    public static final Color TEXT_LIGHT = Color.WHITE;
    public static final Color GOLD = Color.decode("#fdcb6e");
    public static final Color WARRIOR = Color.decode("#d63031");
    public static final Color MAGE = Color.decode("#0984e3");
    public static final Color ARCHER = Color.decode("#00b894");
    public static final Color HEALER = Color.decode("#6c5ce7");

    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 22);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_MONO = new Font("Monospaced", Font.PLAIN, 13);
    public static final Font FONT_LEVEL_HIGH = FONT_BODY.deriveFont(Font.BOLD);

    public static final Border PANEL_BORDER =
            BorderFactory.createEmptyBorder(16, 16, 16, 16);
    public static final Border CARD_BORDER =
            BorderFactory.createEmptyBorder(8, 12, 8, 12);

    private AppTheme() {
    }

    public static Color colorFor(CharacterType type) {
        switch (type) {
            case WARRIOR:
                return WARRIOR;
            case MAGE:
                return MAGE;
            case ARCHER:
                return ARCHER;
            case HEALER:
                return HEALER;
            default:
                return Color.GRAY;
        }
    }

    public static Border cardBorderFor(CharacterType type) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorFor(type), 2),
                CARD_BORDER
        );
    }

    public static String describe(CharacterType type) {
        switch (type) {
            case WARRIOR:
                return "Warrior: jarak dekat, HP tinggi";
            case MAGE:
                return "Mage: serangan sihir, HP rendah";
            case ARCHER:
                return "Archer: jarak jauh, seimbang";
            case HEALER:
                return "Healer: memulihkan dan mendukung tim";
            default:
                return "Role belum terdaftar";
        }
    }
}
