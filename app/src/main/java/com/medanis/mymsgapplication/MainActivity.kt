package com.medanis.mymsgapplication

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


@Suppress("UNCHECKED_CAST")
open class MainActivity : AppCompatActivity() {
    private var messagesArrayList: MutableList<Messages> = ArrayList()
    var docUID = "w691O35sZ2hvDpkTbAnm6aaTjZj1"
    var patUID = "MoIfH0sbt2UjJj7yozXRQF72RgK2"
    var topName=""
    var patientID = ""

    @SuppressLint("LongLogTag")
    fun collectData(docs: Map<String?, Any?>?, mDocUID : String){
        messagesArrayList.clear()
        if (docs != null) {
            for ((_, value) in docs) {
                //Get user map
                val singleUser =
                        value as Map<*, *>
                //Get phone field and append to list
                if(singleUser["message"] as String? !=""){
                    messagesArrayList.add(Messages(
                            singleUser["name"] as String?,
                            singleUser["muid"] as String?,
                            singleUser["time"] as String?,
                            singleUser["message"] as String?)
                    )
                }else{
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val childDate = singleUser["time"] as String
                    FirebaseDatabase.getInstance().reference.child("Messages").child(patUID).child(docUID).child(childDate).removeValue()
                }

            }
            val myrv = findViewById<RecyclerView>(R.id.msgRV)
//            Collections.sort(arraylist);
            messagesArrayList = messagesArrayList.sortedWith(compareBy { it.time }) as MutableList<Messages>
            val myModulesAdapter = MessageRV(this, messagesArrayList)
            myrv.layoutManager = GridLayoutManager(this, 1)
            myrv.adapter = myModulesAdapter
            myrv.scrollToPosition(myModulesAdapter.itemCount - 1)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sendBtn = findViewById<ImageButton>(R.id.sendBtn)
        val messageET = findViewById<EditText>(R.id.messageET)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null){
            patientID = currentUser.uid
        }
        sendBtn.setOnClickListener {
            val msg = Messages(topName, patientID, isSameDay().timeNow(), messageET.text.toString())

            docUID.let { it1 ->
                FirebaseDatabase.getInstance().getReference("Messages")
                        .child(patUID)
                        .child(it1)
                        .child(isSameDay().timeNow())
                        .setValue(msg).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Message Sent", Toast.LENGTH_LONG).show()
//                            msgOrder= (msgOrder.toInt()+1).toString()
                                messageET.text.clear()
                            } else {
                                Log.e("TAG", "create Message Account: Fail!", task.exception)
                                Toast.makeText(this, "Message NOT Sent: "+ task.exception, Toast.LENGTH_LONG).show()
                            }
                        }
            }

        }

        val ref = FirebaseDatabase.getInstance().reference.child("Messages").child(patUID).child(docUID)
        ref.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    @SuppressLint("LongLogTag")
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        //Get map of users in datasnapshot
                        Log.e("//////////////// dataSnapshot",dataSnapshot.value.toString())
                        collectData(dataSnapshot.value as Map<String?, Any?>?, docUID)
                    }
                    override fun onCancelled(databaseError: DatabaseError) { //handle databaseError
                    }
                }
        )
        var name=""
        var muid=""
        var time=""
        var message =""

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }


            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
            }

            @SuppressLint("LongLogTag")
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val newMSG: Messages? = dataSnapshot.getValue(Messages::class.java)

                val myrv = findViewById<RecyclerView>(R.id.msgRV)
                name = dataSnapshot.child("name").value.toString()
                muid = dataSnapshot.child("muid").value.toString()
                time = dataSnapshot.child("time").value.toString()
                message = dataSnapshot.child("message").value.toString()
                var contained = false
                messagesArrayList.forEach{ contained =  it.time !=time}
                if (messagesArrayList.isNotEmpty() && contained){
                    messagesArrayList.add(Messages(newMSG?.name, newMSG?.mUID, newMSG?.time, newMSG?.message))
                    Log.e("//////////////// name ", name)
                    Log.e("//////////////// time ", time)
                    Log.e("//////////////// muid ", muid)
                    Log.e("//////////////// message ", message)
                    messagesArrayList = messagesArrayList.sortedWith(compareBy { it.time }) as MutableList<Messages>
                    val myModulesAdapter = MessageRV(this@MainActivity, messagesArrayList)
                    myrv.layoutManager = GridLayoutManager(this@MainActivity, 1)
                    myrv.adapter = myModulesAdapter
                    myrv.scrollToPosition(myModulesAdapter.itemCount - 1)

                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}
