package mg.arovy.whiteboard.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import mg.arovy.whiteboard.R;
import mg.arovy.whiteboard.data.Figure;
import mg.arovy.whiteboard.data.FigureOval;
import mg.arovy.whiteboard.data.FigureRect;

public class FigureMenuManager {

    // déclenche la méthode dans main avec l'interface menuActionListener
    public interface MenuActionListener {
        void onFigureDeleted(Figure figure);
        void onDrawingChanged();
    }

    private final Context context;
    private final MenuActionListener listener;

    // On injecte les dépendances dans le constructeur
    public FigureMenuManager(Context context, MenuActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    // affiche le menu flottant en creant un popup et en inflate la vue
    public void showFloatingMenu(Figure figure, float x, float y, View anchorView) {
        View view = inflateMenuView();
        PopupWindow popup = createPopup(view, anchorView, x, y);
        bindMenuButtons(view, popup, figure);
    }
    // permet au menu de 'flotter' en dessus de la forme
    private View inflateMenuView() {
        return android.view.LayoutInflater.from(context)
                .inflate(R.layout.menu_floating, null);
    }
    // créer et positionner le popup
    private PopupWindow createPopup(View view, View anchorView, float x, float y) {
        PopupWindow popup = new PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        popup.showAtLocation(anchorView, Gravity.NO_GRAVITY, (int) x, (int) y - 150);
        return popup;
    }
    // rassemble les boutons du menu
    private void bindMenuButtons(View view, PopupWindow popup, Figure figure) {
        bindFillColorButton(view, popup, figure);
        bindStrokeColorButton(view, popup, figure);
        bindStrokeWidthButton(view, popup, figure);
        bindDeleteButton(view, popup, figure);
    }
    // btn_fill_color visible seulement pour Rect et Oval
    private void bindFillColorButton(View view, PopupWindow popup, Figure figure) {
        ImageButton btn = view.findViewById(R.id.btn_fill_color);
        boolean hasFill = (figure instanceof FigureRect || figure instanceof FigureOval);
        btn.setVisibility(hasFill ? View.VISIBLE : View.GONE);
        btn.setOnClickListener(v -> {
            ColorPickerHelper.openColorPicker(context, figure, true, listener::onDrawingChanged);
            popup.dismiss();
        });
    }

    // contour
    private void bindStrokeColorButton(View view, PopupWindow popup, Figure figure) {
        view.findViewById(R.id.btn_color).setOnClickListener(v -> {
            ColorPickerHelper.openColorPicker(context, figure, false, listener::onDrawingChanged);
            popup.dismiss();
        });
    }

    // épaisseur
    private void bindStrokeWidthButton(View view, PopupWindow popup, Figure figure) {
        view.findViewById(R.id.btn_stroke).setOnClickListener(v -> {
            showStrokeDialog(figure);
            popup.dismiss();
        });
    }
    //gomme
    private void bindDeleteButton(View view, PopupWindow popup, Figure figure) {
        view.findViewById(R.id.btn_delete).setOnClickListener(v -> {
            listener.onFigureDeleted(figure);
            popup.dismiss();
        });
    }
    // affiche un dialogue avec un curseur pour régler l'épaisseur
    private void showStrokeDialog(Figure figure) {
        SeekBar seekBar = new SeekBar(context);
        seekBar.setMax(50);
        new AlertDialog.Builder(context)
                .setTitle("Epaisseur")
                .setView(seekBar)
                .setPositiveButton("OK", (d, w) -> {
                    figure.getStrokePaint().setStrokeWidth(seekBar.getProgress());
                    listener.onDrawingChanged();
                })
                .show();
    }
}