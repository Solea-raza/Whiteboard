package mg.arovy.whiteboard.factory;

import android.graphics.Paint;

import mg.arovy.whiteboard.data.Figure;
import mg.arovy.whiteboard.data.FigureRect;

public class RectFactory implements FigureFactory {
    @Override
    public Figure create(float sx, float sy, float ex, float ey, Paint paint) {
        return new FigureRect(sx, sy, ex, ey, paint);
    }
}
