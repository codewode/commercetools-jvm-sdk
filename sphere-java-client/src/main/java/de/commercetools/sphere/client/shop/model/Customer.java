package de.commercetools.sphere.client.shop.model;

import de.commercetools.sphere.client.model.Reference;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/** A customer that exists in the backend. */
@JsonIgnoreProperties("type")
public class Customer {
    private String id;
    private int version;
    private String email = "";
    @JsonProperty("title") private String title = "";
    @JsonProperty("firstName") private String firstName = "";
    @JsonProperty("middleName") private String middleName = "";
    @JsonProperty("lastName") private String lastName = "";
    @JsonProperty("password") private String passwordHash = "";    // not exposed (needed?)
    private List<Address> shippingAddresses = new ArrayList<Address>();  // initialize to prevent NPEs
    private int defaultShippingAddress;
    @JsonProperty("isEmailVerified") private boolean isEmailVerified;
    private Reference<CustomerGroup> customerGroup;

    // for JSON deserializer
    private Customer() {}

    public Customer(String id, int version) {
        this.id = id;
        this.version = version;
    }

    /** Unique id of this customer. */
    public String getId() { return id; }

    /** Version of this customer that increases when the customer is modified. */
    public int getVersion() { return version; }

    /** Email address of the customer. */
    public String getEmail() { return email; }

    /** Returns the parts of customer's name conveniently wrapped in a single object. */
    public CustomerName getName() {
        return new CustomerName(title,  firstName, middleName, lastName);
    }

    /** Customer's title. */
    public String getTitle() { return title; }

    /** Customer's first name. */
    public String getFirstName() { return firstName; }

    /** Customer's last name. */
    public String getLastName() { return lastName; }

    /** Customer's middle name. Use e.g. middle names joined by spaces if multiple middle names are needed. */
    public String getMiddleName() { return middleName; }

    /** List of customer's shipping addresses. */
    public List<Address> getShippingAddresses() { return shippingAddresses; }

    /** Index of the default shipping address in the shipping addresses list. It is optional. */
    public int getDefaultShippingAddress() { return defaultShippingAddress; }

    /** A flag indicating that the customer email has been verified. */
    public boolean isEmailVerified() { return isEmailVerified; }

    /** The customer group this customer belongs to. It is optional. */
    public Reference<CustomerGroup> getCustomerGroup() { return customerGroup; }
}
