package sphere.model.products;

import java.util.ArrayList;

/** Definition of a Product (e.g. 'Shoe').
 *  Every Product must be based on some product definition. */
public class ProductDefinition {
    private String id;
    private String version;
    private String name;
    private String description;
    private ArrayList<AttributeDefinition> attributes = new ArrayList<AttributeDefinition>();

    // for JSON deserializer
    private ProductDefinition() { }

    public String getId() {
        return id;
    }
    public String getVersion() {
        return version;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<AttributeDefinition> getAttributes() {
        return attributes;
    }
}
