package restful.phonebook.factory;

import restful.phonebook.model.Contact;
import restful.phonebook.model.IndexedContact;
import restful.phonebook.dao.PhoneBookDao;
import restful.phonebook.repository.PhoneBookRepository;

import java.util.Map;

/**
 * Should be implemented to create a collection of initial contacts that will be used by classes,
 * like {@link PhoneBookRepository}, that do not load contact data from a persistent mean,
 * but produce their own contacts and use them, as long as the server runs.
 * <p/>
 * This factory will also handle the indexing of the contacts that are inserted in the phone book,
 * removing this responsibility from the {@link PhoneBookDao} implementations that will use it.
 */
public interface Factory {

    /**
     * Should be implemented to create an initial collection of contacts.
     *
     * @return a collection of initial contacts.
     */
    Map<Long, IndexedContact> createInitPhoneBook();

    /**
     * Should be implemented to index a {@link Contact} and transform it
     * to an {@link IndexedContact}.
     *
     * @param contact - contact to be indexed.
     * @return an {@link IndexedContact} derived from the contact passed as parameter.
     */
    IndexedContact createIndexedContact(Contact contact);

}