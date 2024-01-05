package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUIClient;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.utils.Parser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Game initialization GUI class
 */
public class Initialization extends JDialog implements Observer<Object> {
    private JPanel mainPanel; //This contains server status on bottom and container on top
    private final GUIClient guiClient;
    private final JLabel serverStatusLabel = new JLabel(); //Label for server status
    final JButton sendButton = new JButton("Next");
    private JPanel contentPanel; //This contains all elements
    private JLabel messageLabel;
    private JTextField jTextField;
    private String returnedMessage = "";

    /**
     * Class constructor
     * It sets the title for the GUI dialog and calls the set layout method
     * @param guiClient Main gui client class
     */
    public Initialization(GUIClient guiClient) {
        setTitle("Initialization");
        setPreferredSize(new Dimension(400, 170));
        setResizable(false);
        this.guiClient = guiClient;
        //customCursor();
        setLayout();
    }

    /**
     *
     * @return the main guiClient
     */
    public GUIClient getGuiClient() {
        return guiClient;
    }


    /**
     * This method sets the main layout
     * It creates a new BorderLayout main panel, within which there is the server status label,
     * the message label (North position) and a center space used to configure elements.
     * On the bottom we added a button which sends the response to  the server
     */
    private void setLayout() {
        mainPanel = new JPanel(true);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(10,10,10,10)); //Add padding
        mainPanel.setLayout(new BorderLayout(10, 10));
        serverStatusLabel.setText("Connection to server established");
        serverStatusLabel.setForeground(new Color(1,140,8));
        mainPanel.add(serverStatusLabel, BorderLayout.SOUTH);
        contentPanel = new JPanel(true);
        contentPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        contentPanel.add(sendButton, BorderLayout.SOUTH);
        messageLabel = new JLabel("prova");
        contentPanel.add(messageLabel, BorderLayout.NORTH);
        contentPanel.setOpaque(false);
        sendButton.setBackground(new Color(76, 166, 220));
        sendButton.setOpaque(true);
        sendButton.setPreferredSize(new Dimension(150, 25));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBorder(new LineBorder(new Color(62, 136, 180), 2));
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.send(returnedMessage);
            }
        });
        add(mainPanel);
        pack();
        this.getRootPane().setDefaultButton(sendButton);
    }

    /**
     * {@inheritDoc}
     * This method also checks if the received message is a String, in this case, if it's an error, it will be shown
     *            in a JOptionPane
     *            Otherwise the message is sent to setPanelContent method
     *
     * @param msg is the message received
     */
    @Override
    public void update(Object msg) {
        if(msg instanceof ViewMessage) {
            setPanelContent(((ViewMessage) msg).getMessageType(), ((ViewMessage) msg).getMessage());
        } else if (msg instanceof String) {
            if (!guiClient.isConfig()){
                String message = (String) msg;
                if(message.startsWith("ERROR: ")){
                    String errorString = message.substring(7);
                    JOptionPane.showMessageDialog(this,
                            errorString,
                            "Command error",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    /**
     * This method clears the center panel contents every time a message is sent to the server.
     */
    private void resetPanelContent(){
        Component[] components = contentPanel.getComponents();
        for (Component component : components) {
            if(component != messageLabel && component!=sendButton){
                contentPanel.remove(component);
            }
        }
        this.setSize(400, 170);

    }

    /**
     * Add a custom cursor for this interface
     */
    public void customCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.getBestCursorSize(32, 32);
        Image image = toolkit.getImage("images/godpower_hand_select2.png");
        Point hotspot = new Point(0,0);
        Cursor cursor = toolkit.createCustomCursor(image, hotspot, "hand");
        setCursor(cursor);
    }

    private void setPanelContent(MessageType messageType, String message){
        resetPanelContent();
        switch (messageType){
            case PLAYER_NAME:
                jTextField = new JTextField();
                contentPanel.add(jTextField, BorderLayout.CENTER);
                messageLabel.setText("Please, insert your name");
                jTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        JTextField textField = (JTextField) e.getSource();
                        returnedMessage = textField.getText();
                    }
                });
                break;
            case JOIN_OR_CREATE_LOBBY:
                JPanel buttonPanel = new JPanel();
                buttonPanel.setOpaque(false);
                buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
                final JButton createLobby = new JButton("Create a new lobby");
                final JButton joinLobby = new JButton("Join a lobby");
                createLobby.setPreferredSize(new Dimension(50,60));
                buttonPanel.add(createLobby);
                buttonPanel.add(joinLobby);
                messageLabel.setText("");
                contentPanel.add(buttonPanel, BorderLayout.CENTER);
                createLobby.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        returnedMessage = "1";
                        createLobby.setEnabled(false);
                        joinLobby.setEnabled(true);
                    }
                });
                joinLobby.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        returnedMessage = "2";
                        createLobby.setEnabled(true);
                        joinLobby.setEnabled(false);
                    }
                });
                break;
            case LOBBY_SELECTOR:
                messageLabel.setText("Please select the lobby you want to join");
                JPanel lobbyPanel = new JPanel(new BorderLayout(10,10));//new GridLayout(3,1,0,0));
                /*final JLabel instructions = new JLabel();
                instructions.setText("Legend: Id \t| Name \t| Free places \t| Play mode \t");
                instructions.setSize(70,45);*/
                lobbyPanel.setOpaque(false);
                JPanel commandPanel = new JPanel(new GridLayout(2,1,0,2));
                JButton refreshButton = new JButton("Refresh");
                refreshButton.setSize(70, 45);
                refreshButton.setBackground(new Color(76, 166, 220));
                refreshButton.setForeground(Color.WHITE);
                refreshButton.setBorder(new LineBorder(new Color(62, 136, 180), 2));
                refreshButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        guiClient.send("0");
                        guiClient.send("2");
                    }
                });
                commandPanel.add(refreshButton, BorderLayout.NORTH);
                JButton backButton = new JButton("Back");
                backButton.setSize(70, 45);
                backButton.setBackground(new Color(76, 166, 220));
                backButton.setForeground(Color.WHITE);
                backButton.setBorder(new LineBorder(new Color(62, 136, 180), 2));
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        guiClient.send("0");
                    }
                });
                commandPanel.add(backButton, BorderLayout.NORTH);
                lobbyPanel.add(commandPanel,BorderLayout.NORTH);
                //lobbyPanel.add(instructions, BorderLayout.PAGE_END);
                String[] columnNames = { "Id", "Name", "Free place", "Play mode"};
                String [][] data = Parser.parseLobbies(message);
                final JTable table = new JTable(data, columnNames){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                    public void valueChanged(ListSelectionEvent event) {
                        returnedMessage = table.getValueAt(table.getSelectedRow(), 0).toString();
                    }
                });
                table.setBounds(30, 40, 200, 300);
                lobbyPanel.add(new JScrollPane(table), BorderLayout.CENTER);
                this.setSize(400, 400);
                contentPanel.add(lobbyPanel, BorderLayout.CENTER);
                break;
            case LOBBY_NAME:
                messageLabel.setText("Please insert a name for the lobby");
                jTextField = new JTextField();
                contentPanel.add(jTextField, BorderLayout.CENTER);
                jTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        JTextField textField = (JTextField) e.getSource();
                        returnedMessage = textField.getText();
                    }
                });
                break;
            case NUMBER_OF_PLAYERS:
                messageLabel.setText("Select how many player you want in the lobby");
                Integer[] num = new Integer[2];
                num[0] = 2; num[1] = 3;
                JComboBox<Integer> numPlayers = new JComboBox<>(num);
                returnedMessage = "2";
                numPlayers.addItemListener(new ItemListener() {
                    // Listening if a new items of the combo box has been selected.
                    public void itemStateChanged(ItemEvent event) {
                        Object item = event.getItem();
                        returnedMessage = item.toString();
                    }
                });
                contentPanel.add(numPlayers, BorderLayout.CENTER);
                break;
            case SIMPLE_OR_NOT:
                messageLabel.setText("Would you like to play in simple mode?");
                JCheckBox simpleMode = new JCheckBox("Use simple mode");
                returnedMessage = "n";
                simpleMode.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        AbstractButton abstractButton =
                                (AbstractButton)e.getSource();
                        ButtonModel buttonModel = abstractButton.getModel();
                        boolean selected = buttonModel.isSelected();
                        if(selected){
                            returnedMessage = "y";
                        } else {
                            returnedMessage = "n";
                        }
                    }
                });
                contentPanel.add(simpleMode, BorderLayout.CENTER);

                break;
            default:
                guiClient.closeInitializator();
                dispose();
                return;
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
