package mobiledev.unb.ca.roompersistencelab

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import mobiledev.unb.ca.roompersistencelab.db.AppDatabase
import mobiledev.unb.ca.roompersistencelab.repositories.ItemRepository
import mobiledev.unb.ca.roompersistencelab.ui.ItemListAdapter
import mobiledev.unb.ca.roompersistencelab.ui.ItemViewModel
import mobiledev.unb.ca.roompersistencelab.ui.ItemViewModelFactory
import kotlin.getValue

class MainActivity : AppCompatActivity() {
    // Attribute for the item view model
    private val itemViewModel: ItemViewModel by viewModels {
        // Use the factory lambda to inject the repository directly
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ItemRepository(database.itemDao())

        // Return the configured factory
        ItemViewModelFactory(repository)
    }
    // Attribute for item list adapter used for the results
    private lateinit var itemAdapter: ItemListAdapter

    // Attributes for handling the search field
    private lateinit var searchView: SearchView
    private lateinit var resultsCountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Force light mode when running the app
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Initialize UI view binds from XML layout
        searchView = findViewById(R.id.search_view)
        resultsCountTextView = findViewById(R.id.results_count_text_view)

        // Wire up text change listeners to the search element
        setupSearchFilter()

        // Set the RecyclerView for use
        setupRecyclerView()

        // Setup listener to observe state changes
        observeUiState()

        // Add the floating action button on click listener
        val fab: FloatingActionButton = findViewById(R.id.fab_add)
        fab.setOnClickListener {
            showAddItemDialog()
        }
    }

    private fun setupRecyclerView() {
        itemAdapter = ItemListAdapter()

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                itemViewModel.searchItems.collect { items ->
                    // TODO
                    //  Update the resultsCountTextView text to indicate how many records have been found

                    // TODO
                    //  Push the list of items into itemAdapter using submitList()
                }
            }
        }
    }

    private fun setupSearchFilter() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Return true to handle keyboard submission action manually if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Triggers automatically on every single keystroke change which will
                // update the searchItems flow in the view model which triggers
                // an update to the observer defined above
                // TODO
                //  If the newText value is null
                //    Hide the results TextView and make a call to the view model
                //    to reset the search list.
                //  Else
                //    Show the results TextView and make a call to the view model to search
                //    for records in the database that match the query item.
                //    NOTE: This will update the searchItems variable in the view model which
                //    will trigger an update to the observer defined above.
                return true
            }
        })
    }

    fun showAddItemDialog() {
        // Clear the search view text completely without submitting a query
        searchView.setQuery("", false)

        // Inflate the dialog view XML layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null)

        // Reference views from the inflated layout
        val editName: TextInputEditText = dialogView.findViewById(R.id.edit_item_name)
        val editNumber: TextInputEditText = dialogView.findViewById(R.id.edit_item_number)

        // Build and show the dialog wrapper
        val alertDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Set button click listeners
        val btnCancel: Button = dialogView.findViewById(R.id.btn_cancel)
        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        val btnAdd: Button = dialogView.findViewById(R.id.btn_add)
        btnAdd.setOnClickListener {
            // TODO
            //  Check if some text has been entered in both the item and number TextInputEditText fields (i.e. - not empty).
            //  If not indicating that the data entered was incomplete.
            //  HINT:
            //    Have a look TextInputEditText public attributes to see what is available for showing an error message

            // TODO
            //  Call the addItem method using the the text from these EditTexts.

            // Close the dialog when complete
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    private fun addItem(name: String, number: String) {
        // TODO
        //  Make a call to the view model to create a record in the database table
        //  and show a toast message indicating a successful insert
        //  You will also need to write a bit of extra code to get the
        //  UI to behave nicely, e.g., showing and hiding the keyboard
        //  at the right time, clearing text fields appropriately.
        //  Some of that code will likely go here, but you might also make
        //  changes elsewhere in the app. Exactly how you make the
        //  UI behave is up to you, but you should make reasonable
        //  choices.
        //  HINT:
        //    There is a utility object called KeyboardUtils which may be helpful here
    }
}