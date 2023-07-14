package algonquin.cst2335.haiyan0348.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.haiyan0348.R;
import algonquin.cst2335.haiyan0348.data.ChatRoomViewModel;
import algonquin.cst2335.haiyan0348.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.haiyan0348.databinding.ReceiveMessageBinding;
import algonquin.cst2335.haiyan0348.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    private ArrayList<ChatMessage> messages;
    private ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;

    ChatMessageDAO mDAO;

    ChatRoomViewModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FrameLayout fragmentLocation = findViewById( R.id.fragmentLocation);
        boolean IAmTablet = fragmentLocation != null;

        model = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        model.selectedMessage.observe(this, newChatMessage -> {

            if(newChatMessage != null) {
                FragmentManager fMgr = getSupportFragmentManager();

                FragmentTransaction tx = fMgr.beginTransaction();

                //what to show:
                MessageDetailsFragment chatFragment = new MessageDetailsFragment(newChatMessage );

                //where to load:
                tx.add(R.id.fragmentLocation, chatFragment);
                tx.addToBackStack(null);
                tx.commit();//this will show it
            }
        });



        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        // Initialize the ViewModel ArrayList
        messages = model.theWords;



        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() ->
        {
            List<ChatMessage> allMessages = mDAO.getAllMessages();

            messages.addAll(allMessages);
        });


        binding.sendButton.setOnClickListener(click -> {
            String typed = binding.textInput.getText().toString();

            int type = 1;

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");
            String currentDateandTime = sdf.format(new Date());

            ChatMessage chatMessage = new ChatMessage(typed, currentDateandTime, type);

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() ->{
                chatMessage.id = mDAO.insertMessage(chatMessage);//add to database;
                /*this runs in another thread*/
            });

            //insert into ArrayList
            messages.add(chatMessage);

            myAdapter.notifyDataSetChanged(); //updates the rows

            binding.textInput.setText("");//remove what was typed
        });


        binding.receiveButton.setOnClickListener(click -> {
            int type = 0;
            String typed = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(typed, currentDateandTime, type);

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() ->{
                chatMessage.id = mDAO.insertMessage(chatMessage);//add to database;
                /*this runs in another thread*/
            });

            messages.add(chatMessage);
            myAdapter.notifyDataSetChanged(); //updates the rows
            binding.textInput.setText("");
        });



        binding.theRecyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //viewType is 0 or 1 based on getItemViewType(int position)
                if(viewType == 1) {
                    // Sent message
                    SentMessageBinding binding = SentMessageBinding .inflate(getLayoutInflater(), parent, false);
                    //this will initialize the row variables:
                    return new MyRowHolder(binding.getRoot());
                }
                else
                {
                    // Received message
                    ReceiveMessageBinding binding  = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                ChatMessage atThisRow = messages.get(position);
                //override the text in the rows:
                holder.bindMessage(atThisRow);
            }

            @Override
            public int getItemCount() {
                //how many rows there are:
                return messages.size();
            }


            @Override
            public int getItemViewType(int position){

                //even
                if (position % 2 == 0)
                    // Received message
                    return 1;
                else //odd
                    // Sent message
                    return 2;
            }
        });

        binding.theRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    class MyRowHolder extends RecyclerView.ViewHolder {

        private TextView message;
        private TextView time;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener( click -> {

                int index = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(index);

                model.selectedMessage.postValue(selected);


                //RecyclerView.ViewHolder newRow = myAdapter.onCreateViewHolder(null, myAdapter.getItemViewType(position));


               /* AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );

                builder.setMessage("Do you want to delete this?");
                builder.setTitle("Question");
                builder.setNegativeButton("No", (dialog, cl)->{ *//*Hide the dialog, do nothing*//*});
                builder.setPositiveButton("Yes",(dialog, cl)->{

                    //what is the index?
                    int position = getAbsoluteAdapterPosition();

                    ChatMessage toDelete = messages.get(position);

                    Executor thread1 = Executors.newSingleThreadExecutor();
                    thread1.execute(() ->{
                            mDAO.deleteMessage(toDelete);
                            messages.remove(position);//remove from our array list
                            //myAdapter.notifyItemRemoved(position);

                        //must be done on the main UI thread
                        runOnUiThread(() -> {  myAdapter.notifyDataSetChanged(); });

                    Snackbar.make( message, "Deleted your message #"+ position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk ->{
                                Executor myTHread = Executors.newSingleThreadExecutor();
                                myTHread.execute(() -> {
                                            mDAO.deleteMessage(toDelete);
                                            messages.add(position, toDelete);
                                            runOnUiThread( () ->  myAdapter.notifyDataSetChanged());
                                });
                            })
                            .show();

                    });
                });
                builder.create().show(); */
            });

            //THis holds the message Text:
            message = itemView.findViewById(R.id.message);
            //This holds the time text
            time = itemView.findViewById(R.id.time);
        }


        public void bindMessage(ChatMessage chatMessage) {
            message.setText(chatMessage.getMessage());
            time.setText(chatMessage.getTimeSent());


        }
    }


}