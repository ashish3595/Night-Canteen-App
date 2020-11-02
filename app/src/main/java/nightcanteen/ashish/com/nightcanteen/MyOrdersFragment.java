package nightcanteen.ashish.com.nightcanteen;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends BaseFragment {


    public MyOrdersFragment() {
        // Required empty public constructor
    }

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FoodOrderTypeFragment foodOrderTypeFragments[] = new FoodOrderTypeFragment[2];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_my_orders, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mbaseFragmentInterface.setupToolbarForDrawer(toolbar);
        toolbar.setTitle("Orders History");

        Menu menu = toolbar.getMenu();
        toolbar.inflateMenu(R.menu.cart_menu_item);
        MenuItem menuItem_cart = menu.findItem(R.id.badge);
        //final int x = menuItem_cart.getItemId();
        MenuItemCompat.setActionView(menuItem_cart, R.layout.cart_icon);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(menuItem_cart);
        TextView tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        if(HomeActivty.cartItemCount!=0){
            tv.setVisibility(View.VISIBLE);
            tv.setText("" + HomeActivty.cartItemCount);
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
                    case 2:
                        getActivity().finish();
                        System.exit(0);
                        break;
                    default:

                }
                return true;
            }
        });

        viewPager = (ViewPager) view.findViewById(R.id.viewpager2);
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FoodOrderTypeFragment foodOrderTypeFragment = foodOrderTypeFragments[position];
                foodOrderTypeFragment.refreshView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(0);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        foodOrderTypeFragments[0] = FoodOrderTypeFragment.createOrderTypeFragment(FoodOrderTypeFragment.ORDER_TYPE_PENDING);
        foodOrderTypeFragments[1] = FoodOrderTypeFragment.createOrderTypeFragment(FoodOrderTypeFragment.ORDER_TYPE_COMPLETED);

        adapter.addFrag(foodOrderTypeFragments[0], "PENDING");
        adapter.addFrag(foodOrderTypeFragments[1], "COMPLETED");
        viewPager.setAdapter(adapter);
    }

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
