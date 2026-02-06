package mg.arovy.whiteboard.data;

import android.graphics.Canvas;
import android.graphics.Paint;
//MODIFICATION : suppression des commentaires lié aux anciennes versions du codes (qd displayCnavas n'était pas abstract)
public abstract class Figure {
    protected float startX;
    protected float startY;
    protected float endX;
    protected float endY;

    protected Paint paint;

    public Figure(float startX, float startY, float endX, float endY, Paint paint){
        this.endX = endX;
        this.endY = endY;
        this.paint = paint;
        this.startX = startX;
        this.startY = startY;
    }

    public abstract void displayCanvas(Canvas canvas);

}
