
package examenlab1;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class SistemaRenta extends JFrame {

    private ArrayList<RentItem> items = new ArrayList<>();

    // ── Colors & Fonts ─────────────────────────────────────────────────────────
    private static final Color BG        = new Color(10, 10, 20);
    private static final Color PANEL_BG  = new Color(18, 18, 32);
    private static final Color CARD_BG   = new Color(26, 26, 46);
    private static final Color ACCENT    = new Color(255, 80, 80);
    private static final Color ACCENT2   = new Color(80, 160, 255);
    private static final Color TEXT      = new Color(230, 230, 245);
    private static final Color TEXT_DIM  = new Color(140, 140, 165);
    private static final Font  TITLE_F   = new Font("Georgia", Font.BOLD, 28);
    private static final Font  MENU_F    = new Font("Courier New", Font.BOLD, 15);
    private static final Font  BODY_F    = new Font("Courier New", Font.PLAIN, 13);

    public SistemaRenta() {
        setTitle("SISTEMA DE RENTA MULTIMEDIA");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildMenu(), BorderLayout.CENTER);
    }

    
  
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.setBorder(new MatteBorder(0, 0, 2, 0, ACCENT));

        JLabel title = new JLabel("MULTIMEDIA RENTAL SYSTEM", SwingConstants.CENTER);
        title.setFont(TITLE_F);
        title.setForeground(ACCENT);
        title.setBorder(BorderFactory.createEmptyBorder(22, 20, 18, 20));
        header.add(title, BorderLayout.CENTER);

        JLabel sub = new JLabel("Movies & Games  //  Powered by Java Swing", SwingConstants.CENTER);
        sub.setFont(BODY_F);
        sub.setForeground(TEXT_DIM);
        sub.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        header.add(sub, BorderLayout.SOUTH);
        return header;
    }


    private JPanel buildMenu() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG);

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setBackground(BG);
        grid.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        String[][] buttons = {
            {"A", "AGREGAR ÍTEM", "Añadir película o videojuego"},
            {"B", "RENTAR", "Calcular pago de renta"},
            {"C", "SUBMENÚ", "Opciones especiales de juego"},
            {"D", "IMPRIMIR TODO", "Ver catálogo completo"}
        };

        for (String[] b : buttons) grid.add(menuCard(b[0], b[1], b[2]));

        outer.add(grid);
        return outer;
    }

    private JButton menuCard(String letter, String label, String desc) {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? ACCENT.darker()
                         : getModel().isRollover() ? new Color(35, 35, 60) : CARD_BG;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(ACCENT);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);

                g2.setFont(new Font("Georgia", Font.BOLD, 42));
                g2.setColor(new Color(255, 80, 80, 40));
                g2.drawString(letter, 18, getHeight() - 18);

                g2.setFont(MENU_F);
                g2.setColor(TEXT);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(label, (getWidth() - fm.stringWidth(label)) / 2, getHeight()/2 - 5);

                g2.setFont(BODY_F);
                g2.setColor(TEXT_DIM);
                FontMetrics fm2 = g2.getFontMetrics();
                g2.drawString(desc, (getWidth() - fm2.stringWidth(desc)) / 2, getHeight()/2 + 18);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(300, 140));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            switch (letter) {
                case "A": accionAgregarItem(); break;
                case "B": accionRentar(); break;
                case "C": accionSubmenu(); break;
                case "D": accionImprimirTodo(); break;
            }
        });
        return btn;
    }

    // ── A. Agregar Ítem ─────────────────────────────────────────────────────────
    private void accionAgregarItem() {
        String[] tipos = {"Movie", "Game"};
        String tipo = (String) JOptionPane.showInputDialog(this,
            "¿Qué tipo de ítem desea agregar?", "Agregar Ítem",
            JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        if (tipo == null) return;

        String codigo = JOptionPane.showInputDialog(this, "Ingrese el código del ítem:");
        if (codigo == null || codigo.trim().isEmpty()) return;
        if (codigoExiste(codigo.trim())) {
            showMsg("El código '" + codigo + "' ya existe. Use un código único.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        ImageIcon img = cargarImagen();

        if (tipo.equals("Movie")) {
            double precio;
            try { precio = Double.parseDouble(JOptionPane.showInputDialog(this, "Precio base de renta (Lps):")); }
            catch (Exception ex) { showMsg("Precio inválido.", "Error", JOptionPane.ERROR_MESSAGE); return; }

            Movie m = new Movie(codigo.trim(), nombre.trim(), precio);
            m.setImagen(img);

            // Fecha de estreno
            try {
                String yearStr = JOptionPane.showInputDialog(this, "Año de estreno:");
                String mesStr  = JOptionPane.showInputDialog(this, "Mes de estreno (1-12):");
                String diaStr  = JOptionPane.showInputDialog(this, "Día de estreno:");
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(yearStr), Integer.parseInt(mesStr)-1, Integer.parseInt(diaStr));
                m.setFechaEstreno(cal);
            } catch (Exception ex) {}

            items.add(m);
            showMsg("Movie '" + m.getNombre() + "' agregada correctamente.\nEstado: " + m.getEstado(),
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } else {
            Game g = new Game(codigo.trim(), nombre.trim());
            g.setImagen(img);
            items.add(g);
            showMsg("Game '" + g.getNombre() + "' agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private ImageIcon cargarImagen() {
        int resp = JOptionPane.showConfirmDialog(this,
            "¿Desea cargar una imagen para este ítem?", "Imagen", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return null;

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar imagen");
        int r = fc.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
          
            Image scaled = icon.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        return null;
    }

    // ── B. Rentar ───────────────────────────────────────────────────────────────
    private void accionRentar() {
        String codigo = JOptionPane.showInputDialog(this, "Ingrese el código del ítem a rentar:");
        if (codigo == null) return;

        RentItem item = buscarItem(codigo.trim());
        if (item == null) { showMsg("Item No Existe", "Renta", JOptionPane.WARNING_MESSAGE); return; }

        // Show item info dialog with image
        mostrarInfoItem(item, "Información del Ítem");

        int dias;
        try { dias = Integer.parseInt(JOptionPane.showInputDialog(this, "¿Cuántos días desea rentar?")); }
        catch (Exception ex) { showMsg("Número de días inválido.", "Error", JOptionPane.ERROR_MESSAGE); return; }
        if (dias <= 0) { showMsg("Ingrese un número positivo de días.", "Error", JOptionPane.ERROR_MESSAGE); return; }

        double total = item.pagoRenta(dias);
        showMsg(String.format("Renta de: %s\nDías: %d\nTotal a pagar: %.2f Lps",
            item.getNombre(), dias, total), "Pago de Renta", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarInfoItem(RentItem item, String titulo) {
        JDialog dlg = new JDialog(this, titulo, true);
        dlg.setSize(400, 320);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(CARD_BG);
        dlg.setLayout(new BorderLayout(10, 10));

        JPanel info = new JPanel(new BorderLayout(10, 10));
        info.setBackground(CARD_BG);
        info.setBorder(BorderFactory.createEmptyBorder(18, 18, 10, 18));

        if (item.getImagen() != null) {
            JLabel imgLabel = new JLabel(item.getImagen());
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            info.add(imgLabel, BorderLayout.WEST);
        }

        JTextArea ta = new JTextArea(item.toString());
        ta.setFont(BODY_F);
        ta.setForeground(TEXT);
        ta.setBackground(CARD_BG);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        info.add(ta, BorderLayout.CENTER);

        JButton close = styledBtn("CERRAR", ACCENT);
        close.addActionListener(e -> dlg.dispose());
        JPanel bp = new JPanel();
        bp.setBackground(CARD_BG);
        bp.add(close);

        dlg.add(info, BorderLayout.CENTER);
        dlg.add(bp, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── C. Submenú ──────────────────────────────────────────────────────────────
    private void accionSubmenu() {
        String codigo = JOptionPane.showInputDialog(this, "Ingrese el código del ítem:");
        if (codigo == null) return;

        RentItem item = buscarItem(codigo.trim());
        if (item == null) { showMsg("Item No Existe", "Submenú", JOptionPane.WARNING_MESSAGE); return; }

        if (!(item instanceof MenuActions)) {
            showMsg("Este ítem no tiene submenú disponible.", "Submenú", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        MenuActions ma = (MenuActions) item;
        ma.submenu();

        String opStr = JOptionPane.showInputDialog(this, "Ingrese una opción (1-3):");
        if (opStr == null) return;
        try {
            ma.ejecutarOpcion(Integer.parseInt(opStr.trim()));
        } catch (Exception ex) {
            showMsg("Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── D. Imprimir Todo ────────────────────────────────────────────────────────
    private void accionImprimirTodo() {
        if (items.isEmpty()) {
            showMsg("No hay ítems registrados.", "Catálogo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dlg = new JDialog(this, "Catálogo Completo", true);
        dlg.setSize(820, 560);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(BG);
        dlg.setLayout(new BorderLayout());

        // Title bar
        JLabel titleLbl = new JLabel("  CATÁLOGO DE ÍTEMS  (" + items.size() + " registrados)", SwingConstants.LEFT);
        titleLbl.setFont(MENU_F);
        titleLbl.setForeground(ACCENT);
        titleLbl.setOpaque(true);
        titleLbl.setBackground(PANEL_BG);
        titleLbl.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 0));
        dlg.add(titleLbl, BorderLayout.NORTH);

        // Cards panel
        JPanel cards = new JPanel(new WrapLayout(FlowLayout.LEFT, 16, 16));
        cards.setBackground(BG);
        cards.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (RentItem item : items) cards.add(buildCard(item));

        JScrollPane scroll = new JScrollPane(cards);
        scroll.getViewport().setBackground(BG);
        scroll.setBorder(null);
        dlg.add(scroll, BorderLayout.CENTER);

        JButton close = styledBtn("CERRAR", ACCENT);
        close.addActionListener(e -> dlg.dispose());
        JPanel bp = new JPanel();
        bp.setBackground(BG);
        bp.add(close);
        dlg.add(bp, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private JPanel buildCard(RentItem item) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(item instanceof Movie ? ACCENT : ACCENT2, 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        card.setPreferredSize(new Dimension(220, 230));

        // Image
        if (item.getImagen() != null) {
            JLabel imgLabel = new JLabel(item.getImagen());
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(imgLabel);
            card.add(Box.createVerticalStrut(8));
        } else {
            JLabel placeholder = new JLabel(item instanceof Movie ? "MOVIE" : " GAME", SwingConstants.CENTER);
            placeholder.setFont(new Font("Dialog", Font.PLAIN, 32));
            placeholder.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(placeholder);
            card.add(Box.createVerticalStrut(8));
        }

        JLabel nameLbl = new JLabel(item.getNombre());
        nameLbl.setFont(MENU_F);
        nameLbl.setForeground(TEXT);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLbl);

        if (item instanceof Movie) {
            Movie m = (Movie) item;
            JLabel statusLbl = new JLabel(m.getEstado());
            statusLbl.setFont(BODY_F);
            statusLbl.setForeground(m.getEstado().equals("ESTRENO") ? new Color(255, 200, 50) : TEXT_DIM);
            statusLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(statusLbl);
        } else if (item instanceof Game) {
            JLabel gameLbl = new JLabel("PS3 GAME");
            gameLbl.setFont(BODY_F);
            gameLbl.setForeground(ACCENT2);
            gameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(gameLbl);
        }

        JLabel priceLbl = new JLabel(String.format("%.2f Lps/día", item.getPrecioBase()));
        priceLbl.setFont(new Font("Courier New", Font.BOLD, 13));
        priceLbl.setForeground(ACCENT);
        priceLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(4));
        card.add(priceLbl);

        JLabel codeLbl = new JLabel("ID: " + item.getCodigo());
        codeLbl.setFont(BODY_F);
        codeLbl.setForeground(TEXT_DIM);
        codeLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(codeLbl);

        return card;
    }

    // ── Helpers ─────────────────────────────────────────────────────────────────
    private boolean codigoExiste(String codigo) {
        for (RentItem i : items) if (i.getCodigo().equalsIgnoreCase(codigo)) return true;
        return false;
    }

    private RentItem buscarItem(String codigo) {
        for (RentItem i : items) if (i.getCodigo().equalsIgnoreCase(codigo)) return i;
        return null;
    }

    private void showMsg(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }

    private JButton styledBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(MENU_F);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color.darker());
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 1, true),
            BorderFactory.createEmptyBorder(8, 24, 8, 24)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── WrapLayout helper ────────────────────────────────────────────────────────
    static class WrapLayout extends FlowLayout {
        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
        @Override public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }
        @Override public Dimension minimumLayoutSize(Container target) {
            Dimension minimum = layoutSize(target, false);
            minimum.width -= (getHgap() + 1);
            return minimum;
        }
        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
                int hgap = getHgap(), vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);
                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0, rowHeight = 0;
                int nmembers = target.getComponentCount();
                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            addRow(dim, rowWidth, rowHeight);
                            rowWidth = 0;
                            rowHeight = 0;
                        }
                        if (rowWidth != 0) rowWidth += hgap;
                        rowWidth += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
                addRow(dim, rowWidth, rowHeight);
                dim.width += insets.left + insets.right + hgap * 2;
                dim.height += insets.top + insets.bottom + vgap * 2;
                return dim;
            }
        }
        private void addRow(Dimension dim, int rowWidth, int rowHeight) {
            dim.width = Math.max(dim.width, rowWidth);
            if (dim.height > 0) dim.height += getVgap();
            dim.height += rowHeight;
        }
    }

    // ── Main ────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new SistemaRenta().setVisible(true));
    }
}