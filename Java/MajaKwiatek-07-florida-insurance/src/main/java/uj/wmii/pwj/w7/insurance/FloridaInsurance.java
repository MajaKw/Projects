package uj.wmii.pwj.w7.insurance;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FloridaInsurance {
    private final String zipFileName;
    private int countyIdx;
    private int tiv_2011_Idx;
    private int tiv_2012_Idx;

    private List<InsuranceEntry> insuranceEntryList;
    private static final String CSV_SEPERATOR = ",";

    public FloridaInsurance(String zipFileName) {
        this.zipFileName = zipFileName;
        insuranceEntryList = new ArrayList<>();
    }

    void setIndexes(String line) {
        ArrayList<String> columns = new ArrayList<>(Arrays.asList(line.split(CSV_SEPERATOR)));
        countyIdx = columns.indexOf("county");
        tiv_2011_Idx = columns.indexOf("tiv_2011");
        tiv_2012_Idx = columns.indexOf("tiv_2012");
    }

    public void count() {
        ArrayList<String> data = new ArrayList<>();
        data.add(Long.toString(insuranceEntryList.stream().map(InsuranceEntry::county).distinct().count()));
        saveInFile("count.txt", data);
    }

    public void sum() {
        ArrayList<String> data = new ArrayList<>();
        data.add(insuranceEntryList.stream().map(InsuranceEntry::tiv_2012).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
        saveInFile("tiv2012.txt", data);
    }

    public void mostValuable() {
        ArrayList<String> data = new ArrayList<>();
        data.add("country,value");
        data.addAll(insuranceEntryList.stream()
                .collect(Collectors.groupingBy(
                        InsuranceEntry::county,
                        Collectors.reducing(BigDecimal.ZERO,
                                entry -> entry.tiv_2012().subtract(entry.tiv_2011()),
                                BigDecimal::add)
                ))
                .entrySet().stream()
                .sorted((entry1, entry2) ->
                        entry2.getValue().compareTo(entry1.getValue()))
                .limit(10)
                .map(entry -> entry.getKey() + CSV_SEPERATOR +
                        entry.getValue().setScale(2, RoundingMode.HALF_UP))
                .toList());
        saveInFile("most_valuable.txt", data);
    }


    void saveInFile(String fileName, List<String> data) {
        try {
            Path path = Path.of(fileName);
            Files.createFile(path);
            Files.write(path, data, StandardCharsets.UTF_8);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Error: File already exists: " + fileName);
        } catch (IOException e) {
            System.out.println("Error: creating file: " + fileName);
        }
    }

    public void loadFile() {
        try (ZipFile zipFile = new ZipFile(zipFileName)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) break;
                try (Scanner scanner = new Scanner(zipFile.getInputStream(entry))) {
                    String line = scanner.nextLine();
                    setIndexes(line);
                    while (scanner.hasNext()) {
                        line = scanner.nextLine();
                        String[] columns = line.split(CSV_SEPERATOR);
                        insuranceEntryList.add(new InsuranceEntry(columns[countyIdx], new BigDecimal(columns[tiv_2011_Idx]), new BigDecimal(columns[tiv_2012_Idx])));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IO error");
        }
    }


    public static void main(String[] args) {
        FloridaInsurance floridaInsurance = new FloridaInsurance("FL_insurance.csv.zip");
        floridaInsurance.loadFile();
        floridaInsurance.count();
        floridaInsurance.sum();
        floridaInsurance.mostValuable();
    }

}
