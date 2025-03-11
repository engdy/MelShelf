package net.engdy.melshelf

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import net.engdy.melshelf.MainActivity.Companion.LAST_IP_ADDRESS
import net.engdy.melshelf.model.RGBColor
import net.engdy.melshelf.model.RGBColorDatabase
import net.engdy.melshelf.model.RGBColorRepository
import net.engdy.melshelf.ui.RGBColorViewModel
import net.engdy.melshelf.ui.RGBColorViewModelFactory
import net.engdy.melshelf.ui.RGBViewModel
import net.engdy.melshelf.ui.theme.MelShelfTheme

class NewColorActivity : ComponentActivity() {
    private lateinit var viewModel: RGBColorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = RGBColorDatabase.getDatabase(this).rgbColorDao()
        val repository = RGBColorRepository(dao)
        val viewModelFactory = RGBColorViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RGBColorViewModel::class.java)
        val extras = intent.extras
        val red = extras?.getInt("red") ?: 0
        val green = extras?.getInt("green") ?: 0
        val blue = extras?.getInt("blue") ?: 0
        val color = android.graphics.Color.rgb(red, green, blue)

        setContent {
            MelShelfTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NewColor(
                        red = red,
                        green = green,
                        blue = blue,
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun NewColor(modifier: Modifier = Modifier, red: Int, green: Int, blue: Int, viewModel: RGBColorViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    val activity = LocalActivity.current
    val color = Color(red, green, blue)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "     ",
            fontSize = 30.sp,
            color = Color.White,
            modifier = Modifier.background(color).padding(30.dp)
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.name)) }
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = {
                if (name.isNotBlank()) {
                    val newColor = RGBColor(
                            name = name,
                            red = red,
                            green = green,
                            blue = blue
                        )
                    viewModel.insert(newColor)
                    activity?.finish()
                } else {
                    Toast.makeText(
                        activity,
                        "Please enter a name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MelShelfTheme {
        Greeting("Android")
    }
}