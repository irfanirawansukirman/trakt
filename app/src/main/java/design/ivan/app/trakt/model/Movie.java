package design.ivan.app.trakt.model;
public class Movie {
    String title;
    Integer year;
    Ids ids;
    String overview;
    Images images;

    public Movie() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Ids getIds() {
        return ids;
    }

    public void setIds(Ids ids) {
        this.ids = ids;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }
}
