package net.studydrive

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*


class MainActivity : AppCompatActivity() {

    var counter = 0
    private val counterLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val itemListLiveData: MutableLiveData<List<ItemModel>> = MutableLiveData()
    private lateinit var mAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        counterLiveData.value = 0
        itemListLiveData.value = listOf()

        producer_button.setOnClickListener {
            producerJob()
        }

        consumer_button.setOnClickListener {
            consumerJob()
        }

        observeMutable(counterLiveData) {
            it ?: return@observeMutable
            counter_textview.text = it.toString()
        }

        mAdapter = ItemAdapter(listOf())
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            (layoutManager as LinearLayoutManager).reverseLayout = true
            adapter = mAdapter
        }


        observe(itemListLiveData) {
            it ?: return@observe
            mAdapter.items = it
            if(mAdapter.itemCount > 0) {
                recyclerview.smoothScrollToPosition(mAdapter.itemCount - 1)
            }
        }

    }


    private fun producerJob() {
        GlobalScope.launch {
            // Launch a new coroutine in background and continue
            // The Producer and Consumer must use the same coroutine scope or they won't be able to access the same counter variable
            repeat(2000) {
                Log.d("Counter: ", counter.toString())
                withContext(Dispatchers.Main) {
                    // Updates to livedata must be done on the main thread as they are observed on the the main thread
                    counterLiveData.value = counter
                    val itemList = itemListLiveData.value
                    itemListLiveData.value =
                        itemList?.plus(ItemModel(Date(System.currentTimeMillis()).toString(), counter))
                }
                counter++
                delay(3000)
                // non-blocking delay
                // this is crucial because it means the thread can be used by another coroutine while waiting for this delay to complete
                // this makes makes it very efficient at high numbers of producers and consumers
            }
        }

    }

    private fun consumerJob() {
        GlobalScope.launch {
            repeat(2000) {
                Log.d("Counter: ", counter.toString())
                withContext(Dispatchers.Main) {
                    counterLiveData.value = counter
                    val itemList = itemListLiveData.value
                    if(itemList != null && itemList.isNotEmpty()){
                        itemListLiveData.value = itemList.subList(0, itemList.size.minus(1))
                    } else {
                        Toast.makeText(this@MainActivity, "List can't have less than 0 items", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                counter--
                delay(4000)
            }
        }
    }

}
