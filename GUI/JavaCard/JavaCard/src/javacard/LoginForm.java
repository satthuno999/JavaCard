/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacard;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javacard.ConnectCard;
/**
 *
 * @author Bawcs
 */
public class LoginForm extends javax.swing.JFrame {
    boolean firstUse = true;
    
    ConnectCard connect = new ConnectCard();
    /**
     * Creates new form LoginForm
     */
    public LoginForm() {
        initComponents();
        jlbLogin.setEnabled(false);
        txtPIN.setEnabled(false);
        checkbox.setEnabled(false);
        btnLogin.setEnabled(false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtPIN = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        checkbox = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        btnConnect = new javax.swing.JButton();
        jlbLogin = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtPIN.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtPIN.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        getContentPane().add(txtPIN, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 300, 240, 40));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("X");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 10, 20, 30));

        jLabel4.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jLabel4.setText("Mã PIN");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 260, -1, -1));

        checkbox.setBackground(new java.awt.Color(255, 255, 255));
        checkbox.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        checkbox.setText("Hiện mã PIN");
        checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxActionPerformed(evt);
            }
        });
        getContentPane().add(checkbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 350, -1, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel3.setText("Đăng nhập");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 200, 40));

        btnLogin.setBackground(new java.awt.Color(0, 102, 153));
        btnLogin.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Đăng nhập");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        getContentPane().add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 390, 240, 30));

        btnConnect.setBackground(new java.awt.Color(153, 255, 255));
        btnConnect.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnConnect.setText("Kết nối thẻ");
        btnConnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConnectMouseClicked(evt);
            }
        });
        getContentPane().add(btnConnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 540, 350, -1));

        jlbLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Trang.jpg"))); // NOI18N
        getContentPane().add(jlbLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 130, 350, 390));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LM logo.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void checkboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxActionPerformed
        // TODO add your handling code here:
        if(checkbox.isSelected()){
            txtPIN.setEchoChar((char)0);
        }else txtPIN.setEchoChar('*');
    }//GEN-LAST:event_checkboxActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        try {
            // TODO add your handling code here:
            String pin = txtPIN.getText();
            byte[] pinByte = pin.getBytes(pin);
            if(pin.equals("1")){
                AdminForm admin = new AdminForm();
                admin.setVisible(true);
                this.dispose();
            }
            else{
                HomeForm home = new HomeForm();
                home.setVisible(true);
                this.dispose();
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnConnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConnectMouseClicked
          objError errorrReturn = new objError();
          byte[] response = connect.connectapplet();
          
//          if(response.equals()){
//              JOptionPane.showMessageDialog(null,"Kết nối bị lỗi");
//          }
//          else{
//              if(response[1]==(byte)0x90 && response[2] == (byte)0x00){
//                    JOptionPane.showMessageDialog(null, "Kết nối thẻ thành công");    
//                    jlbLogin.setEnabled(true);
//                    txtPIN.setEnabled(true);
//                    checkbox.setEnabled(true);
//                    btnLogin.setEnabled(true);
//              }
//              else{
//                  JOptionPane.showMessageDialog(null,"Kết nối bị lỗi");
//              }
//          }
    }//GEN-LAST:event_btnConnectMouseClicked

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
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnLogin;
    private javax.swing.JCheckBox checkbox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jlbLogin;
    private javax.swing.JPasswordField txtPIN;
    // End of variables declaration//GEN-END:variables
}
