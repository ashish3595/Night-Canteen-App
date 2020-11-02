package nightcanteen.ashish.com.nightcanteen;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends BaseFragment {

    private CartItemsAdapter cartItemsAdapter;
    private RecyclerView recyclerView;
    private List<CartItem> data;

    private static final String REGISTER_URL = "http://nitkncapp.16mb.com/place_order.php";
    String phone = String.valueOf(HomeActivty.phoneNo);
    String canId = String.valueOf(HomeActivty.canteenId);
    QtyDialogFragment qtyDialogFragment = new QtyDialogFragment();


    List<String> newQtyList = new ArrayList<String>(qtyDialogFragment.fQty.size());
    List<String> phoneList = new ArrayList<String>(1);
    List<String> canIdList = new ArrayList<String>(1);


    int totalBillableAmount = 0;
    TextView total;
    Button placeOrder;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        total = (TextView) view.findViewById(R.id.totalBill);
        placeOrder = (Button) view.findViewById(R.id.order_btn);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("Food Cart");

        Menu menu = toolbar.getMenu();
        MenuItem sign_out = menu.add(menu.NONE, 2, menu.NONE, "Sign Out");
        MenuItemCompat.setShowAsAction(sign_out, MenuItemCompat.SHOW_AS_ACTION_NEVER);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mbaseFragmentInterface.removeFragment();
            }
        });



        recyclerView = (RecyclerView) view.findViewById(R.id.cartRecycler);
        cartItemsAdapter = new CartItemsAdapter(getActivity(), getData());
        recyclerView.setAdapter(cartItemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(cartItemsAdapter.getItemCount()==0)
            placeOrder.setEnabled(false);
        else
            placeOrder.setEnabled(true);

        for (Integer myInt : qtyDialogFragment.fQty) {
            newQtyList.add(String.valueOf(myInt));
        }

        phoneList.add(phone);
        canIdList.add(canId);

        total.setText("" + totalBillableAmount);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceOrder ru = new PlaceOrder();
                ru.execute(phoneList, canIdList, qtyDialogFragment.fName, newQtyList);
                FoodMenuFragment.tv.setVisibility(View.INVISIBLE);
                //Toast.makeText(getActivity(), "Your order has been placed successfully!", Toast.LENGTH_SHORT).show();
            }
        });

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

        return view;
    }


    public List<CartItem> getData() {
        List<CartItem> data = new ArrayList<>();

        for (int i = 0; i < qtyDialogFragment.fName.size(); i++) {
            CartItem cur = new CartItem();
            cur.foodItemName = qtyDialogFragment.fName.get(i);
            cur.foodItemQty = qtyDialogFragment.fQty.get(i);
            cur.foodItemPrice = qtyDialogFragment.fPrice.get(i) * cur.foodItemQty;
            totalBillableAmount += cur.foodItemPrice;
            data.add(cur);
        }

        return data;
    }


    class PlaceOrder extends AsyncTask<List<String>, Void, String> {

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getContext(), "Please Wait", "Loading...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getActivity(), "Your order has been placed successfully", Toast.LENGTH_SHORT).show();
            HomeActivty.cartItemCount = 0;
            QtyDialogFragment.fName.clear();
            QtyDialogFragment.fQty.clear();
            QtyDialogFragment.fPrice.clear();
            //Toast.makeText(getActivity(), "Your order has been placed successfully!", Toast.LENGTH_SHORT).show();
            mbaseFragmentInterface.removeFragment();
        }

        @Override
        protected String doInBackground(List<String>... params) {
            String phone = params[0].get(0);
            String canId = params[1].get(0);
            String foodName = new JSONArray(params[2]).toString();
            String qty = new JSONArray(params[3]).toString();
            System.out.println(foodName);
            System.out.println(qty);

            String data = "";

            try {
                URL url = new URL(REGISTER_URL);
                //String urlParams = "name="+name+"&phone="+phone+"&password="+password+"&confirmPassword="+confirmPassword+"&block="+block;
                String transferData = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                        URLEncoder.encode("canId", "UTF-8") + "=" + URLEncoder.encode(canId, "UTF-8") + "&" +
                        URLEncoder.encode("foodName", "UTF-8") + "=" + URLEncoder.encode(foodName, "UTF-8") + "&" +
                        URLEncoder.encode("qty", "UTF-8") + "=" + URLEncoder.encode(qty, "UTF-8");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));

                bufferedWriter.write(transferData);
                bufferedWriter.flush();
                bufferedWriter.close();
                StringBuilder sb = new StringBuilder();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "iso-8859-1"));
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    sb.append(temp + '\n');
                }
                data = sb.toString();
                bufferedReader.close();
                con.disconnect();
                return data;

            } catch (Exception e) {
                return null;
            }

        }
    }
}
