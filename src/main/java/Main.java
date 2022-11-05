import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        //listToJson(list);
        writeString(listToJson(list),"data.json");


    }


    public static List<Employee> parseCSV(String[] Mapping, String fileName) {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy cPMStrategy = new ColumnPositionMappingStrategy();
            cPMStrategy.setType(Employee.class);
            cPMStrategy.setColumnMapping(Mapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(cPMStrategy).build();
            return csv.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json,String fileName){
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
