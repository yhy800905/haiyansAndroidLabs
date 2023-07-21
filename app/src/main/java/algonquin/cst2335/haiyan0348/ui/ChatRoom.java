package algonquin.cst2335.haiyan0348.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.haiyan0348.data.ChatRoomViewModel;
import algonquin.cst2335.haiyan0348.R;
import algonquin.cst2335.haiyan0348.data.ChatRoomViewModel;
import algonquin.cst2335.haiyan0348.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.haiyan0348.databinding.ReceiveMessageBinding;
import algonquin.cst2335.haiyan0348.databinding.SentMessageBinding;

import algonquin.cst2335.haiyan0348.ui.ChatMessageDAO;



public class ChatRoom extends AppCompatActivity {

    private ArrayList<ChatMessage> messages;
    private ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;
    ChatMessageDAO mDAO;
    ChatRoomViewModel model;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);// add your toolbar, onCreateOptionsMenu()

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

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name")
                .fallbackToDestructiveMigration() // This allows for destructive migration
                .build();
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

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        String message = "";
        if( item.getItemId() == R.id.id_delete )
            deleteSelectedChatMessage();
            //put your ChatMessage deletion code here. If you select this item, you should show the alert dialog
            //asking if the user wants to delete this message.
        else if ( item.getItemId() == R.id.id_about)
            Toast.makeText(this, "Version 1.0, created by Haiyan", Toast.LENGTH_SHORT).show();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    private void deleteSelectedChatMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
        int position=0;
        builder.setMessage("Do you want to delete this?");
        builder.setTitle("Question");
        builder.setNegativeButton("No", (dialog, cl)->{ });
        builder.setPositiveButton("Yes",(dialog, cl)->{
            ChatMessage toDelete = messages.get(position);

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() ->{
                mDAO.deleteMessage(toDelete);
                messages.remove(position);//remove from our array list
                //myAdapter.notifyItemRemoved(position);

                //must be done on the main UI thread
                runOnUiThread(() -> {  myAdapter.notifyDataSetChanged(); });

                Snackbar.make( binding.getRoot(), "Deleted your message #"+ position, Snackbar.LENGTH_LONG)
                        .setAction("Undo", clk ->{
                            Executor myThread = Executors.newSingleThreadExecutor();
                            myThread.execute(() -> {
                                mDAO.deleteMessage(toDelete);
                                messages.add(position, toDelete);
                                runOnUiThread( () ->  myAdapter.notifyDataSetChanged());
                            });
                        })
                        .show();
            });
        });
        builder.create().show();
    }


    class MyRowHolder extends RecyclerView.ViewHolder {

        private final TextView message;
        private final TextView time;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener( click -> {

                int index = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(index);

                model.selectedMessage.postValue(selected);
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