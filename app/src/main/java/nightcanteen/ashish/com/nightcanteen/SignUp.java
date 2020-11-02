package nightcanteen.ashish.com.nightcanteen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ashish on 24/3/16.
 */
public class SignUp extends Fragment {

    private EditText name;
    private EditText phone;
    private EditText password;
    private EditText confirmPassword;
    private Spinner block;
    private Button signUp;

    private static final String REGISTER_URL = "http://nitkncapp.16mb.com/user_register.php";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */

        View view = inflater.inflate(
                R.layout.fragment_signup, container, false);


        name = (EditText) view.findViewById(R.id.username);
        phone = (EditText) view.findViewById(R.id.phone_no);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirm_password);
        block = (Spinner) view.findViewById(R.id.spinner);

        Button signUpBtn = (Button) view.findViewById(R.id.sign_up_button);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), HomeActivty.class);
                //startActivity(intent);
                registerUser();
            }
        });


        return view;
    }

    String _name;
    String _phone;

    private void registerUser() {
        _name = name.getText().toString();
        _phone = phone.getText().toString();
        String _password = password.getText().toString();
        String _confirmPassword = confirmPassword.getText().toString();
        //Log.d("a", _password);
        String _block = block.getSelectedItem().toString();

        RegisterUser ru = new RegisterUser();
        ru.execute(_name, _phone, _password, _confirmPassword, _block);
    }


    class RegisterUser extends AsyncTask<String, Void, String> {

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
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            String toComp = "Registered!";
            //s = s.toString();
            //Log.d("s val", s);
            //Log.d("comp val", toComp);
            if(s.trim().equals(toComp)){
                Log.d("s val", s);
                Intent intent = new Intent(getActivity(), HomeActivty.class);
                intent.putExtra("PHONE", _phone);
                HomeActivty.phoneNo=Long.parseLong(_phone);
                startActivity(intent);
                getActivity().finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String phone = params[1];
            EncryptPassword encryptPassword = new EncryptPassword();
            String password = encryptPassword.convert(params[2]);
            String confirmPassword = encryptPassword.convert(params[3]);
            String block = params[4];
            String data="";
            int tap;

            try {
                URL url = new URL(REGISTER_URL);
                //String urlParams = "name="+name+"&phone="+phone+"&password="+password+"&confirmPassword="+confirmPassword+"&block="+block;
                String transferData= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+
                        URLEncoder.encode("confirmPassword","UTF-8")+"="+URLEncoder.encode(confirmPassword,"UTF-8")+"&"+
                        URLEncoder.encode("block","UTF-8")+"="+URLEncoder.encode(block,"UTF-8");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));

                bufferedWriter.write(transferData);
                bufferedWriter.flush();
                bufferedWriter.close();
                StringBuilder sb = new StringBuilder();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "iso-8859-1"));
                String temp;
                while((temp=bufferedReader.readLine())!=null){
                    sb.append(temp+'\n');
                }
                data=sb.toString();
                //Log.d("a", data);
                bufferedReader.close();
                con.disconnect();
                return data;

            } catch (Exception e) {
                return null;
            }
        }
    }
}
