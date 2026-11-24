package com.unpri.charactermanager.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterManager;
import com.unpri.charactermanager.model.CharacterType;

/** Form "Tambah Karakter": membaca input, memvalidasi, lalu menyerahkan ke Model. */
public class CharacterFormPanel extends JPanel {

    private final CharacterManager manager;   // tujuan data yang lolos validasi
    private final Runnable onAdded;           // dipanggil setelah sukses (untuk menyegarkan daftar)

    // Komponen input disimpan sebagai FIELD karena dibaca di method lain
    private final JTextField nameField = new JTextField(14);
    private final JComboBox<CharacterType> roleBox = new JComboBox<>(CharacterType.values());
    private final JSpinner levelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
    private final JLabel messageLabel = new JLabel(" ");   // " " (spasi) agar tinggi label stabil

    private final Border nameBorder;          // border asli, disimpan untuk dipulihkan
    private final Border levelBorder;

    public CharacterFormPanel(CharacterManager manager, Runnable onAdded) {
        this.manager = manager;
        this.onAdded = onAdded;
        this.nameBorder = nameField.getBorder();        // simpan SEBELUM pernah diganti
        this.levelBorder = levelSpinner.getBorder();

        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 8));
        setPreferredSize(new Dimension(250, 0));        // lebar 250 px; tinggi mengikuti wilayah WEST

        JLabel title = new JLabel("Tambah Karakter");
        title.setFont(AppTheme.FONT_BODY.deriveFont(java.awt.Font.BOLD, 16f));
        title.setForeground(AppTheme.TEXT_MAIN);

        messageLabel.setFont(AppTheme.FONT_BODY);

        JButton saveButton = new JButton("Simpan");
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> onSave());    // alat bantu minimal; dibahas penuh di Pertemuan 9
        nameField.addActionListener(e -> onSave());     // tekan Enter di field nama = Simpan

        JPanel rows = new JPanel(new GridLayout(0, 1, 0, 4));  // satu kolom, jarak 4 px
        rows.setOpaque(false);                                 // transparan: warna panel induk tampak
        rows.add(title);
        rows.add(makeLabel("Nama:", nameField));
        rows.add(nameField);
        rows.add(makeLabel("Role:", roleBox));
        rows.add(roleBox);
        rows.add(makeLabel("Level:", levelSpinner));
        rows.add(levelSpinner);
        rows.add(messageLabel);
        rows.add(saveButton);
        add(rows, BorderLayout.NORTH);                         // NORTH: tinggi alami, tidak melar
    }

    private JLabel makeLabel(String text, JComponent target) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.FONT_BODY);
        label.setForeground(AppTheme.TEXT_MAIN);
        label.setLabelFor(target);                             // mengaitkan label dengan komponen inputnya
        return label;
    }

    /** Dipanggil saat Simpan ditekan: baca -> bersihkan -> validasi -> serahkan ke Model. */
    private void onSave() {
        clearMessage();                                        // hapus sisa error sebelumnya

        String name = nameField.getText().trim();              // 1-2. baca + bersihkan
        String problem = validateName(name);                   // 4. validasi bentuk
        if (problem != null) {
            showError(problem, nameField);                     // 5a. gagal: pesan + tandai field
            nameField.requestFocusInWindow();                  //      kursor kembali ke field bermasalah
            return;                                            //      HENTIKAN: jangan lanjut ke Model
        }

        try {
            levelSpinner.commitEdit();                         // pastikan teks yang diketik sudah dikomit
        } catch (ParseException ex) {
            showError("Level harus berupa angka", levelSpinner);
            levelSpinner.requestFocusInWindow();
            return;
        }

        CharacterType role = (CharacterType) roleBox.getSelectedItem(); // cast Object -> enum
        int level = (Integer) levelSpinner.getValue();                  // cast Object -> Integer

        try {
            if (!manager.addCharacter(new Character(name, role, level))) {
                showError("Nama sudah dipakai", nameField);
                nameField.requestFocusInWindow();
                return;
            }
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), levelSpinner);          // tampilkan pesan dari Model apa adanya
            levelSpinner.requestFocusInWindow();
            return;
        }

        nameField.setText("");                                 // 5b. sukses: kosongkan form
        roleBox.setSelectedIndex(0);
        levelSpinner.setValue(1);
        showSuccess("Tersimpan: " + name);
        nameField.requestFocusInWindow();
        onAdded.run();                                         // minta MainWindow menyegarkan daftar
    }

    /** Mengembalikan pesan error pertama, atau null bila nama lolos. Hanya MEMERIKSA, tidak menampilkan. */
    private String validateName(String name) {
        if (name.isEmpty())                 return "Nama wajib diisi";               // kelengkapan
        if (name.length() < 3 || name.length() > 20)
                                            return "Nama harus 3-20 karakter";       // panjang
        if (!name.matches("[A-Za-z ]+"))    return "Nama hanya huruf dan spasi";     // format
        if (name.contains("  "))            return "Nama tidak boleh memiliki spasi berurutan";
        if (manager.isNameTaken(name))      return "Nama sudah dipakai";             // keunikan (Model)
        return null;
    }

    private void showError(String text, JComponent source) {
        messageLabel.setText(text);
        messageLabel.setForeground(AppTheme.ERROR);
        source.setBorder(BorderFactory.createLineBorder(AppTheme.ERROR, 2));  // tanda visual selain warna teks
    }

    private void showSuccess(String text) {
        messageLabel.setText(text);
        messageLabel.setForeground(AppTheme.SUCCESS);
    }

    private void clearMessage() {
        messageLabel.setText(" ");
        nameField.setBorder(nameBorder);                       // pulihkan border asli
        levelSpinner.setBorder(levelBorder);
    }
}