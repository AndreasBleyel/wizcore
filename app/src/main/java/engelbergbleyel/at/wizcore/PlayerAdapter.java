package engelbergbleyel.at.wizcore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by andi on 30.11.16.
 */
public class PlayerAdapter extends ArrayAdapter<Player> {

    public PlayerAdapter(Context context, ArrayList<Player> players){
        super(context,0,players);
    }


/*    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Player player = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_player,parent,false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        tvName.setText(player.getName());

        return convertView;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Player player = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_player,parent,false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(player.getName());

        return convertView;
    }
}
