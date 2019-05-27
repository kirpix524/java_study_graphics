import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class Board {
    private Graphics gBoard;
    private int dimX, dimY, size, startX, startY;

    private String levelPath = "C:/git/java_study_graphics/level";
    private String currentMapPath = "";

    private int lastChangedType = GameObject.TYPE_EMPTY;
    private int curPackManX, curPackManY;
    private GameObject[][] field;

    private class FieldXY {
        public int x;
        public int y;
    }

    public Board(int dimX, int dimY, int startX, int startY, int size, int level) {
        init(dimX, dimY, startX, startY, size, level);
    }

    private void init(int dimX, int dimY, int startX, int startY, int size, int level) {
        this.size = size;
        this.startX = startX;
        this.startY = startY;
        this.dimX = dimX;
        this.dimY = dimY;
        this.curPackManX = 0;
        this.curPackManY = 0;
        this.field = new GameObject[dimY][dimX];
        loadLevel(level);
    }

    private void loadLevel(int level) {
        if (level == 1) {
            loadFromFile(levelPath + "/level1.txt", true);
            return;
        }
        loadFromFile(levelPath+"/levelEmpty.txt", true);

    }

    private void drawWall(Graphics g, int fieldX, int fieldY) {
        drawEmptyField(g, fieldX, fieldY);
        int x = startX + (fieldX) * size * 2;
        int y = startY + (fieldY) * size * 2;
        int x1, y1, x2, y2;
        int corr = 3;
        for (int i = 0; i < size; i++) {
            x1 = x + corr;
            y1 = y + (i * 2) + corr;
            x2 = x + ((size - i) * 2) - corr;
            y2 = y + size * 2 - corr;
            if (y1 > (y + size * 2 - corr)) y1 = (y + size * 2 - corr);
            if (x2 < x + corr) x2 = x + corr;
            if (y2 > (y + size * 2 - corr)) y2 = (y + size * 2 - corr);
            g.drawLine(x1, y1, x2, y2);
            x1 = x + (i * 2) + corr;
            y1 = y + corr;
            x2 = x + (size * 2) - corr;
            y2 = y + (size - i) * 2 - corr;
            if (x1 > x + size * 2 - corr) x1 = x + size * 2 - corr;
            if (x2 > x + size * 2 - corr) x2 = x + size * 2 - corr;
            if (y2 < (y + corr)) y2 = (y + corr);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private void drawFilledDot(Graphics g, int fieldX, int fieldY) {
        drawEmptyField(g, fieldX, fieldY);
        int radius = 3;
        int x = startX + (fieldX) * size * 2 + (size - radius);
        int y = startY + (fieldY) * size * 2 + (size - radius);
        g.fillOval(x, y, radius * 2, radius * 2);
    }

    private void drawBonus(Graphics g, int fieldX, int fieldY) {
        drawEmptyField(g, fieldX, fieldY);
        int radius = 4;
        int x = startX + (fieldX) * size * 2 + (size - radius);
        int y = startY + (fieldY) * size * 2 + (size - radius);
        g.fillOval(x, y, radius * 2, radius * 2);
    }

    private void drawEmptyField(Graphics g, int fieldX, int fieldY) {
        int x = startX + (fieldX) * size * 2;
        int y = startY + (fieldY) * size * 2;
        g.clearRect(x, y, size * 2 + 1, size * 2 + 1);
    }

    private void drawGameObject(Graphics g, GameObject obj, int fieldX, int fieldY) {
        if (obj.getObjectType() == GameObject.TYPE_EMPTY) {
            drawEmptyField(g, fieldX, fieldY);
        }
        if (obj.getObjectType() == GameObject.TYPE_FILLED_DOT) {
            drawFilledDot(g, fieldX, fieldY);
        }
        if (obj.getObjectType() == GameObject.TYPE_WALL) {
            drawWall(g, fieldX, fieldY);
        }
        if (obj.getObjectType() == GameObject.TYPE_BONUS) {
            drawBonus(g, fieldX, fieldY);
        }
    }

    private int correctField(int field, char xy) {
        int max = dimX;
        if (xy == 'y') {
            max = dimY;
        }
        if (field < 0) {
            return 0;
        }
        if (field >= max) {
            return max - 1;
        }
        return field;
    }

    public void redrawObjectInField(Graphics g, PackMan packMan, int fieldX, int fieldY) {
        GameObject obj;
        obj = field[fieldY][fieldX];
        if (obj.isPackManHere() && (packMan != null)) {
            drawEmptyField(g, fieldX, fieldY);
            packMan.drawPackMan(g);
            return;
        }
        drawGameObject(g, obj, fieldX, fieldY);
    }

    public void drawBoard(Graphics g, PackMan packMan) {
        g.drawRect(startX - 1, startY - 1, dimX * size * 2 + 2, dimY * size * 2 + 2);
        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                redrawObjectInField(g, packMan, x, y);
            }
        }
    }

    public void setPackMan(PackMan packMan, int fieldX, int fieldY) {
        fieldX = correctField(fieldX, 'x');
        fieldY = correctField(fieldY, 'y');
        int newX = startX + (fieldX * size * 2) + size;
        int newY = startY + (fieldY * size * 2) + size;
        field[curPackManY][curPackManX].packManGoesOut(1);
        field[fieldY][fieldX].packManGoesIn();
        curPackManX = fieldX;
        curPackManY = fieldY;
        packMan.changePackmanCoordinates(newX, newY);
    }

    public void movePackMan(Graphics g, PackMan packMan, int direction) {
        packMan.changePackManState();
        packMan.changeDirection(direction);
        int newFieldX = curPackManX, newFieldY = curPackManY;
        switch (direction) {
            case PackMan.DIRECTION_EAST:
                newFieldX += 1;
                break;
            case PackMan.DIRECTION_NORTH:
                newFieldY -= 1;
                break;
            case PackMan.DIRECTION_WEST:
                newFieldX -= 1;
                break;
            case PackMan.DIRECTION_SOUTH:
                newFieldY += 1;
                break;
        }
        if ((newFieldX < 0) || (newFieldX >= dimX)) {
            redrawObjectInField(g, packMan, curPackManX, curPackManY);
            return;
        }
        if ((newFieldY < 0) || (newFieldY >= dimY)) {
            redrawObjectInField(g, packMan, curPackManX, curPackManY);
            return;
        }

        if (!field[newFieldY][newFieldX].isPassable()) {
            redrawObjectInField(g, packMan, curPackManX, curPackManY);
            return;
        }

        int newX = startX + (newFieldX * size * 2) + size;
        int newY = startY + (newFieldY * size * 2) + size;
        field[curPackManY][curPackManX].packManGoesOut();
        field[newFieldY][newFieldX].packManGoesIn();
        redrawObjectInField(g, packMan, curPackManX, curPackManY);
        curPackManX = newFieldX;
        curPackManY = newFieldY;
        packMan.changePackmanCoordinates(newX, newY);
        redrawObjectInField(g, packMan, curPackManX, curPackManY);
    }

    public void changeElementOnField(Graphics g, int x, int y) {
        FieldXY fieldXY = getFieldByCoordinate(x, y);
//        JOptionPane.showMessageDialog(new JFrame(), "fieldX: " + fieldXY.x + " fieldY: " + fieldXY.y);
        if (field[fieldXY.y][fieldXY.x].getObjectType() != lastChangedType) {
            field[fieldXY.y][fieldXY.x].changeObjectType(lastChangedType);
        } else {
            int newType = GameObject.TYPE_EMPTY;
            switch (field[fieldXY.y][fieldXY.x].getObjectType()) {
                case GameObject.TYPE_FILLED_DOT:
                    newType = GameObject.TYPE_BONUS;
                    break;
                case (GameObject.TYPE_BONUS):
                    newType = GameObject.TYPE_WALL;
                    break;
                case (GameObject.TYPE_WALL):
                    newType = GameObject.TYPE_EMPTY;
                    break;
                case (GameObject.TYPE_EMPTY):
                    newType = GameObject.TYPE_FILLED_DOT;
                    break;
            }
            field[fieldXY.y][fieldXY.x].changeObjectType(newType);
            lastChangedType = newType;
        }
        redrawObjectInField(g, null, fieldXY.x, fieldXY.y);
    }

    private FieldXY getFieldByCoordinate(int x, int y) {
        FieldXY fieldXY = new FieldXY();
        fieldXY.x = correctField((int) ((x - startX) / (size * 2)), 'x');
        fieldXY.y = correctField((int) ((y - startY) / (size * 2)), 'y');
        return fieldXY;
    }

    public void saveToFile() {
//        JOptionPane.showMessageDialog(new JFrame(), "file name: "+fileName);
        String fileName = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(levelPath));
        if (fileChooser.showSaveDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
            fileName = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return;
        }

        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write("dim " + dimX + " " + dimY + "\n");
            for (int x = 0; x < dimX; x++) {
                for (int y = 0; y < dimY; y++) {
                    GameObject obj = field[y][x];
                    writer.write("obj " + x + " " + y + " " + obj.getObjectType() + "\n");

                }
            }
            writer.flush();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(new JFrame(), "Ошибка: " + ex.toString());
            return;
        }
        JOptionPane.showMessageDialog(new JFrame(), "Успешно!");
    }

    public boolean loadFromFile(String fileName, boolean quiet) {
        try (FileReader reader = new FileReader(fileName)) {
            int c;
            String s = "";
            String[] sArray;
            while ((c = reader.read()) != -1) {
                if (c == KeyEvent.VK_ENTER) {
                    sArray = s.split(" ");
                    if (sArray[0].equals("obj")) {
                        int x = correctField(Integer.parseInt(sArray[1]), 'x');
                        int y = correctField(Integer.parseInt(sArray[2]), 'y');
                        field[y][x]= new GameObject(Integer.parseInt(sArray[3]));
                    }
                    s = "";
                } else {
                    if ((char)c!='\r') {
                        s += (char) c;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(), "Ошибка: " + ex.toString());
            return false;
        }

        currentMapPath = fileName;
        if (!quiet) {
            JOptionPane.showMessageDialog(new JFrame(), "Успешно!");
        }
        return true;
    }

    public boolean loadFromFile() {
        String fileName;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(levelPath));
        if (fileChooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
            fileName = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return false;
        }
        return loadFromFile(fileName, false);
    }

    public void restartMap() {
        loadFromFile(currentMapPath, true);
    }
}
