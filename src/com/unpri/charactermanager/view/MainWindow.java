package com.unpri.charactermanager.view;

import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterManager;
import com.unpri.charactermanager.model.CharacterType;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainWindow extends JFrame {

    private final CharacterManager manager;   // v0.6: menerima MANAGER, bukan salinan list
    private JPanel cardsPanel;                // field: isinya diganti setiap refresh
    private JLabel countLabel;                // field: jumlah di header
    private JLabel statusLabel;               // field: teks di bar status

    public MainWindow(CharacterManager manager) {
        super("Character Manager v0.6");
        this.manager = manager;

        initComponents();

        pack();
        setMinimumSize(new Dimension(820, 420));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        getContentPane().setBackground(AppTheme.BG_MAIN);
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(new CharacterFormPanel(manager, this::refreshList), BorderLayout.WEST); // form baru
        add(createListArea(),    BorderLayout.CENTER);
        add(createLegendPanel(), BorderLayout.EAST);
        add(createStatusPanel(), BorderLayout.SOUTH);

        refreshList();                        // isi daftar untuk pertama kali
    }

    /** Membangun ulang kartu dari data Model dan memperbarui semua penghitung. */
    private void refreshList() {
        cardsPanel.removeAll();                                   // buang kartu lama
        for (Character c : manager.getAllCharacters()) {
            cardsPanel.add(createCharacterLabel(c));              // buat kartu dari data terbaru
        }
        cardsPanel.revalidate();                                  // layout dihitung ulang
        cardsPanel.repaint();                                     // gambar ulang

        int total = manager.getTotalCharacters();
        countLabel.setText(total + " karakter");
        statusLabel.setText("Total: " + total + " karakter  |  Siap");
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.BG_MAIN);
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JLabel title = new JLabel("Character Manager");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_MAIN);

        countLabel = new JLabel(" ");                             // teks diisi oleh refreshList()
        countLabel.setFont(AppTheme.FONT_BODY);
        countLabel.setForeground(AppTheme.GOLD);

        header.add(title, BorderLayout.WEST);
        header.add(countLabel, BorderLayout.EAST);
        return header;
    }

    private JScrollPane createListArea() {
        cardsPanel = new JPanel(new GridLayout(0, 1, 0, 8));      // sekarang FIELD, bukan variabel lokal
        cardsPanel.setBackground(AppTheme.BG_MAIN);

        JPanel holder = new JPanel(new BorderLayout());
        holder.setBackground(AppTheme.BG_MAIN);
        holder.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        holder.add(cardsPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(holder);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppTheme.BG_MAIN);
        scroll.setPreferredSize(new Dimension(420, 280));
        return scroll;
    }

    /** Satu kartu karakter (sama seperti v0.4). */
    private JLabel createCharacterLabel(Character c) {
        String info = c.getName() + " | " + c.getType()
                    + " | Lv." + c.getLevel()
                    + " | HP:" + c.getHitPoints()
                    + " | ATK:" + c.getAttackPower();

        JLabel lbl = new JLabel(info);
        boolean highLevel = c.getLevel() >= AppTheme.LEVEL_HIGHLIGHT_THRESHOLD;
        lbl.setFont(highLevel ? AppTheme.FONT_LEVEL_HIGH : AppTheme.FONT_BODY);
        lbl.setForeground(highLevel ? AppTheme.GOLD : AppTheme.TEXT_LIGHT);
        lbl.setBackground(AppTheme.colorFor(c.getType()));
        lbl.setOpaque(true);                                 // wajib agar background tampak
        lbl.setBorder(AppTheme.cardBorderFor(c.getType()));
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

    private JPanel createStatusPanel() {
        JPanel status = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        status.setBackground(AppTheme.BG_STATUS);

        statusLabel = new JLabel(" ");                            // diisi oleh refreshList()
        statusLabel.setFont(AppTheme.FONT_MONO);
        statusLabel.setForeground(AppTheme.TEXT_MUTED);
        status.add(statusLabel);
        return status;
    }
}