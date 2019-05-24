import java.awt.*;

public class PackMan {
    public static final int DIRECTION_EAST = 0;
    public static final int DIRECTION_NORTH = 1;
    public static final int DIRECTION_WEST = 2;
    public static final int DIRECTION_SOUTH = 3;

    public static final int STATE_OPENED = 0;
    public static final int STATE_CLOSED = 1;

    private int stepSize;

    private int positionX;
    private int positionY;
    private int size;
    private int state;
    private int direction;

    private int borderX, borderY, borderWidth, borderHeight;

    public PackMan(int x, int y, int size) {
        init(x, y, size, DIRECTION_EAST, STATE_CLOSED,0,0,0,0);
    }

    public PackMan(int x, int y, int size, int direction) {
        init(x, y, size, direction, STATE_CLOSED,0,0,0,0);
    }

    public PackMan(int x, int y, int size, int direction, int state) {
        init(x, y, size, direction, state,0,0,0,0);
    }

    public PackMan(int x, int y, int size, int direction, int state, int borderX, int borderY, int borderWidth, int borderHeight) {
        init(x, y, size, direction, state,borderX, borderY, borderWidth, borderHeight);
    }

    private void init(int x, int y, int size, int direction, int state, int borderX, int borderY, int borderWidth, int borderHeight) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (size < 10) size = 10;
        if ((direction != DIRECTION_EAST) && (direction != DIRECTION_NORTH) && (direction != DIRECTION_WEST) && (direction != DIRECTION_SOUTH))
            direction = DIRECTION_EAST;
        if ((state != STATE_OPENED) && (state != STATE_CLOSED)) state = STATE_CLOSED;
        this.positionX = x;
        this.positionY = y;
        this.size = size;
        this.stepSize = (int) (size * 1.5);
        this.direction = direction;
        this.state = state;
        setBorderCoordinates(borderX, borderY, borderWidth, borderHeight);
    }

    public void drawPackMan(Graphics g) {
        int x1, y1, x2, y2;
        double angle;
        int startAngle, arcAngle;

        angle = 45.0;
        if (state == STATE_CLOSED) angle = 10.0;

        startAngle = (int) (angle + direction * 90);
        arcAngle = (int) (360 - angle * 2);


        if ((direction == DIRECTION_WEST) || (direction == DIRECTION_SOUTH)) angle += 180;
        if ((direction == DIRECTION_EAST) || (direction == DIRECTION_WEST)) {
            x1 = positionX + (int) (size * Math.cos(Math.toRadians(angle)));
            y1 = positionY - (int) (size * Math.sin(Math.toRadians(angle)));
            x2 = positionX + (int) (size * Math.cos(Math.toRadians(angle)));
            y2 = positionY + (int) (size * Math.sin(Math.toRadians(angle)));

        } else {
            x1 = positionX - (int) (size * Math.sin(Math.toRadians(angle)));
            y1 = positionY - (int) (size * Math.cos(Math.toRadians(angle)));
            x2 = positionX + (int) (size * Math.sin(Math.toRadians(angle)));
            y2 = positionY - (int) (size * Math.cos(Math.toRadians(angle)));
        }
        g.drawLine(positionX, positionY, x1, y1);
        g.drawLine(positionX, positionY, x2, y2);
        g.drawArc(positionX - size, positionY - size, size * 2, size * 2, startAngle, arcAngle);
    }

    public void erasePackMan(Graphics g) {
        g.clearRect(positionX - size, positionY - size, size * 2 + 1, size * 2 + 1);
    }

    public void movePackMan(Graphics g, int newDirection) {
        movePackMan(g, newDirection, 1);
    }

    private void changePosition(int difX, int difY) {
        if ((borderX >= 0) && (borderY >= 0) && (borderHeight > 0) && (borderWidth > 0)) {
            if (positionX + difX < borderX + size + 1) {
                difX = borderX + size + 1 - positionX;
            }
            if (positionX + difX > borderX + borderWidth - (size + 1)) {
                difX = borderX + borderWidth - (size + 1) - positionX;
            }
            if (positionY + difY < borderY + size + 1) {
                difY = borderY + size + 1 - positionY;
            }
            if (positionY + difY > borderY + borderHeight - (size + 1)) {
                difY = borderY + borderHeight - (size + 1) - positionY;
            }
        }

        positionX += difX;
        positionY += difY;
    }

    private void changeDirection(int newDirection) {
        this.direction = newDirection;
    }

    public void changePackManState() {
        if (state == STATE_CLOSED) {
            state = STATE_OPENED;
        } else {
            state = STATE_CLOSED;
        }
    }

    public void movePackMan(Graphics g, int newDirection, int numberOfSteps) {
        int difX = 0, difY = 0;
        //
        if (numberOfSteps < 1) numberOfSteps = 1;
        //
        switch (newDirection) {
            case DIRECTION_EAST:
                difX = stepSize * numberOfSteps;
                break;
            case DIRECTION_NORTH:
                difY = -1 * stepSize * numberOfSteps;
                break;
            case DIRECTION_WEST:
                difX = -1 * stepSize * numberOfSteps;
                break;
            case DIRECTION_SOUTH:
                difY = stepSize * numberOfSteps;
                break;
            default:
                return;
        }
        //
        changePackManState();
        erasePackMan(g);
        //
        changePosition(difX, difY);
        changeDirection(newDirection);
        //
        drawPackMan(g);
    }

    public void putPackManInTheField(Graphics g, int x, int y) {
        int difX, difY;
        erasePackMan(g);
        difX = x - positionX;
        difY = y - positionY;
        changePosition(difX, difY);
        drawPackMan(g);
    }

    private void setBorderCoordinates(int borderX, int borderY, int borderWidth, int borderHeight) {
        if (borderX < 0) borderX = 0;
        if (borderY < 0) borderY = 0;
        if (borderWidth < 0) borderWidth = 0;
        if (borderHeight < 0) borderHeight = 0;
        this.borderX = borderX;
        this.borderY = borderY;
        this.borderWidth = borderWidth;
        this.borderHeight = borderHeight;
    }

    public void setBorders(Graphics g, int borderX, int borderY, int borderWidth, int borderHeight) {
        setBorderCoordinates(borderX,borderY,borderWidth,borderHeight);
        putPackManInTheField(g, positionX, positionY);
    }
}
