package com.unpri.charactermanager.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.EnumSet;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterType;

public class MainWindow extends JFrame {

    private final ArrayList<Character> characterList; // data dari Main.java (Model)

    public MainWindow(ArrayList<Character> characterList) {
        super("Character Manager v0.5");            // sprint #5
        this.characterList = characterList;

        initComponents();                           // 1. susun semua area

        pack();                                     // 2. ukuran awal = preferred size isi
        setMinimumSize(new Dimension(560, 380));    // 3. batas terkecil yang masih rapi
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);                // 4. pusatkan SETELAH pack()
    }

    private void initComponents() {
        getContentPane().setBackground(AppTheme.BG_MAIN);  // warna di balik semua panel
        setLayout(new BorderLayout());                     // kerangka 4 wilayah (JFrame meneruskan ke content pane)

        add(createHeaderPanel(), BorderLayout.NORTH);      // judul + jumlah
        add(createListArea(),    BorderLayout.CENTER);     // daftar berscroll: satu-satunya yang tumbuh
        add(createLegendPanel(), BorderLayout.EAST);       // legenda role
        add(createStatusPanel(), BorderLayout.SOUTH);      // bar status
    }

    /** NORTH: judul di kiri, jumlah karakter di kanan. */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.BG_MAIN);
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16)); // atas, kiri, bawah, kanan

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(AppTheme.BG_MAIN);
        JLabel title = new JLabel("Character Manager");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_MAIN);

        JLabel count = new JLabel(characterList.size() + " karakter");
        count.setFont(AppTheme.FONT_BODY);
        count.setForeground(AppTheme.GOLD);

        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(count, BorderLayout.EAST);
        header.add(titleRow, BorderLayout.NORTH);
        header.add(createStatisticsPanel(), BorderLayout.SOUTH);
        return header;
    }

    private JPanel createStatisticsPanel() {
        JPanel statistics = new JPanel(new GridLayout(2, 2, 8, 4));
        statistics.setBackground(AppTheme.BG_MAIN);
        statistics.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        int totalLevels = 0;
        int highestLevel = 0;
        EnumSet<CharacterType> types = EnumSet.noneOf(CharacterType.class);
        for (Character character : characterList) {
            totalLevels += character.getLevel();
            highestLevel = Math.max(highestLevel, character.getLevel());
            types.add(character.getType());
        }
        double averageLevel = characterList.isEmpty()
                ? 0
                : (double) totalLevels / characterList.size();

        statistics.add(createStatisticLabel("Karakter: " + characterList.size()));
        statistics.add(createStatisticLabel("Role: " + types.size()));
        statistics.add(createStatisticLabel(String.format(
                java.util.Locale.ROOT, "Rata-rata level: %.1f", averageLevel)));
        statistics.add(createStatisticLabel("Level tertinggi: " + highestLevel));
        return statistics;
    }

    private JLabel createStatisticLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.FONT_BODY);
        label.setForeground(AppTheme.TEXT_MUTED);
        return label;
    }

    /** CENTER: kartu karakter di dalam JScrollPane. */
    private JScrollPane createListArea() {
        JPanel cards = new JPanel(new GridLayout(0, 1, 0, 8)); // satu kolom, jarak 8 px
        cards.setBackground(AppTheme.BG_MAIN);
        for (Character c : characterList) {
            cards.add(createCharacterLabel(c));              // kartu ditambah ke PANEL, bukan ke scroll pane
        }

        // holder: menaruh cards di NORTH agar kartu mempertahankan tinggi alaminya
        JPanel holder = new JPanel(new BorderLayout());
        holder.setBackground(AppTheme.BG_MAIN);
        holder.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 8));
        holder.add(cards, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(holder);        // bungkus HOLDER
        scroll.setBorder(null);                              // buang border bawaan
        scroll.getViewport().setBackground(AppTheme.BG_MAIN);
        scroll.setPreferredSize(new Dimension(460, 280));    // tanpa ini, 50 karakter = jendela raksasa
        return scroll;
    }

    /** Satu kartu karakter (sama seperti v0.4). */
    private JLabel createCharacterLabel(Character c) {
        String info = c.getName() + " | " + c.getType()
                    + " | Lv." + c.getLevel()
                    + " | HP:" + c.getHitPoints()
                    + " | ATK:" + c.getAttackPower();

        JLabel lbl = new JLabel(info);
        lbl.setFont(AppTheme.FONT_BODY);
        lbl.setForeground(AppTheme.TEXT_LIGHT);
        lbl.setBackground(AppTheme.colorFor(c.getType()));
        lbl.setOpaque(true);                                 // wajib agar background tampak
        lbl.setBorder(AppTheme.CARD_BORDER);
        lbl.setToolTipText(AppTheme.describe(c.getType()));
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return lbl;
    }

    /** EAST: legenda warna per role, ditumpuk vertikal dengan BoxLayout. */
    private JPanel createLegendPanel() {
        JPanel legend = new JPanel();
        legend.setLayout(new BoxLayout(legend, BoxLayout.Y_AXIS)); // target = panel ini sendiri!
        legend.setBackground(AppTheme.BG_MAIN);
        legend.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 16));

        JLabel head = new JLabel("Legenda Role");
        head.setFont(AppTheme.FONT_BODY.deriveFont(java.awt.Font.BOLD)); // tebalkan tanpa tahu nama font
        head.setForeground(AppTheme.TEXT_MAIN);
        legend.add(head);
        legend.add(Box.createVerticalStrut(8));             // spasi tetap 8 px

        for (CharacterType type : CharacterType.values()) {  // satu chip per role
            JLabel chip = new JLabel(type.name());
            chip.setOpaque(true);
            chip.setBackground(AppTheme.colorFor(type));
            chip.setForeground(AppTheme.TEXT_LIGHT);
            chip.setFont(AppTheme.FONT_MONO);
            chip.setBorder(AppTheme.CARD_BORDER);
            chip.setToolTipText(AppTheme.describe(type));
            legend.add(chip);
            legend.add(Box.createVerticalStrut(6));
        }
        legend.add(Box.createVerticalGlue());               // dorong semua ke atas
        return legend;
    }

    /** SOUTH: status di kiri dan versi aplikasi di kanan. */
    private JPanel createStatusPanel() {
        JPanel status = new JPanel(new BorderLayout());
        status.setBackground(AppTheme.BG_STATUS);
        status.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

        JLabel lbl = new JLabel("Total: " + characterList.size() + " karakter  |  Siap");
        lbl.setFont(AppTheme.FONT_MONO);
        lbl.setForeground(AppTheme.TEXT_MUTED);
        JLabel version = new JLabel("v0.5");
        version.setFont(AppTheme.FONT_MONO);
        version.setForeground(AppTheme.TEXT_MUTED);
        status.add(lbl, BorderLayout.WEST);
        status.add(version, BorderLayout.EAST);
        return status;
    }
}