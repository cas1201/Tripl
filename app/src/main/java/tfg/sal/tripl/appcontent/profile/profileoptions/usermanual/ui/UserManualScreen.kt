package tfg.sal.tripl.appcontent.profile.profileoptions.usermanual.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.navigation.NavHostController
import coil.imageLoader
import coil.memory.MemoryCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tfg.sal.tripl.R
import tfg.sal.tripl.appcontent.profile.profileoptions.support.ui.BackHeader
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import tfg.sal.tripl.theme.PrimaryColorDay
import java.io.File
import kotlin.math.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PreferencesScreen(
    viewModel: UserManualViewModel,
    navigationController: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = Modifier.padding(16.dp),
        scaffoldState = scaffoldState,
        topBar = { BackHeader { viewModel.onBackPressed(navigationController) } },
        content = { PreferencesBody(viewModel, navigationController) }
    )
}

@Composable
fun BackHeader(onBackPressed: () -> Unit) {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = stringResource(id = R.string.back),
        modifier = Modifier.clickable { onBackPressed() }
    )
}

@SuppressLint("Recycle")
@Composable
fun PreferencesBody(viewModel: UserManualViewModel, navigationController: NavHostController) {
    val context = LocalContext.current
    val fileName = "tripl_user_manual.pdf"
    val assetManager = context.assets
    val inputStream = assetManager.open(fileName)
    val outputFile = File(context.filesDir, fileName)
    inputStream.use {
            input -> outputFile.outputStream().use { output -> input.copyTo(output) }
    }
    val uri = Uri.fromFile(outputFile)
    PdfViewer(uri = uri, viewModel = viewModel, navigationController = navigationController)
}

@SuppressLint("Range")
@Composable
fun PdfViewer(
    modifier: Modifier = Modifier,
    uri: Uri,
    viewModel: UserManualViewModel,
    navigationController: NavHostController,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp)
) {
    val rendererScope = rememberCoroutineScope()
    val mutex = remember { Mutex() }
    val renderer by produceState<PdfRenderer?>(null, uri) {
        rendererScope.launch(Dispatchers.IO) {
            val input = ParcelFileDescriptor.open(uri.toFile(), ParcelFileDescriptor.MODE_READ_ONLY)
            value = PdfRenderer(input)
        }
        awaitDispose {
            val currentRenderer = value
            rendererScope.launch(Dispatchers.IO) {
                mutex.withLock {
                    currentRenderer?.close()
                }
            }
        }
    }
    val context = LocalContext.current
    val imageLoader = LocalContext.current.imageLoader
    val imageLoadingScope = rememberCoroutineScope()
    var scale by remember { mutableStateOf(1f) }
    var translationX by remember { mutableStateOf(1f) }
    var translationY by remember { mutableStateOf(1f) }
    Box {
        BoxWithConstraints(
            modifier
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = translationX,
                    translationY = translationY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 1.5f)
                        translationX = if (scale == 1f) 0f else translationX + pan.x
                        translationY = if (scale == 1f) 0f else translationY + pan.y
                    }
                }
        ) {
            val width = with(LocalDensity.current) { maxWidth.toPx() }.toInt()
            val height = (width * sqrt(2f)).toInt()
            val pageCount by remember(renderer) { derivedStateOf { renderer?.pageCount ?: 0 } }
            LazyColumn(
                verticalArrangement = verticalArrangement
            ) {
                items(
                    count = pageCount,
                    key = { index -> "$uri-$index" }
                ) { index ->
                    val cacheKey = MemoryCache.Key("$uri-$index")
                    var bitmap by remember { mutableStateOf(imageLoader.memoryCache?.get(cacheKey) as? Bitmap?) }
                    if (bitmap == null) {
                        DisposableEffect(uri, index) {
                            val job = imageLoadingScope.launch(Dispatchers.IO) {
                                val destinationBitmap =
                                    Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                                mutex.withLock {
                                    if (!coroutineContext.isActive) return@launch
                                    try {
                                        renderer?.let {
                                            it.openPage(index).use { page ->
                                                page.render(
                                                    destinationBitmap,
                                                    null,
                                                    null,
                                                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                                )
                                            }
                                        }
                                    } catch (e: Exception) {
                                        viewModel.showToast(context, R.string.error_rendering_pdf)
                                        viewModel.pdfRenderError(navigationController)
                                        return@launch
                                    }
                                }
                                bitmap = destinationBitmap
                            }
                            onDispose {
                                job.cancel()
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(PrimaryColorDay)
                                .aspectRatio((1f / sqrt(2f)))
                                .fillMaxWidth()
                        )
                    } else {
                        val request = ImageRequest.Builder(context)
                            .size(width, height)
                            .memoryCacheKey(cacheKey)
                            .data(bitmap)
                            .build()
                        Image(
                            modifier = Modifier
                                .background(PrimaryColorDay)
                                .aspectRatio((1f / sqrt(2f)))
                                .fillMaxWidth()
                                .border(1.dp, Color.Black),
                            contentScale = ContentScale.Fit,
                            painter = rememberAsyncImagePainter(request),
                            contentDescription = "Page ${index + 1} of $pageCount",
                        )
                    }
                }
            }
        }
    }
}
