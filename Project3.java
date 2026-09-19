package question.company;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PROJECT2 {

    //fixed colors used on every window - dark background/buttons, light text
    static Color BG_COLOR = new Color(20, 30, 65);
    static Color BUTTON_COLOR = new Color(40, 50, 95);
    static Color TEXT_COLOR = Color.WHITE;
    static Color BUTTON_HOVER_COLOR = new Color(65, 80, 150);

    //gives a button padding, a border, a bold font, no focus box, and a hover highlight
    static void styleButton(JButton b){
        b.setBackground(BUTTON_COLOR);
        b.setForeground(TEXT_COLOR);
        b.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 120, 200), 2, true),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        b.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                b.setBackground(BUTTON_HOVER_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e){
                b.setBackground(BUTTON_COLOR);
            }
        });
    }
    public static void main(String arg[]){
        try{
            //sql connection (shared by every window)
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","happy");
            Statement s=c.createStatement();
            s.executeUpdate("use Library");
            int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;

            //--MENU--
            JFrame menuframe=new JFrame();
            menuframe.setLayout(null);
            menuframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
            menuframe.setTitle("Library");
            menuframe.getContentPane().setBackground(BG_COLOR);
            ImageIcon imageicon = new ImageIcon(PROJECT2.class.getResource("Bookshelf.png"));
            menuframe.setIconImage(imageicon.getImage());

            JLabel menu=new JLabel("Library Book Issue Management");
            menu.setFont(new Font(Font.SERIF,Font.BOLD,50));
            menu.setBounds(0,10,screenWidth,70);
            menu.setForeground(TEXT_COLOR);
            menu.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel text=new JLabel("There are some buttons, use it for manage the data:-");
            text.setFont(new Font(Font.SERIF,Font.PLAIN,30));
            text.setBounds(0,85,screenWidth,40);
            text.setForeground(TEXT_COLOR);
            text.setHorizontalAlignment(SwingConstants.CENTER);

            JButton addbutton=new JButton("Insert Data");
            JButton updatebutton=new JButton("Update Data");
            JButton deletebutton=new JButton("Delete Data");
            JButton searchbutton=new JButton("Search Data");
            JButton view = new JButton("View All Records");
            for(JButton b : new JButton[]{addbutton, updatebutton, deletebutton, searchbutton, view}){
                styleButton(b);
            }

            // 1. ADDING DATA
            addbutton.setBounds(screenWidth/2-250,200,500,50);
            addbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame addframe=new JFrame();
                    addframe.setLayout(null);
                    addframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    addframe.setTitle("Inserting Data");
                    addframe.getContentPane().setBackground(BG_COLOR);
                    addframe.setIconImage(imageicon.getImage());

                    JLabel insert=new JLabel("Insertion of data");
                    insert.setBounds(500,0,1000,80);
                    insert.setFont(new Font(Font.SERIF,Font.PLAIN,50));
                    insert.setForeground(TEXT_COLOR);

                    //labels
                    JLabel id=new JLabel("Enter ID: ");
                    id.setBounds(20,200,1000,50);
                    id.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
                    id.setForeground(TEXT_COLOR);
                    JLabel name=new JLabel("Enter Name: ");
                    name.setBounds(20,250,1000,50);
                    name.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
                    name.setForeground(TEXT_COLOR);
                    JLabel book=new JLabel("Enter Book Name: ");
                    book.setBounds(20,300,1000,50);
                    book.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
                    book.setForeground(TEXT_COLOR);
                    JLabel date1=new JLabel("Enter Date of Issue: ");
                    date1.setBounds(20,350,1000,50);
                    date1.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
                    date1.setForeground(TEXT_COLOR);
                    JLabel date2=new JLabel("Enter Date of Return: ");
                    date2.setBounds(20,400,1000,50);
                    date2.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
                    date2.setForeground(TEXT_COLOR);

                    //textfields
                    JTextField ID=new JTextField();
                    ID.setBounds(260,210,300,30);
                    ID.setBackground(BUTTON_COLOR);
                    ID.setForeground(TEXT_COLOR);
                    ID.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    JTextField NAME=new JTextField();
                    NAME.setBounds(260,260,300,30);
                    NAME.setBackground(BUTTON_COLOR);
                    NAME.setForeground(TEXT_COLOR);
                    NAME.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    JTextField BOOK=new JTextField();
                    BOOK.setBounds(260,310,300,30);
                    BOOK.setBackground(BUTTON_COLOR);
                    BOOK.setForeground(TEXT_COLOR);
                    BOOK.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    JTextField DATE1=new JTextField();
                    DATE1.setBounds(260,360,300,30);
                    DATE1.setBackground(BUTTON_COLOR);
                    DATE1.setForeground(TEXT_COLOR);
                    DATE1.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    JTextField DATE2=new JTextField();
                    DATE2.setBounds(260,410,300,30);
                    DATE2.setBackground(BUTTON_COLOR);
                    DATE2.setForeground(TEXT_COLOR);
                    DATE2.setBorder(BorderFactory.createLineBorder(Color.WHITE));

                    JButton submit=new JButton("Submit");
                    submit.setBounds(30,470,140,45);
                    styleButton(submit);
                    submit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                JOptionPane.showMessageDialog(null,"Insert Data Successfully");
                                s.executeUpdate("insert into lib values(" + ID.getText() +
                                        ",'" + NAME.getText() +
                                        "','" + BOOK.getText() +
                                        "','" + DATE1.getText() +
                                        "','" + DATE2.getText() + "')");
                            }
                            catch (SQLException ex){
                                ex.printStackTrace();
                            }
                            ID.setText("");
                            NAME.setText("");
                            BOOK.setText("");
                            DATE1.setText("");
                            DATE2.setText("");
                        }
                    });

                    //back button - returns to main menu
                    JButton back=new JButton("Back");
                    back.setBounds(190,470,140,45);
                    styleButton(back);
                    back.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            addframe.dispose();
                        }
                    });

                    addframe.add(insert);
                    addframe.add(id);
                    addframe.add(name);
                    addframe.add(book);
                    addframe.add(date1);
                    addframe.add(date2);
                    addframe.add(ID);
                    addframe.add(NAME);
                    addframe.add(BOOK);
                    addframe.add(DATE1);
                    addframe.add(DATE2);
                    addframe.add(submit);
                    addframe.add(back);

                    //hide the menu while this window is open, bring it back when closed (Back button or X)
                    menuframe.setVisible(false);
                    addframe.setVisible(true);
                    addframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    addframe.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            menuframe.setVisible(true);
                        }
                    });
                }
            });

            // 2. UPDATE DATA
            updatebutton.setBounds(screenWidth/2-250,300,500,50);
            updatebutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame updateframe = new JFrame();
                    updateframe.setSize(500, 500);
                    updateframe.setLayout(null);

                    JLabel updateheading = new JLabel("Updation of Data");
                    updateheading.setBounds(170, 30, 200, 30);
                    updateframe.add(updateheading);

                    JLabel updateid = new JLabel("Enter ID:");
                    updateid.setBounds(50, 80, 100, 30);
                    updateframe.add(updateid);
                    JTextField updateidtextfield = new JTextField();
                    updateidtextfield.setBounds(160, 80, 200, 30);
                    updateframe.add(updateidtextfield);

                    JLabel updatename = new JLabel("Name:");
                    updatename.setBounds(50, 130, 100, 30);
                    updateframe.add(updatename);
                    JTextField updatenametextfield = new JTextField();
                    updatenametextfield.setBounds(160, 130, 200, 30);
                    updateframe.add(updatenametextfield);

                    JLabel updatebook = new JLabel("Book:");
                    updatebook.setBounds(50, 180, 100, 30);
                    updateframe.add(updatebook);
                    JTextField updatebooktextfield = new JTextField();
                    updatebooktextfield.setBounds(160, 180, 200, 30);
                    updateframe.add(updatebooktextfield);

                    JLabel updatedate1 = new JLabel("Issue Date:");
                    updatedate1.setBounds(50, 230, 100, 30);
                    updateframe.add(updatedate1);
                    JTextField updatedate1textfield = new JTextField();
                    updatedate1textfield.setBounds(160, 230, 200, 30);
                    updateframe.add(updatedate1textfield);

                    JLabel updatedate2 = new JLabel("Return Date:");
                    updatedate2.setBounds(50, 280, 100, 30);
                    updateframe.add(updatedate2);
                    JTextField updatedate2textfield = new JTextField();
                    updatedate2textfield.setBounds(160, 280, 200, 30);
                    updateframe.add(updatedate2textfield);

                    JButton update = new JButton("Update");
                    update.setBounds(180, 340, 120, 40);
                    updateframe.add(update);
                    update.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                s.executeUpdate("update lib set Name='" + updatenametextfield.getText() +
                                        "', book='" + updatebooktextfield.getText() +
                                        "', Date_of_issue='" + updatedate1textfield.getText() +
                                        "', Date_of_returning='" + updatedate2textfield.getText() +
                                        "' where id=" + updateidtextfield.getText());
                                JOptionPane.showMessageDialog(null, "Data Updated Successfully");
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                            }
                        }
                    });

                    updateframe.setVisible(true);
                }
            });

            // 3. DELETE DATA
            deletebutton.setBounds(screenWidth/2-250,400,500,50);
            deletebutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame deleteframe=new JFrame();
                    deleteframe.setLayout(null);
                    deleteframe.setTitle("Deleting Data");
                    deleteframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    deleteframe.getContentPane().setBackground(BG_COLOR);
                    deleteframe.setIconImage(imageicon.getImage());

                    JLabel delete=new JLabel("Deletion of Data");
                    delete.setFont(new Font(Font.SERIF,Font.PLAIN,50));
                    delete.setBounds(550,10,1000,80);
                    delete.setForeground(TEXT_COLOR);
                    JLabel deleteInput=new JLabel("Enter the ID: ");
                    deleteInput.setBounds(450,350,1000,50);
                    deleteInput.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
                    deleteInput.setForeground(TEXT_COLOR);

                    JTextField deletetextfield=new JTextField();
                    deletetextfield.setBounds(600,365,400,30);
                    deletetextfield.setBackground(BUTTON_COLOR);
                    deletetextfield.setForeground(TEXT_COLOR);
                    deletetextfield.setBorder(BorderFactory.createLineBorder(Color.WHITE));

                    JButton submit=new JButton("Submit");
                    submit.setBounds(700,450,140,45);
                    styleButton(submit);
                    submit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try{
                                s.executeUpdate("delete from lib where id="+deletetextfield.getText());
                                JOptionPane.showMessageDialog(null,"Delete Data Successfully");
                            }
                            catch (SQLException ex){
                                ex.printStackTrace();
                            }
                            deletetextfield.setText("");
                        }
                    });

                    //back button - returns to main menu
                    JButton back=new JButton("Back");
                    back.setBounds(500,450,140,45);
                    styleButton(back);
                    back.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            deleteframe.dispose();
                        }
                    });

                    deleteframe.add(delete);
                    deleteframe.add(deleteInput);
                    deleteframe.add(deletetextfield);
                    deleteframe.add(submit);
                    deleteframe.add(back);

                    //hide the menu while this window is open, bring it back when closed (Back button or X)
                    menuframe.setVisible(false);
                    deleteframe.setVisible(true);
                    deleteframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    deleteframe.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            menuframe.setVisible(true);
                        }
                    });
                }
            });

            // 4. SEARCH DATA
            searchbutton.setBounds(screenWidth/2-250,500,500,50);
            searchbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame searchframe = new JFrame();
                    searchframe.setSize(500, 500);
                    searchframe.setLayout(null);

                    JLabel searchheading = new JLabel("Search Data");
                    searchheading.setBounds(190, 40, 150, 30);
                    searchframe.add(searchheading);

                    JLabel searchid = new JLabel("Enter ID:");
                    searchid.setBounds(50, 100, 100, 30);
                    searchframe.add(searchid);

                    JTextField searchtextfield = new JTextField();
                    searchtextfield.setBounds(160, 100, 200, 30);
                    searchframe.add(searchtextfield);

                    JButton search = new JButton("Search");
                    search.setBounds(180, 160, 120, 40);
                    searchframe.add(search);

                    JTextArea searchresult = new JTextArea();
                    searchresult.setBounds(50, 230, 390, 150);
                    searchresult.setEditable(false);
                    searchframe.add(searchresult);

                    search.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                ResultSet rs = s.executeQuery("select * from lib where id=" + searchtextfield.getText());
                                if (rs.next()) {
                                    searchresult.setText(
                                            "ID: " + rs.getInt("id") +
                                                    "\nName: " + rs.getString("Name") +
                                                    "\nBook: " + rs.getString("book") +
                                                    "\nIssue Date: " + rs.getDate("Date_of_issue") +
                                                    "\nReturn Date: " + rs.getDate("Date_of_returning")
                                    );
                                } else {
                                    searchresult.setText("No record found.");
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                            }
                        }
                    });

                    searchframe.setVisible(true);
                }
            });

            // 5. VIEW DATA
            view.setBounds(screenWidth/2-250,600,500,50);
            view.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFrame viewframe = new JFrame("Library");
                    viewframe.setSize(1200, 750);
                    viewframe.setLayout(null);
                    viewframe.getContentPane().setBackground(new Color(15, 30, 65));

                    JLabel heading = new JLabel("All Library Records");
                    heading.setBounds(390, 30, 500, 50);
                    heading.setFont(new Font("Serif", Font.BOLD, 38));
                    heading.setForeground(Color.WHITE);
                    viewframe.add(heading);

                    JLabel subtitle = new JLabel("Here are all the records in the library:");
                    subtitle.setBounds(430, 85, 400, 35);
                    subtitle.setFont(new Font("Serif", Font.ITALIC, 20));
                    subtitle.setForeground(new Color(190, 205, 240));
                    viewframe.add(subtitle);

                    // Table
                    String[] columns = {"ID", "Name", "Book", "Issue Date", "Return Date"};
                    DefaultTableModel model = new DefaultTableModel(columns, 0);
                    JTable table = new JTable(model);
                    table.setFont(new Font("Arial", Font.PLAIN, 16));
                    table.setRowHeight(40);
                    table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
                    table.getTableHeader().setBackground(new Color(45, 65, 130));
                    table.getTableHeader().setForeground(Color.WHITE);
                    table.setBackground(new Color(20, 40, 80));
                    table.setForeground(Color.WHITE);
                    table.setGridColor(new Color(70, 95, 160));
                    table.setSelectionBackground(new Color(65, 85, 150));
                    table.setSelectionForeground(Color.WHITE);

                    JScrollPane scrollPane = new JScrollPane(table);
                    scrollPane.setBounds(50, 140, 1100, 450);
                    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 110, 200), 2));
                    viewframe.add(scrollPane);

                    // Load ALL records into the table
                    try {
                        ResultSet rs = s.executeQuery("SELECT * FROM lib");
                        while (rs.next()) {
                            model.addRow(new Object[]{
                                    rs.getInt("id"),
                                    rs.getString("Name"),
                                    rs.getString("book"),
                                    rs.getDate("Date_of_issue"),
                                    rs.getDate("Date_of_returning")
                            });
                        }
                        rs.close();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(viewframe, "Error: " + ex.getMessage());
                    }

                    JButton back = new JButton("← Back");
                    back.setBounds(500, 620, 200, 50);
                    styleButton(back);
                    back.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            viewframe.dispose();
                        }
                    });
                    viewframe.add(back);

                    viewframe.setLocationRelativeTo(null);
                    viewframe.setVisible(true);
                }
            });

            menuframe.add(menu);
            menuframe.add(text);
            menuframe.add(addbutton);
            menuframe.add(deletebutton);
            menuframe.add(updatebutton);
            menuframe.add(searchbutton);
            menuframe.add(view);

            menuframe.setVisible(true);
            menuframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
