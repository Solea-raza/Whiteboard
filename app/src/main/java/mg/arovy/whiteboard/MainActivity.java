package mg.arovy.whiteboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

import mg.arovy.whiteboard.data.Figure;
import mg.arovy.whiteboard.data.FigureRect;
import mg.arovy.whiteboard.data.FigureOval;
import mg.arovy.whiteboard.factory.LineFactory;
import mg.arovy.whiteboard.factory.RectFactory;
import mg.arovy.whiteboard.factory.OvalFactory;
import mg.arovy.whiteboard.views.DrawingView;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addDrawingView();
        setupButtons();
        setupShare();

        // ✅ brancher le listener ici
        drawingView.setOnFigureSelectedListener(
                (figure, x, y) -> showFloatingMenu(figure, x, y)
        );
    }

    private void addDrawingView(){
        drawingView = new DrawingView(this);
        ViewGroup container = findViewById(R.id.drawing_container);
        container.addView(drawingView,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        drawingView.setFigureFactory(new LineFactory());
    }

    private void setupButtons(){
        findViewById(R.id.btn_line).setOnClickListener(v -> drawingView.setFigureFactory(new LineFactory()));
        findViewById(R.id.btn_rect).setOnClickListener(v -> drawingView.setFigureFactory(new RectFactory()));
        findViewById(R.id.btn_oval).setOnClickListener(v -> drawingView.setFigureFactory(new OvalFactory()));
    }

    private void setupShare(){
        findViewById(R.id.btn_share).setOnClickListener(v -> shareDrawing());
    }

    // ✅ Une seule version, la bonne
    private void showFloatingMenu(Figure figure, float x, float y) {

        View view = getLayoutInflater().inflate(R.layout.menu_floating, null);

        PopupWindow popup = new PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popup.showAtLocation(drawingView, Gravity.NO_GRAVITY, (int) x, (int) y - 150);

        // btn_fill_color visible seulement pour Rect et Oval
        ImageButton btnFillColor = view.findViewById(R.id.btn_fill_color);
        boolean hasFill = (figure instanceof FigureRect || figure instanceof FigureOval);
        btnFillColor.setVisibility(hasFill ? View.VISIBLE : View.GONE);

        // 1. Couleur de contour
        view.findViewById(R.id.btn_color).setOnClickListener(v -> {
            openColorPickerForFigure(figure, false);
            popup.dismiss();
        });

        // 2. Couleur de fond
        btnFillColor.setOnClickListener(v -> {
            openColorPickerForFigure(figure, true);
            popup.dismiss();
        });

        // 3. Épaisseur
        view.findViewById(R.id.btn_stroke).setOnClickListener(v -> {
            showStrokeDialog(figure);
            popup.dismiss();
        });

        // 4. Supprimer
        view.findViewById(R.id.btn_delete).setOnClickListener(v -> {
            drawingView.removeFigure(figure);
            popup.dismiss();
        });
    }

    private void showStrokeDialog(Figure figure){
        SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(50);
        new AlertDialog.Builder(this)
                .setTitle("Epaisseur")
                .setView(seekBar)
                .setPositiveButton("OK",(d,w)->{
                    figure.getStrokePaint().setStrokeWidth(seekBar.getProgress());
                    drawingView.invalidate();
                })
                .show();
    }

    private void openColorPickerForFigure(Figure figure, boolean isFill) {
        // Couleurs prédéfinies
        int[] presetColors = {
                Color.BLACK,
                Color.RED,
                Color.BLUE,
                Color.GREEN,
                Color.YELLOW
        };

        // Layout principal vertical
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 24, 32, 8);

        // Ligne de pastilles
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER);

        int dp48 = (int)(48 * getResources().getDisplayMetrics().density);
        int dp8  = (int)(8  * getResources().getDisplayMetrics().density);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Référence pour fermer le dialog depuis le listener HSV
        final AlertDialog[] dialogRef = {null};

        for (int color : presetColors) {
            View circle = new View(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp48, dp48);
            lp.setMargins(dp8, 0, dp8, 0);
            circle.setLayoutParams(lp);

            // Cercle coloré
            android.graphics.drawable.GradientDrawable shape =
                    new android.graphics.drawable.GradientDrawable();
            shape.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            shape.setColor(color);
            shape.setStroke(2, Color.GRAY);
            circle.setBackground(shape);

            final int c = color;
            circle.setOnClickListener(v -> {
                applyColor(figure, isFill, c);
                if (dialogRef[0] != null) dialogRef[0].dismiss();
            });

            row.addView(circle);
        }

        // Bouton "+"
        Button btnMore = new Button(this);
        btnMore.setText("+");
        btnMore.setTextSize(20);
        LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(dp48, dp48);
        lpBtn.setMargins(dp8, 0, dp8, 0);
        btnMore.setLayoutParams(lpBtn);
        btnMore.setPadding(0, 0, 0, 0);

        btnMore.setOnClickListener(v -> {
            if (dialogRef[0] != null) dialogRef[0].dismiss();
            openHsvPicker(figure, isFill);
        });

        row.addView(btnMore);
        root.addView(row);

        builder.setTitle(isFill ? "Couleur de fond" : "Couleur de contour");
        builder.setView(root);
        builder.setNegativeButton("Annuler", null);

        dialogRef[0] = builder.create();
        dialogRef[0].show();
    }

    private void applyColor(Figure figure, boolean isFill, int color) {
        if (isFill) {
            figure.getFillPaint().setColor(color);
        } else {
            figure.getStrokePaint().setColor(color);
        }
        drawingView.invalidate();
    }

    private void openHsvPicker(Figure figure, boolean isFill) {
        int initialColor = isFill
                ? figure.getFillPaint().getColor()
                : figure.getStrokePaint().getColor();

        new com.skydoves.colorpickerview.ColorPickerDialog.Builder(this)
                .setTitle(isFill ? "Couleur de fond" : "Couleur de contour")
                .setPreferenceName("colorPicker")
                .setPositiveButton("OK",
                        (com.skydoves.colorpickerview.listeners.ColorEnvelopeListener)
                                (envelope, fromUser) -> applyColor(figure, isFill, envelope.getColor()))
                .setNegativeButton("Annuler", (d, w) -> d.dismiss())
                .show();
    }

    private void shareDrawing(){
        try{
            Bitmap bitmap = drawingView.getBitmap();
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            File file = new File(cachePath, "drawing.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            Uri uri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    file
            );
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Partager"));
        }catch(Exception e){
            Toast.makeText(this,"Erreur partage",Toast.LENGTH_SHORT).show();
        }
    }
}