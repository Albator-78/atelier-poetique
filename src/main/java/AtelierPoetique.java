import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.events.EventListener;

public class AtelierPoetique {

    private static final List<String> VERS_5_SYLLABES = List.of(
        "Le vent d'automne",
        "Sous la lune claire",
        "Une feuille tombe",
        "L'eau calme du lac",
        "Dans le silence"
    );

    private static final List<String> VERS_7_SYLLABES = List.of(
        "Murmure à travers les bois",
        "Les ombres dansent sans bruit",
        "Un papillon bleu s'envole",
        "La pluie caresse la terre",
        "L'étoile brille dans la nuit"
    );

    public static void main(String[] args) {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement bouton = document.getElementById("btn-generer");

        // Affichage initial au chargement
        afficherHaiku(document);

        // Liaison du clic bouton en Java natif compilé en Wasm
        bouton.addEventListener("click", (EventListener<?>) evt -> afficherHaiku(document));
    }

    private static void afficherHaiku(HTMLDocument document) {
        String[] haiku = genererHaiku();

        // Injection du poème dans le HTML
        HTMLElement poemeEl = document.getElementById("poeme");
        poemeEl.setInnerHTML(String.join("<br>", haiku));

        // Injection de l'analyse métrique et sonore
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
        Random rand = new Random();
        String v1 = VERS_5_SYLLABES.get(rand.nextInt(VERS_5_SYLLABES.size()));
        String v2 = VERS_7_SYLLABES.get(rand.nextInt(VERS_7_SYLLABES.size()));
        String v3;
        do {
            v3 = VERS_5_SYLLABES.get(rand.nextInt(VERS_5_SYLLABES.size()));
        } while (v3.equals(v1));

        return new String[]{v1, v2, v3};
    }

    public static int compterSyllabes(String vers) {
        String texte = vers.toLowerCase().trim();
        Pattern groupeVoyelles = Pattern.compile("[aeiouyàâäéèêëîïôöùûüÿ]+");
        Matcher matcher = groupeVoyelles.matcher(texte);

        int compteur = 0;
        while (matcher.find()) {
            compteur++;
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
        return vers.toLowerCase()
                   .replaceAll("[^a-zàâäéèêëîïôöùûüÿ]", "")
                   .replaceAll("e$", "");
    }
}
