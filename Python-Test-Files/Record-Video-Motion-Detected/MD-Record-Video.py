import RPi.GPIO as GPIO
import time
import cv2

GPIO.setmode(GPIO.BOARD)    # choose BCM(for GPIO numbering) or BOARD(for pin numbering)
GPIO.setwarnings(False)     # turned off warnings
Pir = 29                    # Assigning pin number 29 to a variable
GPIO.setup(Pir,GPIO.IN)     # Setting Pir (Pin) as an Input Pin

fourcc = cv2.VideoWriter_fourcc(*'H264') # This defines the codec we are going to use
print(" Waiting for Motion to be Detected.... ")


while(True):
    if (GPIO.input(Pir) == 0):
        IPCameraCapture = cv2.VideoCapture("rtsp://192.168.X.XXX:554/12") # Capture feed from camera
        # VideoWriter(Filename.Type, video type, frames per second, resolution)
        # Screen resolution from the stream must match the screen resolution u set for the output!
        output = cv2.VideoWriter('MotionDetected.avi', fourcc, 25.0, (640, 480))

        print("Motion Detected! Starting recording...")
        print("Time = %d" % time.time())
        recordtime = time.time() + 20
        while( time.time() <= recordtime):
            # Capture frame-by-frame and store in object
            ret, frame = IPCameraCapture.read()  # ret is boolean value that tells us if the video capturing object is working or not
            output.write(frame)
            
        print("Time = %d" % time.time())
        print("Recording Ended")

        output.release()
        IPCameraCapture.release()
        
    else:
        #print("waiting for motion to be detected....")
        time.sleep(.5)
