package restful.phonebook.repository;

import org.springframework.stereotype.Repository;
import restful.phonebook.model.Contact;
import restful.phonebook.model.IndexedContact;
import restful.phonebook.dao.PhoneBookDao;
import restful.phonebook.factory.ContactsFactory;
import restful.phonebook.factory.Factory;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * An implementation of {@link PhoneBookDao}, that creates a {@link HashMap} of contacts, using a {@link Factory},
 * and maintains it in memory as long as the server runs.
 */
@Repository
public class PhoneBookRepository implements PhoneBookDao {

    private Map<Long, IndexedContact> phoneBook;
    private Factory factory;

    public PhoneBookRepository() {
        factory = new ContactsFactory();
    }

    @PostConstruct
    public void init() {
        phoneBook = factory.createInitPhoneBook();
    }

    @Override
    public Optional<IndexedContact> findContact(Long id) {
        return phoneBook.values().stream().filter(indexedContact -> indexedContact.getId().equals(id)).findFirst();
    }

    @Override
    public Collection<IndexedContact> findContacts(Optional<String> name, Optional<String> address, Optional<String> phone) {
        Stream<IndexedContact> contactStream = phoneBook.values().stream();
        if (!name.isPresent() && !address.isPresent() && !phone.isPresent()) {
            return contactStream.collect(toList());
        }
        return contactStream.filter(contact -> (filterByAttribute(contact::getName, name) && filterByAttribute(contact::getAddress, address) && filterByAttributeAsList(contact::getPhoneNumberList, phone))).collect(toList());
    }

    @Override
    public IndexedContact addContact(Contact contact) {
        IndexedContact indexedContact = factory.createIndexedContact(contact);
        phoneBook.put(indexedContact.getId(), indexedContact);
        return indexedContact;
    }

    @Override
    public IndexedContact putContact(IndexedContact contact) {
        phoneBook.put(contact.getId(), contact);
        return contact;
    }

    @Override
    public void deleteContact(Long id) {
        phoneBook.remove(id);
    }

    /**
     * Compares a contact's String attribute with another String value, referred as <code>comparingAttribute</code> parameter.
     * For example, if the attribute you want to compare is name, the method will compare contact's name with the
     * String contained in <code>comparingAttribute</code>.
     * <p/>
     * If comparingAttribute, which is Optional, contains no value, true will be returned.
     *
     * @param getContactAttribute - the getter that will return contact's proper attribute.
     * @param comparingAttribute  - the value that will be checked against contact's attribute.
     * @return - false if comparingAttribute has a value and is not equal to the contact's attribute, true otherwise.
     */
    private boolean filterByAttribute(Supplier<String> getContactAttribute, Optional<String> comparingAttribute) {
        return comparingAttribute.map(attribute -> getContactAttribute.get().equals(attribute)).orElse(true);
    }

    /**
     * Compares a contact's list of String attributes with another String value, referred as <code>comparingAttribute</code> parameter.
     * For example, if the attribute you want to compare is the phoneNumberList, the method will compare contact's phone numbers with the
     * String contained in <code>comparingAttribute</code>.
     * <p/>
     * If comparingAttributeAsList, which is Optional, contains no value, true will be returned.
     *
     * @param getContactAttributeAsList - the getter that will return contact's proper attribute.
     * @param comparingAttribute        - the value that will be checked against contact's attribute.
     * @return - false if comparingAttribute has a value and is not equal to the contact's attribute, true otherwise.
     */
    private boolean filterByAttributeAsList(Supplier<List<String>> getContactAttributeAsList, Optional<String> comparingAttribute) {
        return comparingAttribute.map(attribute -> getContactAttributeAsList.get().contains(attribute)).orElse(true);
    }
}