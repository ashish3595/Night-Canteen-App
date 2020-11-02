package nightcanteen.ashish.com.nightcanteen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ashish on 5/4/16.
 */
public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.MyViewHolder> {

    public List<CartItem> data = Collections.emptyList();
    private LayoutInflater inflater;


    CartItemsAdapter(Context context, List<CartItem> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_custom_row, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CartItem current = data.get(position);
        holder.setupView(position, current.foodItemName, "" + current.foodItemQty, "" + current.foodItemPrice);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView foodItemName;
        TextView foodItemQty;
        TextView foodItemPrice;

        public MyViewHolder(View _itemView) {
            super(_itemView);
            itemView = _itemView;
            foodItemName = (TextView) itemView.findViewById(R.id.f_name);
            foodItemQty = (TextView) itemView.findViewById(R.id.f_qty);
            foodItemPrice = (TextView) itemView.findViewById(R.id.f_price);

            //itemView.setOnClickListener(onClickListener);

        }

        public void setupView(int index, String _foodItemName, String _foodItemQty, String _foodItemPrice) {
            itemView.setTag(Integer.toString(index));
            foodItemName.setText(_foodItemName);
            foodItemQty.setText(_foodItemQty);
            foodItemPrice.setText(_foodItemPrice);
        }
    }
}
