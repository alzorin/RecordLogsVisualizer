package com.recordlogs;

import com.recordlogs.model.SourceData;
import org.apache.commons.csv.CSVParser;

import java.util.HashSet;
import java.util.Set;

public class SceneData {

    public static CSVParser csvParser;
    public static SourceData sourceData;
    public static Set<String> selectedMeasurements = new HashSet<>();
    public static String selectedCaseColumn;
    public static String selectedTimestampColumn;
    public static String selectedActivityColumn;
    public static String selectedTypeColumn;
    public static long loadTime;

}
