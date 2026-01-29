package mg.arovy.whiteboard.data;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Figure {
    protected float startX;
    protected float startY;
    protected float endX;
    protected float endY;

    protected Paint paint;

    public Figure(float startX, float startY, float endX, float endY, Paint paint){
        this.endX = endX;
        this.endY = endY;
        this.paint = paint;
        this.startX = startX;
        this.startY = startY;
    }

    public abstract void displayCanvas(Canvas canvas);
//    public void display(Canvas canvas){
//        switch (){
//
//            case (1): //line
//                canvas.drawLine(startX,startY,endX,endY, paint);
//                break;
//            case (2): //rect
//                canvas.drawRect(startX,startY,endX,endY, paint);
//                break;
//            case (3): //oval
//                canvas.drawOval(startX,startY,endX,endY, paint);
//                break;
//        }
//   }

}
