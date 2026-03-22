package mg.arovy.whiteboard.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import mg.arovy.whiteboard.views.DrawingView;

public class DrawingExporter {

    // On reçoit le contexte et la vue — on ne les stocke pas en champ,
    // pour envoyer le dessin sous forme png qd on clique sur share
    public static void share(Context context, DrawingView drawingView) {
        try {
            Bitmap bitmap = drawingView.getBitmap();

            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs();

            File file = new File(cachePath, "drawing.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            Uri uri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    file
            );

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(Intent.createChooser(intent, "Partager"));

        } catch (Exception e) {
            Toast.makeText(context, "Erreur partage", Toast.LENGTH_SHORT).show();
        }
    }
}