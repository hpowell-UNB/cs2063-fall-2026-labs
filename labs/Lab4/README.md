# Lab 4 - Broadcast Receivers

Today’s lab will build off the camera integration code used in the last lab to go deeper into working with Broadcast Receivers, alarms, and notifications.  

#### Pre-Lab Reading

Make sure you are familiar with the following sections of the Android developer documentation:

* [Notifications](https://developer.android.com/training/notify-user/build-notification)
	* Read through the _*Create a basic notification*_ section which should be sufficient for this lab
	* Please note this is an older version of this documentation
* [Alarms](http://developer.android.com/training/scheduling/alarms.html)
	* Up to the end of Cancel an Alarm
* [Monitoring battery state](http://developer.android.com/training/monitoring-device-state/battery-monitoring.html)
* Class overview for [PendingIntent](http://developer.android.com/reference/android/app/PendingIntent.html)
* Class overview for [BroadcastReceiver](http://developer.android.com/reference/android/content/BroadcastReceiver.html) 

## Pair Programming

We will again be doing pair programming for this lab.  Details on pair programming can be found at [Pair Programming](../../docs/PAIR_PROGRAMMING.md).  You can again work with anybody of your choosing.

## Objectives

In this lab we will be building a photo taking app that reminds the user to take a photo at regular intervals.  We will also make the app adapt to the battery state of the device to conserve power when the battery is low. 

In building this app we will learn about Android notifications, alarms, and BroadcastReceivers.  You will need to use either a device or emulator for testing; additonal information can be be found in the [Debugging and Testing](../../docs/DEBUGGING.md) document.


#### Initialize Project

You are given an Android project containing the starting point code for the lab.  It is a single ```Activity``` app that has a button which dispatches an implicit ```Intent``` to take a photo and then saves the photo (same code used in Lab 3 to take a photo).  There is also functions created for checking permissions and working with alarms.  Some of these are not fully implemented and will be completed as we go.

Your task is to examine the role broadcast receivers play and how the user can be notified of changes.  For this we will cover:
* Adding a broadcast receiver component for working with messages within the app.
* Creation of a ```Notification```.
* Interacting with the Android system to get device status updates.

### Create the Alarm

First step is to add the functionality to have an alarm go off at regular intervals to remind the user to take a picture.

**Task 1**

Create a ```BroadcastReceiver``` to receive alarms.  For this we will add a new Kotlin file called ```AlarmReceiver.kt``` which extends ```BroadcastReceiver```

1. From the Android view right click on _mobiledev.unb.ca.broadcastreceiverlab_
2. Select New -> Other -> Broadcast Receiver
	* Uncheck the _Exported_ checkbox 
	* Leave the _Enabled_ checkbox selected

This will also add an entry into the ```AndroidManifest.xml``` file which defines the broadcast receiver for use in the project.  You can confirm this by looking for the following section in the file directly below the ```<application>``` opening tag.
	```XML
		<receiver
			android:name=".AlarmReceiver"
			android:enabled="true"
			android:exported="false"></receiver>
	```

No action needed if this entry exists.  However, if it is not there add it to the ```AndroidManifest.xml``` file before proceeding.

**Task 2**

With the Broadcast Receiver in place let's go back and set an alarm.  The alarm should be set to repeat roughly every 60 seconds and should wake the device.  We would typically use alarms for much longer durations (in this case we might set the alarm to run once per day).  However, this short interval will be useful for testing and debugging.

1. Override ```AlarmReceiver```'s ```onReceive``` method
	* This method will be called when the ```BroadcastReceiver``` receives a broadcast
	* Add a ```Log``` message in here for now

2. Update the ```AndroidManifest.xml``` file by declaring the [appropriate exact alarm permission](https://developer.android.com/training/scheduling/alarms.html#exact-permission-declare)
  * For this lab exercise we will be using ```android.permission.SCHEDULE_EXACT_ALARM```

3. Locate the ```initAlarmValues``` function inside ```MainActivity```; this will be used to start the alarm.
	* To help you along the ```AlarmManager``` object has already been defined

4. Update the function to define a pending intent whose action is to start the ```AlarmReceiver``` class.
	* The documentation for [PendingIntent](http://developer.android.com/reference/android/app/PendingIntent.html) will help here

5. Update the ```startAlarm``` function to create the alarm.
	* The alarm should wake up the device and fire the pending intent
	* Set the trigger at value to 5 seconds
	* Set the time intervals value to 60 seconds
	* The documentation for [Alarms](https://developer.android.com/training/scheduling/alarms.html#type) will help here .
	
6. Update the ```cancelAlarm``` function to cancel the alarm.
	* We won't be using this now, however, this will be needed later so it will good to have this in place

6. Run the app.
	* You should see log messages from ```AlarmReceiver``` indicating that ```onReceive``` is being called.

### Add Notifications

With the alarm in place we need to update the project to post a system notification which will prompt the user to take another picture.

**Task 3**

Before we begin making any further changes the app requires a runtime permission for posting notifications.

1. Update the ```AndroidManifest.xml``` file to declare the post notifications permission as shown below.
	```XML
	<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
	```

**Task 4**

We now need to enable to the code which will perform the runtime check.  In this case instead of directly calling the code to initialize the alarm we will use the permission check to do so when a user grants post notification access.

1. Inside the ```onCreate``` function for ```MainActivity``` remove the call to _initAlarmValues()_ and uncomment the call to _checkPermissionAndScheduleAlarm()_
	* HINT: Look for _TODO (NOTIFICATION)_

*Testing Note*

* When using devices running Android 13 or greater the user will be prompted to accept permissions the next time the app starts.
* Devices using older versions automatically allow access to post notifications.

**Task 5**

With the permissions and alarm in place let's add the content for the notification.  The following steps expand upon the sub-sections outlined in the [Create a basic notification](https://developer.android.com/develop/ui/views/notifications/build-notification#simple-notification) guide to include app specific notification options.

*IMPLEMENTATION HINT:*

The methods used to create the notification channels and intents require a context.  Recall that broadcast receiver does not natively inherit the contxt.  To help with this have a look at the signature for ```onRecieve```.  There is a context object you can use.

1. Locate the ```onReceive``` function inside ```AlarmReceiver```; this will be used to create the notification.

2. Create the notification channel with the following properties.
	* Set the importance as IMPORTANCE_HIGH
	* Set the option to allow notifications posted to the channel to vibrate the device
	* Set the notification light color for notifications posted to this channel
	* Note the following values are already defined for you to use
		* The values for the channel name and description can be found in the ```strings.xml``` resource file
		* The value for channel ID can be found in the ```Constants``` file

3. Create the tap action pending intent.
	* The tap action of this notification will be to start ```MainActivity```
	* By clicking on the notification the user is taken back to the app

4. Create the notification with the following properties.
	* Set the small icon to ```R.mipmap.ic_launcher```
	* Set [```setAutoCancel```](http://developer.android.com/reference/android/app/Notification.Builder.html#setAutoCancel%28boolean%29) to ```true``` so that when the user clicks on the notification it is dismissed
	* Set the content intent as the intent created in Step 3
	* Note the following values are already defined for you to use
		* The values for the content title and text can be found in the ```strings.xml``` resource file
		* The value for channel ID can be found in the ```Constants``` file

5. Add the code to show the notification.
	* Note the following:
		* The value for the notification ID can be found in the ```Constants``` file
		* There may be some compiler errors in this section; to resolve them use the tips provided by Android Studio 

6. Run the app again
	* When the app starts you will be prompted to accept the permission to post notifications
	* You should see the notifications that you have created when the alarm is fired
	* Notice as well that the alarm continues to fire even after you have closed the app

### Conserving Power

Android will start killing processes when the battery or other resources are running low.  Having a constantly running alarm task could be a candidate for termination in a low battery state.  

**Task 6**

Android sends an ```ACTION_BATTERY_LOW``` intent when the system changes to a low battery state and an ```ACTION_BATTERY_OKAY``` intent when the battery level is high enough again after previously being low. We will receive these intents to change the behavior of our app.

1. In ```MainActivity``` add the following ```BroadcastReceiver``` object called ```batteryInfoReceiver```
	```kotlin
	private val batteryInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
			override fun onReceive(context: Context, intent: Intent) {
			}
	}
	```

	* We will be referencing this object in the next few steps to complete the implementation.

**Task 7**

Now we will modify our app to conserve power when the battery is low by disabling the alarm.  If the battery is low turn off the alarm and issue a notification. If the battery state becomes OK, turn the alarm on, and issue a notification.

1. In ```MainActivity.onCreate``` create a new ```IntentFilter``` that includes the actions ```ACTION_BATTERY_LOW``` and ```ACTION_BATTERY_OKAY```.
	* Have a look at [IntentFilter](http://developer.android.com/reference/android/content/IntentFilter.html) for reference
	* Call ```addAction``` to add the appropriate actions to it

2. Register ```batteryInfoReceiver``` so that it will receive any intent that matches the filter you just created.
   * Have a look at [registerReceiver](https://developer.android.com/reference/android/content/Context.html#registerReceiver(android.content.BroadcastReceiver,%20android.content.IntentFilter)) for reference
   * Include the intent from Step 1 in the registration call
   * This also gets included in ```MainActivity.onCreate```

3. Update the  ```batteryInfoReceiver.onReceive()``` method.
   * If an ```ACTION_BATTERY_OKAY``` intent is received start the alarm just like you did previously and show a ```Toast``` indicating which intent was received
   * If an ```ACTION_BATTERY_LOW``` intent is received cancel the alarm and show a ```Toast``` message

4. As ```batteryInfoReceiver``` is dynamically registered we also need to unregister it to avoid memory leaks.  For this do the following:
   * Override the function ```MainActivity.onDestroy```
   * Unregister ```batteryInfoReceiver``` here

#### Note About Testing

The above steps can be tested by using an Android emulator as the battery state can be easily adjusted.  However, to test the battery conditions on a physical device will be more difficult to do.  In order to trigger the battery intents you would have to wait for the battery to become low to be able to test if the app responded correctly.

In this case update the code to check against the AC power connection as opposed to battery level.  In this case adjust the implementation to have the app cancel the alarm if the AC power is disconnected and set the alarm when the AC power is connected.
  * Use ```ACTION_POWER_CONNECTED``` instead of ```ACTION_BATTERY_OKAY```
  * Use ```ACTION_POWER_DISCONNECTED``` instead of ```ACTION_BATTERY_LOW```
  
## Writeup Task

Create a document with the following items:
* A screenshot of the toast message when the application enters either a low battery or power disconnected state
* A screenshot of the toast message when the application leaves either a low battery or power disconnected state
* A screenshot of the notification reminding the user to take a picture

## Lab Completion

* LABS COMPLETED IN CLASS: 
  * Show the working app running on an emulator to the instructor or TA for signoff.
  * No files need to be submitted.
* LABS COMPLETED OUTSIDE OF CLASS: 
  * Take screenshots of the application in a either a low battery or power disconnected mode
  * Submit `MainActivity.kt`, `AlarmReceiver.kt`, `AndroidManifest.xml`, and your document with screenshots to the Lab4 drop box folder on D2L

* Keep a copy of your project work and answers for future reference
