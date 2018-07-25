package com.toposdeus.personajesmexicanos;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Alfonso on 17/01/2018.
 */

public class PantallaPrincipal extends FragmentActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RewardedVideoAdListener {
    GoogleApiClient googleapiClient = null;
    Button botonplay, botonopcion, botonmemo, botonmonedas, btnest;
    private RewardedVideoAd mRewardedVideoAd;
    private FirebaseAnalytics mFirebaseAnalytics;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantallaprincipal);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        findViewById(R.id.sign_in_button).setOnClickListener(this);


        googleapiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //Log.e(TAG, "Could not connect to Play games services");
                        finish();
                    }
                }).build();


        botonplay = (Button) findViewById(R.id.botonplay);
        botonmonedas = (Button) findViewById(R.id.botonmonedas);
        btnest = (Button) findViewById(R.id.botonest);
        btnest.setOnClickListener(this);
        botonmonedas.setOnClickListener(this);
        botonplay.setOnClickListener(this);
        botonopcion = (Button) findViewById(R.id.botonopciones);
        botonopcion.setOnClickListener(this);
        botonmemo = (Button) findViewById(R.id.botonmemo);
        botonmemo.setOnClickListener(this);
        Animation mover2 = AnimationUtils.loadAnimation(this, R.anim.mover2);
        botonopcion.startAnimation(mover2);
        botonplay.startAnimation(mover2);
        botonmemo.startAnimation(mover2);
        botonmonedas.startAnimation(mover2);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd("ca-app-pub-1984616735532779/5554750331",
                new AdRequest.Builder().build());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View vi = inflater.inflate(R.layout.dialogoconfirm, null);
            builder.setView(vi);
            final AlertDialog dialog = builder.create();
            //decidir despues si sera cancelable o no
            dialog.setCancelable(false);
            TextView txtconfirm = (TextView) vi.findViewById(R.id.txtconfirm);
            txtconfirm.setText("deseas salir de la aplicacion");
            txtconfirm.setTypeface(Metodos.fuente(this));
            Button botonsi = vi.findViewById(R.id.botonsi);
            botonsi.setTypeface(Metodos.fuente(this));
            botonsi.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                            finish();
                            onDestroy();
                            System.exit(0);
                        }
                    }
            );
            Button botonno = vi.findViewById(R.id.botonno);
            botonno.setTypeface(Metodos.fuente(this));
            botonno.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();

                        }
                    }
            );
            dialog.show();
            //Metodos.dialogo( this, getLayoutInflater(), "¿seguro deseas salir de la aplicacion?", 0 );
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        Animation mover = AnimationUtils.loadAnimation(this, R.anim.mover1);
        Metodos.preferenciasonido(this, R.raw.click);
        Metodos.preferenciavibrar(this, 50);
        switch (v.getId()) {
            case R.id.botonopciones:
                botonopcion.startAnimation(mover);
                new CountDownTimer(350, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent mainIntent1 = new Intent().setClass(PantallaPrincipal.this,
                                Opciones.class);
                        startActivity(mainIntent1);
                        finish();
                    }
                }.start();
                break;
            case R.id.botonplay:
                botonplay.startAnimation(mover);
                new CountDownTimer(350, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent mainIntent = new Intent().setClass(PantallaPrincipal.this,
                                MenuNiveles.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }.start();
                break;
            case R.id.botonmemo:
                if (googleapiClient.isConnected()) {
                    //Games.Achievements.increment(googleapiClient, getString(R.string.achievement_compartenos), 1);
                    startActivityForResult(Games.Achievements.getAchievementsIntent(googleapiClient), 0);

                } else {
                    Metodos.creartoast(this, getLayoutInflater().inflate(
                            R.layout.toast, (ViewGroup) findViewById(R.id.lytLayout)), "no estas conectado");
                }
                break;
            case R.id.botonest:


                btnest.startAnimation(mover);
                new CountDownTimer(350, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent mainIntent1 = new Intent().setClass(PantallaPrincipal.this,
                                Estadisticas.class);
                        startActivity(mainIntent1);
                        finish();
                    }
                }.start();
                break;
            case R.id.botonmonedas:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View vi = inflater.inflate(R.layout.dialogoconfirm, null);
                builder.setView(vi);
                final AlertDialog dialog = builder.create();
                //decidir despues si sera cancelable o no
                dialog.setCancelable(true);
                TextView txtconfirm = vi.findViewById(R.id.txtconfirm);
                txtconfirm.setText("¿quieres reproducir un video por 50 monedas?");
                txtconfirm.setTypeface(Metodos.fuente(this));
                Button botonsi = vi.findViewById(R.id.botonsi);
                botonsi.setTypeface(Metodos.fuente(this));
                botonsi.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mRewardedVideoAd.isLoaded()) {
                                    mRewardedVideoAd.show();
                                } else {
                                    Metodos.creartoast(PantallaPrincipal.this, getLayoutInflater().inflate(
                                            R.layout.toast, (ViewGroup) findViewById(R.id.lytLayout)), " intentalo mas tarde ");
                                }
                                dialog.cancel();
                            }

                        });

                Button botonno = vi.findViewById(R.id.botonno);
                botonno.setTypeface(Metodos.fuente(this));
                botonno.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        }
                );
                dialog.show();
                break;

            case R.id.sign_in_button:

                //Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            default:
                break;
        }

    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first

    }

}
