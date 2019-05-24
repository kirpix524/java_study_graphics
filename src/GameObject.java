public  class GameObject {
    public static int TYPE_EMPTY = 0;
    public static int TYPE_FILLED_DOT = 1;
    public static int TYPE_BONUS = 2;
    public static int TYPE_WALL = 3;

    private int scoreAmount;
    private int objectType;
    private boolean isPackManHere;

    public GameObject(int objectType, int scoreAmount) {
        init(objectType, scoreAmount);
    }

    public void changeObjectType(int newObjectType, int newScoreAmount) {
        newObjectType = correctObjectType(newObjectType);
        if ((newObjectType==TYPE_WALL)||(newObjectType==TYPE_EMPTY)) {
            newScoreAmount=0;
        } else {
            newScoreAmount=correctScoreAmount(newScoreAmount);
        }
        this.objectType = newObjectType;
        this.scoreAmount = newScoreAmount;
    }

    public boolean isEatable() {
        if ((objectType==TYPE_FILLED_DOT)||(objectType==TYPE_BONUS)) {
            return true;
        }
        return false;
    }

    public boolean isPassable() {
        if (objectType==TYPE_WALL) {
            return false;
        }
        return true;
    }

    public int getScoreAmount() {
        if ((objectType==TYPE_EMPTY)||(objectType==TYPE_WALL)) {
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
        if (teleport!=1) {
            this.changeObjectType(TYPE_EMPTY, 0);
        }
    }

    private void init(int objectType, int scoreAmount) {
        this.objectType = correctObjectType(objectType);
        this.scoreAmount = correctScoreAmount(scoreAmount);
    }

    private int correctObjectType(int newObjectType) {
        if ((newObjectType!=TYPE_BONUS)&&(newObjectType!=TYPE_EMPTY)&&(newObjectType!=TYPE_FILLED_DOT)&&(newObjectType!=TYPE_WALL)) {
            return TYPE_EMPTY;
        }
        return newObjectType;
    }

    private int correctScoreAmount(int scoreAmount) {
        if (scoreAmount<0) {
            return 0;
        }
        return scoreAmount;
    }


}
