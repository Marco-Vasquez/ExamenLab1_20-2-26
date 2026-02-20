import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;

public class SistemaRenta extends JFrame {

    private ArrayList<RentItem> items = new ArrayList<>();

    // ── Colores y Fuentes ───────────────────────────────────────────────────────
    private static final Color BG       = new Color(10, 10, 20);
    private static final Color PANEL_BG = new Color(18, 18, 32);
    private static final Color CARD_BG  = new Color(26, 26, 46);
    private static final Color ACCENT   = new Color(255, 80, 80);
    private static final Color ACCENT2  = new Color(80, 160, 255);
    private static final Color TEXT     = new Color(230, 230, 245);
    private static final Color TEXT_DIM = new Color(140, 140, 165);
    private static final Font  TITLE_F  = new Font("Georgia", Font.BOLD, 28);
    private static final Font  MENU_F   = new Font("Courier New", Font.BOLD, 15);
    private static final Font  BODY_F   = new Font("Courier New", Font.PLAIN, 13);

    // ── Imágenes precargadas ────────────────────────────────────────────────────
    // Las imágenes están en /imagenes/1.jpg ... /imagenes/6.jpg
    private static final int TOTAL_IMAGENES = 6;
    private ImageIcon[] imagenesDisponibles = new ImageIcon[TOTAL_IMAGENES];
    private String[]    nombreImagenes      = new String[TOTAL_IMAGENES];

    public SistemaRenta() {
        cargarImagenesPrecargadas();

        setTitle("SISTEMA DE RENTA MULTIMEDIA");
        setSize(940, 660);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildMenu(),   BorderLayout.CENTER);
    }

    // ── Carga de imágenes desde /imagenes/ ──────────────────────────────────────
    private void cargarImagenesPrecargadas() {
        for (int i = 0; i < TOTAL_IMAGENES; i++) {
            String ruta = "/imagenes/" + (i + 1) + ".jpg";
            nombreImagenes[i] = "Imagen " + (i + 1);
            try {
                java.net.URL url = getClass().getResource(ruta);
                if (url != null) {
                    ImageIcon raw = new ImageIcon(url);
                    imagenesDisponibles[i] = escalarIcono(raw, 160, 110);
                } else {
                    // intenta como ruta de archivo absoluta
                    java.io.File f = new java.io.File(ruta);
                    if (f.exists()) {
                        ImageIcon raw = new ImageIcon(f.getAbsolutePath());
                        imagenesDisponibles[i] = escalarIcono(raw, 160, 110);
                    } else {
                        imagenesDisponibles[i] = null;
                    }
                }
            } catch (Exception e) {
                imagenesDisponibles[i] = null;
            }
        }
    }

    private ImageIcon escalarIcono(ImageIcon src, int w, int h) {
        Image scaled = src.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // ── Header ──────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.setBorder(new MatteBorder(0, 0, 2, 0, ACCENT));

        JLabel title = new JLabel("MULTIMEDIA RENTAL SYSTEM", SwingConstants.CENTER);
        title.setFont(TITLE_F);
        title.setForeground(ACCENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 14, 20));
        header.add(title, BorderLayout.CENTER);

        JLabel sub = new JLabel("Movies & Games  //  Java Swing GUI", SwingConstants.CENTER);
        sub.setFont(BODY_F);
        sub.setForeground(TEXT_DIM);
        sub.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        header.add(sub, BorderLayout.SOUTH);
        return header;
    }

    // ── Menú principal ──────────────────────────────────────────────────────────
    private JPanel buildMenu() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG);

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setBackground(BG);
        grid.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        String[][] btns = {
            {"A", "AGREGAR ÍTEM",  "Añadir película o videojuego"},
            {"B", "RENTAR",        "Calcular pago de renta"},
            {"C", "SUBMENÚ",       "Opciones especiales de juego"},
            {"D", "IMPRIMIR TODO", "Ver catálogo completo"}
        };

        for (String[] b : btns) grid.add(menuCard(b[0], b[1], b[2]));
        outer.add(grid);
        return outer;
    }

    private JButton menuCard(String letra, String label, String desc) {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed()  ? ACCENT.darker()
                         : getModel().isRollover() ? new Color(35, 35, 60) : CARD_BG;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(ACCENT);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);

                // letra fantasma de fondo
                g2.setFont(new Font("Georgia", Font.BOLD, 48));
                g2.setColor(new Color(255, 80, 80, 35));
                g2.drawString(letra, 14, getHeight() - 14);

                // label principal
                g2.setFont(MENU_F);
                g2.setColor(TEXT);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(label, (getWidth() - fm.stringWidth(label)) / 2, getHeight()/2 - 6);

                // descripción
                g2.setFont(BODY_F);
                g2.setColor(TEXT_DIM);
                FontMetrics fm2 = g2.getFontMetrics();
                g2.drawString(desc, (getWidth() - fm2.stringWidth(desc)) / 2, getHeight()/2 + 17);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(300, 140));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            switch (letra) {
                case "A": accionAgregarItem(); break;
                case "B": accionRentar();      break;
                case "C": accionSubmenu();     break;
                case "D": accionImprimirTodo();break;
            }
        });
        return btn;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // A. AGREGAR ÍTEM
    // ═══════════════════════════════════════════════════════════════════════════
    private void accionAgregarItem() {
        // Elegir tipo
        String[] tipos = {"Movie", "Game"};
        String tipo = (String) JOptionPane.showInputDialog(this,
            "¿Qué tipo de ítem desea agregar?", "Agregar Ítem",
            JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        if (tipo == null) return;

        // Código único
        String codigo = JOptionPane.showInputDialog(this, "Código del ítem:");
        if (codigo == null || codigo.trim().isEmpty()) return;
        if (codigoExiste(codigo.trim())) {
            showMsg("El código '" + codigo.trim() + "' ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Nombre
        String nombre = JOptionPane.showInputDialog(this, "Nombre del ítem:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        // Seleccionar imagen de las precargadas
        ImageIcon imagenSeleccionada = seleccionarImagenPrecargada();

        if (tipo.equals("Movie")) {
            // Precio
            double precio;
            try {
                String ps = JOptionPane.showInputDialog(this, "Precio base de renta (Lps):");
                if (ps == null) return;
                precio = Double.parseDouble(ps.trim());
            } catch (NumberFormatException ex) {
                showMsg("Precio inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Movie m = new Movie(codigo.trim(), nombre.trim(), precio);
            m.setImagen(imagenSeleccionada);

            // Fecha de estreno
            try {
                String y = JOptionPane.showInputDialog(this, "Año de estreno (ej: 2025):");
                String mo = JOptionPane.showInputDialog(this, "Mes de estreno (1-12):");
                String d  = JOptionPane.showInputDialog(this, "Día de estreno:");
                if (y != null && mo != null && d != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Integer.parseInt(y.trim()),
                            Integer.parseInt(mo.trim()) - 1,
                            Integer.parseInt(d.trim()));
                    m.setFechaEstreno(cal);
                }
            } catch (Exception ex) { /* usa fecha actual */ }

            items.add(m);
            showMsg("✓ Movie agregada: " + m.getNombreItem() + "\nEstado: " + m.getEstado(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } else { // Game
            Game g = new Game(codigo.trim(), nombre.trim());
            g.setImagen(imagenSeleccionada);
            items.add(g);
            showMsg("✓ Game agregado: " + g.getNombreItem() + "\nPrecio fijo: 20 Lps/día",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Muestra un diálogo con las 6 imágenes disponibles para que el usuario elija.
     */
    private ImageIcon seleccionarImagenPrecargada() {
        JDialog dlg = new JDialog(this, "Seleccionar Imagen", true);
        dlg.setSize(700, 280);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(PANEL_BG);
        dlg.setLayout(new BorderLayout(8, 8));

        JLabel instruccion = new JLabel("  Haz clic en una imagen para seleccionarla:", SwingConstants.LEFT);
        instruccion.setFont(BODY_F);
        instruccion.setForeground(TEXT);
        instruccion.setBorder(BorderFactory.createEmptyBorder(10, 10, 4, 10));
        instruccion.setOpaque(true);
        instruccion.setBackground(PANEL_BG);
        dlg.add(instruccion, BorderLayout.NORTH);

        JPanel grid = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        grid.setBackground(PANEL_BG);

        final ImageIcon[] resultado = {null};

        for (int i = 0; i < TOTAL_IMAGENES; i++) {
            final int idx = i;
            JButton imgBtn = new JButton();
            imgBtn.setPreferredSize(new Dimension(100, 80));
            imgBtn.setBackground(CARD_BG);
            imgBtn.setBorder(new LineBorder(TEXT_DIM, 1, true));
            imgBtn.setFocusPainted(false);
            imgBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            if (imagenesDisponibles[i] != null) {
                // miniatura 90x65 para el botón
                ImageIcon mini = escalarIcono(imagenesDisponibles[i].getIconWidth() > 0
                    ? imagenesDisponibles[i]
                    : new ImageIcon(), 90, 65);
                imgBtn.setIcon(imagenesDisponibles[i] != null
                    ? escalarIcono(imagenesDisponibles[i], 90, 65) : null);
            } else {
                imgBtn.setText("<html><center>" + nombreImagenes[i] + "<br><small>(no encontrada)</small></center></html>");
                imgBtn.setForeground(TEXT_DIM);
                imgBtn.setFont(BODY_F);
            }

            imgBtn.setToolTipText(nombreImagenes[i]);

            imgBtn.addActionListener(e -> {
                resultado[0] = imagenesDisponibles[idx];
                // resalta seleccionado
                for (Component c : grid.getComponents())
                    ((JButton) c).setBorder(new LineBorder(TEXT_DIM, 1, true));
                imgBtn.setBorder(new LineBorder(ACCENT, 3, true));
                dlg.dispose();
            });

            grid.add(imgBtn);
        }

        dlg.add(grid, BorderLayout.CENTER);

        // Botón "Sin imagen"
        JButton noImg = styledBtn("Sin imagen", TEXT_DIM);
        noImg.addActionListener(e -> { resultado[0] = null; dlg.dispose(); });
        JPanel bot = new JPanel();
        bot.setBackground(PANEL_BG);
        bot.add(noImg);
        dlg.add(bot, BorderLayout.SOUTH);

        dlg.setVisible(true);
        return resultado[0];
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // B. RENTAR
    // ═══════════════════════════════════════════════════════════════════════════
    private void accionRentar() {
        String codigo = JOptionPane.showInputDialog(this, "Ingrese el código del ítem a rentar:");
        if (codigo == null) return;

        RentItem item = buscarItem(codigo.trim());
        if (item == null) {
            showMsg("Item No Existe", "Renta", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mostrar info con imagen
        mostrarInfoItem(item, "Información del Ítem");

        // Pedir días
        String diasStr = JOptionPane.showInputDialog(this, "¿Cuántos días desea rentar?");
        if (diasStr == null) return;
        int dias;
        try {
            dias = Integer.parseInt(diasStr.trim());
            if (dias <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showMsg("Número de días inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double total = item.pagoRenta(dias);
        showMsg(String.format(
            "═══════════════════════════\n  RESUMEN DE RENTA\n═══════════════════════════\n" +
            "Ítem:    %s\nCódigo:  %s\nDías:    %d\n───────────────────────────\n" +
            "TOTAL:   %.2f Lps",
            item.getNombreItem(), item.getCodigo(), dias, total),
            "Pago de Renta", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Diálogo de info de ítem con imagen ──────────────────────────────────────
    private void mostrarInfoItem(RentItem item, String titulo) {
        JDialog dlg = new JDialog(this, titulo, true);
        dlg.setSize(460, 310);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(CARD_BG);
        dlg.setLayout(new BorderLayout(10, 10));

        JPanel content = new JPanel(new BorderLayout(14, 0));
        content.setBackground(CARD_BG);
        content.setBorder(BorderFactory.createEmptyBorder(18, 18, 10, 18));

        // imagen a la izquierda
        if (item.getImagen() != null) {
            JLabel imgLbl = new JLabel(item.getImagen());
            imgLbl.setVerticalAlignment(SwingConstants.TOP);
            content.add(imgLbl, BorderLayout.WEST);
        }

        // info a la derecha
        JTextArea ta = new JTextArea(item.toString().replace(" | ", "\n"));
        ta.setFont(BODY_F);
        ta.setForeground(TEXT);
        ta.setBackground(CARD_BG);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
        content.add(ta, BorderLayout.CENTER);

        JButton close = styledBtn("CERRAR", ACCENT);
        close.addActionListener(e -> dlg.dispose());
        JPanel bp = new JPanel();
        bp.setBackground(CARD_BG);
        bp.add(close);

        dlg.add(content, BorderLayout.CENTER);
        dlg.add(bp,      BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // C. SUBMENÚ (solo para Game)
    // ═══════════════════════════════════════════════════════════════════════════
    private void accionSubmenu() {
        String codigo = JOptionPane.showInputDialog(this, "Ingrese el código del ítem:");
        if (codigo == null) return;

        RentItem item = buscarItem(codigo.trim());
        if (item == null) {
            showMsg("Item No Existe", "Submenú", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!(item instanceof MenuActions)) {
            showMsg("Este ítem es una Movie y no tiene submenú.", "Submenú", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        MenuActions ma = (MenuActions) item;

        // Diálogo de submenú con botones
        mostrarSubmenuGrafico((Game) item, ma);
    }

    private void mostrarSubmenuGrafico(Game game, MenuActions ma) {
        JDialog dlg = new JDialog(this, "Submenú: " + game.getNombreItem(), true);
        dlg.setSize(420, 320);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(CARD_BG);
        dlg.setLayout(new BorderLayout(10, 10));

        // Encabezado
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 20, 40));
        header.setBorder(new MatteBorder(0, 0, 2, 0, ACCENT2));
        JLabel title = new JLabel("  ► " + game.getNombreItem().toUpperCase() + "  —  PS3 GAME", SwingConstants.LEFT);
        title.setFont(MENU_F);
        title.setForeground(ACCENT2);
        title.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        header.add(title, BorderLayout.CENTER);
        // imagen pequeña si existe
        if (game.getImagen() != null) {
            ImageIcon mini = escalarIcono(game.getImagen(), 60, 42);
            JLabel imgLbl = new JLabel(mini);
            imgLbl.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 10));
            header.add(imgLbl, BorderLayout.EAST);
        }
        dlg.add(header, BorderLayout.NORTH);

        // Botones de opciones
        JPanel opciones = new JPanel(new GridLayout(3, 1, 0, 12));
        opciones.setBackground(CARD_BG);
        opciones.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        String[] labels = {
            "1.  Actualizar fecha de publicación",
            "2.  Agregar especificación técnica",
            "3.  Ver especificaciones técnicas"
        };

        for (int i = 0; i < 3; i++) {
            final int opcion = i + 1;
            JButton ob = opcionBtn(labels[i]);
            ob.addActionListener(e -> {
                dlg.dispose();
                ma.ejecutarOpcion(opcion);
            });
            opciones.add(ob);
        }

        dlg.add(opciones, BorderLayout.CENTER);

        JButton cerrar = styledBtn("CERRAR", TEXT_DIM);
        cerrar.addActionListener(e -> dlg.dispose());
        JPanel bp = new JPanel();
        bp.setBackground(CARD_BG);
        bp.add(cerrar);
        dlg.add(bp, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private JButton opcionBtn(String texto) {
        JButton b = new JButton(texto);
        b.setFont(BODY_F);
        b.setForeground(TEXT);
        b.setBackground(new Color(35, 35, 60));
        b.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(ACCENT2, 1, true),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(new Color(50, 50, 90)); }
            public void mouseExited(MouseEvent e)  { b.setBackground(new Color(35, 35, 60)); }
        });
        return b;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // D. IMPRIMIR TODO
    // ═══════════════════════════════════════════════════════════════════════════
    private void accionImprimirTodo() {
        if (items.isEmpty()) {
            showMsg("No hay ítems registrados aún.", "Catálogo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dlg = new JDialog(this, "Catálogo Completo", true);
        dlg.setSize(860, 580);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(BG);
        dlg.setLayout(new BorderLayout());

        // Barra superior con contador
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(PANEL_BG);
        topBar.setBorder(new MatteBorder(0, 0, 2, 0, ACCENT));
        JLabel catTitle = new JLabel("   CATÁLOGO  (" + items.size() + " ítems registrados)", SwingConstants.LEFT);
        catTitle.setFont(MENU_F);
        catTitle.setForeground(ACCENT);
        catTitle.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 0));
        topBar.add(catTitle, BorderLayout.CENTER);

        // Leyenda colores
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        leyenda.setBackground(PANEL_BG);
        leyenda.add(badge("● Movie", ACCENT));
        leyenda.add(badge("● Game", ACCENT2));
        topBar.add(leyenda, BorderLayout.EAST);
        dlg.add(topBar, BorderLayout.NORTH);

        // Panel de tarjetas
        JPanel cards = new JPanel(new WrapLayout(FlowLayout.LEFT, 18, 18));
        cards.setBackground(BG);
        cards.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        for (RentItem item : items) cards.add(buildCard(item));

        JScrollPane scroll = new JScrollPane(cards);
        scroll.getViewport().setBackground(BG);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        dlg.add(scroll, BorderLayout.CENTER);

        JButton close = styledBtn("CERRAR", ACCENT);
        close.addActionListener(e -> dlg.dispose());
        JPanel bp = new JPanel();
        bp.setBackground(BG);
        bp.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        bp.add(close);
        dlg.add(bp, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private JLabel badge(String texto, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(BODY_F);
        l.setForeground(color);
        return l;
    }

    // ── Tarjeta individual ──────────────────────────────────────────────────────
    private JPanel buildCard(RentItem item) {
        boolean esMovie = item instanceof Movie;
        Color borderColor = esMovie ? ACCENT : ACCENT2;

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(borderColor, 2, true),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        card.setPreferredSize(new Dimension(200, 245));

        // ── Imagen ──
        if (item.getImagen() != null) {
            ImageIcon icono = escalarIcono(item.getImagen(), 160, 105);
            JLabel imgLbl = new JLabel(icono);
            imgLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(imgLbl);
        } else {
            JLabel placeholder = new JLabel(esMovie ? "🎬" : "🎮", SwingConstants.CENTER);
            placeholder.setFont(new Font("Dialog", Font.PLAIN, 40));
            placeholder.setAlignmentX(Component.CENTER_ALIGNMENT);
            placeholder.setPreferredSize(new Dimension(160, 105));
            card.add(placeholder);
        }

        card.add(Box.createVerticalStrut(8));

        // ── Tipo badge ──
        JLabel tipoBadge = new JLabel(esMovie ? "MOVIE" : "PS3 GAME", SwingConstants.CENTER);
        tipoBadge.setFont(new Font("Courier New", Font.BOLD, 10));
        tipoBadge.setForeground(Color.WHITE);
        tipoBadge.setBackground(borderColor);
        tipoBadge.setOpaque(true);
        tipoBadge.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        tipoBadge.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(tipoBadge);

        card.add(Box.createVerticalStrut(5));

        // ── Nombre ──
        JLabel nameLbl = new JLabel("<html><center>" + item.getNombreItem() + "</center></html>", SwingConstants.CENTER);
        nameLbl.setFont(MENU_F);
        nameLbl.setForeground(TEXT);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLbl);

        // ── Estado (solo Movie) ──
        if (esMovie) {
            Movie m = (Movie) item;
            String estado = m.getEstado();
            JLabel statusLbl = new JLabel(estado, SwingConstants.CENTER);
            statusLbl.setFont(new Font("Courier New", Font.BOLD, 12));
            statusLbl.setForeground(estado.equals("ESTRENO") ? new Color(255, 200, 50) : TEXT_DIM);
            statusLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(statusLbl);
        }

        card.add(Box.createVerticalStrut(4));

        // ── Precio ──
        JLabel priceLbl = new JLabel(String.format("%.2f Lps/día", item.getBaseRenta()), SwingConstants.CENTER);
        priceLbl.setFont(new Font("Courier New", Font.BOLD, 13));
        priceLbl.setForeground(new Color(100, 220, 130));
        priceLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(priceLbl);

        // ── Código ──
        JLabel codeLbl = new JLabel("ID: " + item.getCodigo(), SwingConstants.CENTER);
        codeLbl.setFont(BODY_F);
        codeLbl.setForeground(TEXT_DIM);
        codeLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(codeLbl);

        return card;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════════════════════
    private boolean codigoExiste(String codigo) {
        for (RentItem i : items)
            if (i.getCodigo().equalsIgnoreCase(codigo)) return true;
        return false;
    }

    private RentItem buscarItem(String codigo) {
        for (RentItem i : items)
            if (i.getCodigo().equalsIgnoreCase(codigo)) return i;
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

    // ── WrapLayout (FlowLayout con salto de línea automático) ───────────────────
    static class WrapLayout extends FlowLayout {
        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }

        @Override public Dimension preferredLayoutSize(Container t) { return layoutSize(t, true); }
        @Override public Dimension minimumLayoutSize(Container t)   {
            Dimension d = layoutSize(t, false); d.width -= (getHgap() + 1); return d;
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int tw = target.getSize().width;
                if (tw == 0) tw = Integer.MAX_VALUE;
                Insets ins = target.getInsets();
                int maxW = tw - (ins.left + ins.right + getHgap() * 2);
                Dimension dim = new Dimension(0, 0);
                int rw = 0, rh = 0;
                for (int i = 0; i < target.getComponentCount(); i++) {
                    Component m = target.getComponent(i);
                    if (!m.isVisible()) continue;
                    Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                    if (rw + d.width > maxW) { addRow(dim, rw, rh); rw = 0; rh = 0; }
                    if (rw != 0) rw += getHgap();
                    rw += d.width;
                    rh = Math.max(rh, d.height);
                }
                addRow(dim, rw, rh);
                dim.width  += ins.left + ins.right + getHgap() * 2;
                dim.height += ins.top + ins.bottom + getVgap() * 2;
                return dim;
            }
        }
        private void addRow(Dimension dim, int rw, int rh) {
            dim.width = Math.max(dim.width, rw);
            if (dim.height > 0) dim.height += getVgap();
            dim.height += rh;
        }
    }

    // ── Main ────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new SistemaRenta().setVisible(true));
    }
}