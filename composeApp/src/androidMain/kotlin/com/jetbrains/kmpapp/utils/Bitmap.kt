import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import coil3.asDrawable
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.allowHardware
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import androidx.core.graphics.createBitmap
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import androidx.core.graphics.scale


//purely ChatGPT for this helper function
fun getCircularBitmap(bitmap: Bitmap): Bitmap {
    val size = 150
    val output = createBitmap(size, size)
    val canvas = Canvas(output)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // scale bitmap to fit inside the circle
    val scaled = bitmap.scale(size, size)
    val shader = BitmapShader(scaled, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    paint.shader = shader

    val radius = size / 2f
    val borderWidth = 10f

    // draw black border first (slightly larger)
    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }
    canvas.drawCircle(radius, radius, radius, borderPaint)

    // draw circular image slightly smaller
    canvas.drawCircle(radius, radius, radius - borderWidth, paint)

    return output
}


@Composable
fun bitmapDescriptorFromUrl(
    context: Context,
    imageUrl: String?
): State<BitmapDescriptor?> {
    return produceState(initialValue = null, imageUrl) {
        if (imageUrl.isNullOrBlank()) {
            value = null
            return@produceState
        }

        try {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()

            val result = context.imageLoader.execute(request)
            val drawable = result.image?.asDrawable(context.resources)

            if (drawable is BitmapDrawable) {
                val circularBitmap = getCircularBitmap(drawable.bitmap)
                value = BitmapDescriptorFactory.fromBitmap(circularBitmap)
            } else {
                println("Failed to convert Coil Image to Android Bitmap for URL: $imageUrl")
                value = null
            }
        } catch (e: Exception) {
            println("Error loading bitmap for $imageUrl: ${e.message}")
        }
    }
}


