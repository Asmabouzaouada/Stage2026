package tn.esprit.smartrhback.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CvParsingService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("(\\+?\\d{1,3}[\\s.-]?)?(\\d{2}[\\s.-]?){4,5}");

    public String extractText(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null) return "";

        if (filename.toLowerCase().endsWith(".pdf")) {
            return extractFromPdf(file.getInputStream());
        } else if (filename.toLowerCase().endsWith(".docx")) {
            return extractFromDocx(file.getInputStream());
        }
        throw new IllegalArgumentException("Format de fichier non supporté (PDF ou DOCX uniquement).");
    }

    private String extractFromPdf(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractFromDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }

    public String extractEmail(String texte) {
        Matcher matcher = EMAIL_PATTERN.matcher(texte);
        return matcher.find() ? matcher.group() : null;
    }

    public String extractTelephone(String texte) {
        Matcher matcher = PHONE_PATTERN.matcher(texte);
        // On filtre les correspondances trop courtes (faux positifs)
        while (matcher.find()) {
            String candidat = matcher.group().replaceAll("[\\s.-]", "");
            if (candidat.length() >= 8) {
                return matcher.group().trim();
            }
        }
        return null;
    }

    public String extractNom(String texte, String nomFichier) {
        // Heuristique simple : on prend les 2-3 premiers mots significatifs des premières lignes,
        // à défaut on se base sur le nom du fichier (souvent "Nom_Prenom_CV.pdf")
        String[] lignes = texte.split("\\r?\\n");
        for (String ligne : lignes) {
            String l = ligne.trim();
            if (l.length() > 3 && l.length() < 50 && l.matches("^[A-Za-zÀ-ÿ\\s-]+$")) {
                return l;
            }
        }
        return nomFichier != null ? nomFichier.replaceAll("\\.(pdf|docx)$", "").replaceAll("[_-]", " ") : "Candidat inconnu";
    }

    public String extractSection(String texte, String... motsClesSection) {
        String texteMinuscule = texte.toLowerCase();
        for (String motCle : motsClesSection) {
            int index = texteMinuscule.indexOf(motCle.toLowerCase());
            if (index != -1) {
                int fin = Math.min(index + 800, texte.length());
                return texte.substring(index, fin).trim();
            }
        }
        return null;
    }
}