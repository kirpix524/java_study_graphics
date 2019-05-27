public class GameObject {
    public static final int TYPE_EMPTY = 0;
    public static final int TYPE_FILLED_DOT = 1;
    public static final int TYPE_BONUS = 2;
    public static final int TYPE_WALL = 3;

    private final int SCORE_WALL = 0;
    private final int SCORE_EMPTY = 0;
    private final int SCORE_FILLED = 10;
    private final int SCORE_BONUS = 100;

    private int scoreAmount;
    private int objectType;
    private boolean isPackManHere;

    public GameObject(int objectType) {
        init(objectType, getScoreAmount(objectType));
    }

    private int getScoreAmount(int objectType) {
        switch (objectType) {
            case (TYPE_WALL):
                return SCORE_WALL;
            case (TYPE_FILLED_DOT):
                return SCORE_FILLED;
            case (TYPE_BONUS):
                return SCORE_BONUS;
            case (TYPE_EMPTY):
                return SCORE_EMPTY;
            default:
                return 0;
        }
    }

    public void changeObjectType(int newObjectType) {
        newObjectType = correctObjectType(newObjectType);
        this.objectType = newObjectType;
        this.scoreAmount = getScoreAmount(newObjectType);
    }

    public boolean isEatable() {
        if ((objectType == TYPE_FILLED_DOT) || (objectType == TYPE_BONUS)) {
            return true;
        }
        return false;
    }

    public boolean isPassable() {
        if (objectType == TYPE_WALL) {
            return false;
        }
        return true;
    }

    public int getScoreAmount() {
        if ((objectType == TYPE_EMPTY) || (objectType == TYPE_WALL)) {
            return 0;
        }
        return scoreAmount;
    }

    public int getObjectType() {
        return objectType;
    }

    public boolean isPackManHere() {
        return isPackManHere;
    }

    public void packManGoesIn() {
        isPackManHere = true;
    }

    public void packManGoesOut() {
        packManGoesOut(0);
    }

    public void packManGoesOut(int teleport) {
        isPackManHere = false;
        if (teleport != 1) {
            this.changeObjectType(TYPE_EMPTY);
        }
    }

    private void init(int objectType, int scoreAmount) {
        this.objectType = correctObjectType(objectType);
        this.scoreAmount = correctScoreAmount(scoreAmount);
    }

    private int correctObjectType(int newObjectType) {
        if ((newObjectType != TYPE_BONUS) && (newObjectType != TYPE_EMPTY) && (newObjectType != TYPE_FILLED_DOT) && (newObjectType != TYPE_WALL)) {
            return TYPE_EMPTY;
        }
        return newObjectType;
    }

    private int correctScoreAmount(int scoreAmount) {
        if (scoreAmount < 0) {
            return 0;
        }
        return scoreAmount;
    }


}
