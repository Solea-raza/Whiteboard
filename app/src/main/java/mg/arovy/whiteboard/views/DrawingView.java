package mg.arovy.whiteboard.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import mg.arovy.whiteboard.data.Figure;
import mg.arovy.whiteboard.factory.FigureFactory;

public class DrawingView extends View {
    float startX;
    float startY;
    //float currentX;
    //float currentY;
    private Paint drawingPaint;
    private Canvas drawingCanvas;
    private List<Figure> listFigure = new ArrayList<>();
    // MODIFICATIONS : au lieu d'utiliser currentFigureType on utilise l'interface figureFactory
    // private int currentFigureType = 2;
    private FigureFactory currentFactory;
    private Figure currentFigure = null;

    public DrawingView(Context context) {
        super(context);
        initComponents();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponents();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponents();
    }

    private void initComponents(){
        drawingPaint = new Paint();
        drawingPaint.setColor(Color.RED);
        drawingPaint.setStyle(Paint.Style.FILL);
        drawingPaint.setAntiAlias(true);
        drawingPaint.setStrokeWidth(10);
    }

    @Override
    protected  void onDraw(Canvas drawingCanvas){
        super.onDraw(drawingCanvas);
        for (Figure figure: listFigure){
            figure.displayCanvas(drawingCanvas);
        }
            if (currentFigure!= null)
                currentFigure.displayCanvas(drawingCanvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                startX = x;
                startY = y;
                break;
            /*case (MotionEvent.ACTION_MOVE):
                    currentX = x;
                    currentY = y;
                switch (currentFigureType){
                    case 1:
                        currentFigure = new FigureLine(startX, startY, currentX, currentY, drawingPaint);
                        break;
                    case 2:
                        currentFigure = new FigureRect(startX, startY, currentX, currentY, drawingPaint);
                        break;
                    case 3:
                        currentFigure = new FigureOval(startX, startY, currentX, currentY, drawingPaint);
                        break;
                }
                break;*/
            //MODIF : on n'utilise plus les switch/case pour changer de type mais on crée en fonction du currentFigure
            case MotionEvent.ACTION_MOVE:
                if (currentFactory != null) {
                    currentFigure = currentFactory.create(
                            startX, startY, x, y, drawingPaint
                    );
                }
                break;

            case (MotionEvent.ACTION_UP):
                    listFigure.add(currentFigure);
                break;
        }
        invalidate();
        return true;
    }

    //setters pour FigureFactory
    public void setFigureFactory(FigureFactory factory) {
        this.currentFactory = factory;
    }

}
