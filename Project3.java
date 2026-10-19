package question.company;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class PROJECT2 extends JFrame {
    static final Color BG = new Color(0x0F1630), SIDEBAR = new Color(0x0B1128), CARD = new Color(0x182044),
            FIELD = new Color(0x222C5C), ACCENT = new Color(0x5B7CFA), ACCENT_H = new Color(0x7B96FF),
            TEXT = new Color(0xE9EDFF), MUTED = new Color(0x98A4D3), LINE = new Color(0x2C3870),
            GOOD = new Color(0x3DDC97), WARN = new Color(0xFFB84D), BAD = new Color(0xFF6B6B);
    static Font font(int style, int size) { return new Font("SansSerif", style, size); }
    static JLabel label(String t, int style, int size, Color c) {
        JLabel l = new JLabel(t);
        l.setFont(font(style, size));
        l.setForeground(c);
        return l;
    }
    static void aa(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    static Color alpha(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }
    static void say(JLabel l, String text, Color c) {
        l.setText(text); l.setForeground(c);
    }
    record Rec(int id, String name, String phone, String email, String address, String book, LocalDate issue, LocalDate due) {}
    static String status(Rec r) {
        LocalDate t = LocalDate.now();
        return r.due().isBefore(t) ? "Overdue" : r.due().isEqual(t) ? "Due today" : "Active";
    }
    static Color statusColor(String s) {
        return s.equals("Overdue") ? BAD : s.equals("Due today") ? WARN : GOOD;
    }
    interface Store {
        List<Rec> all() throws Exception;
        Rec find(int id) throws Exception;
        void insert(Rec r) throws Exception;
        void update(Rec r) throws Exception;
        boolean delete(int id) throws Exception;
    }
    static class MemoryStore implements Store {
        final TreeMap<Integer, Rec> m = new TreeMap<>();
        MemoryStore() {
            LocalDate t = LocalDate.now();
        }
        void put(int id, String n, String ph, String em, String ad, String b, LocalDate i, LocalDate d) {
            m.put(id, new Rec(id, n, ph, em, ad, b, i, d));
        }
        public List<Rec> all() { return new ArrayList<>(m.values()); }
        public Rec find(int id) { return m.get(id); }
        public void insert(Rec r) throws Exception {
            if (m.containsKey(r.id())) throw new Exception("ID " + r.id() + " already exists.");
            m.put(r.id(), r);
        }
        public void update(Rec r) throws Exception {
            if (!m.containsKey(r.id())) throw new Exception("No record with ID " + r.id() + ".");
            m.put(r.id(), r);
        }
        public boolean delete(int id) { return m.remove(id) != null; }
    }
    static class MySqlStore implements Store {
        static final String COLS = "id, Name, phone, email, address, book, Date_of_issue, Date_of_returning";
        final Connection c;
        MySqlStore() throws Exception {
            c=DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","happy");
            Statement s=c.createStatement();
            s.executeUpdate("use Library");
        }
        static String env(String k, String d) { String v = System.getenv(k); return v == null ? d : v; }
        static String nz(String s) { return s == null ? "" : s; }
        static Rec map(ResultSet rs) throws SQLException {
            return new Rec(rs.getInt(1), rs.getString(2), nz(rs.getString(3)), nz(rs.getString(4)), nz(rs.getString(5)),
                    rs.getString(6), rs.getDate(7).toLocalDate(), rs.getDate(8).toLocalDate());
        }
        public List<Rec> all() throws Exception {
            try (Statement s = c.createStatement();
                 ResultSet rs = s.executeQuery("SELECT " + COLS + " FROM lib ORDER BY id")) {
                List<Rec> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
        public Rec find(int id) throws Exception {
            try (PreparedStatement p = c.prepareStatement("SELECT " + COLS + " FROM lib WHERE id=?")) {
                p.setInt(1, id);
                try (ResultSet rs = p.executeQuery()) { return rs.next() ? map(rs) : null; }
            }
        }
        public void insert(Rec r) throws Exception {
            try (PreparedStatement p = c.prepareStatement("INSERT INTO lib (" + COLS + ") VALUES (?,?,?,?,?,?,?,?)")) {
                p.setInt(1, r.id()); p.setString(2, r.name());
                p.setString(3, r.phone()); p.setString(4, r.email()); p.setString(5, r.address());
                p.setString(6, r.book());
                p.setDate(7, java.sql.Date.valueOf(r.issue())); p.setDate(8, java.sql.Date.valueOf(r.due()));
                p.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                throw new Exception("ID " + r.id() + " already exists.");
            }
        }
        public void update(Rec r) throws Exception {
            try (PreparedStatement p = c.prepareStatement("UPDATE lib SET Name=?, phone=?, email=?, address=?, book=?, Date_of_issue=?, Date_of_returning=? WHERE id=?")) {
                p.setString(1, r.name());
                p.setString(2, r.phone()); p.setString(3, r.email()); p.setString(4, r.address());
                p.setString(5, r.book());
                p.setDate(6, java.sql.Date.valueOf(r.issue())); p.setDate(7, java.sql.Date.valueOf(r.due()));
                p.setInt(8, r.id());
                if (p.executeUpdate() == 0) throw new Exception("No record with ID " + r.id() + ".");
            }
        }
        public boolean delete(int id) throws Exception {
            try (PreparedStatement p = c.prepareStatement("DELETE FROM lib WHERE id=?")) {
                p.setInt(1, id);
                return p.executeUpdate() > 0;
            }
        }
    }
    static class RoundButton extends JButton {
        final Color base, hover;
        boolean over;
        RoundButton(String t, Color base, Color hover, Color fg) {
            super(t);
            this.base = base; this.hover = hover;
            setForeground(fg);
            setFont(font(Font.BOLD, 14));
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false); setOpaque(false);
            setBorder(new EmptyBorder(11, 22, 11, 22));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { over = true; repaint(); }
                public void mouseExited(MouseEvent e) { over = false; repaint(); }
            });
        }
        static RoundButton primary(String t) { return new RoundButton(t, ACCENT, ACCENT_H, Color.WHITE); }
        static RoundButton secondary(String t) { return new RoundButton(t, FIELD, LINE, TEXT); }
        static RoundButton danger(String t) { return new RoundButton(t, BAD, new Color(0xFF8A8A), Color.WHITE); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
            g2.setColor(!isEnabled() ? alpha(base, 70) : over ? hover : base);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
            g2.dispose();
            super.paintComponent(g);
        }
    }
    static class NavButton extends JButton {
        boolean active, over;
        NavButton(String t) {
            super(t);
            setHorizontalAlignment(LEFT);
            setFont(font(Font.BOLD, 14));
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false); setOpaque(false);
            setBorder(new EmptyBorder(12, 26, 12, 16));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            setAlignmentX(LEFT_ALIGNMENT);
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { over = true; repaint(); }
                public void mouseExited(MouseEvent e) { over = false; repaint(); }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
            if (active) {
                g2.setColor(alpha(ACCENT, 60));
                g2.fill(new RoundRectangle2D.Float(10, 2, getWidth() - 20, getHeight() - 4, 12, 12));
                g2.setColor(ACCENT);
                g2.fill(new RoundRectangle2D.Float(0, 10, 5, getHeight() - 20, 5, 5));
            } else if (over) {
                g2.setColor(alpha(Color.WHITE, 14));
                g2.fill(new RoundRectangle2D.Float(10, 2, getWidth() - 20, getHeight() - 4, 12, 12));
            }
            g2.dispose();
            setForeground(active || over ? TEXT : MUTED);
            super.paintComponent(g);
        }
    }
    static class Card extends JPanel {
        Color fill = CARD, line = LINE, stripe;
        Card(int pad) { setOpaque(false); setBorder(new EmptyBorder(pad, pad + 2, pad, pad + 2)); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
            RoundRectangle2D.Float r = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
            g2.setColor(fill); g2.fill(r);
            if (stripe != null) { g2.setClip(r); g2.setColor(stripe); g2.fillRect(0, 0, 5, getHeight()); g2.setClip(null); }
            g2.setColor(line); g2.draw(r);
            g2.dispose();
        }
    }
    static class Field extends JTextField {
        final String hint;
        boolean focus;
        Field(String hint) {
            this.hint = hint;
            setOpaque(false); setForeground(TEXT); setCaretColor(TEXT); setDisabledTextColor(MUTED);
            setFont(font(Font.PLAIN, 15));
            setBorder(new EmptyBorder(10, 14, 10, 14));
            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { focus = true; repaint(); }
                public void focusLost(FocusEvent e) { focus = false; repaint(); }
            });
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
            boolean live = isEditable() && isEnabled();
            RoundRectangle2D.Float r = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            g2.setColor(live ? FIELD : new Color(0x1B2450)); g2.fill(r);
            g2.setColor(focus ? ACCENT : LINE); g2.setStroke(new BasicStroke(focus ? 2f : 1f)); g2.draw(r);
            if (getText().isEmpty() && hint != null && live) {
                g2.setColor(alpha(MUTED, 150)); g2.setFont(getFont());
                Insets in = getInsets();
                g2.drawString(hint, in.left, (getHeight() + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
    static class Pill extends JLabel {
        Color color = GOOD;
        Pill() { setFont(font(Font.BOLD, 12)); setBorder(new EmptyBorder(4, 12, 4, 12)); setOpaque(false); }
        void set(String text) { setText(text); color = statusColor(text); setForeground(color); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
            g2.setColor(alpha(color, 45));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
            g2.dispose();
            super.paintComponent(g);
        }
    }
    static class SlimScrollBar extends BasicScrollBarUI {
        protected void configureScrollBarColors() { thumbColor = LINE; trackColor = CARD; }
        protected JButton createDecreaseButton(int o) { return zero(); }
        protected JButton createIncreaseButton(int o) { return zero(); }
        static JButton zero() { JButton b = new JButton(); b.setPreferredSize(new Dimension(0, 0)); return b; }
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) { g.setColor(trackColor); g.fillRect(r.x, r.y, r.width, r.height); }
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
            g2.setColor(thumbColor); g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8); g2.dispose();
        }
    }
    static class RecTable {
        static final int STATUS_COL = 8;
        final DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Member", "Phone", "Email", "Address", "Book", "Issued", "Due", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Class<?> getColumnClass(int c) { return c == 0 ? Integer.class : String.class; }
        };
        final JTable table = new JTable(model);
        final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        final JScrollPane scroll = new JScrollPane(table);
        RecTable() {
            table.setRowSorter(sorter);
            table.setRowHeight(42); table.setShowGrid(false); table.setIntercellSpacing(new Dimension(0, 0));
            table.setFillsViewportHeight(true); table.setBackground(CARD); table.setFocusable(false);
            table.setFont(font(Font.PLAIN, 14));
            DefaultTableCellRenderer cell = new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                    super.getTableCellRendererComponent(t, v, false, false, r, c);
                    setBorder(new EmptyBorder(0, 14, 0, 14));
                    setFont(font(c == 0 ? Font.BOLD : Font.PLAIN, 14));
                    setBackground(sel ? new Color(0x2F3E85) : r % 2 == 1 ? new Color(0x1C2650) : CARD);
                    setForeground(c == 0 ? ACCENT_H : TEXT);
                    setHorizontalAlignment(LEFT);
                    setToolTipText(v == null ? null : v.toString());
                    return this;
                }
            };
            table.setDefaultRenderer(Object.class, cell);
            table.setDefaultRenderer(Integer.class, cell);
            Pill pill = new Pill();
            JPanel pillCell = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 7));
            pillCell.add(pill);
            table.getColumnModel().getColumn(STATUS_COL).setCellRenderer((t, v, sel, foc, r, c) -> {
                pill.set(String.valueOf(v));
                pillCell.setBackground(sel ? new Color(0x2F3E85) : r % 2 == 1 ? new Color(0x1C2650) : CARD);
                return pillCell;
            });
            JTableHeader h = table.getTableHeader();
            h.setReorderingAllowed(false);
            h.setPreferredSize(new Dimension(0, 40));
            h.setDefaultRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                    super.getTableCellRendererComponent(t, String.valueOf(v).toUpperCase(), false, false, r, c);
                    setOpaque(true); setBackground(FIELD); setForeground(MUTED);
                    setFont(font(Font.BOLD, 11)); setBorder(new EmptyBorder(0, 14, 0, 14));
                    setHorizontalAlignment(LEFT);
                    return this;
                }
            });
            int[] w = {55, 130, 110, 180, 170, 160, 95, 95, 100};
            for (int i = 0; i < w.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
            scroll.setBorder(null);
            scroll.getViewport().setBackground(CARD);
            scroll.setOpaque(false);
            scroll.getVerticalScrollBar().setUI(new SlimScrollBar());
            scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
        }
        void load(List<Rec> rows) {
            model.setRowCount(0);
            for (Rec r : rows)
                model.addRow(new Object[]{r.id(), r.name(), r.phone(), r.email(), r.address(), r.book(),
                        r.issue().toString(), r.due().toString(), status(r)});
        }
        void filter(String q) {
            sorter.setRowFilter(q == null || q.isBlank() ? null : RowFilter.regexFilter("(?i)" + Pattern.quote(q.trim())));
        }
    }
    /** The input fields shared by Issue and Update screens, with validation. */
    static class RecordForm extends JPanel {
        static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
        static final Pattern PHONE_CHARS = Pattern.compile("^\\+?[0-9 ()-]+$");
        final Field id = new Field("e.g. 111"), name = new Field("Member name"),
                phone = new Field("e.g. +91 98765 43210"), email = new Field("name@example.com"),
                address = new Field("Street, city, PIN"), book = new Field("Book title"),
                issue = new Field("YYYY-MM-DD"), due = new Field("YYYY-MM-DD");
        RecordForm() {
            setOpaque(false);
            setLayout(new GridBagLayout());
            cell(id, "Record ID", 0, 0, 1);
            cell(name, "Member name", 1, 0, 1);
            cell(phone, "Phone number", 0, 1, 1);
            cell(email, "Email", 1, 1, 1);
            cell(address, "Address", 0, 2, 2);
            cell(book, "Book title", 0, 3, 2);
            cell(issue, "Issue date", 0, 4, 1);
            cell(due, "Return / due date", 1, 4, 1);
            clear();
        }
        void cell(Field f, String title, int x, int y, int w) {
            JPanel p = new JPanel(new BorderLayout(0, 6));
            p.setOpaque(false);
            p.add(label(title.toUpperCase(), Font.BOLD, 11, MUTED), BorderLayout.NORTH);
            p.add(f, BorderLayout.CENTER);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = x; c.gridy = y; c.gridwidth = w; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 0, 16, x == 0 && w == 1 ? 18 : 0);
            add(p, c);
        }
        void clear() {
            id.setText(""); name.setText(""); phone.setText(""); email.setText(""); address.setText(""); book.setText("");
            issue.setText(LocalDate.now().toString()); due.setText(LocalDate.now().plusDays(14).toString());
        }
        void setRecord(Rec r) {
            id.setText(String.valueOf(r.id())); name.setText(r.name());
            phone.setText(r.phone()); email.setText(r.email()); address.setText(r.address());
            book.setText(r.book());
            issue.setText(r.issue().toString()); due.setText(r.due().toString());
        }
        void setFormEnabled(boolean on) { for (Field f : all()) f.setEnabled(on); }
        Field[] all() { return new Field[]{id, name, phone, email, address, book, issue, due}; }
        void onEnter(Runnable r) { for (Field f : all()) f.addActionListener(e -> r.run()); }
        static LocalDate date(String text, String what) {
            try { return LocalDate.parse(text.trim()); }
            catch (Exception e) { throw new IllegalArgumentException(what + " must look like 2025-01-31."); }
        }
        Rec getRecord() {
            int i;
            try { i = Integer.parseInt(id.getText().trim()); }
            catch (NumberFormatException e) { throw new IllegalArgumentException("Record ID must be a whole number."); }
            if (i <= 0) throw new IllegalArgumentException("Record ID must be greater than zero.");
            String n = name.getText().trim(), b = book.getText().trim();
            String ph = phone.getText().trim(), em = email.getText().trim(), ad = address.getText().trim();
            if (n.isEmpty()) throw new IllegalArgumentException("Member name is required.");
            if (ph.isEmpty()) throw new IllegalArgumentException("Phone number is required.");
            int digits = ph.replaceAll("\\D", "").length();
            if (!PHONE_CHARS.matcher(ph).matches() || digits < 7 || digits > 15)
                throw new IllegalArgumentException("Phone number must have 7 to 15 digits (+, spaces, - and () allowed).");
            if (em.isEmpty()) throw new IllegalArgumentException("Email is required.");
            if (!EMAIL.matcher(em).matches()) throw new IllegalArgumentException("Email must look like name@example.com.");
            if (ad.isEmpty()) throw new IllegalArgumentException("Address is required.");
            if (b.isEmpty()) throw new IllegalArgumentException("Book title is required.");
            LocalDate a = date(issue.getText(), "Issue date"), d = date(due.getText(), "Return date");
            if (d.isBefore(a)) throw new IllegalArgumentException("Return date can't be before the issue date.");
            return new Rec(i, n, ph, em, ad, b, a, d);
        }
    }
    static JPanel header(String title, JLabel sub, JComponent right) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JPanel left = new JPanel(new GridLayout(2, 1, 0, 4));
        left.setOpaque(false);
        left.add(label(title, Font.BOLD, 28, TEXT));
        left.add(sub);
        p.add(left, BorderLayout.WEST);
        if (right != null) p.add(right, BorderLayout.EAST);
        return p;
    }
    static JPanel header(String title, String sub, JComponent right) { return header(title, label(sub, Font.PLAIN, 14, MUTED), right); }
    static JPanel flow(JComponent... cs) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        p.setOpaque(false);
        for (JComponent c : cs) p.add(c);
        return p;
    }
    static JPanel lookupRow(Field f, RoundButton b) {
        f.setPreferredSize(new Dimension(180, 42));
        return flow(label("RECORD ID", Font.BOLD, 11, MUTED), f, b);
    }
    static JPanel top(JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(c, BorderLayout.NORTH);
        return p;
    }
    static DocumentListener changed(Runnable r) {
        return new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { r.run(); }
            public void removeUpdate(DocumentEvent e) { r.run(); }
            public void changedUpdate(DocumentEvent e) { r.run(); }
        };
    }
    final Store store;
    final CardLayout cards = new CardLayout();
    final JPanel pages = new JPanel(cards);
    final Map<String, Screen> screens = new LinkedHashMap<>();
    final Map<String, NavButton> navs = new LinkedHashMap<>();
    abstract static class Screen extends JPanel {
        Screen() {
            setOpaque(false);
            setLayout(new BorderLayout(0, 22));
            setBorder(new EmptyBorder(28, 34, 28, 34));
        }
        void onShow() {}
    }
    PROJECT2(Store store, boolean demo) {
        super("Library Book Issue Management");
        this.store = store;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 720));
        setSize(1240, 820);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        screens.put("dashboard", new Dashboard());
        screens.put("add", new AddScreen());
        screens.put("update", new UpdateScreen());
        screens.put("delete", new DeleteScreen());
        screens.put("search", new SearchScreen());
        screens.put("records", new RecordsScreen());
        pages.setOpaque(false);
        screens.forEach(pages::add);
        add(sidebar(demo), BorderLayout.WEST);
        add(pages, BorderLayout.CENTER);
        go("dashboard");
    }
    JPanel sidebar(boolean demo) {
        JPanel s = new JPanel();
        s.setBackground(SIDEBAR);
        s.setLayout(new BoxLayout(s, BoxLayout.Y_AXIS));
        s.setPreferredSize(new Dimension(240, 10));
        s.setBorder(new EmptyBorder(26, 0, 22, 0));
        JComponent logo = new JComponent() {
            { setPreferredSize(new Dimension(44, 44)); }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
                g2.setPaint(new GradientPaint(0, 0, ACCENT_H, 44, 44, new Color(0x8B5CF6)));
                g2.fill(new RoundRectangle2D.Float(0, 0, 44, 44, 14, 14));
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(11, 11, 9, 22, 3, 3));
                g2.fill(new RoundRectangle2D.Float(23, 11, 9, 22, 3, 3));
                g2.dispose();
            }
        };
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        brand.setOpaque(false);
        brand.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        brand.setAlignmentX(LEFT_ALIGNMENT);
        JPanel names = new JPanel(new GridLayout(2, 1));
        names.setOpaque(false);
        names.add(label("Library", Font.BOLD, 20, TEXT));
        names.add(label("Book Issue Manager", Font.PLAIN, 12, MUTED));
        brand.add(logo);
        brand.add(names);
        s.add(brand);
        s.add(Box.createVerticalStrut(30));
        String[][] items = {{"dashboard", "Dashboard"}, {"add", "Issue Book"}, {"update", "Update Record"},
                {"delete", "Delete Record"}, {"search", "Search"}, {"records", "All Records"}};
        for (String[] it : items) {
            NavButton b = new NavButton(it[1]);
            b.addActionListener(e -> go(it[0]));
            navs.put(it[0], b);
            s.add(b);
            s.add(Box.createVerticalStrut(4));
        }
        s.add(Box.createVerticalGlue());
        JLabel st = label((demo ? "  Demo data (in memory)" : "  Connected to MySQL"), Font.PLAIN, 12, MUTED);
        st.setIcon(new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create(); aa(g2);
                g2.setColor(demo ? WARN : GOOD); g2.fillOval(x, y, 9, 9); g2.dispose();
            }
            public int getIconWidth() { return 9; }
            public int getIconHeight() { return 9; }
        });
        st.setBorder(new EmptyBorder(0, 26, 0, 0));
        st.setAlignmentX(LEFT_ALIGNMENT);
        s.add(st);
        return s;
    }
    void go(String key) {
        cards.show(pages, key);
        navs.forEach((k, b) -> { b.active = k.equals(key); b.repaint(); });
        screens.get(key).onShow();
    }
    void error(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
    }
    class Dashboard extends Screen {
        final JLabel total = new JLabel("0"), active = new JLabel("0"), dueToday = new JLabel("0"), overdue = new JLabel("0");
        final RecTable recent = new RecTable();
        Dashboard() {
            RoundButton issueBtn = RoundButton.primary("+  Issue a book");
            issueBtn.addActionListener(e -> go("add"));
            add(header("Dashboard", "A quick look at what's out on loan.", issueBtn), BorderLayout.NORTH);
            JPanel stats = new JPanel(new GridLayout(1, 4, 16, 0));
            stats.setOpaque(false);
            stats.add(stat("TOTAL RECORDS", total, ACCENT_H, ACCENT));
            stats.add(stat("ACTIVE LOANS", active, GOOD, GOOD));
            stats.add(stat("DUE TODAY", dueToday, WARN, WARN));
            stats.add(stat("OVERDUE", overdue, BAD, BAD));
            Card list = new Card(20);
            list.setLayout(new BorderLayout(0, 14));
            list.add(label("Recently issued", Font.BOLD, 16, TEXT), BorderLayout.NORTH);
            list.add(recent.scroll, BorderLayout.CENTER);
            JPanel body = new JPanel(new BorderLayout(0, 18));
            body.setOpaque(false);
            body.add(stats, BorderLayout.NORTH);
            body.add(list, BorderLayout.CENTER);
            add(body, BorderLayout.CENTER);
        }
        JPanel stat(String title, JLabel value, Color color, Color stripe) {
            Card k = new Card(18);
            k.stripe = stripe;
            k.setLayout(new BorderLayout(0, 6));
            value.setFont(font(Font.BOLD, 36));
            value.setForeground(color);
            k.add(label(title, Font.BOLD, 11, MUTED), BorderLayout.NORTH);
            k.add(value, BorderLayout.CENTER);
            return k;
        }
        void onShow() {
            try {
                List<Rec> all = store.all();
                int a = 0, d = 0, o = 0;
                for (Rec r : all) { String s = status(r); if (s.equals("Overdue")) o++; else if (s.equals("Due today")) d++; else a++; }
                total.setText(String.valueOf(all.size())); active.setText(String.valueOf(a + d));
                dueToday.setText(String.valueOf(d)); overdue.setText(String.valueOf(o));
                all.sort(Comparator.comparing(Rec::issue).reversed().thenComparing(Rec::id, Comparator.reverseOrder()));
                recent.load(all.subList(0, Math.min(6, all.size())));
            } catch (Exception e) { error(e); }
        }
    }
    class AddScreen extends Screen {
        final RecordForm form = new RecordForm();
        final JLabel msg = label(" ", Font.BOLD, 13, MUTED);
        AddScreen() {
            add(header("Issue a Book", "Create a new issue record.", null), BorderLayout.NORTH);
            RoundButton save = RoundButton.primary("Save record"), clear = RoundButton.secondary("Clear");
            save.addActionListener(e -> save());
            clear.addActionListener(e -> { form.clear(); say(msg, " ", MUTED); });
            form.onEnter(this::save);
            Card card = new Card(26);
            card.setLayout(new BorderLayout(0, 4));
            card.add(form, BorderLayout.CENTER);
            JPanel south = new JPanel(new BorderLayout(0, 14));
            south.setOpaque(false);
            south.add(flow(save, clear), BorderLayout.NORTH);
            south.add(msg, BorderLayout.SOUTH);
            card.add(south, BorderLayout.SOUTH);
            Card tips = new Card(22);
            tips.setLayout(new BorderLayout(0, 12));
            tips.add(label("Quick tips", Font.BOLD, 16, TEXT), BorderLayout.NORTH);
            JLabel body = new JLabel("<html><body style='width:215px;color:#98A4D3'>"
                    + "<p><b style='color:#E9EDFF'>Record ID</b><br>Must be unique. Saving with an existing ID is rejected.</p><br>"
                    + "<p><b style='color:#E9EDFF'>Contact</b><br>Phone needs 7 to 15 digits. Email must look like name@example.com.</p><br>"
                    + "<p><b style='color:#E9EDFF'>Dates</b><br>Use YYYY-MM-DD. Issue date defaults to today, return date to +14 days.</p><br>"
                    + "<p><b style='color:#E9EDFF'>Shortcut</b><br>Press Enter in any field to save.</p></body></html>");
            body.setFont(font(Font.PLAIN, 13));
            body.setVerticalAlignment(SwingConstants.TOP);
            tips.add(body, BorderLayout.CENTER);
            JPanel east = top(tips);
            east.setPreferredSize(new Dimension(280, 10));
            JPanel row = new JPanel(new BorderLayout(18, 0));
            row.setOpaque(false);
            row.add(top(card), BorderLayout.CENTER);
            row.add(east, BorderLayout.EAST);
            add(row, BorderLayout.CENTER);
        }
        void save() {
            try {
                Rec r = form.getRecord();
                store.insert(r);
                say(msg, "Saved. Record #" + r.id() + " was added.", GOOD);
                form.clear();
            } catch (IllegalArgumentException e) { say(msg, e.getMessage(), WARN);
            } catch (Exception e) { say(msg, "Could not save: " + e.getMessage(), BAD); }
        }
        void onShow() { say(msg, " ", MUTED); }
    }
    class UpdateScreen extends Screen {
        final Field lookup = new Field("e.g. 104");
        final RecordForm form = new RecordForm();
        final JLabel msg = label(" ", Font.BOLD, 13, MUTED);
        final RoundButton save = RoundButton.primary("Save changes"), discard = RoundButton.secondary("Discard");
        Rec loaded;
        UpdateScreen() {
            add(header("Update a Record", "Load a record by ID, edit it, then save.", null), BorderLayout.NORTH);
            RoundButton load = RoundButton.primary("Load record");
            load.addActionListener(e -> loadFromField());
            lookup.addActionListener(e -> loadFromField());
            save.addActionListener(e -> saveChanges());
            discard.addActionListener(e -> { if (loaded != null) form.setRecord(loaded); say(msg, " ", MUTED); });
            form.onEnter(this::saveChanges);
            Card card = new Card(26);
            card.setLayout(new BorderLayout(0, 18));
            JPanel north = new JPanel(new BorderLayout(0, 18));
            north.setOpaque(false);
            north.add(lookupRow(lookup, load), BorderLayout.NORTH);
            JPanel sep = new JPanel();
            sep.setBackground(LINE);
            sep.setPreferredSize(new Dimension(10, 1));
            north.add(sep, BorderLayout.SOUTH);
            card.add(north, BorderLayout.NORTH);
            card.add(form, BorderLayout.CENTER);
            JPanel south = new JPanel(new BorderLayout(0, 14));
            south.setOpaque(false);
            south.add(flow(save, discard), BorderLayout.NORTH);
            south.add(msg, BorderLayout.SOUTH);
            card.add(south, BorderLayout.SOUTH);
            add(top(card), BorderLayout.CENTER);
        }
        void reset() {
            loaded = null; form.clear(); form.setFormEnabled(false);
            save.setEnabled(false); discard.setEnabled(false);
        }
        void loadId(int id) { lookup.setText(String.valueOf(id)); loadFromField(); }
        void loadFromField() {
            try {
                Rec r = store.find(Integer.parseInt(lookup.getText().trim()));
                if (r == null) { reset(); say(msg, "No record found with ID " + lookup.getText().trim() + ".", WARN); return; }
                loaded = r; form.setRecord(r); form.setFormEnabled(true); form.id.setEditable(false);
                save.setEnabled(true); discard.setEnabled(true);
                say(msg, "Record #" + r.id() + " loaded. Edit the fields and save.", MUTED);
            } catch (NumberFormatException e) { reset(); say(msg, "Enter a numeric record ID.", WARN);
            } catch (Exception e) { error(e); }
        }
        void saveChanges() {
            if (loaded == null) return;
            try {
                Rec r = form.getRecord();
                store.update(r);
                loaded = r;
                say(msg, "Saved. Record #" + r.id() + " was updated.", GOOD);
            } catch (IllegalArgumentException e) { say(msg, e.getMessage(), WARN);
            } catch (Exception e) { say(msg, "Could not update: " + e.getMessage(), BAD); }
        }
        void onShow() { lookup.setText(""); reset(); say(msg, "Enter an ID above to begin.", MUTED); }
    }
    class DeleteScreen extends Screen {
        final Field lookup = new Field("e.g. 105");
        final JLabel msg = label(" ", Font.BOLD, 13, MUTED);
        final JPanel details = new JPanel(new BorderLayout());
        final RoundButton delete = RoundButton.danger("Delete record"), cancel = RoundButton.secondary("Cancel");
        Rec found;
        DeleteScreen() {
            add(header("Delete a Record", "This permanently removes the record. It can't be undone.", null), BorderLayout.NORTH);
            RoundButton find = RoundButton.primary("Find record");
            find.addActionListener(e -> findRecord());
            lookup.addActionListener(e -> findRecord());
            delete.addActionListener(e -> confirmDelete());
            cancel.addActionListener(e -> onShow());
            details.setOpaque(false);
            Card card = new Card(26);
            card.setLayout(new BorderLayout(0, 18));
            card.add(lookupRow(lookup, find), BorderLayout.NORTH);
            card.add(details, BorderLayout.CENTER);
            JPanel south = new JPanel(new BorderLayout(0, 14));
            south.setOpaque(false);
            south.add(flow(delete, cancel), BorderLayout.NORTH);
            south.add(msg, BorderLayout.SOUTH);
            card.add(south, BorderLayout.SOUTH);
            add(top(card), BorderLayout.CENTER);
        }
        void loadId(int id) { lookup.setText(String.valueOf(id)); findRecord(); }
        void findRecord() {
            details.removeAll();
            found = null; delete.setEnabled(false); cancel.setEnabled(false);
            try {
                Rec r = store.find(Integer.parseInt(lookup.getText().trim()));
                if (r == null) { say(msg, "No record found with ID " + lookup.getText().trim() + ".", WARN); }
                else {
                    found = r; delete.setEnabled(true); cancel.setEnabled(true);
                    details.add(preview(r), BorderLayout.CENTER);
                    say(msg, "Review the record below, then confirm deletion.", MUTED);
                }
            } catch (NumberFormatException e) { say(msg, "Enter a numeric record ID.", WARN);
            } catch (Exception e) { error(e); }
            details.revalidate(); details.repaint();
        }
        JComponent preview(Rec r) {
            Card c = new Card(20);
            c.fill = new Color(0x1B2450); c.line = alpha(BAD, 140); c.stripe = BAD;
            c.setLayout(new GridBagLayout());
            Pill p = new Pill(); p.set(status(r));
            String[] k = {"RECORD ID", "MEMBER", "PHONE", "EMAIL", "ADDRESS", "BOOK", "ISSUED", "DUE"};
            String[] v = {"#" + r.id(), r.name(), r.phone(), r.email(), r.address(), r.book(), r.issue().toString(), r.due().toString()};
            GridBagConstraints g = new GridBagConstraints();
            g.anchor = GridBagConstraints.WEST; g.insets = new Insets(5, 0, 5, 24);
            for (int i = 0; i < k.length; i++) {
                g.gridy = i; g.gridx = 0; c.add(label(k[i], Font.BOLD, 11, MUTED), g);
                g.gridx = 1; g.weightx = 1; c.add(label(v[i], Font.BOLD, 15, TEXT), g); g.weightx = 0;
            }
            g.gridy = k.length; g.gridx = 0; c.add(label("STATUS", Font.BOLD, 11, MUTED), g);
            g.gridx = 1; c.add(p, g);
            return c;
        }
        void confirmDelete() {
            if (found == null) return;
            int ok = JOptionPane.showConfirmDialog(this,
                    "Delete record #" + found.id() + " (" + found.name() + " - " + found.book() + ")?\nThis can't be undone.",
                    "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok != JOptionPane.YES_OPTION) return;
            try {
                int id = found.id();
                boolean done = store.delete(id);
                onShow();
                say(msg, done ? "Deleted record #" + id + "." : "Record #" + id + " no longer exists.", done ? GOOD : WARN);
            } catch (Exception e) { say(msg, "Could not delete: " + e.getMessage(), BAD); }
        }
        void onShow() {
            lookup.setText(""); details.removeAll(); found = null;
            delete.setEnabled(false); cancel.setEnabled(false);
            say(msg, "Enter an ID above to find the record.", MUTED);
            details.revalidate(); details.repaint();
        }
    }
    class SearchScreen extends Screen {
        final Field q = new Field("Search by ID, name, phone, email, address or book...");
        final RecTable t = new RecTable();
        final JLabel count = label(" ", Font.BOLD, 13, MUTED);
        SearchScreen() {
            add(header("Search", "Type to filter instantly. Matches any column.", null), BorderLayout.NORTH);
            Card card = new Card(22);
            card.setLayout(new BorderLayout(0, 16));
            JPanel bar = new JPanel(new BorderLayout(16, 0));
            bar.setOpaque(false);
            bar.add(q, BorderLayout.CENTER);
            bar.add(count, BorderLayout.EAST);
            card.add(bar, BorderLayout.NORTH);
            card.add(t.scroll, BorderLayout.CENTER);
            add(card, BorderLayout.CENTER);
            q.getDocument().addDocumentListener(changed(() -> { t.filter(q.getText()); updateCount(); }));
        }
        void updateCount() {
            int shown = t.table.getRowCount(), all = t.model.getRowCount();
            count.setText(shown == all ? all + " records" : shown + " of " + all + " records");
        }
        void onShow() {
            try { t.load(store.all()); t.filter(q.getText()); updateCount(); } catch (Exception e) { error(e); }
        }
    }
    class RecordsScreen extends Screen {
        final RecTable t = new RecTable();
        final JLabel sub = label(" ", Font.PLAIN, 14, MUTED);
        RecordsScreen() {
            RoundButton refresh = RoundButton.secondary("Refresh");
            refresh.addActionListener(e -> onShow());
            add(header("All Records", sub, refresh), BorderLayout.NORTH);
            Card card = new Card(14);
            card.setLayout(new BorderLayout());
            card.add(t.scroll, BorderLayout.CENTER);
            add(card, BorderLayout.CENTER);
        }
        void onShow() {
            try { List<Rec> all = store.all(); t.load(all); sub.setText(all.size() + " records. Click a column header to sort."); }
            catch (Exception e) { error(e); }
        }
    }
    void prep(String key) {
        LocalDate t = LocalDate.now();
        switch (key) {
            case "add" -> ((AddScreen) screens.get("add")).form.setRecord(
                    new Rec(111, "Neha Kulkarni", "+91 98765 43210", "neha@example.com", "12 MG Road, Jaipur 302001", "Gitanjali", t, t.plusDays(14)));
            case "update" -> ((UpdateScreen) screens.get("update")).loadId(104);
            case "delete" -> ((DeleteScreen) screens.get("delete")).loadId(105);
            case "search" -> ((SearchScreen) screens.get("search")).q.setText("an");
            default -> {}
        }
    }
    static void snap(PROJECT2 f, File out) throws Exception {
        Container c = f.getContentPane();
        double s = 1.5;
        BufferedImage img = new BufferedImage((int) (c.getWidth() * s), (int) (c.getHeight() * s), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.scale(s, s);
        c.paint(g);
        g.dispose();
        ImageIO.write(img, "png", out);
    }
    static void screenshots(File dir) throws Exception {
        dir.mkdirs();
        PROJECT2[] app = new PROJECT2[1];
        SwingUtilities.invokeAndWait(() -> {
            app[0] = new PROJECT2(new MemoryStore(), true);
            app[0].setSize(1240, 820); app[0].setLocation(0, 0); app[0].setVisible(true);
        });
        String[] keys = {"dashboard", "add", "update", "delete", "search", "records"};
        for (int i = 0; i < keys.length; i++) {
            String k = keys[i];
            File out = new File(dir, (i + 1) + "-" + k + ".png");
            SwingUtilities.invokeAndWait(() -> { app[0].go(k); app[0].prep(k); });
            Thread.sleep(500);
            SwingUtilities.invokeAndWait(() -> { try { snap(app[0], out); } catch (Exception e) { throw new RuntimeException(e); } });
        }
        System.exit(0);
    }
    public static void main(String[] args) throws Exception {
        System.setProperty("swing.aatext", "true");
        List<String> a = Arrays.asList(args);
        if (a.contains("--screenshots")) { screenshots(new File(a.get(a.indexOf("--screenshots") + 1))); return; }
        boolean demo = a.contains("--demo");
        Store s;
        try { s = demo ? new MemoryStore() : new MySqlStore(); }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not connect to MySQL:\n" + e.getMessage()
                    + "\n\nRun with --demo to try the app with sample data.", "Connection failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Store st = s;
        SwingUtilities.invokeLater(() -> new PROJECT2(st, demo).setVisible(true));
    }
}
