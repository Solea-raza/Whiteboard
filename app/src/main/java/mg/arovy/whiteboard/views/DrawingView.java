package mg.arovy.whiteboard.views;

import android.content.Context;
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
import mg.arovy.whiteboard.data.FigureLine;
import mg.arovy.whiteboard.data.FigureOval;
import mg.arovy.whiteboard.data.FigureRect;

public class DrawingView extends View {
    float startX;
    float startY;
    float currentX;
    float currentY;
    private Paint drawingPaint;
    private Canvas drawingCanvas;
    private List<Figure> listFigure = new ArrayList<>();
    private int currentFigureType = 2;
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
        //int radius = 200;
        //drawingCanvas.drawCircle(posX, posY, radius, drawingPaint);
        for (Figure figure: listFigure){
            figure.displayCanvas(drawingCanvas);
        }
            if (currentFigure!= null)
                currentFigure.displayCanvas(drawingCanvas);
        //drawingCanvas.drawLine(startX, startY, currentX, currentY, drawingPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //posX = event.getX(); // position X dans la View
        //posY = event.getY(); //position Y dans la View
        //invalidate();
        //return true;

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                startX = x;
                startY = y;
                break;
            case (MotionEvent.ACTION_MOVE):
                    currentX = x;
                    currentY = y;
                    //currentFigure = new Figure(currentFigureType, startX, startY, currentX,currentY, drawingPaint);
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
                break;

            case (MotionEvent.ACTION_UP):
                    listFigure.add(currentFigure);
                break;
        }
        invalidate();
        return true;
    }
}