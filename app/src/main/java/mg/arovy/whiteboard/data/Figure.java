package mg.arovy.whiteboard.data;

import android.graphics.Canvas;
import android.graphics.Paint;
public abstract class Figure {
    protected float startX;
    protected float startY;
    protected float endX;
    protected float endY;

    protected Paint strokePaint;
    protected Paint fillPaint;

    public Figure(float startX, float startY, float endX, float endY, Paint strokePaint, Paint fillPaint) {
        this.endX = endX;
        this.endY = endY;
        this.startX = startX;
        this.startY = startY;

        // pour que qd on change une couleur, les autres figurents ne changent pas de la même couleur
        this.strokePaint = new Paint(strokePaint);
        this.fillPaint = new Paint(fillPaint);
    }

    public abstract void displayCanvas(Canvas canvas);

    public abstract boolean contains(float x, float y);

    public void move(float dx, float dy) {
        startX += dx;
        startY += dy;
        endX += dx;
        endY += dy;
    }
    public Paint getStrokePaint() {
        return strokePaint;
    }
    public Paint getFillPaint() {
        return fillPaint;
    }
}
