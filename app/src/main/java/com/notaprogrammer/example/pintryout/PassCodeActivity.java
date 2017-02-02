package com.notaprogrammer.example.pintryout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

public class PassCodeActivity extends AppCompatActivity {

    public static final String TAG = "PinLockView";
    public static final String STEP_TWO = "STEP_TWO";
    public static final String CODE = "CODE";
    public static final String ACCESS_RIGHT = "ACCESS_RIGHT";

    public static final int REQUEST_CODE_FOR_VERIFY = 13;

    protected PinLockView mPinLockView;
    protected IndicatorDots mIndicatorDots;
    protected TextView messageTextView;
    private PassCode passCode = null;

    private static final int INITIAL_SETUP = 1;
    private static final int REPEAT_PASSWORD = 2;
    private static final int ASKING_PERMISSION = 3;

    private Activity activity;
    private int currentStep = 0;
    private int verifyCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_passcode);
        activity = this;
        passCode = new PassCode(this.getApplicationContext());

        String message = "Enter new Security Code";
        currentStep = INITIAL_SETUP;

        Intent intent = getIntent();

        if(intent != null && intent.getBooleanExtra(STEP_TWO, false) && intent.getIntExtra(CODE, -10) != -10 ){
            message = "Verify Security passcode";
            verifyCode = intent.getIntExtra(CODE, -10);
            currentStep = REPEAT_PASSWORD;
        }

        if(intent != null && intent.getBooleanExtra(ACCESS_RIGHT, false)){
            message = "Enter Security Code";
            currentStep = ASKING_PERMISSION;
        }

        messageTextView = (TextView) findViewById(R.id.login_page_message);
        messageTextView.setText(message);

        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        mPinLockView.setTextColor(getResources().getColor(R.color.white));
        mPinLockView.setShowDeleteButton(false);

    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onComplete(String pin) {

            if(   currentStep == INITIAL_SETUP ){
                //go to step II
                Intent intent = new Intent(activity, PassCodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(STEP_TWO, true);
                intent.putExtra(CODE, Integer.parseInt(pin));
                startActivityForResult(intent, REQUEST_CODE_FOR_VERIFY);
            }

            if( currentStep == REPEAT_PASSWORD ){
                if (verifyCode == Integer.parseInt(pin)){
                    passCode.setSavedPassCode(Integer.parseInt(pin));

                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();

                }else{
                    messageTextView.setText("Incorrect Security Code, Try Again?");
                    messageTextView.setTextColor(Color.LTGRAY);
                }
            }

            if( currentStep == ASKING_PERMISSION) {
                if(passCode.getSavedPassCode() == Integer.parseInt(pin)){
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }else{
                    messageTextView.setText("Incorrect Security Code, Try Again?");
                }
            }
            Log.d(TAG, "Pin complete: " + pin);
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FOR_VERIFY  ) {

            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Security code is set " , Toast.LENGTH_SHORT).show();
                finish();
            }else{
                mPinLockView.resetPinLockView();
            }

        }

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
}
