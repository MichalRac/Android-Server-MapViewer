package com.example.android_server_mapviewerclient

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_server_mapviewerclient.ui.theme.AndroidServerMapViewerClientTheme
import java.io.ByteArrayInputStream
import java.io.InputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidServerMapViewerClientTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MapViewer()
                }
            }
        }
    }
}

const val minLat = 54.333251000649454
const val maxLat = 54.413205389562194
const val minLong = 18.572490218700082
const val maxLong = 18.709215200253727
var selectedLatMin = minLat
var selectedLatMax = maxLat
var selectedLongMin = minLong
var selectedLongMax = maxLong

@Composable
fun MapViewer()
{
    var imageBase64 by remember { mutableStateOf("R0lGODlhAQABAIAAAMLCwgAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==") }
    var latMinText by remember { mutableStateOf(minLat.toString().substring(0,10)) }
    var latMaxText by remember { mutableStateOf(maxLat.toString().substring(0,10)) }
    var longMinText by remember { mutableStateOf(minLong.toString().substring(0,10)) }
    var longMaxText by remember { mutableStateOf(maxLong.toString().substring(0,10)) }

    val decodedString = Base64.decode(imageBase64, Base64.DEFAULT)
    val inputStream: InputStream = ByteArrayInputStream(decodedString)
    val bitmap = BitmapFactory.decodeStream(inputStream)

    Column(
        Modifier
            .fillMaxHeight()
            .wrapContentHeight(Alignment.CenterVertically)){

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "contentDescription",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .height(200.dp)
                .width(200.dp)
        )
        
        Spacer(modifier = Modifier.height(25.dp))

        TextField(
            value = latMaxText,
            onValueChange = {
                latMaxText = it
                selectedLatMax = latMaxText.toDouble()
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .width(150.dp),
            label = { Text(text = "max lat");
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)) {
            TextField(
                value = longMinText,
                onValueChange = {
                    longMinText = it
                    selectedLongMin = longMinText.toDouble()
                },
                modifier = Modifier.width(150.dp),
                label = { Text(text = "min long");
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(25.dp))
            TextField(
                value = longMaxText,
                onValueChange = {
                    longMaxText = it
                    selectedLongMax = longMaxText.toDouble()
                },
                modifier = Modifier.width(150.dp),
                label = { Text(text = "max long");
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        TextField(
            value = latMinText,
            onValueChange = {
                latMinText = it
                selectedLatMin = latMinText.toDouble()
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .width(150.dp),
            label = { Text(text = "min lat");
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally))
        {
            Button(onClick = {
                selectedLatMin = minLat
                selectedLatMax = maxLat
                selectedLongMin = minLong
                selectedLongMax = maxLong
                latMinText = minLat.toString().substring(0,10)
                latMaxText = maxLat.toString().substring(0,10)
                longMinText = minLong.toString().substring(0,10)
                longMaxText = maxLong.toString().substring(0,10)
            }) { Text("Set Max") }

            Spacer(Modifier.width(25.dp))

            Button(onClick = {
                var mapRequestHandler = MapRequestHandler()
                var listener = IMapResponseListener { imageBase64 = it }
                mapRequestHandler.SendRequest(listener, selectedLatMin, selectedLatMax, selectedLongMin, selectedLongMax)
            }){ Text("Evaluate") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidServerMapViewerClientTheme {
        MapViewer()
    }
}