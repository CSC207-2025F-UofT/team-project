package entity;

import java.util.ArrayList;
import java.util.List;

public class Paper {

    private final String title;
    private final List<String> authors;
    private final String url;
    private final List<String> genes;

    public Paper(String title, List<String> authors,
                 String url, List<String> genes) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (authors == null || genes == null) {
            throw new IllegalArgumentException("Lists cannot be null");
        }

        this.title = title;
        this.authors = new ArrayList<>(authors);
        this.url = url;
        this.genes = new ArrayList<>();

        for (String gene : genes) {
            addGene(gene);
        }
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return new ArrayList<>(authors);
    }

    public String getUrl() {
        return url;
    }

    public List<String> getGenes() {
        return new ArrayList<>(genes);
    }

    public void addGene(String gene) {
        if (gene == null || gene.trim().isEmpty()) {
            throw new IllegalArgumentException("Gene tag cannot be empty");
        }

        if (!genes.contains(gene)) {
            genes.add(gene);
        }
    }

    public void removeGene(String gene) {
        genes.remove(gene);
    }
}