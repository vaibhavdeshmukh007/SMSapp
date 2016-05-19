# SMSapp
This is an android application project to send and receive SMS.

The app basically has 4 screens:-

i) Inbox - Lists all the received SMS where each item is in following format 

{Sender's Name/Number} {Number of received SMSes from this sender}

{First few words of last received SMS by this sender}


ii) All SMS by same number - Once a user clicks on an item of Inbox page, he/she can view ALL the SMSes received from that number.
This screen also provides a text area and a button to the user. The user can type a new message and click on the button to send this new message to that number.


iii) Compose New SMS - Type in a new message, provide receiver's phone number and click on the button to send a SMS.


iv) Search Results - User can search for a text in Inbox and this page will provide results for the search query.




Requirements Summary:

●	The app should be able to send and receive SMS : DONE

●	The app should show all the messages received by the user, even those that are received by the user before the app install : DONE(Inbox lists all the received SMSes)

●	Whenever the user receives a sms, a notification should be shown by the app : DONE(and once the user clicks on the notification, user will be redirected to Inbox page)

●	The messages should be grouped by the sender i.e. all the messages sent by one sender can be seen together : DONE(all messages by same sender are grouped together in Inbox, also providing total number of messages sent by the sender)

●	Full text search should be implemented in the app i.e. the user should be able to search through all his smses : DONE

●	Back up all the messages to the user’s google drive : NOT DONE 



Expected Solution:

1.	The source code of the app to be uploaded on github along with a readme file.The readme file should contain a list of all libraries used.

https://github.com/vaibhavdeshmukh007/SMSapp

2.	A debug build apk of your app. 

Available here

SMSapp/app-debug.apk






Information for Geeks

Used Cursor to read data from inbox.

Used SmsManager to send SMS.

Used Broadcast Receiver and Notification Builder to create notification.

Used ViewHolder to provide smooth scrolling of ListView(filled using custom adapter).

Used SearchView in ActionBar to enable search feature.

