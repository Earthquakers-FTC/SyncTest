// The size of the temporary buffer to store the incoming data in
#define BUFFER_LEN 2

// The number of bytes to keep in storage
#define STORAGE_SIZE 8

// The position of the index of the data in the storage array
#define BUFFER_INDEX_POS 0

// The position of the data in the buffer
#define BUFFER_DATA_POS 1

#define LED_PIN 13

byte storage[STORAGE_SIZE];
byte buffer[BUFFER_LEN];

void readItem();
void writeItem(char, char);

void setup() {
  memset(buffer, 0, BUFFER_LEN);
  memset(storage, 0, BUFFER_LEN);
  
  Serial.begin(9600);
}

void loop() {
  // if(Serial.available() > 0)
  //  readItem();
 
  writeItem(random(0, STORAGE_SIZE), random(0, 255));
  
  delay(100);
}

void readItem() {
  //Serial.readBytes(buffer, 2);

  byte index = buffer[BUFFER_INDEX_POS];
  
  if(index > STORAGE_SIZE - 1)
    return;
  
  storage[index] = buffer[BUFFER_DATA_POS];
}

void writeItem(char index, char new_value) {
  if(index > STORAGE_SIZE - 1)
    return;
    
  storage[index] = new_value;
  
  buffer[BUFFER_INDEX_POS] = index;
  buffer[BUFFER_DATA_POS] = new_value;

  Serial.write(buffer, BUFFER_LEN);
}
