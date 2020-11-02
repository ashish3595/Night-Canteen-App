package nightcanteen.ashish.com.nightcanteen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by ashish on 26/3/16.
 */
public class FoodItemsAdapter extends RecyclerView.Adapter<FoodItemsAdapter.MyViewHolder> {

    public List<FoodItem> data = Collections.emptyList();

    public View.OnClickListener onClickListener;
    private LayoutInflater inflater;

    FoodItemsAdapter(Context context, View.OnClickListener onClickListener){
        inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    public void setData(List<FoodItem> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.food_item_custom_row, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FoodItem current = data.get(position);
        holder.setupView(position, current.foodItemName, "" + current.foodItemPrice);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView foodItemName;
        TextView foodItemPrice;

        public MyViewHolder(View _itemView) {
            super(_itemView);

            itemView = _itemView;
            foodItemName = (TextView) itemView.findViewById(R.id.food_item_name);
            foodItemPrice = (TextView) itemView.findViewById(R.id.food_item_price);

            itemView.setOnClickListener(onClickListener);

        }

        public void setupView(int index, String _foodItemName, String _foodItemPrice) {
            itemView.setTag(Integer.toString(index));
            foodItemName.setText(_foodItemName);
            foodItemPrice.setText(_foodItemPrice);
        }
    }
}
