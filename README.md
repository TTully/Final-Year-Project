# Final-Year-Project
Final Year Home Security Project 

This project/Repository contains the code for my Android App, Webserver and my Webpages.
This project uses a variety of different coding languages like java, python, javascript and html.

The android App allows the user to add there RTSP IP cameras to the app by entering the details into a database, by entering a name for the 
camera (e.g front of house) and the IP address. If the user is unable to find out the IP Address of the camera, there is a feature included
where you can scan the network at which your phone is on and it will display all the active IP Addresses, so once your phone and your IP 
Camera are on the same network it should find the ip address of the camera. The android app also uses the LibVLC library, developed by the 
VideoLan Organisation (https://www.videolan.org/)

The Webserver is running a small website which basically prompts the user to enter in their username and password which the the password is encrypted and stored in a database in MongoDB. If the user tries to access the home oage which stream the IP Cameras they will be forced to login in first. The templating engine used is Handlebars. I use session tracking and passport to verify if the usernames and passwords entered are correct. I downloaded files provided by VideoExpertsGroup (https://www.videoexpertsgroup.com/) and stored them on my server which i can then run my RTSP cameras live feed on my webpage using vxg playeres provided in their files downloaded. You must be using GHoogle Chrome and download their plugin in order for you to view the live feed from your IP Cameras. The Home page will display both of the IP Cameras live feed out on the webpage for you to view and there is a sign out button which terminates the session and logs the user out.

The python code provided basically poles a  gpio pin for when it changes state and once it does a function is called and records a video from one of the IP Cameras and stores it locally on the Raspberry pi. While this is happening a new thread is called and is executing a different function which sends an email to the user alerting them that motion has being detected.
