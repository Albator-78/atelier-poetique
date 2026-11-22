import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

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

    // Générateur pseudo-aléatoire autonome (sans java.util.Random)
    private static long graine = System.currentTimeMillis();

    private static int aleatoire(int max) {
        graine = (graine * 6364136223846793005L + 1442695040888963407L);
        int valeur = (int) (graine >>> 33);
        return (valeur < 0 ? -valeur : valeur) % max;
    }

    public static void main(String[] args) {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement bouton = document.getElementById("btn-generer");

        afficherHaiku(document);

        bouton.addEventListener("click", evt -> afficherHaiku(document));
    }

    private static void afficherHaiku(HTMLDocument document) {
        String[] haiku = genererHaiku();

        HTMLElement poemeEl = document.getElementById("poeme");
        poemeEl.setInnerHTML(haiku[0] + "<br>" + haiku[1] + "<br>" + haiku[2]);

        StringBuilder analyseHtml = new StringBuilder("<ul>");
        for (int i = 0; i < haiku.length; i++) {
            int syllabes = compterSyllabes(haiku[i]);
            analyseHtml.append("<li>Vers ").append(i + 1)
                       .append(" : « ").append(haiku[i]).append(" » — <strong>")
                       .append(syllabes).append(" syllabes</strong></li>");
        }
        analyseHtml.append("</ul>");

        boolean rime = rimentEnsemble(haiku[0], haiku[2]);
        analyseHtml.append("<p>Rime embrassée (Vers 1 & 3) : <strong>")
                   .append(rime ? "Oui" : "Non (vers libres)")
                   .append("</strong></p>");

        document.getElementById("analyse").setInnerHTML(analyseHtml.toString());
    }

    public static String[] genererHaiku() {
        String v1 = VERS_5_SYLLABES[aleatoire(VERS_5_SYLLABES.length)];
        String v2 = VERS_7_SYLLABES[aleatoire(VERS_7_SYLLABES.length)];
        String v3;
        do {
            v3 = VERS_5_SYLLABES[aleatoire(VERS_5_SYLLABES.length)];
        } while (v3.equals(v1));

        return new String[]{v1, v2, v3};
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
