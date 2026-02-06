package mg.arovy.whiteboard.factory;

import android.graphics.Paint;

import mg.arovy.whiteboard.data.Figure;
import mg.arovy.whiteboard.data.FigureLine;

public class LineFactory implements FigureFactory {
    @Override
    public Figure create(float sx, float sy, float ex, float ey, Paint paint) {
        return new FigureLine(sx, sy, ex, ey, paint);
    }
}
