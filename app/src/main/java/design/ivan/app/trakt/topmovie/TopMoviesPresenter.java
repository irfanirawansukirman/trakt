package design.ivan.app.trakt.topmovie;

import android.app.Activity;


public class TopMoviesPresenter implements ITopMoviesContract.ActionListener {

    ITopMoviesContract.TopMoviesView topMoviesView;

    public TopMoviesPresenter(ITopMoviesContract.TopMoviesView topMoviesView) {
        this.topMoviesView = topMoviesView;
    }

    @Override
    public void setupListeners(Activity main) {

    }

    @Override
    public void clearListeners(Activity main) {

    }
}
