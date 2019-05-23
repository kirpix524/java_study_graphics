import java.awt.*;

public class Board {
    private int startX;
    private int startY;
    private int sizeX;
    private int sizeY;
    private int winLen; //Длина, которую нужно собрать для победы
    private char[][] fields;
    private final int fieldSize = 20;

    private class Checker {
        private int signsInRow = 0;
        private int winLen;

        private void dropCounter() {
            signsInRow = 0;
        }

        private void incCounter() {
            signsInRow += 1;
        }

        private boolean checkCounter() {
            if (signsInRow >= winLen) {
                return true;
            } else {
                return false;
            }

        }

        public Checker(int winLen) {
            if (winLen < 3) {
                winLen = 3;
            }
            this.winLen = winLen;
            dropCounter();
        }

        public boolean checkWin(char[][] fields, int x, int y, char sign) {
            if (fields[x][y] == sign) {
                incCounter();
            } else {
                dropCounter();
            }
            if (checkCounter()) {
                return true;
            }
            return false;
        }
    }

    private class Coordinates {
        private int x;
        private int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private void init(int sizeX, int sizeY, int winLen, int startX, int startY) {
        if (sizeX < 3) sizeX = 3;
        if (sizeY < 3) sizeY = 3;
        if (winLen < 3) winLen = 3;
        if (winLen > sizeX) winLen = sizeX;
        if (winLen > sizeY) winLen = sizeY;
        if (startX < 0) startX = 0;
        if (startY<0) startY = 0;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.winLen = winLen;
        this.startX = startX;
        this.startY = startY;
        //Заполняем игровые поля
        fields = new char[sizeX][sizeY];
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                fields[x][y] = '*';
            }
        }
    }

    public Board() {
        int sizeX = 3;
        int sizeY = 3;
        int winLen = 3;
        int startX = 20;
        int startY=100;
        init(sizeX, sizeY, winLen, startX, startY);
    }

    public Board(int sizeX, int sizeY) {
        int winLen = 3;
        int startX = 20;
        int startY = 100;
        init(sizeX, sizeY, winLen, startX, startY);
    }

    public Board(int sizeX, int sizeY, int winLen) {
        int startX = 20;
        int startY = 100;
        init(sizeX, sizeY, winLen, startX, startY);
    }

    public Board(int sizeX, int sizeY, int winLen, int startX, int startY) {
        init(sizeX, sizeY, winLen, startX, startY);
    }

    private Coordinates getSignPlace(int coordX, int coordY) {
        int x=(int)(coordX-startX)/fieldSize;
        int y=(int)(coordY-startY)/fieldSize;
        return new Coordinates(x, y);
    }


    public boolean setMark(int coordX, int coordY, char sign) {
        if ((coordX<startX)||(coordX>(startX+(fieldSize*sizeX)))) return false;
        if ((coordY<startY)||(coordY>(startY+(fieldSize*sizeY)))) return false;
        Coordinates signPlace = getSignPlace(coordX, coordY);
        Result res=setSign(signPlace.getX(), signPlace.getY(), sign);
        return res.getResult();
    }

    public Result setSign(int x, int y, char sign) {
        boolean res = true;
        String mes = "";
        if ((sign != 'X') && (sign != 'O')) {
            return new Result(false, "Wrong symbol of the board! Only X or O are allowed!");
        }

        if ((x < 0) || (x > sizeX - 1) || (y < 0) || (y > sizeY - 1)) {
            return new Result(false, "Неправильные координаты! Координата X должна быть от 1 до " + sizeX + ", координата Y должна быть от 1 до " + sizeY);
        }
        if (fields[x][y] != '*') {
            return new Result(false, "Поле [" + (x + 1) + "," + (y + 1) + "] занято!");
        }
        fields[x][y] = sign;
        return new Result(res, mes);
    }

    public boolean isWinner(char sign) {
        boolean res = false;
        String mes = "";
        //Checking horizontal and vertical lines
        Checker checker = new Checker(winLen);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (checker.checkWin(fields, x, y, sign)) {
                    return true;
                }
            }
        }
        checker = new Checker(winLen);
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (checker.checkWin(fields, x, y, sign)) {
                    return true;
                }
            }
        }
        //Checking diagonals
        //main diagonal
        checker = new Checker(winLen);
        for (int x = sizeX - 1; x >= 0; x--) {
            int xTmp = x;
            int y = 1;
            while ((xTmp < sizeX) && (y < sizeY)) {
                if (checker.checkWin(fields, xTmp, y, sign)) {
                    return true;
                }
                xTmp += 1;
                y += 1;
            }
        }
        checker = new Checker(winLen);
        for (int y = sizeY - 1; y >= 1; y--) {
            int yTmp = y;
            int x = 1;
            while ((yTmp < sizeY) && (x < sizeX)) {
                if (checker.checkWin(fields, x, yTmp, sign)) {
                    return true;
                }
                x += 1;
                yTmp += 1;
            }
        }
        //sideline diagonal
        checker = new Checker(winLen);
        for (int x = 0; x < sizeX; x++) {
            int xTmp = x;
            int y = 1;
            while ((xTmp >= 0) && (y < sizeY)) {
                if (checker.checkWin(fields, xTmp, y, sign)) {
                    return true;
                }
                xTmp -= 1;
                y += 1;
            }
        }
        checker = new Checker(winLen);
        for (int y = 1; y < sizeY; y++) {
            int yTmp = y;
            int x = 4;
            while ((yTmp < sizeY) && (x >= 0)) {
                if (checker.checkWin(fields, x, yTmp, sign)) {
                    return true;
                }
                x -= 1;
                yTmp += 1;
            }
        }
        return false;
    }

    public boolean isFull() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (fields[x][y] == '*') {
                    return false;
                }
            }
        }
        return true;
    }

    public void sayMoveMessage(char sign) {
        sayMessage("Ходит игрок \"" + sign + "\", для победы нужно выстроить " + winLen + " " + sign + "-ков в ряд", 0);
        sayMessage("Введите координаты поля в формате X Y", 0);
        //sayMessage("Для выхода из игры введите ^",1);
    }

    public void sayWinMessage(char sign) {
        sayMessage("Победил игрок \"" + sign + "\"!, он выстроил " + winLen + " " + sign + "-ков в ряд!", 1);
    }

    public void sayMessage(String message, int linesAfter) {
        System.out.println(message);
        if (linesAfter > 0) {
            for (int lines = 0; lines <= linesAfter; lines++) {
                System.out.println();
            }
        }

    }
}
