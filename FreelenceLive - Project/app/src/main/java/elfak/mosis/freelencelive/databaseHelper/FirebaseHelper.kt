package elfak.mosis.freelencelive.databaseHelper

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import elfak.mosis.freelencelive.MainWindowActivity
import elfak.mosis.freelencelive.MyProfileFragment
import elfak.mosis.freelencelive.SignUpFragmentDirections
import elfak.mosis.freelencelive.data.User
import elfak.mosis.freelencelive.databinding.FragmentMyProfileBinding
import elfak.mosis.freelencelive.databinding.FragmentSignUpBinding
import elfak.mosis.freelencelive.model.addEventViewModel
import elfak.mosis.freelencelive.model.userViewModel
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object FirebaseHelper {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var realtimeDatabase: FirebaseDatabase = Firebase.database
    private var realtimeReference: DatabaseReference = realtimeDatabase.reference

//UPIS U REALTIMEDB
    //realtimeReference.getReference("Neki string/I tako dalje").setValue() //msm da moze i ovako
    //realtimeReference.child("Neki string").child("I tako dalje").setValue()
    //                                                            .getValue()
//CITANJE IZ REALTIMEDB
    // #1 - SA VALUEEVENTLISTENER-OM
//    realtimeReference.addValueEventListener(object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            // This method is called once with the initial value and again
//            // whenever data at this location is updated.
//            val value = dataSnapshot.getValue<String>()
//            Log.d(TAG, "Value is: $value")
//            textLoad.text = value
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            // Failed to read value
//            Log.w(TAG, "Failed to read value.", error.toException())
//        }
//    })
    // #2 - OTPAKIVANJE LISTE SNAPSHOT-OVA U EVENT objekat
//    Firebase.database.reference.child("events").get().addOnSuccessListener {
//        //textLoad.text = it.getValue<Event>()?.name
//        //val event: List<Event>? = it.getValue<List<Event>>()
//        for (postSnapshot in it.children) {
////                    val event: Event = postSnapshot.toObject<Event>()
//            Log.d("tag", postSnapshot.toString())
//            val event = postSnapshot.getValue(Event::class.java);
//            //Toast.makeText(this, event?.name + event?.organiser, Toast.LENGTH_LONG).show()
//            Log.d("tag", event!!.name + " " + event!!.organiser)
//        }
//        // textLoad.text = event?.name
//    }.addOnFailureListener{
//        Log.e("firebase", "Error getting data", it)
//    }


    private var cloudFirestore: FirebaseFirestore = Firebase.firestore
    //private var cloudReference : CollectionReference = cloudFirestore.collection("")
//DODAVANJE U CLOUD FIRESTORE-A
//    val user: MutableMap<String, Any> = HashMap()
//    user["firstName"] = "Filip"
//    user["lastName"] = "Trajkovic"

//    cloudFirestore.collection("users")
//    .add(user)
//    .addOnSuccessListener {
//        Toast.makeText(this, "Sucessful added data", Toast.LENGTH_LONG).show()
//    }
//    .addOnFailureListener{
//        Toast.makeText(this, "Not Sucessful added data", Toast.LENGTH_LONG).show()
//    }

    //PRIBAVLJANJE ID-JA ZA PRAZNI DOKUMENT KOJI NIJE DODAT U BAZU
//        val newRef = cloudFirestore.collection("users").document()
//        val id = newRef.id

//PRIBAVLJANJE PODATAKA IZ CLOUD FIRESTORE-A
//    cloudFirestore.collection("users")
//    .get()
//    .addOnSuccessListener { result ->
//        for (document in result) {
//            Log.d(TAG, "${document.id} => ${document.data}")
//            Toast.makeText(this, document.data.toString(), Toast.LENGTH_LONG).show()
//            val event: Event = Event("", "","","","","")
//            event.name = document.data["firstName"].toString()
//            event.organiser = document.data["lastName"].toString()
//
//            Toast.makeText(this, event.name +" "+ event.organiser, Toast.LENGTH_LONG).show()
//        }
//    }
//    .addOnFailureListener { exception ->
//        Log.w(TAG, "Error getting documents.", exception)
//        Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
//    }


    private var cloudStorage: FirebaseStorage = Firebase.storage
    private var storageRef: StorageReference = cloudStorage.reference

//POSTAVLJANJE SLIKE U CLOUD STORAGE IZ IMAGEVIEW-A

//    val imageView: ImageView = findViewById(R.id.openCamera)
//    imageView.isDrawingCacheEnabled = true
//    imageView.buildDrawingCache()
//    val bitmap = (imageView.drawable as BitmapDrawable).bitmap
//
//    val baos = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
//    val data = baos.toByteArray()
//
//
//    // Create a reference to "mountains.jpg"
//    val mountainsRef = storageRef.child("mountains.jpg")
//
//    // Create a reference to 'images/mountains.jpg'
//    val mountainImagesRef = storageRef.child("images/mountains.jpg")
//
//    uploadTask = mountainImagesRef.putBytes(data)
//    uploadTask.addOnFailureListener {
//        Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
//
//    }.addOnSuccessListener { taskSnapshot ->
//        Toast.makeText(this, "File added!", Toast.LENGTH_LONG ).show()
//        pBar.progress = 0
//
//    }.addOnProgressListener {
//        var progress: Double = (100.0 * it.bytesTransferred / it.totalByteCount)
//        pBar.progress = progress.toInt()
//    }

//PRIBAVLJANJE PODATAKA IZ STORAGE-A
// Create a reference with an initial file path and name
    //val pathReference = storageRef.child("images/mountains.jpg")

    //    // Create a reference to a file from a Google Cloud Storage URI
    //    val gsReference = cloudStorage.getReferenceFromUrl("gs://bucket/images/stars.jpg")

    //    // Create a reference from an HTTPS URL
    //    // Note that in the URL, characters are URL escaped!
    //    val httpsReference = cloudStorage.getReferenceFromUrl(
    //        "https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg")

    private lateinit var uploadTask: UploadTask

//--------------------------------------------------------------------------------------------------

    fun registerUser(
        email: String,
        password: String,
        username: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        binding: FragmentSignUpBinding,
        imageBitmap: Bitmap,
        pd: ProgressDialog,
        fragment: Fragment
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    Log.d("user", user.toString())

                    var currUser: User = User(
                        email,
                        username,
                        firstName,
                        lastName,
                        phoneNumber,
                        0,
                        0,
                        ""
//                            listOf<User>(),
//                            listOf<String>()
                    )
                    try {
                        val db = user?.let { it1 ->
                            cloudFirestore.collection("Profiles").document(
                                it1.uid
                            ).set(currUser).addOnCompleteListener {

                                val baos = ByteArrayOutputStream()
                                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                                val data = baos.toByteArray()

                                val mountainImagesRef =
                                    storageRef.child("ProfileImages/${user.uid}.png")
                                uploadTask = mountainImagesRef.putBytes(data)
                                uploadTask.addOnFailureListener {
                                    // Handle unsuccessful uploads
                                    Log.d("PictureFail", "This is a picture upload failure")
                                }.addOnSuccessListener { taskSnapshot ->
                                    //taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                                    firebaseAuth.signOut()
//                                    navController.navigate(R.id.action)
                                    val action =
                                        SignUpFragmentDirections.actionSignupGotoLogin()
                                    NavHostFragment.findNavController(fragment).navigate(action)
                                    pd.hide()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        user?.delete()?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                pd.hide()
                                Snackbar.make(
                                    binding.root,
                                    e.toString(),
                                    //"Neuspešna registracija V2",
                                    Snackbar.LENGTH_LONG
                                ).show()

                            }
                        }
                    }
                } else {
                    pd.hide()
                    Snackbar.make(
                        binding.root,
                        "Neuspešna registracija!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun logInUser(
        context: Context,
        email: String,
        password: String,
        pd: ProgressDialog,
        fragment: Fragment,
        activity: Activity
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            pd.dismiss()
            if (it.isSuccessful) {
                Toast.makeText(
                    fragment.requireActivity(),
                    "UserLogin successful!",
                    Toast.LENGTH_LONG
                ).show()

                val user = firebaseAuth.currentUser
                if (user != null) {
                    getUserDataAndStartActivity(context, activity)
                } else {
                    Toast.makeText(
                        fragment.requireActivity(),
                        "Login successful, but \"currentUser\" is null!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    fragment.requireActivity(),
                    "Login not successful!",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    private fun getUserDataAndStartActivity(context: Context, activity: Activity) {

        val user = firebaseAuth.currentUser
        var data = cloudFirestore.collection("Profiles").document(
            user!!.uid
        ).get()
            .addOnSuccessListener { result ->

                if (result != null) {
                    Log.d("100", "DocumentSnapshot data: ${result.data}")
                    try {

                    var userTmp: User = User("", "", "", "", "", 0, 0, "")

                    //user = result.toObject<User>()
                    userTmp.email = result.data?.get("email").toString()
                    userTmp.userName = result.data?.get("userName").toString()
                    userTmp.firstName = result.data?.get("firstName").toString()
                    userTmp.lastName = result.data?.get("lastName").toString()
                    userTmp.phoneNumber = result.data?.get("phoneNumber").toString()
                    var tmpString: String = result.data?.get("numOfRatings").toString()
                    userTmp.numOfRatings = tmpString.toInt()
                    tmpString = result.data?.get("totalScore").toString()
                    userTmp.totalScore = tmpString.toInt()
                    //user.numOfRatings = result.data?.get("numOfRatings") as Int
                    //user.totalScore = result.data?.get("totalScore") as Int

//                    var profileImg = " "
//                    val storageRef = Firebase.storage.reference
                    storageRef.child("ProfileImages/${user.uid}.png").downloadUrl.addOnSuccessListener {
                        userTmp.imageUrl = it.toString()

                        val mapa: Map<String, String> = mapOf("imageUrl" to userTmp.imageUrl)

                        cloudFirestore.collection("Profiles").document(
                            user!!.uid
                        ).update(mapa)

                        Toast.makeText(context, "Data downloaded!", Toast.LENGTH_LONG).show()
                        val intent = Intent(context, MainWindowActivity::class.java)
                        intent.putExtra("user", userTmp as Serializable)

                        startActivity(context, intent, null)
                        activity.finish()
                    }
                } catch (e: Exception) {
                user?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                           context,
                            e.toString(),
                            //"Neuspešna registracija V2",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG).show()
            }
    }

    fun updateProfilePhoto(pd: ProgressDialog, imageBitmap: Bitmap, binding: FragmentMyProfileBinding, imageUri: Uri?){
        val user = firebaseAuth.currentUser

        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()

        val mountainImagesRef =
            storageRef.child("ProfileImages/${user?.uid}.png")
        uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            pd.dismiss()
            // Handle unsuccessful uploads
            Log.d("PictureFail", "This is a picture upload failure")
        }.addOnSuccessListener { taskSnapshot ->
            //taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            pd.dismiss()
            binding.imageCameraBackground.setImageBitmap(null)
            binding.imageCameraBackground.setImageURI(imageUri)
        }
    }



    fun updateUserData( mapa: HashMap<String, String>, pd: ProgressDialog, context: Context, navController: NavController){
        val user = firebaseAuth.currentUser

        cloudFirestore.collection("Profiles").document(
            user!!.uid
        ).update(mapa as Map<String, Any>).addOnSuccessListener {
            Toast.makeText(context, "Data updated successfully!", Toast.LENGTH_LONG).show()

            navController.popBackStack()
        }.addOnFailureListener{
            Toast.makeText(context, "Data not updated !", Toast.LENGTH_LONG).show()
        }

    }

    fun getOtherUsers(context: Context, userViewModel: userViewModel){
        val userMe = firebaseAuth.currentUser

        cloudFirestore.collection("Profiles").get().addOnSuccessListener {
            val document = it.documents
            val lista: ArrayList<User> = arrayListOf<User>()
            document.forEach{ user ->
                //if(!(userMe?.equals(user)!!)){

                    var userTmp: User = User("", "", "", "", "", 0, 0, "")

                    //user = result.toObject<User>()
                    userTmp.email = user.data?.get("email").toString()
                    userTmp.userName = user.data?.get("userName").toString()
                    userTmp.firstName = user.data?.get("firstName").toString()
                    userTmp.lastName = user.data?.get("lastName").toString()
                    userTmp.phoneNumber = user.data?.get("phoneNumber").toString()
                    var tmpString: String = user.data?.get("numOfRatings").toString()
                    userTmp.numOfRatings = tmpString.toInt()
                    tmpString = user.data?.get("totalScore").toString()
                    userTmp.totalScore = tmpString.toInt()

                    storageRef.child("ProfileImages/${user.id}.png").downloadUrl.addOnSuccessListener {
                        userTmp.imageUrl = it.toString()
                        lista.add(userTmp)
                    }
                        .addOnFailureListener{
                            lista.add(userTmp)
                        }
                //}

            }
            userViewModel.addUserList(lista)

        }.addOnFailureListener{
            Toast.makeText(context, "Users not downloaded!", Toast.LENGTH_LONG).show()
        }
    }


    fun probniCloudFirestore(context: Context) {

        val docData = hashMapOf(
            "stringExample" to "Hello world!",
            "booleanExample" to true,
            "numberExample" to 3.14159265,
            "listExample" to arrayListOf(1, 2, 3),
            "nullExample" to null
        )

        val nestedData = hashMapOf(
            "a" to 5,
            "b" to true
        )

        docData["objectExample"] = nestedData

        val currUser = User(
            "ficax00@gmail.com",
            "Aa1234.",
            "ficax99",
            "Trajkovic",
            "0616440562",
            0,
            0,
            ""
        )

        cloudFirestore.collection("Profiles").document("one").set(currUser)
            .addOnSuccessListener {
                Toast.makeText(context, "Sucessful added data", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
            }

    }

}
