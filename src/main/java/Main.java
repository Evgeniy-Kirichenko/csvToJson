import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/*
В данном домашнем задании вам предстоит написать два конвертора: из формата CSV и XML в формат JSON, а так же парсер JSON файлов в Java классы.

В первой задаче вам предстоит произвести запись в файл JSON объекта, полученного из CSV файла.

Для работы с проектом потребуются вспомогательные библиотеки, поэтому необходимо создать новый проект с использованием сборщика проекта Gradle или Maven. Далее пропишите зависимости для следующих библиотек: opencsv, json-simple и gson. Ниже приведен пример для сборщика Gradle:

compile 'com.opencsv:opencsv:5.1'
compile 'com.googlecode.json-simple:json-simple:1.1.1'
compile 'com.google.code.gson:gson:2.8.2'
В качестве исходной информации создайте файл data.csv со следующим содержимым и поместите его в корень созданного проекта:

1,John,Smith,USA,25
2,Inav,Petrov,RU,23
Помимо этого потребуется класс Employee, который будет содержать информацию о сотрудниках. Обратите внимание, что для парсинга Java классов из CSV потребуется пустой конструктор класса.

public class Employee {
    public long id;
    public String firstName;
    public String lastName;
    public String country;
    public int age;

    public Employee() {
        // Пустой конструктор
    }

    public Employee(long id, String firstName, String lastName, String country, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }
}
В резльтате работы программы в корне проекта должен появиться файл data.json со следующим содержимым:

[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Smith",
    "country": "USA",
    "age": 25
  },
  {
    "id": 2,
    "firstName": "Inav",
    "lastName": "Petrov",
    "country": "RU",
    "age": 23
  }
]
Реализация
Первым делом в классе Main в методе main() создайте массив строчек columnMapping, содержащий информацию о
предназначении колонок в CVS файле:

String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
Далее определите имя для считываемого CSV файла:

String fileName = "data.csv";
Далее получите список сотрудников, вызвав метод parseCSV():

List<Employee> list = parseCSV(columnMapping, fileName);
Метод parseCSV() вам необходимо реализовать самостоятельно. В этом вам поможет экземпляр класса CSVReader.
Передайте в его конструктор файловый ридер FileReader файла fileName. Данную операцию производите либо в
блоке try-catch с ресурсами, либо не забудьте закрыть поток после использования. Так же вам потребуется объект
класса ColumnPositionMappingStrategy. Используя объект стратении, укажите тип setType() и тип колонок
setColumnMapping(). Далее создайте экземпляр CsvToBean с использованием билдера CsvToBeanBuilder. При постройке
CsvToBean используйте ранее созданный объект стратегии ColumnPositionMappingStrategy. Созданный экземпляр объекта
CsvToBean имеет метод parse(), который вернет вам список сотрудников.

Полученный список преобразуйте в строчку в формате JSON. Сделайте это с помощью метода listToJson(), который вам
так же предстоит реализовать самостоятельно.

String json = listToJson(list);
При написании метода listToJson() вам понадобятся объекты типа GsonBuilder и Gson. Для преобразования списка объектов
в JSON, требуется определить тип этого спика:

Type listType = new TypeToken<List<T>>() {}.getType();
Получить JSON из экземпляра класса Gson можно с помощтю метода toJson(), передав в качестве аргументов список
сотрудников и тип списка:

String json = gson.toJson(list, listType);
Далее запишите полученный JSON в файл с помощью метода writeString(), который необходимо реализовать самостоятельно.
В этом вам поможет FileWriter и его метод write().

Задача 2: XML - JSON парсер
Описание
В данной задаче вам предстоит произвести запись в файл JSON объекта, полученного из XML файла.

Данную задачу выполняйте в рамках созданного в предыдущей задаче проекта.

В качестве исходной информации создайте файл data.xml со следующим содержимым (поместите этот файл в корень проекта):

<staff>
    <employee>
        <id>1</id>
        <firstName>John</firstName>
        <lastName>Smith</lastName>
        <country>USA</country>
        <age>25</age>
    </employee>
    <employee>
        <id>2</id>
        <firstName>Inav</firstName>
        <lastName>Petrov</lastName>
        <country>RU</country>
        <age>23</age>
    </employee>
</staff>
В резyльтате работы программы в корне проекта должен появиться файл data2.json с содержимым,
аналогичным json-файлу из предыдущей задачи.

Реализация
Для получения списка сотрудников из XML документа используйте метод parseXML():

List<Employee> list = parseXML("data.xml");
При реализации метода parseXML() вам необходимо получить экземпляр класса Document с использованием
DocumentBuilderFactory и DocumentBuilder через метод parse(). Далее получите из объекта Document
корневой узел Node с помощью метода getDocumentElement(). Из корневого узла извлеките список узлов
NodeList с помощью метода getChildNodes(). Пройдитесь по списку узлов и получите из каждого из них Element.
У элементов получите значения, с помощью которых создайте экземпляр класса Employee. Так как элементов может
быть несколько, организуйте всю работу в цикле. Метод parseXML() должен возвращать список сотрудников.

С помощью ранее написанного метода listToJson() преобразуйте список в JSON и запишите его в файл c помощью метода
writeString().

Задача 3: JSON парсер (со звездочкой *)
Описание
В данной задаче вам предстоит произвести чтение файла JSON, его парсинг и преобразование объектов JSON в классы Java.

В ходе выполнения программы в консоле вы должны увидеть следующие строки

> Task :Main.main()
Employee{id=1, firstName='John', lastName='Smith', country='USA', age=25}
Employee{id=2, firstName='Inav', lastName='Petrov', country='RU', age=23}
Реализация
Выполнение задачи следует начать с получения JSON из файла. Сделайте это с помощью метода readString():

String json = readString("new_data.json");
Метод readString() реализуйте самостоятельно с использованием BufferedReader и FileReader.
Метод должен возвращать прочитанный из файла JSON типа String.

Прочитанный JSON необходимо преобразовать в список сотрудников. Сделайте это с помощью метода jsonToList():

List<Employee> list = jsonToList(json);
При реализации метода jsonToList() вам потребуются такие объекта как: JSONParser, GsonBuilder, Gson.
JSONParser даст вам возможность с помощью метода parse() получить из строчки json массив JSONArray.
GsonBuilder будет использован исключительно для создания экземпляра Gson. Пройдитесь циклом по всем элементам
jsonArray и преобразуйте все jsonObject в Employee.class с помощью метода gson.fromJson().
Полученные экземпляры класса Employee добавляйте в список, который должен быть выведен из метода после его окончания.

Далее, выведите содержимое полученного списка в консоль. Не забудьте переопределить метод toString() в классе Employee.
 */
public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String fileNameCsvJson = "data.json";
        String fileNameXml = "data.xml";
        String fileNameXmlToJson = "data1.json";
        List<Employee> listCSV = parseCSV(columnMapping, fileName);
        writeString(listToJson(listCSV), fileNameCsvJson);
        List<Employee> listXML = parseXML(fileNameXml);
        writeString(listToJson(listXML), fileNameXmlToJson);



    }

    public static List<Employee> parseCSV(String[] Mapping, String fileName) {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(Mapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy).build();
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

    public static void writeString(String json, String fileName) {
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> userList = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fileName);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("employee");
            for (int i = 0; i < nodeList.getLength(); i++) {
                userList.add(getUser(nodeList.item(i)));
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static Employee getUser(Node node) {
        Employee employee = new Employee();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            employee.id = Integer.parseInt(getTagValue("id", element));
            employee.firstName = getTagValue("firstName", element);
            employee.lastName = getTagValue("lastName", element);
            employee.age = Integer.parseInt(getTagValue("age", element));
        }
        return employee;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }



}






