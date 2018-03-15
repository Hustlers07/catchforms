package com.catchforms.vinod.rupesh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class OTP extends AppCompatActivity {

    EditText edtPasscode1,edtPasscode2,edtPasscode3,edtPasscode4;
    TextView remainingtimer,otptimeleft,submit_otp,otpsent;
    static StringBuilder sb;
    //  static int time;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    static boolean registration=false;
    static String reset_pass_id;
    EditText new_pass,confirm_pass;
    Button submit_reset_pass;
    boolean visibility_password=false;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getSupportActionBar().hide();
        initialise();

        ed.putString("otp",otpgenerator.random().toString());
        ed.commit();

        if(registration){
            sendOTP();
        }else {
            forgetpass();
        }
        otp_countdown();
        //   startRepeatingTask();
        edittextbox();
    }


    private void initialise() {
        sb = new StringBuilder();
        edtPasscode1 =(EditText)findViewById(R.id.otp1);
        edtPasscode2 =(EditText)findViewById(R.id.otp2);
        edtPasscode3 =(EditText)findViewById(R.id.otp3);
        edtPasscode4 = (EditText)findViewById(R.id.otp4);
        remainingtimer = (TextView)findViewById(R.id.otptimer);
        otptimeleft =(TextView)findViewById(R.id.otptimeleft);
        submit_otp = (TextView)findViewById(R.id.otpsubmit) ;
        otpsent = (TextView)findViewById(R.id.otpsent);
        sp=getSharedPreferences("Data",MODE_PRIVATE);
        ed= sp.edit();
        //     time=30000;
    }
    CountDownTimer otp_count;
    public void otp_countdown(){
        otp_count = new CountDownTimer(200000,10000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingtimer.setText(" "+String.valueOf(millisUntilFinished/10000+" sec"));
                submit_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String value = edtPasscode1.getText().toString() + edtPasscode2.getText().toString() + edtPasscode3.getText().toString() + edtPasscode4.getText().toString();

                        if (value.length() != 4) {
                            Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                        } else {
                            if (sp.getString("otp",null).contentEquals(value)){
                                Toast.makeText(getApplicationContext(), "Matched", Toast.LENGTH_SHORT).show();
                                if (registration){
                                    submitDetails();
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Reset Password", Toast.LENGTH_SHORT).show();
                                    resetPassword();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_SHORT).show();
                            }
                        }
//                        catch (Exception e){
//                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
            }

            @Override
            public void onFinish() {
                remainingtimer.setText(" ");
                ed.putString("otp","xxxx");
                ed.commit();

                otptimeleft.setText("Resend");
                otptimeleft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(otptimeleft.getText().toString().contentEquals("Resend")) {
                            try{
                                otp_count.start();
                                otptimeleft.setText("Time Left");
                                //time = 30000;
                                ed.putString("otp",otpgenerator.random().toString());
                                ed.commit();
                                forgetpass();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }
                });
                submit_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Time Out",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        otp_count.start();
    }

//    void startRepeatingTask()
//    {
//        mHandlerTask.run();
//    }

//        void stopRepeatingTask()
//    {
//        handler.removeCallbacks(mHandlerTask);
//    }

//    Handler handler=new Handler();

//    Runnable mHandlerTask =new Runnable() {
//        @Override
//        public void run() {
//            if(time==0){
    //              stopRepeatingTask();
//                remainingtimer.setText(" ");
//                ed.putString("otp","xxxx");
//                ed.commit();
//
//             otptimeleft.setText("Resend");
//                otptimeleft.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(otptimeleft.getText().toString().contentEquals("Resend")) {
//                            otptimeleft.setText("Time Left");
//                            time = 30000;
//                            ed.putString("otp",otpgenerator.random().toString());
//                            ed.commit();
//                            sendOTP();
//                            startRepeatingTask();
//                        }
//                    }
//                });
//                submit_otp.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(),"Time Out",Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
    //           else {
    //      Integer otptime = time/1000;
//                remainingtimer.setText(otptime.toString());
//                submit_otp.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                            String value = edtPasscode1.getText().toString() + edtPasscode2.getText().toString() + edtPasscode3.getText().toString() + edtPasscode4.getText().toString();
//
//                            if (value.length() != 4) {
//                                Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
//                            } else {
//                                if (sp.getString("otp",null).contentEquals(value)){
//                                    Toast.makeText(getApplicationContext(), "Matched", Toast.LENGTH_SHORT).show();
//                                    if (registration){
//                                        submitDetails();
//                                        finish();
//                                    }
//                                   else {
//                                        Toast.makeText(getApplicationContext(), "Reset Password", Toast.LENGTH_SHORT).show();
//                                        resetPassword();
//                                    }
//                                }
//                                else {
//                                    Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_SHORT).show();
//                                }
//                            }
////                        catch (Exception e){
////                            e.printStackTrace();
////                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
////                        }
//                    }
//                });
    //      time-=1000;
    //  }
    //  handler.postDelayed(mHandlerTask,4000);
    //       }
    //   };


    ImageView pass_visibility;
    private void resetPassword() {
        View view =getLayoutInflater().inflate(R.layout.resetpass_dialog,null);
        AlertDialog.Builder build = new AlertDialog.Builder(OTP.this);
        build.setView(view);
        new_pass =(EditText)view.findViewById(R.id.new_password);
        confirm_pass=(EditText)view.findViewById(R.id.confirm_password);
        pass_visibility=(ImageView)view.findViewById(R.id.reset_password_visibility);
        submit_reset_pass =(Button)view.findViewById(R.id.reset_submit);
        submit_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordfield_check())
                {
                    volley_resetpass();
                }

            }
        });
        pass_visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visibility_password==false){
                    new_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirm_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pass_visibility.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    visibility_password=true;
                }
                else {
                    new_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirm_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pass_visibility.setImageResource(R.drawable.ic_remove_red_eye_black_24dp);
                    visibility_password=false;
                }
            }
        });
        build.create().show();
    }

    private boolean passwordfield_check() {
        if (new_pass.getText().toString().isEmpty() || confirm_pass.getText().toString().isEmpty() || !new_pass.getText().toString().contentEquals(confirm_pass.getText().toString())){
            if (new_pass.getText().toString().isEmpty()){
                new_pass.setError("Required");
            }
            else if (confirm_pass.getText().toString().isEmpty()){
                confirm_pass.setError("Required");
            }
            else {
                confirm_pass.setError("Password Not Matched");
            }
            return false;
        }
        else {
            return true;
        }
    }

    private void volley_resetpass() {
        String url = "http://catchforms.in/vinod/catchforms/user_data/resetpassword.php?loggedid="+reset_pass_id+"&pass="+new_pass.getText().toString();
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("reset_status")==501){

                        startActivity(new Intent(getApplicationContext(),firstscreen.class));
                        finish();
                        Toast.makeText(getApplicationContext(),"Reset Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else if(response.getInt("reset_status")==503)
                    {
                        Toast.makeText(getApplicationContext(),"Unable to Reset Password",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"User does not exist",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Unable to reset",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("datatotal","volleyerror");
            }
        }


        );
        queue.add(jsonObjectRequest);


    }

    private void forgetpass() {
        String contact;
        if (getIntent().getStringExtra("contact").contains("+91")){
            contact = getIntent().getStringExtra("contact").replace("+91","");
        }
        else {
            contact = getIntent().getStringExtra("contact");
        }
        url = "http://catchforms.in/vinod/catchforms/user_data/forgotpass.php?contact="+contact+"&otp="+sp.getString("otp","null");
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("response111",response.toString());
                    if(response.getInt("account")==1){
                        otpsent.setVisibility(View.VISIBLE);

                        if(response.getInt("otp_status")==301){
                            otpsent.setText("OTP Sent to : "+response.getString("Email"));
                            reset_pass_id =response.getString("Logged_id");
                        }
                        else if(response.getInt("otp_status")==302 ){
                            if (response.getString("status").equals("success")){
                                JSONArray message = response.getJSONArray("messages");
                                JSONObject recipient = message.getJSONObject(0);
                                reset_pass_id =response.getString("Logged_id");
                                otpsent.setText("OTP Sent to : "+recipient.getString("recipient").replace("91",""));
                            }
                            else {
                                otpsent.setText("Failed to send OTP (Registration time 9am - 9pm");
                            }
                        }
                        else {
                            otpsent.setText("Failed to send OTP");
                        }
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("unablee",url);
                    Toast.makeText(getApplicationContext(),"Unable to Send OTP",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("datatotal","volleyerror");
            }
        }


        );
        queue.add(jsonObjectRequest);

    }

    private void edittextbox() {
        edtPasscode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1){
                    sb.deleteCharAt(0);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(sb.length()==0&edtPasscode1.length()==1){
                    sb.append(s);
                    edtPasscode1.clearFocus();
                    edtPasscode2.requestFocus();
                    edtPasscode2.setCursorVisible(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(sb.length()==0){
                    edtPasscode2.requestFocus();
                }
            }
        });


        edtPasscode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1){
                    sb.deleteCharAt(0);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(sb.length()==0&edtPasscode2.length()==1){
                    sb.append(s);
                    edtPasscode2.clearFocus();
                    edtPasscode3.requestFocus();
                    edtPasscode3.setCursorVisible(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(sb.length()==0){
                    edtPasscode3.requestFocus();
                }
            }
        });


        edtPasscode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1){
                    sb.deleteCharAt(0);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(sb.length()==0&edtPasscode3.length()==1){
                    sb.append(s);
                    edtPasscode3.clearFocus();
                    edtPasscode4.requestFocus();
                    edtPasscode4.setCursorVisible(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(sb.length()==0){
                    edtPasscode4.requestFocus();
                }
            }
        });


        edtPasscode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(sb.length()==1){
                    sb.deleteCharAt(0);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(sb.length()==0&edtPasscode4.length()==1){
                    sb.append(s);
                    edtPasscode4.clearFocus();
                    edtPasscode1.requestFocus();
                    edtPasscode1.setCursorVisible(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(sb.length()==0){
                    edtPasscode1.requestFocus();
                }
            }
        });
    }
    private void sendOTP() {
        String contact;
        if (getIntent().getStringExtra("contact").contains("+91")){
            contact = getIntent().getStringExtra("contact").replace("+91","");
        }
        else {
            contact = getIntent().getStringExtra("contact");
        }
        String url = "http://catchforms.in/vinod/catchforms/user_data/smsotp.php?contact="+contact+"&otp="+sp.getString("otp","null");


        final JSONObject[] errors = new JSONObject[1];
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    try {
                        errors[0] = (JSONObject) response.getJSONArray("errors").get(0);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if (response.getString("status").contentEquals("success")){
                        Toast.makeText(getApplicationContext(),"OTP Sent",Toast.LENGTH_SHORT).show();
                    }
                    else if(errors[0].getString("message").equals("Messages can only be sent between 9am to 9pm as restricted by TRAI NCCP regulation")) {
                        Toast.makeText(getApplicationContext(),"Registration Time (9:00am - 9:00pm)",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),firstscreen.class));
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Unable To Send OTP",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Unable to Send OTP",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("datatotal","volleyerror");
            }
        }


        );
        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registration = false;
        reset_pass_id=null;
//        startRepeatingTask();
    }
    public void submitDetails(){

        Intent data =getIntent();

        String url="http://catchforms.in/vinod/catchforms/user_data/register.php?username="+sp.getString("name","Not present")+"&password="+data.getStringExtra("center_pass")+"&email="+sp.getString("personEmail","Not present")+"&loggedid="+sp.getString("personId","notpresent")+"&loggedwith="+sp.getString("loggedwith","Not present")+"&contact="+data.getStringExtra("contact")+"&state="+data.getStringExtra("state")+"&centername="+data.getStringExtra("center_name")+"&centeraddress="+data.getStringExtra("center_address")+"&aadhar="+data.getStringExtra("aadhar")+"&voterid="+data.getStringExtra("voter_id")+"&pan="+data.getStringExtra("pan")+"&ration="+data.getStringExtra("ration")+"&restother="+data.getStringExtra("restother")+"&rest="+data.getStringExtra("rest")+"&latitude="+data.getStringExtra("latitude")+"&longitude="+data.getStringExtra("longitude");
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        Log.d("success11",url);
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int s=Integer.parseInt(response.getString("status"));

                    if (s==401){
                        ed.putString("logged_as","Cafe Center");
                        ed.commit();
                        startActivity(new Intent(getApplicationContext(),firstscreen.class));
                        Toast.makeText(getApplicationContext(),"Registeration Success",Toast.LENGTH_SHORT).show();
                    }
                    else if(s==400){
                        ed.putString("logged_as","Cafe Center");
                        ed.commit();
                        startActivity(new Intent(getApplicationContext(),firstscreen.class));
                        Toast.makeText(getApplicationContext(),"Registrar Already Exists",Toast.LENGTH_SHORT).show();
                    }
                    else if(s==402){
                        Toast.makeText(getApplicationContext(),"Registeration Failed",Toast.LENGTH_SHORT).show();
                    }
                    else if(s==404){
                        Toast.makeText(getApplicationContext(),"Registerion Failed",Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){

                    Log.d("datatotal","exception");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("datatotal","volleyerror");
            }
        }

        );
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        registration = false;
        reset_pass_id=null;
//        stopRepeatingTask();
        startActivity(new Intent(getApplicationContext(),firstscreen.class));
        finish();
    }
}
