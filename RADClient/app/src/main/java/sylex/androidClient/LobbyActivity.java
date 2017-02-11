package sylex.androidClient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import sylex.androidClient.interfaces.RoomEventReceiver;
import sylex.model.ClientModel;

public class LobbyActivity extends AppCompatActivity implements RoomEventReceiver {

    private ListView gameUserList;
    private ArrayAdapter adapter;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        gameUserList = (ListView) findViewById(R.id.GameUserList);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ClientModel.clientsInGame);
        gameUserList.setAdapter(adapter);
        ClientModel.bindRoomEvent(this);
    }

    @Override
    public void onGameUserListChanged() {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.d("D", "UPDATE USER LIST : "+ClientModel.clientsInGame.toString()+"\n\n");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                adapter.clear();
                adapter.addAll(ClientModel.clientsInGame);
//                gameUserList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

        @Override
    public void onJoinGameOk() {

    }

    @Override
    public void onJoinGameBad() {

    }
}
