package algonquin.cst2335.haiyan0348.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import algonquin.cst2335.haiyan0348.R;
import algonquin.cst2335.haiyan0348.data.ChatRoomViewModel;
import algonquin.cst2335.haiyan0348.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.haiyan0348.databinding.ReceiveMessageBinding;
import algonquin.cst2335.haiyan0348.databinding.SentMessageBinding;


public class ChatRoom extends AppCompatActivity {

    private ArrayList<ChatMessage> messages;
    private ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChatRoomViewModel chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        ;

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the ViewModel ArrayList
        messages = chatModel.getChatMessages().getValue();

        if (messages == null) {
            messages = new ArrayList<>();
            chatModel.setChatMessages(messages);
        }

        binding.sendButton.setOnClickListener(click -> {

            String typed = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(typed, currentDateandTime, true);
            messages.add(chatMessage);


            //notify the adapter:
            myAdapter.notifyItemInserted(messages.size() - 1);//redraw the missing row

            binding.textInput.setText("");//remove what was typed
        });

        binding.receiveButton.setOnClickListener(click -> {
            String typed = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(typed, currentDateandTime, false);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
        });



        binding.theRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //viewType is 0 or 1 based on getItemViewType(int position)
                if(viewType == 0) {
                    SentMessageBinding rowBinding = SentMessageBinding .inflate(getLayoutInflater(), parent, false);

                    //this will initialize the row variables:
                    return new MyRowHolder(rowBinding.getRoot());

                }else{
                    ReceiveMessageBinding messageBinding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);

                    return new MyRowHolder(messageBinding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                ChatMessage chatMessage = messages.get(position);
                //override the text in the rows:
                holder.bindMessage(chatMessage);


            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position){
                //which layout to use for object at position?
                ChatMessage chatMessage = messages.get(position);
                return chatMessage.isSentButton() ? 0 : 1;
            }
        });

        binding.theRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    class MyRowHolder extends RecyclerView.ViewHolder {

        private TextView message;
        private TextView time;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
        }


        public void bindMessage(ChatMessage chatMessage) {
            message.setText(chatMessage.getMessage());
            time.setText(chatMessage.getTimeSent());
        }
    }


}