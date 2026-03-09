package mg.arovy.whiteboard.data;

import android.graphics.Canvas;
import android.graphics.Paint;

public class FigureLine extends Figure{
    public FigureLine(float startX, float startY, float endX, float endY, Paint paint) {
        super(startX, startY, endX, endY, paint);
    }

    @Override
    public void displayCanvas(Canvas canvas) {
        canvas.drawLine(startX, startY, endX, endY, paint);
    }
    @Override
    public boolean contains(float x, float y) {

        float dx = endX - startX;
        float dy = endY - startY;

        float length = (float)Math.sqrt(dx*dx + dy*dy);

        float distance =
                Math.abs(dy*x - dx*y + endX*startY - endY*startX) / length;

        return distance < 20;
    }
}
