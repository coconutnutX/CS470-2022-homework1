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
│   │   │   │   ├── Control.java    // Main simulation logic
│   │   │   │   ├── Storage.java    // Store state of the processor
│   │   │   │   ├── dataStructure
│   │   │   │   │   ├── ActiveListItem.java
│   │   │   │   │   ├── Instruction.java
│   │   │   │   │   ├── IntegerQueueItem.java
│   │   │   │   │   └── PhyRegFile.java
│   │   │   │   └── state           // functions in each stage
│   │   │   │       ├── FD.java     // 1. Fetch and Decode
│   │   │   │       └── RD.java     // 2. Rename and Dispatch
│   │   │   │       ├── IS.java     // 3. Issue
│   │   │   │       ├── EX.java     // 4. Execute
│   │   │   │       ├── CM.java     // 5. Commit
│   │   │   └── util
│   │   │       └── Parser.java     // input and output JSON
│   │   └── resources
│   └── test
│       └── java
└── target
```

# Implementation Details

![pipeline](./pipeline.jpg)

## Data structures

The Storage object contains all data structures in the processor.

The program keeps a list of Storage representing the data in each cycle.

The program initilizes a Storage in cycle 0, and makes a deep copy in each each subsequent cycle.

## Execution order

Since later stages may recycle resources, and earlier stages can use them in the same cycle. In Control.propagate(), the stages are processed in reverse order.

## Dependency

Use [Gson](https://github.com/google/gson) to convert JSON/Java Object, and for serialization/deserialization when deep copying Storage.

Use [Logback](https://logback.qos.ch/) to log intermediate results.

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