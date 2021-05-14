# NanoFightingGameRecorder_ClientApplication
This is the Android Client GUI for the Nano Fighting Game Recorder project. This microservice allows the the user to connect to the Rest API and send commands with the click of a button. 

# Setup
**For Developing**
For dependencies, all you need to do is download Android Studio and run this project. Gradle manages the build of the project and will import the dependencies for you.

**Android Physical Demo**
apk file destination: client_application -> app-debug.apk

Once you download this file to your Android device you will be able to run this application from your files.

**Network Settings**
When you have the Jetson Nano running the local server API on your network, you need to make sure that your android device is also connected to the same network. Make sure you have your IPv4 address from the Jetson Nano and of course the Port number that the API is using. You will have to manually enter in the Jetson Nano's IP and the port into the Android application

# The App In Action
**Connect Page**
Assuming that you have the Jetson Nano from the main project running the local API server, we can fire up this client android application. Please do not skip the 'Connect' page, because this is how you will connect to the Jetson Nano device. Now, you need to enter in the Jetson Nano's IP with the appropriate port number that it is running on into the text bar above the 'Connect' button. The correct format would be 'https://10.0.0.1:30'. This app is still case sensitive so if you add an extra slash at the end of the port number like this, 'https://10.0.0.1:30/', then you will not be able to connect to the device. You need a network protocol (https://), a valid ip address, port number, and you also need to leave out the final forward slash like I mentioned before.

**Commands**
Once you are connected you will be notified at the bottom of the Connect page. You can now move to the Commands page! This page will have 3 buttons including Start, Stop, and Unpair. The device will let you know when the recording has started on the Jetson Nano. Feel free to test it out and see if you get a recording going from the Jetson Nano.

**Admins**
TBA

# APP IS EARLY IN DEVELOPMENT
Please be in mind that this app is early in development and there hasn't been an extensive debugging campaign yet. We know that there are still a lot of issues and that we haven't even started on the clip uploading feature yet.
