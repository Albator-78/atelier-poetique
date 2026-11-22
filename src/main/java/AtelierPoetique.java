import org.teavm.interop.Export;

public class AtelierPoetique {

    private static final String[] VERS_5_SYLLABES = {
        "Le vent d'automne",
        "Sous la lune claire",
        "Une feuille tombe",
        "L'eau calme du lac",
        "Dans le silence"
    };

    private static final String[] VERS_7_SYLLABES = {
        "Murmure à travers les bois",
        "Les ombres dansent sans bruit",
        "Un papillon bleu s'envole",
        "La pluie caresse la terre",
        "L'étoile brille dans la nuit"
    };

    private static final String VOYELLES = "aeiouyàâäéèêëîïôöùûüÿ";
    private static long graine = System.currentTimeMillis();

    private static int idx1 = 0;
    private static int idx2 = 0;
    private static int idx3 = 1;

    public static void main(String[] args) {
        nouveauHaiku();
    }

    private static int aleatoire(int max) {
        graine = (graine * 6364136223846793005L + 1442695040888963407L);
        int valeur = (int) (graine >>> 33);
        return (valeur < 0 ? -valeur : valeur) % max;
    }

    @Export(name = "nouveauHaiku")
    public static void nouveauHaiku() {
        idx1 = aleatoire(VERS_5_SYLLABES.length);
        idx2 = aleatoire(VERS_7_SYLLABES.length);
        do {
            idx3 = aleatoire(VERS_5_SYLLABES.length);
        } while (idx3 == idx1);
    }

    @Export(name = "getSyllabesVers")
    public static int getSyllabesVers(int numeroVers) {
        String vers = getTexteVers(numeroVers);
        return compterSyllabes(vers);
    }

    @Export(name = "getIndexVers")
    public static int getIndexVers(int numeroVers) {
        if (numeroVers == 1) return idx1;
        if (numeroVers == 2) return idx2;
        return idx3;
    }

    @Export(name = "aRimeEmbrassee")
    public static int aRimeEmbrassee() {
        return rimentEnsemble(VERS_5_SYLLABES[idx1], VERS_5_SYLLABES[idx3]) ? 1 : 0;
    }

    private static String getTexteVers(int numeroVers) {
        if (numeroVers == 1) return VERS_5_SYLLABES[idx1];
        if (numeroVers == 2) return VERS_7_SYLLABES[idx2];
        return VERS_5_SYLLABES[idx3];
    }

    public static int compterSyllabes(String vers) {
        String texte = vers.toLowerCase().trim();
        int compteur = 0;
        boolean dansGroupeVoyelles = false;

        for (int i = 0; i < texte.length(); i++) {
            char c = texte.charAt(i);
            if (VOYELLES.indexOf(c) >= 0) {
                if (!dansGroupeVoyelles) {
                    compteur++;
                    dansGroupeVoyelles = true;
                }
            } else {
                dansGroupeVoyelles = false;
            }
        }

        if (texte.endsWith("e") || texte.endsWith("es") || texte.endsWith("ent")) {
            compteur--;
        }
        return Math.max(1, compteur);
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
