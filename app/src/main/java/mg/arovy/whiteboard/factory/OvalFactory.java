package mg.arovy.whiteboard.factory;

import android.graphics.Paint;

import mg.arovy.whiteboard.data.Figure;
import mg.arovy.whiteboard.data.FigureOval;

public class OvalFactory implements FigureFactory {
    @Override
    public Figure create(float sx, float sy, float ex, float ey, Paint strokePaint, Paint fillPaint) {
        return new FigureOval(sx, sy, ex, ey, strokePaint, fillPaint);
    }
}

