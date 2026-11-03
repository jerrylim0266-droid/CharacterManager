package com.unpri.charactermanager.view;

import com.unpri.charactermanager.model.Character;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame {

    private JPanel mainPanel;
    private final ArrayList<Character> characterList;

    public MainWindow(ArrayList<Character> characterList) {
        super("Character Manager v0.4");
        this.characterList = characterList;

        initComponents();

        pack();
        setMinimumSize(new Dimension(420, 300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        mainPanel = new JPanel(new GridLayout(0, 1, 0, 8));
        mainPanel.setBackground(AppTheme.BG_MAIN);
        mainPanel.setBorder(AppTheme.PANEL_BORDER);

        mainPanel.add(createTitleLabel());

        for (Character c : characterList) {
            mainPanel.add(createCharacterLabel(c));
        }

        add(mainPanel);
    }

    /** Membuat judul layar. */
    private JLabel createTitleLabel() {
        JLabel label = new JLabel(
                "Daftar Karakter (" + characterList.size() + ")",
                SwingConstants.CENTER
        );
        label.setFont(AppTheme.FONT_TITLE);
        label.setForeground(AppTheme.TEXT_MAIN);
        return label;
    }

    /** Membuat satu kartu karakter. */
    private JLabel createCharacterLabel(Character character) {
        String info = character.getName() + " | " + character.getType()
                + " | Lv." + character.getLevel()
                + " | HP:" + character.getHitPoints()
                + " | ATK:" + character.getAttackPower();

        JLabel label = new JLabel(info);
        boolean highLevel = character.getLevel() >= AppTheme.LEVEL_HIGHLIGHT_THRESHOLD;
        label.setFont(highLevel ? AppTheme.FONT_LEVEL_HIGH : AppTheme.FONT_BODY);
        label.setForeground(highLevel ? AppTheme.GOLD : AppTheme.TEXT_LIGHT);
        label.setBackground(AppTheme.colorFor(character.getType()));
        label.setOpaque(true);
        label.setBorder(AppTheme.cardBorderFor(character.getType()));
        label.setToolTipText(AppTheme.describe(character.getType())
                + " | HP: " + character.getHitPoints()
                + " | ATK: " + character.getAttackPower());
        return label;
    }
}
