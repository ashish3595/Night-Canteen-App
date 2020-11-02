package nightcanteen.ashish.com.nightcanteen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class FoodMenuFragment extends BaseFragment {
    
    private ViewPager viewPager;
    private TabLayout tabLayout;
    static TextView tv;

    private FoodListFragment foodListFragmentArray[] = new FoodListFragment[6];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_food_menu, container, false);

        Bundle bundle = getArguments();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);


        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        for (int i = 0; i < numOfPages; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }

        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);*/

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle(bundle.getString("KEY_DETAIL", "no argument pass"));

        Menu menu = toolbar.getMenu();
        /*MenuItem search = menu.add(menu.NONE, 0, menu.NONE, "Search");
        search.setIcon(R.drawable.ic_search_24dp);
        MenuItemCompat.setShowAsAction(search, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);*/

        toolbar.inflateMenu(R.menu.cart_menu_item);
        MenuItem menuItem_cart = menu.findItem(R.id.badge);
        //final int x = menuItem_cart.getItemId();
        MenuItemCompat.setActionView(menuItem_cart, R.layout.cart_icon);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(menuItem_cart);
        tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);

        if(HomeActivty.cartItemCount!=0){
            tv.setVisibility(View.VISIBLE);
            tv.setText("" + HomeActivty.cartItemCount);
            Log.d("ac", ""+HomeActivty.cartItemCount);
        }


        MenuItem sign_out = menu.add(menu.NONE, 2, menu.NONE, "Sign Out");
        MenuItemCompat.setShowAsAction(sign_out, MenuItemCompat.SHOW_AS_ACTION_NEVER);

        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment cartFragment = new CartFragment();
                mbaseFragmentInterface.addFragmentToBackStack(cartFragment);
            }
        });

        //menu.clear();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 0:
                        Toast.makeText(getActivity(), "Search was clicked!", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        getActivity().finish();
                        System.exit(0);
                        break;
                    default:

                }
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tv.setVisibility(View.INVISIBLE);
                HomeActivty.cartItemCount = 0;
                QtyDialogFragment.fName.clear();
                QtyDialogFragment.fQty.clear();
                QtyDialogFragment.fPrice.clear();
                mbaseFragmentInterface.removeFragment();
            }
        });


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FoodListFragment foodListFragment = foodListFragmentArray[position];
                foodListFragment.refreshView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(0);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public static void updateCartCount(){
        if(HomeActivty.cartItemCount!=0){
            tv.setVisibility(View.VISIBLE);
            tv.setText("" + HomeActivty.cartItemCount);
            Log.d("ac", ""+HomeActivty.cartItemCount);
        }
    }


    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        foodListFragmentArray[0] = FoodListFragment.createFoodListFragment(FoodListFragment.FOOD_TYPE_STARTERS);
        foodListFragmentArray[1] = FoodListFragment.createFoodListFragment(FoodListFragment.FOOD_TYPE_MAIN_COURSE);
        foodListFragmentArray[2] = FoodListFragment.createFoodListFragment(FoodListFragment.FOOD_TYPE_RICE);
        foodListFragmentArray[3] = FoodListFragment.createFoodListFragment(FoodListFragment.FOOD_TYPE_NOODLES);
        foodListFragmentArray[4] = FoodListFragment.createFoodListFragment(FoodListFragment.FOOD_TYPE_PARATHAS);
        foodListFragmentArray[5] = FoodListFragment.createFoodListFragment(FoodListFragment.FOOD_TYPE_JUICES);


        adapter.addFrag(foodListFragmentArray[0], "STARTERS");
        adapter.addFrag(foodListFragmentArray[1], "MAIN COURSE");
        adapter.addFrag(foodListFragmentArray[2], "RICE");
        adapter.addFrag(foodListFragmentArray[3], "NOODLES");
        adapter.addFrag(foodListFragmentArray[4], "PARATHAS");
        adapter.addFrag(foodListFragmentArray[5], "JUICES");

        viewPager.setAdapter(adapter);

    }
    
    // Refer : http://stackoverflow.com/questions/10396321/remove-fragment-page-from-viewpager-in-android/10399127#10399127
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
