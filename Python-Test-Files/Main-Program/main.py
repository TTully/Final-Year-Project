import RPi.GPIO as GPIO
import time
import cv2
import smtplib
import config
from _thread import start_new_thread
from datetime import datetime

GPIO.setmode(GPIO.BOARD)    # choose BCM(for GPIO numbering) or BOARD(for pin numbering)
GPIO.setwarnings(False)     # turned off warnings
Pir = 29                    # Assigning pin number 29 to a variable
GPIO.setup(Pir,GPIO.IN)     # Setting Pir (Pin) as an Input Pin

fourcc = cv2.VideoWriter_fourcc(*'H264') # This defines the codec we are going to use
print(" Waiting for Motion to be Detected.... ")

subject = "**** Security Alert ****"    # variables to be used in the send_email function
msg = "Warning! We have detected movement around your house. "


def record_video():
    start_new_thread(send_email, (subject, msg))    # Start a new Thread to Send an Email
    IPCameraCapture = cv2.VideoCapture("rtsp://192.168.1.110:554/12") # Capture feed from camera
    # VideoWriter(Filename.Type, video type, frames per second, resolution)
    # Screen resolution from the stream must match the screen resolution u set for the output!
    output = cv2.VideoWriter('MotionDetected.avi', fourcc, 25.0, (640, 480))

    print("Motion Detected! Starting recording...")
    #print("Time = %d" % time.time())
    recordtime = time.time() + 20
    while( time.time() <= recordtime):
        # Capture frame-by-frame and store in object
        ret, frame = IPCameraCapture.read()  # ret is boolean value that tells us if the video capturing object is working or not
        output.write(frame)
            
    #print("Time = %d" % time.time())
    print("Recording Ended")

    output.release()
    IPCameraCapture.release()
    #send_email(subject, msg)



def send_email(subject, msg):
    try:
        DateTime = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        server = smtplib.SMTP('smtp.gmail.com:587')
        server.ehlo()
        server.starttls()
        server.login(config.EMAIL_ADDRESS, config.PASSWORD)
        msg = msg + "\nEvent Occured: " + DateTime
        message = 'Subject: {}\n\n{}'.format(subject, msg)
        server.sendmail(config.EMAIL_ADDRESS, config.EMAIL_ADDRESS, message)
        server.quit()
        print("Success: Email sent!")
        #print(DateTime)

    except:
        print("Email Failed to send.")


while(True):
    if (GPIO.input(Pir) == 0):
        record_video()
        
    else:
        time.sleep(.5)
