import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FormMain extends JFrame {
    private Graphics g;
    private PackMan packMan;
    private Board board;
    private int wallX = 30;
    private int wallY = 50;
    private int wallHeight = 500;
    private int wallWidth = 1000;

    public FormMain() {
        setTitle("Packman");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(10, 10, 1300, 700);
        setVisible(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton()==MouseEvent.BUTTON1) {
//                    g = getGraphics();
                }

                //packMan.putPackManInTheField(g, e.getX(), e.getY());
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                g = getGraphics();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        board.movePackMan(g,packMan, PackMan.DIRECTION_SOUTH);
                        break;
                    case KeyEvent.VK_UP:
                        board.movePackMan(g,packMan, PackMan.DIRECTION_NORTH);
                        break;
                    case KeyEvent.VK_RIGHT:
                        board.movePackMan(g,packMan, PackMan.DIRECTION_EAST);
                        break;
                    case KeyEvent.VK_LEFT:
                        board.movePackMan(g,packMan, PackMan.DIRECTION_WEST);
                        break;
                    default:
                        return;
                }
            }
        });

        CardLayout cardLayout = new CardLayout();
        JPanel jpBottom = new JPanel(cardLayout);
        add(jpBottom, BorderLayout.SOUTH);
        //---------------panel for main menu----------------------
        JPanel jpMainMenu = new JPanel(new GridLayout());
        //Button startNewGame
        JButton jbStart = new JButton("Начать новую игру");
        jbStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(jpBottom, "jpPlaying");
            }
        });
        jpMainMenu.add(jbStart);
        //button editMap
        JButton jbEditMap = new JButton("Редактировать поле");
        jbEditMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        jpMainMenu.add(jbEditMap);
        //button Exit


        //--------------panel while playing-----------------------
        JPanel jpPlaying = new JPanel(new GridLayout());
        //button restartGame

        //button backToMainMenu

        //--------------panel for map editor----------------------
        //button load from file

        //button save to file

        //button backToMenu




        init();

    }

    private void init() {
        JButton buttonStart = new JButton("Start");
        JLabel labelStart = new JLabel("");
        labelStart.setHorizontalAlignment(JLabel.CENTER);
        setLayout(new BorderLayout());

        g = getGraphics();

        packMan = new PackMan(0, 0, 20);
        //packMan.setBorders(g, wallX, wallY, wallWidth, wallHeight);
        board = new Board(25,14, packMan.getSize(), 1);
        board.setPackMan(packMan, 0,0);
        board.drawBoard(g,packMan);

//        add(buttonStart, BorderLayout.NORTH);
//        add(labelStart,BorderLayout.CENTER);
//        buttonStart.addActionListener(e -> {
//            labelStart.setText(labelStart.getText() + "|");
//        });
    }

    public void paint(Graphics my_picture) {
//        my_picture.drawLine(10,40, 1000,40);
//        my_picture.drawRect(wallX, wallY, wallWidth, wallHeight);
//        my_picture.clearRect(0, 0, 1, 1);
//        my_picture.drawLine(0,10,100,10);
//        int sizeX = 3;
//        int startX = 20;
//        int fieldSize = 20;
//        int startY = 50;
//        int sizeY = 3;
//        for (int x = 1; x < sizeX; x++) {
//            my_picture.drawLine((startX + fieldSize * x), startY, (startX + fieldSize * x), (startY + fieldSize * sizeY));
//            for (int y = 1; y < sizeY; y++) {
//                my_picture.drawLine(startX, (startY + fieldSize * y), (startX + fieldSize * sizeX), (startY + fieldSize * y));
//            }
//        }
    }

//    private void erasePackman(int x, int y, int size) {
//        g = getGraphics();
//        g.clearRect(x-size, y-size, size*2+1, size*2+1);
//    }
//
//    private void drawPackman(int x, int y, int size, int direction, int state) {
//        g = getGraphics();
//        int x1;
//        int y1;
//        int x2;
//        int y2;
//        int arcAngle;
//        int startAngle;
//        double angle;
//        angle = 45.0;
//        if (state == 1) angle = 10.0;
//
//        startAngle = (int) (angle + direction * 90);
//        arcAngle = (int) (360 - angle * 2);
//
//
//        if ((direction == 2) || (direction == 3)) angle += 180;
//        if ((direction == 0) || (direction == 2)) {
//            x1 = x + (int) (size * Math.cos(Math.toRadians(angle)));
//            y1 = y - (int) (size * Math.sin(Math.toRadians(angle)));
//            x2 = x + (int) (size * Math.cos(Math.toRadians(angle)));
//            y2 = y + (int) (size * Math.sin(Math.toRadians(angle)));
//
//        } else {
//            x1 = x - (int) (size * Math.sin(Math.toRadians(angle)));
//            y1 = y - (int) (size * Math.cos(Math.toRadians(angle)));
//            x2 = x + (int) (size * Math.sin(Math.toRadians(angle)));
//            y2 = y - (int) (size * Math.cos(Math.toRadians(angle)));
//        }
//        g.drawLine(x, y, x1, y1);
//        g.drawLine(x, y, x2, y2);
//        g.drawArc(x - size, y - size, size * 2, size * 2, startAngle, arcAngle);
//    }
}
