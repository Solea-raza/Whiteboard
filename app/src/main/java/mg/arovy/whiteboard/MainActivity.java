package mg.arovy.whiteboard;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Color;

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
        setupShapeMenu();
        setupColorPalette();
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

    // 🎯 choix des formes
    private void setupButtons() {
        ImageButton btnLine = findViewById(R.id.btn_line);
        ImageButton btnRect = findViewById(R.id.btn_rect);
        ImageButton btnOval = findViewById(R.id.btn_oval);

        btnLine.setOnClickListener(v -> drawingView.setFigureFactory(new LineFactory()));
        btnRect.setOnClickListener(v -> drawingView.setFigureFactory(new RectFactory()));
        btnOval.setOnClickListener(v -> drawingView.setFigureFactory(new OvalFactory()));
    }

    // 📂 menu formes
    private void setupShapeMenu(){

        View shapeIcon = findViewById(R.id.icon_shape);
        View shapeMenu = findViewById(R.id.menu_shapes);

        shapeIcon.setOnClickListener(v -> {
            if(shapeMenu.getVisibility() == View.GONE){
                shapeMenu.setVisibility(View.VISIBLE);
            } else {
                shapeMenu.setVisibility(View.GONE);
            }
        });
    }

    // 🎨 palette + modes
    private void setupColorPalette(){

        // 🔥 MODE
        findViewById(R.id.btn_stroke)
                .setOnClickListener(v -> {
                    drawingView.setStrokeMode();
                    Toast.makeText(this,"Mode bordure",Toast.LENGTH_SHORT).show();
                });

        findViewById(R.id.btn_fill)
                .setOnClickListener(v -> {
                    drawingView.setFillMode();
                    Toast.makeText(this,"Mode fond",Toast.LENGTH_SHORT).show();
                });

        // 🎨 COULEURS
        findViewById(R.id.color_black)
                .setOnClickListener(v -> drawingView.setColor(Color.BLACK));

        findViewById(R.id.color_red)
                .setOnClickListener(v -> drawingView.setColor(Color.RED));

        findViewById(R.id.color_green)
                .setOnClickListener(v -> drawingView.setColor(Color.GREEN));

        findViewById(R.id.color_blue)
                .setOnClickListener(v -> drawingView.setColor(Color.BLUE));

        findViewById(R.id.color_custom)
                .setOnClickListener(v -> openColorPicker());
    }

    // 🎯 color picker dynamique (CORRIGÉ)
    private void openColorPicker(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setHint("#FF00FF");

        builder.setTitle("Enter HEX Color");
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {

            try{
                int color = Color.parseColor(input.getText().toString());
                drawingView.setColor(color); // ✅ respecte le mode
            }
            catch(Exception e){
                Toast.makeText(this,"Invalid HEX",Toast.LENGTH_SHORT).show();
            }

        });

        builder.setNegativeButton("Cancel",null);

        builder.show();
    }
}