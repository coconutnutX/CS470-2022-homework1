# Structure

```
.
├── README.md
├── logback.xml
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── Main.java
│   │   │   ├── mips
│   │   │   │   ├── Control.java
│   │   │   │   ├── Storage.java
│   │   │   │   ├── dataStructure
│   │   │   │   │   ├── ActiveListItem.java
│   │   │   │   │   ├── Instruction.java
│   │   │   │   │   ├── IntegerQueueItem.java
│   │   │   │   │   └── PhyRegFile.java
│   │   │   │   └── state
│   │   │   │       ├── CM.java
│   │   │   │       ├── EX.java
│   │   │   │       ├── FD.java
│   │   │   │       ├── IS.java
│   │   │   │       └── RD.java
│   │   │   └── util
│   │   │       └── Parser.java
│   │   └── resources
│   └── test
│       └── java
└── target
```

1. The program reads a json file in Main.java, run some simulations, and outputs a json file.
2. Main simulation logic is in Control.propagate().
3. It initializes an empty Storage, make a deep copy at the beginning of each cycle, and modify the copy.
4. Functions in each state are in the state package. (i.e. FD/RD/IS/EX/CM.java)

# Run

## 1. run jar in terminal

1. Put test.json in target folder

2. cd to target

3. Run

   ```
   java -jar ACA-hw1-1.0-SNAPSHOT-jar-with-dependencies.jar "test.json"
   ```

4. It will output test_output.json

## 2. run Main in IDEA

1. Put test.json in this folder

2. Open Main.java in IDEA

3. Modify line 15 & 16

   ```java
   // String testFileName = args[0];
   String testFileName = "test.json";
   ```

4. Run Main

5. It will output test_output.json