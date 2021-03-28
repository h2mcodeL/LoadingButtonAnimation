# LoadingButtonAnimation
Custom Loading Button App uses a custom built button with animated features to demonstrate the download function in Android/Kotlin.

# Prerequisites
Android SDK v30, Android Build Tools v30.0.0, DataBinding
MotionLayout, ValueAnimator



Select a radio button for the item to be downloaded, then click download button. 
The custom button state is updated following a button click to inform the user of the current download state, its also has an animation with a horizontal colour change as the download is happening. The text output on the button describes the download state. I.e. Downloading, Clicked, Download Complete. 

A successful and complete download triggers a notification. On selecting the notification, this directs the user to the results activity. MotionLayout is used to provide an interesting interface, where the user swipes an arrow to reveal a button that returns to the Main Activity. 

