package sylex.androidClient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import sylex.model.ClientModel;

public class NewGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
    }

    public void onClickConfirm(View view) {
        EditText gameNameText = (EditText) findViewById(R.id.gameName);
        String gameName = gameNameText.getText().toString();
        Spinner playerCountSpin = (Spinner) findViewById(R.id.playerCount);
        byte playerCount = Byte.parseByte(playerCountSpin.getSelectedItem().toString());
        Spinner levelSpin = (Spinner) findViewById(R.id.level);
        int level = Integer.parseInt(levelSpin.getSelectedItem().toString());
        Log.d("D", "prenew\n");
        ClientModel.sendNew(gameName, playerCount, level);
        Log.d("D", "postnew\n");
        this.finish();
        //Log.d("D", "finished\n");
    }

    public void onClickCancel(View view) {
        this.finish();
    }

}
