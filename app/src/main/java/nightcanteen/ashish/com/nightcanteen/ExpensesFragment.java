package nightcanteen.ashish.com.nightcanteen;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpensesFragment extends BaseFragment implements ExpenseFilterDialogFragment.ExpenseFilterDialogFragmentInterface{

    TextView totNC3;
    TextView totNC7;
    TextView totNC8;
    int filterDialogOptionNumber = 0;
    JSONArray jsonArray;
    private static final String TAG_RESULTS = "server_response";
    private static final String TAG_CAN_ID = "canId";
    private static final String TAG_TOTAL = "total";
    private static final String REGISTER_URL = "http://nitkncapp.16mb.com/get_expenses.php";

    public ExpensesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mbaseFragmentInterface.setupToolbarForDrawer(toolbar);
        toolbar.setTitle("My Expenses");

        Menu menu = toolbar.getMenu();

        MenuItem filter = menu.add(menu.NONE, 1, menu.NONE, "Filter");
        filter.setIcon(R.drawable.ic_settings_24dp);
        MenuItemCompat.setShowAsAction(filter, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

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


        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment cartFragment = new CartFragment();
                mbaseFragmentInterface.addFragmentToBackStack(cartFragment);
            }
        });

        MenuItem sign_out = menu.add(menu.NONE, 2, menu.NONE, "Sign Out");
        MenuItemCompat.setShowAsAction(sign_out, MenuItemCompat.SHOW_AS_ACTION_NEVER);

        //menu.clear();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1:
                        showDialog(view);
                        break;
                    case 2:
                        getActivity().finish();
                        System.exit(0);
                        break;
                    default:
                        Toast.makeText(getActivity(), "Nothing was clicked!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        totNC3 = (TextView) view.findViewById(R.id.tot_NC3);
        totNC7 = (TextView) view.findViewById(R.id.tot_NC7);
        totNC8 = (TextView) view.findViewById(R.id.tot_NC8);
        return view;
    }

    private void refreshView() {
        GetNCTotal g = new GetNCTotal();
        g.execute(String.valueOf(HomeActivty.phoneNo), String.valueOf(filterDialogOptionNumber));
    }

    @Override

    public void setFilterDialogOptionNumber(int number) {
        this.filterDialogOptionNumber = number;
        refreshView();
    }

    class GetNCTotal extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String phone = params[0];
            String days = params[1];

            Log.d("msg 1", "Phone: " + phone + "    " + "FoodType: " + days);
            String data = "";

            try {
                URL url = new URL(REGISTER_URL);

                String transferData = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                        URLEncoder.encode("days", "UTF-8") + "=" + URLEncoder.encode(days, "UTF-8");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
                bufferedWriter.write(transferData);
                bufferedWriter.flush();
                bufferedWriter.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                data = sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            String myJSON = result;
            System.out.println(result);
            //if (getActivity() != null)
            //    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObj = new JSONObject(myJSON);
                jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    int canId = Integer.parseInt(c.getString(TAG_CAN_ID));
                    String total = c.getString(TAG_TOTAL);
                    System.out.println(total);
                    switch (canId){
                        case 1:
                            totNC3.setText(""+total);
                            break;
                        case 2:
                            totNC7.setText(""+total);
                            break;
                        case 3:
                            totNC8.setText(""+total);
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void showDialog(View v)
    {
        ExpenseFilterDialogFragment expenseFilterDialogFragment = new ExpenseFilterDialogFragment();
        expenseFilterDialogFragment.expenseFilterDialogFragmentInterface = this;
        expenseFilterDialogFragment.show(getActivity().getSupportFragmentManager(), "filterDialog");
    }



}
