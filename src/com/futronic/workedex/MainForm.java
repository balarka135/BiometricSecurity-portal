/*
 * MainForm.java
 */

package com.futronic.workedex;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import com.futronic.SDKHelper.*;
import java.util.Vector;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinReg;
import java.awt.event.*;
import java.util.*;

import javax.mail.*;

import javax.mail.internet.*;

import javax.activation.*;


/**
 *
 * @author  Shail khan
 */
public class MainForm extends javax.swing.JFrame 
        implements IEnrollmentCallBack , IVerificationCallBack, IIdentificationCallBack
{
    static final long serialVersionUID = 1L;
    static final String kCompanyName = "Futronic";
    static final String kProductName = "SDK 4.0";
    static final String kDbName = "DataBaseNet";
    
     String szUserName;
     String Email;
    /** Creates new form MainForm */
    public MainForm()
    {
        initComponents();
        setMinimumSize( new Dimension( getWidth(), getHeight() ) );

        /*
         * Set main frame to the center of display
         */
        setLocationRelativeTo( null );

        this.setIconImage( createImageIcon( "image/Icon.png" ).getImage() );
        
        m_FingerPrintImage = new MyIcon();
        m_FingerPrintImage.setImage( createImageIcon( "image/Futronic.png" ).getImage() );
       
        FingerImage.setIcon( m_FingerPrintImage );
     
        
        
        
        
        
        
        
        
        
        // Get database folder
        try
        {
            m_DbDir = GetDatabaseDir();
        }
        catch( AppException e )
        {
            JOptionPane.showMessageDialog(null,
                                        "Initialization failed. Application will be close.\nError description: " + e.getMessage(),
                                          getTitle(), JOptionPane.ERROR_MESSAGE);
            System.exit( 0 );
        }
        
        
    
        
        
        
        
        
        
        
        
        // Set default parameters
       try
        {
            FutronicEnrollment dummy = new FutronicEnrollment();
         
            cbMaxFrames.setSelectedItem( String.valueOf( dummy.getMaxModels() ) );
           
           
            SetIdentificationLimit( dummy.getIdentificationsLeft() );
        }
        catch( FutronicException e )
        {
            JOptionPane.showMessageDialog(null,
                                          "Initialization failed. Application will be close.\nError description: " + e.getMessage(),
                                          getTitle(), JOptionPane.ERROR_MESSAGE);
            System.exit( 0 );
        }
        m_Operation = null;
        
        btnVerify.setVisible(true);
        EnableControls( true );
    
          

 

 

                
                
                
                
                
                
                
                
                
                
                
                
                
                
          
    
    }
    
    
    
    
    
   
  
        
        
        
        
    
    
    
    
    
    
    
    
    
    ////////////////////////////////////////////////////////////////////
    // ICallBack interface implementation
    ////////////////////////////////////////////////////////////////////
    
    /*
     * The "Put your finger on the scanner" event.
     *
     * @param Progress the current progress data structure.
     */
    public void OnPutOn( FTR_PROGRESS Progress )
    {
        txtMessage.setText( "Put finger into device, please ..." );
    }

    /**
     * The "Take off your finger from the scanner" event.
     *
     * @param Progress the current progress data structure.
     */
    public void OnTakeOff( FTR_PROGRESS Progress )
    {
        txtMessage.setText( "Take off finger from device, please ..." );
      
    }

    /*
     * The "Show the current fingerprint image" event.
     *
     * @param Bitmap the instance of Bitmap class with fingerprint image.
     */
    public void UpdateScreenImage( java.awt.image.BufferedImage Bitmap )
    {
         int width = Bitmap.getWidth();
        int height = Bitmap.getHeight();
 
        // Convert to negative
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int p = Bitmap.getRGB(x,y);
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;
 
                //subtract RGB from 255
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
 
                //set new RGB value
                p = (a<<24) | (r<<16) | (g<<8) | b;
                Bitmap.setRGB(x, y, p);
            }
        }
        
     
        try{
            String fileloc="C:\\Users\\Khan\\Documents\\Futronic\\SDK 4.0\\DataBaseNet\\"+szUserName+".png";
        ImageIO.write(Bitmap,"jpg",new File(fileloc));
        }
        catch(IOException e){
                             System.out.println("Error occured\n");
        }
        m_FingerPrintImage.setImage( Bitmap );
        FingerImage.repaint();
    }

    /*
     * The "Fake finger detected" event.
     *
     * @param Progress the fingerprint image.
     *
     * @return <code>true</code> if the current identification operation 
     * should be aborted, otherwise is <code>false</code>
     */
    public boolean OnFakeSource( FTR_PROGRESS Progress )
    {
        int nResponse;
        nResponse = JOptionPane.showConfirmDialog( this,
                "Fake source detected. Do you want continue process?",
                getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
        return (nResponse == JOptionPane.NO_OPTION);
    }

    ////////////////////////////////////////////////////////////////////
    // ICallBack interface implementation
    ////////////////////////////////////////////////////////////////////
    
    /*
     * The "Enrollment operation complete" event.
     *
     * @param bSuccess <code>true</code> if the operation succeeds, otherwise is
     * <code>false</code>.
     * @param The Futronic SDK return code (see FTRAPI.h).
     */
    public void OnEnrollmentComplete( boolean bSuccess, int nResult )
    {
        if( bSuccess )
        {
            // set status string
            txtMessage.setText("Enrollment process finished successfully. Quality: " + 
                    ((FutronicEnrollment)m_Operation).getQuality() );

            // Set template into passport and save it
            ((DbRecord)m_OperationObj).setTemplate( ((FutronicEnrollment)m_Operation).getTemplate() );
            try
            {
                ((DbRecord)m_OperationObj).Save( m_DbDir + File.separator + ((DbRecord)m_OperationObj).getUserName(),m_DbDir + File.separator + ((DbRecord)m_OperationObj).getEmail() );
            }
            catch( IOException e )
            {
                JOptionPane.showMessageDialog(this,
                                              e.getMessage(),
                                              getTitle(), JOptionPane.WARNING_MESSAGE );
            }
            
              String to = Email;
                 String subject = "Access";                          /*sending Mails */
                 String msg ="Enrolled successfully" ;
                 final String from ="apurbakrnath17@gmail.com";
                 final  long password =8876403297L;
                 final String s=Long.toString(password);

                 Properties props = new Properties();  
                 props.setProperty("mail.transport.protocol", "smtp");     
                 props.setProperty("mail.host", "smtp.gmail.com");  
                 props.put("mail.smtp.auth", "true");  
                 props.put("mail.smtp.port", "465");  
                 props.put("mail.debug", "true");  
                 props.put("mail.smtp.socketFactory.port", "465");  
                 props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
                 props.put("mail.smtp.socketFactory.fallback", "false");  
    Session session = Session.getDefaultInstance(props,  
    new javax.mail.Authenticator() {
       protected PasswordAuthentication getPasswordAuthentication() {  
       return new PasswordAuthentication(from,s);  
   }  
   });  

   //session.setDebug(true);  
  try{
   Transport transport = session.getTransport();  
  
   InternetAddress addressFrom = new InternetAddress(from);  

   MimeMessage message = new MimeMessage(session);  
   message.setSender(addressFrom);  
   message.setSubject(subject);  
   message.setContent(msg, "text/plain");  
   message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));  

   transport.connect();  
   Transport.send(message);  
   transport.close();
  }
  catch(Exception e){
      System.out.println(e);
  }
            
            
            
            
        } else {
            txtMessage.setText("Enrollment process failed. Error description: " + 
                    FutronicSdkBase.SdkRetCode2Message(nResult) );
              String to = Email;
                 String subject = "Unsuccessful";                          /*sending Mails */
                 String msg ="Enrollment process Failed" ;
                 final String from ="apurbakrnath17@gmail.com";
                 final  long password =8876403297L;
                 final String s=Long.toString(password);

                 Properties props = new Properties();  
                 props.setProperty("mail.transport.protocol", "smtp");     
                 props.setProperty("mail.host", "smtp.gmail.com");  
                 props.put("mail.smtp.auth", "true");  
                 props.put("mail.smtp.port", "465");  
                 props.put("mail.debug", "true");  
                 props.put("mail.smtp.socketFactory.port", "465");  
                 props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
                 props.put("mail.smtp.socketFactory.fallback", "false");  
    Session session = Session.getDefaultInstance(props,  
    new javax.mail.Authenticator() {
       protected PasswordAuthentication getPasswordAuthentication() {  
       return new PasswordAuthentication(from,s);  
   }  
   });  

   //session.setDebug(true);  
  try{
   Transport transport = session.getTransport();  
  
   InternetAddress addressFrom = new InternetAddress(from);  

   MimeMessage message = new MimeMessage(session);  
   message.setSender(addressFrom);  
   message.setSubject(subject);  
   message.setContent(msg, "text/plain");  
   message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));  

   transport.connect();  
   Transport.send(message);  
   transport.close();
  }
  catch(Exception e){
      System.out.println(e);
  }
        }

        m_Operation = null;
        m_OperationObj = null;
        EnableControls( true );
    }

    /*
     * The "Verification operation complete" event.
     *
     * @param bSuccess <code>true</code> if the operation succeeds, otherwise is <code>false</code>
     * @param nResult the Futronic SDK return code.
     * @param bVerificationSuccess if the operation succeeds (bSuccess is <code>true</code>), 
     * this parameters shows the verification operation result. <code>true</code>
     * if the captured from the attached scanner template is matched, otherwise is <code>false</code>.
     */
    public void OnVerificationComplete( boolean bSuccess,
                                        int nResult,
                                        boolean bVerificationSuccess )
    {   
        Email=((DbRecord)m_OperationObj).getEmail() ;
                           
        
        StringBuffer szResult = new StringBuffer();
        if( bSuccess )
        {
            if (bVerificationSuccess)
            {
                szResult.append("Verification is successful.");
                szResult.append("User Name: ");
                szResult.append( ((DbRecord)m_OperationObj).getUserName() );
                
                  
                 String to = Email;
                 String subject = "ACCESS GRANTED!!";                          /*sending Mails */
                 String msg ="Verification Successful" ;
                 final String from ="apurbakrnath17@gmail.com";
                 final  long password =8876403297L;
                 final String s=Long.toString(password);

                 Properties props = new Properties();  
                 props.setProperty("mail.transport.protocol", "smtp");     
                 props.setProperty("mail.host", "smtp.gmail.com");  
                 props.put("mail.smtp.auth", "true");  
                 props.put("mail.smtp.port", "465");  
                 props.put("mail.debug", "true");  
                 props.put("mail.smtp.socketFactory.port", "465");  
                 props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
                 props.put("mail.smtp.socketFactory.fallback", "false");  
    Session session = Session.getDefaultInstance(props,  
    new javax.mail.Authenticator() {
       protected PasswordAuthentication getPasswordAuthentication() {  
       return new PasswordAuthentication(from,s);  
   }  
   });  

   //session.setDebug(true);  
  try{
   Transport transport = session.getTransport();  
  
   InternetAddress addressFrom = new InternetAddress(from);  

   MimeMessage message = new MimeMessage(session);  
   message.setSender(addressFrom);  
   message.setSubject(subject);  
   message.setContent(msg, "text/plain");  
   message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));  

   transport.connect();  
   Transport.send(message);  
   transport.close();
  }
  catch(Exception e){
      System.out.println(e);
  }
                
                
                
            }
            else
            {
              
                szResult.append("Verification failed.");
            
                           
              
                  
                 String to = Email;
                 String subject = "ACCESS DENIED!!";                          /*sending Mails */
                 String msg ="Verification Failed" ;
                 final String from ="apurbakrnath17@gmail.com";
                 final  long password =8876403297L;
                 final String s=Long.toString(password);

                 Properties props = new Properties();  
                 props.setProperty("mail.transport.protocol", "smtp");     
                 props.setProperty("mail.host", "smtp.gmail.com");  
                 props.put("mail.smtp.auth", "true");  
                 props.put("mail.smtp.port", "465");  
                 props.put("mail.debug", "true");  
                 props.put("mail.smtp.socketFactory.port", "465");  
                 props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
                 props.put("mail.smtp.socketFactory.fallback", "false");  
    Session session = Session.getDefaultInstance(props,  
    new javax.mail.Authenticator() {
       protected PasswordAuthentication getPasswordAuthentication() {  
       return new PasswordAuthentication(from,s);  
   }  
   });  

   //session.setDebug(true);  
  try{
   Transport transport = session.getTransport();  
  
   InternetAddress addressFrom = new InternetAddress(from);  

   MimeMessage message = new MimeMessage(session);  
   message.setSender(addressFrom);  
   message.setSubject(subject);  
   message.setContent(msg, "text/plain");  
   message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));  

   transport.connect();  
   Transport.send(message);  
   transport.close();
  }
  catch(Exception e){
      System.out.println(e);
  }
      
            
            }
            }
        else
        {
            szResult.append( "Verification process failed." );
            szResult.append( "Error description: ");
            szResult.append(FutronicSdkBase.SdkRetCode2Message(nResult));
        }
        txtMessage.setText( szResult.toString() );
        SetIdentificationLimit( m_Operation.getIdentificationsLeft() );
        m_Operation = null;
        m_OperationObj = null;
        EnableControls( true );
    }

    /*
     * The "Get base template operation complete" event.
     *
     * @param bSuccess <code>true</code> if the operation succeeds, otherwise is <code>false</code>.
     * @param nResult The Futronic SDK return code.
     */
    public void OnGetBaseTemplateComplete( boolean bSuccess, int nResult )
    {
          
        
        
        StringBuffer szMessage = new StringBuffer();
        if (bSuccess)
        {
            txtMessage.setText("Starting identification...");
            Vector<DbRecord> Users = (Vector<DbRecord>)m_OperationObj;
            FtrIdentifyRecord[] rgRecords = new FtrIdentifyRecord[ Users.size() ];
            for( int iUsers = 0; iUsers < Users.size(); iUsers++ )
                rgRecords[iUsers] = Users.get( iUsers ).getFtrIdentifyRecord();

            FtrIdentifyResult result = new FtrIdentifyResult();
            
            nResult = ((FutronicIdentification)m_Operation).Identification(rgRecords, result);
            if (nResult == FutronicSdkBase.RETCODE_OK)
            {
                szMessage.append("Identification process complete. User: ");
                if( result.m_Index != -1 ){
                    szMessage.append(Users.get( result.m_Index ).getUserName() );
     
    
  
      
                    
                }else
                  {  szMessage.append("not found");
                  
                   }
                
                
                
                
                
            } else {
                szMessage.append( "Identification failed." );
                szMessage.append( FutronicSdkBase.SdkRetCode2Message( nResult ) );
            }
            SetIdentificationLimit( m_Operation.getIdentificationsLeft() );
        } else {
            szMessage.append("Can not retrieve base template.");
            szMessage.append("Error description: ");
            szMessage.append( FutronicSdkBase.SdkRetCode2Message(nResult) );
        }
        txtMessage.setText( szMessage.toString() );
        m_Operation = null;
        m_OperationObj = null;
        EnableControls(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jLabel3 = new javax.swing.JLabel();
        cbMaxFrames = new javax.swing.JComboBox();
        lblIdentificationsLimit = new javax.swing.JLabel();
        txtMessage = new javax.swing.JTextField();
        btnExit = new javax.swing.JButton();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        btnEnroll = new javax.swing.JButton();
        btnVerify = new javax.swing.JButton();
        btnIdentify = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        FingerImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Biometric Security Portal ");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        setType(java.awt.Window.Type.POPUP);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLayeredPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(" Settings "));
        jLayeredPane1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Set max frames in template:");
        jLayeredPane1.add(jLabel3);
        jLabel3.setBounds(20, 40, 160, 20);

        cbMaxFrames.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbMaxFrames.setMaximumRowCount(10);
        cbMaxFrames.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        jLayeredPane1.add(cbMaxFrames);
        cbMaxFrames.setBounds(200, 40, 50, 21);

        lblIdentificationsLimit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblIdentificationsLimit.setText("jLabel5");
        jLayeredPane1.add(lblIdentificationsLimit);
        lblIdentificationsLimit.setBounds(20, 120, 410, 30);

        txtMessage.setEditable(false);
        txtMessage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMessageActionPerformed(evt);
            }
        });

        btnExit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnExit.setText("Exit");
        btnExit.setMaximumSize(new java.awt.Dimension(75, 23));
        btnExit.setMinimumSize(new java.awt.Dimension(75, 23));
        btnExit.setPreferredSize(new java.awt.Dimension(75, 23));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        jLayeredPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(" Operations "));
        jLayeredPane2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnEnroll.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnEnroll.setText("Enroll");
        btnEnroll.setMaximumSize(new java.awt.Dimension(75, 23));
        btnEnroll.setMinimumSize(new java.awt.Dimension(75, 23));
        btnEnroll.setPreferredSize(new java.awt.Dimension(75, 23));
        btnEnroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrollActionPerformed(evt);
            }
        });
        jLayeredPane2.add(btnEnroll);
        btnEnroll.setBounds(20, 60, 90, 23);

        btnVerify.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnVerify.setText("Detect");
        btnVerify.setMaximumSize(new java.awt.Dimension(75, 23));
        btnVerify.setMinimumSize(new java.awt.Dimension(75, 23));
        btnVerify.setPreferredSize(new java.awt.Dimension(75, 23));
        btnVerify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerifyActionPerformed(evt);
            }
        });
        jLayeredPane2.add(btnVerify);
        btnVerify.setBounds(20, 130, 90, 23);

        btnIdentify.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnIdentify.setText("Identify");
        btnIdentify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIdentifyActionPerformed(evt);
            }
        });
        jLayeredPane2.add(btnIdentify);
        btnIdentify.setBounds(20, 200, 90, 23);

        btnStop.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnStop.setText("Stop");
        btnStop.setMaximumSize(new java.awt.Dimension(75, 23));
        btnStop.setMinimumSize(new java.awt.Dimension(75, 23));
        btnStop.setPreferredSize(new java.awt.Dimension(75, 23));
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });
        jLayeredPane2.add(btnStop);
        btnStop.setBounds(210, 130, 90, 23);

        FingerImage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        FingerImage.setMaximumSize(new java.awt.Dimension(160, 210));
        FingerImage.setMinimumSize(new java.awt.Dimension(160, 210));
        FingerImage.setPreferredSize(new java.awt.Dimension(160, 210));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(184, 184, 184)
                        .add(btnExit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 407, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(6, 6, 6)
                                .add(jLayeredPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 401, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(txtMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 420, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(FingerImage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 183, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 53, Short.MAX_VALUE)
                        .add(txtMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jLayeredPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 281, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(FingerImage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(18, 18, 18)
                .add(btnExit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIdentifyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnIdentifyActionPerformed
    {//GEN-HEADEREND:event_btnIdentifyActionPerformed
        Vector<DbRecord> Users = DbRecord.ReadRecords( m_DbDir );
        if( Users.size() == 0 )
        {
            JOptionPane.showMessageDialog(this,
                                          "Users not found. Please, run enrollment process first.",
                                          getTitle(), JOptionPane.ERROR_MESSAGE);
            
        

               
           
            
            
            
            
            
            return;
        }
        m_OperationObj = Users;
           

        try
        {
            m_Operation = new FutronicIdentification();

            // Set control properties
           
            m_Operation.setFFDControl( true );
       
         
         
            EnableControls(false);

            // start verification process
            ((FutronicIdentification)m_Operation).GetBaseTemplate( this );
        }
        catch( FutronicException e )
        {
            JOptionPane.showMessageDialog(this,
                                          "Can not start identification operation.\nError description: " + e.getMessage(),
                                          getTitle(), JOptionPane.ERROR_MESSAGE);
            m_Operation = null;
            m_OperationObj = null;
        }
        
    }//GEN-LAST:event_btnIdentifyActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnStopActionPerformed
    {//GEN-HEADEREND:event_btnStopActionPerformed
        m_Operation.OnCalcel();
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnVerifyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnVerifyActionPerformed
    {//GEN-HEADEREND:event_btnVerifyActionPerformed
        DbRecord SelectedUser = null;
        Vector<DbRecord> Users = DbRecord.ReadRecords( m_DbDir );
        if( Users.size() == 0 )
        {
            JOptionPane.showMessageDialog(this,
                                          "Users not found. Please, run enrollment process first.",
                                          getTitle(), JOptionPane.ERROR_MESSAGE);
            return;
        }
        SelectUser dlg = new SelectUser(this, true, Users, m_DbDir );
        dlg.setVisible( true );
        SelectedUser = dlg.getRecord();
        if( SelectedUser == null )
        {
            JOptionPane.showMessageDialog(this,
                                          "No selected user",
                                          getTitle(), JOptionPane.ERROR_MESSAGE);
            return;
        }
        m_OperationObj = SelectedUser;
        
        Email=((DbRecord)m_OperationObj).getEmail();
        try
        {
            m_Operation = new FutronicVerification( SelectedUser.getTemplate() );

            // Set control properties
           
            m_Operation.setFFDControl( true );
        //    m_Operation.setFARN( Integer.parseInt( tbFARN.getText() ) );
           
          
            EnableControls(false);

            // start verification process
            ((FutronicVerification)m_Operation).Verification( this );
        }
        catch( FutronicException e )
        {
            JOptionPane.showMessageDialog(this,
                                          "Can not start verification operation.\nError description: " + e.getMessage(),
                                          getTitle(), JOptionPane.ERROR_MESSAGE);
            m_Operation = null;
            m_OperationObj = null;
        }
    }//GEN-LAST:event_btnVerifyActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        if( m_Operation != null )
        {
            m_Operation.Dispose();
        }
    }//GEN-LAST:event_formWindowClosing

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExitActionPerformed
    {//GEN-HEADEREND:event_btnExitActionPerformed
        if( m_Operation != null )
        {
            m_Operation.Dispose();
        }
        System.exit( 0 );
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnEnrollActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnEnrollActionPerformed
    {//GEN-HEADEREND:event_btnEnrollActionPerformed
        try
        {                                                                              
            // Get user name                                                                                             /*Enrollment process */
         
               szUserName = JOptionPane.showInputDialog( this, "Enter user name", "User name", JOptionPane.PLAIN_MESSAGE );
               
                 Email = JOptionPane.showInputDialog( this, "Enter E-mail", "Email", JOptionPane.PLAIN_MESSAGE );
            if( szUserName == null || szUserName.length() == 0)
            {
                JOptionPane.showMessageDialog(this,
                                          "You must enter a user name.",
                                          getTitle(), JOptionPane.WARNING_MESSAGE );
                return;
            }
            
            // Try creat the file for template
            if( isUserExists( szUserName ) )
            {
                int nResponse;
                nResponse = JOptionPane.showConfirmDialog( this,
                                                           "User already exists. Do you want replace it?",
                                                           getTitle(), 
                                                           JOptionPane.YES_NO_OPTION,
                                                           JOptionPane.QUESTION_MESSAGE );
                if( nResponse == JOptionPane.NO_OPTION )
                    return;
            } else {
                CreateFile( szUserName );
                CreateFile1(Email);
            }
            
            m_OperationObj = new DbRecord();
            ((DbRecord)m_OperationObj).setUserName( szUserName );
            ((DbRecord)m_OperationObj).setEmail( Email );
            m_Operation = new FutronicEnrollment();

            // Set control properties
            
            m_Operation.setFFDControl( true );
           //  m_Operation.setFARN( Integer.parseInt( tbFARN.getText() ) );
          
            ((FutronicEnrollment)m_Operation).setMaxModels( Integer.parseInt( (String)cbMaxFrames.getSelectedItem() ) );

          
            

            EnableControls(false);

            // start enrollment process
            ((FutronicEnrollment)m_Operation).Enrollment( this );
        }
        catch( Exception e )
        {
            JOptionPane.showMessageDialog(this,
                                          "Can not start enrollment operation.\nError description: " + e.getMessage(),
                                          getTitle(), JOptionPane.ERROR_MESSAGE);
            m_Operation = null;
            m_OperationObj = null;
        }

    }//GEN-LAST:event_btnEnrollActionPerformed

    private void txtMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMessageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMessageActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
                new MainForm().setVisible(true);
            }
        });
    }
    
    private void SetIdentificationLimit(int nLimit)
    {
        if (nLimit == Integer.MAX_VALUE)
        {
            lblIdentificationsLimit.setText( "Identification limit: No limits" );
        }
        else
        {
            lblIdentificationsLimit.setText( "Identification limit: No limits" );
            lblIdentificationsLimit.setText("Identification limit: " + Integer.toString(nLimit) );
        }
    }
    
    private void EnableControls(boolean bEnable)    //To Enable or Disable he buttons.
    {
        btnEnroll.setEnabled( bEnable );
        btnIdentify.setEnabled( bEnable );
        btnVerify.setEnabled( bEnable );
        btnStop.setEnabled( !bEnable );
    }
    
    /**
     * Get the database directory.
     *
     * @return the database directory.
     */
    static private String GetDatabaseDir()
        throws AppException
    {
        String userDocumentFolder = Shell32Util.getFolderPath( ShlObj.CSIDL_MYDOCUMENTS );
        File companyFolder = new File( userDocumentFolder, kCompanyName );
        if( companyFolder.exists() )
        {
            if( !companyFolder.isDirectory() )
                throw new AppException( "Can not create database directory " + companyFolder.getAbsolutePath() + 
                        ". File with the same name already exist." );
        } else {
            try
            {
                companyFolder.mkdir();
            }
            catch( SecurityException e )
            {
                throw new AppException( "Can not create database directory " + companyFolder.getAbsolutePath() +
                        ". Access denied.");
            }
        }
        
        File productFolder = new File( companyFolder, kProductName );
        if( productFolder.exists() )
        {
            if( !productFolder.isDirectory() )
                throw new AppException( "Can not create database directory " + productFolder.getAbsolutePath() + 
                        ". File with the same name already exist." );
        } else {
            try
            {
                productFolder.mkdir();
            }
            catch( SecurityException e )
            {
                throw new AppException( "Can not create database directory " + productFolder.getAbsolutePath() +
                        ". Access denied.");
            }
        }

        File dataBaseFolder = new File( productFolder, kDbName );
        if( dataBaseFolder.exists() )
        {
            if( !dataBaseFolder.isDirectory() )
                throw new AppException( "Can not create database directory " + dataBaseFolder.getAbsolutePath() + 
                        ". File with the same name already exist." );
        } else {
            try
            {
                dataBaseFolder.mkdir();
            }
            catch( SecurityException e )
            {
                throw new AppException( "Can not create database directory " + dataBaseFolder.getAbsolutePath() +
                        ". Access denied.");
            }
        }

        return dataBaseFolder.getAbsolutePath();
    }

    private boolean isUserExists( String szUserName )
    {
        File f = new File( m_DbDir, szUserName );
        return f.exists();
    }

    private void CreateFile( String szFileName )
        throws AppException
    {
        File f = new File( m_DbDir, szFileName );
        try
        {
            f.createNewFile();
            f.delete();
        }
        catch( IOException e )
        {
            throw new AppException( "Can not create file " + szFileName + " in database." );
        }
        catch( SecurityException e )
        {
            throw new AppException( "Can not create file " + szFileName + " in database. Access denied" );
        }
    }
        
     private void CreateFile1( String Email )
        throws AppException
    {
        File f = new File( m_DbDir, Email );
        try
        {
            f.createNewFile();
            f.delete();
        }
        catch( IOException e )
        {
            throw new AppException( "Can not create file " +Email + " in database." );
        }
        catch( SecurityException e )
        {
            throw new AppException( "Can not create file " + Email + " in database. Access denied" );
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /** 
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon( String path )
    {
        java.net.URL imgURL = MainForm.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL );
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    class FARNValueVerifier extends InputVerifier
    {
        public boolean shouldYieldFocus( JComponent input )
        {
            String szErrorMessage = null;
            boolean bRetCode = true;
            
            JTextField tf = (JTextField)input;
            int nValue = -1;
            try
            {
                nValue = Integer.parseInt( tf.getText() );
            }
            catch( NumberFormatException e )
            {
                szErrorMessage = "Invalid FARN value. Only digits are permited";
                bRetCode = false;
            }
            if(bRetCode && (nValue > 1000 || nValue < 1) )
            {
                szErrorMessage = "Invalid FARN value. The range of value is from 1 to 1000";
                bRetCode = false;
            }
            if( !bRetCode )
            {
                java.awt.Container myFrame = input;
                do
                {
                    myFrame = myFrame.getParent();
                }while( myFrame.getParent() != null );
                JOptionPane.showMessageDialog(myFrame,
                                              szErrorMessage,
                                              ((JFrame)myFrame).getTitle(), JOptionPane.ERROR_MESSAGE);
            }            
            return bRetCode;
        }

        public boolean verify( JComponent input )
        {
            return true;
        }
    }
    
    class MyIcon implements  Icon
    {
        public MyIcon()
        {
            m_Image = null;
        }

        public void paintIcon(Component c, Graphics g, int x, int y)          //paint icon.
        {
            if( m_Image != null )
                g.drawImage( m_Image, x, y, getIconWidth(), getIconHeight(), null );
            else
                g.fillRect( x, y, getIconWidth(), getIconHeight() );
        }

        public int getIconWidth()
        {                                    //height and width of the icon.
            return 441;
        }

        public int getIconHeight()
        {
            return 556;
        }
        
        public boolean LoadImage( String path )
        {
            boolean bRetCode = false;                       //To load the image.
            Image newImg;
            try                         
            {
                File f = new File( path );
                newImg = ImageIO.read( f );
                bRetCode = true;
                setImage( newImg );
            }
            catch( IOException e )
            {
            }
            
            return bRetCode;
        }
        
        public void setImage( Image Img )
        {
            if( Img != null )
                m_Image = Img.getScaledInstance( getIconWidth(), getIconHeight(), Image.SCALE_FAST );
            else
                m_Image = null;
        }
        
        private Image m_Image;
    }
   
    private MyIcon  m_FingerPrintImage;
    
    /**
     * Contain reference for current operation object
     */
    private FutronicSdkBase m_Operation;
    
    /**
     * A database directory name.
     */
    private String m_DbDir;

    /**
     * The type of this parameter is depending from current operation. For
     * enrollment operation this is DbRecord.
     */
    private Object m_OperationObj;

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FingerImage;
    private javax.swing.JButton btnEnroll;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnIdentify;
    private javax.swing.JButton btnStop;
    private javax.swing.JButton btnVerify;
    private javax.swing.JComboBox cbMaxFrames;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JLabel lblIdentificationsLimit;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
    
}