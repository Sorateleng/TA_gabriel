package stta.gabriel.ta_gabriel.util

import android.app.Activity
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class UploadImage(
    val activity: Activity?,
    private val nameUser: String,
    private val nameFile: String,
    private val bmap: File?,
    private val callbackUploadImageUrl: CallbackUploadImageUrl?,
    private val dirImage: String? = DIR_UNSET_IMG

) {
    constructor() : this(null, "", "", null, null, null)

    fun upload(access: Int = 0) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl(FBS_BUCKET_NAME)
        val imageRef = storageRef.child("images/$dirImage/$nameUser/$nameFile")
        imageRef.putFile(Uri.fromFile(bmap))
            .addOnSuccessListener { p0 ->
                p0!!.metadata!!.reference!!.downloadUrl.addOnSuccessListener { p0 ->
                    callbackUploadImageUrl?.imageUrl(
                        p0!!.toString(),
                        null,
                        access
                    )
                }
            }
            .addOnFailureListener { p0 ->
                callbackUploadImageUrl?.imageUrl(
                    null,
                    p0.message,
                    access
                )
            }

    }

    fun deleteImage(deleteImgBefore: String = "UNSET") {
        if (deleteImgBefore != "UNSET" && deleteImgBefore.isNotBlank() && deleteImgBefore.contains(
                "firebasestorage"
            )
        ) {
            val storage = FirebaseStorage.getInstance()
            val imageDelRef = storage.getReferenceFromUrl(deleteImgBefore)
            imageDelRef.delete()
        }
    }

    interface CallbackUploadImageUrl {
        fun imageUrl(toString: String?, error: String?, access: Int)
    }
}