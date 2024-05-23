# Plant-Irrigation-Project
This project is concerned with remote care of housing plants. We have built a project consisting of an irrigation device and an application that recognizes the plant and reports how to take care of it using AI and provides the ability to monitor the soil moisture level of the plant via the Internet, and the possibility of watering it remotely directly when needed.


## System Architecture
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/151cd867-065b-4dbd-98b0-53024a0849ab)

## Required Hardware & Software

1- ESP32:	Is a low-cost, low-power microcontroller with an integrated Wi-Fi and Bluetooth.
2-	Soil moisture sensor:	Measure the volumetric water content in soil.
3-	Water pump:	A component is used to move the water and irrigate the plant.
4-	Relay:	an electrically operated switch that connects or disconnects two circuits

##  Required Software
1-	Android Studio:	Is the official Integrated Development Environment (IDE) for Android app development.
2-	Arduino IDE:	Is used to write and upload code from the computer to the motherboard.
3-	Arduino C:	 Arduino programming language.
4-	Kotlin:	The programming language used for building Android apps.
5-	XML:	Use the XML language to create UI layouts of Android apps.
6-	Firebase: 	Is a set of backend cloud computing services and application development platforms provided by Google.
7-	TensorFlow Lite:	Is a set of tools that enables on-device machine learning by helping developers run their models on mobile, embedded, and edge devices

## Implementation Process
### 1.	Training the AI Model
First, we researched houseplants and selected a number of them to train the model. We collected 70 images of each plant, 50 images of model training, and 20 images of model testing. We used Teachable Machine Google site that uses TensorFlow Lite to build AI models. Currently, due to lack of time, the model can identify 4 plants: Dracaena Compacta, Pothos, Peperomia, Zamioculcas zamiifolia.

### 2.	Create the Android App
1.	Front-End
The app contains Three activities: 
-	Home activity, which is the main page enabling the client to access all the services of the application.
-	Scan Plant activity, which contains the trained AI Model
-	Irrigation activity, which displays the soil moisture level and allows the user to send irrigation command to the device. 
 to create activity interfaces, we used XML language.

### 2.	Back-End
For backend we used Kotlin language to program the app and make the functions and connect the DataBase.

### 3.	Create the DataBase
We used FireBase to maintain plants data, and we used it as an intermediary to send and receive data from the IoT device.
-	Plants DataBase:
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/6fe9ccfd-392f-4b81-b2f3-2feff3aa6ce2)
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/7f206a37-2fe9-44d5-addb-a315bffa7c59)

- Sensors DataBase:
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/c0647b6b-0694-42fd-9b4a-883e7939a801)

### 4.	Build the IoT device
In the beginning, we identified the basic functions of the device, namely: measuring the rate of soil moisture, and irrigation.
First, we chose the appropriate microcontroller for our device which is ESP-WROOM-32, chosen because it has integrated Wi-Fi and Bluetooth connectivity for a wide range of applications. Then we chose the electronic components that help in carrying out these tasks: soil moisture sensor, water pump, relay, and jumper wires.
Then we designed the electrical circuit of the device as shown in the picture below
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/79c76fc8-b1db-41bd-b1d8-542daeefff67)
Then we actually built the device and connected the parts, then we programmed it using Arduino C language.


## System Integration
### 1.	Model Integration
To integrate the plant recognition AI Model with the application, we downloaded the module files with the .tflite extension and included them in the application files. Then, to use the Model in kotlin, we used the tensorflow.lite library, which contains many functions that facilitate dealing with the module.
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/e8e432d4-e63d-497f-ad84-6b91fa1e694b)

### 2.	DataBase Integration
First, We connect the Firebase DB to the Android Studio by adding the Firebase Android configuration file (google-services.json) to the app and adding its dependencies. Then we can read and write to our DB by the following codes:

#### retrieve plant information based on the label
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/3167a729-3341-438f-ad1a-543738e63b1d)

#### read soil moisture level from DB 
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/fc77bf28-b53e-46f5-905e-e2a917b56b03)

#### Write Irrigation Command to DB
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/1be71fd4-3e9e-4666-a364-1534cfb50a7b)

### 3.	Device Integration
To allow the IoT device to send the Soil Moisture level to the app or to receive the irrigation command from the app, we need to connect it to the DB so that it will act as an intermediary between the device and the application. These two libraries allow the Device to read and write from FireBase
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/37bd3a3e-bfae-40ad-803b-4a0d19d46018)

Part of the Device Code shows read and write from DB:
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/7d9af833-075e-48ab-9723-9f9a37652a29)

## System Screens Snapshots
### Home Page:
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/ec63e4b6-b43e-4fd7-9bb3-c99b9d9853d3)

### Identify A Plant Page:
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/4d74b839-55f7-4598-8958-41b459eac8b2)

![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/a8e21e72-6bc1-4092-89ea-58bbadb8db0c)

![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/41300940-70ae-45b2-904d-39ba2cd66014)

### Irrigation page:
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/905f46ff-b282-4924-b5c7-06dde1f59b63)
![image](https://github.com/Mjd0001/Plant-Irrigation-Project/assets/105239889/8cf619e3-c6aa-4f82-b833-34c7c3f76301)


## Limitations and Future Work
### Limitations
•	Currently, the model only recognizes four houseplants. A larger number will be worked on in the future.
•	Currently, the application can manage only one device, that is, it can receive sensor readings and send commands to only one device
•	One device can only irrigate one plant because it has one pump and one moisture sensor.


### Future Work

-	Login, logout, and irrigation scheduling we couldn’t apply them in this time frame, so that we will consider it as future work.
-	A sensor to measure the water level in the tank.
-	Adding a smart irrigation feature. 
-	identifying a greater number of houseplants
-	Adding the notification feature to remind the user to water the plant.
-	3D structure design for the pot.
-	Adding the feature of monitoring the plant remotely to see if it needs fertilizer or suffers from a disease.
-	Auto problem diagnosis

## Project Team Members 
- Majd Otif 
- Manar Hakami 
- Afnan Matari 
- Manar Thabit




