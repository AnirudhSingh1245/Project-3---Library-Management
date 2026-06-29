import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;                                          //Library Management System using Java and MySQL
public class Project3 {
    public static void main(String arg[]){
        try{
            //sql connection
            String url="jdbc:mysql://localhost:3306/";
            String username="root";
            String password="happy";
            Connection c=DriverManager.getConnection(url,username,password);
            Statement s=c.createStatement();
            s.executeUpdate("use Library");
            String blank="";

            //--MENU--
            //creating frame
            JFrame menuframe=new JFrame();
            menuframe.setLayout(null);
            menuframe.setExtendedState(menuframe.MAXIMIZED_BOTH);
            menuframe.setTitle("Library");

            //setting the icon
            ImageIcon imageicon = new ImageIcon(Project3.class.getResource("Bookshelf.png"));
            menuframe.setIconImage(imageicon.getImage());

            //Heading
            JLabel menu=new JLabel("Library Book Issue Management");
            menu.setFont(new Font(Font.SERIF,Font.BOLD,50));
            menu.setBounds(410,10,1000,70);
         //   menu.setHorizontalAlignment(menu.CENTER);
          //  menu.setVerticalAlignment(menu.TOP);

            //text in additional
            JLabel text=new JLabel("There are some buttons, use it for manage the data:-");
            text.setFont(new Font(Font.SERIF,Font.PLAIN,30));
            text.setBounds(450,80,1000,40);

            //Making menu with buttons
            JButton addbutton=new JButton();
            JButton updatebutton=new JButton();
            JButton deletebutton=new JButton();
            JButton searchbutton=new JButton();

            // 1. ADDING DATA
            addbutton.setText("Insert Data");
            addbutton.setBounds(500,200,500,50);
            addbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource()==addbutton){
                        try{
                            //creating Frame
                            JFrame addframe=new JFrame();
                            addframe.setLayout(null);
                            addframe.setExtendedState(addframe.MAXIMIZED_BOTH);
                            addframe.setTitle("Inserting Data");
                            addframe.setBackground(Color.GRAY);

                            //setting the icon
                            ImageIcon imageicon = new ImageIcon(getClass().getResource("Bookshelf.png"));
                            addframe.setIconImage(imageicon.getImage());

                            //Heading
                            JLabel insert=new JLabel();
                            insert.setBounds(500,0,1000,80);
                            insert.setText("Insertion of data");
                            insert.setFont(new Font(Font.SERIF,Font.PLAIN,50));

                            //labels
                            //id
                            JLabel id=new JLabel();
                            id.setText("Enter ID: ");
                            id.setBounds(20,200,1000,50);
                            id.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
                            //name
                            JLabel name=new JLabel();
                            name.setText("Enter Name: ");
                            name.setBounds(20,250,1000,50);
                            name.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
                            //book
                            JLabel book=new JLabel();
                            book.setText("Enter Book Name: ");
                            book.setBounds(20,300,1000,50);
                            book.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
                            //date1
                            JLabel date1=new JLabel();
                            date1.setText("Enter Date of Issue: ");
                            date1.setBounds(20,350,1000,50);
                            date1.setFont(new Font(Font.DIALOG,Font.PLAIN,25));
                            //date2
                            JLabel date2=new JLabel();
                            date2.setText("Enter Date of Return: ");
                            date2.setBounds(20,400,1000,50);
                            date2.setFont(new Font(Font.DIALOG,Font.PLAIN,25));

                            //textfields
                            //ID
                            JTextField ID=new JTextField();
                            ID.setBounds(260,210,300,30);
                            //NAME
                            JTextField NAME=new JTextField();
                            NAME.setBounds(260,260,300,30);
                            //BOOK
                            JTextField BOOK=new JTextField();
                            BOOK.setBounds(260,310,300,30);
                            //DATE1
                            JTextField DATE1=new JTextField();
                            DATE1.setBounds(260,360,300,30);
                            //DATE2
                            JTextField DATE2=new JTextField();
                            DATE2.setBounds(260,410,300,30);

                            //button
                            JButton submit=new JButton();
                            submit.setText("Submit");
                            submit.setBounds(30,470,100,40);
                            //adding actionlistener in submit button
                            submit.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if(e.getSource()==submit){
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
                                        ID.setText(blank);
                                        NAME.setText(blank);
                                        BOOK.setText(blank);
                                        DATE1.setText(blank);
                                        DATE2.setText(blank);
                                    }

                                }
                            });

                            //adding in the frame
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
                            addframe.setVisible(true);      //for making frame visible
                            addframe.setDefaultCloseOperation(addframe.EXIT_ON_CLOSE);
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            });


            // 2. UPDATE DATA
            updatebutton.setText("Update Data");
            updatebutton.setBounds(500,300,500,50);
            updatebutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource()==updatebutton){
                        try{
                            //creating Frame
                            JFrame updateframe=new JFrame();
                            updateframe.setLayout(null);
                            updateframe.setExtendedState(updateframe.MAXIMIZED_BOTH);
                            updateframe.setTitle("Updating Data");
                            updateframe.setBackground(Color.GRAY);

                            //setting the icon
                            ImageIcon imageicon = new ImageIcon(getClass().getResource("Bookshelf.png"));
                            updateframe.setIconImage(imageicon.getImage());

                            //Heading
                            JLabel update=new JLabel();
                            update.setBounds(500,0,1000,80);
                            update.setText("Updation of data");
                            update.setFont(new Font(Font.SERIF,Font.PLAIN,50));

                            //to make frame visible
                            updateframe.add(update);
                            updateframe.setVisible(true);
                            updateframe.setDefaultCloseOperation(updateframe.EXIT_ON_CLOSE);
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            });


            //  3. DELETE DATA
            deletebutton.setBounds(500,400,500,50);
            deletebutton.setText("Delete Data");
            deletebutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource()==deletebutton){
                        try{
                            //creating frame
                            JFrame deleteframe=new JFrame();
                            deleteframe.setLayout(null);
                            deleteframe.setTitle("Deleting Data");
                            deleteframe.setExtendedState(deleteframe.MAXIMIZED_BOTH);

                            //Imageicon
                            ImageIcon imageIcon=new ImageIcon(getClass().getResource("Bookshelf.png"));
                            deleteframe.setIconImage(imageIcon.getImage());

                            //label
                            JLabel delete=new JLabel();
                            JLabel deleteInput=new JLabel();
                            delete.setText("Deletion of Data");
                            delete.setFont(new Font(Font.SERIF,Font.PLAIN,50));
                            delete.setBounds(550,10,1000,80);
                            deleteInput.setBounds(450,350,1000,50);    //taking input for delete
                            deleteInput.setText("Enter the ID: ");
                            deleteInput.setFont(new Font(Font.DIALOG,Font.PLAIN,25));

                            //textfield
                            JTextField deletetextfield=new JTextField();
                            deletetextfield.setBounds(600,365,400,30);

                            // submit button
                            JButton submit=new JButton();
                            submit.setText("Submit");
                            submit.setBounds(700,450,100,40);

                            //adding actionlistener in submit button
                            submit.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if(e.getSource()==submit){
                                        try{
                                            s.executeUpdate("delete from lib where id="+deletetextfield.getText());
                                            JOptionPane.showMessageDialog(null,"Delete Data Successfully");

                                        }
                                        catch (SQLException ex){
                                            ex.printStackTrace();
                                        }
                                        deletetextfield.setText(blank);
                                    }
                                }
                            });

                            //adding in frame
                            deleteframe.add(delete);
                            deleteframe.add(deleteInput);
                            deleteframe.add(deletetextfield);
                            deleteframe.add(submit);

                            //to make frame visible
                            deleteframe.setVisible(true);
                            deleteframe.setDefaultCloseOperation(deleteframe.EXIT_ON_CLOSE);
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            });


            // 4.SEARCH DATA
            searchbutton.setBounds(500,500,500,50);
            searchbutton.setText("Search Data");
            searchbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource()==searchbutton){
                        try{
                            //to create frame
                            JFrame searchframe=new JFrame();
                            searchframe.setTitle("Searching Data");
                            searchframe.setLayout(null);
                            searchframe.setExtendedState(searchframe.MAXIMIZED_BOTH);

                            //image icon
                            ImageIcon imageicon=new ImageIcon(getClass().getResource("Bookshelf.png"));
                            searchframe.setIconImage(imageicon.getImage());

                            //label
                            JLabel search=new JLabel();
                            JLabel searchinput=new JLabel();
                            search.setText("Searching Data");
                            search.setBounds(600,10,1000,60);
                            search.setFont(new Font(Font.SERIF,Font.PLAIN,50));
                            searchinput.setBounds(450,350,1000,50);    //taking input for delete
                            searchinput.setText("Enter the ID: ");
                            searchinput.setFont(new Font(Font.DIALOG,Font.PLAIN,25));

                            //textfield
                            JTextField searchtextfield=new JTextField();
                            searchtextfield.setBounds(600,365,400,30);

                            // submit button
                            JButton submit=new JButton();
                            submit.setText("Submit");
                            submit.setBounds(700,450,100,40);
                            submit.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if(e.getSource()==submit){
                                        try {
                                            searchinput.setVisible(false);
                                            searchtextfield.setVisible(false);
                                            submit.setVisible(false);
                                            JLabel id=new JLabel();
                                            s.executeUpdate("Select * from lib where id="+searchtextfield+";");
                                        }
                                        catch (SQLException ex){
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            });

                            //adding in frame
                            searchframe.add(search);
                            searchframe.add(searchinput);
                            searchframe.add(searchtextfield);
                            searchframe.add(submit);

                            //to make frame visible
                            searchframe.setVisible(true);
                            searchframe.setDefaultCloseOperation(searchframe.EXIT_ON_CLOSE);
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            });

            //Adding in menuframe
            menuframe.add(menu);
            menuframe.add(text);
            menuframe.add(addbutton);
            menuframe.add(deletebutton);
            menuframe.add(updatebutton);
            menuframe.add(searchbutton);

            //to make frame visible
            menuframe.setVisible(true);
            menuframe.setDefaultCloseOperation(menuframe.EXIT_ON_CLOSE);
        }
        catch (Exception e){
            e.printStackTrace();        //Exception if 'try' doesn't work
        }
    }
}

