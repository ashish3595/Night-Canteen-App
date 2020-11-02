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
 * Created by ashish on 19/4/16.
 */


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    public List<OrderItem> data = Collections.emptyList();

    private LayoutInflater inflater;

    OrdersAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<OrderItem> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pending_completed_custom_row, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderItem current = data.get(position);
        holder.setupView(position, current.canName, current.foodItems, ""+ current.totalBill, current.orderDate, current.orderTime);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView canName;
        TextView foodItems;
        TextView totalBill;
        TextView orderDate;
        TextView orderTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            canName = (TextView) view.findViewById(R.id.pc_canteen);
            foodItems = (TextView) view.findViewById(R.id.pc_order);
            totalBill = (TextView) view.findViewById(R.id.pc_bill);
            orderDate = (TextView) view.findViewById(R.id.pc_date);
            orderTime = (TextView) view.findViewById(R.id.pc_time);
        }

        public void setupView(int index, String _canName, StringBuilder _foodItems, String _totalBill, String _orderDate, String _orderTime) {
            view.setTag(Integer.toString(index));
            canName.setText(_canName);
            foodItems.setText(_foodItems);
            totalBill.setText(_totalBill);
            orderDate.setText(_orderDate);
            orderTime.setText(_orderTime);
        }
    }
}
