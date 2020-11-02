package nightcanteen.ashish.com.nightcanteen;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

class OrderItem{
    public String canName;
    public StringBuilder foodItems=new StringBuilder();
    public int totalBill=0;
    public String orderDate;
    public String orderTime;
}



public class FoodOrderTypeFragment extends Fragment {

    int orderType;

    public OrdersAdapter ordersAdapter;
    public RecyclerView recyclerView;
    private static final String REGISTER_URL = "http://nitkncapp.16mb.com/get_pend_comp_orders.php";

    JSONArray order_item;
    private static final String TAG_RESULTS = "server_response";
    private static final String TAG_CAN_ID = "canId";
    private static final String TAG_FOOD_NAME = "foodName";
    private static final String TAG_FOOD_QTY = "quantity";
    private static final String TAG_FOOD_PRICE = "price";
    private static final String TAG_ORDER_TIME = "time";
    private static final String TAG_ORDER_STATE = "state";

    private String phoneNo = String.valueOf(HomeActivty.phoneNo);

    SimpleDateFormat mysqlDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    Date getDate;
    Date getTime;


    public static FoodOrderTypeFragment createOrderTypeFragment(int orderType) {
        FoodOrderTypeFragment foodOrderTypeFragment = new FoodOrderTypeFragment();
        foodOrderTypeFragment.orderType = orderType;
        return foodOrderTypeFragment;
    }

    public FoodOrderTypeFragment() {
        // Required empty public constructor
    }


    public  static final int ORDER_TYPE_PENDING = 0;
    public  static final int ORDER_TYPE_COMPLETED = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_order_type, container, false);

//        TextView tv = (TextView) view.findViewById(R.id.orderType);
//
//        switch (orderType){
//
//            case 0:
//                tv.setText("Pending_Orders_Fragment");
//                break;
//            case 1:
//                tv.setText("Completed_Orders_Fragment");
//                break;
//            default:
//                tv.setText("Error");
//        }

        recyclerView = (RecyclerView) view.findViewById(R.id.orderTypeRecycler);
        ordersAdapter = new OrdersAdapter(getActivity());
        recyclerView.setAdapter(ordersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public void refreshView() {
        GetOrderJSON g = new GetOrderJSON();
        g.execute(phoneNo);
    }

    class GetOrderJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String phoneNo = params[0];

            Log.d("msg 4", "Order Type: " + orderType);
            String data = "";

            try {
                URL url = new URL(REGISTER_URL);

                String transferData = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phoneNo, "UTF-8");

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
            //if (getActivity() != null)
            //    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            try {
                System.out.println(myJSON);
                JSONObject jsonObj = new JSONObject(myJSON);
                order_item = jsonObj.getJSONArray(TAG_RESULTS);

                List<OrderItem> pending = new ArrayList<>();
                List<OrderItem> completed = new ArrayList<>();

                OrderItem cur = new OrderItem();
                cur.foodItems.append("(");
                //String tempDate=order_item.getJSONObject(0).getString(TAG_ORDER_TIME);

                mysqlDate.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Kolkata"));
                sdfDate.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Kolkata"));
                sdfTime.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Kolkata"));

                for (int i = 0; i < order_item.length(); i++) {
                    JSONObject c = order_item.getJSONObject(i);
                    int can_id = Integer.parseInt(c.getString(TAG_CAN_ID));
                    String food_name = c.getString(TAG_FOOD_NAME);
                    int food_qty = Integer.parseInt(c.getString(TAG_FOOD_QTY));
                    int food_price = Integer.parseInt(c.getString(TAG_FOOD_PRICE));
                    String order_time = c.getString(TAG_ORDER_TIME);
                    int order_state = Integer.parseInt(c.getString(TAG_ORDER_STATE));
                    String next_order_time;
                    if ((i+1) != order_item.length())
                        next_order_time = order_item.getJSONObject(i+1).getString(TAG_ORDER_TIME);
                    else
                        next_order_time = "random";

                    cur.foodItems.append(food_name + " x" + food_qty + ", ");
                    cur.totalBill += food_price*food_qty;

                    System.out.println(cur.foodItems);
                    System.out.println(cur.totalBill);

                    if (next_order_time.equals(order_time)==false) {
                        System.out.println("Entering this loop...");
                        switch (can_id){
                            case 1:
                                cur.canName="3rd Block";
                                break;
                            case 2:
                                cur.canName="7th Block";
                                break;
                            case 3:
                                cur.canName="8th Block";
                                break;
                        }
                        getDate = mysqlDate.parse(order_time);
                        getTime = mysqlDate.parse(order_time);
                        cur.orderDate = sdfDate.format(getDate);
                        cur.orderTime = sdfTime.format(getTime);
                        cur.foodItems.setCharAt(cur.foodItems.lastIndexOf(","), ')');

                        if (orderType==0 && order_state!=2)
                        {
                            System.out.println(cur.canName + " " + cur.foodItems + " " + cur.orderDate + " " + cur.orderTime + " " + cur.totalBill);
                            System.out.println("Added to pending!");
                            pending.add(cur);
                            for (int x=0; x<pending.size(); x++){
                                System.out.println(pending.get(x).foodItems);
                            }
                        }
                        else if(orderType==1 && order_state==2)
                        {
                            System.out.println(cur.canName + " " + cur.foodItems + " " + cur.orderDate + " " + cur.orderTime + " " + cur.totalBill);
                            System.out.println("Added to completed!");
                            completed.add(cur);
                        }
                        cur = new OrderItem();
                        cur.foodItems.append("(");

                    }
                }

                if (ordersAdapter != null && orderType==0){
                    System.out.println("Entered ordertype = 0");

                    ordersAdapter.setData(pending);
                }
                else if (ordersAdapter != null && orderType==1)
                {
                    System.out.println("Entered ordertype = 1");
                    for (OrderItem i : completed){
                        System.out.println(i.toString());
                    }
                    ordersAdapter.setData(completed);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}
