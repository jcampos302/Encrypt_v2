/* 
* @author = Jorge E. Campos II
* @name = FileEncrypt.java
* @version = FileEncrypt_v2
*
* This is a file encryption program that uses AES to encrypt files.
* 
* Usage:
* $ javac *.java 
* $ java FileEncrypt
*
*/

// Long list of imports
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;




// Frame class Starts here
class FileEncryptionFrame extends JFrame implements ActionListener {
    
    // Labels
    JButton Encrypt,Decrypt, OpenFile;
    JLabel shiftLabel;
    JTextArea shiftText, Filelog, statusLog;
    Container contentPane;
    JFileChooser fc;
    File file, inputFile;
    String key, Varakey;
    int keysize, returnVal;
    

    // Frame class
    public FileEncryptionFrame() {
        setSize(400, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = getContentPane();

        Encrypt = new JButton("Encrypt");
        Encrypt.setBounds(50, 200, 90, 30);
        Encrypt.addActionListener(this);

        Decrypt = new JButton("Decrypt");
        Decrypt.setBounds(270, 200, 90, 30);
        Decrypt.addActionListener(this);

        shiftLabel = new JLabel("Enter Password");
        shiftLabel.setBounds(25, 125, 225, 30);
        shiftLabel.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        
        shiftText = new JTextArea(5,20);
        shiftText.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        shiftText.setBounds(175, 125, 200, 30);
        shiftText.setEditable(true);

        OpenFile = new JButton("Open File...");
        OpenFile.setBounds(25, 50, 125, 30);
        OpenFile.addActionListener(this);

        Filelog = new JTextArea(5, 20);
        Filelog.setBounds(150, 50, 225, 30);
        Filelog.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        Filelog.setEditable(false);

        statusLog = new JTextArea(5,20);
        statusLog.setBounds(35,275,325,30);
        statusLog.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        statusLog.setEditable(false);

        contentPane.add(Encrypt);
        contentPane.add(Decrypt);
        contentPane.add(OpenFile);
        contentPane.add(Filelog);
        contentPane.add(shiftText);
        contentPane.add(shiftLabel);
        contentPane.add(statusLog);

        statusLog.setText("Welcome to Encrypt\n");

    }
    // File manager class
    public void OpenFile(){
        returnVal = fc.showOpenDialog(FileEncryptionFrame.this);
 
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            Filelog.setText("Opening: " +file.getName() +".\n");
        } else {
            Filelog.setText("Open Command Cancelled by user.\n");
        }
        Filelog.setCaretPosition(Filelog.getDocument().getLength());
            
    }
    // Key Checker class
    // This checks key size for AES
    public void keyCheck(String key) {
        keysize = key.length();

        if(keysize == 16){
            Varakey = key;
            
        }else if(keysize <16){
            for(int i = keysize; i<16; i++){
                key = key + 0;
            }
            Varakey = key;
        }else if(keysize > 16){
            Varakey = key.substring(0, 16);
            
        }
    }
    // This is where the encryption and decryption happens using cipher libary.
    static void fileProcessor(int cipherMode,String key,File inputFile){
        try {
              Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
              Cipher cipher = Cipher.getInstance("AES");
              cipher.init(cipherMode, secretKey);
   
              FileInputStream inputStream = new FileInputStream(inputFile);
              byte[] inputBytes = new byte[(int) inputFile.length()];
              inputStream.read(inputBytes);
   
              byte[] outputBytes = cipher.doFinal(inputBytes);
   
              FileOutputStream outputStream = new FileOutputStream(inputFile);
              outputStream.write(outputBytes);
   
              inputStream.close();
              outputStream.close();
   
           } catch (NoSuchPaddingException | NoSuchAlgorithmException 
                        | InvalidKeyException | BadPaddingException
                    | IllegalBlockSizeException | IOException e) {
           e.printStackTrace();
               }
        }
    // Encryption class
    public void Encryption(String key, File inputFile){
        
        try{
            fileProcessor(Cipher.ENCRYPT_MODE,key,inputFile);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
                ex.printStackTrace();
        
        }
    }
    // Decryption class
    public void Decryption(String key, File inputFile){
        try{
            fileProcessor(Cipher.DECRYPT_MODE,key,inputFile);
        }catch(Exception ex){

        }
    }
    // Action listener class
    public void actionPerformed(ActionEvent e){
        fc = new JFileChooser();
        key = shiftText.getText();
        if (e.getSource() == OpenFile) {  
            OpenFile();
        }
        else if (e.getSource() == Encrypt) {
            if(file != null){
                keyCheck(key);
                Encryption(Varakey,file);
                statusLog.setText("File Encrypted");
            }else{
                statusLog.setText("Error Please Select a File.\n");
            }
        }
        else if (e.getSource() == Decrypt) {
            if(file != null){
                keyCheck(key);
                Decryption(Varakey, file);
                statusLog.setText("File Decrypted");
            } else{
                statusLog.setText("Error Please Select a File.\n");
            }
        }
    }
}