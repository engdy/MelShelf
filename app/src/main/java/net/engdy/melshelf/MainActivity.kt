package net.engdy.melshelf

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.engdy.melshelf.model.RGBColor
import net.engdy.melshelf.ui.RGBViewModel
import net.engdy.melshelf.ui.theme.MelShelfTheme
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: RGBViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val lastAddress: String = prefs.getString(LAST_IP_ADDRESS, "")!!
        viewModel = RGBViewModel(lastAddress)
        setContent {
            MelShelfTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MissyShelf(viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val lastAddress: String = prefs.getString(LAST_IP_ADDRESS, "")!!
        attemptAddress(prefs, lastAddress)
    }

    private fun failAddress() {
        viewModel.disconnect()
    }

    fun attemptAddress(prefs: SharedPreferences, address: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val url = URL("http://$address:$PORT/")
            val client = OkHttpClient()
            try {
                val request: Request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body: String = response.body!!.string()
                val vitekShelf = body.substring(0..10)
                Log.d(TAG, "Body = '$body'")
                if (vitekShelf == "Vitek Shelf") {
                    Log.d(TAG, "Connected to $url")
                    val rgbhex = body.substring(13..18)
                    val r = rgbhex.substring(0..1).toInt(radix = 16)
                    val g = rgbhex.substring(2..3).toInt(radix = 16)
                    val b = rgbhex.substring(4..5).toInt(radix = 16)
                    viewModel.connect(address)
                    viewModel.setColor(RGBColor(0, "", r, g, b))
                    val ed = prefs.edit()
                    ed.putString(LAST_IP_ADDRESS, address)
                    ed.apply()
                } else {
                    Log.d(TAG, "Not a Vitek Shelf")
                    failAddress()
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                failAddress()
            }
        }
    }

    fun sendChange(address: String, red: Int, green: Int, blue: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val url = URL("http://$address:$PORT/change-color?r=$red&g=$green&b=$blue")
            val client = OkHttpClient()
            try {
                val request: Request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body: String = response.body!!.string()
                Log.d(TAG, "Body = '$body'")
                viewModel.setColor(RGBColor(0, "", red, green, blue))
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

    companion object {
        val TAG = MainActivity::class.simpleName
        const val LAST_IP_ADDRESS = "LAST_IP_ADDRESS"
        const val PORT = 8080
    }
}

@Composable
fun ShelfDisconnected(
    modifier: Modifier = Modifier,
    viewModel: RGBViewModel = viewModel()
) {
    val rgbUiState by viewModel.uiState.collectAsState()
    var address by remember { mutableStateOf(rgbUiState.ipAddress) }
    val activity = LocalActivity.current as MainActivity
    val prefs = activity.getPreferences(Context.MODE_PRIVATE)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(50.dp)
        )
        Text(
            text = stringResource(R.string.main_title),
            fontSize = 30.sp,
            color = Color.White,
            modifier = Modifier.background(Color(0xFF990000)).padding(30.dp)
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(stringResource(R.string.ip_address)) }
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = {
                activity.attemptAddress(prefs, address)
            }
        ) {
            Text(
                text = stringResource(R.string.connect),
                fontSize = 30.sp
            )
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ShelfConnected(
    modifier: Modifier = Modifier,
    viewModel: RGBViewModel = viewModel()
) {
    val rgbUiState by viewModel.uiState.collectAsState()
    val address by remember { mutableStateOf(rgbUiState.ipAddress) }
    var red by remember { mutableFloatStateOf(rgbUiState.color.red.toFloat()) }
    red = rgbUiState.color.red.toFloat()
    var green by remember { mutableFloatStateOf(rgbUiState.color.green.toFloat()) }
    green = rgbUiState.color.green.toFloat()
    var blue by remember { mutableFloatStateOf(rgbUiState.color.blue.toFloat()) }
    blue = rgbUiState.color.blue.toFloat()
    val activity = LocalActivity.current as MainActivity
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(50.dp)
        )
        Text(
            text = stringResource(R.string.main_title),
            fontSize = 30.sp,
            color = Color.White,
            modifier = Modifier.background(Color(0xFF009900)).padding(30.dp)
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Slider(
            value = red,
            onValueChange = { red = it },
            valueRange = 0f..255f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Red,
            ),
            onValueChangeFinished = {
                activity.sendChange(address, red.toInt(), green.toInt(), blue.toInt())
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Slider(
            value = green,
            onValueChange = { green = it },
            valueRange = 0f..255f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Green,
                activeTrackColor = Color.Green,
            ),
            onValueChangeFinished = {
                activity.sendChange(address, red.toInt(), green.toInt(), blue.toInt())
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Slider(
            value = blue,
            onValueChange = { blue = it },
            valueRange = 0f..255f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Blue,
                activeTrackColor = Color.Blue,
            ),
            onValueChangeFinished = {
                activity.sendChange(address, red.toInt(), green.toInt(), blue.toInt())
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Text(
            text = if (red.toInt() == 0 && green.toInt() == 0 && blue.toInt() == 0) {
                stringResource(R.string.off)
            } else {
                stringResource(R.string.color)
            },
            fontSize = 30.sp,
            color = Color.White,
            modifier = Modifier.background(Color(red.toInt(), green.toInt(), blue.toInt())).padding(30.dp)
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    val intent = Intent(activity, NewColorActivity::class.java)
                    intent.putExtra("red", red.toInt())
                    intent.putExtra("green", green.toInt())
                    intent.putExtra("blue", blue.toInt())
                    activity.startActivity(intent)
                }
            ) {
                Text(
                    text = stringResource(R.string.save),
                    fontSize = 30.sp
                )
            }
            Spacer(
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    val intent = Intent(activity, ColorListActivity::class.java)
                    activity.startActivity(intent)
                }
            ) {
                Text(
                    text = stringResource(R.string.load),
                    fontSize = 30.sp
                )
            }
            Spacer(
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun MissyShelf(
    modifier: Modifier = Modifier,
    viewModel: RGBViewModel = viewModel()
) {
    val rgbUiState by viewModel.uiState.collectAsState()
    if (rgbUiState.isConnected) {
        ShelfConnected(modifier = modifier, viewModel)
    } else {
        ShelfDisconnected(modifier = modifier, viewModel)
    }
}