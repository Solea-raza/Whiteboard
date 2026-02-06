package mg.arovy.whiteboard.factory;

import android.graphics.Paint;

import mg.arovy.whiteboard.data.Figure;

public interface FigureFactory {
    Figure create(
            float startX,
            float startY,
            float endX,
            float endY,
            Paint paint
    );
}

