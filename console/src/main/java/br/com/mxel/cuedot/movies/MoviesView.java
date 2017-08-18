package br.com.mxel.cuedot.movies;

import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;

import br.com.mxel.cuedot.ConsoleComponent;
import br.com.mxel.cuedot.data.model.Movie;
import br.com.mxel.cuedot.util.CueDotConstants;

/**
 * Created by michelribeiro on 03/08/17.
 */

public class MoviesView implements IMoviesView {

    @Inject
    MoviesPresenter presenter;

    @Inject
    Scanner scanner;

    private boolean _isRunning = false;
    private boolean _canLoadMore = true;

    public MoviesView(ConsoleComponent consoleComponent) {

        DaggerMoviesComponent.builder()
                .consoleComponent(consoleComponent)
                .moviesModule(new MoviesModule(this))
                .build()
                .inject(this);

        presenter.getMoviesOrderedBy(CueDotConstants.ORDER_BY_POPULAR);
    }

    @Override
    public void showLoading() {
        System.out.println("Loading...\n");
    }

    @Override
    public void hideLoading() {
        System.out.println("Loading complete\n");
    }

    @Override
    public void showError(Throwable throwable) {
        System.out.println("Cassildis Error: " + throwable.getMessage());
    }

    @Override
    public void showMoviesList(List<Movie> movies) {

        int count = 0;
        for (Movie m : movies) {
            count++;
            System.out.println(String.valueOf(count) + ": " + m.title);
        }
    }

    @Override
    public void setEnableLoadMore(boolean enabled) {
        _canLoadMore = enabled;
    }

    @Override
    public void hideError() {

    }

    public String promptUser() {

        String options = "\nPlease choose an option\n";
        options += "Enter a number to see the corresponding movie detail\n";
        if(_canLoadMore) {
            options += "Press 'm' to load more movies\n";
        }
        options += "Press 'q' to exit\n";

        System.out.println(options);

        String query = scanner.nextLine();
        if (query.equalsIgnoreCase("q")) {
            _isRunning = true;
        } else if(query.equalsIgnoreCase("m") && _canLoadMore) {
            System.out.println("Loading more...\n");
            presenter.loadMore();
        }

        return query;
    }

    // ================= Getters / Setters =====================

    public boolean getIsRunning() {
        return _isRunning;
    }
}