package restful.phonebook.model;

import java.util.List;

/**
 * A class containing all the useful information of a contact in the phone book.
 * <p/>
 * When a phone book user wants to insert a new contact in the book, he will only be
 * concerned about the information included in this class.
 */
public class Contact {

    private String name;
    private String address;
    private List<String> phoneNumberList;

    Contact(Contact contact) {
        this.name = contact.getName();
        this.address = contact.getAddress();
        this.phoneNumberList = contact.getPhoneNumberList();
    }

    Contact() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }
}
