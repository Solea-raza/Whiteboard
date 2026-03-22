package mg.arovy.whiteboard.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import mg.arovy.whiteboard.data.Figure;
import mg.arovy.whiteboard.factory.FigureFactory;

public class DrawingView extends View {

    float startX, startY;
    private Paint strokePaint;
    private Paint fillPaint;

    private List<Figure> listFigure = new ArrayList<>();

    private FigureFactory currentFactory;
    private Figure currentFigure = null;
    private Figure selectedFigure = null;

    private float lastX, lastY;

    public DrawingView(Context context) {
           super(context);
        initComponents();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponents();
    }

    private void initComponents(){
        strokePaint = new Paint();
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(8);
        strokePaint.setAntiAlias(true);

        fillPaint = new Paint();
        fillPaint.setColor(Color.TRANSPARENT); // 🔥 important
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        for (Figure figure: listFigure){
            figure.displayCanvas(canvas);
        }

        if (currentFigure != null){
            currentFigure.displayCanvas(canvas);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: handleDown(x, y); break;
            case MotionEvent.ACTION_MOVE: handleMove(x, y); break;
            case MotionEvent.ACTION_UP:   handleUp(x, y);   break;
        }

        invalidate();
        return true;
    }
    // au lieu d'utiliser case,j'ai séparé en des méthodes pour que la méth ne fasse pas +30 lignes
    private void handleDown(float x, float y) {
        lastX = x;
        lastY = y;

        for (int i = listFigure.size() - 1; i >= 0; i--) {
            Figure f = listFigure.get(i);
            if (f.contains(x, y)) {
                selectedFigure = f;
                if (listener != null) listener.onFigureSelected(f, x, y);
                return;
            }
        }

        startX = x;
        startY = y;
    }
    private void handleMove(float x, float y) {
        if (selectedFigure != null) {
            moveSelectedFigure(x, y);
        } else if (currentFactory != null) {
            currentFigure = currentFactory.create(startX, startY, x, y, strokePaint, fillPaint);
        }
    }

    private void moveSelectedFigure(float x, float y) {
        float dx = x - lastX;
        float dy = y - lastY;
        selectedFigure.move(dx, dy);
        lastX = x;
        lastY = y;
    }
    private void handleUp(float x, float y) {
        if (selectedFigure != null) {
            selectedFigure = null;
            return;
        }
        if (currentFigure != null) {
            finalizeCurrentFigure(x, y);
        }
    }

    private void finalizeCurrentFigure(float x, float y) {
        listFigure.add(currentFigure);
        if (listener != null) listener.onFigureSelected(currentFigure, x, y);
        currentFigure = null;
    }
    public void setFigureFactory(FigureFactory factory) {
        this.currentFactory = factory;
    }
    public Bitmap getBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }
    private OnFigureSelectedListener listener;

    public interface OnFigureSelectedListener{
        void onFigureSelected(Figure figure, float x, float y);
    }

    public void setOnFigureSelectedListener(OnFigureSelectedListener l){
        this.listener = l;
    }
    public void removeFigure(Figure f){
        listFigure.remove(f);
        invalidate();
    }
}