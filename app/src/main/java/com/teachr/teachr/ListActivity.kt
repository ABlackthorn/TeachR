package com.teachr.teachr

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.Window
import android.view.WindowManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import java.time.LocalDateTime
import com.google.firebase.auth.FirebaseAuth


class ListActivity : Activity() {

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

       list.add(Entry(1, "13/02", 2, 10, 24, 20,
            Subject(1,"Mathématiques"), "Wolfgang Amadeus Mozart", 1))
        list.add(Entry(1, "13/02", 2, 10, 24, 20,
                Subject(1,"Mathématiques"), "Wolfgang Amadeus Mozart", 1))
        list.add(Entry(1, "13/02", 2, 10, 24, 20,
                Subject(1,"Mathématiques"), "Wolfgang Amadeus Mozart", 1))
        list.add(Entry(1, "13/02", 2, 10, 24, 20,
                Subject(1,"Mathématiques"), "Wolfgang Amadeus Mozart", 1))

        /*
        val filterButton = findViewById<Button>(R.id.filterButton);
            filterButton.setOnClickListener{
                var intent : Intent = Intent(this, ListActivity::class.java);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
            val fab: View = findViewById(R.id.fab)
            fab.setOnClickListener { view ->
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
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

}