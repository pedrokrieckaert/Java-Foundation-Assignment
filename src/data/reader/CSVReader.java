package src.data.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;

public class CSVReader<T> {
    private String separator;
    private String file;
    private Map<String, Field> privateFields = new LinkedHashMap<String, Field>();
    private Class<T> genericType;
    private List<T> data;
    private List<String> order;
    private List<String> headers;
    private boolean initCompleted;
    private boolean hasHeader;

    public CSVReader(final Class<T> type, String file, boolean hasHeader, String separator) {
        this.setSeparator(separator);
        this.setFile(file);
        this.setGenericType(type);
        this.setHasHeader(hasHeader);
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
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
     * Getter for this data, read from the specified CSV file as a new ArrayList.
     * @return this data, stored in the class variable.
     */
    public List<T> getData() {
        if (null == data) data = new ArrayList<T>();
        return data;
    }

    /**
     * Setter for this data
     * @param data the data to set to this data
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

    /**
     *
     * @param order
     * @return
     */
    public CSVReader<T> read(List<String> order) {
        this.setOrder(order);
        init();
        return this;
    }

    public CSVReader<T> read() {
        init();
        return this;
    }

    /**
     * Initializes the CSVReader object by populating the privateFields map and reading data from the CSV file if not already initialized.
     * The privateFields map contains private fields of the generic type.
     * Data is read from the CSV file using the readData() method.
     */
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

    /**
     * Reads the CSV file, checks for a header row, and assigns the read values to the object's fields
     * in the order specified by a String array. <br>
     * The read data is populated into the object's fields based on the specified order.
     * If the file has a header row, it is skipped during assignment.
     * Each line of the CSV file is processed and assigned to the corresponding fields of the object.
     * The object's fields are assigned values in the order specified by the String array.
     *
     * @throws IllegalArgumentException if the value cannot be assigned to the field.
     * @throws IllegalAccessException if there is an illegal access exception during field assignment.
     */
    private void readData() throws InstantiationException, IllegalAccessException{
        BufferedReader reader = null;
        String line = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                List<String> row = Arrays.asList(line.split(separator));

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

    /**
     * Assigns a value to a field of the given object using reflection.
     *
     * @param refObject the object to assign the value to.
     * @param field the field to assign the value to.
     * @param value the value to assign.
     * @return the assigned field.
     * @throws IllegalArgumentException if the value cannot be assigned to the field.
     * @throws IllegalAccessException if there is an illegal access exception during field assignment.
     */
    private Field assign(T refObject, Field field, String value) throws IllegalArgumentException, IllegalAccessException {
        field.setAccessible(true);
        if (field.getType().getName().equals("int")) {
            int intValue = Integer.parseInt(value);
            field.set(refObject, intValue);
        } else if (field.getType().getName().equals("java.math.BigDecimal")) {
            BigDecimal bigDecimalValue = new BigDecimal(value);
            field.set(refObject, bigDecimalValue);
        }else {
            field.set(refObject, value);
        }
        field.setAccessible(false);
        return field;
    }

}
