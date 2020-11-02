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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ashish on 24/3/16.
 */
public class Login extends Fragment {

    private AutoCompleteTextView phone;
    private EditText password;

    private static final String REGISTER_URL = "http://nitkncapp.16mb.com/user_login.php";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */

        View view = inflater.inflate(
                R.layout.fragment_login, container, false);

        phone = (AutoCompleteTextView) view.findViewById(R.id.login_phone);
        password = (EditText) view.findViewById(R.id.login_password);

        Button loginBtn = (Button) view.findViewById(R.id.log_in_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), HomeActivty.class);
                //startActivity(intent);
                //getActivity().finish();

                loginUser();
            }
        });

        Button signUpBtn = (Button) view.findViewById(R.id.show_sign_up_screen);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp signUp= new SignUp();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, signUp, "signUpFrag")
                        .addToBackStack(null)
                        .commit();
            }
        });


        return view;

    }

    String _phone;
    String _password;

    private void loginUser() {
        _phone = phone.getText().toString();
        _password = password.getText().toString();
        LoginUser lu = new LoginUser();
        lu.execute(_phone, _password);
    }

    class LoginUser extends AsyncTask<String, Void, String> {

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
            if(s.trim().equals("Login Failed")){
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }else{
                Log.d("s val", s);
                Intent intent = new Intent(getActivity(), HomeActivty.class);
                intent.putExtra("PHONE", _phone);
                System.out.println("Hello" + _phone + "bye");
                //System.out.println(Long.parseLong(_phone));
                HomeActivty.phoneNo=Long.parseLong(_phone);
                startActivity(intent);
                getActivity().finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String phone = params[0];
            EncryptPassword encryptPassword = new EncryptPassword();
            String password = encryptPassword.convert(params[1]);
            String data="";

            try {
                URL url = new URL(REGISTER_URL);
                //String urlParams = "name="+name+"&phone="+phone+"&password="+password+"&confirmPassword="+confirmPassword+"&block="+block;
                String transferData= URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();;
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
