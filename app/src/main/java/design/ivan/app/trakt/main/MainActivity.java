package design.ivan.app.trakt.main;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import design.ivan.app.trakt.R;
import design.ivan.app.trakt.model.Movie;
import design.ivan.app.trakt.search.SearchFragment;
import design.ivan.app.trakt.topmovie.TopMoviesFragment;

public class MainActivity extends AppCompatActivity implements IMainContract.MainView{
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private static final String TAG = "MainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private IMainContract.ActionListener actionListener;
    private BottomSheetBehavior mBottomSheetBehavior;
    @BindView(R.id.container)
    ViewPager mViewPager;
    Snackbar snackbar;
    @BindView(R.id.main_content)
    CoordinatorLayout root;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.frame_bottom_sheet)
    FrameLayout frameBottomSheet;
    @BindView(R.id.sheet_item_thumb)
    ImageView thumb;
    @BindView(R.id.sheet_item_overview)
    TextView txtOverview;
    @BindView(R.id.sheet_item_released)
    TextView txtItemReleased;
    @BindView(R.id.sheet_item_title)
    TextView txtTitle;
    @BindView(R.id.sheet_item_year)
    TextView txtYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mBottomSheetBehavior = BottomSheetBehavior.from(frameBottomSheet);
        actionListener = new MainPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionListener.setupListeners(this);
        if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            hideBottomSheet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        actionListener.clearListeners(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //*** MainView implementation ***//

    @Override
    public void showSnackbar(@StringRes int resMessage) {
        showSnackbar(resMessage, false);
    }

    @Override
    public void showSnackbar(@StringRes int resMessage, boolean alwaysOn) {
        if(alwaysOn){
            snackbar = Snackbar.make(root, resMessage, Snackbar.LENGTH_INDEFINITE);
        } else {
            snackbar = Snackbar.make(root, resMessage, Snackbar.LENGTH_LONG);
        }
        snackbar.show();
    }

    @Override
    public void showBottomSheet(Movie movie) {
        if(movie == null){
            Log.d(TAG, "showBottomSheet: movie is null not show in bottom sheet");
            return;
        }
        txtItemReleased.setText(movie.getReleased()!=null?movie.getReleased():"");
        txtOverview.setText(movie.getOverview());
        txtTitle.setText(movie.getTitle());
        txtYear.setText(movie.getYear()!=null?movie.getYear().toString():"");
        Glide.with(this)
                .load(movie.getImages().getPoster().getThumb())
                .placeholder(ContextCompat.getDrawable(this, R.drawable.ic_landscape_black_60dp))
                .error(ContextCompat.getDrawable(this, R.drawable.ic_filler_drawable_60dp))
                .into(thumb);

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void hideBottomSheet() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    // +++ End MainView implementation +++

    public IMainContract.ActionListener getActionListener(){
        return actionListener;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) return TopMoviesFragment.newIstance();
            return SearchFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.top_movies);
                case 1:
                    return getString(R.string.search);
            }
            return null;
        }
    }
}
