import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FormMain extends JFrame {
    private Graphics g;
    private PackMan packMan;
    private Board board;
    private Board boardTemplate;
    private boolean editorModeOn = false;
    //
    private final int packManSize = 20;
    private final int dimX = 25;
    private final int dimY = 14;
    private final int startX = 20;
    private final int startY = 50;


    private class MyKeyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                return actKeyPressed(e.getKeyCode());
            }
            return false;
        }
    }

    private Font btnFont = new Font("Times New Roman", Font.PLAIN, 16);

    public FormMain() {
        setSize(1300, 700);
        setResizable(false);
        setLocation(10, 10);

        setTitle("Packman");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//        setBounds(10, 10, 1300, 700);

        initListeners();

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyKeyDispatcher());

        setVisible(true);
        init();
        initMenu();

    }

    private void initListeners() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (editorModeOn) {
                        g = getGraphics();
                        changeElement(g, e.getX(), e.getY());
                    }
//                    String msg = "x: " + e.getX() + " y:" + e.getY();
//                    JOptionPane.showMessageDialog(new JFrame(), msg);
//                    g = getGraphics();
                }

                //packMan.putPackManInTheField(g, e.getX(), e.getY());
            }
        });
    }

    private void initMenu() {
        JPanel jpBottom = addBottomMenu();
        jpBottom.setFocusable(false);
        //init panels
        //---------------panel for main menu----------------------
        JPanel jpMainMenu = addMainMenu(jpBottom);
        jpMainMenu.setFocusable(false);
        jpBottom.add(jpMainMenu, "jpMainMenu");
        //--------------panel while playing-----------------------
        JPanel jpPlaying = addPlayMenu(jpBottom);
        jpPlaying.setFocusable(false);
        jpBottom.add(jpPlaying, "jpPlaying");
        //--------------panel for map editor----------------------
        JPanel jpEditMap = addEditorMenu(jpBottom);
        jpEditMap.setFocusable(false);
        jpBottom.add(jpEditMap, "jpEditMap");
//        Show card layout
        ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpMainMenu");
    }

    private JPanel addBottomMenu() {
        JPanel jpBottom = new JPanel(new CardLayout());  //Creating object for panel, which will contain menus
        jpBottom.setPreferredSize(new Dimension(1, 60));  //setting preferred size
        jpBottom.setFocusable(false);
        this.add(jpBottom, BorderLayout.SOUTH); //add bottom panel to the form
        return jpBottom;
    }

    private JPanel addMainMenu(JPanel jpBottom) {
        JPanel jpMainMenu = new JPanel(new GridLayout());
        //Button startNewGame
        JButton jbStart = new JButton("Начать новую игру");
        jbStart.setFont(btnFont);
        jbStart.setFocusable(false);
        jbStart.addActionListener(e -> {
            ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpPlaying");
            newGame(false, false);
        });
        jpMainMenu.add(jbStart);
        //button editMap
        JButton jbEditMap = new JButton("Редактировать поле");
        jbEditMap.setFont(btnFont);
        jbEditMap.setFocusable(false);
        jbEditMap.addActionListener(e -> {
            ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpEditMap");
            startEditor();
        });
        jpMainMenu.add(jbEditMap);
        //button Exit
        JButton jbExit = new JButton("Выход");
        jbExit.setFont(btnFont);
        jbExit.setFocusable(false);
        jbExit.addActionListener(e -> {
            System.exit(0);
        });
        jpMainMenu.add(jbExit);
        return jpMainMenu;
    }

    private JPanel addPlayMenu(JPanel jpBottom) {
        JPanel jpPlaying = new JPanel(new GridLayout());
        //button restartGame
        JButton jbRestart = new JButton("Начать заново");
        jbRestart.setFont(btnFont);
        jbRestart.setFocusable(false);
        jbRestart.addActionListener(e -> {
            newGame(false, true);
        });
        jpPlaying.add(jbRestart);
        //button changeMap
        JButton jbChangeMap = new JButton("Изменить карту");
        jbChangeMap.setFont(btnFont);
        jbChangeMap.setFocusable(false);
        jbChangeMap.addActionListener(e -> {
            newGame(true, false);
        });
        jpPlaying.add(jbChangeMap);
        //button backToMainMenu
        JButton jbBackToMainMenu = new JButton("Вернуться в основное меню");
        jbBackToMainMenu.setFocusable(false);
        jbBackToMainMenu.addActionListener(e -> {
            ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpMainMenu");
            boardTemplate = null;
            board = null;
            clearField();
        });
        jpPlaying.add(jbBackToMainMenu);
        return jpPlaying;
    }

    private JPanel addEditorMenu(JPanel jpBottom) {
        JPanel jpEditMap = new JPanel(new GridLayout());
        //button load from file
        JButton jbLoadMapFromFile = new JButton("Загрузить из файла");
        jbLoadMapFromFile.setFont(btnFont);
        jbLoadMapFromFile.addActionListener(e -> {
            boardTemplate.loadFromFile();
            g = getGraphics();
            boardTemplate.drawBoard(g, null);
        });
        jpEditMap.add(jbLoadMapFromFile);
        //button save to file
        JButton jbSaveMapToFile = new JButton("Сохранить в файл");
        jbSaveMapToFile.setFont(btnFont);
        jbSaveMapToFile.addActionListener(e -> {
            boardTemplate.saveToFile();
        });
        jpEditMap.add(jbSaveMapToFile);
        //button backToMenu
        JButton jbBackToMainMenu = new JButton("Вернуться в основное меню");
        jbBackToMainMenu.setFont(btnFont);
        jbBackToMainMenu.addActionListener(e -> {
            ((CardLayout) jpBottom.getLayout()).show(jpBottom, "jpMainMenu");
            boardTemplate = null;
            board = null;
            clearField();
        });
        jpEditMap.add(jbBackToMainMenu);
        return jpEditMap;
    }

    @Override
    public void paint(Graphics g) {

    }

    private void init() {

    }

    private void newGame(boolean changeMap, boolean restart) {
        packMan = new PackMan(0, 0, packManSize);
        if (restart) {
            board.restartMap();
        } else {
            board = new Board(dimX, dimY, startX, startY, packManSize, 1);
            if (changeMap) {
                if (!board.loadFromFile()) {
                    return;
                }
            }
        }
        board.setPackMan(packMan, 0, 0);
        g = getGraphics();
        board.drawBoard(g, packMan);
    }

    private void startEditor() {
        boardTemplate = new Board(dimX, dimY, startX, startY, packManSize, 0);
        g = getGraphics();
        boardTemplate.drawBoard(g, null);
        editorModeOn = true;
    }

    private void changeElement(Graphics g, int x, int y) {
        boardTemplate.changeElementOnField(g, x, y);
    }

    private boolean actKeyPressed(int keyCode) {
        if (board != null) {
            g = getGraphics();
            switch (keyCode) {
                case KeyEvent.VK_DOWN:
                    board.movePackMan(g, packMan, PackMan.DIRECTION_SOUTH);
                    return true;
                case KeyEvent.VK_UP:
                    board.movePackMan(g, packMan, PackMan.DIRECTION_NORTH);
                    return true;
                case KeyEvent.VK_RIGHT:
                    board.movePackMan(g, packMan, PackMan.DIRECTION_EAST);
                    return true;
                case KeyEvent.VK_LEFT:
                    board.movePackMan(g, packMan, PackMan.DIRECTION_WEST);
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private void clearField() {
        g = getGraphics();
        g.clearRect(0, 0, startX + dimX * packManSize * 2 + 5, startY + dimY * packManSize * 2 + 5);
    }
}
