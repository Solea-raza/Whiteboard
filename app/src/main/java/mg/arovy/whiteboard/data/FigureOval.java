package mg.arovy.whiteboard.data;


import android.graphics.Canvas;
import android.graphics.Paint;

public class FigureOval extends Figure{
    public FigureOval(float startX, float startY, float endX, float endY, Paint paint) {
        super(startX, startY, endX, endY, paint);
    }

    @Override
    public void displayCanvas(Canvas canvas) {
        canvas.drawOval(startX, startY, endX, endY, paint);
    }
}
