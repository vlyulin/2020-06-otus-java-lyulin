package ru.otus.chart.core.model;

import java.util.HashMap;
import java.util.Map;

public class ModuleOperationsData {
    private String moduleName;
    Map<String,Integer> chartData;

    public ModuleOperationsData(String moduleName) {
        this.moduleName = moduleName;
        this.chartData = new HashMap<>();
    }

    public ModuleOperationsData(String moduleName, Map<String, Integer> chartData) {
        this.moduleName = moduleName;
        this.chartData = chartData;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Map<String, Integer> getChartData() {
        return chartData;
    }

    public void setChartData(Map<String, Integer> chartData) {
        this.chartData = chartData;
    }
}
