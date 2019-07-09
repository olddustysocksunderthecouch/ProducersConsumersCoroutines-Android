package net.studydrive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    var counter = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val counterContext = newSingleThreadContext("CounterContext")

        producer_button.setOnClickListener {
            addJob(counterContext)
        }

        consumer_button.setOnClickListener {
            consumerJob(counterContext)
        }

    }

    data class Ball(var hits: Int)


    fun addJob(thread: ExecutorCoroutineDispatcher) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                repeat(200) {
                    withContext(thread) {
                            println("Counter = $counter")
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
                        counter--
                    }
                    delay(3000)
                }
            }
        }
    }

}
