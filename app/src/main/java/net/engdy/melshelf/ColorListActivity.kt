package net.engdy.melshelf

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import net.engdy.melshelf.model.RGBColor
import net.engdy.melshelf.model.RGBColorDatabase
import net.engdy.melshelf.model.RGBColorRepository
import net.engdy.melshelf.ui.RGBColorViewModel
import net.engdy.melshelf.ui.RGBColorViewModelFactory
import net.engdy.melshelf.ui.theme.MelShelfTheme

class ColorListActivity : ComponentActivity() {
    private lateinit var viewModel: RGBColorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = RGBColorDatabase.getDatabase(this).rgbColorDao()
        val repository = RGBColorRepository(dao)
        val viewModelFactory = RGBColorViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RGBColorViewModel::class.java)

        setContent {
            MelShelfTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RGBColorScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun RGBColorScreen(viewModel: RGBColorViewModel) {
    val context = LocalContext.current
    val colors by viewModel.allColors.observeAsState(initial = emptyList())
    var name by remember { mutableStateOf("") }
    var red by remember { mutableStateOf("") }
    var green by remember { mutableStateOf("") }
    var blue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = red,
            onValueChange = { red = it },
            label = { Text("Red (0-255)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = green,
            onValueChange = { green = it },
            label = { Text("Green (0-255)") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = blue,
            onValueChange = { blue = it },
            label = { Text("Blue (0-255)") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (name.isNotBlank() && red.isNotBlank() && green.isNotBlank() && blue.isNotBlank()) {
                    try {
                        val redValue = red.toInt()
                        val greenValue = green.toInt()
                        val blueValue = blue.toInt()
                        if (redValue in 0..255 && greenValue in 0..255 && blueValue in 0..255) {
                            val newColor = RGBColor(
                                name = name,
                                red = redValue,
                                green = greenValue,
                                blue = blueValue
                            )
                            viewModel.insert(newColor)
                            name = ""
                            red = ""
                            green = ""
                            blue = ""
                        } else {
                            Toast.makeText(
                                context,
                                "RGB values must be between 0 and 255",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: NumberFormatException) {
                        Toast.makeText(
                            context,
                            "Invalid RGB values",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Add Color")
        }

        LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
            items(colors) { color ->
                ColorItem(color = color, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ColorItem(color: RGBColor, viewModel: RGBColorViewModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier.width(40.dp).background(Color(color.red, color.green, color.blue))
            )
            Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                Text(text = color.name, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                Text(text = "RGB: ${color.red}, ${color.green}, ${color.blue}")
            }
            Button(onClick = {
                viewModel.delete(color)
                Toast.makeText(context, "Color deleted", Toast.LENGTH_SHORT).show()
            }) {
                Text("Delete")
            }
        }
    }
}