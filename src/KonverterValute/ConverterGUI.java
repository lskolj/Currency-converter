package KonverterValute;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class ConverterGUI extends JFrame {

    private final Konvertiranje konvertiranje = new Konvertiranje();

    private JComboBox<Valuta> valutaCombo;
    private JTextField iznosPolje, rezultatPolje;
    private JLabel     tecajLabel;
    private JButton    osvjeziGumb;
    private boolean    eurUStranu = true;

    public ConverterGUI() {
        setTitle("HNB Tecajna Lista");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        izgradniGUI();
        ucitajTecajeve();
    }

    private void izgradniGUI() {
        JPanel glavni = new JPanel(new BorderLayout(10, 10));
        glavni.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        glavni.setBackground(Color.WHITE);

        JLabel naslov = new JLabel("HNB Tecajni Konverter", SwingConstants.CENTER);
        naslov.setFont(new Font("SansSerif", Font.BOLD, 18));
        naslov.setForeground(Color.BLUE);
        naslov.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        glavni.add(naslov, BorderLayout.NORTH);

        // --- Srednji panel ---
        JPanel srednji = new JPanel(new GridBagLayout());
        srednji.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill   = GridBagConstraints.HORIZONTAL;

        valutaCombo = new JComboBox<>();
        valutaCombo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        valutaCombo.addActionListener(e -> prikaziTecaj());

        tecajLabel = new JLabel("Tecaj: -");
        tecajLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        tecajLabel.setForeground(new Color(100, 100, 100));

        JToggleButton smjerGumb = new JToggleButton("EUR  →  Strana valuta");
        stilizacijaGumba(smjerGumb, true);
        smjerGumb.addActionListener(e -> {
            eurUStranu = !smjerGumb.isSelected();
            smjerGumb.setText(eurUStranu ? "EUR  →  Strana valuta" : "Strana valuta  →  EUR");
            prikaziTecaj();
        });

        iznosPolje = new JTextField("1.00");
        iznosPolje.setFont(new Font("Monospaced", Font.PLAIN, 14));
        iznosPolje.setHorizontalAlignment(JTextField.RIGHT);
        iznosPolje.addActionListener(e -> pretvori());

        rezultatPolje = new JTextField();
        rezultatPolje.setFont(new Font("Monospaced", Font.BOLD, 14));
        rezultatPolje.setHorizontalAlignment(JTextField.RIGHT);
        rezultatPolje.setEditable(false);
        rezultatPolje.setBackground(new Color(220, 235, 255));

        // Dodavanje u grid: [label, komponenta] po retku
        dodajRedak(srednji, g, 0, new JLabel("Valuta:"), valutaCombo);
        dodajSirokiRedak(srednji, g, 1, tecajLabel);
        dodajSirokiRedak(srednji, g, 2, smjerGumb);
        dodajRedak(srednji, g, 3, new JLabel("Iznos:"),    iznosPolje);
        dodajRedak(srednji, g, 4, new JLabel("Rezultat:"), rezultatPolje);

        glavni.add(srednji, BorderLayout.CENTER);

        // --- Donji panel ---
        JButton pretvoriGumb = new JButton("Pretvori");
        stilizacijaGumba(pretvoriGumb, true);
        pretvoriGumb.setPreferredSize(new Dimension(110, 36));
        pretvoriGumb.addActionListener(e -> pretvori());

        osvjeziGumb = new JButton("Osvježi");
        osvjeziGumb.setPreferredSize(new Dimension(110, 36));
        osvjeziGumb.addActionListener(e -> ucitajTecajeve());

        JPanel donji = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        donji.setBackground(Color.WHITE);
        donji.add(pretvoriGumb);
        donji.add(osvjeziGumb);
        glavni.add(donji, BorderLayout.SOUTH);

        add(glavni);
    }

    // --- Pomoćne metode za grid ---

    private void dodajRedak(JPanel p, GridBagConstraints g, int y, JComponent lijevo, JComponent desno) {
        g.gridwidth = 1;
        g.gridx = 0; g.gridy = y; g.weightx = 0; p.add(lijevo, g);
        g.gridx = 1;              g.weightx = 1; p.add(desno,  g);
    }

    private void dodajSirokiRedak(JPanel p, GridBagConstraints g, int y, JComponent c) {
        g.gridx = 0; g.gridy = y; g.gridwidth = 2; g.weightx = 1;
        p.add(c, g);
        g.gridwidth = 1;
    }

    private void stilizacijaGumba(AbstractButton b, boolean pun) {
        b.setFont(new Font("SansSerif", pun ? Font.BOLD : Font.PLAIN, pun ? 12 : 13));
        if (pun) { b.setBackground(Color.BLUE); b.setForeground(Color.WHITE); }
        b.setFocusPainted(false);
    }

    // --- Logika ---

    private void ucitajTecajeve() {
        osvjeziGumb.setEnabled(false);
        osvjeziGumb.setText("Ucitavam...");

        iznosPolje.setText("");
        rezultatPolje.setText("");

        valutaCombo.removeAllItems();
        valutaCombo.addItem(new Valuta("...", "Ucitavam tecajeve", 0));

        new SwingWorker<List<Valuta>, Void>() {
            @Override protected List<Valuta> doInBackground() throws Exception {
                return konvertiranje.dohvatiTecajeve();
            }
            @Override protected void done() {
                osvjeziGumb.setEnabled(true);
                osvjeziGumb.setText("Osvježi");
                valutaCombo.removeAllItems();
                try {
                    get().forEach(valutaCombo::addItem);
                    prikaziTecaj();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ConverterGUI.this,
                            "Greška pri dohvatu tecajeve:\n" + ex.getCause().getMessage(),
                            "Mrežna greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void prikaziTecaj() {
        Valuta v = (Valuta) valutaCombo.getSelectedItem();
        if (v == null || v.getTecaj() == 0) { tecajLabel.setText("Tecaj: -"); return; }
        tecajLabel.setText(eurUStranu
                ? String.format("Tecaj: 1 EUR = %.4f %s", v.getTecaj(), v.getValuta())
                : String.format("Tecaj: 1 %s = %.6f EUR", v.getValuta(), 1.0 / v.getTecaj()));
    }

    private void pretvori() {
        Valuta v = (Valuta) valutaCombo.getSelectedItem();
        if (v == null || v.getTecaj() == 0) {
            JOptionPane.showMessageDialog(this, "Odaberite valutu.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double iznos    = Double.parseDouble(iznosPolje.getText().replace(",", ".").trim());
            double rezultat = eurUStranu ? iznos * v.getTecaj() : iznos / v.getTecaj();
            String oznaka   = eurUStranu ? v.getValuta() : "EUR";
            rezultatPolje.setText(new DecimalFormat("#,##0.####").format(rezultat) + " " + oznaka);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Unesite ispravan broj.", "Greška unosa", JOptionPane.ERROR_MESSAGE);
        }
    }
}