package nightcanteen.ashish.com.nightcanteen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivty extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseFragment.BaseFragmentInterface {

    public static int cartItemCount = 0;
    public static int canteenId = 1;
    public static long phoneNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activty);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //TextView t1 = (TextView) navigationView.getHeaderView(R.layout.nav_header_home_activty).findViewById(R.id.user_name);
        //TextView t2 = (TextView) navigationView.getHeaderView(R.layout.nav_header_home_activty).findViewById(R.id.phone_no);
        //Intent intent = getIntent();

        // Refer : http://stackoverflow.com/questions/33194594/navigationview-get-find-header-layout
        View headerLayout = navigationView.getHeaderView(0);
        Intent intent = getIntent();

        TextView t2 = (TextView) headerLayout.findViewById(R.id.phone_no);
        t2.setText(intent.getStringExtra("PHONE"));

        //t1.setText(intent.getStringExtra("NAME"));
        //t2.setText(intent.getStringExtra("PHONE"));
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new CanteenSelectFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_activty, menu);
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
            //Toast.makeText(this, "Sign out!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        if (id == R.id.nav_camera) {
            replaceFragment(new CanteenSelectFragment());

        } else if (id == R.id.nav_gallery) {
            replaceFragment(new MyOrdersFragment());
        } else if (id == R.id.nav_slideshow) {
            replaceFragment(new ExpensesFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void replaceFragment(BaseFragment baseFragment) {
        baseFragment.setmListener(this);
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fm.beginTransaction();
        String tag = baseFragment.getClass().getName();
        ft.replace(R.id.home_fragment_container, baseFragment, tag);
        ft.commit();
    }

    @Override
    public void addFragmentToBackStack(BaseFragment baseFragment) {
        baseFragment.setmListener(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        String tag = baseFragment.getClass().getName();
        ft.add(R.id.home_fragment_container, baseFragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }

    @Override
    public void setupToolbarForDrawer(Toolbar toolbar) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void removeFragment(){
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

}
