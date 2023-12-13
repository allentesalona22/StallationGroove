package dev.sg.stellationgroove;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class StellationMain extends AppCompatActivity{

    private  static final int COMBONUMBER = 7;
    private static final int COEF = 72;
    private static final int COEFW = 142;
    private static final int COEFE = 212;
    private int position1 = 5;
    private int position2 = 5;
    private int position3 = 5;
    private final int[] slot = {1, 2, 3, 4, 5, 6, 7};

    private RecyclerView rv1;
    private RecyclerView rv2;
    private RecyclerView rv3;
    private CustomManager layoutManager1;
    private CustomManager layoutManager2;
    private CustomManager layoutManager3;


    private TextView energyBallPrice;
    private TextView myPower;
    private TextView bet;

    int myCoinsval;
    int betVal;
    int jackpotVal;

    private boolean firstRun;

    private GameLogic gameLogic;

    private SharedPreferences pref;
    private MediaPlayer bgsound;
    public static final String PREFS_NAME = "FirstRun";


    private int playmusic;
    private int playsound;
    private ImageView musicOff;
    private ImageView musicOn;
    private ImageView soundon;
    private ImageView soundoff;

    @Override
    @SuppressWarnings("DEPRECATION")
    protected void onCreate(Bundle savedInstanceState) {

        ImageButton minusButton;
        ImageButton plusButton;
        SpinnerAdapter adapter;
        ImageView settingsButton;
        ImageButton spinButton;
        MediaPlayer mP;
        MediaPlayer win;

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_stellationmain);

        bgsound = MediaPlayer.create(this,R.raw.bg_music);
        bgsound.setLooping(true);
        mP = MediaPlayer.create(this, R.raw.spin);
        win = MediaPlayer.create(this, R.raw.win);

        pref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        firstRun = pref.getBoolean("firstRun1", true);

        if (firstRun) {
            playmusic = 1;
            playsound = 1;
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
        } else {
            playmusic= pref.getInt("music1", 1);
            playsound = pref.getInt("sound1", 1);
            checkmusic();

        }

        Log.d("MUSIC",String.valueOf(playmusic));

        //Initializations
        gameLogic = new GameLogic();
        settingsButton = findViewById(R.id.settings);
        spinButton = findViewById(R.id.spinButton);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);
        energyBallPrice = findViewById(R.id.energyBall);
        myPower = findViewById(R.id.energy);
        bet = findViewById(R.id.bet);
        adapter = new SpinnerAdapter();

        //RecyclerView settings
        rv1 = findViewById(R.id.spinner1);
        rv2 = findViewById(R.id.spinner2);
        rv3 = findViewById(R.id.spinner3);
        rv1.setHasFixedSize(true);
        rv2.setHasFixedSize(true);
        rv3.setHasFixedSize(true);

        layoutManager1 = new CustomManager(this);
        layoutManager1.setScrollEnabled(false);
        rv1.setLayoutManager(layoutManager1);
        layoutManager2 = new CustomManager(this);
        layoutManager2.setScrollEnabled(false);
        rv2.setLayoutManager(layoutManager2);
        layoutManager3 = new CustomManager(this);
        layoutManager3.setScrollEnabled(false);
        rv3.setLayoutManager(layoutManager3);

        rv1.setAdapter(adapter);
        rv2.setAdapter(adapter);
        rv3.setAdapter(adapter);
        rv1.scrollToPosition(position1);
        rv2.scrollToPosition(position2);
        rv3.scrollToPosition(position3);

        setText();
        updateText();

        //RecyclerView listeners
        rv1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    rv1.scrollToPosition(gameLogic.getPosition(0));
                    layoutManager1.setScrollEnabled(false);
                }
            }
        });

        rv2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    rv2.scrollToPosition(gameLogic.getPosition(1));
                    layoutManager2.setScrollEnabled(false);
                }
            }
        });
        rv3.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    rv3.scrollToPosition(gameLogic.getPosition(2));
                    layoutManager3.setScrollEnabled(false);
                    updateText();
                    if (gameLogic.getHasWon()) {
                        if (playsound == 1) {
                            win.start();
                        }
                        View rootView = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(rootView, "", 1800);

                        View customSnackbarView = getLayoutInflater().inflate(R.layout.winner, null);

                        TextView winCoins = customSnackbarView.findViewById(R.id.win_coins);
                        winCoins.setText(gameLogic.getPrize());

                        snackbar.getView().setBackgroundColor(getResources().getColor(android.R.color.transparent, getTheme()));

                        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                        snackbarLayout.addView(customSnackbarView, 0);

                        snackbar.show();

                    }
                    spinButton.setEnabled(true);
                }
            }
        });

        //Button listeners
        spinButton.setOnClickListener(v -> {
            spinButton.setEnabled(false);
            if(playsound == 1){
                mP.start();
            }
            layoutManager1.setScrollEnabled(true);
            layoutManager2.setScrollEnabled(true);
            layoutManager3.setScrollEnabled(true);
            gameLogic.getSpinResults();
            position1 = gameLogic.getPosition(0) + COEF;
            position2 = gameLogic.getPosition(1) + COEFW;
            position3 = gameLogic.getPosition(2) + COEFE;
            rv1.smoothScrollToPosition(position1);
            rv2.smoothScrollToPosition(position2);
            rv3.smoothScrollToPosition(position3);
        });

        plusButton.setOnClickListener(v -> {
            if(playsound == 1){
                mP.start();
            }
            gameLogic.betUp();
            updateText();
        });

        minusButton.setOnClickListener(v -> {
            if(playsound == 1){
                mP.start();
            }
            gameLogic.betDown();
            updateText();
        });

        settingsButton.setOnClickListener(v -> {
            if(playsound == 1){
                mP.start();
            }
            showSettingsDialog();
        });
    }

    private void setText(){
        if(firstRun){
            gameLogic.setMyCoins(1000);
            gameLogic.setBet(5);
            gameLogic.setJackpot(100000);

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

        }else {
            String coins = pref.getString("coins","");
            String stellationbet = pref.getString("bet","");
            String jackpot = pref.getString("jackpot","");
            Log.d("COINS",coins);
            myCoinsval = Integer.parseInt(coins);
            betVal = Integer.parseInt(stellationbet);
            jackpotVal = Integer.parseInt(jackpot);
            gameLogic.setMyCoins(myCoinsval);
            gameLogic.setBet(betVal);
            gameLogic.setJackpot(jackpotVal);
        }
    }

    private void updateText() {
        energyBallPrice.setText(gameLogic.getJackpot());
        myPower.setText(gameLogic.getMyCoins());
        bet.setText(gameLogic.getBet());

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("coins",gameLogic.getMyCoins());
        editor.putString("bet",gameLogic.getBet());
        editor.putString("jackpot",gameLogic.getJackpot());
        editor.apply();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView pic;

        public ItemViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.spinner_item);
        }
    }

    private class SpinnerAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StellationMain.this);
            View view = layoutInflater.inflate(R.layout.spin_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            int i = position < 7 ? position : position % COMBONUMBER;
            switch (slot[i]) {
                case 1:
                    holder.pic.setImageResource(R.drawable.combination_1);
                    break;
                case 2:
                    holder.pic.setImageResource(R.drawable.combination_2);
                    break;
                case 3:
                    holder.pic.setImageResource(R.drawable.combination_3);
                    break;
                case 4:
                    holder.pic.setImageResource(R.drawable.combination_4);
                    break;
                case 5:
                    holder.pic.setImageResource(R.drawable.combination_5);
                    break;
                case 6:
                    holder.pic.setImageResource(R.drawable.combination_6);
                    break;
                case 7:
                    holder.pic.setImageResource(R.drawable.combination_7);
                    break;
                default:
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
    }

    private void showSettingsDialog() {
        final Dialog dialog;

        dialog = new Dialog(this, R.style.WinDialog);
        Objects.requireNonNull(dialog.getWindow()).setContentView(R.layout.settings);

        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        ImageView close = dialog.findViewById(R.id.close);
        close.setOnClickListener(v -> dialog.dismiss()); // Close the dialog when the close button is clicked

        musicOn = dialog.findViewById(R.id.music_on);
        musicOn.setOnClickListener(v -> {
            playmusic = 0;
            checkmusic();
            musicOn.setVisibility(View.INVISIBLE);
            musicOff.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("music", playmusic);
            editor.apply();
        });

        musicOff =  dialog.findViewById(R.id.music_off);
        musicOff.setOnClickListener(v -> {
            playmusic = 1;
            bgsound.start();
            dialog.show();
            checkmusic();
            musicOn.setVisibility(View.VISIBLE);
            musicOff.setVisibility(View.INVISIBLE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("music", playmusic);
            editor.apply();
        });

        soundon = dialog.findViewById(R.id.sounds_on);
        soundon.setOnClickListener(v -> {
            playsound = 0;
            checksound();
            soundon.setVisibility(View.INVISIBLE);
            soundoff.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("sound", playsound);
            editor.apply();
        });

        soundoff = dialog.findViewById(R.id.sounds_off);
        soundoff.setOnClickListener(v -> {
            playsound = 1;
            dialog.show();
            checksound();
            soundon.setVisibility(View.VISIBLE);
            soundoff.setVisibility(View.INVISIBLE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("sound", playsound);
            editor.apply();
        });

        checkmusicdraw();
        checksounddraw();

        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        bgsound.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkmusic();
    }

    private void checkmusic(){
        if (playmusic == 1){
            bgsound.start();
        }
        else {
            bgsound.pause();
        }
    }

    private void checksound(){
        if(playsound == 1){
            bgsound.start();
        }
        else{
            bgsound.pause();
        }
    }

    private void checkmusicdraw(){
        if (playmusic == 1){
            musicOn.setVisibility(View.VISIBLE);
            musicOff.setVisibility(View.INVISIBLE);
        }
        else {
            musicOn.setVisibility(View.INVISIBLE);
            musicOff.setVisibility(View.VISIBLE);
        }
    }

    private void checksounddraw(){
        if (playsound == 1){
            soundon.setVisibility(View.VISIBLE);
            soundoff.setVisibility(View.INVISIBLE);
        }
        else {
            soundon.setVisibility(View.INVISIBLE);
            soundoff.setVisibility(View.VISIBLE);
        }
    }
}