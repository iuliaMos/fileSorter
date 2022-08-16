package com.filesorter;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestSorter {
    private final String selectedFilePath;
    private final Pattern yearPattern;
    private final Pattern singlePattern;

    private long lineNr = 0;
    private long outLineNr = 0;

    Map<String, List<String>> mapByYear = new HashMap<>();
    Map<String, List<String>> mapSingleByYear = new HashMap<>();

    public TestSorter(String selectedFilePath) {
        this.selectedFilePath = selectedFilePath;
        String yearPattern = "(19|20)\\d{2}";
        this.yearPattern = Pattern.compile(yearPattern);
        this.singlePattern = Pattern.compile("single");
    }

    public void sortFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.selectedFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                this.lineNr ++;
                this.findYearInAString(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findYearInAString(String str) {
        if (str == null || str.trim().length() <= 4 || (str.trim().length() == 13 && str.endsWith(" - SINGLE"))) {
            return;
        }
        Matcher yearMatcher = this.yearPattern.matcher(str);
        Matcher singleMatcher = this.singlePattern.matcher(str.toLowerCase());
        if (yearMatcher.find()) {
            String year = yearMatcher.group();
            if (singleMatcher.find()) {
                List<String> list = mapSingleByYear.get(year);
                if (Objects.isNull(list)) {
                    list = new ArrayList<>();
                    mapSingleByYear.put(year, list);
                }
                list.add(str);
                this.outLineNr ++;
            } else {
                List<String> list = mapByYear.get(year);
                if (Objects.isNull(list)) {
                    list = new ArrayList<>();
                    mapByYear.put(year, list);
                }
                list.add(str);
                this.outLineNr ++;
            }
        } else {
            System.out.println(str);
        }

    }

    public void writeToFile() {
        File outFile = getValidFile();

        try (FileWriter fw = new FileWriter(outFile)) {
            writeToFile(fw, String.format("Nr of lines read: %s, nr of songs sorted: %s", this.lineNr, this.outLineNr));
            writeToFile(fw, "");

            List<String> years = new ArrayList<>(mapByYear.keySet());
            List<String> additionalYears = mapSingleByYear.keySet().stream().filter(year -> !years.contains(year)).collect(Collectors.toList());
            years.addAll(additionalYears);
            Collections.sort(years);

            years.forEach(year -> {
                List<String> singleSongs = mapSingleByYear.get(year);
                if (singleSongs != null && singleSongs.size() > 0) {
                    writeToFile(fw, year + " - SINGLE");
                    singleSongs.forEach(song -> writeToFile(fw, song));
                    writeToFile(fw, "");
                }
            });
            writeToFile(fw, "");
            years.forEach(year -> {
                List<String> singleSongs = mapByYear.get(year);
                if (singleSongs != null && singleSongs.size() > 0) {
                    writeToFile(fw, year);
                    singleSongs.forEach(song -> writeToFile(fw, song));
                    writeToFile(fw, "");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(FileWriter fw, String str) {
        try {
            fw.write(str + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getValidFile() {
        String pathOut = Paths.get(this.selectedFilePath).getParent().toString();
        String outFileName = "out.sorted.txt";
        File outFile = new File(pathOut + "/" + outFileName);
        boolean exists = outFile.exists();
        int i = 1;
        while (exists) {
            outFile = new File(pathOut + "/" + "out.sorted" + i + ".txt");
            i++;
            exists = outFile.exists();
        }
        return outFile;
    }
}
