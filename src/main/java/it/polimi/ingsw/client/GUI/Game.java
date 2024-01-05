package it.polimi.ingsw.client.GUI;

import com.google.gson.Gson;
import it.polimi.ingsw.client.GUIClient;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.utils.Parser;

import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Timer;

/**
 * GUI Class
 */
public class Game extends JFrame implements Observer<Object> {
    private final Dimension windowsSize;
    private final GUIClient guiClient;
    private boolean initedBoard = false;
    private boolean isSimplePlay = true;
    private final ArrayList<String> opponentsNames = new ArrayList<>();
    private final HashMap<String, String> opponentGods = new HashMap<>();
    private final HashMap<String, String> myGod = new HashMap<>();
    private JPanel mainPanel, leftPanel, centerPanel, rightPanel, overlayPanel, initialBoardPanel, godPanel, endGamePanel, endGamePanelPlayers, exitGame, playAgain;
    private JLabel messageLabel, background, endGameImage, southPanel;
    private JButton startPlayBtn;
    private MessageType messageType = MessageType.PLAYER_NAME;
    private Player player;
    private ClientConfigurator clientConfigurator;
    private String response;
    private final ArrayList<String> multipleSelections = new ArrayList<>();
    private String chosenCellX;
    private String chosenCellY;
    private final JButton[][] board = new JButton[5][5];
    private double value = 0;
    protected int selectedWorker = -1;
    private final HashMap<JButton, Integer> cellsX = new HashMap<>();
    private final HashMap<JButton, Integer> cellsY = new HashMap<>();
    private GraphicsEnvironment ge;
    private Font customFont;
    private HashMap<String,String> godsFunction;
    int labelWidth = 0;
    int labelHeight = 0;

    /**
     * Class constructor
     * @param guiClient GUI Application class instance
     * @param width screen width
     */
    public Game(final GUIClient guiClient, int width){
        Toolkit tk = Toolkit.getDefaultToolkit();
        if(width>0 && width<100){
            width=(int)(tk.getScreenSize().getWidth()*width)/100;
        }
        windowsSize=new Dimension(width, (width/16)*9);
        customCursor();
        Image img = Toolkit.getDefaultToolkit().getImage(Game.class.getResource("/images/icon.png"));
        this.setIconImage(img);
        this.guiClient = guiClient;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Santorini");
        setResizable(false);
        setLayout();
    }

    /**
     *
     * @param player the playerof this game
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     *
     * @param clientConfigurator the configuration message recived from the server
     */
    public void setClientConfigurator(ClientConfigurator clientConfigurator) {
        this.clientConfigurator = clientConfigurator;
    }

    private void customCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.getBestCursorSize(32, 32);
        Image image = Toolkit.getDefaultToolkit().getImage((getClass().getClassLoader().getResource("images/godpower_hand_select2.png"))).getScaledInstance(32, 32,
                Image.SCALE_SMOOTH);
        Point hotspot = new Point(0,0);
        Cursor cursor = toolkit.createCustomCursor(image, hotspot, "hand");
        setCursor(cursor);
    }

    private void setJButtonProperties(JButton button){
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
    }

    private void setJPanelProperties(JPanel panel, int hgap, int vgap, int width, int height){
        hgap=(int)((float)(hgap*windowsSize.getHeight()))/1080;
        vgap=(int)((float)(vgap*windowsSize.getHeight()))/1080;
        panel.setLayout(new BorderLayout(hgap, vgap));
        panel.setPreferredSize(new Dimension(width, height));
        panel.revalidate();
        panel.repaint();
        panel.setSize(width, height);
        panel.revalidate();
        panel.repaint();
        panel.setOpaque(false);
        panel.revalidate();
        panel.repaint();
    }

    private void setJLabelProperties(JLabel label, int hgap, int vgap, float fontDimension, Color color, int width, int height){
        fontDimension=((float)(fontDimension*windowsSize.getHeight()))/1080;
        hgap=(int)((float)(hgap*windowsSize.getHeight()))/1080;
        vgap=(int)((float)(vgap*windowsSize.getHeight()))/1080;
        label.setLayout(new BorderLayout(hgap, vgap));
        label.setPreferredSize(new Dimension(width, height));
        label.revalidate();
        label.repaint();
        label.setSize(width, height);
        label.revalidate();
        label.repaint();
        label.setOpaque(false);
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("fonts/LillyBelle.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT,is).deriveFont(fontDimension);
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.CENTER);
        label.setFont(customFont);
        label.setForeground(color);
        label.revalidate();
        label.repaint();
    }

    private void setLayout() {
        background = new JLabel();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d=windowsSize;
        setContentPane(background);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //togliere il commento qua sotto per metterlo completo full screen.
        //this.setUndecorated(true);
        Image dimg = Toolkit.getDefaultToolkit().getImage((getClass().getClassLoader().getResource("images/home/SantoriniHomeBackground.png"))).getScaledInstance(d.width, d.height,
                Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        background.setIcon(imageIcon);
        background.revalidate();
        background.repaint();
        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setSize(d);
        mainPanel.revalidate();
        mainPanel.repaint();
        mainPanel.setOpaque(false);

        add(mainPanel);

        startPlayBtn = new JButton();
        setJButtonProperties(startPlayBtn);
        startPlayBtn.setSize(526*3/4,644*3/4);
        startPlayBtn.revalidate();
        startPlayBtn.repaint();
        Image normal = Toolkit.getDefaultToolkit().getImage((getClass().getClassLoader().getResource("images/button-play-normal.png"))).getScaledInstance(startPlayBtn.getWidth(), startPlayBtn.getHeight(),
                Image.SCALE_SMOOTH);
        Image pressed = Toolkit.getDefaultToolkit().getImage((getClass().getClassLoader().getResource("images/button-play-down.png"))).getScaledInstance(startPlayBtn.getWidth(), startPlayBtn.getHeight(),
                Image.SCALE_SMOOTH);
        startPlayBtn.setIcon(new ImageIcon(normal));
        startPlayBtn.revalidate();
        startPlayBtn.repaint();
        startPlayBtn.setPressedIcon(new ImageIcon(pressed));
        startPlayBtn.revalidate();
        startPlayBtn.repaint();


        mainPanel.add(startPlayBtn, BorderLayout.SOUTH);

        //Add logo
        Image dimg2 = Toolkit.getDefaultToolkit().getImage((getClass().getClassLoader().getResource("images/home/SantoriniHomeLogo.png"))).getScaledInstance(d.width*3/4, d.height/3,
                Image.SCALE_SMOOTH);
        ImageIcon imageIcon2 = new ImageIcon(dimg2);
        mainPanel.add(new JLabel(imageIcon2), BorderLayout.NORTH);

        startPlayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.openInitializator();
            }
        });


        pack();
        this.getRootPane().setDefaultButton(startPlayBtn);

    }

    private ImageIcon loadImage(String path, int width, int height){
        ImageIcon imageIcon;

        Image dimg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(path)).getScaledInstance(width, height, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(dimg);

        return imageIcon;
    }

    private void initGame() {
        //this.guiClient.removeExcept(this);
        value = 0.484375;
        this.setEnabled(true);
        clearGui();
        JLabel background = new JLabel();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = windowsSize;
        this.setSize(windowsSize);
        revalidate();
        repaint();
        setContentPane(background);
        add(mainPanel);

        background.setIcon(loadImage("images/SantoriniBoard.png", d.width, d.height));
        background.revalidate();
        background.repaint();

        centerPanel = new JPanel(true);
        setJPanelProperties(centerPanel, 0,0,(int)(mainPanel.getWidth() * value),(mainPanel.getHeight()));//(int)(mainPanel.getHeight() - mainPanel.getHeight() * 0.1389));
        centerPanel.revalidate();
        centerPanel.repaint();

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        value = 0.1389;
        southPanel = new JLabel(){
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor( getBackground() );
                super.paintComponent(g);
                Graphics2D graphics = (Graphics2D) g;
                Dimension arcs = new Dimension(20, 20);
                graphics.setColor(getBackground());
                graphics.fillRoundRect(southPanel.getWidth()/4, southPanel.getHeight()/4, getWidth()/2, getHeight()/2, arcs.width, arcs.height);
                graphics.setColor(getForeground());
            }
        };
        setJLabelProperties(southPanel, 10,10,40f, new Color(200,0,0),mainPanel.getWidth(), (int)(mainPanel.getHeight() * value));
        southPanel.setOpaque(false);
        southPanel.setBackground(new Color(255, 0, 0, 0));
        southPanel.revalidate();
        southPanel.repaint();

        mainPanel.add(southPanel, BorderLayout.SOUTH);
        if (labelWidth == 0 && centerPanel.getWidth() == (int)(mainPanel.getWidth() * 0.484375) && centerPanel.getHeight() != mainPanel.getHeight()){
            labelWidth = centerPanel.getWidth();
            labelHeight = (int)(centerPanel.getHeight() * value);
        } else if (labelWidth == 0) {
            labelWidth = (int)(mainPanel.getWidth() * 0.484375);
            labelHeight = (int)(mainPanel.getHeight() * value);
        }

        value = 0.2942708;
        leftPanel = new JPanel(true);

        setJPanelProperties(leftPanel,10,50, (int)(mainPanel.getWidth() * value), mainPanel.getHeight());
        leftPanel.revalidate();
        leftPanel.repaint();

        mainPanel.add(leftPanel, BorderLayout.WEST);

        rightPanel = new JPanel(true);
        setJPanelProperties(rightPanel,10,10,(int)(mainPanel.getWidth() * value), mainPanel.getHeight());
        rightPanel.revalidate();
        rightPanel.repaint();

        mainPanel.add(rightPanel, BorderLayout.EAST);

        value = 0.12;
        messageLabel = new JLabel();
        setJLabelProperties(messageLabel,10,10, 25f, Color.WHITE,centerPanel.getWidth(), (int)(centerPanel.getHeight() * value));

        messageLabel.setIcon(loadImage("images/Santorini_GenericPopup.png", labelWidth, labelHeight));
        messageLabel.revalidate();
        messageLabel.repaint();

        centerPanel.add(messageLabel, BorderLayout.NORTH);

        centerPanel.revalidate();
        centerPanel.repaint();
        addInitialBoard();
        mainPanel.revalidate();
        mainPanel.repaint();
        revalidate();
        repaint();
    }

    private void clearGui(){
        for(Component component: mainPanel.getComponents()){
            mainPanel.remove(component);
        }
        getContentPane().removeAll();
        remove(mainPanel);
    }

    private void addMyCard(final GameMessage gameMessage) {
        try {
            JPanel myPanel = new JPanel(true);
            myPanel.setOpaque(false);
            myPanel.setLayout(new BorderLayout());
            myPanel.setSize(leftPanel.getWidth(), leftPanel.getHeight() / 3);
            myPanel.revalidate();
            myPanel.repaint();
            JPanel playerPanel = new JPanel(true);
            playerPanel.setSize(myPanel.getWidth() / 2, myPanel.getHeight());
            playerPanel.revalidate();
            playerPanel.repaint();
            playerPanel.setOpaque(false);
            playerPanel.setLayout(new BorderLayout(0, 0));
            JLabel nameLabel = new JLabel();
            JLabel godLabel = new JLabel();
            value = 0.2;
            setJLabelProperties(nameLabel,0,0,25f,Color.WHITE,(playerPanel.getWidth()), (int) (playerPanel.getHeight() * value) );
            value = 2;
            setJLabelProperties(godLabel, 0,0,25f, Color.WHITE,(playerPanel.getWidth()), (int) (playerPanel.getHeight() * value) / 2);
            String path;
            if (isSimplePlay){
                path="images/enemy_player.png";
            }
            else {
                myGod.put(gameMessage.getPlayer().getPlayerName(), gameMessage.getPlayer().getGodCard().getName());
                //System.out.println(gameMessage.getPlayer().getPlayerName());
                String godName = myGod.get(gameMessage.getPlayer().getPlayerName());
                path="images/God_with_frame/" + Parser.toCapitalize(godName) + ".png";
            }
            nameLabel.setIcon(loadImage("images/myNameFrame.png", nameLabel.getWidth(), nameLabel.getHeight()));
            nameLabel.revalidate();
            nameLabel.repaint();
            godLabel.setIcon(loadImage(path, godLabel.getWidth(), godLabel.getHeight()));
            godLabel.revalidate();
            godLabel.repaint();
            if (!isSimplePlay){
                godLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        String god = myGod.get(gameMessage.getPlayer().getPlayerName());
                        String string;
                        string = correspondingGod(god);
                        //string = godsFunction.get(god);
                        JOptionPane.showMessageDialog(Game.this,string,Parser.toCapitalize(god) + "'s information",JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            }
            int maxLength = Math.min(gameMessage.getPlayer().getPlayerName().length(), 9);
            nameLabel.setText(gameMessage.getPlayer().getPlayerName().substring(0,maxLength));
            playerPanel.add(nameLabel, BorderLayout.NORTH);
            playerPanel.add(godLabel, BorderLayout.CENTER);
            myPanel.add(playerPanel, BorderLayout.NORTH);
            myPanel.setAlignmentX(SwingConstants.CENTER);
            leftPanel.add(myPanel, BorderLayout.NORTH);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String correspondingGod(String god){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("GodsDescription.json");
        InputStreamReader isReader = new InputStreamReader(inputStream);
        //Creating a BufferedReader object
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        try {
            while((str = reader.readLine())!= null){
                sb.append(str);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        Gson gson = new Gson();
        HashMap<String,String> gods = gson.fromJson(sb.toString(),HashMap.class);
        return gods.get(Parser.toCapitalize(god));
    }

    private void addOpponents() {
        try {
            JPanel opponentsPanel = new JPanel(true);
            opponentsPanel.setOpaque(false);
            opponentsPanel.setLayout(new GridLayout(opponentGods.size(), 1, 0, 0));
            opponentsPanel.setSize(rightPanel.getWidth()/2, 230 * opponentGods.size());
            opponentsPanel.revalidate();
            opponentsPanel.repaint();
            for (String opponentName : opponentGods.keySet()) {
                //System.out.println(opponentName);
                String godName = opponentGods.get(opponentName);
                final String opponentNameForMouseClicked = opponentName;
                JPanel playerPanel = new JPanel(true);
                playerPanel.setSize(opponentsPanel.getWidth() * 3 / 2, opponentsPanel.getHeight() * 11/10 / opponentGods.size());
                playerPanel.revalidate();
                playerPanel.repaint();
                playerPanel.setOpaque(false);
                playerPanel.setLayout(new BorderLayout(0, 0));
                JLabel nameLabel = new JLabel();
                JLabel godLabel = new JLabel();
                value = 0.304347826;
                setJLabelProperties(nameLabel, 0,0,20f, Color.WHITE,(playerPanel.getWidth()) / opponentGods.size() + 1, (int)(playerPanel.getHeight() * value));
                value = 1.7391304347826;
                setJLabelProperties(godLabel, 0,0, 20f, Color.WHITE,(playerPanel.getWidth()) / opponentGods.size() + 1, (int)(playerPanel.getHeight() * value) / opponentGods.size() + 1);
                String filename;
                if (clientConfigurator.getOpponentsNames().get(opponentName).equals("red")){
                    filename = "images/opponentNameFrame.png";
                }
                else {
                    filename = "images/opponentGreenNameFrame.png";
                }
                nameLabel.setIcon(loadImage(filename, nameLabel.getWidth(), nameLabel.getHeight()));
                nameLabel.revalidate();
                nameLabel.repaint();
                godLabel.setIcon(loadImage("images/Podium/" + Parser.toCapitalize(godName) + "_podium.png", godLabel.getWidth(), godLabel.getHeight()));
                godLabel.revalidate();
                godLabel.repaint();
                if (!isSimplePlay){
                    godLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            super.mouseClicked(e);
                            String god = opponentGods.get(opponentNameForMouseClicked);
                            String string;
                            string = correspondingGod(god);
                            //string = godsFunction.get(Parser.toCapitalize(god));
                            JOptionPane.showMessageDialog(Game.this,string,Parser.toCapitalize(god) + "'s information",JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
                int maxLength = Math.min(opponentName.length(), 9);
                nameLabel.setText(opponentName.substring(0,maxLength));
                playerPanel.add(nameLabel, BorderLayout.SOUTH);
                playerPanel.add(godLabel, BorderLayout.CENTER);
                opponentsPanel.add(playerPanel, BorderLayout.SOUTH);
            }
            opponentsPanel.setAlignmentX(SwingConstants.CENTER);
            rightPanel.add(opponentsPanel, BorderLayout.SOUTH);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addOpponentsSimpleMode() {
        try {
            String filename;
            JPanel opponentsPanel = new JPanel(true);
            opponentsPanel.setOpaque(false);
            opponentsPanel.setLayout(new GridLayout(opponentsNames.size(), 1, 0, 0));
            opponentsPanel.setSize(rightPanel.getWidth()/2, rightPanel.getHeight()*2/3);
            opponentsPanel.revalidate();
            opponentsPanel.repaint();
            for (String opponentName : opponentsNames) {
                JPanel playerPanel = new JPanel(true);
                playerPanel.setSize(opponentsPanel.getWidth() * 3 / 2, opponentsPanel.getHeight() / opponentsNames.size());
                playerPanel.revalidate();
                playerPanel.repaint();
                playerPanel.setOpaque(false);
                playerPanel.setLayout(new BorderLayout(0, 0));
                JLabel nameLabel = new JLabel();
                JLabel enemyLabel = new JLabel();
                value = 0.2;
                setJLabelProperties(nameLabel,0,0,20f,Color.WHITE,(playerPanel.getWidth() * 9/10), (int)(playerPanel.getHeight() * value) );
                value = 0.75;
                setJLabelProperties(enemyLabel, 0,0,20f, Color.WHITE,(playerPanel.getWidth() * 9/(10*opponentsNames.size())), (int)(playerPanel.getHeight() * value));
                BufferedImage enemy, frame;
                if (clientConfigurator.getOpponentsNames().get(opponentName).equals("red")){
                    filename = "images/opponentNameFrame.png";
                }
                else {
                    filename = "images/opponentGreenNameFrame.png";
                }
                nameLabel.setIcon(loadImage(filename,nameLabel.getWidth(), nameLabel.getHeight()));
                nameLabel.revalidate();
                nameLabel.repaint();
                enemyLabel.setIcon(loadImage("images/enemy_player.png", enemyLabel.getWidth(), enemyLabel.getHeight()));
                enemyLabel.revalidate();
                enemyLabel.repaint();
                int maxLength = Math.min(opponentName.length(), 9);
                nameLabel.setText(opponentName.substring(0,maxLength));
                playerPanel.add(nameLabel, BorderLayout.SOUTH);
                playerPanel.add(enemyLabel, BorderLayout.CENTER);
                opponentsPanel.add(playerPanel, BorderLayout.SOUTH);
            }
            opponentsPanel.setAlignmentX(SwingConstants.CENTER);
            rightPanel.add(opponentsPanel, BorderLayout.SOUTH);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setMessageOnPopup(String message) {
        try{
            messageLabel.setText(message);
            messageLabel.revalidate();
            messageLabel.repaint();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }


    private void resetOverlayPanel(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                try{
                    for (ActionListener actionListener :((JButton)overlayPanel.getComponent(i*5+j)).getActionListeners()) {
                        ((JButton)overlayPanel.getComponent(i*5+j)).removeActionListener(actionListener);
                    }
                    overlayPanel.getComponent(i*5+j).setVisible(false);
                }catch(ArrayIndexOutOfBoundsException e){
                    //ignore
                }

            }
        }
    }

    private void resetBoardPanel(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                try{
                    for (ActionListener actionListener:((JButton)initialBoardPanel.getComponent(i*5+j)).getActionListeners()) {
                        ((JButton)initialBoardPanel.getComponent(i*5+j)).removeActionListener(actionListener);
                    }
                }catch(ArrayIndexOutOfBoundsException e){
                    //ignore
                }

            }
        }
    }

    private void resetGodPanel(){
        try{
            for (Component component: godPanel.getComponents()){
                godPanel.remove(component);
            }
            leftPanel.remove(godPanel);
            leftPanel.revalidate();
            leftPanel.repaint();
        }catch(NullPointerException e){
            //ignore
        }

    }

    private void addOverlayPanel() {
        overlayPanel = new JPanel(true);
        overlayPanel.setLayout(new GridLayout(5,5,0,0));
        overlayPanel.setPreferredSize(new Dimension(centerPanel.getWidth(), centerPanel.getHeight()));
        overlayPanel.revalidate();
        overlayPanel.repaint();
        overlayPanel.setSize(centerPanel.getWidth(), centerPanel.getHeight());
        overlayPanel.revalidate();
        overlayPanel.repaint();
        overlayPanel.setOpaque(false);
        overlayPanel.setBorder(BorderFactory.createEmptyBorder());
        centerPanel.add(overlayPanel, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                final JButton cell = new JButton();
                setJButtonProperties(cell);
                cell.setPreferredSize(new Dimension(overlayPanel.getWidth() / 5, overlayPanel.getHeight() / 5));
                cell.revalidate();
                cell.repaint();
                cell.setSize(overlayPanel.getWidth() / 5,overlayPanel.getHeight() / 5);
                cell.revalidate();
                cell.repaint();
                cell.setIcon(loadImage("images/blue_square.png",cell.getWidth(), cell.getHeight() ));
                cell.revalidate();
                cell.repaint();
                overlayPanel.add(cell,BorderLayout.CENTER);
                overlayPanel.revalidate();
                overlayPanel.repaint();
                cell.setVisible(false);
                cellsX.put(cell,i);
                cellsY.put(cell,j);
            }
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void addInitialBoard(){
        initialBoardPanel = new JPanel();
        initialBoardPanel.setLayout(new GridLayout(5,5,0,0));
        initialBoardPanel.setPreferredSize(new Dimension(centerPanel.getWidth(), centerPanel.getHeight()));
        initialBoardPanel.revalidate();
        initialBoardPanel.repaint();
        initialBoardPanel.setSize(centerPanel.getWidth(), centerPanel.getHeight());
        initialBoardPanel.revalidate();
        initialBoardPanel.repaint();
        initialBoardPanel.setOpaque(false);
        initialBoardPanel.setBorder(BorderFactory.createEmptyBorder());
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                board[i][j] = new JButton();
                setJButtonProperties(board[i][j]);
                board[i][j].setPreferredSize(new Dimension(initialBoardPanel.getWidth()/5, initialBoardPanel.getHeight()/5));
                board[i][j].revalidate();
                board[i][j].repaint();
                board[i][j].setSize(initialBoardPanel.getWidth()/5, initialBoardPanel.getHeight()/5);
                board[i][j].revalidate();
                board[i][j].repaint();
                board[i][j].setEnabled(false);
                board[i][j].setVisible(false);
                initialBoardPanel.add(board[i][j]);
            }
        }
        centerPanel.add(initialBoardPanel,BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void drawCards(){
        final HashMap<JButton, Integer> gods = new LinkedHashMap<>();
        setMessageOnPopup("Please select " + clientConfigurator.getNumberOfPlayer() + " god cards");

        final JLabel label = new JLabel();
        setJLabelProperties(label,0,0,25f, Color.WHITE,(int)(leftPanel.getWidth() * 0.75),(int)(leftPanel.getHeight() * 0.5));
        final JLabel label2 = new JLabel(){
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor( getBackground() );
                super.paintComponent(g);
                Graphics2D graphics = (Graphics2D) g;
                Dimension arcs = new Dimension(20, 20);
                graphics.setColor(getBackground());
                graphics.fillRoundRect(getWidth()/32, 0, (int)(getWidth() * 0.90), (int)(getHeight() * 0.70), arcs.width, arcs.height);
                graphics.setColor(getForeground());

            }
        };
        setJLabelProperties(label2,0,0,25f, Color.WHITE,(int)(leftPanel.getWidth() * 0.75),(int)(leftPanel.getHeight() * 0.5));

        label2.setOpaque(false);
        label2.setBackground(new Color(255, 255, 255, 0));
        label2.revalidate();
        label2.repaint();

        leftPanel.add(label,BorderLayout.NORTH);
        leftPanel.add(label2,BorderLayout.CENTER);

        final JPanel panel = new JPanel(true);
        panel.setSize((int)(centerPanel.getWidth() * 0.873737), (int)(mainPanel.getHeight() * 0.8333));//(int)(mainPanel.getHeight() - mainPanel.getHeight() * 0.1389));
        panel.revalidate();
        panel.repaint();
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(3,3,10,10));

        final JLabel centerSouthLabel = new JLabel(){
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                super.paintComponent(g);

                Graphics2D graphics = (Graphics2D) g;

                Dimension arcs = new Dimension(20, 20);

                //Draws the rounded opaque panel with borders.
                graphics.setColor(getBackground());
                graphics.fillRoundRect((int)(southPanel.getWidth() * 0.28), southPanel.getHeight()/4, (int)(getWidth() * 0.4405), getHeight()/2, arcs.width, arcs.height);
                graphics.setColor(getForeground());

            }
        };
        setJLabelProperties(centerSouthLabel, 0,0, 40f, Color.BLUE, southPanel.getWidth(),southPanel.getHeight());
        centerSouthLabel.setOpaque(false);
        centerSouthLabel.setBackground(new Color(0, 0, 255, 60));
        centerSouthLabel.setText("Simple Gods");
        southPanel.add(centerSouthLabel);

        final JPanel arrowPanel = new JPanel(true);
        arrowPanel.setLayout(new GridLayout(1,2,100,10));
        arrowPanel.setSize((int)(centerPanel.getWidth() * 0.873737), (int)(mainPanel.getHeight() * 0.1));//(mainPanel.getHeight() - mainPanel.getHeight()/10));
        arrowPanel.revalidate();
        arrowPanel.repaint();
        arrowPanel.setOpaque(false);
        JButton leftArrow=new JButton();
        setJButtonProperties(leftArrow);
        leftArrow.setSize(southPanel.getWidth()/10,southPanel.getHeight()/2);
        leftArrow.revalidate();
        leftArrow.repaint();
        leftArrow.setIcon(loadImage("images/Miscellaneous/btn_back.png",leftArrow.getWidth(), leftArrow.getHeight()));
        leftArrow.revalidate();
        leftArrow.repaint();
        leftArrow.setPressedIcon(loadImage("images/Miscellaneous/btn_back_pressed.png",leftArrow.getWidth(), leftArrow.getHeight()));
        leftArrow.revalidate();
        leftArrow.repaint();
        leftArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Component component:gods.keySet()) {
                    panel.remove(component);
                }
                for (JButton god:gods.keySet()) {
                    if(gods.get(god)>=0 && gods.get(god)<=8)
                    panel.add(god);
                }
                centerSouthLabel.setText("Simple Gods");
                revalidate();
                repaint();
            }
        });
        arrowPanel.add(leftArrow);


        JButton rightArrow = new JButton();
        setJButtonProperties(rightArrow);
        rightArrow.setSize(southPanel.getWidth()/10,southPanel.getHeight()/2);
        rightArrow.revalidate();
        rightArrow.repaint();
        rightArrow.setIcon(loadImage("images/Miscellaneous/btn_front.png", rightArrow.getWidth(), rightArrow.getHeight()));
        rightArrow.revalidate();
        rightArrow.repaint();
        rightArrow.setPressedIcon(loadImage("images/Miscellaneous/btn_front_pressed.png", rightArrow.getWidth(), rightArrow.getHeight()));
        rightArrow.revalidate();
        rightArrow.repaint();
        rightArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Component component:gods.keySet()) {
                    panel.remove(component);
                }
                for (JButton god:gods.keySet()) {
                    if(gods.get(god)>=9 && gods.get(god)<=13)
                        panel.add(god);
                }
                centerSouthLabel.setText("Advanced Gods");
                revalidate();
                repaint();
            }
        });
        arrowPanel.add(rightArrow);
        southPanel.add(arrowPanel);

        for (int i=0; i<14; i++) {
            final JButton god = new JButton();
            setJButtonProperties(god);
            god.setSize((int)(panel.getWidth() * 0.3),(int)(panel.getHeight() * 0.22222));//panel.getWidth()/3 - 30,panel.getHeight()/3 - 100);
            god.revalidate();
            god.repaint();
            String fileName = Gods.getGod(i).toString();
            fileName = fileName.substring(fileName.lastIndexOf('.')+1, fileName.indexOf('@'));
            god.setIcon(loadImage("images/God_with_frame/"+ fileName +".png", god.getWidth(), god.getHeight()));
            god.revalidate();
            god.repaint();
            if(i<=8){
                panel.add(god);
            }
            gods.put(god, i);
            god.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    label2.setOpaque(false);
                    label2.setBackground(new Color(255, 255, 255, 75));
                    String string = Gods.getGod(gods.get((JButton) e.getSource())).toString();
                    string = string.substring(string.lastIndexOf('.')+1, string.indexOf('@'));
                    if (gods.get((JButton) e.getSource()) > 8){
                        label.setIcon(loadImage("images/God_with_frame/"+ string +".png", god.getWidth(), (int)(god.getHeight() * 1.5)));
                    } else {
                        label.setIcon(loadImage("images/God_with_frame/"+ string +".png", (int)(god.getWidth() * 1.5), (int)(god.getHeight() * 1.5)));
                    }
                    String labelText = "<html><p style=\"width:" + (int)(label2.getWidth() * 0.85) + ";text-align:center;\">" + correspondingGod(Parser.toCapitalize(string)) + "</p></html>";// godsFunction.get(Parser.toCapitalize(string)) + "</p></html>";
                    label2.setText(labelText);
                    label2.setVerticalTextPosition(SwingConstants.TOP);
                    label2.setVerticalAlignment(SwingConstants.TOP);
                }
            });
            god.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    multipleSelections.add(gods.get((JButton) e.getSource()).toString());
                    gods.remove((JButton) e.getSource());
                    panel.remove((JButton) e.getSource());
                    panel.revalidate();
                    panel.repaint();
                    synchronized (multipleSelections) {
                        if (multipleSelections.size() == clientConfigurator.getNumberOfPlayer()) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < multipleSelections.size(); i++) {
                                stringBuilder.append(multipleSelections.get(i));
                                if (i < multipleSelections.size()-1) {
                                    stringBuilder.append(',');
                                }
                            }
                            response = stringBuilder.toString();
                            guiClient.send(response);
                            southPanel.remove(arrowPanel);
                            multipleSelections.clear();
                            centerSouthLabel.setText("");
                            centerPanel.remove(panel);
                            centerPanel.revalidate();
                            centerPanel.repaint();
                            southPanel.remove(centerSouthLabel);
                            southPanel.revalidate();
                            southPanel.repaint();
                            leftPanel.removeAll();
                            leftPanel.revalidate();
                            leftPanel.repaint();
                        }
                    }
                }
            });
        }
        centerPanel.add(panel);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void pickCard(ViewMessage viewMessage) {
        final JLabel label = new JLabel();
        setJLabelProperties(label,0,0,25f, Color.WHITE,(int)(leftPanel.getWidth() * 0.75),(int)(leftPanel.getHeight() * 0.5));
        final JLabel label2 = new JLabel(){
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor( getBackground() );
                super.paintComponent(g);
                Graphics2D graphics = (Graphics2D) g;
                Dimension arcs = new Dimension(20, 20);
                graphics.setColor(getBackground());
                graphics.fillRoundRect(getWidth()/32, 0, (int)(getWidth() * 0.90), (int)(getHeight() * 0.70), arcs.width, arcs.height);
                graphics.setColor(getForeground());
            }
        };
        setJLabelProperties(label2,0,0,25f, Color.WHITE,(int)(leftPanel.getWidth() * 0.75),(int)(leftPanel.getHeight() * 0.5));

        label2.setOpaque(false);
        label2.setBackground(new Color(255, 255, 255, 0));
        label2.revalidate();
        label2.repaint();

        leftPanel.add(label,BorderLayout.NORTH);
        leftPanel.add(label2,BorderLayout.CENTER);
        final ArrayList<String> godsName = new ArrayList<>();
        String[] splitted = viewMessage.getMessage().split("\n");
        String firstGod = Parser.toCapitalize(splitted[1].substring(4).trim());
        String secondGod = Parser.toCapitalize(splitted[2].substring(4).trim());
        godsName.add(firstGod);
        godsName.add(secondGod);
        if (clientConfigurator.getNumberOfPlayer() == 3 && splitted.length > 3) {
            String thirdGod = Parser.toCapitalize(splitted[3].substring(4).trim());
            godsName.add(thirdGod);
        }
        final HashMap<JButton, Integer> gods = new HashMap<>();
        setMessageOnPopup("Please select a god card");
        BufferedImage image;
        final JPanel panel = new JPanel(true);
        panel.setSize((int)(centerPanel.getWidth() * 0.873737), centerPanel.getHeight());
        panel.revalidate();
        panel.repaint();
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(2,2,0,0));
        for (int i = 0; i < godsName.size(); i++) {
            final JButton god = new JButton();
            setJButtonProperties(god);
            god.setSize((int)(panel.getWidth() * 0.3),(int)(panel.getHeight() * 0.3));
            god.revalidate();
            god.repaint();
            String fileName = godsName.get(i);
            god.setIcon(loadImage("images/God_with_frame/" + fileName + ".png",god.getWidth(), god.getHeight()));
            god.revalidate();
            god.repaint();
            panel.add(god);
            gods.put(god, i);
            god.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    label2.setOpaque(false);
                    label2.setBackground(new Color(255, 255, 255, 75));
                    String string = godsName.get(gods.get((JButton) e.getSource()));
                    if (godsName.size() == 2){//quando tocca al terzo giocatore
                        label.setIcon(loadImage("images/God_with_frame/"+ Parser.toCapitalize(string) +".png", (god.getWidth()/2), (god.getHeight())));
                    } else {//da mettere quando tocca al secondo giocatore
                        label.setIcon(loadImage("images/God_with_frame/"+ Parser.toCapitalize(string) +".png", (god.getWidth()), (god.getHeight())));
                    }
                    String labelText = "<html><p style=\"width:" + (int)(label2.getWidth() * 0.9) + ";text-align:center;\">" + correspondingGod(Parser.toCapitalize(string)) + "</p></html>";//godsFunction.get(Parser.toCapitalize(string)) + "</p></html>";
                    label2.setText(labelText);
                    label2.setVerticalTextPosition(SwingConstants.TOP);
                    label2.setVerticalAlignment(SwingConstants.TOP);
                }
            });
            god.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    response = gods.get((JButton) e.getSource()).toString();
                    guiClient.send(response);
                    centerPanel.remove(panel);
                    centerPanel.revalidate();
                    centerPanel.repaint();
                    leftPanel.removeAll();
                    leftPanel.revalidate();
                    leftPanel.repaint();
                }
            });
            panel.add(god);

        }
        centerPanel.add(panel);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void sendCell(ActionEvent e){
        StringBuilder stringBuilder = new StringBuilder();
        chosenCellX = cellsX.get((JButton) e.getSource()).toString();
        chosenCellY = cellsY.get((JButton) e.getSource()).toString();
        stringBuilder.append(chosenCellX);
        stringBuilder.append(",");
        stringBuilder.append(chosenCellY);
        response = stringBuilder.toString();
        guiClient.send(response);
    }

    private void placeWorker(Board board) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                if(board.getCell(i,j).isFree()){
                    (overlayPanel.getComponent(i*5+j)).setVisible(true);
                }
                ((JButton)overlayPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendCell(e);
                        if(messageType==MessageType.SET_WORKER_2){
                            player.setWorkers(new Worker(new Cell(Integer.parseInt(chosenCellX), Integer.parseInt(chosenCellY))));
                        }
                        resetOverlayPanel();
                    }
                });
            }
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void prepareMove(GameMessage gameMessage){
        for(int i=0; i<5; i++){
            for(int j=0;j<5;j++){
                boolean mine=false;
                Cell cell = ((GameBoardMessage)gameMessage).getBoard().getCell(i,j);
                try{
                    if(player.getWorker(0).getCell().equals(cell) || player.getWorker(1).getCell().equals(cell)){
                        mine=true;
                    }
                }catch (IndexOutOfBoundsException e2){
                    //ignore
                }
                if(mine){
                    board[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean stop=false;
                            ((JButton)e.getSource()).setSelected(false);
                            for(int i=0; i<5; i++){
                                for(int j=0; j<5; j++){
                                    if(e.getSource() == board[i][j]){
                                        performMove(i,j);
                                        stop=true;
                                        break;
                                    }
                                }
                                if(stop){
                                    break;
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    private void performMove(int x, int y) {
        if(player.getWorker(0).getCell().getX()==x && player.getWorker(0).getCell().getY()==y){
            this.selectedWorker=0;
        } else if(player.getWorker(1).getCell().getX()==x && player.getWorker(1).getCell().getY()==y){
            this.selectedWorker=1;
        } else {
            setMessageOnPopup("This is not your worker");
            return;
        }
        resetOverlayPanel();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++){
                try{
                    if(!(i == x && j == y) && i >= 0 && j >= 0 && i <= 4 && j <= 4 && !(player.getWorker(0).getCell().getX() == i && player.getWorker(0).getCell().getY() == j) && !(player.getWorker(1).getCell().getX() == i && player.getWorker(1).getCell().getY() == j)){
                        (overlayPanel.getComponent(i * 5 + j)).setVisible(true);
                        (overlayPanel.getComponent(i * 5 + j)).setEnabled(true);
                        ((JButton)overlayPanel.getComponent(i * 5 + j)).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                ((JButton)e.getSource()).setSelected(false);
                                StringBuilder stringBuilder = new StringBuilder();
                                chosenCellX = cellsX.get((JButton) e.getSource()).toString();
                                chosenCellY = cellsY.get((JButton) e.getSource()).toString();
                                stringBuilder.append(selectedWorker);
                                stringBuilder.append(",");
                                stringBuilder.append(chosenCellX);
                                stringBuilder.append(",");
                                stringBuilder.append(chosenCellY);
                                response = stringBuilder.toString();
                                guiClient.send(response);
                            }
                        });
                        final int ii = i;
                        final int jj = j;
                        initialBoardPanel.getComponent(i * 5 + j).setEnabled(true);
                        ((JButton)initialBoardPanel.getComponent(i * 5 + j)).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                ((JButton)e.getSource()).setSelected(false);
                                StringBuilder stringBuilder = new StringBuilder();
                                chosenCellX = String.valueOf(ii);
                                chosenCellY = String.valueOf(jj);
                                stringBuilder.append(selectedWorker);
                                stringBuilder.append(",");
                                stringBuilder.append(chosenCellX);
                                stringBuilder.append(",");
                                stringBuilder.append(chosenCellY);
                                response = stringBuilder.toString();
                                guiClient.send(response);
                            }
                        });
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    private void performBuild(){
        int x=player.getWorker(player.getUsedWorker()).getCell().getX();
        int y=player.getWorker(player.getUsedWorker()).getCell().getY();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++){
                try{
                    if((!(i == x && j == y) || player.getGodCard().getCardGod()==Gods.ZEUS) && i >= 0 && j >= 0 && i <= 4 && j <= 4 &&
                            (!(player.getWorker(0).getCell().getX() == i && player.getWorker(0).getCell().getY() == j) || player.getGodCard().getCardGod() == Gods.ZEUS) &&
                            (!(player.getWorker(1).getCell().getX() == i && player.getWorker(1).getCell().getY() == j) || player.getGodCard().getCardGod() == Gods.ZEUS)){
                        (overlayPanel.getComponent(i * 5 + j)).setVisible(true);
                        (overlayPanel.getComponent(i * 5 + j)).setEnabled(true);
                        ((JButton)overlayPanel.getComponent(i * 5 + j)).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                sendCell(e);
                            }
                        });
                        (initialBoardPanel.getComponent(i*5+j)).setEnabled(true);
                        ((JButton)initialBoardPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i1=0; i1<5; i1++){
                                    for(int j1=0; j1<5; j1++){
                                        if(e.getSource() == board[i1][j1]){
                                            chosenCellX = String.valueOf(i1);
                                            chosenCellY = String.valueOf(j1);
                                        }
                                    }
                                }
                                stringBuilder.append(chosenCellX);
                                stringBuilder.append(",");
                                stringBuilder.append(chosenCellY);
                                response = stringBuilder.toString();
                                guiClient.send(response);
                            }
                        });
                    }

                }catch(Exception e){
                    //ignore
                }

            }
        }
    }

    private String getDomeLevelImage(Blocks previewsLevel){
        String path = "";
        switch (previewsLevel){
            case EMPTY:
                path = "empty_dome";
                break;
            case LEVEL1:
                path = "level1_dome";
                break;
            case LEVEL2:
                path = "level2_dome";
                break;
            case LEVEL3: case DOME:
                path = "dome";
                break;
        }
        return path;
    }

    private void setCell(JButton cell, Cell boardCell, boolean isFree, String color){
        String path="images/Blocks/";
        boolean isVisible=true;
            switch(boardCell.getLevel()){
                case EMPTY:
                    isVisible=!isFree;
                    break;
                case LEVEL1:
                    path+="level1";
                    break;
                case LEVEL2:
                    path+="level2";
                    break;
                case LEVEL3:
                    path+="level3";
                    break;
                case DOME:
                    path+=getDomeLevelImage(boardCell.getPreviousLevel());
                    break;
            }
            if(!isFree){
                if(color.equals("blue")){
                    path+="_me";
                }
                else {
                    path+="_enemy_" + color;
                }
            }
            path+=".png";
            if(isVisible){
                Image normal = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(path)).getScaledInstance(cell.getWidth(), cell.getHeight(), Image.SCALE_AREA_AVERAGING);
                cell.setIcon(new ImageIcon(normal));
                cell.revalidate();
                cell.repaint();
                cell.setDisabledIcon(new ImageIcon(normal));
            }
            cell.setVisible(isVisible);


    }

    private void removeBoard(){
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void endGame(HashMap<Player, Integer> podium){
        removeBoard();
        endGamePanel = new JPanel();
        setJPanelProperties(endGamePanel,0,0,mainPanel.getWidth(), mainPanel.getHeight());
        endGamePanel.revalidate();
        endGamePanel.repaint();

        //mainPanel.add(endGamePanel);
        add(endGamePanel);
        endGameImage = new JLabel();
        endGameImage.setPreferredSize(new Dimension(endGamePanel.getWidth(), endGamePanel.getHeight()));
        endGameImage.revalidate();
        endGameImage.repaint();
        endGameImage.setSize(endGamePanel.getWidth(), endGamePanel.getHeight());
        endGameImage.revalidate();
        endGameImage.repaint();
        endGameImage.setIcon(loadImage("images/End_game.png", endGameImage.getWidth(),endGameImage.getHeight()));
        endGameImage.revalidate();
        endGameImage.repaint();

        //mainPanel.add(endGameImage, BorderLayout.CENTER);
        add(endGameImage, BorderLayout.CENTER);
        //mainPanel.revalidate();
        //mainPanel.repaint();
        remove(mainPanel);
        setJPanelsOnEndGame(podium);
    }

    private void removeEndGameLayout(){
        endGameImage.setIcon(loadImage("images/SantoriniBoard.png", endGameImage.getWidth(),endGameImage.getHeight()));
        endGameImage.revalidate();
        endGameImage.repaint();
        messageLabel.setText("Wait for the answers of the other players.");
        endGamePanel.removeAll();
        remove(endGamePanel);
        //mainPanel.revalidate();
        //mainPanel.repaint();
        resetNewGame();
        revalidate();
        repaint();
    }

    private void resetNewGame(){
        initedBoard = false;
        opponentsNames.clear();
        opponentGods.clear();
        myGod.clear();
        selectedWorker = -1;
        response = "";
        multipleSelections.clear();
        chosenCellY = "";
        chosenCellX = "";
        cellsX.clear();
        cellsY.clear();
        messageType = MessageType.PLAYER_NAME;
    }

    private void setJPanelsOnEndGame(HashMap<Player, Integer> podium){
        endGamePanelPlayers = new JPanel();
        value = 0.552083;
        setJPanelProperties(endGamePanelPlayers, 10,60, (int)(endGamePanel.getWidth() * value),(int)(endGamePanel.getHeight() * 0.83333));// (int)(endGamePanel.getHeight() - endGamePanel.getHeight() * 0.1389));
        endGamePanelPlayers.revalidate();
        endGamePanelPlayers.repaint();

        endGamePanel.add(endGamePanelPlayers, BorderLayout.CENTER);

        value = 0.2129629;
        southPanel = new JLabel();
        setJLabelProperties(southPanel, 10,60, 25f,Color.WHITE, (int)(endGamePanel.getWidth() * value), (int)(endGamePanel.getHeight() * value));
        southPanel.revalidate();
        southPanel.repaint();

        endGamePanel.add(southPanel, BorderLayout.SOUTH);

        value = 0.260416;
        playAgain = new JPanel(true);
        setJPanelProperties(playAgain, 10,50, (int)(endGamePanel.getWidth() * value), endGamePanel.getHeight());
        playAgain.revalidate();
        playAgain.repaint();

        JButton playAgainButton = new JButton();
        playAgainButton.setSize(playAgain.getWidth()/2, playAgain.getHeight()/4);
        playAgainButton.revalidate();
        playAgainButton.repaint();
        setJButtonProperties(playAgainButton);

        playAgainButton.setHorizontalAlignment(SwingConstants.CENTER);
        Image playAgainImage = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/button-play-again-normal.png")).getScaledInstance(playAgainButton.getWidth(), playAgainButton.getHeight(), Image.SCALE_AREA_AVERAGING);
        Image playAgainImagePressed = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/button-play-again-down.png")).getScaledInstance(playAgainButton.getWidth(), playAgainButton.getHeight(), Image.SCALE_AREA_AVERAGING);
        playAgainButton.setIcon(new ImageIcon(playAgainImage));
        playAgainButton.revalidate();
        playAgainButton.repaint();
        playAgainButton.setPressedIcon(new ImageIcon(playAgainImagePressed));
        playAgainButton.revalidate();
        playAgainButton.repaint();
        playAgain.add(playAgainButton,BorderLayout.CENTER);
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.send("y");
                removeEndGameLayout();
            }
        });
        endGamePanel.add(playAgain, BorderLayout.WEST);

        exitGame = new JPanel(true);
        setJPanelProperties(exitGame, 10,10,(int)(endGamePanel.getWidth() * value), endGamePanel.getBounds().height);
        exitGame.revalidate();
        exitGame.repaint();

        JButton exitButton = new JButton();
        exitButton.setSize(exitGame.getWidth()/2, exitGame.getHeight()/4);
        exitButton.revalidate();
        exitButton.repaint();
        setJButtonProperties(exitButton);

        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        Image exit = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/btn-exit-normal.png")).getScaledInstance(exitButton.getWidth(), exitButton.getHeight(), Image.SCALE_AREA_AVERAGING);
        Image exit_pressed = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/btn-exit-down.png")).getScaledInstance(exitButton.getWidth(), exitButton.getHeight(), Image.SCALE_AREA_AVERAGING);
        exitButton.setIcon(new ImageIcon(exit));
        exitButton.revalidate();
        exitButton.repaint();
        exitButton.setPressedIcon(new ImageIcon(exit_pressed));
        exitButton.revalidate();
        exitButton.repaint();
        exitGame.add(exitButton,BorderLayout.CENTER);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.send("n");
                System.exit(0);
            }
        });
        endGamePanel.add(exitGame, BorderLayout.EAST);

        value = 0.199074;
        messageLabel = new JLabel();
        setJLabelProperties(messageLabel,10,10, 25f, Color.WHITE,labelWidth, labelHeight);
        messageLabel.revalidate();
        messageLabel.repaint();

        endGamePanel.add(messageLabel, BorderLayout.NORTH);

        Image dimg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/Santorini_GenericPopup.png")).getScaledInstance(messageLabel.getWidth(), messageLabel.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        messageLabel.setIcon(imageIcon);
        messageLabel.revalidate();
        messageLabel.repaint();
        //messageLabel.setForeground(Color.WHITE);
        endGamePanel.revalidate();
        endGamePanel.repaint();
        revalidate();
        repaint();
        addPlayersEndGame(podium);
        revalidate();
        repaint();
    }

    private void addPlayersEndGame(HashMap<Player, Integer> podium){
        double value2 = 0.8;
        double name = 0.5;
        double height = 0.2;
        String[] podiumNames = new String[3];
        String[] realPodiumNames = new String[3];
        JLabel centerEnd = new JLabel();
        String[] s=new String[3];
        centerEnd.setLayout(new BorderLayout());
        centerEnd.setPreferredSize(new Dimension(2,50));
        centerEnd.revalidate();
        centerEnd.repaint();
        centerEnd.setSize(2,50);
        centerEnd.revalidate();
        centerEnd.repaint();
        centerEnd.setOpaque(false);
        endGamePanelPlayers.add(centerEnd,BorderLayout.CENTER);
        if(!isSimplePlay){
            for (Player player: podium.keySet()) {
                s[podium.get(player) - 1]=player.getGodCard().getName();
            }
        }
        else{
            for (Player player: podium.keySet()) {
                if(player.equals(this.player)){
                    s[podium.get(player) - 1] = "our";
                }
                else if (clientConfigurator.getOpponentsNames().get(player.getPlayerName()).equals("red")){
                    s[podium.get(player) - 1] = "Enemy_red";
                }
                else {
                    s[podium.get(player) - 1] = "Enemy_green";
                }
            }
        }
        for (Player player: podium.keySet()) {
            if(player.equals(this.player)){
                podiumNames[podium.get(player) - 1] = "Our";
                realPodiumNames[podium.get(player) - 1] = player.getPlayerName();
            }
            else if (clientConfigurator.getOpponentsNames().get(player.getPlayerName()).equals("red")){
                podiumNames[podium.get(player) - 1] = "Enemy_red";
                realPodiumNames[podium.get(player) - 1] = player.getPlayerName();
            }
            else {
                podiumNames[podium.get(player) - 1] = "Enemy_green";
                realPodiumNames[podium.get(player) - 1] = player.getPlayerName();
            }
        }

        JPanel winnerPanel = new JPanel();
        setJPanelProperties(winnerPanel,0,0,endGamePanelPlayers.getWidth()/2,endGamePanelPlayers.getHeight()/2);

        JLabel winnerName = new JLabel();
        setJLabelProperties(winnerName, 0,0,25f,Color.WHITE, (int)(winnerPanel.getWidth() * name),(int)(winnerPanel.getHeight() * height));
        int maxLength = Math.min(realPodiumNames[0].length(), 9);
        winnerName.setText(realPodiumNames[0].substring(0,maxLength));
        winnerName.setHorizontalTextPosition(SwingConstants.CENTER);
        JLabel winner = new JLabel();
        value = 0.6;
        setJLabelProperties(winner,0,0, 25f,Color.WHITE, (int)(winnerPanel.getWidth() * value),(int)(winnerPanel.getHeight() * value2));

        Image dimg2;

        Image dimg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/Podium_win/"+Parser.toCapitalize(s[0])+"_podium_win.png")).getScaledInstance(winner.getWidth(),winner.getHeight(), Image.SCALE_AREA_AVERAGING);
        if (podiumNames[0].equals("Our")){
            dimg2 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/myNameFrame.png")).getScaledInstance(winnerName.getWidth(),winnerName.getHeight(), Image.SCALE_AREA_AVERAGING);
        }
        else if (podiumNames[0].equals("Enemy_red")){
            dimg2 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/opponentNameFrame.png")).getScaledInstance(winnerName.getWidth(),winnerName.getHeight(), Image.SCALE_AREA_AVERAGING);
        }
        else {
            dimg2 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/opponentGreenNameFrame.png")).getScaledInstance(winnerName.getWidth(),winnerName.getHeight(), Image.SCALE_AREA_AVERAGING);
        }
        ImageIcon imageIcon = new ImageIcon(dimg);
        ImageIcon imageIcon2 = new ImageIcon(dimg2);
        winner.setIcon(imageIcon);
        winner.revalidate();
        winner.repaint();
        winnerName.setIcon(imageIcon2);
        winner.revalidate();
        winner.repaint();
        winnerPanel.add(winner,BorderLayout.CENTER);
        winnerPanel.add(winnerName, BorderLayout.SOUTH);
        winnerPanel.revalidate();
        winnerPanel.repaint();
        endGamePanelPlayers.add(winnerPanel, BorderLayout.NORTH);
        endGamePanelPlayers.revalidate();
        endGamePanelPlayers.repaint();

        JPanel losers = new JPanel();
        JPanel loserPanel1 = new JPanel();
        JPanel loserPanel2 = new JPanel();
        losers.setLayout(new GridLayout(1,2,0,0));
        losers.setPreferredSize(new Dimension(endGamePanelPlayers.getWidth(),endGamePanelPlayers.getHeight()/2));
        losers.revalidate();
        losers.repaint();
        losers.setSize(endGamePanelPlayers.getWidth(),endGamePanelPlayers.getHeight()/2);
        losers.revalidate();
        losers.repaint();
        losers.setOpaque(false);
        setJPanelProperties(loserPanel1,0,0,losers.getWidth()/2,losers.getHeight());
        setJPanelProperties(loserPanel2,0,0,losers.getWidth()/2,losers.getHeight());
        JLabel loser1 = new JLabel();
        JLabel loserName1 = new JLabel();
        JLabel loser2 = new JLabel();
        JLabel loserName2 = new JLabel();

        setJLabelProperties(loser1, 0,0, 25f, Color.WHITE,(int)(loserPanel1.getWidth() * value),(int)(loserPanel1.getHeight() * value2));
        setJLabelProperties(loserName1, 0,0, 25f, Color.WHITE,(int)(loserPanel1.getWidth() * name),(int)(loserPanel1.getHeight() * height));
        int maxLength2 = Math.min(realPodiumNames[1].length(), 9);
        loserName1.setText(realPodiumNames[1].substring(0,maxLength2));

        Image dimg4;
        Image dimg3 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/Podium_silver/"+Parser.toCapitalize(s[1])+"_podium_silver.png")).getScaledInstance(loser1.getWidth(),loser1.getHeight(), Image.SCALE_AREA_AVERAGING);
        if (podiumNames[1].equals("Our")){
            dimg4 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/myNameFrame.png")).getScaledInstance(loserName1.getWidth(),loserName1.getHeight(), Image.SCALE_AREA_AVERAGING);
        }
        else if (podiumNames[1].equals("Enemy_red")){
            dimg4 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/opponentNameFrame.png")).getScaledInstance(loserName1.getWidth(),loserName1.getHeight(), Image.SCALE_AREA_AVERAGING);
        }
        else {
            dimg4 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/opponentGreenNameFrame.png")).getScaledInstance(loserName1.getWidth(),loserName1.getHeight(), Image.SCALE_AREA_AVERAGING);
        }

        ImageIcon imageIcon3 = new ImageIcon(dimg3);
        ImageIcon imageIcon4 = new ImageIcon(dimg4);
        loser1.setIcon(imageIcon3);
        loser1.revalidate();
        loser1.repaint();
        loserName1.setIcon(imageIcon4);
        loserName1.revalidate();
        loserName1.repaint();
        loserPanel1.add(loser1,BorderLayout.CENTER);
        loserPanel1.add(loserName1, BorderLayout.SOUTH);
        loserPanel1.revalidate();
        loserPanel1.repaint();
        losers.add(loserPanel1);
        losers.revalidate();
        losers.repaint();

        if(s[2] != null){
            setJLabelProperties(loser2, 0,0, 25f, Color.WHITE,(int)(loserPanel2.getWidth() * value),(int)(loserPanel2.getHeight() * value2));
            setJLabelProperties(loserName2, 0,0, 25f, Color.WHITE,(int)(loserPanel2.getWidth() * name),(int)(loserPanel2.getHeight() * height));
            int maxLength3 = Math.min(realPodiumNames[2].length(), 9);
            loserName2.setText(realPodiumNames[2].substring(0,maxLength3));
            Image dimg6;
            Image dimg5 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/Podium_bronze/"+Parser.toCapitalize(s[2])+"_podium_bronze.png")).getScaledInstance(loser2.getWidth(),loser2.getHeight(), Image.SCALE_AREA_AVERAGING);
            if (podiumNames[2].equals("Our")){
                dimg6 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/myNameFrame.png")).getScaledInstance(loserName2.getWidth(),loserName2.getHeight(), Image.SCALE_AREA_AVERAGING);
            }
            else if (podiumNames[2].equals("Enemy_red")){
                dimg6 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/opponentNameFrame.png")).getScaledInstance(loserName2.getWidth(),loserName2.getHeight(), Image.SCALE_AREA_AVERAGING);
            }
            else {
                dimg6 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/opponentGreenNameFrame.png")).getScaledInstance(loserName2.getWidth(),loserName2.getHeight(), Image.SCALE_AREA_AVERAGING);
            }
            ImageIcon imageIcon5 = new ImageIcon(dimg5);
            ImageIcon imageIcon6 = new ImageIcon(dimg6);
            loser2.setIcon(imageIcon5);
            loser2.revalidate();
            loser2.repaint();
            loserName2.setIcon(imageIcon6);
            loserName1.revalidate();
            loserName1.repaint();
            loserPanel2.add(loser2,BorderLayout.CENTER);
            loserPanel2.add(loserName2, BorderLayout.SOUTH);
            loserPanel2.revalidate();
            loserPanel2.repaint();
            losers.add(loserPanel2);
            losers.revalidate();
            losers.repaint();

        }
        setMessageOnPopup("Would you like to play again?");
        endGamePanelPlayers.add(losers, BorderLayout.SOUTH);
        endGamePanelPlayers.revalidate();
        endGamePanelPlayers.repaint();
    }

    private void updateBoard(Board board, boolean enable){
        for (Player player: board.getPlayers()){
            if (player.equals(this.player)){
                this.player = player;
            }
        }
        try{
            for(int i=0; i<5; i++){
                for(int j=0; j<5; j++){
                    String color = "";
                    Cell cell = board.getCell(i,j);
                    if (!cell.isFree()){
                        try{
                            for (int p = 0; p < clientConfigurator.getNumberOfPlayer(); p++){
                                Player play = board.getPlayers()[p];
                                if(play.equals(player)){
                                    if (player.getWorker(0).getCell().equals(cell)){
                                        color = "blue";
                                        break;
                                    }
                                    else if(player.getWorker(1).getCell().equals(cell)){
                                        color = "blue";
                                        break;
                                    }
                                } else {
                                    if (play.getWorker(0).getCell().equals(cell)){
                                        color = clientConfigurator.getOpponentsNames().get(play.getPlayerName());
                                        break;
                                    }
                                    else if (play.getWorker(1).getCell().equals(cell)){
                                        color = clientConfigurator.getOpponentsNames().get(play.getPlayerName());
                                        break;
                                    }
                                }


                            }
                        }catch (IndexOutOfBoundsException e2){
                            e2.printStackTrace();
                        }
                    }
                    JButton jButton = ((JButton) initialBoardPanel.getComponent(i * 5 + j));
                    setCell(jButton, cell, cell.isFree(), color);
                    jButton.setEnabled(enable);

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void usePower(){
        resetBoardPanel();
        setMessageOnPopup("Make a choice");
        resetOverlayPanel();



        godPanel = new JPanel(true){

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = ImageIO.read(Game.class.getClassLoader().getResource("images/GodPower/main.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Image image = bufferedImage.getScaledInstance(leftPanel.getWidth(), leftPanel.getHeight()/2, Image.SCALE_AREA_AVERAGING);
                g.drawImage(image, 0, 0, null);

            }
        };
        godPanel.setLayout(new BorderLayout(10,10));
        godPanel.setOpaque(false);
        godPanel.setPreferredSize(new Dimension(leftPanel.getWidth(), leftPanel.getHeight()/2));
        godPanel.revalidate();
        godPanel.repaint();
        godPanel.setSize(leftPanel.getWidth(), leftPanel.getHeight()/2);
        godPanel.revalidate();
        godPanel.repaint();

        godPanel.setAlignmentX(SwingConstants.CENTER);
        godPanel.setAlignmentY(SwingConstants.CENTER);

        JPanel internalPanel=new JPanel(true);
        internalPanel.setLayout(new GridLayout(1,2,10,0));
        internalPanel.setPreferredSize(new Dimension(godPanel.getWidth()/2, (int)(godPanel.getHeight()*0.6)));
        internalPanel.revalidate();
        internalPanel.repaint();
        internalPanel.setSize(godPanel.getWidth(), godPanel.getHeight()/3);
        internalPanel.revalidate();
        internalPanel.repaint();
        internalPanel.setOpaque(false);

        JButton yes = new JButton();
        JButton no = new JButton();
        yes.setSize(internalPanel.getWidth()/4, internalPanel.getHeight()/5);
        yes.revalidate();
        yes.repaint();
        no.setSize(internalPanel.getWidth()/4, internalPanel.getHeight()/5);
        no.revalidate();
        no.repaint();
        setJButtonProperties(yes);
        setJButtonProperties(no);

        yes.setHorizontalAlignment(SwingConstants.LEFT);
        no.setHorizontalAlignment(SwingConstants.RIGHT);
        Image n, n2;
        Image np, n2p;

        n = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/GodPower/btn_green.png")).getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_AREA_AVERAGING);
        yes.setIcon(new ImageIcon(n));
        yes.revalidate();
        yes.repaint();
        np = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/GodPower/btn_green_pressed.png")).getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_AREA_AVERAGING);
        yes.setPressedIcon(new ImageIcon(np));
        yes.revalidate();
        yes.repaint();
        n2 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/GodPower/btn_coral.png")).getScaledInstance(no.getWidth(), no.getHeight(), Image.SCALE_AREA_AVERAGING);
        no.setIcon(new ImageIcon(n2));
        no.revalidate();
        no.repaint();
        n2p = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/GodPower/btn_coral_pressed.png")).getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_AREA_AVERAGING);
        no.setPressedIcon(new ImageIcon(n2p));
        no.revalidate();
        no.repaint();

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.send("y");
                resetGodPanel();
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.send("n");
                resetGodPanel();
            }
        });


        internalPanel.add(no);
        internalPanel.add(yes);
        godPanel.add(internalPanel, BorderLayout.SOUTH);

        leftPanel.add(godPanel, BorderLayout.SOUTH);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void prometheusPower(){
        setMessageOnPopup("Make a choice");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                try{
                    ((JButton)initialBoardPanel.getComponent(i*5+j)).removeActionListener(((JButton)initialBoardPanel.getComponent(i*5+j)).getActionListeners()[0]);
                }catch(ArrayIndexOutOfBoundsException e){
                    //ignore
                }
                if(player.getWorker(0).getCell().getX()==i && player.getWorker(0).getCell().getY()==j){
                    ((JButton)initialBoardPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectedWorker=0;
                        }
                    });
                }else if(player.getWorker(1).getCell().getX()==i && player.getWorker(1).getCell().getY()==j){
                    ((JButton)initialBoardPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectedWorker=1;
                        }
                    });
                }

            }
        }
        godPanel=new JPanel(true){
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = ImageIO.read(Game.class.getClassLoader().getResource("images/GodPower/select_worker.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Image normal = bufferedImage.getScaledInstance(leftPanel.getWidth(), leftPanel.getHeight()/2, Image.SCALE_AREA_AVERAGING);
                g.drawImage(normal, 0, 0, null);

            }
        };
        setJPanelProperties(godPanel,10,10,leftPanel.getWidth(),leftPanel.getHeight()/2);

        godPanel.setAlignmentX(SwingConstants.CENTER);
        godPanel.setAlignmentY(SwingConstants.CENTER);

        JPanel internalPanel=new JPanel(true);
        internalPanel.setLayout(new GridLayout(1,1,10,0));
        internalPanel.setPreferredSize(new Dimension(godPanel.getWidth()/2, (int)(godPanel.getHeight()*0.6)));
        internalPanel.revalidate();
        internalPanel.repaint();
        internalPanel.setSize(godPanel.getWidth(), godPanel.getHeight()/3);
        internalPanel.revalidate();
        internalPanel.repaint();
        internalPanel.setOpaque(false);

        JButton yes = new JButton();
        setJButtonProperties(yes);
        yes.setSize(internalPanel.getWidth()/4, internalPanel.getHeight()/5);
        yes.revalidate();
        yes.repaint();
        yes.setHorizontalAlignment(SwingConstants.CENTER);
        Image n;
        Image np;

        n = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/GodPower/btn_ok.png")).getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_AREA_AVERAGING);
        yes.setIcon(new ImageIcon(n));
        yes.revalidate();
        yes.repaint();

        np = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/GodPower/btn_ok_pressed.png")).getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_AREA_AVERAGING);
        yes.setPressedIcon(new ImageIcon(np));
        yes.revalidate();
        yes.repaint();


        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedWorker==0 || selectedWorker == 1) {
                    guiClient.send(Integer.toString(selectedWorker));
                    resetGodPanel();
                } else {
                    showError("Please select a worker");
                }
            }
        });

        internalPanel.add(yes);

        godPanel.add(internalPanel, BorderLayout.SOUTH);

        leftPanel.add(godPanel, BorderLayout.SOUTH);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void turnPhaseManager(GameMessage gameMessage) {
        switch (gameMessage.getMessageType()) {
            case DRAW_CARD:
                isSimplePlay = false;
                drawCards();
                break;
            case PICK_CARD:
                isSimplePlay = false;
                pickCard(gameMessage);
                break;
            case SET_WORKER_1:
                addMyCard(gameMessage);
                addOverlayPanel();
                placeWorker(((GameBoardMessage)gameMessage).getBoard());
                break;
            case SET_WORKER_2:
                placeWorker(((GameBoardMessage)gameMessage).getBoard());
                break;
            case MOVE:
                resetOverlayPanel();
                resetBoardPanel();
                prepareMove(gameMessage);
                break;
            case BUILD:
                resetOverlayPanel();
                resetBoardPanel();
                performBuild();
                break;
            case USE_POWER:
                usePower();
                break;
            case PROMETHEUS:
                selectedWorker = -1;
                prometheusPower();
                break;
            case VICTORY:
            case LOSE:
                resetGodPanel();
                break;
            case END_GAME:
                resetGodPanel();
                endGame(((EndGameMessage) gameMessage).getPodium());
                break;
            default:
                break;
        }


    }

    private void phaseManager(GameMessage gameMessage){
        switch (gameMessage.getMessageType()) {
            case SET_WORKER_1:
                try {
                    if (!isSimplePlay && opponentGods.size() != clientConfigurator.getNumberOfPlayer() - 1) {
                        Player opponent = gameMessage.getPlayer();
                        if (!opponentGods.containsKey(opponent.getPlayerName())) {
                            opponentGods.put(opponent.getPlayerName(), opponent.getGodCard().getName());
                        }
                    }
                    else if(isSimplePlay && opponentsNames.size() != clientConfigurator.getNumberOfPlayer() - 1){
                        opponentsNames.add(gameMessage.getPlayer().getPlayerName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SET_WORKER_2:
                try {
                    if (!isSimplePlay && opponentGods.size() == clientConfigurator.getNumberOfPlayer() - 1) {
                        addOpponents();
                    }
                    else if(isSimplePlay && opponentsNames.size() == clientConfigurator.getNumberOfPlayer() - 1){
                        addOpponentsSimpleMode();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case MOVE:
            case BUILD:
            case USE_POWER:
            case PROMETHEUS:
                resetOverlayPanel();
                break;
            case VICTORY:
            case LOSE:
                resetGodPanel();
                break;
            case END_GAME:
                endGame(((EndGameMessage) gameMessage).getPodium());
                break;
            default:
                break;
        }


    }

    private void configuratorHandler(ViewMessage viewMessage){
        if(viewMessage.getMessageType() == MessageType.WAIT_FOR_START){
            initGame();
            this.initedBoard = true;
            setMessageOnPopup(viewMessage.getMessage());
        }
    }

    private void handleTurnMessage(GameMessage arg, Player player) {
        if (this.player.equals(player)) {
            this.player=arg.getPlayer();
            if(arg instanceof GameBoardMessage){
                updateBoard(((GameBoardMessage)arg).getBoard(), true);
            }
            setMessageOnPopup(arg.getMessage());
            turnPhaseManager(arg);
        } else {
            int maxLength = Math.min(player.getPlayerName().length(), 9);
            setMessageOnPopup("It's now " + player.getPlayerName().substring(0,maxLength) + "'s turn");
            phaseManager(arg);
            if(arg instanceof GameBoardMessage){
                updateBoard(((GameBoardMessage)arg).getBoard(), false);
            }

        }
    }

    private void showError(String s){
        try{
            if (!s.equals("Wrong input, please insert x,y") || messageType.equals(MessageType.USE_POWER)){
                southPanel.setText(s);
                southPanel.setOpaque(false);
                southPanel.setBackground(new Color(255, 0, 0, 60));
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        southPanel.setText("");
                        southPanel.setBackground(new Color(255, 0, 0, 0));
                    }
                }, 3000);
            }
        } catch (Exception e) {
            //ignore
        }
    }

    @Override
    public void update(Object msg) {

        if(msg instanceof String){
            String message = (String) msg;
            if(message.startsWith("ERROR: ")){
                String errorString = message.substring(7);
                showError(errorString);
            }
        } else if (msg instanceof ViewMessage) {
            ViewMessage viewMessage = (ViewMessage) msg;

            this.messageType = viewMessage.getMessageType();
            if(viewMessage.getMessageType() == MessageType.BEGINNING && !this.initedBoard){
                initGame();
                initedBoard = true;
            }
            if(viewMessage instanceof GameMessage) {
                GameMessage gameMessage = (GameMessage) viewMessage;
                handleTurnMessage(gameMessage, gameMessage.getPlayer());
            } else {
                setMessageOnPopup(viewMessage.getMessage());
            }
            configuratorHandler(viewMessage);
        }


    }
}
