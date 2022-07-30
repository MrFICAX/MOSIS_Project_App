package elfak.mosis.freelencelive

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import elfak.mosis.freelencelive.data.Event
import elfak.mosis.freelencelive.databaseHelper.FirebaseHelper
import java.io.ByteArrayOutputStream
import java.sql.Date
import java.sql.Time

class TestDatabaseActivity : AppCompatActivity() {

    private val TAG = "101"
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var realtimeDatabase : FirebaseDatabase
    private lateinit var cloudFirestore : FirebaseFirestore
    private lateinit var cloudStorage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private lateinit var uploadTask: UploadTask

    private lateinit var myRef: DatabaseReference
    private lateinit var textBox: TextView
    private lateinit var textLoad: TextView
    private lateinit var buttonPost: Button
    private lateinit var buttonLoad: Button
    private lateinit var pBar: ProgressBar



    fun setDatabaseReference(){
        realtimeDatabase = Firebase.database
        cloudFirestore = Firebase.firestore
        cloudStorage = Firebase.storage
        storageRef = cloudStorage.reference

        myRef = realtimeDatabase.reference.child("message")//getReference("message")

    }

    fun postDataToCloudFirestore(){

//        val user = hashMapOf(
//            "first" to "Ada",
//            "last" to "Lovelace",
//            "born" to 1815
//        )
//
//        cloudFirestore.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }

        //PRIBAVLJANJE ID-JA ZA PRAZNI DOKUMENT KOJI NIJE DODAT U BAZU
//        val newRef = cloudFirestore.collection("users").document()
//        val id = newRef.id

        val user: MutableMap<String, Any> = HashMap()
        user["firstName"] = "Filip"
        user["lastName"] = "Trajkovic"
        cloudFirestore.collection("users")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Sucessful added data", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Not Sucessful added data", Toast.LENGTH_LONG).show()
            }

    }

    fun getDataFromCloudFirestore(){

        cloudFirestore.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    Toast.makeText(this, document.data.toString(), Toast.LENGTH_LONG).show()
                    val event: Event = Event("","","",0.0,0.0, Date(0, 0, 0), Time(0,0,0), hashMapOf())
                    event.name = document.data["firstName"].toString()
                    event.organiser = document.data["lastName"].toString()

                    Toast.makeText(this, event.name +" "+ event.organiser, Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
            }
    }

    fun postFileToCloudStorage(){

        val imageView: ImageView = findViewById(R.id.openCamera)
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()


        // Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child("mountains.jpg")

        // Create a reference to 'images/mountains.jpg'
        val mountainImagesRef = storageRef.child("images/mountains.jpg")

        uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()

        }.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(this, "File added!", Toast.LENGTH_LONG ).show()
            pBar.progress = 0

        }.addOnProgressListener {
            var progress: Double = (100.0 * it.bytesTransferred / it.totalByteCount)
            pBar.progress = progress.toInt()
        }




        // While the file names are the same, the references point to different files
//        mountainsRef.name == mountainImagesRef.name // true
//        mountainsRef.path == mountainImagesRef.path // false


    }

    fun downloadFilesFromCloudStorage(){
        // Create a reference with an initial file path and name
        val pathReference = storageRef.child("images/mountains.jpg")

        // Create a reference to a file from a Google Cloud Storage URI
        val gsReference = cloudStorage.getReferenceFromUrl("gs://bucket/images/stars.jpg")

        // Create a reference from an HTTPS URL
        // Note that in the URL, characters are URL escaped!
        val httpsReference = cloudStorage.getReferenceFromUrl(
            "https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg")
    }

    fun postEventToRealTimeDB(){

        val event: Event = Event("","Cepanje drva", "Marko", 3.14, 3.14, Date(0, 0, 0), Time(0, 0, 0),hashMapOf<String, Boolean>())

        val newRef:DatabaseReference = myRef.database.getReference("events")
        newRef.child("123").setValue(event)
        newRef.child("3231").setValue(event)

    }

    fun setViews(){
        textBox =  findViewById(R.id.textComment)
        textLoad = findViewById(R.id.textViewLoad)
        buttonPost = findViewById(R.id.buttonPost)
        buttonLoad = findViewById(R.id.buttonLoad)
        pBar = findViewById(R.id.progressBar)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)

        setDatabaseReference()
        //postEventToRealTimeDB()
        //postDataToCloudFirestore()
        setViews()
        //addTempData()
        //getTempData()
        //addCollectionToTempData()
        //getDataFromCloudFirestore()
        //downloadFilesFromCloudStorage()

        //FirebaseHelper.probniCloudFirestore(this)


        var tekst:String? = null;
        if (firebaseAuth.currentUser != null)
            tekst = firebaseAuth.currentUser!!.email.toString();
        Toast.makeText(this, firebaseAuth.currentUser?.email.toString(), Toast.LENGTH_LONG ).show()
        //textBox.setText(tekst)

        buttonPost.setOnClickListener {
            if (uploadTask.isInProgress){
                Toast.makeText(this, "Upload is in progress!", Toast.LENGTH_LONG )
            } else{
                postFileToCloudStorage()
            }

        }

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Value is: $value")
                textLoad.text = value
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        buttonLoad.setOnClickListener {
            myRef = realtimeDatabase.reference.child("events")

            val event: Event
            val EventRef = Firebase.database.reference.child("events").get().addOnSuccessListener {
                //textLoad.text = it.getValue<Event>()?.name
                //val event: List<Event>? = it.getValue<List<Event>>()
                for (postSnapshot in it.children) {
//                    val event: Event = postSnapshot.toObject<Event>()
                    Log.d("tag", postSnapshot.toString())
                    val event = postSnapshot.getValue(Event::class.java);
                    //Toast.makeText(this, event?.name + event?.organiser, Toast.LENGTH_LONG).show()
                    Log.d("tag", event!!.name + " " + event!!.organiser)
                }
               // textLoad.text = event?.name
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

//            val myNewRef = database.collection("events").child("123")
//            myNewRef.get().addOnSuccessListener { document ->
//                if (document != null){
//                    Log.d(TAG, "DocumentSnapshot data: ${document.}")
//                } else {
//                    Log.d(TAG, "No such document")
//                }
//            }
//                .addOnFailureListener { exception ->
//                    Log.d(TAG, "get failed with ", exception)
//                }

        }

    }

    private fun addTempData() {

        val cities = Firebase.firestore.collection("cities")

        val data5 = hashMapOf(
            "name" to "Beijing",
            "state" to null,
            "country" to "China",
            "capital" to true,
            "population" to 21500000,
            "hashMapa" to hashMapOf<String, Boolean>(),
            "regions" to listOf("jingjinji", "hebei")
        )
        cities.document("BJ").update(data5)
    }

    private fun addCollectionToTempData(){
        val citiesRef = Firebase.firestore.collection("cities")
        val jpData = mapOf(
            "name" to "Jingshan Park",
            "type" to "park"
        )
        citiesRef.document("BJ").collection("landmarks").add(jpData)

        val baoData = mapOf(
            "name" to "Beijing Ancient Observatory",
            "type" to "musuem"
        )
        citiesRef.document("BJ").collection("landmarks").add(baoData)
    }

    private fun getTempData(){
        val cities = Firebase.firestore.collection("cities")

        //cities.whereEqualTo("hashMapa.123", true).get()... /MOZE I OVO

        cities.orderBy("hashMapa.123").get().addOnSuccessListener {queryDocumentSnapshots ->
            var lista = queryDocumentSnapshots.documents
            lista.forEach {
            Log.d("mrficax", it.data.toString())
            }
            Toast.makeText(this, "Uspesno; Ovoliko elemenata:"+lista.size, Toast.LENGTH_LONG).show()
        }
            .addOnFailureListener{
                Toast.makeText(this, "Neuspesno", Toast.LENGTH_LONG).show()
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        firebaseAuth.signOut()
    }

    override fun onDestroy() {
        super.onDestroy()

        firebaseAuth.signOut()
    }
}