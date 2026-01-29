package mg.arovy.whiteboard.data;

import android.graphics.Canvas;
import android.graphics.Paint;

public class FigureRect extends Figure{
    public FigureRect(float startX, float startY, float endX, float endY, Paint paint) {
        super(startX, startY, endX, endY, paint);
    }

    @Override
    public void displayCanvas(Canvas canvas) {
        canvas.drawRect(startX, startY, endX, endY, paint);
    }
}

