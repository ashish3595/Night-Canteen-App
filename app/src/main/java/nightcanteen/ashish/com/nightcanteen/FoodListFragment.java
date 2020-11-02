package nightcanteen.ashish.com.nightcanteen;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class FoodListFragment extends Fragment {

    public int foodType;

    public FoodItemsAdapter foodItemsAdapter;
    public RecyclerView recyclerView;
    public View.OnClickListener onClickListener;

    public static final int FOOD_TYPE_STARTERS = 0;
    public static final int FOOD_TYPE_MAIN_COURSE = 1;
    public static final int FOOD_TYPE_RICE = 2;
    public static final int FOOD_TYPE_NOODLES = 3;
    public static final int FOOD_TYPE_PARATHAS = 4;
    public static final int FOOD_TYPE_JUICES = 5;

    private String foodName;
    private int foodPrice;

    private static final String REGISTER_URL = "http://nitkncapp.16mb.com/get_food_menu_items.php";

    JSONArray food_item;
    private static final String TAG_RESULTS = "server_response";
    private static final String TAG_FOOD_NAME = "food_name";
    private static final String TAG_FOOD_PRICE = "price";


    private String canId = String.valueOf(HomeActivty.canteenId);


    public static FoodListFragment createFoodListFragment(int foodType) {
        FoodListFragment foodListFragment = new FoodListFragment();
        foodListFragment.foodType = foodType;
        return foodListFragment;
    }


    public FoodListFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        //TextView tv = (TextView) view.findViewById(R.id.foodType);


        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(v.getTag().toString());
                FoodItem foodItem = foodItemsAdapter.data.get(position);

                foodName = foodItem.foodItemName;
                foodPrice = foodItem.foodItemPrice;

                showDialog(v);

            }
        };

        recyclerView = (RecyclerView) view.findViewById(R.id.ordersRecycler);
        foodItemsAdapter = new FoodItemsAdapter(getActivity(), onClickListener);
        recyclerView.setAdapter(foodItemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public void refreshView() {
        GetDataJSON g = new GetDataJSON();
        g.execute(canId, String.valueOf(foodType));
    }

    class GetDataJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String canId = params[0];
            String food_type = params[1];

            Log.d("msg 1", "CanID: " + canId + "    " + "FoodType: " + foodType);
            String data = "";

            try {
                URL url = new URL(REGISTER_URL);

                String transferData = URLEncoder.encode("canId", "UTF-8") + "=" + URLEncoder.encode(canId, "UTF-8") + "&" +
                        URLEncoder.encode("foodType", "UTF-8") + "=" + URLEncoder.encode(food_type, "UTF-8");

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

                String line;
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
            //   Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObj = new JSONObject(myJSON);
                food_item = jsonObj.getJSONArray(TAG_RESULTS);

                List<FoodItem> fi = new ArrayList<>();
                for (int i = 0; i < food_item.length(); i++) {
                    JSONObject c = food_item.getJSONObject(i);
                    String food_name = c.getString(TAG_FOOD_NAME);
                    String food_price = c.getString(TAG_FOOD_PRICE);

                    FoodItem cur = new FoodItem();
                    cur.foodItemName = food_name;
                    cur.foodItemPrice = Integer.parseInt(food_price);
                    fi.add(cur);
                }

                if (foodItemsAdapter != null)
                    foodItemsAdapter.setData(fi);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void showDialog(View v) {
        QtyDialogFragment qtyDialogFragment = new QtyDialogFragment();
        //qtyDialogFragment.setTargetFragment(getParentFragment(), REQUEST_CODE);
        Bundle bundle = new Bundle();
        bundle.putString("FOOD_NAME", foodName);
        bundle.putInt("FOOD_PRICE", foodPrice);
        qtyDialogFragment.setArguments(bundle);
        qtyDialogFragment.show(getActivity().getSupportFragmentManager(), "qtyDialog");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        Log.d("DEF", "Nope");
        menuInflater.inflate(R.menu.home_activty, menu);
        menu.clear();
        super.onCreateOptionsMenu(menu, menuInflater);
    }
}
