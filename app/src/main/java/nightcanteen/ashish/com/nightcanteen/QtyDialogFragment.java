package nightcanteen.ashish.com.nightcanteen;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QtyDialogFragment extends DialogFragment implements View.OnClickListener {

    public static List<String> fName = new ArrayList<String>();
    public static List<Integer> fQty = new ArrayList<Integer>();
    public static List<Integer> fPrice = new ArrayList<Integer>();



    Button increment, decrement, cancel, add;
    TextView number;
    int cur;

    public QtyDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_qty_dialog, null);

        increment = (Button) view.findViewById(R.id.plus_button);
        decrement = (Button) view.findViewById(R.id.minus_button);
        cancel = (Button) view.findViewById(R.id.cancel_button);
        add = (Button) view.findViewById(R.id.add_button);
        number = (TextView) view.findViewById(R.id.number_pick);

        cancel.setOnClickListener(this);
        add.setOnClickListener(this);
        increment.setOnClickListener(this);
        decrement.setOnClickListener(this);

        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        cur = Integer.parseInt(number.getText().toString());

        if(v.getId()==R.id.cancel_button)
        {
            dismiss();
        }
        else if (v.getId()==R.id.add_button){
            dismiss();
            //Toast.makeText(getActivity(), cur+"", Toast.LENGTH_SHORT).show();
            Bundle bundle = getArguments();

            int flag = 0;
            int tempPos = -1;
            for(int i=0; i<fName.size(); i++){
                if(fName.get(i).equals(bundle.getString("FOOD_NAME"))){
                    flag=1;
                    fQty.set(i, Integer.parseInt(number.getText().toString()));
                    if(Integer.parseInt(number.getText().toString())==0)
                        tempPos = i;
                }
            }
            System.out.println("Temp pos: " + tempPos);
            if (tempPos!=-1){
                System.out.println("Temp pos if block entered... ");
                fQty.remove(tempPos);
                fName.remove(tempPos);
                fPrice.remove(tempPos);
                HomeActivty.cartItemCount -= 1;
                FoodMenuFragment.tv.setText("" + HomeActivty.cartItemCount);
                if (HomeActivty.cartItemCount==0){
                    FoodMenuFragment.tv.setVisibility(View.INVISIBLE);
                }
            }
            if(Integer.parseInt(number.getText().toString())!=0){
                if (flag!=1){
                    fName.add(bundle.getString("FOOD_NAME"));
                    fPrice.add(bundle.getInt("FOOD_PRICE"));
                    fQty.add(Integer.parseInt(number.getText().toString()));
                    HomeActivty.cartItemCount += 1;
                    FoodMenuFragment.updateCartCount();
                }
            }
        }
        else if (v.getId()==R.id.minus_button){
            if(cur!=0){
                cur--;
                number.setText(""+cur);
            }
        }
        else{
            cur++;
            number.setText(""+cur);
        }
    }

}
