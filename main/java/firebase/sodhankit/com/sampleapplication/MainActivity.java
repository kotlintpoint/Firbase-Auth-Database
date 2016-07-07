package firebase.sodhankit.com.sampleapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    EditText etName,etNumber;
    Button btnSave;
    ListView listview;
    ArrayList<User> userList;
    ArrayAdapter<User> adapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        listview=(ListView)findViewById(R.id.listview);
        userList=new ArrayList<>();
        adapter=new ArrayAdapter<User>(MainActivity.this,android.R.layout.simple_list_item_1,
                userList);
        listview.setAdapter(adapter);

        etName=(EditText)findViewById(R.id.etName);
        etNumber=(EditText)findViewById(R.id.etNumber);

        btnSave=(Button)findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random random = new Random();

                String name=etName.getText().toString();
                String number=etNumber.getText().toString();

                String id=Integer.toString(random.nextInt(100));

                User user=new User(name, number);
                //mDatabase.child("users").child(id).setValue(user);
                mDatabase.child("users").push().setValue(user);
            }
        });

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                HashMap<String,HashMap<String,HashMap<String,String>>> hashMap = (HashMap<String,HashMap<String,HashMap<String,String>>>) dataSnapshot.getValue();
                HashMap<String,HashMap<String,String>> hashMap1=hashMap.get("users");
                Log.i("TAG",hashMap.size()+"");
                Set<String> keySet=hashMap1.keySet();
                userList.clear();
                for (String key:keySet) {
                    HashMap<String,String> hashMap2=hashMap1.get(key);
                    User user=new User(hashMap2.get("name"),hashMap2.get("number"));
                    userList.add(user);
                    adapter.notifyDataSetChanged();
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        mDatabase.addValueEventListener(postListener);
    }

}
