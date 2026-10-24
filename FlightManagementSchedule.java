import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class FlightManagementSchedule extends JFrame {

    // Configure these with environment variables, or edit the defaults for your machine.
    private static final String DB_URL  = System.getenv().getOrDefault(
            "FLIGHT_DB_URL", "jdbc:mysql://localhost:3306/flight_db");
    private static final String DB_USER = System.getenv().getOrDefault(
            "FLIGHT_DB_USER", "root");
    private static final String DB_PASS = System.getenv().getOrDefault(
            "FLIGHT_DB_PASS", "");

    // Dark aviation palette (same as original)
    private static final Color BG_MAIN      = new Color(10, 16, 30);
    private static final Color BG_PANEL     = new Color(18, 27, 48);
    private static final Color BG_CARD      = new Color(24, 36, 60);
    private static final Color COLOR_ACCENT = new Color(251, 191, 36);
    private static final Color COLOR_BLUE   = new Color(56, 189, 248);
    private static final Color COLOR_GREEN  = new Color(52, 211, 153);
    private static final Color COLOR_GRAY   = new Color(148, 163, 184);
    private static final Color COLOR_HEADER = new Color(226, 232, 240);
    private static final Color COLOR_BORDER = new Color(30, 46, 80);
    private static final Color CONFIRMED_BG = new Color(6, 78, 59);
    private static final Color CONFIRMED_FG = new Color(110, 231, 183);
    private static final Color NOTCONF_BG   = new Color(69, 26, 3);
    private static final Color NOTCONF_FG   = new Color(252, 211, 77);
    private static final Color ROW_ALT      = new Color(22, 33, 58);
    private static final Color HEADER_BG    = new Color(14, 22, 40);
    private static final Color HEADER_FG    = new Color(251, 191, 36);

    private static final String[] COLUMNS = {
        "Name", "Date", "Time", "From", "To", "Flight No", "Status"
    };

    private JTextField flightNoField;
    private JComboBox<String> fromCombo, toCombo, statusCombo;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel statusBar;

    // Date & Time formatters (non‑lenient for strict validation)
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    static {
        DATE_FORMAT.setLenient(false);
        TIME_FORMAT.setLenient(false);
    }

    public FlightManagementSchedule() {
        setTitle("Flight Management Schedule");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 620);
        setMinimumSize(new Dimension(820, 500));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 8));
        root.setBackground(BG_MAIN);
        root.setBorder(BorderFactory.createEmptyBorder(18, 22, 10, 22));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(BG_MAIN);
        topPanel.add(buildHeader());
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(buildFilters());
        topPanel.add(Box.createVerticalStrut(8));

        statusBar = new JLabel("  Connecting to database...");
        statusBar.setFont(new Font("Courier New", Font.PLAIN, 11));
        statusBar.setForeground(COLOR_GRAY);
        statusBar.setBackground(new Color(14, 22, 40));
        statusBar.setOpaque(true);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER),
            BorderFactory.createEmptyBorder(5, 6, 5, 6)
        ));

        root.add(topPanel, BorderLayout.NORTH);
        root.add(buildTable(), BorderLayout.CENTER);
        root.add(statusBar, BorderLayout.SOUTH);
        setContentPane(root);
        getContentPane().setBackground(BG_MAIN);
        setVisible(true);
        loadTableData("SELECT * FROM flights");
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_MAIN);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JPanel titleBlock = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleBlock.setBackground(BG_MAIN);
        JLabel icon = new JLabel("✈");
        icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 26));
        icon.setForeground(COLOR_ACCENT);
        JLabel title = new JLabel("FLIGHT MANAGEMENT SCHEDULE");
        title.setFont(new Font("Courier New", Font.BOLD, 20));
        title.setForeground(COLOR_ACCENT);
        titleBlock.add(icon);
        titleBlock.add(title);

        JButton refreshBtn = styledButton("Refresh", COLOR_BLUE);
        refreshBtn.addActionListener(e -> {
            resetAll();
            setStatus("  Data refreshed from database.", false);
        });
        p.add(titleBlock, BorderLayout.WEST);
        p.add(refreshBtn, BorderLayout.EAST);
        return p;
    }

    private JPanel buildFilters() {
        JPanel outer = new JPanel();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setBackground(BG_CARD);
        outer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, COLOR_BORDER),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        outer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row1.setBackground(BG_CARD);
        JLabel lbl = new JLabel("FLIGHT NO:");
        lbl.setFont(new Font("Courier New", Font.BOLD, 12));
        lbl.setForeground(COLOR_ACCENT);
        flightNoField = new JTextField(16);
        flightNoField.setFont(new Font("Courier New", Font.PLAIN, 13));
        styleField(flightNoField);
        flightNoField.addActionListener(e -> applyFilter());
        JButton searchBtn = styledButton("Search", COLOR_GREEN);
        JButton resetBtn  = styledButton("Reset", COLOR_GRAY);
        searchBtn.addActionListener(e -> applyFilter());
        resetBtn.addActionListener(e -> resetAll());
        row1.add(lbl);
        row1.add(flightNoField);
        row1.add(searchBtn);
        row1.add(resetBtn);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row2.setBackground(BG_CARD);
        row2.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        fromCombo = styledCombo(new String[]{"All From", "USA", "Japan", "Philippines", "Australia"});
        toCombo   = styledCombo(new String[]{"All To", "Japan", "Australia", "Philippines", "Korea", "USA"});
        statusCombo = styledCombo(new String[]{"All Status", "Confirmed", "Not Confirmed"});

        JButton applyBtn  = styledButton("Apply Filter", COLOR_BLUE);
        JButton addBtn    = styledButton("Add Flight", COLOR_GREEN);
        JButton editBtn   = styledButton("Edit Flight", COLOR_ACCENT);
        JButton cancelBtn = styledButton("Cancel Flight", new Color(248, 113, 113));

        applyBtn.addActionListener(e -> applyFilter());
        addBtn.addActionListener(e -> addFlight());
        editBtn.addActionListener(e -> editFlight());
        cancelBtn.addActionListener(e -> cancelFlight());

        row2.add(fromCombo);
        row2.add(toCombo);
        row2.add(statusCombo);
        row2.add(applyBtn);
        row2.add(addBtn);
        row2.add(editBtn);
        row2.add(cancelBtn);

        outer.add(row1);
        outer.add(row2);
        return outer;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(BG_PANEL);
        table.setSelectionBackground(new Color(30, 64, 110));
        table.setSelectionForeground(new Color(226, 232, 240));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lbl = new JLabel(value == null ? "" : value.toString());
                lbl.setFont(new Font("Courier New", Font.BOLD, 12));
                lbl.setBackground(HEADER_BG);
                lbl.setForeground(HEADER_FG);
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(251, 191, 36, 60)),
                    BorderFactory.createEmptyBorder(0, 12, 0, 5)
                ));
                lbl.setPreferredSize(new Dimension(lbl.getPreferredSize().width, 40));
                return lbl;
            }
        });
        header.setReorderingAllowed(false);
        header.setBackground(HEADER_BG);

        int[] widths = {160, 110, 75, 125, 125, 85, 140};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        table.setDefaultRenderer(Object.class, new AlternatingRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new StatusRenderer());

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, COLOR_BORDER));
        sp.getViewport().setBackground(BG_PANEL);
        return sp;
    }

    // ---------- VALIDATION HELPERS ----------
    private boolean isValidDate(String dateStr) {
        if (dateStr == null) return false;
        try {
            DATE_FORMAT.parse(dateStr.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidTime(String timeStr) {
        if (timeStr == null) return false;
        try {
            TIME_FORMAT.parse(timeStr.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean flightNoExists(String flightNo) throws SQLException {
        String sql = "SELECT 1 FROM flights WHERE flight_no = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, flightNo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    // ---------- ADD FLIGHT (NEW FEATURE) ----------
    private void addFlight() {
        JTextField nameField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField fromField = new JTextField();
        JTextField toField = new JTextField();
        JTextField flightField = new JTextField();
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Confirmed", "Not Confirmed"});

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Passenger Name *"));
        panel.add(nameField);
        panel.add(new JLabel("Date (YYYY-MM-DD) *"));
        panel.add(dateField);
        panel.add(new JLabel("Time (HH:MM, 24h) *"));
        panel.add(timeField);
        panel.add(new JLabel("Origin *"));
        panel.add(fromField);
        panel.add(new JLabel("Destination *"));
        panel.add(toField);
        panel.add(new JLabel("Flight Number *"));
        panel.add(flightField);
        panel.add(new JLabel("Status *"));
        panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Flight", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        String name = nameField.getText().trim();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String origin = fromField.getText().trim();
        String dest = toField.getText().trim();
        String flightNo = flightField.getText().trim();
        String status = (String) statusBox.getSelectedItem();

        // Basic empty checks
        if (name.isEmpty() || date.isEmpty() || time.isEmpty() || origin.isEmpty() || dest.isEmpty() || flightNo.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        // Date validation
        if (!isValidDate(date)) {
            showError("Invalid date format. Use YYYY-MM-DD (e.g., 2026-05-30).");
            return;
        }

        // Time validation (24h)
        if (!isValidTime(time)) {
            showError("Invalid time format. Use HH:MM in 24-hour format (00:00 - 23:59).");
            return;
        }

        // Duplicate flight number
        try {
            if (flightNoExists(flightNo)) {
                showError("Flight number '" + flightNo + "' already exists. Use a unique flight number.");
                return;
            }
        } catch (SQLException e) {
            showError("Database error while checking duplicate flight: " + e.getMessage());
            return;
        }

        String sql = "INSERT INTO flights (name, date, time, origin, destination, flight_no, status) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, date);
            stmt.setString(3, time);
            stmt.setString(4, origin);
            stmt.setString(5, dest);
            stmt.setString(6, flightNo);
            stmt.setString(7, status);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Flight added successfully!");
            loadTableData("SELECT * FROM flights");
            setStatus("  New flight added: " + flightNo, false);
        } catch (SQLException e) {
            showError("Failed to add flight:\n" + e.getMessage());
        }
    }

    // ---------- EDIT FLIGHT (with validation) ----------
    private void editFlight() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight first.", "No Flight Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String oldFlightNo = tableModel.getValueAt(row, 5).toString();
        JTextField nameField = new JTextField(tableModel.getValueAt(row, 0).toString());
        JTextField dateField = new JTextField(tableModel.getValueAt(row, 1).toString());
        JTextField timeField = new JTextField(tableModel.getValueAt(row, 2).toString());
        JTextField fromField = new JTextField(tableModel.getValueAt(row, 3).toString());
        JTextField toField = new JTextField(tableModel.getValueAt(row, 4).toString());
        JTextField flightField = new JTextField(oldFlightNo);
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Confirmed", "Not Confirmed"});
        statusBox.setSelectedItem(tableModel.getValueAt(row, 6).toString());

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Passenger Name"));
        panel.add(nameField);
        panel.add(new JLabel("Date (YYYY-MM-DD)"));
        panel.add(dateField);
        panel.add(new JLabel("Time (HH:MM, 24h)"));
        panel.add(timeField);
        panel.add(new JLabel("Origin"));
        panel.add(fromField);
        panel.add(new JLabel("Destination"));
        panel.add(toField);
        panel.add(new JLabel("Flight Number"));
        panel.add(flightField);
        panel.add(new JLabel("Status"));
        panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Flight", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        String newName = nameField.getText().trim();
        String newDate = dateField.getText().trim();
        String newTime = timeField.getText().trim();
        String newOrigin = fromField.getText().trim();
        String newDest = toField.getText().trim();
        String newFlightNo = flightField.getText().trim();
        String newStatus = (String) statusBox.getSelectedItem();

        if (newName.isEmpty() || newDate.isEmpty() || newTime.isEmpty() || newOrigin.isEmpty() || newDest.isEmpty() || newFlightNo.isEmpty()) {
            showError("All fields must be filled.");
            return;
        }
        if (!isValidDate(newDate)) {
            showError("Invalid date. Use YYYY-MM-DD.");
            return;
        }
        if (!isValidTime(newTime)) {
            showError("Invalid time. Use HH:MM in 24-hour format (00:00-23:59).");
            return;
        }

        // Check duplicate flight number only if changed
        if (!newFlightNo.equals(oldFlightNo)) {
            try {
                if (flightNoExists(newFlightNo)) {
                    showError("Flight number '" + newFlightNo + "' already exists. Cannot change to it.");
                    return;
                }
            } catch (SQLException e) {
                showError("Database error: " + e.getMessage());
                return;
            }
        }

        String sql = "UPDATE flights SET name=?, date=?, time=?, origin=?, destination=?, flight_no=?, status=? WHERE flight_no=?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, newDate);
            stmt.setString(3, newTime);
            stmt.setString(4, newOrigin);
            stmt.setString(5, newDest);
            stmt.setString(6, newFlightNo);
            stmt.setString(7, newStatus);
            stmt.setString(8, oldFlightNo);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Flight updated successfully!");
            loadTableData("SELECT * FROM flights");
            setStatus("  Flight " + oldFlightNo + " updated.", false);
        } catch (SQLException e) {
            showError("Error updating flight:\n" + e.getMessage());
        }
    }

    // ---------- CANCEL FLIGHT (delete) ----------
    private void cancelFlight() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight first.", "No Flight Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String flightNo = tableModel.getValueAt(row, 5).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to CANCEL (delete) Flight " + flightNo + "?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM flights WHERE flight_no=?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, flightNo);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Flight cancelled (deleted) successfully!");
            loadTableData("SELECT * FROM flights");
            setStatus("  Flight " + flightNo + " cancelled.", false);
        } catch (SQLException e) {
            showError("Error cancelling flight:\n" + e.getMessage());
        }
    }

    // ---------- DATABASE LOAD & FILTER ----------
    private void loadTableData(String sql) {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            int count = 0;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("name"), rs.getString("date"), rs.getString("time"),
                    rs.getString("origin"), rs.getString("destination"),
                    rs.getString("flight_no"), rs.getString("status")
                });
                count++;
            }
            setStatus("  " + count + " record(s) loaded.", false);
        } catch (SQLException e) {
            setStatus("  DB Error: " + e.getMessage(), true);
            JOptionPane.showMessageDialog(this, "Database error:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilter() {
        String flightNo = flightNoField.getText().trim();
        String from = (String) fromCombo.getSelectedItem();
        String to = (String) toCombo.getSelectedItem();
        String status = (String) statusCombo.getSelectedItem();
        StringBuilder sql = new StringBuilder("SELECT * FROM flights WHERE 1=1");
        List<String> params = new ArrayList<>();
        if (!flightNo.isEmpty()) {
            sql.append(" AND flight_no LIKE ?");
            params.add("%" + flightNo + "%");
        }
        if (!"All From".equals(from)) {
            sql.append(" AND origin = ?");
            params.add(from);
        }
        if (!"All To".equals(to)) {
            sql.append(" AND destination = ?");
            params.add(to);
        }
        if (!"All Status".equals(status)) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) stmt.setString(i + 1, params.get(i));
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("name"), rs.getString("date"), rs.getString("time"),
                    rs.getString("origin"), rs.getString("destination"),
                    rs.getString("flight_no"), rs.getString("status")
                });
                count++;
            }
            setStatus("  Filter applied — " + count + " record(s) found.", false);
        } catch (SQLException e) {
            setStatus("  DB Error: " + e.getMessage(), true);
        }
    }

    private void resetAll() {
        flightNoField.setText("");
        fromCombo.setSelectedIndex(0);
        toCombo.setSelectedIndex(0);
        statusCombo.setSelectedIndex(0);
        loadTableData("SELECT * FROM flights");
    }

    private void setStatus(String msg, boolean isError) {
        statusBar.setText(msg);
        statusBar.setForeground(isError ? new Color(248, 113, 113) : COLOR_GRAY);
    }

    // ---------- UI STYLING HELPERS ----------
    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(new Color(10, 16, 30));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(text.length() * 9 + 22, 32));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBackground(BG_CARD);
        combo.setForeground(COLOR_HEADER);
        combo.setPreferredSize(new Dimension(145, 32));
        return combo;
    }

    private void styleField(JTextField f) {
        f.setBackground(BG_CARD);
        f.setForeground(COLOR_HEADER);
        f.setCaretColor(COLOR_ACCENT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        f.setPreferredSize(new Dimension(180, 32));
    }

    // ---------- RENDERERS ----------
    class AlternatingRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 10));
            if (!sel) {
                setBackground(row % 2 == 0 ? BG_PANEL : ROW_ALT);
                setForeground(COLOR_HEADER);
            }
            if (col == 5) {
                setFont(new Font("Courier New", Font.BOLD, 13));
                if (!sel) setForeground(COLOR_ACCENT);
            } else {
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
            }
            return this;
        }
    }

    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            JLabel lbl = new JLabel(v == null ? "" : v.toString());
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setOpaque(true);
            lbl.setHorizontalAlignment(CENTER);
            lbl.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
            boolean confirmed = "Confirmed".equals(v);
            lbl.setBackground(sel ? new Color(30, 64, 110) : (confirmed ? CONFIRMED_BG : NOTCONF_BG));
            lbl.setForeground(sel ? COLOR_HEADER : (confirmed ? CONFIRMED_FG : NOTCONF_FG));
            return lbl;
        }
    }

    // ---------- MAIN ----------
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found!\nAdd mysql-connector-j.jar to classpath.", "Missing Driver", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(FlightManagementSchedule::new);
    }
}