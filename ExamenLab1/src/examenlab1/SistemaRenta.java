package examenlab1;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class SistemaRenta extends JFrame {

    private ArrayList<RentItem> items=new ArrayList<>();
   static final Color C_BG=new Color(0x00001A);
    static final Color C_PANEL=new Color(0x000000);
    static final Color C_CARD=new Color(0x202A44);
    static final Color C_FIELD=new Color(22,22,38);
    static final Color C_BORDER=new Color(50,50,80);
    static final Color C_RED=new Color(220,60,60);
    static final Color C_BLUE=new Color(60,140,240);
    static final Color C_GREEN=new Color(60,200,120);
    static final Color C_TEXT=new Color(220,220,240);
    static final Color C_MUTED=new Color(110,110,150);
    static final Color C_YELLOW=new Color(240,190,50);

    static final Font F_TITLE=new Font("Times New Roman",Font.BOLD,30);
    static final Font F_HEAD=new Font("Times New Roman",Font.BOLD,16);
    static final Font F_MONO=new Font("Times New Roman",Font.PLAIN,14);
    static final Font F_MONOB=new Font("Times New Roman",Font.BOLD,15);
    static final Font F_SMALL=new Font("Times New Roman",Font.PLAIN,13);

    public SistemaRenta() {
        setTitle("MULTIMEDIA RENTAL SYSTEM");
        setSize(860, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(C_BG);
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildMenu(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(C_PANEL);
        h.setBorder(new MatteBorder(0, 0, 2, 0, C_RED));
        JLabel logo = new JLabel("MULTIMEDIA RENTAL", SwingConstants.CENTER);
        logo.setFont(F_TITLE);
        logo.setForeground(C_RED);
        logo.setBorder(BorderFactory.createEmptyBorder(16, 24, 14,16));
        h.add(logo, BorderLayout.NORTH);
      
        return h;
    }

    private JPanel buildMenu() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(C_BG);
        JPanel grid = new JPanel(new GridLayout(2,2,14,14));
        grid.setBackground(C_BG);
        grid.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        grid.add(navBtn("", "AGREGAR ITEM","Registrar pelicula o juego",C_RED,e -> accionAgregar()));
        grid.add(navBtn("", "RENTAR","Calcular pago de renta",C_BLUE,e -> accionRentar()));
        grid.add(navBtn("", "SUBMENU GAME","Opciones especiales PS3",C_GREEN,  e -> accionSubmenu()));
        grid.add(navBtn("", "VER CATALOGO","Todos los items registrados",C_YELLOW, e -> accionCatalogo()));
        wrap.add(grid);
        return wrap;
    }

    private JButton navBtn(String letra, String titulo, String desc, Color accent, ActionListener al) {
        JButton b=new JButton(){
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()?new Color(35, 35, 58):C_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(getModel().isRollover() ? accent : C_BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight()-1, 12, 12);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
                g2.setFont(new Font("Times New Roman", Font.BOLD, 52));
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 25));
                g2.drawString(letra, getWidth() - 58, getHeight() - 10);
                g2.setFont(F_MONOB);
                g2.setColor(C_TEXT);
                g2.drawString(titulo,20,getHeight()/2-4);
                g2.setFont(F_SMALL);
                g2.setColor(C_MUTED);
                g2.drawString(desc, 20, getHeight() / 2 + 14);
                g2.dispose();
            }
        };
        b.setPreferredSize(new Dimension(340, 130));
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(al);
        return b;
    }

    private void accionAgregar(){
        JDialog sel = new JDialog(this, "Agregar Item", true);
        sel.setSize(320, 155);
        sel.setLocationRelativeTo(this);
        sel.setResizable(false);
        sel.getContentPane().setBackground(C_PANEL);
        sel.setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Que deseas agregar?", SwingConstants.CENTER);
        lbl.setFont(F_HEAD);
        lbl.setForeground(C_TEXT);
        lbl.setBorder(BorderFactory.createEmptyBorder(18, 0, 12, 0));
        sel.add(lbl, BorderLayout.NORTH);
        JPanel btns = new JPanel(new GridLayout(1, 2, 10, 0));
        btns.setBackground(C_PANEL);
        btns.setBorder(BorderFactory.createEmptyBorder(0, 24, 20, 24));
        JButton bMovie = formBtn("MOVIE", C_RED);
        JButton bGame  = formBtn("GAME", C_BLUE);
        bMovie.addActionListener(e -> { sel.dispose(); new FormMovie(this).setVisible(true); });
        bGame.addActionListener(e  -> { sel.dispose(); new FormGame(this).setVisible(true); });
        btns.add(bMovie);
        btns.add(bGame);
        sel.add(btns, BorderLayout.CENTER);
        sel.setVisible(true);
    }

    private void accionRentar(){
        new VentanaRentar(this).setVisible(true);
    }
    private void accionSubmenu(){ 
        new VentanaSubmenu(this).setVisible(true); 
    }
    private void accionCatalogo(){ 
        new VentanaCatalogo(this).setVisible(true); 
    }

    boolean codigoExiste(String cod) {
        for (RentItem i :items)
            if (i.getCodigo().equalsIgnoreCase(cod)) 
                return true;
        return false;
    }

    RentItem buscarItem(String cod) {
        for (RentItem i : items)
            if (i.getCodigo().equalsIgnoreCase(cod)) 
                return i;
        return null;
    }

    void agregarItem(RentItem r) { items.add(r); }

    static JButton formBtn(String txt, Color accent) {
        JButton b = new JButton(txt);
        b.setFont(F_MONOB);
        b.setForeground(C_TEXT);
        b.setBackground(new Color(accent.getRed() / 4, accent.getGreen() / 4, accent.getBlue() / 4));
        b.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(accent, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(accent.darker()); }
            public void mouseExited(MouseEvent e)  { b.setBackground(new Color(accent.getRed() / 4, accent.getGreen() / 4, accent.getBlue() / 4)); }
        });
        return b;
    }

    static JTextField styledField(){
        JTextField f = new JTextField();
        f.setFont(F_MONO);
        f.setForeground(C_TEXT);
        f.setBackground(C_FIELD);
        f.setCaretColor(C_TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(C_BORDER, 1, true),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        return f;
    }

    static JLabel fieldLabel(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(F_SMALL);
        l.setForeground(C_MUTED);
        return l;
    }

    static JLabel errorLabel() {
        JLabel l = new JLabel(" ");
        l.setFont(F_SMALL);
        l.setForeground(C_RED);
        return l;
    }

    static ImageIcon escalar(ImageIcon src, int w, int h) {
        if (src == null) return null;
        return new ImageIcon(src.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }

    public static void main(String[] args) {
        new SistemaRenta().setVisible(true);
    }


    class FormMovie extends JDialog {
        private JTextField fCodigo, fNombre, fPrecio, fYear, fMes, fDia;
        private JLabel imgPreview, errLabel, okLabel;
        private ImageIcon imgSeleccionada = null;

        FormMovie(JFrame owner) {
            super(owner, "Nueva Movie", true);
            setSize(580, 430);
            setLocationRelativeTo(owner);
            setResizable(false);
            getContentPane().setBackground(C_PANEL);
            setLayout(new BorderLayout());
            buildUI();
        }

        private void buildUI() {
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(new Color(18, 10, 10));
            header.setBorder(new MatteBorder(0, 0, 2, 0, C_RED));
            JLabel title = new JLabel("  REGISTRAR MOVIE", SwingConstants.LEFT);
            title.setFont(F_HEAD);
            title.setForeground(C_RED);
            title.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 0));
            header.add(title, BorderLayout.CENTER);
            add(header, BorderLayout.NORTH);

            fCodigo = styledField(); fNombre = styledField();
            fPrecio = styledField(); fYear   = styledField();
            fMes    = styledField(); fDia    = styledField();
            fYear.setText("2025"); fMes.setText("1"); fDia.setText("1");

            JPanel fechaRow = new JPanel(new GridLayout(1, 3, 6, 0));
            fechaRow.setBackground(C_PANEL);
            fechaRow.add(fYear); fechaRow.add(fMes); fechaRow.add(fDia);

            JPanel leftCol = new JPanel();
            leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
            leftCol.setBackground(C_PANEL);
            leftCol.add(fieldLabel("CODIGO")); leftCol.add(Box.createVerticalStrut(3));
            leftCol.add(fCodigo); leftCol.add(Box.createVerticalStrut(8));
            leftCol.add(fieldLabel("NOMBRE")); leftCol.add(Box.createVerticalStrut(3));
            leftCol.add(fNombre); leftCol.add(Box.createVerticalStrut(8));
            leftCol.add(fieldLabel("PRECIO BASE (Lps)")); leftCol.add(Box.createVerticalStrut(3));
            leftCol.add(fPrecio); leftCol.add(Box.createVerticalStrut(8));
            leftCol.add(fieldLabel("FECHA ESTRENO  (anno / mes / dia)")); leftCol.add(Box.createVerticalStrut(3));
            leftCol.add(fechaRow);

            imgPreview = new JLabel("Sin imagen", SwingConstants.CENTER);
            imgPreview.setFont(F_SMALL); imgPreview.setForeground(C_MUTED);
            imgPreview.setBackground(C_FIELD); imgPreview.setOpaque(true);
            imgPreview.setBorder(new LineBorder(C_BORDER, 1, true));
            imgPreview.setPreferredSize(new Dimension(180, 130));
            imgPreview.setMaximumSize(new Dimension(180, 130));
            imgPreview.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton btnImg = formBtn("Subir imagen", C_MUTED);
            btnImg.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnImg.setMaximumSize(new Dimension(180, 32));
            btnImg.addActionListener(e -> subirImagen());

            JPanel rightCol = new JPanel();
            rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
            rightCol.setBackground(C_PANEL);
            rightCol.add(fieldLabel("IMAGEN")); rightCol.add(Box.createVerticalStrut(6));
            rightCol.add(imgPreview); rightCol.add(Box.createVerticalStrut(8));
            rightCol.add(btnImg);

            JPanel split = new JPanel(new BorderLayout(16, 0));
            split.setBackground(C_PANEL);
            split.setBorder(BorderFactory.createEmptyBorder(14, 20, 10, 20));
            split.add(leftCol, BorderLayout.CENTER);
            split.add(rightCol, BorderLayout.EAST);
            add(split, BorderLayout.CENTER);

            errLabel = errorLabel();
            okLabel  = new JLabel(" "); okLabel.setFont(F_SMALL); okLabel.setForeground(C_GREEN);
            JButton btnGuardar = formBtn("GUARDAR MOVIE", C_RED);
            btnGuardar.addActionListener(e -> guardar());

            JPanel footer = new JPanel(new BorderLayout(0, 4));
            footer.setBackground(C_PANEL);
            footer.setBorder(BorderFactory.createEmptyBorder(8, 20, 14, 20));

            JPanel msgs = new JPanel(new GridLayout(2, 1));
            msgs.setBackground(C_PANEL);
            msgs.add(errLabel); msgs.add(okLabel);

            footer.add(msgs, BorderLayout.NORTH);
            footer.add(btnGuardar, BorderLayout.CENTER);
            add(footer, BorderLayout.SOUTH);
        }

        private void subirImagen() {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Seleccionar imagen");
            fc.setFileFilter(new FileNameExtensionFilter("Imagenes (jpg, png, gif)", "jpg", "jpeg", "png", "gif"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imgSeleccionada = new ImageIcon(fc.getSelectedFile().getAbsolutePath());
                imgPreview.setIcon(escalar(imgSeleccionada, 178, 128));
                imgPreview.setText("");
            }
        }

        private void guardar() {
            errLabel.setText(" "); okLabel.setText(" ");
            String cod  = fCodigo.getText().trim();
            String nom  = fNombre.getText().trim();
            String prec = fPrecio.getText().trim();
            if (cod.isEmpty() || nom.isEmpty() || prec.isEmpty()) {
                errLabel.setText("Codigo, nombre y precio son obligatorios."); return;
            }
            if (codigoExiste(cod)) {
                errLabel.setText("El codigo '" + cod + "' ya existe."); return;
            }
            double precio;
            try { precio = Double.parseDouble(prec); }
            catch (NumberFormatException ex) { errLabel.setText("Precio invalido."); return; }

            Movie m = new Movie(cod, nom, precio);
            m.setImagen(imgSeleccionada);
            try {
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(fYear.getText().trim()),
                        Integer.parseInt(fMes.getText().trim()) - 1,
                        Integer.parseInt(fDia.getText().trim()));
                m.setFechaEstreno(cal);
            } catch (Exception ex) { }

            agregarItem(m);
            okLabel.setText("Movie guardada. Estado: " + m.getEstado());
            fCodigo.setText(""); fNombre.setText(""); fPrecio.setText("");
            fYear.setText("2025"); fMes.setText("1"); fDia.setText("1");
            imgPreview.setIcon(null); imgPreview.setText("Sin imagen");
            imgSeleccionada = null;
        }
    }


    class FormGame extends JDialog {
        private JTextField fCodigo, fNombre, fYear, fMes, fDia, fSpec;
        private JLabel imgPreview, errLabel, okLabel;
        private JList<String> specList;
        private DefaultListModel<String> listModel;
        private ImageIcon imgSeleccionada = null;
        private Game gameActual = null;

        FormGame(JFrame owner) {
            super(owner, "Nuevo PS3 Game", true);
            setSize(680, 500);
            setLocationRelativeTo(owner);
            setResizable(false);
            getContentPane().setBackground(C_PANEL);
            setLayout(new BorderLayout());
            buildUI();
        }

        private void buildUI() {
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(new Color(10, 14, 22));
            header.setBorder(new MatteBorder(0, 0, 2, 0, C_BLUE));
            JLabel title = new JLabel("  REGISTRAR PS3 GAME", SwingConstants.LEFT);
            title.setFont(F_HEAD); title.setForeground(C_BLUE);
            title.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 0));
            header.add(title, BorderLayout.CENTER);
            JLabel pFijo = new JLabel("Precio fijo: 20 Lps/dia  ", SwingConstants.RIGHT);
            pFijo.setFont(F_SMALL); pFijo.setForeground(C_MUTED);
            header.add(pFijo, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);

            fCodigo = styledField(); fNombre = styledField();
            fYear   = styledField(); fMes    = styledField(); fDia = styledField();
            fYear.setText("2025"); fMes.setText("1"); fDia.setText("1");

            JPanel fechaRow = new JPanel(new GridLayout(1, 3, 6, 0));
            fechaRow.setBackground(C_PANEL);
            fechaRow.add(fYear); fechaRow.add(fMes); fechaRow.add(fDia);

            JPanel topLeft = new JPanel();
            topLeft.setLayout(new BoxLayout(topLeft, BoxLayout.Y_AXIS));
            topLeft.setBackground(C_PANEL);
            topLeft.add(fieldLabel("CODIGO")); topLeft.add(Box.createVerticalStrut(3));
            topLeft.add(fCodigo); topLeft.add(Box.createVerticalStrut(8));
            topLeft.add(fieldLabel("NOMBRE DEL JUEGO")); topLeft.add(Box.createVerticalStrut(3));
            topLeft.add(fNombre); topLeft.add(Box.createVerticalStrut(8));
            topLeft.add(fieldLabel("FECHA PUBLICACION  (anno / mes / dia)")); topLeft.add(Box.createVerticalStrut(3));
            topLeft.add(fechaRow);

            imgPreview = new JLabel("Sin imagen", SwingConstants.CENTER);
            imgPreview.setFont(F_SMALL); imgPreview.setForeground(C_MUTED);
            imgPreview.setBackground(C_FIELD); imgPreview.setOpaque(true);
            imgPreview.setBorder(new LineBorder(C_BORDER, 1, true));
            imgPreview.setPreferredSize(new Dimension(170, 115));
            imgPreview.setMaximumSize(new Dimension(170, 115));
            imgPreview.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton btnImg = formBtn("Subir imagen", C_MUTED);
            btnImg.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnImg.setMaximumSize(new Dimension(170, 32));
            btnImg.addActionListener(e -> subirImagen());

            JPanel topRight = new JPanel();
            topRight.setLayout(new BoxLayout(topRight, BoxLayout.Y_AXIS));
            topRight.setBackground(C_PANEL);
            topRight.add(fieldLabel("IMAGEN")); topRight.add(Box.createVerticalStrut(6));
            topRight.add(imgPreview); topRight.add(Box.createVerticalStrut(8));
            topRight.add(btnImg);

            JPanel topRow = new JPanel(new BorderLayout(16, 0));
            topRow.setBackground(C_PANEL);
            topRow.add(topLeft, BorderLayout.CENTER);
            topRow.add(topRight, BorderLayout.EAST);

            fSpec = styledField();
            fSpec.setMaximumSize(new Dimension(9999, 32));
            JButton btnAddSpec = formBtn("Agregar", C_BLUE);
            btnAddSpec.addActionListener(e -> agregarSpec());

            JPanel specInput = new JPanel(new BorderLayout(6, 0));
            specInput.setBackground(C_PANEL);
            specInput.add(fSpec, BorderLayout.CENTER);
            specInput.add(btnAddSpec, BorderLayout.EAST);

            listModel = new DefaultListModel<>();
            specList  = new JList<>(listModel);
            specList.setFont(F_MONO); specList.setForeground(C_TEXT);
            specList.setBackground(C_FIELD); specList.setSelectionBackground(C_BLUE);
            specList.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

            JScrollPane specScroll = new JScrollPane(specList);
            specScroll.setBorder(new LineBorder(C_BORDER, 1, true));
            specScroll.setPreferredSize(new Dimension(0, 80));
            specScroll.getViewport().setBackground(C_FIELD);

            JButton btnDelSpec = formBtn("Eliminar seleccionada", new Color(150, 50, 50));
            btnDelSpec.addActionListener(e -> {
                int idx = specList.getSelectedIndex();
                if (idx >= 0) listModel.remove(idx);
            });

            

            JPanel body = new JPanel();
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setBackground(C_PANEL);
            body.setBorder(BorderFactory.createEmptyBorder(14, 20, 10, 20));
            body.add(topRow);
            body.add(Box.createVerticalStrut(12));
            body.add(new JSeparator() {{
                setForeground(C_BORDER); setBackground(C_BORDER);
                setMaximumSize(new Dimension(9999, 1));
            }});
            body.add(Box.createVerticalStrut(10));

            add(body, BorderLayout.CENTER);

            errLabel = errorLabel();
            okLabel  = new JLabel(" "); okLabel.setFont(F_SMALL); okLabel.setForeground(C_GREEN);
            JButton btnGuardar = formBtn("GUARDAR GAME", C_BLUE);
            btnGuardar.addActionListener(e -> guardar());

            JPanel msgs = new JPanel(new GridLayout(2, 1));
            msgs.setBackground(C_PANEL);
            msgs.add(errLabel); msgs.add(okLabel);

            JPanel footer = new JPanel(new BorderLayout(0, 4));
            footer.setBackground(C_PANEL);
            footer.setBorder(BorderFactory.createEmptyBorder(8, 20, 14, 20));
            footer.add(msgs, BorderLayout.NORTH);
            footer.add(btnGuardar, BorderLayout.CENTER);
            add(footer, BorderLayout.SOUTH);
        }

        private void subirImagen() {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Seleccionar imagen");
            fc.setFileFilter(new FileNameExtensionFilter("Imagenes (jpg, png, gif)", "jpg", "jpeg", "png", "gif"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imgSeleccionada = new ImageIcon(fc.getSelectedFile().getAbsolutePath());
                imgPreview.setIcon(escalar(imgSeleccionada, 168, 113));
                imgPreview.setText("");
            }
        }

        private void agregarSpec() {
            String s = fSpec.getText().trim();
            if (!s.isEmpty()) { listModel.addElement(s); fSpec.setText(""); }
        }

        private void guardar() {
            errLabel.setText(" "); okLabel.setText(" ");
            String cod = fCodigo.getText().trim();
            String nom = fNombre.getText().trim();
            if (cod.isEmpty() || nom.isEmpty()) {
                errLabel.setText("Codigo y nombre son obligatorios."); return;
            }
            if (codigoExiste(cod)) {
                errLabel.setText("El codigo '" + cod + "' ya existe."); return;
            }
            Game g = new Game(cod, nom);
            g.setImagen(imgSeleccionada);
            try {
                g.setFechaPublicacion(Integer.parseInt(fYear.getText().trim()),
                                      Integer.parseInt(fMes.getText().trim()),
                                      Integer.parseInt(fDia.getText().trim()));
            } catch (Exception ex) { }
            for (int i = 0; i < listModel.size(); i++)
                g.addEspecificaciones(listModel.get(i));

            agregarItem(g);
            okLabel.setText("Game guardado. Specs: " + listModel.size());
            fCodigo.setText(""); fNombre.setText("");
            fYear.setText("2025"); fMes.setText("1"); fDia.setText("1");
            fSpec.setText(""); listModel.clear();
            imgPreview.setIcon(null); imgPreview.setText("Sin imagen");
            imgSeleccionada = null;
        }
    }


    class VentanaRentar extends JDialog {
        private JTextField fCodigo, fDias;
        private JLabel imgLbl, infoLbl, resultLbl, errLbl;
        private RentItem itemActual = null;

        VentanaRentar(JFrame owner) {
            super(owner, "Rentar Item", true);
            setSize(560, 370);
            setLocationRelativeTo(owner);
            setResizable(false);
            getContentPane().setBackground(C_PANEL);
            setLayout(new BorderLayout());
            buildUI();
        }

        private void buildUI() {
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(new Color(10, 18, 12));
            header.setBorder(new MatteBorder(0, 0, 2, 0, C_GREEN));
            JLabel t = new JLabel("  RENTAR", SwingConstants.LEFT);
            t.setFont(F_HEAD); t.setForeground(C_GREEN);
            t.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 0));
            header.add(t, BorderLayout.CENTER);
            add(header, BorderLayout.NORTH);

            fCodigo = styledField(); fCodigo.setMaximumSize(new Dimension(9999, 32));
            fDias   = styledField(); fDias.setMaximumSize(new Dimension(9999, 32));

            JButton btnBuscar = formBtn("BUSCAR", C_BLUE);
            btnBuscar.addActionListener(e -> buscar());
            btnBuscar.setAlignmentX(Component.LEFT_ALIGNMENT);

            JButton btnCalc = formBtn("CALCULAR RENTA", C_GREEN);
            btnCalc.addActionListener(e -> calcular());
            btnCalc.setAlignmentX(Component.LEFT_ALIGNMENT);

            infoLbl   = new JLabel("<html><font color='gray'>Ingresa un codigo para buscar.</font></html>");
            infoLbl.setFont(F_MONO); infoLbl.setForeground(C_TEXT);

            resultLbl = new JLabel(" ");
            resultLbl.setFont(new Font("Courier New", Font.BOLD, 15));
            resultLbl.setForeground(C_GREEN);

            errLbl = errorLabel();

            JPanel left = new JPanel();
            left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
            left.setBackground(C_PANEL);
            left.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 10));
            left.add(fieldLabel("CODIGO DEL ITEM")); left.add(Box.createVerticalStrut(3));
            left.add(fCodigo); left.add(Box.createVerticalStrut(6));
            left.add(btnBuscar); left.add(Box.createVerticalStrut(8));
            left.add(infoLbl); left.add(Box.createVerticalStrut(10));
            left.add(new JSeparator() {{ setForeground(C_BORDER); setBackground(C_BORDER); setMaximumSize(new Dimension(9999, 1)); }});
            left.add(Box.createVerticalStrut(10));
            left.add(fieldLabel("CANTIDAD DE DIAS")); left.add(Box.createVerticalStrut(3));
            left.add(fDias); left.add(Box.createVerticalStrut(6));
            left.add(btnCalc); left.add(Box.createVerticalStrut(8));
            left.add(errLbl); left.add(Box.createVerticalStrut(4));
            left.add(resultLbl);

            imgLbl = new JLabel("—", SwingConstants.CENTER);
            imgLbl.setFont(F_SMALL); imgLbl.setForeground(C_MUTED);
            imgLbl.setBackground(C_FIELD); imgLbl.setOpaque(true);
            imgLbl.setBorder(new LineBorder(C_BORDER, 1, true));
            imgLbl.setPreferredSize(new Dimension(170, 130));

            JPanel right = new JPanel(new BorderLayout());
            right.setBackground(C_PANEL);
            right.setBorder(BorderFactory.createEmptyBorder(14, 10, 10, 18));
            right.add(imgLbl, BorderLayout.NORTH);

            JPanel body = new JPanel(new BorderLayout());
            body.setBackground(C_PANEL);
            body.add(left, BorderLayout.CENTER);
            body.add(right, BorderLayout.EAST);
            add(body, BorderLayout.CENTER);
        }

        private void buscar() {
            String cod = fCodigo.getText().trim();
            itemActual = buscarItem(cod);
            if (itemActual == null) {
                infoLbl.setText("<html><font color='#dc3c3c'>Item No Existe</font></html>");
                imgLbl.setIcon(null); imgLbl.setText("—");
                resultLbl.setText(" ");
            } else {
                String tipo  = itemActual instanceof Movie ? "MOVIE" : "PS3 GAME";
                String extra = itemActual instanceof Movie ? " · " + ((Movie) itemActual).getEstado() : "";
                infoLbl.setText("<html><b>" + itemActual.getNombreItem() + "</b>  [" + tipo + extra + "]<br>"
                    + "Precio base: " + itemActual.getBaseRenta() + " Lps/dia</html>");
                if (itemActual.getImagen() != null) {
                    imgLbl.setIcon(escalar(itemActual.getImagen(), 168, 128));
                    imgLbl.setText("");
                } else {
                    imgLbl.setIcon(null);
                    imgLbl.setText(itemActual instanceof Movie ? "MOVIE" : "GAME");
                    imgLbl.setFont(F_MONOB);
                }
                errLbl.setText(" "); resultLbl.setText(" ");
            }
        }

        private void calcular() {
            if (itemActual == null) { errLbl.setText("Busca un item primero."); return; }
            int dias;
            try {
                dias = Integer.parseInt(fDias.getText().trim());
                if (dias <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) { errLbl.setText("Dias invalido."); return; }
            errLbl.setText(" ");
            double total = itemActual.pagoRenta(dias);
            resultLbl.setText(String.format("TOTAL: %.2f Lps  (%d dias)", total, dias));
        }
    }


    class VentanaSubmenu extends JDialog {
        private JTextField fCodigo;
        private JLabel infoLbl, imgLbl, errLbl;
        private Game gameActual = null;
        private JButton b1, b2, b3;

        VentanaSubmenu(JFrame owner) {
            super(owner, "Submenu PS3 Game", true);
            setSize(560, 390);
            setLocationRelativeTo(owner);
            setResizable(false);
            getContentPane().setBackground(C_PANEL);
            setLayout(new BorderLayout());
            buildUI();
        }

        private void buildUI() {
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(new Color(10, 12, 22));
            header.setBorder(new MatteBorder(0, 0, 2, 0, C_BLUE));
            JLabel t = new JLabel("  SUBMENU PS3 GAME", SwingConstants.LEFT);
            t.setFont(F_HEAD); t.setForeground(C_BLUE);
            t.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 0));
            header.add(t, BorderLayout.CENTER);
            add(header, BorderLayout.NORTH);

            fCodigo = styledField(); fCodigo.setMaximumSize(new Dimension(9999, 32));
            JButton btnBuscar = formBtn("BUSCAR", C_BLUE);

            infoLbl = new JLabel("<html><font color='gray'>Ingresa el codigo del juego.</font></html>");
            infoLbl.setFont(F_MONO); infoLbl.setForeground(C_TEXT);

            errLbl = errorLabel();

            b1 = formBtn("1.  Actualizar fecha de publicacion", C_BLUE);
            b2 = formBtn("2.  Agregar especificacion tecnica",  C_BLUE);
            b3 = formBtn("3.  Ver especificaciones tecnicas",   C_BLUE);
            b1.setEnabled(false); b2.setEnabled(false); b3.setEnabled(false);

            b1.addActionListener(e -> { if (gameActual != null) gameActual.ejecutarOpcion(1); });
            b2.addActionListener(e -> { if (gameActual != null) gameActual.ejecutarOpcion(2); });
            b3.addActionListener(e -> { if (gameActual != null) gameActual.ejecutarOpcion(3); });

            btnBuscar.addActionListener(e -> {
                buscar();
                boolean ok = gameActual != null;
                b1.setEnabled(ok); b2.setEnabled(ok); b3.setEnabled(ok);
            });

            JPanel opciones = new JPanel(new GridLayout(3, 1, 0, 8));
            opciones.setBackground(C_PANEL);
            opciones.add(b1); opciones.add(b2); opciones.add(b3);

            JPanel left = new JPanel();
            left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
            left.setBackground(C_PANEL);
            left.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 10));
            left.add(fieldLabel("CODIGO DEL JUEGO")); left.add(Box.createVerticalStrut(3));
            left.add(fCodigo); left.add(Box.createVerticalStrut(6));
            left.add(btnBuscar); left.add(Box.createVerticalStrut(8));
            left.add(infoLbl); left.add(Box.createVerticalStrut(4));
            left.add(errLbl); left.add(Box.createVerticalStrut(12));
            left.add(new JSeparator() {{ setForeground(C_BORDER); setBackground(C_BORDER); setMaximumSize(new Dimension(9999, 1)); }});
            left.add(Box.createVerticalStrut(12));
            left.add(opciones);

            imgLbl = new JLabel("—", SwingConstants.CENTER);
            imgLbl.setFont(F_SMALL); imgLbl.setForeground(C_MUTED);
            imgLbl.setBackground(C_FIELD); imgLbl.setOpaque(true);
            imgLbl.setBorder(new LineBorder(C_BORDER, 1, true));
            imgLbl.setPreferredSize(new Dimension(160, 120));

            JPanel right = new JPanel(new BorderLayout());
            right.setBackground(C_PANEL);
            right.setBorder(BorderFactory.createEmptyBorder(14, 10, 10, 18));
            right.add(imgLbl, BorderLayout.NORTH);

            JPanel body = new JPanel(new BorderLayout());
            body.setBackground(C_PANEL);
            body.add(left, BorderLayout.CENTER);
            body.add(right, BorderLayout.EAST);
            add(body, BorderLayout.CENTER);
        }

        private void buscar() {
            String cod = fCodigo.getText().trim();
            RentItem item = buscarItem(cod);
            if (item == null) {
                infoLbl.setText("<html><font color='#dc3c3c'>Item No Existe</font></html>");
                gameActual = null; imgLbl.setIcon(null); imgLbl.setText("—");
            } else if (!(item instanceof Game)) {
                infoLbl.setText("<html><font color='#dc3c3c'>Ese item es una Movie, no tiene submenu.</font></html>");
                gameActual = null; imgLbl.setIcon(null); imgLbl.setText("—");
            } else {
                gameActual = (Game) item;
                infoLbl.setText("<html><b>" + gameActual.getNombreItem() + "</b>  [PS3 GAME]<br>"
                    + "Specs: " + gameActual.getEspecificaciones().size() + " registradas</html>");
                if (gameActual.getImagen() != null) {
                    imgLbl.setIcon(escalar(gameActual.getImagen(), 158, 118));
                    imgLbl.setText("");
                } else {
                    imgLbl.setIcon(null);
                    imgLbl.setText("GAME");
                    imgLbl.setFont(F_MONOB);
                }
            }
        }
    }


    class VentanaCatalogo extends JDialog {
        VentanaCatalogo(JFrame owner) {
            super(owner, "Catalogo Completo", true);
            setSize(860, 560);
            setLocationRelativeTo(owner);
            getContentPane().setBackground(C_BG);
            setLayout(new BorderLayout());
            buildUI();
        }

        private void buildUI() {
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(C_PANEL);
            header.setBorder(new MatteBorder(0, 0, 2, 0, C_YELLOW));
            JLabel t = new JLabel("  CATALOGO  —  " + items.size() + " items registrados", SwingConstants.LEFT);
            t.setFont(F_HEAD); t.setForeground(C_YELLOW);
            t.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 0));
            header.add(t, BorderLayout.CENTER);
            JPanel legend = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 10));
            legend.setBackground(C_PANEL);
            JLabel lm = new JLabel("Movie"); lm.setForeground(C_RED); lm.setFont(F_SMALL);
            JLabel lg = new JLabel("Game"); lg.setForeground(C_BLUE); lg.setFont(F_SMALL);
            legend.add(lm); legend.add(lg);
            header.add(legend, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);

            if (items.isEmpty()) {
                JLabel empty = new JLabel("No hay items registrados.", SwingConstants.CENTER);
                empty.setFont(F_HEAD); empty.setForeground(C_MUTED);
                add(empty, BorderLayout.CENTER);
            } else {
                JPanel cards = new JPanel(new WrapLayout(FlowLayout.LEFT, 14, 14));
                cards.setBackground(C_BG);
                cards.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
                for (RentItem item : items) cards.add(buildCard(item));
                JScrollPane scroll = new JScrollPane(cards);
                scroll.getViewport().setBackground(C_BG);
                scroll.setBorder(null);
                scroll.getVerticalScrollBar().setUnitIncrement(16);
                add(scroll, BorderLayout.CENTER);
            }

            JButton close = formBtn("CERRAR", C_MUTED);
            close.addActionListener(e -> dispose());
            JPanel foot = new JPanel(); foot.setBackground(C_BG);
            foot.setBorder(BorderFactory.createEmptyBorder(6, 0, 8, 0));
            foot.add(close);
            add(foot, BorderLayout.SOUTH);
        }

        private JPanel buildCard(RentItem item) {
            boolean esMovie = item instanceof Movie;
            Color ac = esMovie ? C_RED : C_BLUE;
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(C_CARD);
            card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ac, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            card.setPreferredSize(new Dimension(190, 240));

            if (item.getImagen() != null) {
                JLabel img = new JLabel(escalar(item.getImagen(), 164, 108));
                img.setAlignmentX(Component.CENTER_ALIGNMENT);
                card.add(img);
            } else {
                JLabel ph = new JLabel(esMovie ? "MOVIE" : "GAME", SwingConstants.CENTER);
                ph.setFont(new Font("Courier New", Font.BOLD, 16));
                ph.setForeground(ac);
                ph.setAlignmentX(Component.CENTER_ALIGNMENT);
                ph.setPreferredSize(new Dimension(164, 108));
                card.add(ph);
            }

            card.add(Box.createVerticalStrut(6));

            JLabel badge = new JLabel(esMovie ? " MOVIE " : " PS3 GAME ", SwingConstants.CENTER);
            badge.setFont(new Font("Courier New", Font.BOLD, 9));
            badge.setForeground(Color.WHITE); badge.setBackground(ac);
            badge.setOpaque(true); badge.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(badge);
            card.add(Box.createVerticalStrut(4));

            JLabel name = new JLabel("<html><center>" + item.getNombreItem() + "</center></html>", SwingConstants.CENTER);
            name.setFont(F_MONOB); name.setForeground(C_TEXT);
            name.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(name);

            if (esMovie) {
                String est = ((Movie) item).getEstado();
                JLabel estLbl = new JLabel(est, SwingConstants.CENTER);
                estLbl.setFont(F_SMALL); estLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                estLbl.setForeground(est.equals("ESTRENO") ? C_YELLOW : C_MUTED);
                card.add(estLbl);
            }

            card.add(Box.createVerticalStrut(3));

            JLabel price = new JLabel(String.format("%.2f Lps/dia", item.getBaseRenta()), SwingConstants.CENTER);
            price.setFont(new Font("Courier New", Font.BOLD, 12));
            price.setForeground(C_GREEN); price.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(price);

            JLabel cod = new JLabel("ID: " + item.getCodigo(), SwingConstants.CENTER);
            cod.setFont(F_SMALL); cod.setForeground(C_MUTED);
            cod.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(cod);

            return card;
        }
    }


    static class WrapLayout extends FlowLayout {
        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
        @Override public Dimension preferredLayoutSize(Container t) { return ls(t, true); }
        @Override public Dimension minimumLayoutSize(Container t) { Dimension d = ls(t, false); d.width -= (getHgap() + 1); return d; }
        private Dimension ls(Container target, boolean pref) {
            synchronized (target.getTreeLock()) {
                int tw = target.getSize().width; if (tw == 0) tw = Integer.MAX_VALUE;
                Insets ins = target.getInsets();
                int maxW = tw - (ins.left + ins.right + getHgap() * 2);
                Dimension dim = new Dimension(0, 0); int rw = 0, rh = 0;
                for (int i = 0; i < target.getComponentCount(); i++) {
                    Component m = target.getComponent(i); if (!m.isVisible()) continue;
                    Dimension d = pref ? m.getPreferredSize() : m.getMinimumSize();
                    if (rw + d.width > maxW) { dim.width = Math.max(dim.width, rw); if (dim.height > 0) dim.height += getVgap(); dim.height += rh; rw = 0; rh = 0; }
                    if (rw != 0) rw += getHgap(); rw += d.width; rh = Math.max(rh, d.height);
                }
                dim.width = Math.max(dim.width, rw); if (dim.height > 0) dim.height += getVgap(); dim.height += rh;
                dim.width += ins.left + ins.right + getHgap() * 2; dim.height += ins.top + ins.bottom + getVgap() * 2;
                return dim;
            }
        }
    }
}