package algonquin.cst2335.haiyan0348.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.haiyan0348.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment {


    ChatMessage selected;

    public MessageDetailsFragment(ChatMessage m)
    {
        selected = m;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater, container, false);

        binding.messageText.setText( selected.message );
        binding.timeText.setText( selected.timeSent );
        binding.isSentOrReceive.setText( String.valueOf(selected.sendOrReceive) );
        binding.idText.setText( "Id = " + selected.id );

        return binding.getRoot();
    }
}
