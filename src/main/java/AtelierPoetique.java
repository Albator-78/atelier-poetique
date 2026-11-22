import org.teavm.interop.Export;

public class AtelierPoetique {

    // Position 1 : Passé / Origine (5 syllabes)
    private static final String[] VERS_POS_1 = {
        "Le sac sur l'épaule,",
        "Sur la table neuve,",
        "Sous le voile sombre,",
        "Graine en plein sillon,",
        "Pierre sur le roc,",
        "Entre deux piliers,",
        "Au croisement nu,",
        "Deux sphinx attelés,",
        "Plateaux de métal,",
        "Pas lents dans la nuit,",
        "Le cercle a tourné,",
        "Le feu maîtrisé,",
        "Liens noués au bois,",
        "Épis moissonnés,",
        "Deux coupes d'argent,",
        "Chaînes au brasier,",
        "Tour dressée trop haut,",
        "Cruches d'eau versées,",
        "Reflets sur l'étang,",
        "L'ombre derrière soi,",
        "Appel sous la dalle,",
        "Quatre coins du ciel,"
    };

    // Position 2 : Présent / Action (7 syllabes)
    private static final String[] VERS_POS_2 = {
        "Au bord du gouffre sans nom,",
        "La main choisit ses outils,",
        "Le livre scellé d'argent,",
        "L'étoile au front souverain,",
        "L'équerre trace la loi,",
        "Le souffle unit les esprits,",
        "Le cœur hésite et frémit,",
        "Les rênes fermes en main,",
        "Le glaive tranche le doute,",
        "La lampe sous le manteau,",
        "La main saisit le pivot,",
        "Douceur sur la gueule ouverte,",
        "Le ciel regarde à l'envers,",
        "L'acier dénude le sol,",
        "L'eau pure passe sans bruit,",
        "Les torches dansent la nuit,",
        "L'éclair frappe le donjon,",
        "À genoux sous l'astre clair,",
        "Deux tours veillent dans le bleu,",
        "Le grand jour chauffe la pierre,",
        "L'éclat d'or fend le silence,",
        "Danse au cœur de la couronne,"
    };

    // Position 3 : Futur / Perspective (5 syllabes)
    private static final String[] VERS_POS_3 = {
        "Le vent libre appelle.",
        "L'œuvre prend racine.",
        "L'ombre se dévoile.",
        "Le verbe fleurit.",
        "L'édifice tient.",
        "Le lien s'accomplit.",
        "La voie se décide.",
        "Le char passe en roi.",
        "Tout reprend son poids.",
        "Le sentier s'éclaire.",
        "Le sort se renverse.",
        "Le calme triomphe.",
        "L'être lâche prise.",
        "Un germe renaît.",
        "La fièvre s'apaise.",
        "Le pacte se brise.",
        "Les murs s'ouvrent nus.",
        "La terre reverdit.",
        "Le songe s'éveille.",
        "La joie rayonne.",
        "L'âme se relève.",
        "Le cycle est entier."
    };

    private static final String VOYELLES = "aeiouyàâäéèêëîïôöùûüÿ";
    private static long graine = System.currentTimeMillis();

    // Indices des 3 lames tirées (0 à 21)
    private static int lame1 = 0;
    private static int lame2 = 1;
    private static int lame3 = 2;

    public static void main(String[] args) {
        tirerTriade();
    }

    private static int aleatoire(int max) {
        graine = (graine * 6364136223846793005L + 1442695040888963407L);
        int valeur = (int) (graine >>> 33);
        return (valeur < 0 ? -valeur : valeur) % max;
    }

    @Export(name = "tirerTriade")
    public static void tirerTriade() {
        int total = VERS_POS_1.length; // 22 arcanes majeurs
        lame1 = aleatoire(total);
        do {
            lame2 = aleatoire(total);
        } while (lame2 == lame1);
        do {
            lame3 = aleatoire(total);
        } while (lame3 == lame1 || lame3 == lame2);
    }

    @Export(name = "getLame")
    public static int getLame(int position) {
        if (position == 1) return lame1;
        if (position == 2) return lame2;
        return lame3;
    }

    @Export(name = "getSyllabesVers")
    public static int getSyllabesVers(int position) {
        if (position == 1) return compterSyllabes(VERS_POS_1[lame1]);
        if (position == 2) return compterSyllabes(VERS_POS_2[lame2]);
        return compterSyllabes(VERS_POS_3[lame3]);
    }

    @Export(name = "aRimeEmbrassee")
    public static int aRimeEmbrassee() {
        return rimentEnsemble(VERS_POS_1[lame1], VERS_POS_3[lame3]) ? 1 : 0;
    }

    public static int compterSyllabes(String vers) {
        String texte = vers.toLowerCase().trim()
                           .replaceAll("[^a-zàâäéèêëîïôöùûüÿ\\s'-]", "")
                           .replace("'", " ")
                           .replace("-", " ");
        String[] mots = texte.split("\\s+");
        int totalSyllabes = 0;

        for (int m = 0; m < mots.length; m++) {
            String mot = mots[m];
            if (mot.isEmpty()) continue;

            int syllabesMot = 0;
            boolean dansVoyelles = false;
            for (int i = 0; i < mot.length(); i++) {
                char c = mot.charAt(i);
                if (VOYELLES.indexOf(c) >= 0) {
                    if (!dansVoyelles) {
                        syllabesMot++;
                        dansVoyelles = true;
                    }
                } else {
                    dansVoyelles = false;
                }
            }

            // Élision du 'e' final devant voyelle suivante ou en fin de vers
            boolean finDeVers = (m == mots.length - 1);
            boolean suiviDeVoyelle = false;
            if (!finDeVers && !mots[m + 1].isEmpty()) {
                char initialeSuivante = mots[m + 1].charAt(0);
                suiviDeVoyelle = (VOYELLES.indexOf(initialeSuivante) >= 0);
            }

            if (syllabesMot > 1 && (finDeVers || suiviDeVoyelle)) {
                if (mot.endsWith("e") || mot.endsWith("es") || (finDeVers && mot.endsWith("ent"))) {
                    syllabesMot--;
                }
            }
            totalSyllabes += Math.max(1, syllabesMot);
        }
        return Math.max(1, totalSyllabes);
    }

    public static boolean rimentEnsemble(String vers1, String vers2) {
        String fin1 = nettoyerFinDeVers(vers1);
        String fin2 = nettoyerFinDeVers(vers2);
        if (fin1.length() < 2 || fin2.length() < 2) {
            return false;
        }
        return fin1.substring(fin1.length() - 2).equals(fin2.substring(fin2.length() - 2));
    }

    private static String nettoyerFinDeVers(String vers) {
        String s = vers.toLowerCase();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= 'a' && c <= 'z') || VOYELLES.indexOf(c) >= 0) {
                sb.append(c);
            }
        }
        String propre = sb.toString();
        if (propre.endsWith("e")) {
            propre = propre.substring(0, propre.length() - 1);
        }
        return propre;
    }
}
