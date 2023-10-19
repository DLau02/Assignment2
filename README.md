# Assignment2
To run the Jar files:

    Open your Ubuntu environment in your Virtual Machine

    Find the Directory where Assignment2-DHL190004 is stored

    Open your Linux Terminal (ctrl + alt + t)

    Start HDFS
        $ start-dfs.sh
        $ start-yarn.sh

    Set a directory in your HDFS
        $ hdfs dfs -mkdir

    Put your input_hw1.txt file in the directory
        Change cd to Directory where Assignment2-DHL190004 is stored
        $ hdfs dfs -put <filename in local file system/source>

    Change cd to Directory where Assignment2-DHL190004 is stored

        Make sure your first slash is backwards than all the rest are forwards
            for example, $ cd \Documents/Assignment2

        To run each JAR file:
            For 1A
                Run: $ cd /Assignment_2_1A
                Next Run: $ hadoop jar Assignment2_1A.jar Assignment2_1A /user/daniel/input/city_temperature.csv /output2_1A
            For 1B
                Run : $ cd ..
                Next Run: $ cd /Assignment_2_1B
                Next Run: $ hadoop jar Assignment2_1B.jar Assignment2_1B /user/daniel/input/city_temperature.csv /output2_1B
            For 1C
                Run : $ cd ..
                Run: $ cd /Assignment_2_1C
                Next Run: $ hadoop jar Assignment2_1C.jar Assignment2_1C /user/daniel/input/city_temperature.csv /output2_1C
            For 1D
                Run : $ cd ..
                Run: $ cd /Assignment_2_1D
                Next Run: $ hadoop jar Assignment2_1D.jar Assignment2_1D /user/daniel/input/city_temperature.csv /user/daniel/input/country-list.csv /output2_1C

    Outputs are already in Assignment1-DHL190004:
        Open folder of corresponding problem i.e. for 1C Assignment_2_1C
        Click on output i.e. output2_1C
        Open text file output_ to see the results for the corresponding problem number i.e. output2_1C

    To find Java source code files:
        Open folder of corresponding problem i.e. for 1C Assignment_2_1C
        Open folder of Java Project i.e. for 1C Assignment2_1C
        Open src folder and you find the java source code file
