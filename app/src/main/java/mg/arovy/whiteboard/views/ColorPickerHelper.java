package mg.arovy.whiteboard.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import mg.arovy.whiteboard.data.Figure;

public class ColorPickerHelper {

    // L'interface permet à l'appelant d'être notifié sans que
    // ColorPickerHelper connaisse DrawingView ou MainActivity
    public interface OnColorApplied {
        void onApplied();
    }

    // assemble et affiche le dialogue
    public static void openColorPicker(
            Context context, Figure figure, boolean isFill, OnColorApplied callback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog[] dialogRef = {null};

        LinearLayout root = buildColorPickerLayout(context, figure, isFill, callback, dialogRef);

        builder.setTitle(isFill ? "Couleur de fond" : "Couleur de contour");
        builder.setView(root);
        builder.setNegativeButton("Annuler", null);

        dialogRef[0] = builder.create();
        dialogRef[0].show();
    }

    // Méthode privée que seul cette classe peut utiliser
    private static void applyColor(Figure figure, boolean isFill, int color) {
        Paint paint = isFill ? figure.getFillPaint() : figure.getStrokePaint();
        paint.setColor(color);
    }
    // construire le layout racine
    private static LinearLayout buildColorPickerLayout(
            Context context, Figure figure, boolean isFill,
            OnColorApplied callback, AlertDialog[] dialogRef) {

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 24, 32, 8);

        LinearLayout row = buildColorRow(context, figure, isFill, callback, dialogRef);
        root.addView(row);

        return root;
    }
    // construire la rangée de paastille
    private static LinearLayout buildColorRow(
            Context context, Figure figure, boolean isFill,
            OnColorApplied callback, AlertDialog[] dialogRef) {

        int[] presetColors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
        int dp48 = (int)(48 * context.getResources().getDisplayMetrics().density);
        int dp8  = (int)(8  * context.getResources().getDisplayMetrics().density);

        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER);

        for (int color : presetColors) {
            row.addView(buildColorCircle(context, color, dp48, dp8, figure, isFill, callback, dialogRef));
        }
        row.addView(buildMoreButton(context, dp48, dp8, figure, isFill, callback, dialogRef));

        return row;
    }
    // construit le cercle avec les couleurs qu'on peut personnaliser
    private static View buildColorCircle(
            Context context, int color, int dp48, int dp8,
            Figure figure, boolean isFill, OnColorApplied callback, AlertDialog[] dialogRef) {

        View circle = new View(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp48, dp48);
        lp.setMargins(dp8, 0, dp8, 0);
        circle.setLayoutParams(lp);

        // cercle coloré
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(color);
        shape.setStroke(2, Color.GRAY);
        circle.setBackground(shape);

        circle.setOnClickListener(v -> {
            applyColor(figure, isFill, color);
            callback.onApplied();
            if (dialogRef[0] != null) dialogRef[0].dismiss();
        });

        return circle;
    }
    // construire le bouton "+"
    private static Button buildMoreButton(
            Context context, int dp48, int dp8,
            Figure figure, boolean isFill, OnColorApplied callback, AlertDialog[] dialogRef) {

        Button btn = new Button(context);
        btn.setText("+");
        btn.setTextSize(20);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp48, dp48);
        lp.setMargins(dp8, 0, dp8, 0);
        btn.setLayoutParams(lp);

        btn.setOnClickListener(v -> {
            if (dialogRef[0] != null) dialogRef[0].dismiss();
            openHsvPicker(context, figure, isFill, callback);
        });

        return btn;
    }
    // permet de choisir une couleur perso dans le cercle
    private static void openHsvPicker(
            Context context,
            Figure figure,
            boolean isFill,
            OnColorApplied callback
    ) {
        new com.skydoves.colorpickerview.ColorPickerDialog.Builder(context)
                .setTitle(isFill ? "Couleur de fond" : "Couleur de contour")
                .setPreferenceName("colorPicker")
                .setPositiveButton("OK",
                        (com.skydoves.colorpickerview.listeners.ColorEnvelopeListener)
                                (envelope, fromUser) -> {
                                    applyColor(figure, isFill, envelope.getColor());
                                    callback.onApplied();
                                })
                .setNegativeButton("Annuler", (d, w) -> d.dismiss())
                .show();
    }
}
