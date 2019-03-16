package com.teachr.teachr

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import values.Statics


class ListActivity : Activity() {
    private lateinit var _db: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager

    // private val myDataset = mutableListOf<Subject>()
    val list = ArrayList<Entry>()

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

        _db = FirebaseDatabase.getInstance().reference

        list.add(Entry("", "13/02", 2, Geopoint(45.494354, -73.561818), 24,
            Subject(1,"Mathématiques"), "Wolfgang Amadeus Mozart", 1))

        val filterButton = findViewById<Button>(R.id.filterButton);
            filterButton.setOnClickListener{
                //var intent : Intent = Intent(this, ListActivity::class.java);
                //startActivity(intent,
                        //ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }

       /* val fab: View = findViewById<FloatingActionButton>(R.id.fab)
            fab.setOnClickListener { view ->;
                addEntry()
            }
*/
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
    }

    override fun onStart() {
        super.onStart()
    }

    private fun addEntry() {
        // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        var entry = Entry("", "13/02", 2, Geopoint(45.494354, -73.561818), 24,
                Subject(1,"Mathématiques"), "Wolfgang Amadeus Mozart", 1);
        //Get the object id for the new task from the Firebase Database
        val newEntry = _db.child(Statics.FIREBASE_ENTRY).push()
        entry.id = newEntry.key

        //Set the values for new task in the firebase using the footer form
        newEntry.setValue(entry)
        list.add(entry)
        Toast.makeText(this, "Task added to the list successfully" + entry.id, Toast.LENGTH_SHORT).show()
    }
}