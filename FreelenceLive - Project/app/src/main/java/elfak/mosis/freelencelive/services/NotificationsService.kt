package elfak.mosis.freelencelive.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.*
import android.widget.Toast
import android.os.Process
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import elfak.mosis.freelencelive.MainActivity
import elfak.mosis.freelencelive.MainWindowActivity
import elfak.mosis.freelencelive.R
import elfak.mosis.freelencelive.data.UserLocation
import elfak.mosis.freelencelive.model.userViewModel


class NotificationsService : Service() {

    private val channelId = "FreelenceLiveChannel"
    private var serviceRunning = false
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            val notifiedMap = HashMap<String, Boolean>()

            while (serviceRunning) {
                try {
                    val database = Firebase.database
                    val dataRef = database.getReference("map")
                    dataRef.child("users").get().addOnSuccessListener {
                        val temp: HashMap<Any, Any> = it.value as HashMap<Any, Any>
                        val users: MutableList<UserLocation> = mutableListOf()
                        var currentUser: UserLocation? = null
                        temp.forEach { user ->

                            val userMap = user.value as HashMap<String, Any>


                            val newUser = UserLocation(
                                user.key as String,
                                userMap["lat"] as Double,
                                userMap["lon"] as Double
                            )
                            if (user.key == Firebase.auth.uid) currentUser = newUser
                            else users.add(newUser)
                        }
                        val distance = FloatArray(3)
                        for (user in users) {

                            Location.distanceBetween(
                                currentUser?.latitude ?: 0.0,
                                currentUser?.longitude ?: 0.0,
                                user.latitude,
                                user.longitude,
                                distance
                            )

                            if (distance[0] <= 450) {
                                if (notifiedMap.containsKey(user.userId)) {

                                    if (notifiedMap[user.userId] == false) {

                                        notifiedMap[user.userId] = true
                                        createNotification(distance[0], currentUser, user.userId)

//                                    Thread.sleep(15*6000)
                                    }
                                } else {
                                    notifiedMap[user.userId] = false
                                    //Log.d("nearbyPerson", "This person is not near:\n ${user.id}")
                                }
                            } else{
                                notifiedMap[user.userId] = false

                            }

                        }

                    }
                } catch (e: InterruptedException) {
                    // Restore interrupt status.
                    Thread.currentThread().interrupt()
                }
                Thread.sleep(5000)

            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        createNotificationChannel()
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceRunning = true
            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()
        if (intent != null){

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }


        }
        // If we get killed, after returning from here, restart
        return START_STICKY_COMPATIBILITY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        serviceRunning = false
        stopSelf()
    }

    fun createNotification(distance: Float, currentUser: UserLocation?, nearId: String) {

        try {

            var intent: Intent = Intent(this, MainWindowActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }

            intent.putExtra("currentUserId", currentUser?.userId)
            intent.putExtra("nearUserId", nearId)


            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
// Create an Intent for the activity you want to start
            val resultIntent = Intent(this, MainWindowActivity::class.java)
            resultIntent.putExtra("currentUserId", currentUser?.userId)
            resultIntent.putExtra("nearUserId", nearId)
// Create the TaskStackBuilder
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                // Add the intent, which inflates the back stack
                addNextIntentWithParentStack(resultIntent)
                // Get the PendingIntent containing the entire back stack
                getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }

            val builder = NotificationCompat.Builder(this, channelId).apply {
                setContentIntent(resultPendingIntent)
                setSmallIcon(R.drawable.programmer)
                setContentTitle("Someone is near!")
                setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                setContentText("Check in app!")
                setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Open app to check who is around you...")
                )
                setPriority(NotificationCompat.PRIORITY_DEFAULT)
                setAutoCancel(true)
            }
            with(NotificationManagerCompat.from(this)) {
                notify((System.currentTimeMillis() % 10000).toInt(), builder.build())
            }

//            var builder = NotificationCompat.Builder(this, channelId)
//                .setBadgeIconType(R.drawable.programmer)
//                .setSmallIcon(R.drawable.programmer)
//                .setContentTitle("Neko je u blizini!")
//                .setContentText("Proverite u aplikaciji!")
//                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
//                .setStyle(
//                    NotificationCompat.BigTextStyle()
//                        .bigText("Open app to check who is around you...")
//                )
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//
//            with(NotificationManagerCompat.from(applicationContext)) {
//                // notificationId is a unique int for each notification that you must define
//                notify((System.currentTimeMillis() % 10000).toInt(), builder.build())
//
//            }
        } catch (ex: Exception) {
            Log.d("ErrorWhileLoading", ex.toString())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "FreelenceLiveApp"
            val descriptionText = "Notification description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}