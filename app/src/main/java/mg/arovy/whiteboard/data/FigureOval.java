package mg.arovy.whiteboard.data;


import android.graphics.Canvas;
import android.graphics.Paint;

public class FigureOval extends Figure{
    public FigureOval(float startX, float startY, float endX, float endY, Paint strokePaint, Paint fillPaint) {
        super(startX, startY, endX, endY, strokePaint, fillPaint);
    }

    @Override
    public void displayCanvas(Canvas canvas) {
        canvas.drawOval(startX, startY, endX, endY, fillPaint);
        canvas.drawOval(startX, startY, endX, endY, strokePaint);
    }
    @Override
    public boolean contains(float x, float y) {

        // calcul du centre et du radius
        float centerX = (startX + endX) / 2;
        float centerY = (startY + endY) / 2;

        float radiusX = Math.abs(endX - startX) / 2;
        float radiusY = Math.abs(endY - startY) / 2;

        // si le radius est égale à 0 donc ce n'est pas un cercle
        if(radiusX == 0 || radiusY == 0) return false;

        // déclarer un point de coordonnées (x,y) et ce point est
        // à l'intérieur si la valeur est <=1
        float value =
                ((x - centerX)*(x - centerX))/(radiusX*radiusX) +
                        ((y - centerY)*(y - centerY))/(radiusY*radiusY);

        return value <= 1;
    }
}
