package net.studydrive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {

    var counter = 10;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val counterContext = newSingleThreadContext("CounterContext")
        var counter = 0

        fun main() = launch {
            withContext(Dispatchers.Default) {
                massiveRun {
                    // confine each increment to a single-threaded context
                    withContext(counterContext) {
                        counter++
                    }
                }
            }
            println("Counter = $counter")
        }

    }

}
