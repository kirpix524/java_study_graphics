import java.awt.*;

public class Board {
    private int dimX,dimY,size;

    private Object[][] field;

    public Board(int dimX, int dimY, int size, int level) {

    }

    private void init(int dimX,int dimY, int size, int level) {
        field = new Object[dimX][dimY];
    }
}
