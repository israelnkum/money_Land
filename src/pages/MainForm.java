/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author root
 */
public class MainForm extends javax.swing.JFrame {
 Connection conn;
    ResultSet rs;
    PreparedStatement pst;
     String status;
     String sender_id = "MoneyLandGH";
    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
      
        conn = dbConnection.getConnection();
         txt_SerachAllClient.setBackground(new Color(0,0,0,0));
         txt_searchSendSmsTable.setBackground(new Color(0,0,0,0));
         //txt_recieverName.setVisible(false);
        // txt_recieverPhone.setVisible(false);
         reciever_id.setVisible(false);
         txt_searchSMSlogTable.setBackground(new Color(0,0,0,0));
         txt_SearchMatchesTable.setBackground(new Color(0,0,0,0));
         txt_SearchPledges.setBackground(new Color (0,0,0,0));
        selectAllusers();
         selectAllClients();
           selectAllPledges();
         
    }
    
    
    
    
    /*
    *SENDING SMS ALERT TO A CLIENT
    */
    
    public void sendSMS() throws IOException{
   
     try {
         String API_key = "cdaf1d8b873e8dce08a7";
         String message =txt_msg.getText();
         String phone_number =txt_recieverPhone.getText();
         
         
         System.out.println(API_key);
         System.out.println(message);
         System.out.println(phone_number);
         System.out.println(sender_id);
         /*******************API URL FOR SENDING MESSAGES********/
         URL url = new URL("http://sms.gadeksystems.com/smsapi?key=" + API_key + "&to=" + phone_number + "&msg=" + message + "&sender_id=" + sender_id);
    
     
             /****************API URL TO CHECK BALANCE****************/
                                          //   URL url = new URL("http://sms.gadeksystems.com/api/smsapibalance?key=" + API_key);
                                                
URLConnection connection = url.openConnection();
BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
String inputLine;
inputLine = in.readLine().trim();
                if (inputLine.equals("1000")) {       
                    status ="Message was successfully sent";
               JOptionPane.showMessageDialog(null,"Message sent");
               
               }
                else if (inputLine.equals("1002")) {
                   status ="Error";
               JOptionPane.showMessageDialog(null,"Message not sent");
                } else if (inputLine.equals("1003")) {
                     status ="Error";
               JOptionPane.showMessageDialog(null,"You don't have enough balance");
               
               } else if (inputLine.equals("1004")) {
                JOptionPane.showMessageDialog(null,"Invalid API Key");
               } else if (inputLine.equals("1005")) {
               JOptionPane.showMessageDialog(null,"Phone number not valid");
                } else if (inputLine.equals("1006")) {
                 JOptionPane.showMessageDialog(null,"Invalid Sender ID");
                 } else if (inputLine.equals("1008")) {
                JOptionPane.showMessageDialog(null,"Empty message");
                                                               }
in.close();
     } catch (MalformedURLException ex) {
         Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
     }

    }
    
    
 



    /*
    * Pulling all registered users from the database to the manage users table
    */
    private void selectAllusers(){
    
        try {
        
            
           String sql1="SELECT `id` as 'ID', `firstname` as 'First name', `middle_name` as 'Middle name', `lastname` as 'Last name', `phone` as 'Phone number' FROM `clients` ";           
            pst=conn.prepareStatement(sql1);
            rs=pst.executeQuery();
            tbl_SendSms.setModel(DbUtils.resultSetToTableModel(rs));
            
            
            String smsLog="SELECT `id` as 'ID', `phone` as 'Phone number', `message` as 'Message body', `status` as 'Status', `dates` as 'Date & time', `type` as 'Type', `sender` as 'Sender', `receipient` FROM `sms_messages` ";           
            pst=conn.prepareStatement(smsLog);
            rs=pst.executeQuery();
            tbl_smsLog.setModel(DbUtils.resultSetToTableModel(rs));
                     
            } catch (SQLException e) {
    JOptionPane.showMessageDialog( null, e);
        }
        finally{
        
        try{
         rs.close();
         pst.close();
        }
        catch(Exception e){  
        }}
      }
    
    
    /*
    * Select all Client from the Database 
    */
    
    private void selectAllClients(){
    
        try {
           String sql="SELECT id as 'ID', firstname as 'First name', lastname as 'Last name', phone as 'Phone number', address as 'Address', email as 'E-mail', created_at as 'Created At', updated_at as 'Updated At'  FROM clients";
           //id`, `code`, `firstname`, `middle_name`, `lastname`, `gender`, `phone`, `email`, ``, `date_joined`, , ``, `
            pst=conn.prepareStatement(sql);
            
            rs=pst.executeQuery();
            
            tbl_ManageClients.setModel(DbUtils.resultSetToTableModel(rs));
            
            //SELECT `id`, `pledge`, `client`, `receiver_name`, `mobile_account_type`, `mobile_money_no`, `amount`, `type`, `confirmed`, `created_at`, `updated_at` FROM `matches`
      String selectPledges="SELECT `id` as 'ID', `pledge` as 'Pledge', `client` as 'Client', `receiver_name` as 'Receiver name', `mobile_account_type` as 'Mobile Acc. type', `mobile_money_no` as 'Mobile money Number', `amount` as 'Amount', `type` as 'Type', `confirmed` as 'Confirmed', `created_at` as 'Created At', `updated_at` as 'Update At' FROM `matches`";
          
            pst=conn.prepareStatement(selectPledges);
            
            rs=pst.executeQuery();
            
            tbl_Matches.setModel(DbUtils.resultSetToTableModel(rs));
        
        } catch (SQLException e) {
    JOptionPane.showMessageDialog( null, e);
        }
        finally{
        
        try{
         rs.close();
         pst.close();
        }
        catch(Exception e){  
        }}
      }
    
    
    
    /*
    * Selecting all pledges from the database 
   */
    
     private void selectAllPledges(){
    
        try {
            //`                                                                             id`, `pledge_maker_id`, `pledged_amount`, `earning`, `payment_confirm`, `deadline_payment`, `maturity_date`, `transaction_code`, `status`, `matched`, `pledge_receiver_id`, `created_at`, `updated_at` FROM `pledges`
           String sql="SELECT id as 'ID', pledge_maker_id as 'Pledger',pledged_amount as 'Pledged amout', earning as 'Earning', payment_confirm as 'Confirmation', deadline_payment as 'Deadline', maturity_date as 'Maturity date', transaction_code as 'Trans. Code', status as 'Status', matched as 'Matched', pledge_receiver_id as 'Pledge receiver', created_at as 'Created at', updated_at as 'Updated at'  FROM pledges";
          
            pst=conn.prepareStatement(sql);
            
            rs=pst.executeQuery();
            
            tbl_Pledges.setModel(DbUtils.resultSetToTableModel(rs));
            
        } catch (SQLException e) {
    JOptionPane.showMessageDialog( null, e);
        }
        finally{
        
        try{
         rs.close();
         pst.close();
        }
        catch(Exception e){  
        }}
      }
    
     
  
    /*
     *Filter users TAble

       private void filterUserTable (String sql){
        DefaultTableModel model = new DefaultTableModel();
          model =(DefaultTableModel) tbl_ManageUsers.getModel();
     
         TableRowSorter<DefaultTableModel> tr = new TableRowSorter<> (model);
         tbl_ManageUsers.setRowSorter(tr);
         tr.setRowFilter(RowFilter.regexFilter(sql));
              }
       */
       
           /*
     *Filter users TAble
     */
       private void filterMatchesTable (String sql){
        DefaultTableModel model = new DefaultTableModel();
          model =(DefaultTableModel) tbl_Matches.getModel();
     
         TableRowSorter<DefaultTableModel> tr = new TableRowSorter<> (model);
         tbl_Matches.setRowSorter(tr);
         tr.setRowFilter(RowFilter.regexFilter(sql));
              }
       
           /*
     *Filter smsLog TAble
     */
       private void filterSmsLogTable (String sql){
        DefaultTableModel model = new DefaultTableModel();
          model =(DefaultTableModel) tbl_smsLog.getModel();
     
         TableRowSorter<DefaultTableModel> tr = new TableRowSorter<> (model);
         tbl_smsLog.setRowSorter(tr);
         tr.setRowFilter(RowFilter.regexFilter(sql));
              }
       
       
          /*
     *Filter pledges TAble
     */
       private void filterPledgesTable (String sql){
        DefaultTableModel model = new DefaultTableModel();
          model =(DefaultTableModel) tbl_Pledges.getModel();
     
         TableRowSorter<DefaultTableModel> tr = new TableRowSorter<> (model);
         tbl_Pledges.setRowSorter(tr);
         tr.setRowFilter(RowFilter.regexFilter(sql));
              }
       
       
       
       /*
       *Filter SendSms Table
       */
       
       
         private void filterSndSmsTable (String sql){
        DefaultTableModel model = new DefaultTableModel();
          model =(DefaultTableModel) tbl_SendSms.getModel();
     
         TableRowSorter<DefaultTableModel> tr = new TableRowSorter<> (model);
         tbl_SendSms.setRowSorter(tr);
         tr.setRowFilter(RowFilter.regexFilter(sql));
   
     }
       
       
       
       
       
       /*
       * Search Data in the clients Table
       */
    
             private void filterAllClientsTable (String sql){
        DefaultTableModel model = new DefaultTableModel();
          model =(DefaultTableModel) tbl_ManageClients.getModel();
     
         TableRowSorter<DefaultTableModel> tr = new TableRowSorter<> (model);
         tbl_ManageClients.setRowSorter(tr);
         tr.setRowFilter(RowFilter.regexFilter(sql));
     }
    /*
    * A method to generated password for each user randomly
    */
 /* public void passwordGenerator(){
  
char[] chars = "!@#$%^&*()_+ABCDEFGHIJKL0987654321abcdefghijklmnopqrstuvwxyz".toCharArray();
StringBuilder sb = new StringBuilder(20);
Random random = new Random();
for (int i = 0; i < 10; i++) {
    char c = chars[random.nextInt(chars.length)];
    sb.append(c);
}
String output = sb.toString();
//lbl_password.setText(output);
  }

  public void userNameGenerator(){
          String fname = txt_firstName.getText();
          String substring = fname.substring(1,3);
   
          String lname = txt_lastName.getText();
    char[]chars ="01234@#$56789!%^&*()_+".toCharArray();
    StringBuilder sb = new StringBuilder(20);
Random random = new Random();
for (int i = 0; i < 3; i++) {
    char c = chars[random.nextInt(chars.length)];
    sb.append(c);
}
String output = sb.toString();

lbl_userName.setText(substring+"."+lname.toLowerCase()+output);
  }
  
  */
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_parent = new javax.swing.JPanel();
        pnl_clients = new javax.swing.JPanel();
        pnl_allClients = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_ManageClients = new javax.swing.JTable();
        jSeparator9 = new javax.swing.JSeparator();
        lbl_allClient = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txt_SerachAllClient = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        id = new javax.swing.JLabel();
        pnl_transactions = new javax.swing.JPanel();
        pnl_Allsms = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        smsParentPnl = new javax.swing.JPanel();
        pnl_sndSMS = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_SendSms = new javax.swing.JTable();
        jSeparator10 = new javax.swing.JSeparator();
        txt_searchSendSmsTable = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txt_msg = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        txt_recieverPhone = new javax.swing.JLabel();
        txt_recieverName = new javax.swing.JLabel();
        reciever_id = new javax.swing.JLabel();
        pnl_smsLog = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbl_smsLog = new javax.swing.JTable();
        btn_printSmsLog = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        txt_searchSMSlogTable = new javax.swing.JTextField();
        pnl_matches = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_Matches = new javax.swing.JTable();
        jLabel38 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        txt_SearchMatchesTable = new javax.swing.JTextField();
        bnt_printMatches = new javax.swing.JButton();
        pnl_Pledges = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_Pledges = new javax.swing.JTable();
        btn_printPledges = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JSeparator();
        txt_SearchPledges = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnl_parent.setOpaque(false);
        pnl_parent.setLayout(new java.awt.CardLayout());

        pnl_clients.setOpaque(false);
        pnl_clients.setLayout(new java.awt.CardLayout());

        pnl_allClients.setOpaque(false);
        pnl_allClients.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setOpaque(false);

        tbl_ManageClients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_ManageClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ManageClientsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_ManageClients);

        pnl_allClients.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1070, 480));
        pnl_allClients.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 260, -1));

        lbl_allClient.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lbl_allClient.setForeground(new java.awt.Color(0, 0, 51));
        lbl_allClient.setText("All Clients");
        lbl_allClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_allClientMouseClicked(evt);
            }
        });
        pnl_allClients.add(lbl_allClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 0, -1, -1));

        jLabel21.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel21.setText("All Clients");
        pnl_allClients.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 200, 250, 80));

        txt_SerachAllClient.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txt_SerachAllClient.setForeground(new java.awt.Color(255, 255, 255));
        txt_SerachAllClient.setBorder(null);
        txt_SerachAllClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_SerachAllClientKeyReleased(evt);
            }
        });
        pnl_allClients.add(txt_SerachAllClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 260, 30));

        jLabel25.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Search:");
        pnl_allClients.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, -1, -1));

        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        pnl_allClients.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 60, -1, -1));

        jButton2.setText("Delete");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        pnl_allClients.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 60, -1, -1));

        id.setForeground(new java.awt.Color(255, 255, 255));
        id.setText("dv");
        id.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_allClients.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 60, -1, -1));

        pnl_clients.add(pnl_allClients, "card2");

        pnl_parent.add(pnl_clients, "card3");

        pnl_transactions.setOpaque(false);
        pnl_transactions.setLayout(new java.awt.CardLayout());

        pnl_Allsms.setOpaque(false);
        pnl_Allsms.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 0, 51));
        jLabel26.setText("Pledges");
        jLabel26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel26MouseClicked(evt);
            }
        });
        pnl_Allsms.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 9, -1, 20));

        jLabel20.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 0, 51));
        jLabel20.setText("Matches");
        jLabel20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel20MouseClicked(evt);
            }
        });
        pnl_Allsms.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 9, -1, 20));

        jLabel19.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 0, 51));
        jLabel19.setText("SMS");
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });
        pnl_Allsms.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 9, -1, 20));

        smsParentPnl.setOpaque(false);
        smsParentPnl.setLayout(new java.awt.CardLayout());

        pnl_sndSMS.setOpaque(false);
        pnl_sndSMS.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel30.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("SMS Log");
        jLabel30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel30MouseClicked(evt);
            }
        });
        pnl_sndSMS.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 40, -1, -1));

        jLabel31.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Send SMS");
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
        });
        pnl_sndSMS.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, -1, -1));

        tbl_SendSms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_SendSms.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_SendSmsMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbl_SendSms);

        pnl_sndSMS.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 530, 430));
        pnl_sndSMS.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 260, -1));

        txt_searchSendSmsTable.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txt_searchSendSmsTable.setForeground(new java.awt.Color(255, 255, 255));
        txt_searchSendSmsTable.setBorder(null);
        txt_searchSendSmsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchSendSmsTableKeyReleased(evt);
            }
        });
        pnl_sndSMS.add(txt_searchSendSmsTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 260, 40));

        jLabel35.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Search:");
        pnl_sndSMS.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        txt_msg.setColumns(20);
        txt_msg.setForeground(new java.awt.Color(0, 0, 0));
        txt_msg.setRows(5);
        txt_msg.setOpaque(false);
        jScrollPane6.setViewportView(txt_msg);

        pnl_sndSMS.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 210, -1, 290));

        jButton4.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jButton4.setText("Send");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        pnl_sndSMS.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 440, -1, -1));

        jLabel36.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Message body:");
        jLabel36.setToolTipText("");
        pnl_sndSMS.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 180, -1, -1));

        txt_recieverPhone.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txt_recieverPhone.setForeground(new java.awt.Color(255, 255, 255));
        txt_recieverPhone.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true), "Phone number:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N
        pnl_sndSMS.add(txt_recieverPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 350, 240, 70));

        txt_recieverName.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txt_recieverName.setForeground(new java.awt.Color(255, 255, 255));
        txt_recieverName.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true), "Phone number:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N
        pnl_sndSMS.add(txt_recieverName, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 270, 240, 60));

        reciever_id.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        reciever_id.setForeground(new java.awt.Color(204, 255, 255));
        reciever_id.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true), "Name:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 0, 16))); // NOI18N
        pnl_sndSMS.add(reciever_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 40, 60, 30));

        smsParentPnl.add(pnl_sndSMS, "card2");

        pnl_smsLog.setOpaque(false);
        pnl_smsLog.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Send SMS");
        jLabel33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel33MouseClicked(evt);
            }
        });
        pnl_smsLog.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, -1, -1));

        jLabel34.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("SMS Log");
        jLabel34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel34MouseClicked(evt);
            }
        });
        pnl_smsLog.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 40, -1, -1));

        tbl_smsLog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(tbl_smsLog);

        pnl_smsLog.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 1060, 450));

        btn_printSmsLog.setText("Print");
        btn_printSmsLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_printSmsLogActionPerformed(evt);
            }
        });
        pnl_smsLog.add(btn_printSmsLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 80, -1, -1));

        jLabel32.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Search:");
        pnl_smsLog.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));
        pnl_smsLog.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 102, 230, 10));

        txt_searchSMSlogTable.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txt_searchSMSlogTable.setForeground(new java.awt.Color(255, 255, 255));
        txt_searchSMSlogTable.setBorder(null);
        txt_searchSMSlogTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchSMSlogTableKeyReleased(evt);
            }
        });
        pnl_smsLog.add(txt_searchSMSlogTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 230, 40));

        smsParentPnl.add(pnl_smsLog, "card3");

        pnl_Allsms.add(smsParentPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, 580));

        pnl_transactions.add(pnl_Allsms, "card4");

        pnl_matches.setOpaque(false);
        pnl_matches.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel29.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(51, 0, 51));
        jLabel29.setText("Pledges");
        jLabel29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel29MouseClicked(evt);
            }
        });
        pnl_matches.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, -1, -1));

        jLabel28.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(51, 0, 51));
        jLabel28.setText("Matches");
        jLabel28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel28MouseClicked(evt);
            }
        });
        pnl_matches.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, -1, -1));

        jLabel27.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 0, 51));
        jLabel27.setText("SMS");
        jLabel27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel27MouseClicked(evt);
            }
        });
        pnl_matches.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, -1));

        tbl_Matches.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tbl_Matches);

        pnl_matches.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 1060, 490));

        jLabel38.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Search:");
        pnl_matches.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));
        pnl_matches.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 260, -1));

        txt_SearchMatchesTable.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txt_SearchMatchesTable.setForeground(new java.awt.Color(255, 255, 255));
        txt_SearchMatchesTable.setBorder(null);
        txt_SearchMatchesTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_SearchMatchesTableKeyReleased(evt);
            }
        });
        pnl_matches.add(txt_SearchMatchesTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 33, 260, 40));

        bnt_printMatches.setText("Print");
        bnt_printMatches.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnt_printMatchesActionPerformed(evt);
            }
        });
        pnl_matches.add(bnt_printMatches, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 40, -1, -1));

        pnl_transactions.add(pnl_matches, "card3");

        pnl_Pledges.setOpaque(false);
        pnl_Pledges.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 0, 51));
        jLabel5.setText("SMS");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        pnl_Pledges.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, -1));

        jLabel17.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 0, 51));
        jLabel17.setText("Matches");
        jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel17MouseClicked(evt);
            }
        });
        pnl_Pledges.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, -1, -1));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 51));
        jLabel1.setText("Pledges");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        pnl_Pledges.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, -1));

        tbl_Pledges.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tbl_Pledges);

        pnl_Pledges.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 1060, 490));

        btn_printPledges.setText("Print");
        btn_printPledges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_printPledgesActionPerformed(evt);
            }
        });
        pnl_Pledges.add(btn_printPledges, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 30, -1, -1));
        pnl_Pledges.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 240, -1));

        txt_SearchPledges.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txt_SearchPledges.setForeground(new java.awt.Color(255, 255, 255));
        txt_SearchPledges.setBorder(null);
        txt_SearchPledges.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_SearchPledgesKeyReleased(evt);
            }
        });
        pnl_Pledges.add(txt_SearchPledges, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 240, 40));

        jLabel39.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Search:");
        pnl_Pledges.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        pnl_transactions.add(pnl_Pledges, "card2");

        pnl_parent.add(pnl_transactions, "card4");

        getContentPane().add(pnl_parent, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1090, 580));

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Clients");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, -1, -1));

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Transaction");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 80)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/loa.png"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -120, 1090, 760));

        setSize(new java.awt.Dimension(1097, 661));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
             pnl_parent.setVisible(true);
          pnl_parent.removeAll();
          pnl_parent.repaint();
          pnl_parent.revalidate();
          
          pnl_parent.add(pnl_clients);
           pnl_parent.repaint();
          pnl_parent.revalidate();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
             pnl_parent.setVisible(true);
          pnl_parent.removeAll();
          pnl_parent.repaint();
          pnl_parent.revalidate();
          
          pnl_parent.add(pnl_transactions);
           pnl_parent.repaint();
          pnl_parent.revalidate();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
   
     
    }//GEN-LAST:event_formWindowActivated

    private void lbl_allClientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_allClientMouseClicked
        // TODO add your handling code here:
        
           pnl_clients.setVisible(true);
          pnl_clients.removeAll();
          pnl_clients.repaint();
          pnl_clients.revalidate();
          
          pnl_clients.add(pnl_allClients);
           pnl_clients.repaint();
          pnl_clients.revalidate(); 
    }//GEN-LAST:event_lbl_allClientMouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
          pnl_transactions.setVisible(true);
          pnl_transactions.removeAll();
          pnl_transactions.repaint();
          pnl_transactions.revalidate();
          
          pnl_transactions.add(pnl_Allsms);
           pnl_transactions.repaint();
          pnl_transactions.revalidate(); 
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        // TODO add your handling code here:
        
          pnl_transactions.setVisible(true);
          pnl_transactions.removeAll();
          pnl_transactions.repaint();
          pnl_transactions.revalidate();
          
          pnl_transactions.add(pnl_matches);
           pnl_transactions.repaint();
          pnl_transactions.revalidate(); 
    }//GEN-LAST:event_jLabel17MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
              pnl_transactions.setVisible(true);
          pnl_transactions.removeAll();
          pnl_transactions.repaint();
          pnl_transactions.revalidate();
          
          pnl_transactions.add(pnl_Pledges);
           pnl_transactions.repaint();
          pnl_transactions.revalidate(); 
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        // TODO add your handling code here:
                   pnl_transactions.setVisible(true);
          pnl_transactions.removeAll();
          pnl_transactions.repaint();
          pnl_transactions.revalidate();
          
          pnl_transactions.add(pnl_Allsms);
           pnl_transactions.repaint();
          pnl_transactions.revalidate(); 
    }//GEN-LAST:event_jLabel19MouseClicked

    private void jLabel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseClicked
        // TODO add your handling code here:
          pnl_transactions.setVisible(true);
          pnl_transactions.removeAll();
          pnl_transactions.repaint();
          pnl_transactions.revalidate();
          
          pnl_transactions.add(pnl_matches);
           pnl_transactions.repaint();
          pnl_transactions.revalidate(); 
    }//GEN-LAST:event_jLabel20MouseClicked

    private void jLabel26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel26MouseClicked
        // TODO add your handling code here:
                   pnl_transactions.setVisible(true);
          pnl_transactions.removeAll();
          pnl_transactions.repaint();
          pnl_transactions.revalidate();
          
          pnl_transactions.add(pnl_Pledges);
           pnl_transactions.repaint();
          pnl_transactions.revalidate(); 
    }//GEN-LAST:event_jLabel26MouseClicked

    private void jLabel27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel27MouseClicked
        // TODO add your handling code here:
             pnl_transactions.setVisible(true);
          pnl_transactions.removeAll();
          pnl_transactions.repaint();
          pnl_transactions.revalidate();
          
          pnl_transactions.add(pnl_Allsms);
           pnl_transactions.repaint();
          pnl_transactions.revalidate(); 
        
    }//GEN-LAST:event_jLabel27MouseClicked

    private void jLabel28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel28MouseClicked
        // TODO add your handling code here:
          pnl_transactions.setVisible(true);
          pnl_transactions.removeAll();
          pnl_transactions.repaint();
          pnl_transactions.revalidate();
          
          pnl_transactions.add(pnl_matches);
           pnl_transactions.repaint();
          pnl_transactions.revalidate(); 
    }//GEN-LAST:event_jLabel28MouseClicked

    private void jLabel29MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel29MouseClicked
        // TODO add your handling code here:
                   pnl_transactions.setVisible(true);
          pnl_transactions.removeAll();
          pnl_transactions.repaint();
          pnl_transactions.revalidate();
          
          pnl_transactions.add(pnl_Pledges);
           pnl_transactions.repaint();
          pnl_transactions.revalidate(); 
    }//GEN-LAST:event_jLabel29MouseClicked

    private void txt_SerachAllClientKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_SerachAllClientKeyReleased
        // TODO add your handling code here:
          String textToSearch=txt_SerachAllClient.getText();
        filterAllClientsTable(textToSearch);  
    }//GEN-LAST:event_txt_SerachAllClientKeyReleased

    private void jLabel33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MouseClicked
        // TODO add your handling code here:
                   smsParentPnl.setVisible(true);
          smsParentPnl.removeAll();
          smsParentPnl.repaint();
          smsParentPnl.revalidate();
          
          smsParentPnl.add(pnl_sndSMS);
           smsParentPnl.repaint();
          smsParentPnl.revalidate(); 
    }//GEN-LAST:event_jLabel33MouseClicked

    private void jLabel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseClicked
        // TODO add your handling code here:
               smsParentPnl.setVisible(true);
          smsParentPnl.removeAll();
          smsParentPnl.repaint();
          smsParentPnl.revalidate();
          
          smsParentPnl.add(pnl_smsLog);
           smsParentPnl.repaint();
          smsParentPnl.revalidate(); 
    }//GEN-LAST:event_jLabel34MouseClicked

    private void jLabel30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel30MouseClicked
        // TODO add your handling code here:
               smsParentPnl.setVisible(true);
          smsParentPnl.removeAll();
          smsParentPnl.repaint();
          smsParentPnl.revalidate();
          
          smsParentPnl.add(pnl_smsLog);
           smsParentPnl.repaint();
          smsParentPnl.revalidate(); 
    }//GEN-LAST:event_jLabel30MouseClicked

    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
        // TODO add your handling code here:
               smsParentPnl.setVisible(true);
          smsParentPnl.removeAll();
          smsParentPnl.repaint();
          smsParentPnl.revalidate();
          
          smsParentPnl.add(pnl_sndSMS);
           smsParentPnl.repaint();
          smsParentPnl.revalidate(); 
    }//GEN-LAST:event_jLabel31MouseClicked

    private void tbl_SendSmsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_SendSmsMouseClicked
      
   
        
        
         try{

            int row = tbl_SendSms.getSelectedRow();
            String Table_click=(tbl_SendSms.getModel().getValueAt(row,0).toString());

            String sql ="SELECT * FROM clients WHERE id = '"+Table_click+"'";
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();

            if(rs.next()){

                String user_id = rs.getString("id");
                reciever_id.setText(user_id);
                          
                 String fname = rs.getString("firstname");
              
                 String m_name = rs.getString("middle_name");
                
                               
                String lname = rs.getString("lastname");
                txt_recieverName.setText(fname+" "+m_name+" "+lname);
              
                String userName = rs.getString("phone");
                txt_recieverPhone.setText(userName);
             }

        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
            finally{
        
        try{
         rs.close();
         pst.close();
        }
        catch(Exception e){
        
        
        }}
    
    }//GEN-LAST:event_tbl_SendSmsMouseClicked

    private void btn_printSmsLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_printSmsLogActionPerformed
        // TODO add your handling code here:
        /*
        *Printing the sms log Table 
        */
         MessageFormat header = new MessageFormat("All SMS LOG");
         MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        
        try{
             
             tbl_smsLog.print(JTable.PrintMode.FIT_WIDTH, header, footer);


    }catch(PrinterException e)
    {
           System.err.format("Cannont print %s%n",e.getMessage());

    }
    }//GEN-LAST:event_btn_printSmsLogActionPerformed

    private void txt_searchSendSmsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchSendSmsTableKeyReleased
        // TODO add your handling code here:
        
        String textToSearch=txt_searchSendSmsTable.getText();
        filterSndSmsTable (textToSearch); 
    }//GEN-LAST:event_txt_searchSendSmsTableKeyReleased

    private void txt_searchSMSlogTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchSMSlogTableKeyReleased
        // TODO add your handling code here:
        String textToSearch=txt_searchSMSlogTable.getText();
        filterSmsLogTable(textToSearch); 
    }//GEN-LAST:event_txt_searchSMSlogTableKeyReleased

    private void txt_SearchMatchesTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_SearchMatchesTableKeyReleased
        // TODO add your handling code here:
        
          String textToSearch=txt_SearchMatchesTable.getText();
        filterMatchesTable(textToSearch); 
    }//GEN-LAST:event_txt_SearchMatchesTableKeyReleased

    private void bnt_printMatchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnt_printMatchesActionPerformed
        // TODO add your handling code here:
        
        /*
        *Printing the matches table Table 
        */
         MessageFormat header = new MessageFormat("MATCHES - SUMMARY");
         MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        
        try{
             
             tbl_Matches.print(JTable.PrintMode.FIT_WIDTH, header, footer);


    }catch(PrinterException e)
    {
           System.err.format("Cannont print %s%n",e.getMessage());

    }
    }//GEN-LAST:event_bnt_printMatchesActionPerformed

    private void btn_printPledgesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_printPledgesActionPerformed
        // TODO add your handling code here:
        /*
        *Printing the pledges Table 
        */
         MessageFormat header = new MessageFormat("All PLEDGES");
         MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        
        try{
             
             tbl_Pledges.print(JTable.PrintMode.FIT_WIDTH, header, footer);


    }catch(PrinterException e)
    {
           System.err.format("Cannont print %s%n",e.getMessage());

    }
    }//GEN-LAST:event_btn_printPledgesActionPerformed

    private void txt_SearchPledgesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_SearchPledgesKeyReleased
        // TODO add your handling code here:
        String textToSearch=txt_SearchPledges.getText();
        filterPledgesTable(textToSearch); 
    }//GEN-LAST:event_txt_SearchPledgesKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String date;
     date = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime());
    
        
        try {
         // TODO add your handling code here:
         
         if (txt_recieverName.getText().isEmpty() || txt_recieverPhone.getText().isEmpty() || txt_msg.getText().isEmpty()) {
          JOptionPane.showMessageDialog(null, "Please select a client from the table");
         }
         else{
          sendSMS();
          
          try{
        String sql = "INSERT INTO sms_messages(phone, message, status, dates, type, sender, receipient) VALUES (?,?,?,?,?,?,?)";
                    
                    pst=conn.prepareStatement(sql);
                    pst.setString(1, txt_recieverPhone.getText());
                    pst.setString(2, txt_msg.getText());
                    pst.setString(3, status);
                    pst.setString(4,date);
                    pst.setString(5, "Notification");
                    pst.setString(6, sender_id);
                    pst.setString(7, txt_recieverName.getText());
  
                    pst.execute();
                   
                    JOptionPane.showMessageDialog(null," Success");
                 
                    txt_recieverPhone.setText("");
                    txt_msg.setText("");
                   
                    txt_recieverName.setText("");
                
              
        }
        catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog( null, e);
}
          
            txt_recieverName.setText("");
        txt_recieverPhone.setText("");
        reciever_id.setText("");
        txt_msg.setText("");
         }
        
     } catch (IOException ex) {
         Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
     }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
                 int row=tbl_ManageClients.getSelectedRow();
            
            if(row==-1){
                
                JOptionPane.showMessageDialog(null, "No Row Selected");
            
            }
            else{
                
                 int c = JOptionPane.showConfirmDialog(null, "Do you realy wanna Delete","Delete",JOptionPane.YES_NO_OPTION);
      
        if(c==0){
        String sql =" DELETE FROM clients WHERE id =?";
        try{
            
            pst=conn.prepareStatement(sql);
            pst.setString(1,id.getText());
            pst.execute();
            
            JOptionPane.showMessageDialog(null, " Deleted Successfully");
            
    selectAllClients();
        }

        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        
        }
        }
        
            }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
         /*
        *Printing the CLIENTS Table 
        */
         MessageFormat header = new MessageFormat("All CLIENTS");
         MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        
        try{
             
             tbl_ManageClients.print(JTable.PrintMode.FIT_WIDTH, header, footer);


    }catch(PrinterException e)
    {
           System.err.format("Cannont print %s%n",e.getMessage());

    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tbl_ManageClientsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ManageClientsMouseClicked
        // TODO add your handling code here:
        
        try{

            int row = tbl_ManageClients.getSelectedRow();
            String Table_click=(tbl_ManageClients.getModel().getValueAt(row,0).toString());

            String sql ="SELECT * FROM clients WHERE id = '"+Table_click+"'";
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();

            if(rs.next()){

                
                String clients_id = rs.getString("id");
                id.setText(clients_id);
                
   
            }

        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
            finally{
        
        try{
         rs.close();
         pst.close();
        }
        catch(Exception e){
        
        
        }}
    }//GEN-LAST:event_tbl_ManageClientsMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bnt_printMatches;
    private javax.swing.JButton btn_printPledges;
    private javax.swing.JButton btn_printSmsLog;
    private javax.swing.JLabel id;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lbl_allClient;
    private javax.swing.JPanel pnl_Allsms;
    private javax.swing.JPanel pnl_Pledges;
    private javax.swing.JPanel pnl_allClients;
    private javax.swing.JPanel pnl_clients;
    private javax.swing.JPanel pnl_matches;
    private javax.swing.JPanel pnl_parent;
    private javax.swing.JPanel pnl_smsLog;
    private javax.swing.JPanel pnl_sndSMS;
    private javax.swing.JPanel pnl_transactions;
    private javax.swing.JLabel reciever_id;
    private javax.swing.JPanel smsParentPnl;
    private javax.swing.JTable tbl_ManageClients;
    private javax.swing.JTable tbl_Matches;
    private javax.swing.JTable tbl_Pledges;
    private javax.swing.JTable tbl_SendSms;
    private javax.swing.JTable tbl_smsLog;
    private javax.swing.JTextField txt_SearchMatchesTable;
    private javax.swing.JTextField txt_SearchPledges;
    private javax.swing.JTextField txt_SerachAllClient;
    private javax.swing.JTextArea txt_msg;
    private javax.swing.JLabel txt_recieverName;
    private javax.swing.JLabel txt_recieverPhone;
    private javax.swing.JTextField txt_searchSMSlogTable;
    private javax.swing.JTextField txt_searchSendSmsTable;
    // End of variables declaration//GEN-END:variables
}
