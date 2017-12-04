import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class Chat extends JPanel implements KeyListener{
    protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";
    private static String s = "";

    // TCP Components
    public static ServerSocket server = null;
    public static Socket socket = null;
    public static BufferedReader in = null;
    public static PrintWriter out = null;

    public static StringBuffer toAppend = new StringBuffer("");
    public static StringBuffer toSend = new StringBuffer("");

    public Chat(int isServer, int port, String serverIP) {
        super(new GridBagLayout());

        this.setFocusable(true);
        this.setRequestFocusEnabled(true);

        textArea = new JTextArea(30, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        // textArea.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        // scrollPane.setOpaque(false);

        textField = new JTextField(20);
        // textField.setOpaque(false);
        textField.addKeyListener(this);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(scrollPane, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(textField, c);

        setOpaque(false);

        try {
        
            if(isServer == 1){
                this.server = new ServerSocket(port);
                this.socket = server.accept();
            }
            else if(isServer == 0){
                this.socket = new Socket(serverIP, port);
            }

            this.in = new BufferedReader(new 
                InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            
        } catch (Exception e) {
            //TODO: handle exception
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 100);
    }

    public void keyTyped(KeyEvent key){
    }

    public void keyReleased(KeyEvent key){
    }

    public void keyPressed(KeyEvent key){

        if(key.getKeyCode() == KeyEvent.VK_ESCAPE){
            this.setVisible(false);
        }
        if(key.getKeyCode() == KeyEvent.VK_ENTER){
            String text = textField.getText();
            textArea.append(text + newline);
            textField.setText("");
    
            textArea.setCaretPosition(textArea.getDocument().getLength());

        }

    }

    public JTextField getTextField(){
        return textField;
    }

    // Thread-safe way to append to the chat box
    private static void appendToChatBox(String s) {
        synchronized (toAppend) {
        toAppend.append(s);
        }
    }

    /////////////////////////////////////////////////////////////////

    // Add text to send-buffer
    private static void sendString(String s) {
        synchronized (toSend) {
        toSend.append(s + "\n");
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    // private static void createAndShowGUI() {
    //     //Create and set up the window.
    //     JFrame frame = new JFrame("Chat");
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //     //Add contents to the window.
    //     frame.add(new Chat());

    //     //Display the window.
    //     frame.pack();
    //     frame.setVisible(true);
    // }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                while(true){
                    try {
                        // Send data
                        if (toSend.length() != 0) {
                            out.print(toSend); out.flush();
                            toSend.setLength(0);
                        }

                        // Receive data
                        if (in.ready()) {
                            s = in.readLine();
                            if ((s != null) &&  (s.length() != 0)) {
                                // Otherwise, receive what text
                                appendToChatBox("INCOMING: " + s + "\n");
                            }
                        }
                  
                    } catch (IOException e) {
                        //TODO: handle exception
                    }
                }
            }
        });
    }
}


/*

Reference:
    - docs.oracle.com

*/