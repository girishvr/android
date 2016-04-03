package com.bitjini.myfirstprogram;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by girish on 03/04/16.
 */
public class FreshPage extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //create an object of widget refering to xml file
        final ListView listView=(ListView)findViewById(R.id.list);

        //create an array
        String [] values={"Android","ios","web"};

        // Create an arrayadapter
        // 1st param = Context
        // 2nd param = layout
        // 3rd param = id of textview
        //  4th param = array

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(FreshPage.this,
                R.layout.list_items,R.id.listitem,values);

        // Assign adapter to listView
        listView.setAdapter(adapter);

       // setonclick listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                int item_position=position;

                String item=(String)listView.getItemAtPosition(position);

                Toast.makeText(FreshPage.this," itemPosition is :"+item_position
                        +":" + item,
                        Toast.LENGTH_LONG).show();

            }
        });



        }
    }
