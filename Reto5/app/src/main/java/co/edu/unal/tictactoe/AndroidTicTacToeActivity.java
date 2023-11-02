package co.edu.unal.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import edu.harding.tictactoe.BoardView;
import edu.harding.tictactoe.TicTacToeGame;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    private TicTacToeGame mGame;
    private BoardView mBoardView;
    // Buttons making up the board
    private Button mBoardButtons[];
    // Various text displayed
    private TextView mInfoTextView;
    private Button mnewGameBttn;
    private static boolean mGameOver;
    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;

    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;
    MediaPlayer mWinnerMediaPlayer;

    public void setGame(TicTacToeGame game) {
        mGame = game;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);*/
        mInfoTextView = (TextView) findViewById(R.id.information);

        mGame = new TicTacToeGame();
        mBoardView = findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);
        //mnewGameBttn = findViewById(R.id.newGameBtn);
        //mnewGameBttn.setOnClickListener(new NewGameButtonClickListener());
        startNewGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};
                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.
                int selected = 0;
                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss(); // Close dialog
                                // TODO: Set the diff level of mGame based on which item was selected.
                                if (item == 0)
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                else if (item == 1)
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                else if (item == 2){
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                }
                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();

                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AndroidTicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }

    private void startNewGame() {
        mGameOver = false;
        mGame.clearBoard();
        mBoardView.invalidate();
        mInfoTextView.setText(R.string.human_first);
        /*for(int i=0; i<mBoardButtons.length; i++){
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }*/
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int location = row * 3 + col;
            if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, location)){
                mHumanMediaPlayer.start(); // Play the sound effect
                // If no winner yet, let the computer make a move

                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.android_turn);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            int move = mGame.getComputerMove();
                            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                            mBoardView.invalidate();
                            mComputerMediaPlayer.start();    // Play the sound effect
                            int winner = mGame.checkForWinner();

                            if (winner != 0){
                                AndroidTicTacToeActivity.mGameOver = true;
                                mWinnerMediaPlayer.start();
                            }

                            if (winner == 0)
                                mInfoTextView.setText(R.string.human_turn);
                            else if (winner == 1)
                                mInfoTextView.setText(R.string.tie_result);
                            else if (winner == 2)
                                mInfoTextView.setText(R.string.human_wins_result);
                            else
                                mInfoTextView.setText(R.string.android_wins_result);
                        }
                    }, 1000);
                }

                if (winner != 0){
                    AndroidTicTacToeActivity.mGameOver = true;
                    mWinnerMediaPlayer.start();
                }

                if (winner == 0)
                    mInfoTextView.setText(R.string.human_turn);
                else if (winner == 1)
                    mInfoTextView.setText(R.string.tie_result);
                else if (winner == 2)
                    mInfoTextView.setText(R.string.human_wins_result);
                else
                    mInfoTextView.setText(R.string.android_wins_result);
            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    private boolean setMove(char player, int location) {
        if (mGame.setMove(player, location)) {
            mBoardView.invalidate(); // Redraw the board
            return true;
        }
        return false;
        /*if (!mGameOver){
            mGame.setMove(player, location);
            mBoardButtons[location].setEnabled(false);
            mBoardButtons[location].setText(String.valueOf(player));
            if (player == TicTacToeGame.HUMAN_PLAYER)
                mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
            else
                mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
        }*/
    }

    /*private class NewGameButtonClickListener implements View.OnClickListener {
        public NewGameButtonClickListener(){}
        public void onClick(View view) {
            startNewGame();
        }
    }*/

    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location) {
            this.location = location;
        }
        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled()) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);
                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.android_turn);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                if (winner != 0)
                    AndroidTicTacToeActivity.mGameOver = true;

                if (winner == 0)
                    mInfoTextView.setText(R.string.human_turn);
                else if (winner == 1)
                    mInfoTextView.setText(R.string.tie_result);
                else if (winner == 2)
                    mInfoTextView.setText(R.string.human_wins_result);
                else
                    mInfoTextView.setText(R.string.android_wins_result);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human_media);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.android_media);
        mWinnerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.winner_media);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }




}

