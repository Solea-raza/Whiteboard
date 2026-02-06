package mg.arovy.whiteboard;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        setupButtons();   //les boutons son créés ici
    }

    private void addDrawingView(){
        drawingView = new DrawingView(this);
        drawingView.setId(View.generateViewId());

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        );

        // place le DrawingView sous les boutons --> sinon il ne se passe rien qd on clique sur les boutons
        params.topToBottom = findViewById(R.id.button_layout).getId();
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;

        ConstraintLayout mainLayout = findViewById(R.id.main);
        mainLayout.addView(drawingView, params);

        drawingView.setFigureFactory(new LineFactory()); //valeure par défaut
    }

    //ajout de setUpButton pour lier au Factory correspondant les boutons et écouter les cliques de la souris pour changer
    //ce qu'il faut dessiner
    private void setupButtons() {
        Button btnLine = findViewById(R.id.btn_line);
        Button btnRect = findViewById(R.id.btn_rect);
        Button btnOval = findViewById(R.id.btn_oval);

        btnLine.setOnClickListener(v -> drawingView.setFigureFactory(new LineFactory()));
        btnRect.setOnClickListener(v -> drawingView.setFigureFactory(new RectFactory()));
        btnOval.setOnClickListener(v -> drawingView.setFigureFactory(new OvalFactory()));
    }
}