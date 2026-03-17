package mg.arovy.whiteboard.data;

import android.graphics.Canvas;
import android.graphics.Paint;
//MODIFICATION : suppression des commentaires lié aux anciennes versions du codes (qd displayCnavas n'était pas abstract)
public abstract class Figure {

    protected float startX, startY, endX, endY;
    protected Paint strokePaint;
    protected Paint fillPaint;

    public Figure(float startX, float startY, float endX, float endY,
                  Paint strokePaint, Paint fillPaint){

        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        // 🔥 COPIE obligatoire
        this.strokePaint = new Paint(strokePaint);
        this.fillPaint = new Paint(fillPaint);
    }

    public abstract void displayCanvas(Canvas canvas);
    public abstract boolean contains(float x, float y);

    public void move(float dx, float dy){
        startX += dx;
        startY += dy;
        endX += dx;
        endY += dy;
    }
}