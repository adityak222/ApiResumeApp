package com.example.signup

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signup.Resume
import com.example.signup.ui.theme.SignupTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.*

import androidx.compose.ui.tooling.preview.Preview
import com.example.signup.Project
import com.example.signup.ui.theme.SignupTheme

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ResumeScreen(viewModel: ResumeViewModel) {
    val resumeState by viewModel.resumeState.collectAsState()
    val location by viewModel.location.collectAsState()

    // --- State for Customization ---
    val fontSize by viewModel.fontSize.collectAsState()
    val fontColor by viewModel.fontColor.collectAsState()
    val backgroundColor by viewModel.backgroundColor.collectAsState()

    // --- Permission Handling ---
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            viewModel.startLocationUpdates()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resume Generator") },
                actions = {
                    if (locationPermissionsState.allPermissionsGranted) {
                        val lat = location?.first
                        val lon = location?.second
                        Text(
                            text = if (lat != null && lon != null) String.format(Locale.US, "Lat: %.2f, Lon: %.2f", lat, lon) else "Location...",
                            modifier = Modifier.padding(end = 16.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            )
        }
        
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (!locationPermissionsState.allPermissionsGranted) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Location permission is required to show coordinates.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                        Text("Grant Permission")
                    }
                }
            }


            // --- Resume Content ---
            else {
                when (val state = resumeState) {
                    is ResumeUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

                    is ResumeUiState.Success -> {
                        ResumeContent(
                            resume = state.resume,
                            fontSize = fontSize,
                            fontColor = fontColor,
                            backgroundColor = backgroundColor,
                            modifier = Modifier.weight(weight = 1f)
                        )
                    }

                    is ResumeUiState.Error -> {
                        Text(
                            text = state.message,
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

        Spacer(modifier = Modifier.height(16.dp))

        CustomizationControls(
            fontSize = fontSize,
            onFontSizeChange = { viewModel.onFontSizeChange(it) },
            onFontColorChange = { viewModel.onFontColorChange(it) },
            onBgColorChange = { viewModel.onBackgroundColorChange(it) }
        )
    }
    }
}

@Composable
fun CustomizationControls(
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    onFontColorChange: (Color) -> Unit,
    onBgColorChange: (Color) -> Unit
) {
    var showFontColorPicker by remember { mutableStateOf(false) }
    var showBgColorPicker by remember { mutableStateOf(false) }
Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter)
{
    Card {
        Column(
            modifier = Modifier.padding(16.dp),

            ) {
            // Font Size Slider
            Text("Font Size: ${fontSize.toInt()}")
            Slider(
                modifier = Modifier.height(20.dp),
                value = fontSize,
                onValueChange = onFontSizeChange,
                valueRange = 12f..28f,
                steps = 15
            )
            Spacer(modifier = Modifier.height(10.dp))
            // Color Pickers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { showFontColorPicker = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) { Text("Font Color") }
                Button(onClick = { showBgColorPicker = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) { Text("Bg Color") }
            }
        }
    }
}


    if (showFontColorPicker) {
        ColorPickerDialog(
            onColorSelected = { onFontColorChange(it) },
            onDismiss = { showFontColorPicker = false }
        )
    }
    if (showBgColorPicker) {
        ColorPickerDialog(
            onColorSelected = { onBgColorChange(it) },
            onDismiss = { showBgColorPicker = false }
        )
    }
}


@Composable
fun ResumeContent(resume: Resume, fontSize: Float, fontColor: Color, backgroundColor: Color, modifier: Modifier = Modifier) {
     LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(16.dp),
         verticalArrangement = Arrangement.Top
    ) {
        // Name
        item {
            Text(
                text = resume.name,
                fontSize = (fontSize+8).sp,
                fontWeight = FontWeight.Bold,
                color = fontColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = resume.phone,
                fontSize = (fontSize+2).sp,
                fontWeight = FontWeight.Thin,
                color = fontColor,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = resume.email,
                fontSize = (fontSize +2).sp,
                fontWeight = FontWeight.Thin,
                color = fontColor,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = resume.twitter,
                fontSize = (fontSize + 2).sp,
                fontWeight = FontWeight.Thin,
                color = fontColor,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = resume.address,
                fontSize = (fontSize + 2).sp,
                fontWeight = FontWeight.Thin,
                color = fontColor,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp))
        }

        item {
            Text(
                text = "SKILLS",
                fontSize = (fontSize + 3).sp,
                fontWeight = FontWeight.Bold,
                color = fontColor,
            )
            Spacer(modifier = Modifier.height(8.dp))
            resume.skills.forEach { skill ->
                Text(
                    text = "• $skill",
                    fontSize = fontSize.sp,
                    color = fontColor,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "PROJECTS",
                fontSize = (fontSize + 3).sp,
                fontWeight = FontWeight.Bold,
                color = fontColor,
            )
            Spacer(modifier = Modifier.height(8.dp))
            resume.projects.forEach { project ->
                Text(
                    text = "• ${project.title}",
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = fontColor,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = "•${project.startDate}-${project.endDate}",
                    fontSize = (fontSize-2).sp,
                    //fontWeight = FontWeight.SemiBold,
                    color = fontColor,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = project.description,
                    fontSize = (fontSize-1).sp,
                    color = fontColor,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }
        }
         item {
             Text(
                 text = "SUMMARY",
                 fontSize = (fontSize + 3).sp,
                 fontWeight = FontWeight.Bold,
                 color = fontColor,
             )
             Text(
                 text = resume.summary,
                 fontSize = (fontSize).sp,
                 fontWeight = FontWeight.Thin,
                 color = fontColor,
                 textAlign = TextAlign.Left,
                 modifier = Modifier.fillMaxWidth()
             )
         }

    }
}

@Composable
fun ColorPickerDialog(onColorSelected: (Color) -> Unit, onDismiss: () -> Unit) {
    val colors = listOf(
        Color.Black, Color.White, Color.Red, Color.Green, Color.Blue,
        Color(0xFFE0F7FA), Color(0xFFFFEB3B), Color(0xFF673AB7)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select a Color") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(1.dp, Color.Gray, CircleShape)
                            .clickable {
                                onColorSelected(color)
                                onDismiss()
                            }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}




// --- PREVIEW FUNCTIONS ---

/**
 * Creates a sample Resume object for use in previews.
 */
/*private fun createDummyResume(): Resume {
    return Resume(
        name = "Jane Doe",
        skills = listOf(
            "Kotlin, Java, Android Development",
            "REST APIs, Firebase, MVVM Architecture",
            "Git, Github"
        ),
        projects = listOf(
            Project(
                name = "VirusCheck",
                description = "Scan File and URL for potential threat."
            ),
            Project(
                name = "Portfolio App",
                description = "A personal portfolio to showcase my work."
            )
        )
    )
}*/

/**
 * Previews the main content area where the resume text is displayed.
 * This shows how the resume looks with a specific set of customizations.

@Preview(showBackground = true, name = "Resume Content Preview")
@Composable
fun ResumeContentPreview() {
    SignupTheme {
        ResumeContent(
            resume = createDummyResume(),
            fontSize = 18f,
            fontColor = Color.DarkGray,
            backgroundColor = Color(0xFFE0F7FA)
        )
    }
}

/**
 * Previews the customization controls card, including the
 * font size slider and the color picker buttons.
 */
@Preview(showSystemUi = true, name = "Customization Controls Preview")
@Composable
fun CustomizationControlsPreview() {
    SignupTheme {
        CustomizationControls(
            fontSize = 20f,
            onFontSizeChange = {}, // Lambdas can be empty for previews
            onFontColorChange = {},
            onBgColorChange = {}
        )
    }
}

/**
 * Previews the Color Picker Dialog component.
 */
@Preview(name = "Color Picker Dialog Preview")
@Composable
fun ColorPickerDialogPreview() {
    SignupTheme{
        ColorPickerDialog(
            onColorSelected = {},
            onDismiss = {}
        )
    }
}


/**
 * Previews the full screen in its "Success" state, showing how all the
 * components fit together. This preview simulates the data being successfully
 * loaded from the API.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Full Screen Success State")
@Composable
fun FullScreenSuccessPreview() {
    SignupTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Resume Generator") },
                    actions = {
                        Text(
                            text = "Lat: 40.71, Lon: -74.00",
                            modifier = Modifier.padding(end = 16.dp),
                            fontSize = 12.sp
                        )
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))
                ResumeContent(
                    resume = createDummyResume(),
                    fontSize = 18f,
                    fontColor = Color.Black,
                    backgroundColor = Color(0xFFE0F7FA)
                )
                CustomizationControls(
                    fontSize = 18f,
                    onFontSizeChange = {},
                    onFontColorChange = {},
                    onBgColorChange = {}
                )
            }
        }
    }
}*/