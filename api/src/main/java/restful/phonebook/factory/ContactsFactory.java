package restful.phonebook.factory;

import restful.phonebook.model.Contact;
import restful.phonebook.model.IndexedContact;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the {@link Factory} interface
 */
public class ContactsFactory implements Factory {

    private long identifier = 0;

    @Override
    public Map<Long, IndexedContact> createInitPhoneBook() {
        return new HashMap<>();
    }

    @Override
    public IndexedContact createIndexedContact(Contact contact) {
        return new IndexedContact(newUniqueIdentifier(), contact);
    }

    private long newUniqueIdentifier() {
        return identifier++;
    }

}