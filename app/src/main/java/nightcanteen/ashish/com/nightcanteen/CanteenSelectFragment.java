package nightcanteen.ashish.com.nightcanteen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CanteenSelectFragment extends BaseFragment {

    String clickedDetail;
    JSONArray canStateArray;
    int openCloseArray[] = new int[3];
    private static final String REGISTER_URL = "http://nitkncapp.16mb.com/get_canteen_state.php";
    private static final String TAG_RESULTS = "server_response";
    private static final String TAG_CAN_ID = "canId";
    private static final String TAG_CAN_STATE = "canState";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_canteen_select, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mbaseFragmentInterface.setupToolbarForDrawer(toolbar);
        toolbar.setTitle("Select Canteen");

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



        String[] canteenList = getResources().getStringArray(R.array.canteen_select);
        final ListView lv = (ListView) view.findViewById(R.id.canteen_select_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.text_aligned_centre, canteenList);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            clickedDetail = (String) lv.getItemAtPosition(position);
            if(clickedDetail.equals("3rd Block NC"))
                HomeActivty.canteenId=1;
            else if(clickedDetail.equals("7th Block NC"))
                HomeActivty.canteenId=2;
            else if (clickedDetail.equals("8th Block NC"))
                HomeActivty.canteenId = 3;

            GetCanStateJSON lu = new GetCanStateJSON();
            lu.execute();
            //Log.d("asdlasd", "" + HomeActivty.canteenId);
            //for (int i=0; i<3; i++)
            //    System.out.println("2Can " + i + " state: " + openCloseArray[i]);

            }
        });

        return view;
    }

    class GetCanStateJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String data="";

            try {
                URL url = new URL(REGISTER_URL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();;
                con.setDoOutput(true);
                con.setDoInput(true);
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"), 8);
                String temp;
                while((temp=bufferedReader.readLine())!=null){
                    sb.append(temp+'\n');
                }
                data=sb.toString();
                bufferedReader.close();
                con.disconnect();
            } catch (Exception e) {
                return null;
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            String myJSON = result;
            //if (getActivity() != null)
            //   Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObj = new JSONObject(myJSON);
                canStateArray = jsonObj.getJSONArray(TAG_RESULTS);

                for (int i = 0; i < canStateArray.length(); i++) {
                    JSONObject c = canStateArray.getJSONObject(i);
                    int canId = Integer.parseInt(c.getString(TAG_CAN_ID));
                    String canState = c.getString(TAG_CAN_STATE).trim();
                    if (canState.equals("open"))
                        openCloseArray[(canId-1)] = 1;
                    else
                        openCloseArray[(canId-1)] = 0;
                }
                //for (int i=0; i<3; i++)
                //    System.out.println("Can " + i + " state: " + openCloseArray[i]);
                if (openCloseArray[HomeActivty.canteenId-1]==1)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY_DETAIL", clickedDetail);
                    FoodMenuFragment foodMenuFragment = new FoodMenuFragment();
                    foodMenuFragment.setArguments(bundle);
                    mbaseFragmentInterface.addFragmentToBackStack(foodMenuFragment);
                }
                else{
                    Toast.makeText(getActivity(), "Sorry! The selected canteen is closed right now!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
