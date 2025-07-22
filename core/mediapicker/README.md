# Android MediaPicker

## What is Android MediaPicker module

Simple android library for selecting and taking image and video.

--

1. Add `mediapicker` module dependency to your `build.gradle`:

  ```
dependencies {
    implementation(projects.core.mediapicker)
}
  ```

2. Add `include(":core:mediapicker")` in your `settings.gradle`. Ensure
   that auto generated not kts `settings.gradle`
   is removed after adding the module to compile your project

   3. Add FileProvider in your manifest e.g.:
         ```
       <provider
           android:name="androidx.core.content.FileProvider"
           android:authorities="${applicationId}.fileprovider"
           android:exported="false"
           android:grantUriPermissions="true">
           <meta-data
               android:name="android.support.FILE_PROVIDER_PATHS"
               android:resource="@xml/file_paths" />
       </provider>
       ```

      and on `/res/xml/file_paths`:

           <?xml version="1.0" encoding="utf-8"?>
           <paths xmlns:android="http://schemas.android.com/apk/res/android">
               <cache-path
                   name="cached_files"
                   path="." />
               <files-path
                   name="images"
                   path="." />
           </paths>

      2. Create MediaPicker launcher inside Composable:
       ```
            val context = LocalContext.current
            val mediaPickerHelper = remember(context) {
                                MediaPickerHelper(
                                    context = context,
                                    options = MediaPickerOptions(
                                        authority = "${context.packageName}.fileprovider",
                                        providerFilePath = ".",
                                    )
                                )
                  }

            val mediaPickerLaunchers = rememberMediaPickerLaunchers(
                mediaPickerHelper = mediaPickerHelper,
                onError = {},
                onResult = { result ->
                    // Process result uri here either URI or List<URI>
                }
            )
            // Trigger actions on a click listener
            mediaPickerLaunchers.pickMediaAction()
            mediaPickerLaunchers.pickMultipleMediaAction()
            mediaPickerLaunchers.pickMultipleImageAction()
            mediaPickerLaunchers.pickMultipleVideoAction()
            mediaPickerLaunchers.takePictureAction()
            mediaPickerLaunchers.captureVideoAction()
       ```
To compress file or uri:

```
// create instance
val mediaCompressor = MediaCompressor(
    context = context,
    options = mediaPickerOptions,
    imageCompression = ImageCompression(
        width = width,
        height = height,
        quality = quality, // 0 to 100
        formt = Bitmap.CompressFormat.PNG,
        maxFileSize = 1024 * 1024 // in bytes
    ),
    videoCompression = VideoCompression(
        isSharedStorage = true,
        quality = VideoQuality.MEDIUM,
        videoBitrateInMbps = 5
    )
)

mediaCompressor.compressedFile(uri) // uri or file
     .collectLatest { state ->
         when (state) {
             is MediaCompressor.State.CompressedFile -> Log.d(TAG, state.value.absolutePath)
             is MediaCompressor.State.CompressionProgress -> Log.d(TAG, "percent completed: ${state.value}")
             is MediaCompressor.State.Failed -> Log.e(TAG, "compression failed", state.value)
             MediaCompressor.State.Cancelled -> Log.w(TAG, "compression is cancelled)
         }
     }
```

Note: should be called only from a coroutine or another suspend function

## Authors and acknowledgment

- 📝 [Melecio, Raymundo](mailto:raymundo.melecio@appetiser.com.au) (
  Maintainer)

--

## License

Copyright 2022 Appetiser Pty Ltd

Licensed under the Apache License, Version 2.0 (the "License"); you may
not use this file except in compliance with the License. You may
obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
