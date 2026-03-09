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
import mg.arovy.whiteboard.factory.FigureFactory;

public class DrawingView extends View {
    float startX;
    float startY;

    private Paint drawingPaint;
    private List<Figure> listFigure = new ArrayList<>();
    // MODIFICATIONS : au lieu d'utiliser currentFigureType on utilise l'interface figureFactory

    private FigureFactory currentFactory;
    private Figure currentFigure = null;
    private Figure selectedFigure = null;
    private float lastX;
    private float lastY;
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
            case MotionEvent.ACTION_DOWN:

                lastX = x;
                lastY = y;

                for(int i = listFigure.size() - 1; i >= 0; i--) {

                    Figure f = listFigure.get(i);

                    if(f.contains(x,y)){
                        selectedFigure = f;
                        return true;
                    }
                }

                startX = x;
                startY = y;
                break;

            //MODIF : on n'utilise plus les switch/case pour changer de type mais on crée en fonction du currentFigure
            case MotionEvent.ACTION_MOVE:

                if(selectedFigure != null){

                    float dx = x - lastX;
                    float dy = y - lastY;

                    selectedFigure.move(dx,dy);

                    lastX = x;
                    lastY = y;

                }
                else if(currentFactory != null){

                    currentFigure = currentFactory.create(
                            startX,startY,x,y,drawingPaint
                    );

                }

                break;

            case MotionEvent.ACTION_UP:

                if(selectedFigure != null){
                    selectedFigure = null;
                }
                else if(currentFigure != null){
                    listFigure.add(currentFigure);
                    currentFigure = null;
                }

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
