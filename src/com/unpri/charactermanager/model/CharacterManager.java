package com.unpri.charactermanager.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Mengelola koleksi Character: tambah, cari, hapus, dan statistik dasar.
 * Menerapkan Composition: CharacterManager "has-a" ArrayList<Character>.
 */
public class CharacterManager {

    private final ArrayList<Character> characterList;

    public CharacterManager() {
        this.characterList = new ArrayList<>();   // list kosong saat manager pertama dibuat
    }

    /**
     * Menambahkan karakter baru ke dalam list.
     * Menolak jika sudah ada karakter dengan nama yang sama (cek via equals()).
     * @return true jika berhasil ditambahkan, false jika ditolak karena duplikat
     */
    public boolean addCharacter(Character character) {
        if (character == null) {
            throw new IllegalArgumentException("Character tidak boleh null");
        }
        if (characterList.contains(character)) {   // contains menggunakan equals() untuk membandingkan nama
            System.out.println(" Gagal: karakter dengan nama '" + character.getName() + "' sudah ada!");
            return false;
        }
        characterList.add(character);
        return true;
    }

    /**
     * Mencari karakter berdasarkan nama.
     * @return object Character jika ditemukan, null jika tidak ada
     */
    public Character findByName(String name) {
        for (Character c : characterList) {         // iterasi setiap elemen di list
            if (Objects.equals(c.getName(), name)) { // bandingkan ISI nama, bukan ==
                return c;                             // langsung kembalikan referensi yang ditemukan
            }
        }
        return null;                                  // tidak ditemukan setelah seluruh list diperiksa
    }

    /**
     * Menghapus karakter dari list berdasarkan nama.
     * @return true jika berhasil dihapus, false jika tidak ditemukan
     */
    public boolean removeCharacter(String name) {
        Character target = findByName(name);
        if (target == null) {
            System.out.println("❌ Gagal hapus: karakter '" + name + "' tidak ditemukan.");
            return false;
        }
        characterList.remove(target);                 // ArrayList.remove(Object) memakai equals() di balik layar
        System.out.println("🗑️ Karakter '" + name + "' berhasil dihapus.");
        return true;
    }

    public int getTotalCharacters() {
        return characterList.size();
    }

    public double getAverageLevel() {
        if (characterList.isEmpty()) {
            return 0;
        }

        double totalLevel = 0;
        for (Character character : characterList) {
            totalLevel += character.getLevel();
        }
        return totalLevel / characterList.size();
    }

    public ArrayList<Character> findByType(CharacterType type) {
        ArrayList<Character> matchingCharacters = new ArrayList<>();
        for (Character character : characterList) {
            if (character.getType() == type) {
                matchingCharacters.add(character);
            }
        }
        return matchingCharacters;
    }

    /**
     * Menghitung jumlah karakter dengan tipe tertentu tanpa mengekspos list internal.
     */
    public int countByType(CharacterType type) {
        int count = 0;
        for (Character character : characterList) {
            if (character.getType() == type) {
                count++;
            }
        }
        return count;
    }

    /**
     * Mengganti nama karakter jika karakter ditemukan dan nama baru belum digunakan.
     *
     * @return true jika nama berhasil diganti atau tidak berubah, false jika validasi gagal
     */
    public boolean renameCharacter(String oldName, String newName) {
        Character target = findByName(oldName);
        if (target == null) {
            System.out.println(" Gagal: karakter '" + oldName + "' tidak ditemukan.");
            return false;
        }
        if (newName == null || newName.trim().isEmpty()) {
            System.out.println(" Gagal: nama baru tidak boleh kosong.");
            return false;
        }
        if (Objects.equals(oldName, newName)) {
            return true;
        }
        if (findByName(newName) != null) {
            System.out.println(" Gagal: karakter dengan nama '" + newName + "' sudah ada!");
            return false;
        }

        target.setName(newName);
        System.out.println(" Nama karakter berhasil diubah dari '" + oldName + "' menjadi '" + newName + "'.");
        return true;
    }

    /**
     * Mengembalikan SALINAN list, bukan referensi asli —
     * agar pemanggil tidak bisa mengubah data internal manager secara langsung.
     */
    public ArrayList<Character> getAllCharacters() {
        return new ArrayList<>(characterList);        // defensive copy — lihat Halaman 7 untuk eksperimen
    }
}