package mg.arovy.whiteboard.data;

import android.graphics.Canvas;
import android.graphics.Paint;

public class FigureRect extends Figure{
    public FigureRect(float startX, float startY, float endX, float endY, Paint strokePaint, Paint fillPaint) {
        super(startX, startY, endX, endY, strokePaint, fillPaint);
    }

    @Override
    public void displayCanvas(Canvas canvas) {
        canvas.drawRect(startX, startY, endX, endY, strokePaint);
        canvas.drawRect(startX, startY, endX, endY, fillPaint);
    }
    @Override
    public boolean contains(float x, float y) {

        float left = Math.min(startX, endX);
        float right = Math.max(startX, endX);
        float top = Math.min(startY, endY);
        float bottom = Math.max(startY, endY);

        return x >= left && x <= right && y >= top && y <= bottom;
    }
}

