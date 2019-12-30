package restful.phonebook.dao;

import restful.phonebook.model.Contact;
import restful.phonebook.model.IndexedContact;

import java.util.Collection;
import java.util.Optional;

/**
 * An interface that should be implemented to interact with the data layer that contains the contacts.
 */
public interface PhoneBookDao {

    /**
     * If all the optional parameters are empty, will return a collection with all the phonebook's contacts.
     * <p/>
     * If there are non-empty parameters, will return a collection with the contacts that match these parameters.
     *
     * @param name    - optional parameter that when not empty will be used to filter contacts with this specific name.
     * @param address - optional parameter that when not empty will be used to filter contacts with this specific address.
     * @param phone   - optional parameter that when not empty will be used to filter contacts with this specific phone.
     * @return all contacts if all parameters are empty, or the contacts that match the non empty parameters.
     */
    Collection<IndexedContact> findContacts(Optional<String> name, Optional<String> address, Optional<String> phone);

    /**
     * Will return a contact with the specific id, or an empty optional if no match could be made.
     *
     * @param id - the id of the contact that should be returned.
     * @return a contact with the specific id, or an empty optional if no match could be made.
     */
    Optional<IndexedContact> findContact(Long id);

    /**
     * Will add a contact in the phone book. The specific {@link PhoneBookDao} implementation will handle the indexing of the contact,
     * and make sure it provides a valid id to it.
     *
     * @param contact - the contact that will be added in the phone book. No id information should be given here.
     * @return an {@link IndexedContact} with the same details as the {@link Contact} provided, but also the id given by the phone book.
     */
    IndexedContact addContact(Contact contact);

    /**
     * Will add an {@link IndexedContact} in the phone book. The parameter is an {@link IndexedContact},
     * which means that it has an id field. If there is already a contact with this id in the {@link PhoneBookDao},
     * the old contact will be replaced with the new one. If there is no contact with this id, the contact
     * will be saved to the phone book.
     *
     * @param contact - the contact to be put in the phone book. Contact already has an id assigned to it.
     * @return the contact added in the phone book.
     */
    IndexedContact putContact(IndexedContact contact);

    /**
     * Will delete the contact with the specific id.
     *
     * @param id - the id of the contact to be deleted.
     */
    void deleteContact(Long id);

}
