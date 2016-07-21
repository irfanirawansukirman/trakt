package design.ivan.app.trakt.model;

/**
 * Created by ivanm on 7/21/16.
 */
public class SearchResult {
    String type;
    float score;
    Movie movie;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
