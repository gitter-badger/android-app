package io.kida.geofancy.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.kida.geofancy.app.R;

@EActivity(R.layout.activity_signup)

public class SignupActivity extends Activity {

    @ViewById(R.id.username_text)
    EditText mUsernameText;

    @ViewById(R.id.email_text)
    EditText mEmailText;

    @ViewById(R.id.password_text)
    EditText mPasswordText;

    @ViewById(R.id.signup_button)
    Button mSignupButton;

    @ViewById(R.id.tos_button)
    Button mTosButtonl;

    private GeofancyNetworkingCallback mNetworkingCallback = null;
    private ProgressDialog mProgressDialog = null;

    @AfterViews
    void setup(){

    }

    @Click(R.id.signup_button)
    void signup(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(R.string.loading);
        mProgressDialog.setMessage("Creating Account…");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        GeofancyNetworkingCallback networkingCallback = new GeofancyNetworkingCallback() {
            @Override
            public void onLoginFinished(boolean success, String sessionId) {

            }

            @Override
            public void onSignupFinished(boolean success, boolean userAlreadyExisting) {
                mProgressDialog.dismiss();
                if (!success) {
                    if (userAlreadyExisting) {
                        simpleAlert("Error when creating Account. Username or E-Mail is already existing.");
                        return;
                    }
                    simpleAlert("Error when creating Account. Please try again.");
                    return;
                }
                new AlertDialog.Builder(SignupActivity.this)
                        .setMessage("Your Account has been created. You may now sign in with your Username & Password")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SignupActivity.this.finish();
                            }
                        })
                        .show();
            }

            @Override
            public void onCheckSessionFinished(boolean sessionValid) {

            }

            @Override
            public void onDispatchFencelogFinished(boolean success) {

            }
        };

        getApp().getNetworking().doSignup(mUsernameText.getText().toString(), mPasswordText.getText().toString(), mEmailText.getText().toString(), networkingCallback);
    }

    @Click(R.id.tos_button)
    void tosClick(){
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(Constants.TOS_URI));
        startActivity(intent);
    }

    private void simpleAlert(String msg){
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setNeutralButton("OK", null)
                .show();
    }

    private GeofancyApplication getApp(){
        return (GeofancyApplication) getApplication();
    }
}
