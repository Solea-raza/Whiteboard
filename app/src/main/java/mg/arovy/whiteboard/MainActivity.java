package mg.arovy.whiteboard;

import android.os.Bundle;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import mg.arovy.whiteboard.data.Figure;
import mg.arovy.whiteboard.factory.LineFactory;
import mg.arovy.whiteboard.factory.OvalFactory;
import mg.arovy.whiteboard.factory.RectFactory;
import mg.arovy.whiteboard.utils.DrawingExporter;
import mg.arovy.whiteboard.utils.FigureMenuManager;
import mg.arovy.whiteboard.views.DrawingView;
// la classe main ne sert qu'à tout assembler ici
public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;
    private FigureMenuManager menuManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // appelle les méthodes
        addDrawingView();
        setupMenuManager();
        setupButtons();
        setupShare();
    }

    // ajouter un dessin, par défaut une ligne
    private void addDrawingView() {
        drawingView = new DrawingView(this);
        ViewGroup container = findViewById(R.id.drawing_container);
        container.addView(drawingView,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
        drawingView.setFigureFactory(new LineFactory());
    }

    private void setupMenuManager() {

        menuManager = new FigureMenuManager(this, new FigureMenuManager.MenuActionListener() {
            @Override
            public void onFigureDeleted(Figure figure) {
                drawingView.removeFigure(figure);
            }

            @Override
            public void onDrawingChanged() {
                drawingView.invalidate();
            }
        });
        // un listner qui va afficher le menu flottant qd on clique sur un dessin
        drawingView.setOnFigureSelectedListener(
                (figure, x, y) -> menuManager.showFloatingMenu(figure, x, y, drawingView)
        );
    }

    private void setupButtons() {
        // selon les boutons, on va créer la forme
        findViewById(R.id.btn_line).setOnClickListener(
                v -> drawingView.setFigureFactory(new LineFactory())
        );
        findViewById(R.id.btn_rect).setOnClickListener(
                v -> drawingView.setFigureFactory(new RectFactory())
        );
        findViewById(R.id.btn_oval).setOnClickListener(
                v -> drawingView.setFigureFactory(new OvalFactory())
        );
    }

    // écoute si on appuie sur le bouton share
    private void setupShare() {
        findViewById(R.id.btn_share).setOnClickListener(
                v -> DrawingExporter.share(this, drawingView)
        );
    }
}