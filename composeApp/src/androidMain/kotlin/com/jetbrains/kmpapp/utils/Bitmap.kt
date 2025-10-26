import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.asAndroidBitmap
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.allowHardware
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

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
            val imageBitmap = result.image
            if (imageBitmap != null) {
                // Convert ImageBitmap to Android Bitmap
                val bitmap: android.graphics.Bitmap? = when (val img = result.image) {
                    is android.graphics.drawable.BitmapDrawable -> img.bitmap
                    else -> null
                }
                if (bitmap != null) {
                    value = BitmapDescriptorFactory.fromBitmap(bitmap)
                } else {
                    value = null
                }
            }
        } catch (e: Exception) {
            println("Error loading bitmap for $imageUrl: ${e.message}")
        }
    }
}
