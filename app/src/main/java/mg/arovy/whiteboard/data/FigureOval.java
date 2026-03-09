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
    @Override
    public boolean contains(float x, float y) {

        float centerX = (startX + endX) / 2;
        float centerY = (startY + endY) / 2;

        float radiusX = Math.abs(endX - startX) / 2;
        float radiusY = Math.abs(endY - startY) / 2;

        if(radiusX == 0 || radiusY == 0) return false;

        float value =
                ((x - centerX)*(x - centerX))/(radiusX*radiusX) +
                        ((y - centerY)*(y - centerY))/(radiusY*radiusY);

        return value <= 1;
    }
}
