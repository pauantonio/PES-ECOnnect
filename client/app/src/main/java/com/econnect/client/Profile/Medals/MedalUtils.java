package com.econnect.client.Profile.Medals;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.econnect.client.R;
import com.econnect.client.StartupActivity;

public class MedalUtils {

    private MedalUtils() {}

    private enum Medals {
        None(0, R.string.medal_none, R.drawable.ic_medal_24),
        EficienciaA(1, R.string.medal_eficiencia_a, R.drawable.medal_eficiencia_a),
        EficienciaB(2, R.string.medal_eficiencia_b, R.drawable.medal_eficiencia_b),
        EficienciaC(3, R.string.medal_eficiencia_c, R.drawable.medal_eficiencia_c),
        EficienciaD(4, R.string.medal_eficiencia_d, R.drawable.medal_eficiencia_d),
        EficienciaE(5, R.string.medal_eficiencia_e, R.drawable.medal_eficiencia_e),
        EficienciaF(6, R.string.medal_eficiencia_f, R.drawable.medal_eficiencia_f),
        EficienciaG(7, R.string.medal_eficiencia_g, R.drawable.medal_eficiencia_g),
        EmpresaOr(8, R.string.medal_empresa_or, R.drawable.medal_empresa_or),
        EmpresaPlata(9, R.string.medal_empresa_plata, R.drawable.medal_empresa_plata),
        EmpresaBronze(10, R.string.medal_empresa_bronze, R.drawable.medal_empresa_bronze),
        ProducteOr(11, R.string.medal_producte_or, R.drawable.medal_producte_or),
        ProductePlata(12, R.string.medal_producte_plata, R.drawable.medal_producte_plata),
        ProducteBronze(13, R.string.medal_producte_bronze, R.drawable.medal_producte_bronze),
        ForumOr(14, R.string.medal_forum_or, R.drawable.medal_forum_or),
        ForumPlata(15, R.string.medal_forum_plata, R.drawable.medal_forum_plata),
        ForumBronze(16, R.string.medal_forum_bronze, R.drawable.medal_forum_bronze),
        LikeOr(17, R.string.medal_like_or, R.drawable.medal_like_or),
        LikePlata(18, R.string.medal_like_plata, R.drawable.medal_like_plata),
        LikeBronze(19, R.string.medal_like_bronze, R.drawable.medal_like_bronze),
        PreguntaOr(20, R.string.medal_pregunta_or, R.drawable.medal_pregunta_or),
        PreguntaPlata(21, R.string.medal_pregunta_plata, R.drawable.medal_pregunta_plata),
        PreguntaBronze(22, R.string.medal_pregunta_bronze, R.drawable.medal_pregunta_bronze);

        private final int id;
        private final int nameResourceId;
        private final int drawableResourceId;

        Medals(int id, int nameResourceId, int drawableResourceId) {
            this.id = id;
            this.nameResourceId = nameResourceId;
            this.drawableResourceId = drawableResourceId;
        }
        public int getId() {
            return id;
        }
        public static Medals getMedal(int id) {
            for (Medals medal : Medals.values()) {
                if (medal.getId() == id) return medal;
            }
            return null;
        }
    }

    public static String medalName(int id) {
        Medals medal = Medals.getMedal(id);
        assert medal != null;
        int stringId = medal.nameResourceId;
        return StartupActivity.globalContext().getString(stringId);
    }

    public static Drawable medalIcon(int id) {
        Medals medal = Medals.getMedal(id);
        assert medal != null;
        int drawableId = medal.drawableResourceId;
        return ResourcesCompat.getDrawable(
                StartupActivity.globalContext().getResources(),
                drawableId,
                StartupActivity.globalContext().getTheme()
        );
    }

    public static int getNumMedals() {
        // Return the number of existing medals, including the null medal
        return Medals.values().length;
    }
}
