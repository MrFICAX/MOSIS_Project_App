package elfak.mosis.freelencelive.databaseHelper

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import elfak.mosis.freelencelive.*
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.data.*
import elfak.mosis.freelencelive.databinding.FragmentMyProfileBinding
import elfak.mosis.freelencelive.databinding.FragmentSignUpBinding
import elfak.mosis.freelencelive.model.userViewModel
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
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
//        Toast.makeText(this, "Successful added data", Toast.LENGTH_LONG).show()
//    }
//    .addOnFailureListener{
//        Toast.makeText(this, "Not Successful added data", Toast.LENGTH_LONG).show()
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

//UPITI U CLOUD FIRESTORE-U

    // 1. collection("").whereEqualTo("keyString", "value") //vraca


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
                        user?.uid!!,
                        email,
                        username,
                        firstName,
                        lastName,
                        phoneNumber,
                        0f,
                        0,
                        "",
                        hashMapOf<String, Boolean>()
//                            listOf<User>(),
//                            listOf<String>()
                    )
                    try {
                        val db = user.let { it1 ->
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
        activity: Activity,
        userViewModel: userViewModel
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
                    getUserDataAndStartActivity(context, activity, userViewModel)
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

    fun getUserDataAndStartActivity(
        context: Context,
        activity: Activity,
        userViewModel: userViewModel
    ) {

        val user = firebaseAuth.currentUser
        var data = cloudFirestore.collection("Profiles").document(
            user!!.uid
        ).get()
            .addOnSuccessListener { result ->

                if (result != null) {
                    Log.d("100", "DocumentSnapshot data: ${result.data}")
                    try {

                        var userTmp: User = User(
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0f,
                            0,
                            "",
                            hashMapOf<String, Boolean>()
                        )

                        //user = result.toObject<User>()
                        userTmp.id = result.data?.get("id").toString()
                        userTmp.email = result.data?.get("email").toString()
                        userTmp.userName = result.data?.get("userName").toString()
                        userTmp.firstName = result.data?.get("firstName").toString()
                        userTmp.lastName = result.data?.get("lastName").toString()
                        userTmp.phoneNumber = result.data?.get("phoneNumber").toString()
                        var tmpString: String = result.data?.get("numOfRatings").toString()
                        userTmp.numOfRatings = tmpString.toInt()
                        tmpString = result.data?.get("totalScore").toString()
                        userTmp.totalScore = tmpString.toFloat()

                        var lista = result.data?.getValue("friendsList")
                        userTmp.friendsList = lista as HashMap<String, Boolean>
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
                            userViewModel.setNewUser(userTmp)

                            startActivity(context, intent, null)
                            activity.finish()
                        }
                    } catch (e: Exception) {
//                user?.delete()?.addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            e.toString(),
                            //"Neuspešna registracija V2",
                            Toast.LENGTH_LONG
                        ).show()

//                    }
//                }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG).show()
            }
    }

    fun getUserData(
        context: Context,
        activity: Activity,
        userViewModel: userViewModel,
        requireContext: Context
    ) {

        val user = firebaseAuth.currentUser
        var data = cloudFirestore.collection("Profiles").document(
            user!!.uid
        ).get()
            .addOnSuccessListener { result ->

                if (result != null) {
                    Log.d("100", "DocumentSnapshot data: ${result.data}")
                    try {

                        var userTmp: User = User(
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0f,
                            0,
                            "",
                            hashMapOf<String, Boolean>()
                        )

                        //user = result.toObject<User>()
                        userTmp.id = result.data?.get("id").toString()
                        userTmp.email = result.data?.get("email").toString()
                        userTmp.userName = result.data?.get("userName").toString()
                        userTmp.firstName = result.data?.get("firstName").toString()
                        userTmp.lastName = result.data?.get("lastName").toString()
                        userTmp.phoneNumber = result.data?.get("phoneNumber").toString()
                        var tmpString: String = result.data?.get("numOfRatings").toString()
                        userTmp.numOfRatings = tmpString.toInt()
                        tmpString = result.data?.get("totalScore").toString()
                        userTmp.totalScore = tmpString.toFloat()

                        var lista = result.data?.getValue("friendsList")
                        userTmp.friendsList = lista as HashMap<String, Boolean>
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
                            userViewModel.setNewUser(userTmp)
                            getMyServiceValue(userViewModel, requireContext)
//                            getOnlineValues(userViewModel, requireContext)
                            setOnlineValueEventListener(userViewModel, requireContext)
                        }
                    } catch (e: Exception) {
//                user?.delete()?.addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            e.toString(),
                            //"Neuspešna registracija V2",
                            Toast.LENGTH_LONG
                        ).show()

//                    }
//                }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG).show()
            }
    }

    fun updateProfilePhoto(
        pd: ProgressDialog,
        imageBitmap: Bitmap,
        userViewModel: userViewModel,
        binding: FragmentMyProfileBinding,
        imageUri: Uri?
    ) {
        val user: User? = userViewModel.user.value// = firebaseAuth.currentUser

        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()

        val mountainImagesRef =
            storageRef.child("ProfileImages/${user?.id}.png")
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


    fun updateUserData(
        mapa: HashMap<String, String>,
        userViewModel: userViewModel,
        pd: ProgressDialog,
        context: Context,
        navController: NavController
    ) {
        val user: User? = userViewModel.user.value//firebaseAuth.currentUser

        cloudFirestore.collection("Profiles").document(
            user!!.id
        ).update(mapa as Map<String, Any>).addOnSuccessListener {
            Toast.makeText(context, "Data updated successfully!", Toast.LENGTH_LONG).show()
            userViewModel.updateUserData(user, mapa)
            navController.popBackStack()
        }.addOnFailureListener {
            Toast.makeText(context, "Data not updated !", Toast.LENGTH_LONG).show()
        }

    }

    fun getOtherUsers(context: Context, userViewModel: userViewModel) {
        val userMe: User? = userViewModel.user.value//firebaseAuth.currentUser

        cloudFirestore.collection("Profiles").get().addOnSuccessListener {
            val document = it.documents
            val lista: MutableList<User> = mutableListOf<User>()
            document.forEach { user ->
                if (!(userMe?.id.equals(user.id))) {

                    var userTmp: User = User(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        0f,
                        0,
                        "",
                        hashMapOf<String, Boolean>()
                    )

                    //user = result.toObject<User>()
                    userTmp.id = user.data?.get("id").toString()
                    userTmp.email = user.data?.get("email").toString()
                    userTmp.userName = user.data?.get("userName").toString()
                    userTmp.firstName = user.data?.get("firstName").toString()
                    userTmp.lastName = user.data?.get("lastName").toString()
                    userTmp.phoneNumber = user.data?.get("phoneNumber").toString()
                    var tmpString: String = user.data?.get("numOfRatings").toString()
                    userTmp.numOfRatings = tmpString.toInt()
                    tmpString = user.data?.get("totalScore").toString()
                    userTmp.totalScore = tmpString.toFloat()

                    var hashMapa = user.data?.getValue("friendsList")
                    userTmp.friendsList = hashMapa as HashMap<String, Boolean>
                    lista.add(userTmp)

                    storageRef.child("ProfileImages/${user.id}.png").downloadUrl.addOnSuccessListener {
                        //userTmp.imageUrl = it.toString()
                        userViewModel.setPhotoUrlToUser(userTmp.id, it.toString())
                    }
                        .addOnFailureListener {
                            lista.add(userTmp)
                        }
                    //}

                }
            }
            userViewModel.addUserList(lista)
            setUserValueChangedListener(userViewModel)

        }.addOnFailureListener {
            Toast.makeText(context, "Users not downloaded!", Toast.LENGTH_LONG).show()
        }
    }

    fun getAllFriendRequests(context: Context, userViewModel: userViewModel) {
        val userMe: User? = userViewModel.user.value // = firebaseAuth.currentUser

        cloudFirestore.collection("friendRequests").get().addOnSuccessListener {
            val document = it.documents
            val lista: ArrayList<friendRequest> = arrayListOf<friendRequest>()
            document.forEach { singleRequestDocument ->

                var friendRequestTmp: friendRequest = friendRequest(
                    "",
                    "",
                    ""
                )

                //user = result.toObject<User>()
                friendRequestTmp.id = singleRequestDocument.data?.get("id").toString()
                friendRequestTmp.issuedBy = singleRequestDocument.data?.get("issuedBy").toString()
                friendRequestTmp.requestTo = singleRequestDocument.data?.get("requestTo").toString()

                lista.add(friendRequestTmp)

            }
            userViewModel.addFriendsRequestList(lista)
        }.addOnFailureListener {
            Toast.makeText(context, "requests not downoloaded!", Toast.LENGTH_LONG).show()
        }
    }

    fun sendFriendsRequest(
        userId: String,
        context: Context,
        pd: ProgressDialog,
        sendRequestButton: Button,
        userViewModel: userViewModel
    ) {

//        val request = hashMapOf(
//            "issuedBy" to FirebaseAuth.getInstance().currentUser?.uid,
//            "requestTo" to userId
//        )


        val newRef = cloudFirestore.collection("friendRequests").document()
        val id = newRef.id

        val newRequest: friendRequest? =
            userViewModel.user.value?.id?.let { friendRequest(id, it, userId) }


        if (newRequest != null) {
            newRef.set(newRequest)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successful added data", Toast.LENGTH_LONG).show()
                    pd.dismiss()
                    sendRequestButton.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                R.color.yellow
                            )
                        )
                    )
                    sendRequestButton.setText("REQUEST SENT")
                    sendRequestButton.isClickable = false
                    userViewModel.addNewFriendRequest(newRequest)

                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                    pd.dismiss()
                }
        }
    }


    fun getAllInvitations(requireContext: Context, userViewModel: userViewModel) {

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
            "1",
            "ficax00@gmail.com",
            "Aa1234.",
            "ficax99",
            "Trajkovic",
            "0616440562",
            0f,
            0,
            "",
            hashMapOf<String, Boolean>()
        )

        cloudFirestore.collection("users").document("SFKdYaQ7ZwBsuGuUN13Y").delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Successful added data", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
            }

    }

    //TESTIRATI
    fun declineRequest(
        singleRequest: friendRequest,
        context: Context,
        userViewModel: userViewModel,
        viewItem: View,
        invitationsLayout: LinearLayout
    ) {
        val newRef = cloudFirestore.collection("friendRequests").document(singleRequest.id)
            .delete().addOnSuccessListener {
                Toast.makeText(context, "Request deleted! " + it.toString(), Toast.LENGTH_LONG)
                    .show()

                val updatedRequest: friendRequest? =
                    userViewModel.friendReqeusts.value?.filter { it.id.equals(singleRequest.id) }
                        ?.firstOrNull()
                val updatedListOfRequests: List<friendRequest>? =
                    userViewModel.friendReqeusts.value?.filter { !it.id.equals(singleRequest.id) }

                updatedListOfRequests?.minus(updatedRequest)
                userViewModel.addFriendsRequestList(updatedListOfRequests!!)

            }.addOnFailureListener {
                Toast.makeText(context, "Request not deleted! " + it.toString(), Toast.LENGTH_LONG)
                    .show()

            }

    }

    fun acceptRequest(
        singleRequest: friendRequest,
        context: Context,
        userViewModel: userViewModel,
        viewItem: View,
        invitationsLayout: LinearLayout
    ) {

        try {

            cloudFirestore.collection("friendRequests").document(singleRequest.id).delete()
                .addOnSuccessListener {

                    val hashMapRequestToUser: HashMap<String, Boolean> =
                        hashMapOf(singleRequest.requestTo to true)
                    var tmpMapa: HashMap<String, Boolean> = userViewModel.user.value!!.friendsList
                    tmpMapa.put(singleRequest.issuedBy, true)

                    val hashMapIssuedByUser = hashMapOf(singleRequest.issuedBy to true)
                    val tmpMapa2: HashMap<String, Boolean> =
                        userViewModel.users.value?.filter { it.id.equals(singleRequest.issuedBy) }
                            ?.firstOrNull()!!.friendsList
                    tmpMapa2.put(singleRequest.requestTo, true)

                    try {

                        cloudFirestore.collection("Profiles")
                            .document(singleRequest.issuedBy)
                            .get().addOnSuccessListener {
//                        var hashMapa = it.data?.getValue("friendsList")
//                        var hashMap: HashMap<String, Boolean> = hashMapa as HashMap<String, Boolean>
//                        hashMapRequestToUser.plus(hashMap)

                            }

                        cloudFirestore.collection("Profiles")
                            .document(singleRequest.issuedBy)
                            .update(mapOf("friendsList" to tmpMapa2)) //.set(hashMapRequestToUser, SetOptions.merge())
                            .addOnSuccessListener {

                                cloudFirestore.collection("Profiles")
                                    .document(singleRequest.requestTo).get().addOnSuccessListener {
//                                var hashMapa = it.data?.getValue("friendsList")
//                                var hashMap: HashMap<String, Boolean> = hashMapa as HashMap<String, Boolean>
//                                hashMapIssuedByUser.plus(hashMap)
                                    }

                                cloudFirestore.collection("Profiles")
                                    .document(singleRequest.requestTo)
                                    .update(mapOf("friendsList" to tmpMapa as Map<String, Any>))
//                            .set(hashMapIssuedByUser, SetOptions.merge())

                                val updatedRequest: friendRequest? =
                                    userViewModel.friendReqeusts.value?.filter {
                                        it.id.equals(
                                            singleRequest.id
                                        )
                                    }
                                        ?.firstOrNull()
                                val updatedListOfRequests: List<friendRequest>? =
                                    userViewModel.friendReqeusts.value?.filter {
                                        !it.id.equals(
                                            singleRequest.id
                                        )
                                    }

                                updatedListOfRequests?.minus(updatedRequest)
                                userViewModel.addFriendsRequestList(updatedListOfRequests!!)

//                        val tmpLista =
//                            userViewModel.friendReqeusts.value?.filter { !it.id.equals(singleRequest.id) }
//                        userViewModel.addFriendsRequestList(tmpLista!!)

                                val updatedUser: User = userViewModel.user.value!!
                                updatedUser.friendsList.put(singleRequest.issuedBy, true)
                                userViewModel.setNewUser(updatedUser)

                                val updatedFriend: User? =
                                    userViewModel.users.value?.filter { it.id.equals(singleRequest.issuedBy) }
                                        ?.firstOrNull()
                                updatedFriend?.friendsList?.put(singleRequest.requestTo, true)


                                val updatedList: MutableList<User>? =
                                    userViewModel.users.value?.filter { !it.id.equals(singleRequest.issuedBy) } as MutableList<User>?


                                updatedList?.add(updatedFriend!!)

                                userViewModel.addUserList(updatedList!!)

                                invitationsLayout.removeView(viewItem)


                            }.addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Data not updated to profiles!",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }

                    } catch (e: Exception) {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                        cloudFirestore.collection("friendRequests").document(singleRequest.id)
                            .set(singleRequest)

                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Request not deleted! " + it.toString(),
                        Toast.LENGTH_LONG
                    )
                        .show()

                }
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()

        }
    }


    fun acceptJobInvitation(
        event: Event, userViewModel: userViewModel,
        pd: ProgressDialog,
        viewItem: View,
        invitationsLayout: LinearLayout,
        requireContext: Context
    ) {
        val meId = userViewModel.user.value?.id

        var currentEvent: Event =
            userViewModel.events.value!!.filter { it.id.equals(event.id) }.firstOrNull()!!

        var tmpMapa: MutableMap<String, String> =
            currentEvent.listOfUsers as MutableMap<String, String>
        tmpMapa.set(meId.toString(), "true")


        cloudFirestore.collection("events")
            .document(event.id)
            .update(mapOf("listOfUsers" to tmpMapa as Map<String, Any>)).addOnSuccessListener {

                //Toast.makeText(requireContext, "Event updated", Toast.LENGTH_SHORT).show()
                userViewModel.updateJob(event, meId)
                pd.dismiss()
                //invitationsLayout.removeView(viewItem)

            }.addOnFailureListener {

                Toast.makeText(requireContext, "Event not updated", Toast.LENGTH_SHORT).show()
                pd.dismiss()
            }
    }

    fun declineJobInvitation(
        event: Event, userViewModel: userViewModel,
        pd: ProgressDialog,
        viewItem: View?,
        invitationsLayout: LinearLayout?,
        requireContext: Context
    ) {
        val meId = userViewModel.user.value?.id

        var currentEvent: Event =
            userViewModel.events.value!!.filter { it.id.equals(event.id) }.firstOrNull()!!

        var tmpMapa: MutableMap<String, String> =
            currentEvent.listOfUsers as MutableMap<String, String>
        tmpMapa.remove(meId.toString())


        cloudFirestore.collection("events")
            .document(event.id)
            .update(mapOf("listOfUsers" to tmpMapa as Map<String, Any>)).addOnSuccessListener {

                //Toast.makeText(requireContext, "Event updated", Toast.LENGTH_SHORT).show()
                //userViewModel.updateJob(event, meId)
                pd.dismiss()
                //invitationsLayout.removeView(viewItem)

            }.addOnFailureListener {

                Toast.makeText(requireContext, "Event not updated", Toast.LENGTH_SHORT).show()
                pd.dismiss()
            }
    }


    fun postComment(
        newComment: Comment,
        requireContext: Context,
        userViewModel: userViewModel,
        pd: ProgressDialog,
        commentText: TextInputEditText
    ) {

        val newRef = cloudFirestore.collection("comments").document()
        val id = newRef.id

        newRef.set(newComment)
            .addOnSuccessListener {
                Toast.makeText(requireContext, "Successful added data", Toast.LENGTH_LONG).show()
                pd.dismiss()

                var listaKomentara: MutableList<Comment> =
                    (userViewModel.comments.value as MutableList<Comment>?)!!
                listaKomentara.add(newComment)
                userViewModel.addCommentList(listaKomentara)
                commentText.text?.clear()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext, it.toString(), Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
    }

    fun getAllCommentsForSingleUser(
        userId: String,
        userViewModel: userViewModel,
        context: Context
    ) {

        cloudFirestore.collection("comments").whereEqualTo("issuedFor", userId).get()
            .addOnSuccessListener {
                val comments = it.documents
                val lista: ArrayList<Comment> = arrayListOf<Comment>()
                comments.forEach { singleRequestDocument ->

                    var commentTmp: Comment = Comment(
                        "",
                        "",
                        ""
                    )

                    //user = result.toObject<User>()
                    commentTmp.text = singleRequestDocument.data?.get("text").toString()
                    commentTmp.issuedBy = singleRequestDocument.data?.get("issuedBy").toString()
                    commentTmp.issuedFor =
                        singleRequestDocument.data?.get("issuedFor").toString()

                    lista.add(commentTmp)

                }
                userViewModel.addCommentList(lista)
            }.addOnFailureListener {
                Toast.makeText(context, "comments not downoloaded!", Toast.LENGTH_LONG).show()
            }
    }

    fun postRating(
        newRating: Rating,
        requireContext: Context,
        userViewModel: userViewModel,
        pd: ProgressDialog,
        commentText: TextInputEditText
    ) {
        val newRef = cloudFirestore.collection("ratings").document()
        val id = newRef.id
        newRating.id = id

        val checkRef = cloudFirestore.collection("ratings")
            .whereEqualTo("issuedBy", userViewModel.user.value?.id)
            .whereEqualTo("issuedFor", newRating.issuedFor).get().addOnSuccessListener {

                var ratings = it.documents.firstOrNull()
                if (ratings != null) {

                    var ratingTmp: Rating = Rating("", "", "", 0f)
                    newRating.id = ratings?.data?.get("id").toString()
                    cloudFirestore.collection("ratings").document(newRating.id)
                        .update("score", newRating.score)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext,
                                "Successful updated data",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            pd.dismiss()

                            val updatedRating: Rating? =
                                userViewModel.ratings.value?.filter { it.id.equals(newRating.id) }
                                    ?.firstOrNull()

                            updatedRating?.score = newRating.score

                            val updatedList: MutableList<Rating>? =
                                userViewModel.ratings.value?.filter { !(it.id.equals(newRating.id)) } as MutableList<Rating>?


                            updatedList?.add(updatedRating!!)
                            userViewModel.addRatingList(updatedList!!)

                        }.addOnFailureListener {
                            Toast.makeText(
                                requireContext,
                                "Not successful updated data",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            pd.dismiss()
                        }
                } else {
                    newRef.set(newRating)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext,
                                "Successful added data",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            pd.dismiss()

                            var listaRatinga: MutableList<Rating> =
                                (userViewModel.ratings.value as MutableList<Rating>?)!!
                            listaRatinga.add(newRating)

                            userViewModel.addRatingList(listaRatinga)
                            //commentText.text?.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext, it.toString(), Toast.LENGTH_LONG)
                                .show()
                            pd.dismiss()
                        }
                }

            }.addOnFailureListener {
                Toast.makeText(requireContext, it.toString(), Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
    }

    fun getAllRatingsForSingleUser(
        userId: String,
        userViewModel: userViewModel,
        context: Context
    ) {

        cloudFirestore.collection("ratings").whereEqualTo("issuedFor", userId).get()
            .addOnSuccessListener {
                val ratings = it.documents
                val lista: ArrayList<Rating> = arrayListOf<Rating>()
                ratings.forEach { singleRequestDocument ->

                    var ratingTmp: Rating = Rating(
                        "",
                        "",
                        "",
                        0f
                    )

                    //user = result.toObject<User>()
                    ratingTmp.id = singleRequestDocument.data?.get("id").toString()
                    val score = singleRequestDocument.data?.get("score").toString()
                    ratingTmp.score = score.toFloat()
                    ratingTmp.issuedBy = singleRequestDocument.data?.get("issuedBy").toString()
                    ratingTmp.issuedFor =
                        singleRequestDocument.data?.get("issuedFor").toString()

                    lista.add(ratingTmp)

                }

                var listaRatinga: MutableList<Rating>? =
                    userViewModel.ratings.value as MutableList<Rating>?

                if (listaRatinga.isNullOrEmpty()) {
                    listaRatinga = mutableListOf()
                    listaRatinga = listaRatinga.plus(lista) as MutableList<Rating>
                } else
                    listaRatinga = listaRatinga.plus(lista) as MutableList<Rating>
//                listaRatinga = (listaRatinga + lista) as MutableList<Rating>

                userViewModel.addRatingList(listaRatinga)

                //userViewModel.addRatingList(lista)
            }.addOnFailureListener {
                Toast.makeText(context, "Ratings not downloaded!", Toast.LENGTH_LONG).show()
            }
    }

    fun getAllRatings(userViewModel: userViewModel, context: Context) {

        cloudFirestore.collection("ratings").get()
            .addOnSuccessListener {
                val ratings = it.documents
                val lista: ArrayList<Rating> = arrayListOf<Rating>()
                ratings.forEach { singleRequestDocument ->

                    var ratingTmp: Rating = Rating(
                        "",
                        "",
                        "",
                        0f
                    )

                    //user = result.toObject<User>()
                    ratingTmp.id = singleRequestDocument.data?.get("id").toString()
                    val score = singleRequestDocument.data?.get("score").toString()
                    ratingTmp.score = score.toFloat()
                    ratingTmp.issuedBy = singleRequestDocument.data?.get("issuedBy").toString()
                    ratingTmp.issuedFor =
                        singleRequestDocument.data?.get("issuedFor").toString()

                    lista.add(ratingTmp)

                }
                userViewModel.addRatingList(lista)
            }.addOnFailureListener {
                Toast.makeText(context, "Ratings not downloaded!", Toast.LENGTH_LONG).show()
            }
    }

    fun createEvent(
        eventTmp: Event?,
        requireContext: Context,
        pd: ProgressDialog,
        userViewModel: userViewModel
    ) {
        val newRef = cloudFirestore.collection("events").document()
        val id = newRef.id

        var date: String? = eventTmp?.date.toString()
        val lista = date?.split(" ")
        val dan = lista?.get(2)?.toInt()

        val dateHashMap = hashMapOf<String, Int>(
            "year" to eventTmp?.date?.year!!,
            "month" to eventTmp?.date?.month!!,
            "day" to dan!!,
            "hours" to eventTmp?.date?.hours!!,
            "minutes" to eventTmp?.date?.minutes!!
        )
        eventTmp?.id = id
        eventTmp.dateHashMap = dateHashMap
        newRef.set(eventTmp!!)
            .addOnSuccessListener {
                Toast.makeText(requireContext, "Successful added data", Toast.LENGTH_LONG).show()

//                var lista = userViewModel.events.value
//                var novaLista = lista as MutableList<Event>
//                novaLista.add(eventTmp)
//                userViewModel.addEventList(novaLista)

                var listaEventa: MutableList<Event> =
                    (userViewModel.events.value as MutableList<Event>?)!!
                listaEventa.add(eventTmp)
                userViewModel.addEventList(listaEventa)

                pd.dismiss()

            }
            .addOnFailureListener {
                Toast.makeText(requireContext, it.toString(), Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
    }

    fun getAllEvents(context: Context, userViewModel: userViewModel) {

        cloudFirestore.collection("events").get().addOnSuccessListener {
            val document = it.documents
            val lista: ArrayList<Event> = arrayListOf<Event>()
            document.forEach { singleRequestDocument ->

                var eventTmp: Event = Event(
                    "",
                    "",
                    false,
                    "",
                    0.0,
                    0.0,
                    Date(),
                    hashMapOf(),
                    hashMapOf(),
                    mutableListOf()
                )

                //user = result.toObject<User>()
                eventTmp.id = singleRequestDocument.data?.get("id").toString()
                eventTmp.name = singleRequestDocument.data?.get("name").toString()
                eventTmp.finished = singleRequestDocument.data?.get("finished") as Boolean

                eventTmp.organiser = singleRequestDocument.data?.get("organiser").toString()
                var tmpString: String = singleRequestDocument.data?.get("latitude").toString()
                eventTmp.latitude = tmpString.toDouble()
                tmpString = singleRequestDocument.data?.get("longitude").toString()
                eventTmp.longitude = tmpString.toDouble()

//                val tmpDate = singleRequestDocument.data?.get("date")
//                //eventTmp.date = Date()
//                val timestamp: com.google.firebase.Timestamp =
//                    tmpDate as com.google.firebase.Timestamp
//                val date: Date = getUTCdatetimeAsDate(timestamp)

                val dateHashMap = singleRequestDocument.data?.getValue("dateHashMap")
                val hashMapaDate: HashMap<String, Int> = dateHashMap as HashMap<String, Int>
                var month = hashMapaDate["month"]!!
                var monthInt: Int? = month?.toInt()//?.minus(1)
                var date: Date = Date(
                    hashMapaDate.get("year")!!,
                    monthInt!!,
                    hashMapaDate["day"]!!,
                    hashMapaDate["hours"]!!,
                    hashMapaDate["minutes"]!!
                )

                eventTmp.date = date //Date(timestamp.seconds * 1000)

                //PARSIRANJE VREME NE RADII KAKO TREBA, RESENJE JE DA SE DATUM U BAZI PAMTI KAO STRING ILI KAO HASHMAP SA POLJIMA ZA SVAKU VREDNOST

                var listOfUsersTmp = singleRequestDocument.data?.getValue("listOfUsers")
                eventTmp.listOfUsers = listOfUsersTmp as HashMap<String, Boolean>

                lista.add(eventTmp)

            }
            userViewModel.addEventList(lista)
            setEventValueChangedListener(userViewModel)
        }.addOnFailureListener {
            Toast.makeText(context, "requests not downoloaded!", Toast.LENGTH_LONG).show()
        }
    }

    const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun getUTCdatetimeAsDate(utcString: Timestamp): Date {
        // note: doesn't check for null
        return stringDateToDate(utcString)
    }


    fun stringDateToDate(StrDate: Timestamp): Date {
        val sdf = SimpleDateFormat(DATE_FORMAT)
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+2"))
        val date = sdf.format(Date(StrDate.seconds * 1000))

        var dateToReturn: Date = Date()
        val dateFormat = SimpleDateFormat(DATE_FORMAT)
        try {
            dateToReturn = dateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateToReturn
    }

    fun updateEventData(
        selectedEvent: Event,
        mapa: HashMap<String, Any>,
        pd: ProgressDialog,
        requireContext: Context,
        findNavController: NavController
    ) {

        cloudFirestore.collection("events").document(
            selectedEvent.id
        ).update(mapa as Map<String, Any>).addOnSuccessListener {
            Toast.makeText(requireContext, "Data updated successfully!", Toast.LENGTH_LONG)
                .show()
            pd.dismiss()
            //findNavController.popBackStack()
        }.addOnFailureListener {
            Toast.makeText(requireContext, "Data not updated !", Toast.LENGTH_LONG).show()
            pd.dismiss()
        }
    }

    fun addPhotosToDatabase(
        selectedEvent: Event,
        photosListTmp: MutableList<Bitmap>,
        requireContext: Context,
        findNavController: NavController
    ) {

        photosListTmp.forEach { element ->

            val baos = ByteArrayOutputStream()
            element.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val data = baos.toByteArray()

            //storageRef.child("EventImages").child("${selectedEvent.id}").child(UUID.randomUUID().toString())

            val mountainImagesRef =
                storageRef.child("EventImages").child(selectedEvent.id)
                    .child(UUID.randomUUID().toString() + ".png")
            uploadTask = mountainImagesRef.putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                Log.d("PictureFail", "This is a picture upload failure")
            }.addOnSuccessListener { taskSnapshot ->

                Toast.makeText(requireContext, taskSnapshot.toString(), Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireContext, it.toString(), Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun deleteSinglePhotoForSelectedEventFromDatabase(
        viewItem: View,
        photo: String,
        selectedEvent: Event,
        requireContext: Context,
        gsPhotosList: List<String>,
        userViewModel: userViewModel
    ) {

        val listOfPhotos: MutableList<String> = mutableListOf()
        gsPhotosList.forEach { element ->
            val StringList: List<String> = element.split("/")
            listOfPhotos.add(StringList.lastOrNull()?.split(".")?.firstOrNull()!!)
        }

        val itemToDelete = listOfPhotos.filter { photo.contains(it.toString()) }.firstOrNull()


        val desertRef =
            storageRef.child("EventImages/${selectedEvent.id}/" + itemToDelete + ".png")

        desertRef.delete().addOnSuccessListener {
            Toast.makeText(requireContext, "File deleted successfully!", Toast.LENGTH_SHORT)
                .show()
            (viewItem.getParent() as ViewGroup).removeView(viewItem)
            userViewModel.deletePhotoFromSelectedEvent(photo)
        }.addOnFailureListener {

            Toast.makeText(requireContext, it.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    fun getPhotosForSingleEventFromDatabase(
        selectedEvent: Event,
        requireContext: Context,
        userViewModel: userViewModel
    ) {

        val photosListTmp: MutableList<Bitmap> = mutableListOf()
        val photosListStringTmp: MutableList<String> = mutableListOf()
        val photosListGSStringTmp: MutableList<String> = mutableListOf()


        val mountainImagesRef =
            storageRef.child("EventImages").child(selectedEvent.id).listAll()
                .addOnSuccessListener {

//                it.items.forEach { item ->
////                    val inst = FirebaseStorage.getInstance()
////                    val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(item.toString())
////                    imageRef.getBytes(10 * 1024 * 1024).addOnSuccessListener {
////                        val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
////                        photosListTmp.add(bitmap)
////                    }.addOnFailureListener {
////                       Log.d("1", it.toString())
////                    }
//                    var downloadUrl = item.downloadUrl
//                    photosListStringTmp.add(downloadUrl.toString())
//
//                }
//                it.items.forEach { item ->
//                    val FIVE_MEGABYTE: Long = 5 * 1024 * 1024
//
//                    item.getBytes(FIVE_MEGABYTE).addOnSuccessListener {
//
//                        val arrayInputStream = ByteArrayInputStream(it as ByteArray?)
//                        val bitmap = BitmapFactory.decodeStream(arrayInputStream)
//                        photosListTmp.add(bitmap)
//
//                    }.addOnFailureListener{
//                        Toast.makeText(requireContext, it.toString(), Toast.LENGTH_SHORT).show()
//                    }
//                }
                    // photosListStringTmp = it.items
                    it.items.forEach { item ->
                        photosListGSStringTmp.add(item.toString())
                        val gsReference =
                            FirebaseStorage.getInstance().getReferenceFromUrl(item.toString())
                        gsReference.downloadUrl.addOnSuccessListener {
                            val string = it.toString()
                            userViewModel.setPhotoUrlToSelectedEvent(string.toString())

                        }
                    }
                    userViewModel.setSelectedEventGSPhotos(photosListGSStringTmp)
//
//                userViewModel.setSelectedEventPhotos(photosListStringTmp)

                }.addOnFailureListener {
                    // Uh-oh, an error occurred!
                }
    }

    fun sendAskToJoinRequest(
        eventid: String,
        requireContext: Context,
        pd: ProgressDialog,
        sendRequestButton: Button,
        userViewModel: userViewModel,
        context: Context
    ) {

        val newRef = cloudFirestore.collection("askToJoins").document()
        val id = newRef.id

        val newAskToJoin: askToJoin? =
            userViewModel.user.value?.id?.let { askToJoin(id, it, eventid) }


        if (newAskToJoin != null) {
            newRef.set(newAskToJoin)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successful added data", Toast.LENGTH_LONG).show()
                    pd.dismiss()
                    sendRequestButton.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                context,
                                R.color.red
                            )
                        )
                    )
                    sendRequestButton.setText("REQUEST SENT")
                    sendRequestButton.isClickable = false
                    userViewModel.addNewAskToJoin(newAskToJoin)

                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                    pd.dismiss()
                }
        }
    }

    fun getAllAskToJoins(requireContext: Context, userViewModel: userViewModel) {

        cloudFirestore.collection("askToJoins").get().addOnSuccessListener {
            val document = it.documents
            val lista: ArrayList<askToJoin> = arrayListOf<askToJoin>()
            document.forEach { singleRequestDocument ->

                var askToJoinTmp: askToJoin = askToJoin(
                    "",
                    "",
                    ""
                )

                //user = result.toObject<User>()
                askToJoinTmp.id = singleRequestDocument.data?.get("id").toString()
                askToJoinTmp.issuedBy = singleRequestDocument.data?.get("issuedBy").toString()
                askToJoinTmp.joinToJob = singleRequestDocument.data?.get("joinToJob").toString()

                lista.add(askToJoinTmp)

            }
            userViewModel.addAskToJoinList(lista)
        }.addOnFailureListener {
            Toast.makeText(requireContext, "Requests not downloaded!", Toast.LENGTH_LONG).show()
        }
    }


    fun declineAskToJoin(
        singleAskToJoin: askToJoin,
        context: Context,
        userViewModel: userViewModel,
        viewItem: View,
        invitationsLayout: LinearLayout
    ) {
        val newRef = cloudFirestore.collection("askToJoins").document(singleAskToJoin.id)
            .delete().addOnSuccessListener {
                Toast.makeText(context, "Ask to join deleted! ", Toast.LENGTH_LONG)
                    .show()

                val updatedAskToJoin: askToJoin? =
                    userViewModel.askToJoin.value?.filter { it.id.equals(singleAskToJoin.id) }
                        ?.firstOrNull()
                var updatedListOfRequests: List<askToJoin>? =
                    userViewModel.askToJoin.value?.filter { !it.id.equals(singleAskToJoin.id) }

                updatedListOfRequests =
                    (updatedListOfRequests?.minus(updatedAskToJoin) as List<askToJoin>?)!!
                userViewModel.addAskToJoinList(updatedListOfRequests!!)

                invitationsLayout.removeView(viewItem)

            }.addOnFailureListener {
                Toast.makeText(context, "Request not deleted! " + it.toString(), Toast.LENGTH_LONG)
                    .show()

            }

    }

    fun acceptAskToJoin(
        singleAskToJoin: askToJoin,
        event: Event,
        context: Context,
        userViewModel: userViewModel,
        viewItem: View,
        invitationsLayout: LinearLayout
    ) {

        try {

            cloudFirestore.collection("askToJoins").document(singleAskToJoin.id).delete()
                .addOnSuccessListener {

                    var tmpMapa: MutableMap<String, String> =
                        event.listOfUsers as MutableMap<String, String>
                    tmpMapa.put(singleAskToJoin.issuedBy, "true")

                    try {

                        cloudFirestore.collection("events")
                            .document(singleAskToJoin.joinToJob)
                            .get().addOnSuccessListener {
//                        var hashMapa = it.data?.getValue("friendsList")
//                        var hashMap: HashMap<String, Boolean> = hashMapa as HashMap<String, Boolean>
//                        hashMapRequestToUser.plus(hashMap)

                            }

                        cloudFirestore.collection("events")
                            .document(singleAskToJoin.joinToJob)
                            .update(mapOf("listOfUsers" to tmpMapa)) //.set(hashMapRequestToUser, SetOptions.merge())
                            .addOnSuccessListener {


                                val updatedAskToJoin: askToJoin? =
                                    userViewModel.askToJoin.value?.filter {
                                        it.id.equals(
                                            singleAskToJoin.id
                                        )
                                    }
                                        ?.firstOrNull()
                                var updatedListOfRequests: List<askToJoin>? =
                                    userViewModel.askToJoin.value?.filter {
                                        !it.id.equals(
                                            singleAskToJoin.id
                                        )
                                    }

                                updatedListOfRequests =
                                    (updatedListOfRequests?.minus(updatedAskToJoin) as List<askToJoin>?)!!
                                userViewModel.addAskToJoinList(updatedListOfRequests!!)


                                val updatedEvent: Event? =
                                    userViewModel.events.value?.filter {
                                        it.id.equals(
                                            singleAskToJoin.joinToJob
                                        )
                                    }
                                        ?.firstOrNull()

//                                updatedEvent?.listOfUsers?.put(singleAskToJoin.issuedBy, "true")

                                userViewModel.addNewUserToJob(event, singleAskToJoin.issuedBy)



                                invitationsLayout.removeView(viewItem)

                            }.addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Data not updated to profiles!",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }

                    } catch (e: Exception) {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                        cloudFirestore.collection("askToJoin").document(singleAskToJoin.id)
                            .set(singleAskToJoin)

                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Request not deleted! " + it.toString(),
                        Toast.LENGTH_LONG
                    )
                        .show()

                }
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()

        }
    }

    fun postMyLocation(myLocation: org.osmdroid.util.GeoPoint, userViewModel: userViewModel) {

        val userMap = mapOf(
            "lat" to myLocation?.latitude,
            "lon" to myLocation?.longitude
        )

        userViewModel.user.value?.id?.let {
            realtimeDatabase.getReference("map").child("users")
                .child(it).setValue(userMap)
        }
    }

    fun postMyServiceValue(
        serviceActivatedBoolean: Boolean,
        userViewModel: userViewModel,
        requireContext: Context
    ) {

        val userMap = mapOf(
            "serviceActive" to serviceActivatedBoolean
        )

        userViewModel.user.value?.id?.let {
            realtimeDatabase.getReference("users").child("serviceValues")
                .child(it).setValue(userMap).addOnSuccessListener {
                    userViewModel.setBackGroundService(serviceActivatedBoolean)

                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext,
                        "UserService value not posted to database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    fun postMyOnlineValue(
        online: Boolean,
        userViewModel: userViewModel,
        requireContext: Context
    ) {

        val userMap = mapOf(
            "online" to online
        )

        userViewModel.user.value?.id?.let {
            realtimeDatabase.getReference("users").child("online")
                .child(it).setValue(userMap).addOnSuccessListener {
                    //userViewModel.setBackGroundService(serviceActivatedBoolean)
                    userViewModel.setOnlineValueForUser(online)
                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext,
                        "UserService value not posted to database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    fun getOnlineValues(userViewModel: userViewModel, requireContext: Context) {

        userViewModel.user.value?.id?.let {
            realtimeDatabase.getReference("users").child("online")
                .get().addOnSuccessListener {
                    if (it.exists()) {
                        val temp: HashMap<Any, Any> = it.value as HashMap<Any, Any>
                        val creatingHashMap: HashMap<String, Boolean> = hashMapOf()
//                        var serviceItem: Boolean = false
                        var tmpHashMap: HashMap<String, Boolean> = hashMapOf()

                        temp.forEach { item ->
                            if (item.key == userViewModel.user.value!!.id) {
                                tmpHashMap = item.value as HashMap<String, Boolean>
                                tmpHashMap.get("online")
                                    ?.let { it1 -> userViewModel.setOnlineValueForUser(it1) }
                            }
//                            serviceItem = item.value as Boolean
                            tmpHashMap = item.value as HashMap<String, Boolean>
                            tmpHashMap.get("online")
                                ?.let { it1 -> creatingHashMap.put(item.key as String, it1) }
                        }
                        userViewModel.setOnlineUsersMap(creatingHashMap)
                    } else {
                        userViewModel.setOnlineUsersMap(hashMapOf<String, Boolean>())
                    }
                    setOnlineValueEventListener(userViewModel, requireContext)

                }.addOnFailureListener {
                    Toast.makeText(requireContext, "UserService not fetched", Toast.LENGTH_SHORT)
                        .show()
                    userViewModel.setBackGroundService(false)
                }
        }
    }

    fun setOnlineValueEventListener(userViewModel: userViewModel, requireContext: Context) {
        userViewModel.user.value?.id?.let {
            realtimeDatabase.getReference("users").child("online").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(it: DataSnapshot) {
                    if (it.exists()) {
                        val temp: HashMap<Any, Any> = it.value as HashMap<Any, Any>
                        val creatingHashMap: HashMap<String, Boolean> = hashMapOf()
//                        var serviceItem: Boolean = false
                        var tmpHashMap: HashMap<String, Boolean> = hashMapOf()

                        temp.forEach { item ->
                            if (item.key == userViewModel.user.value!!.id) {
                                tmpHashMap = item.value as HashMap<String, Boolean>
                                tmpHashMap.get("online")
                                    ?.let { it1 -> userViewModel.setOnlineValueForUser(it1) }
                            }
//                            serviceItem = item.value as Boolean
                            tmpHashMap = item.value as HashMap<String, Boolean>
                            tmpHashMap.get("online")
                                ?.let { it1 -> creatingHashMap.put(item.key as String, it1) }
                        }
                        userViewModel.setOnlineUsersMap(creatingHashMap)
                    } else {
                        userViewModel.setOnlineUsersMap(hashMapOf<String, Boolean>())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }
    }

    fun getMyServiceValue(userViewModel: userViewModel, requireContext: Context) {

        userViewModel.user.value?.id?.let {
            realtimeDatabase.getReference("users").child("serviceValues")
                .child(it).get().addOnSuccessListener {
                    if (it.exists()) {
                        val temp: HashMap<Any, Any> = it.value as HashMap<Any, Any>
                        var serviceItem: Boolean = false
                        temp.forEach { item ->
                            serviceItem = item.value as Boolean
                        }
                        userViewModel.setBackGroundService(serviceItem)
                    } else {
                        userViewModel.setBackGroundService(false)

                    }

                }.addOnFailureListener {
                    Toast.makeText(requireContext, "UserService not fetched", Toast.LENGTH_SHORT)
                        .show()
                    userViewModel.setBackGroundService(false)
                }
        }
    }

    fun getUserLocationsData(userViewModel: userViewModel) {

        val dataRef = realtimeDatabase.getReference("map")
        dataRef.child("users").get().addOnSuccessListener {
            val temp: HashMap<Any, Any> = it.value as HashMap<Any, Any>

            val userLocations: MutableList<UserLocation> = mutableListOf()

            temp.forEach { entry ->
                if (!entry.key.toString().equals(userViewModel.user.value?.id)) {

                    val eventMap = entry.value as HashMap<String, Any>
                    val event = UserLocation(
                        entry.key.toString(),
                        eventMap["lat"] as Double,
                        eventMap["lon"] as Double
                    )
                    userLocations.add(event)
                }
            }
            userViewModel.addUserLocationsList(userLocations)
        }

    }

    fun setUserLocationEventListener(userViewModel: userViewModel) {
        val dataRef = realtimeDatabase.getReference("map")
        dataRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                getUserLocationsData(userViewModel)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun sendInviteFriend(
        selectedUser: User?,
        selectedEvent: Event,
        userViewModel: userViewModel,
        dialog: Dialog?,
        requireContext: Context
    ) {

        var tmpMapa: MutableMap<String, Boolean> =
            selectedEvent.listOfUsers as MutableMap<String, Boolean>
        tmpMapa.put(selectedUser?.id!!, false)

        try {

            cloudFirestore.collection("events")
                .document(selectedEvent.id)
                .update(mapOf("listOfUsers" to tmpMapa)) //.set(hashMapRequestToUser, SetOptions.merge())
                .addOnSuccessListener {

                    val updatedEvent: Event? =
                        userViewModel.events.value?.filter {
                            it.id.equals(
                                selectedEvent.id
                            )
                        }
                            ?.firstOrNull()
                    //updatedEvent?.listOfUsers?.put(selectedUser.id, "false")

                    userViewModel.addNewInvitedUserToJob(selectedEvent, selectedUser.id)
                    dialog?.dismiss()

                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext,
                        "Data not updated!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

        } catch (e: Exception) {
            Toast.makeText(requireContext, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun setEventValueChangedListener(userViewModel: userViewModel) {
        cloudFirestore.collection("events").addSnapshotListener { snapshots, e ->
            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        var eventTmp: Event = Event(
                            "",
                            "",
                            false,
                            "",
                            0.0,
                            0.0,
                            Date(),
                            hashMapOf(),
                            hashMapOf(),
                            mutableListOf()
                        )

                        //user = result.toObject<User>()
                        eventTmp.id = dc.document.data?.get("id").toString()
                        eventTmp.name = dc.document.data?.get("name").toString()
                        eventTmp.finished = dc.document.data?.get("finished") as Boolean

                        eventTmp.organiser = dc.document.data?.get("organiser").toString()
                        var tmpString: String = dc.document.data?.get("latitude").toString()
                        eventTmp.latitude = tmpString.toDouble()
                        tmpString = dc.document.data?.get("longitude").toString()
                        eventTmp.longitude = tmpString.toDouble()

//                val tmpDate = singleRequestDocument.data?.get("date")
//                //eventTmp.date = Date()
//                val timestamp: com.google.firebase.Timestamp =
//                    tmpDate as com.google.firebase.Timestamp
//                val date: Date = getUTCdatetimeAsDate(timestamp)

                        val dateHashMap = dc.document.data?.getValue("dateHashMap")
                        val hashMapaDate: HashMap<String, Int> = dateHashMap as HashMap<String, Int>
                        var month = hashMapaDate["month"]!!
                        var monthInt: Int? = month?.toInt()//?.minus(1)
                        var date: Date = Date(
                            hashMapaDate.get("year")!!,
                            monthInt!!,
                            hashMapaDate["day"]!!,
                            hashMapaDate["hours"]!!,
                            hashMapaDate["minutes"]!!
                        )

                        eventTmp.date = date //Date(timestamp.seconds * 1000)

                        //PARSIRANJE VREME NE RADII KAKO TREBA, RESENJE JE DA SE DATUM U BAZI PAMTI KAO STRING ILI KAO HASHMAP SA POLJIMA ZA SVAKU VREDNOST

                        var listOfUsersTmp = dc.document.data?.getValue("listOfUsers")
                        eventTmp.listOfUsers = listOfUsersTmp as HashMap<String, Boolean>

                        var listaEventa: MutableList<Event> =
                            (userViewModel.events.value as MutableList<Event>?)!!
                        if (!listaEventa.contains(eventTmp))
                            listaEventa.add(eventTmp)
                        userViewModel.addEventList(listaEventa)
                    }
                    DocumentChange.Type.MODIFIED -> {
                        var eventTmp: Event = Event(
                            "",
                            "",
                            false,
                            "",
                            0.0,
                            0.0,
                            Date(),
                            hashMapOf(),
                            hashMapOf(),
                            mutableListOf()
                        )

                        //user = result.toObject<User>()
                        eventTmp.id = dc.document.data?.get("id").toString()
                        eventTmp.name = dc.document.data?.get("name").toString()
                        eventTmp.finished = dc.document.data?.get("finished") as Boolean

                        eventTmp.organiser = dc.document.data?.get("organiser").toString()
                        var tmpString: String = dc.document.data?.get("latitude").toString()
                        eventTmp.latitude = tmpString.toDouble()
                        tmpString = dc.document.data?.get("longitude").toString()
                        eventTmp.longitude = tmpString.toDouble()

//                val tmpDate = singleRequestDocument.data?.get("date")
//                //eventTmp.date = Date()
//                val timestamp: com.google.firebase.Timestamp =
//                    tmpDate as com.google.firebase.Timestamp
//                val date: Date = getUTCdatetimeAsDate(timestamp)

                        val dateHashMap = dc.document.data?.getValue("dateHashMap")
                        val hashMapaDate: HashMap<String, Int> = dateHashMap as HashMap<String, Int>
                        var month = hashMapaDate["month"]!!
                        var monthInt: Int? = month?.toInt()//?.minus(1)
                        var date: Date = Date(
                            hashMapaDate.get("year")!!,
                            monthInt!!,
                            hashMapaDate["day"]!!,
                            hashMapaDate["hours"]!!,
                            hashMapaDate["minutes"]!!
                        )

                        eventTmp.date = date //Date(timestamp.seconds * 1000)

                        //PARSIRANJE VREME NE RADII KAKO TREBA, RESENJE JE DA SE DATUM U BAZI PAMTI KAO STRING ILI KAO HASHMAP SA POLJIMA ZA SVAKU VREDNOST

                        var listOfUsersTmp = dc.document.data?.getValue("listOfUsers")
                        eventTmp.listOfUsers = listOfUsersTmp as HashMap<String, Boolean>

                        userViewModel.updateEvent(eventTmp.id, eventTmp)
                    }
                    DocumentChange.Type.REMOVED -> {

                    }
                }
            }

        }
    }

    fun setUserValueChangedListener(userViewModel: userViewModel) {

        val userMe: String? = userViewModel.user.value?.id


        cloudFirestore.collection("Profiles").addSnapshotListener { snapshots, e ->
            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {

                    }
                    DocumentChange.Type.MODIFIED -> {

                        try {
                            var userTmp: User = User(
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                0f,
                                0,
                                "",
                                hashMapOf<String, Boolean>()
                            )
                            //user = result.toObject<User>()
                            userTmp.id = dc.document.data?.get("id").toString()

                            userTmp.email = dc.document.data?.get("email").toString()
                            userTmp.userName = dc.document.data?.get("userName").toString()
                            userTmp.firstName = dc.document.data?.get("firstName").toString()
                            userTmp.lastName = dc.document.data?.get("lastName").toString()
                            userTmp.phoneNumber = dc.document.data?.get("phoneNumber").toString()
                            var tmpString: String = dc.document.data?.get("numOfRatings").toString()
                            userTmp.numOfRatings = tmpString.toInt()
                            tmpString = dc.document.data?.get("totalScore").toString()
                            userTmp.totalScore = tmpString.toFloat()

                            var hashMapa = dc.document.data?.getValue("friendsList")
                            userTmp.friendsList = hashMapa as HashMap<String, Boolean>

                            if (!(userTmp.id == userMe!!)) {

                                storageRef.child("ProfileImages/${userTmp.id}.png").downloadUrl.addOnSuccessListener {
                                    //userTmp.imageUrl = it.toString()
                                    userViewModel.setPhotoUrlToUser(userTmp.id, it.toString())
                                }

                                userViewModel.updateUser(userTmp.id, userTmp)
                            }

                        } catch (e: Exception) {
                            Log.d("exception", e.toString())
                        }
                    }
                    DocumentChange.Type.REMOVED -> {

                    }
                }
            }

        }

    }


}
