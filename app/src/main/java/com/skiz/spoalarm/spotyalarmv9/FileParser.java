package com.skiz.spoalarm.spotyalarmv9;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileParser {

        //Constructor initialize Attributes
        public FileParser(String filepath, String filename) {
            this.filepath = filepath;
            this.filename = filename;
            alarmfile = new File(filepath+filename);
        }

        private String initFile = "countalarm = 0\n";
        private String filepath,filename,content;
        private int[] counter = {0, 0, 0, 1, 0, 0};

        private StringUtility initialize = new StringUtility();
        private File alarmfile;

        //Create File
        public void initFile() throws IOException {
            if(alarmfile.exists() != true){
            alarmfile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(alarmfile));
            writer.write(initFile);
            writer.close();
            }
        }

        //Alarm-File to String
        public String readFile() throws IOException{
            return new String (Files.readAllBytes(Paths.get(alarmfile.getAbsolutePath())));
        }

        //Count how much alarms exist
        public int countAlarm()throws IOException{
            String content = readFile();
            int length = "countalarm".length();
            return Integer.parseInt(content.substring(length+3,length+4));
        }

        public void addAlarm(String alarmName,String time,int weekrepeat)throws IOException{
            content = readFile();
            this.content += "alarm"+(countAlarm()+1)+" = {"+alarmName+";"+time+";"+weekrepeat+";}\n";
            content = content.substring(0,13) + (countAlarm()+1) + content.substring(13 + 1);
        }

        public String[] alarmToArray() throws IOException{
            String[] alarms = readFile().substring(15).split("\n");
            String[] content = new String[countAlarm() * 4];
            int[] counter = {0, 0, 0, 1, 0, 0};

            while ((alarms.length - 1) >= counter[0]) {
                String line = alarms[counter[0]];
                while (2 >= counter[1]) {
                    if (line.startsWith("alarm")) {
                        int startalarm = line.indexOf("alarm" + counter[3]);
                        int endalarm = startalarm + ("alarm" + counter[3]).length();
                        content[counter[2]] = line.substring(startalarm, endalarm);
                        line = line.substring((line.indexOf("{") + 1), line.indexOf("}"));
                        counter[2]++;
                    }
                    counter[5] = counter[4] + 1;
                    counter[4] = line.indexOf(";", counter[5]);
                    switch (counter[1]) {
                        case 0:
                            content[counter[2]] = line.substring(counter[5] - 1, counter[4]);
                            break;
                        default:
                            content[counter[2]] = line.substring(counter[5], counter[4]);
                            break;

                    }
                    counter[1]++;
                    counter[2]++;
                }
                counter[0]++;
                counter[3]++;
                counter[1] = 0;
                counter[4] = 0;
                counter[5] = 0;
                }
            return content;
        }

        public void saveFile()throws IOException{
            BufferedWriter writer = new BufferedWriter(new FileWriter(alarmfile));
            writer.write(content);
            writer.close();
        }
}
