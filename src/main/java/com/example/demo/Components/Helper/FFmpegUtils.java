package com.example.demo.Components.Helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class FFmpegUtils {

    public static long getVideoDuration(String filePath) {
        try {
            String ffmpegPath = "C:\\ProgramData\\chocolatey\\lib\\ffmpeg\\tools\\ffmpeg\\bin\\ffmpeg.exe"; // Full path to ffmpeg executable
            String[] command = {
                ffmpegPath, "-i", filePath
            };
    
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
    
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            StringBuilder errorOutput = new StringBuilder();
    
            while ((line = stdError.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
    
            process.waitFor();
    
            System.out.println("FFmpeg Output: " + errorOutput.toString()); // Add this line for debugging
    
            return parseDuration(errorOutput.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    

    private static long parseDuration(String ffmpegOutput) {
        System.out.println("Parsing FFmpeg Output: " + ffmpegOutput); // Add this line for debugging
        Pattern pattern = Pattern.compile("Duration: (\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{2})");
        Matcher matcher = pattern.matcher(ffmpegOutput);

        if (matcher.find()) {
            int hours = Integer.parseInt(matcher.group(1));
            int minutes = Integer.parseInt(matcher.group(2));
            int seconds = Integer.parseInt(matcher.group(3));
            // int fraction = Integer.parseInt(matcher.group(4));

            long totalSeconds = hours * 3600 + minutes * 60 + seconds;
            return totalSeconds;
        }

        return 0;
    }
}

