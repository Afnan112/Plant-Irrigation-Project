#include <WiFi.h>
#include <FirebaseESP32.h>
//Provide the token generation process info.
#include <addons/TokenHelper.h>
//Provide the RTDB payload printing info and other helper functions.
#include <addons/RTDBHelper.h>

/* 1. Define the WiFi credentials */
#define WIFI_SSID "--------"
#define WIFI_PASSWORD "---------"
/* 2. Define the API Key */
#define API_KEY "rG5A14QyNqR4NtzR-----------3"
/* 3. Define the RTDB URL */
#define DATABASE_URL "https://plant-irrigation----------b.firebaseio.com/sensors" //<databaseName>.firebaseio.com or <databaseName>.<region>.firebasedatabase.app

//Define Firebase Data object
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

/Pins 
int soil_sensor_val = 0;
int soil_sensor_pin = 34;
int soil_per;

int water_pump = 27;
int wp_state = 0;

void setup()
{
  Serial.begin(115200);
  delay(2000);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);

  /* Assign the api key (required) */
  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;

  pinMode(water_pump, OUTPUT); 
  Firebase.begin(DATABASE_URL, API_KEY);
  Firebase.setDoubleDigits(5);
}

void loop()
{
  if (Firebase.ready()) 
  {
    //read water pump state from FireBase
    Serial.printf("Get int pump_state--  %s\n", Firebase.getInt(fbdo, "/sensors/pump_state") ? String(fbdo.to<int>()).c_str() : fbdo.errorReason().c_str());
     wp_state=fbdo.to<int>();

    // Write soil moisture level in FireBase
    soil_sensor_val = analogRead(soil_sensor_pin);
    soil_per = ( (soil_sensor_val/4095.00) * 100 );
    Firebase.setInt(fbdo, "/sensors/soil_sensor", soil_per);

    //If water pump state is 1 (True) then turn on the water pump for 10 seconds
    if(wp_state == 1){
      digitalWrite(water_pump,HIGH);
      delay(10000);
      digitalWrite(water_pump,LOW);
      
      wp_state = 0;
      /// change water pump state to 0 in firebase
      Firebase.setInt(fbdo, "/sensors/pump_state", 0);
    }    
  }

  Serial.println();  
  Serial.print("pump_state:");
  Serial.print(wp_state);
  
  Serial.println();
  Serial.println("------------------");
  Serial.println();
  delay(1000);
  
}
