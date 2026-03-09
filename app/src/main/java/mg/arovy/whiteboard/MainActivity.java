package mg.arovy.whiteboard;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mg.arovy.whiteboard.factory.LineFactory;
import mg.arovy.whiteboard.factory.RectFactory;
import mg.arovy.whiteboard.factory.OvalFactory;
import mg.arovy.whiteboard.views.DrawingView;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addDrawingView();
        setupButtons();
        setupMenuToggle();
        setupShapeMenu();
    }

    private void addDrawingView(){

        drawingView = new DrawingView(this);

        ViewGroup container = findViewById(R.id.drawing_container);

        container.addView(
                drawingView,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        drawingView.setFigureFactory(new LineFactory());
    }

    //ajout de setUpButton pour lier au Factory correspondant les boutons et écouter les cliques de la souris pour changer
    //ce qu'il faut dessiner
    private void setupButtons() {
        ImageButton btnLine = findViewById(R.id.btn_line);
        ImageButton btnRect = findViewById(R.id.btn_rect);
        ImageButton btnOval = findViewById(R.id.btn_oval);

        btnLine.setOnClickListener(v -> drawingView.setFigureFactory(new LineFactory()));
        btnRect.setOnClickListener(v -> drawingView.setFigureFactory(new RectFactory()));
        btnOval.setOnClickListener(v -> drawingView.setFigureFactory(new OvalFactory()));
    }
    private void setupMenuToggle(){

        View toggle = findViewById(R.id.toggle_button);
        View mainMenu = findViewById(R.id.menu_main);

        toggle.setOnClickListener(v -> {

            if(mainMenu.getVisibility() == View.GONE){
                mainMenu.setVisibility(View.VISIBLE);
            }else{
                mainMenu.setVisibility(View.GONE);
            }

        });
    }
    private void setupShapeMenu(){

        View shapeIcon = findViewById(R.id.icon_shape);
        View shapeMenu = findViewById(R.id.menu_shapes);

        shapeIcon.setOnClickListener(v -> {

            if(shapeMenu.getVisibility() == View.GONE){
                shapeMenu.setVisibility(View.VISIBLE);
            }else{
                shapeMenu.setVisibility(View.GONE);
            }

        });

    }
}