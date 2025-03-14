package com.kematian.profile189.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import com.kematian.profile189.CustomRadioButton
import com.kematian.profile189.FocusableOutlinedTextField
import com.kematian.profile189.ProfileRepository
import com.kematian.profile189.ProfileViewModel
import com.kematian.profile189.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SimpleDateFormat")
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val profileState by viewModel.profile.collectAsStateWithLifecycle()
    var selectedDate by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("Azərbaycan dili") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val datePickerState = rememberDatePickerState()
    var expandedDatePicker by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var saveJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(profileState) {
        profileState?.let { profile ->
            username = profile.username
            gender = profile.gender
            telephone = profile.telephone
            email = profile.email
            language = profile.language

            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            profile.birthDate.takeIf { it.isNotEmpty() }?.let {
                selectedDate = LocalDate.parse(it, formatter).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            }
            imageUri = viewModel.getImageUriFromPreferences(context)
        }
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcherCamera = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val uri = saveImageToInternalStorage(context, bitmap)
            ImageLoader(context).memoryCache?.clear()
            imageUri = uri
            viewModel.saveImageUriToPreferences(context, uri)
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            launcherCamera.launch()
        }
    }


    val takePicture = {
        if (hasCameraPermission) {
            launcherCamera.launch()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    val launcherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val scheduleSave = {
        saveJob?.cancel()
        saveJob = coroutineScope.launch {
            delay(1000)
            viewModel.saveProfile(
                username = username,
                gender = gender,
                birthDate = selectedDate,
                telephone = telephone,
                email = email,
                language = language,
                profilePictureUri = imageUri?.toString()
            )
        }
    }

    val saveProfileDataImmediately = {
        saveJob?.cancel()
        viewModel.saveProfile(
            username = username,
            gender = gender,
            birthDate = selectedDate,
            telephone = telephone,
            email = email,
            language = language,
            profilePictureUri = imageUri?.toString()
        )
        viewModel.saveImageUriToPreferences(context, imageUri)
    }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val date = Date(millis)
            val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            selectedDate = formatter.format(date)
            expandedDatePicker = false
        }
    }

    LaunchedEffect(username) { scheduleSave() }
    LaunchedEffect(gender) { scheduleSave() }
    LaunchedEffect(telephone) { scheduleSave() }
    LaunchedEffect(email) { scheduleSave() }
    LaunchedEffect(language) { scheduleSave() }
    LaunchedEffect(selectedDate) { saveProfileDataImmediately() }
    LaunchedEffect(imageUri) { viewModel.saveImageUriToPreferences(context, imageUri) }

    MaterialTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .systemBarsPadding()
                    .background(color = Color(0xFFF8F8F8))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .shadow(5.dp, ambientColor = Color(0x0000000D))
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                        .background(color = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = "Go Back",
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .size(28.dp)
                                    .align(Alignment.CenterStart)
                                    .clickable(onClick = {
                                        saveProfileDataImmediately()
                                        navController.navigate("map")
                                    })
                            )
                            Text(
                                text = "Profil",
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    color = Color(0xFF2A2A2A),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 40.sp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(top = 28.dp, start = 10.dp)
                                .width(101.dp)
                                .height(105.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Image(
                                painter = if (imageUri != null) {
                                    rememberAsyncImagePainter(imageUri)
                                } else {
                                    painterResource(id = R.drawable.profile_picture)
                                },
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(92.dp)
                                    .clip(shape = RoundedCornerShape(12.dp))
                                    .border(
                                        width = 2.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFBE502))
                                    .border(2.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Icon",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            showBottomSheet = true
                                        }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
//                                .shadow(5.dp, ambientColor = Color(0x0000000D))
                                .clip(RoundedCornerShape(16.dp))
                                .background(color = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 20.dp, end = 20.dp)
                                    .fillMaxWidth()
                            ) {
                                FocusableOutlinedTextField(
                                    value = username,
                                    onValueChange = { username = it },
                                    placeholderText = "Vusat Orujov",
                                    fontSize = 20,
                                    fontHeight = 28,
                                    hasTrailingIcon = true,
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                )
                                Row(
                                    modifier = Modifier
                                        .padding(top = 12.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    val options = listOf("Kişi", "Qadın")
                                    var expanded by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(
                                        expanded = expanded,
                                        onExpandedChange = { expanded = !expanded },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = if (expanded) 2.dp else 1.dp,
                                                color = if (expanded) Color(0xFFFBE502) else Color(
                                                    0xFFBCBCBC
                                                ),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .weight(1f)
                                    ) {
                                        OutlinedTextField(
                                            value = gender,
                                            onValueChange = {},
                                            readOnly = true,
                                            placeholder = {
                                                Text(
                                                    text = "Cins",
                                                    color = Color(0xFFBCBCBC),
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Normal,
                                                        fontSize = 16.sp,
                                                        lineHeight = 20.sp
                                                    )
                                                )
                                            },
                                            trailingIcon = {
                                                AnimatedVisibility(expanded) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.arrow_up),
                                                        contentDescription = "Dropdown Icon",
                                                        tint = Color(0xFFBCBCBC),
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                    )
                                                }
                                                AnimatedVisibility(!expanded) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.arrow_down),
                                                        contentDescription = "Dropdown Icon",
                                                        tint = Color(0xFFBCBCBC),
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                    )
                                                }
                                            },
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedBorderColor = Color.Transparent,
                                                focusedBorderColor = Color.Transparent
                                            ),
                                            modifier = Modifier
                                                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                                .fillMaxWidth()
                                        )

                                        ExposedDropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false },
                                            shape = RoundedCornerShape(8.dp),
                                            containerColor = Color(0xFFFFFFFF),
                                            modifier = Modifier
//                                            .padding(top = 2.dp, bottom = 2.dp)
                                                .border(
                                                    width = 2.dp,
                                                    color = Color(0xFFFBE502),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                verticalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                options.forEach { option ->
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clickable {
                                                                gender = option
                                                                expanded = false
                                                            },
                                                    ) {
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(
                                                                    start = 16.dp,
                                                                    top = 12.dp,
                                                                    bottom = 12.dp,
                                                                    end = 16.dp
                                                                )
                                                                .align(Alignment.Center)
                                                        ) {
                                                            Text(
                                                                text = option,
                                                                style = TextStyle(
                                                                    color = Color(0xFF14110F),
                                                                    fontWeight = FontWeight.Normal,
                                                                    fontSize = 16.sp,
                                                                    lineHeight = 24.sp
                                                                ),
                                                                modifier = Modifier
                                                                    .align(Alignment.CenterVertically)
                                                                    .weight(1f)
                                                            )
                                                            CustomRadioButton(
                                                                selected = option == gender,
                                                                onClick = {
                                                                    gender = option
                                                                    expanded = false
                                                                }
                                                            )
                                                        }
                                                    }
                                                    if (option != options.last()) {
                                                        HorizontalDivider(
                                                            thickness = 1.dp,
                                                            color = Color(0xFFDEDEDE),
                                                            modifier = Modifier
                                                                .padding(
                                                                    start = 16.dp,
                                                                    end = 16.dp
                                                                )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = if (expandedDatePicker) 2.dp else 1.dp,
                                                color = if (expandedDatePicker) Color(0xFFFBE502) else Color(
                                                    0xFFBCBCBC
                                                ),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .weight(1f)
                                            .clickable {
                                                expandedDatePicker = true
                                            },
                                    ) {
                                        OutlinedTextField(
                                            value = selectedDate,
                                            onValueChange = {},
                                            readOnly = true,
                                            enabled = false,
                                            placeholder = {
                                                Text(
                                                    text = "MM/DD/YY",
                                                    color = Color(0xFFBCBCBC),
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Normal,
                                                        fontSize = 16.sp,
                                                        lineHeight = 20.sp
                                                    )
                                                )
                                            },
                                            textStyle = TextStyle(
                                                color = Color.Black,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 16.sp,
                                                lineHeight = 24.sp
                                            ),
                                            trailingIcon = {
                                                Icon(
                                                    painter = painterResource(id = if (expandedDatePicker) R.drawable.arrow_up else R.drawable.arrow_down),
                                                    contentDescription = "Dropdown Icon",
                                                    tint = Color(0xFFBCBCBC),
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                )
                                            },
                                            colors = OutlinedTextFieldDefaults.colors(
                                                unfocusedBorderColor = Color.Transparent,
                                                focusedBorderColor = Color.Transparent,
                                                disabledBorderColor = Color.Transparent
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                    }
                                    if (expandedDatePicker) {
                                        DatePickerDialog(
                                            onDismissRequest = { expandedDatePicker = false },
                                            confirmButton = { },
                                            dismissButton = { },
                                            colors = DatePickerDefaults.colors(
                                                todayDateBorderColor = Color(0xFFFBE502)
                                            )
                                        ) {
                                            DatePicker(
                                                state = datePickerState,
                                                title = null,
                                                headline = null,
                                                showModeToggle = false
                                            )
                                        }
                                    }
                                }
                                FocusableOutlinedTextField(
                                    value = telephone,
                                    onValueChange = { telephone = it },
                                    placeholderText = "+994 70 878 28 48",
                                    hasTrailingIcon = true,
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                                FocusableOutlinedTextField(
                                    value = email,
                                    onValueChange = { email = it },
                                    placeholderText = "orucov.vuset@gmail.com",
                                    hasTrailingIcon = true,
                                    modifier = Modifier.padding(top = 16.dp, bottom = 20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
//                                .shadow(5.dp, ambientColor = Color(0x0000000D))
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(color = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                                    .fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = Color(0xFFBCBCBC),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { /*TODO*/ }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(16.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.google),
                                            contentDescription = "Google",
                                            modifier = Modifier
                                                .size(24.dp)
                                        )
                                        Text(
                                            text = "Google ilə davam et",
                                            style = TextStyle(
//                                        fontFamily = FontFamily(Font(R.font.poppinslight)),
                                                color = Color(0xFF9B9B9B),
                                                fontSize = 20.sp,
                                                lineHeight = 28.sp
                                            ),
                                            modifier = Modifier
                                                .padding(start = 58.dp)
                                                .align(Alignment.CenterVertically)
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = Color(0xFFBCBCBC),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { /*TODO*/ }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(16.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.facebook),
                                            contentDescription = "Facebook",
                                            modifier = Modifier
                                                .size(24.dp)
                                        )
                                        Text(
                                            text = "Facebook ilə davam et",
                                            style = TextStyle(
//                                        fontFamily = FontFamily(Font(R.font.poppinslight)),
                                                color = Color(0xFF9B9B9B),
                                                fontSize = 20.sp,
                                                lineHeight = 28.sp
                                            ),
                                            modifier = Modifier
                                                .padding(start = 58.dp)
                                                .align(Alignment.CenterVertically)
                                        )
                                    }
                                }
                                val options =
                                    listOf("Azərbaycan dili", "Rus dili", "Ingilis dili")
                                var expanded by remember { mutableStateOf(false) }

                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded },
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .fillMaxWidth()
                                        .border(
                                            width = if (expanded) 2.dp else 1.dp,
                                            color = if (expanded) Color(0xFFFBE502) else Color(
                                                0xFFBCBCBC
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    OutlinedTextField(
                                        value = language,
                                        onValueChange = {},
                                        readOnly = true,
                                        placeholder = {
                                            Text(
                                                text = "Azərbaycan dili",
                                                color = Color(0xFFBCBCBC),
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 16.sp,
                                                    lineHeight = 20.sp
                                                )
                                            )
                                        },
                                        trailingIcon = {
                                            AnimatedVisibility(expanded) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.arrow_up),
                                                    contentDescription = "Dropdown Icon",
                                                    tint = Color(0xFFBCBCBC),
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                )
                                            }
                                            AnimatedVisibility(!expanded) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.arrow_down),
                                                    contentDescription = "Dropdown Icon",
                                                    tint = Color(0xFFBCBCBC),
                                                    modifier = Modifier
                                                        .size(28.dp)
                                                )
                                            }
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            unfocusedBorderColor = Color.Transparent,
                                            focusedBorderColor = Color.Transparent
                                        ),
                                        modifier = Modifier
                                            .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                            .fillMaxWidth()
                                    )

                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
                                        shape = RoundedCornerShape(8.dp),
                                        containerColor = Color(0xFFFFFFFF),
                                        modifier = Modifier
//                                            .padding(top = 2.dp, bottom = 2.dp)
                                            .border(
                                                width = 2.dp,
                                                color = Color(0xFFFBE502),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            options.forEach { option ->
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            language = option
                                                            expanded = false
                                                        },
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(
                                                                start = 16.dp,
                                                                top = 12.dp,
                                                                bottom = 12.dp,
                                                                end = 16.dp
                                                            )
                                                            .align(Alignment.Center)
                                                    ) {
                                                        Text(
                                                            text = option,
                                                            style = TextStyle(
                                                                color = Color(0xFF14110F),
                                                                fontWeight = FontWeight.Normal,
                                                                fontSize = 16.sp,
                                                                lineHeight = 24.sp
                                                            ),
                                                            modifier = Modifier
                                                                .align(Alignment.CenterVertically)
                                                                .weight(1f)
                                                        )
                                                        CustomRadioButton(
                                                            selected = option == language,
                                                            onClick = {
                                                                language = option
                                                                expanded = false
                                                            }
                                                        )
                                                    }
                                                }
                                                if (option != options.last()) {
                                                    HorizontalDivider(
                                                        thickness = 1.dp,
                                                        color = Color(0xFFDEDEDE),
                                                        modifier = Modifier
                                                            .padding(start = 16.dp, end = 16.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                FilledTonalButton(
                                    onClick = { /*TODO*/ },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonColors(
                                        contentColor = Color(0xFF878787),
                                        containerColor = Color(0xFFF1F1F1),
                                        disabledContainerColor = Color.Transparent,
                                        disabledContentColor = Color.Transparent,
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .border(
                                            width = 2.dp,
                                            color = Color(0xFFF1F1F1),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    Text(
                                        text = "Hesabdan çıx",
                                        textAlign = TextAlign.Center,
                                        color = Color(0xFF878787),
                                        style = TextStyle(
//                                            fontFamily = FontFamily(Font(R.font.poppinsmedium)),
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 16.sp,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                FilledTonalButton(
                                    onClick = { /*TODO*/ },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonColors(
                                        contentColor = Color.Transparent,
                                        containerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        disabledContentColor = Color.Transparent,
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .border(
                                            width = 2.dp,
                                            color = Color(0xFFF1F1F1),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    Text(
                                        text = "Hesabı sil",
                                        textAlign = TextAlign.Center,
                                        color = Color(0xFFFF0000),
                                        style = TextStyle(
//                                            fontFamily = FontFamily(Font(R.font.poppinsmedium)),
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 16.sp,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            scrimColor = Color.Transparent,
            containerColor = Color.White,
            contentColor = Color.Black,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Choose an Option",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            takePicture()
                            showBottomSheet = false
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Camera")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Take a Photo", fontSize = 16.sp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            launcherGallery.launch("image/*")
                            showBottomSheet = false
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Gallery"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Choose from Gallery", fontSize = 16.sp)
                }
            }
        }
    }
}

fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): Uri {
    val timestamp = System.currentTimeMillis()
    val file = File(context.filesDir, "profile_picture_$timestamp.jpg")
    file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
    context.filesDir.listFiles()?.filter {
        it.name.startsWith("profile_picture_") && it.name != file.name
    }?.forEach { it.delete() }

    return file.toUri()
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        navController = NavController(LocalContext.current),
        viewModel = viewModel(
            factory = ProfileViewModel.ProfileViewModelFactory(
                ProfileRepository.getInstance(LocalContext.current)
            )
        )
    )
}
