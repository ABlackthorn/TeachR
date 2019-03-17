package com.teachr.teachr

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import values.Statics


class ListActivity : AppCompatActivity() {
    private lateinit var _db: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager

    var _entryListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            loadEntryList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    // private val myDataset = mutableListOf<Subject>()
    private val list: MutableList<Entry> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getSupportActionBar()?.hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_list)
        val mAuth = FirebaseAuth.getInstance()
        findViewById<Button>(R.id.button).setOnClickListener{
            mAuth.signOut()
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        _db = FirebaseDatabase.getInstance().reference.child("entry")

        val filterButton = findViewById<Button>(R.id.filterButton);
            filterButton.setOnClickListener{
                //var intent : Intent = Intent(this, ListActivity::class.java);
                //startActivity(intent,
                        //ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
        val fab: View = findViewById<FloatingActionButton>(R.id.fab)
            fab.setOnClickListener { view ->;
                addEntry()
            }
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewAdapter = RecyclerAdapter(this, list)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView

            // use a linear layout manager
            layoutManager = linearLayoutManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
        _db.orderByKey().addValueEventListener(_entryListener)
    }

    override fun onStart() {
        super.onStart()
    }

    private fun loadEntryList(dataSnapshot: DataSnapshot) {
        Log.d("MainActivity", "loadEntryList")

        val entries = dataSnapshot.children.iterator()
        //Check if current database contains any collection
        if (entries.hasNext()) {

            list!!.clear()


            val listIndex = entries.next()
            val itemsIterator = listIndex.children.iterator()

            //check if the collection has any task or not
            while (itemsIterator.hasNext()) {

                //get current task
                val currentItem = itemsIterator.next()

                //get current data in a map
                val map = currentItem.getValue() as HashMap < String, Any >;
                val subject = map.get("subject") as HashMap < String, Any >
                Log.d("oo", subject.toString())
                //key will return the Firebase ID
                val entry = Entry(currentItem.key,
                        map.get("date") as String?, map.get("duration") as Long?,
                        map.get("latitude") as Double?, map.get("longitude") as Double?, map.get("price") as Long?, map.get("subject") as String?,
                        map.get("user") as String?, map.get("type") as Long?)
                list!!.add(entry)
            }
        }

        //alert adapter that has changed
        viewAdapter.notifyDataSetChanged()

    }

    private fun addEntry() {
        // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        var entry = Entry("", "13/02", 2, 45.494354, -73.561818, 24, "", "Wolfgang Amadeus Mozart", 1);
        //Get the object id for the new task from the Firebase Database

        val newEntry = _db.child(Statics.FIREBASE_ENTRY).push()
        entry.id = newEntry.key

        //Set the values for new task in the firebase using the footer form
        newEntry.setValue(entry)
        list.add(entry)
        Toast.makeText(this, "Task added to the list successfully" + entry.id, Toast.LENGTH_SHORT).show()
    }
}