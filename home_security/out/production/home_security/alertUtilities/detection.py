from time import sleep
from gpiozero import MotionSensor
import cv2
import os
import random
import uuid
import json
from time import localtime, strftime

print("Running detection server")

# PIR Sensor (GPIO PIN 04)
pir_sensor = MotionSensor(4)

# locating haarcascades in opencv /data folder
human_casc_path = os.path.dirname(
          cv2.__file__) + "/data/haarcascade_fullbody.xml"
          
face_casc_path = os.path.dirname(  
          cv2.__file__) + "/data/haarcascade_frontalface_default.xml"

human_cascade = cv2.CascadeClassifier(human_casc_path)  # setting as the classifier
face_cascade = cv2.CascadeClassifier(face_casc_path)  # setting as the classifier

detected_images = []
PATH = "/home/albjon/Desktop/mainworkspace/home_security/data/intruder_references/references.json"

if os.path.isfile(PATH) and os.access(PATH, os.R_OK):
    with open(PATH, "r") as ref:
        detected_images = json.load(ref)
        print("Loaded existing data: " + str(len(detected_images)) + " rows")
else:
    with open(PATH, "w+") as ref:
        print ("Either file is missing or is not readable, creating file...")
        ref.write(str([]))
    
def detectMotion():
    pir_sensor.wait_for_motion()
    return True


def detectHumans():
    pi_camera = cv2.VideoCapture(0)  # loading pi camera module
    # capturing each frame
    ret, frames = pi_camera.read()
    grey = cv2.cvtColor(frames, cv2.COLOR_BGR2GRAY)  # converting to greyscale
    humans = human_cascade.detectMultiScale(
        grey,
        scaleFactor=1.1,
        minNeighbors=5,
        minSize=(30, 30),
        flags=cv2.CASCADE_SCALE_IMAGE)
        
    faces = face_cascade.detectMultiScale(
        grey,
        scaleFactor=1.1,
        minNeighbors=5,
        minSize=(30, 30),
        flags=cv2.CASCADE_SCALE_IMAGE)

    
    if len(humans) > 0 or len(faces) > 0:
        print("Intruder Detected")
		
        img_timestamp = strftime("%d-%m-%Y | %H:%M:%S", localtime()) # image timestamp
        
        #dt = datetime.now()
        #unix_timestamp = datetime.timestamp(dt) # unix timestamp
		
        for x1, y1, x2, y2 in humans:            # x-axis, y-axis, width, height
            cv2.putText(frames,
                        "Human Detected",        # text
                        (x1, y1 - 10),           # location
                        cv2.FONT_HERSHEY_PLAIN,  # font
                        1,                       # text size
                        (255, 255, 255))         # BGR 0 - 255
                        
            cv2.rectangle(frames,
                          (x1, y1),              # startpoint
                          (x1 + x2, y1 + y2),    # endpoint
                          (0, 0, 255),           # BGR 0 - 255
                          1)                     # thickness
                          
        for x1, y1, x2, y2 in faces:
            cv2.putText(frames,
                        "Face Detected",         # text
                        (x1, y1 - 10),           # location
                        cv2.FONT_HERSHEY_PLAIN,  # font
                        1,                       # text size
                        (255, 255, 255))         # BGR 0 - 255
                                      
            cv2.rectangle(frames,
                          (x1, y1),              # startpoint
                          (x1 + x2, y1 + y2),    # endpoint
                          (255, 255, 255),       # BGR 0 - 255
                          1)                     # thickness
        # timestamp text
        cv2.putText(frames,
                    img_timestamp,
                    (5, 15),
                    cv2.FONT_HERSHEY_PLAIN,
                    1,
                    (255, 255, 255),
                    2)
                                       
        img_uuid = str(uuid.uuid4()) # unique image identifier
        
        img = f"/home/albjon/Desktop/mainworkspace/home_security/data/intruder_captures/image_{img_uuid}.png"  # image name
        
        print(str(len(detected_images)))
        # json object for image
        json_img_ref = {
                      "id": img_uuid, 
                      "name": img,
                      "timestamp": img_timestamp
                      }
                      
        detected_images.append(json_img_ref)
        print(str(len(detected_images)))
        with open("/home/albjon/Desktop/mainworkspace/home_security/data/intruder_references/references.json", "w") as ref:
            json.dump(detected_images, ref, indent = 4) # indent for readable structure
        
        # writing image to disk                 
        cv2.imwrite(img, frames)
        humans = ()
        faces = ()

while True:
    if detectMotion():
        detectHumans()
