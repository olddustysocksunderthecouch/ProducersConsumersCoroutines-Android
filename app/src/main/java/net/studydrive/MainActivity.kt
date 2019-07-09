package net.studydrive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {

    var counter = 10
    private val counterLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private lateinit var mAdapter: ItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        counterLiveData.value = 10

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
            adapter = mAdapter
        }

        observe(counterLiveData) {
            it ?: return@observe
            mAdapter.items = it

        }


    }


    fun addJob(thread: ExecutorCoroutineDispatcher) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                repeat(200) {
                    withContext(thread) {
                            println("Counter = $counter")
                        withContext(Dispatchers.Main) {
                            counterLiveData.value = counter
                        }
                            counter++
                        }
                    delay(4000)
                }
            }
        }
    }

    fun consumerJob(thread: ExecutorCoroutineDispatcher) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                repeat(200) {
                    withContext(thread) {
                        println("Counter = $counter")
                        withContext(Dispatchers.Main) {
                            counterLiveData.value = counter
                        }

                        counter--
                    }
                    delay(3000)
                }
            }
        }
    }

}
