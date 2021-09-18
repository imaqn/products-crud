package Form;

import Konfigurasi.Koneksi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class form_barang extends JFrame{
    private JPanel daftarDataBarang;
    private JLabel namaBarangLabel;
    private JTextField jTextField_nama_barang;
    private JTextField jTextField_harga;
    private JTextField jTextField_kode_barang;
    private JTextField jTextField_satuan;
    private JTextField jTextField_stok;
    private JButton jButton_baru;
    private JButton jButton_simpan;
    private JButton jButton_edit;
    private JButton jButton_hapus;
    private JTable jTable_barang;

    private DefaultTableModel DftTblModel_barang;
    private String SQL;

    public form_barang(String title){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(daftarDataBarang);
        this.pack();
        this.TampilData();
        jButton_simpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = Koneksi.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(
                            "insert into barang(kode_barang, nama_barang, harga, satuan, stok) values(?,?,?,?,?)");
                    stmt.setString(1, jTextField_kode_barang.getText());
                    stmt.setString(2, jTextField_nama_barang.getText());
                    stmt.setString(3, jTextField_harga.getText());
                    stmt.setString(4, jTextField_satuan.getText());
                    stmt.setString(5, jTextField_stok.getText());
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null,
                            "Data berhasil disimpan",
                            "Pesan",
                            JOptionPane.INFORMATION_MESSAGE);
                    TampilData();
                } catch (SQLException err) {
                    System.out.println(err.getMessage());
                }
            }
        });
        jTable_barang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int baris = jTable_barang.getSelectedRow();
                jTextField_kode_barang.setText(DftTblModel_barang.getValueAt(baris, 0).toString());
                jTextField_nama_barang.setText(DftTblModel_barang.getValueAt(baris, 1).toString());
                jTextField_harga.setText(DftTblModel_barang.getValueAt(baris, 2).toString());
                jTextField_satuan.setText(DftTblModel_barang.getValueAt(baris, 3).toString());
                jTextField_stok.setText(DftTblModel_barang.getValueAt(baris, 4).toString());
            }
        });
        jButton_edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = Koneksi.getConnection();
                    PreparedStatement stmt = conn.prepareStatement("update barang set nama_barang=?, harga=?, satuan=?, stok=? where kode_barang=?");
                    stmt.setString(1, jTextField_nama_barang.getText());
                    stmt.setString(2, jTextField_harga.getText());
                    stmt.setString(3, jTextField_satuan.getText());
                    stmt.setString(4, jTextField_stok.getText());
                    stmt.setString(5, jTextField_kode_barang.getText());
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    TampilData();
                } catch (SQLException err) {
                    System.out.println(err.getMessage());
                }
            }
        });
        jButton_hapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection conn = Koneksi.getConnection();
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Apakah anda yakin ingin menghapus data tersebut?",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (confirm == 0) {
                    try {
                        java.sql.PreparedStatement stmt = conn.prepareStatement("delete from barang where kode_barang ='" +
                                jTextField_kode_barang.getText() + "'");
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null,
                                "Data berhasil dihapus",
                                "Pesan",
                                JOptionPane.INFORMATION_MESSAGE);
                        TampilData();
                        jTextField_kode_barang.setText("");
                        jTextField_nama_barang.setText("");
                        jTextField_harga.setText("");
                        jTextField_satuan.setText("");
                        jTextField_stok.setText("");
                        jTextField_kode_barang.requestFocus();
                    } catch (SQLException err) {
                        JOptionPane.showMessageDialog(null,
                                "Data gagal di hapus" + err.getMessage(),
                                "Pesan", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new form_barang("Daftar Data Barang");
        frame.setVisible(true);
    }

    private void TampilData() {
        DftTblModel_barang = new DefaultTableModel();
        DftTblModel_barang.addColumn("KODE BARANG");
        DftTblModel_barang.addColumn("NAMA BARANG");
        DftTblModel_barang.addColumn("HARGA");
        DftTblModel_barang.addColumn("SATUAN");
        DftTblModel_barang.addColumn("STOK");
        jTable_barang.setModel(DftTblModel_barang);
        Connection conn = Koneksi.getConnection();
        try {
            java.sql.Statement stmt = conn.createStatement();
            SQL = "select * from barang";
            java.sql.ResultSet res = stmt.executeQuery(SQL);
            while (res.next()) {
                DftTblModel_barang.addRow(new Object[]{
                        res.getString("kode_barang"),
                        res.getString("nama_barang"),
                        res.getString("harga"),
                        res.getString("satuan"),
                        res.getString("stok")
                });
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
