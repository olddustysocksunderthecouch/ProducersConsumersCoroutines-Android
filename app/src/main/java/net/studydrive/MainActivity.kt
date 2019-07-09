package net.studydrive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {

    var counter = 10
    private val counterLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private val itemListLiveData: MutableLiveData<List<ItemModel>> = MutableLiveData()
    private lateinit var mAdapter: ItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        counterLiveData.value = 1
        itemListLiveData.value = List(1) { ItemModel("", 1) }

        val counterContext = newSingleThreadContext("CounterContext")

        producer_button.setOnClickListener {
            addJob(counterContext)
        }

        consumer_button.setOnClickListener {
            consumerJob(counterContext)
        }

        observeMutable(counterLiveData){
            it ?: return@observeMutable
            counter_textview.text = it.toString()
        }

        mAdapter = ItemAdapter(this@MainActivity, listOf())
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            (layoutManager as LinearLayoutManager).reverseLayout = true
            adapter = mAdapter
        }


        observe(itemListLiveData) {
            it ?: return@observe
            mAdapter.items = it
            recyclerview.smoothScrollToPosition(mAdapter.itemCount-1)

        }

    }


    private fun addJob(thread: ExecutorCoroutineDispatcher) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                repeat(200) {
                    withContext(thread) {
                            println("Counter = $counter")
                        withContext(Dispatchers.Main) {
                            counterLiveData.value = counter
                            val itemList = itemListLiveData.value
                            itemListLiveData.value = itemList?.plus( ItemModel("", counter))
                        }
                            counter++
                        }
                    delay(4000)
                }
            }
        }
    }

    private fun consumerJob(thread: ExecutorCoroutineDispatcher) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                repeat(200) {
                    withContext(thread) {
                        println("Counter = $counter")
                        withContext(Dispatchers.Main) {
                            counterLiveData.value = counter
                            val itemList = itemListLiveData.value
                            itemListLiveData.value = itemList?.subList(0, itemList.size.minus(1))
                        }

                        counter--
                    }
                    delay(3000)
                }
            }
        }
    }

}
