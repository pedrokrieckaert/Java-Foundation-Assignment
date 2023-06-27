package src.data.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVReader<T> {
    private String seperator;
    private String file;
    private Map<String, Field> privateFields = new LinkedHashMap<String, Field>();
    private Class<T> genericType;
    private List<T> data;
    private List<String> order;
    private List<String> headers;
    private boolean initCompleted;
    private boolean hasHeader;

    public CSVReader(final Class<T> type, String file, boolean hasHeader, String seperator) {
        this.setSeperator(seperator);
        this.setFile(file);
        this.setGenericType(type);
        this.setHasHeader(hasHeader);
    }

    public String getSeperator() {
        return seperator;
    }

    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Map<String, Field> getPrivateFields() {
        return privateFields;
    }

    public void setPrivateFields(Map<String, Field> privateFields) {
        this.privateFields = privateFields;
    }

    public Class<T> getGenericType() {
        return genericType;
    }

    public void setGenericType(Class<T> genericType) {
        this.genericType = genericType;
    }

    /**
     * @return the data
     */
    public List<T> getData() {
        if (null == data) data = new ArrayList<T>();
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    public List<String> getOrder() {
        return order;
    }

    public CSVReader<T> setOrder(List<String> order) {
        this.order = order;
        return this;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public boolean isInitCompleted() {
        return initCompleted;
    }

    public void setInitCompleted(boolean initCompleted) {
        this.initCompleted = initCompleted;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public CSVReader<T> read(List<String> order) {
        this.setOrder(order);
        init();
        return this;
    }

    public CSVReader<T> read() {
        init();
        return this;
    }

    private void init() {
        if (!this.initCompleted) {
            Field[] allFields = genericType.getDeclaredFields();

            for (Field field : allFields) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    privateFields.put(field.getName(), field);
                }
            }
            try {
                readData();
            } catch (InstantiationException | IllegalAccessException e) {
                this.initCompleted = false;
            }
            this.initCompleted = true;
        }
    }

    private void readData() throws InstantiationException, IllegalAccessException{
        BufferedReader reader = null;
        String line = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                List<String> row = Arrays.asList(line.split(seperator));

                if (this.hasHeader) {
                    setHeaders(row);
                    this.hasHeader = false;
                    continue;
                }

                T refObject = genericType.newInstance();
                int index = 0;

                List<String> listOfFieldNames = (null != getOrder() ? getOrder() : new ArrayList<String>(privateFields.keySet()));

                for (String fieldName : listOfFieldNames) {
                    if (index >= row.size()) break;

                    assign(refObject, privateFields.get(fieldName), row.get(index++));
                }
                getData().add(refObject);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {

            }
        }
    }

    private Field assign(T refObject, Field field, String value) throws IllegalArgumentException, IllegalAccessException {
        field.setAccessible(true);
        if (field.getType().getName().equals("int")) {
            int intValue = Integer.parseInt(value);
            field.set(refObject, intValue);
        } else if (field.getType().getName().equals("java.util.Date")) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            try {
                Date dateValue = sdf.parse(value);
                field.set(refObject, dateValue);
            } catch (ParseException e) {

            }
        } else {
            field.set(refObject, value);
        }
        field.setAccessible(false);
        return field;
    }

}
