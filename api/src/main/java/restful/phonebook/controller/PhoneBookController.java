package restful.phonebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.Controller;
import restful.phonebook.model.Contact;
import restful.phonebook.model.IndexedContact;
import restful.phonebook.dao.PhoneBookDao;
import restful.phonebook.exceptions.ContactNotFoundException;
import restful.phonebook.exceptions.IllegalContactException;

import java.util.Collection;
import java.util.Optional;

/**
 * The controller is actually a concept of the Spring MVC framework.
 * <p>
 * The component that takes responsibility to process the request from the front controller is the {@link Controller}, and it
 * applies server's business logic. This class is a controller that will handle (REST) HTTP requests for the project's phonebook.
 */
@RestController
@RequestMapping("/phonebook/contacts")
public class PhoneBookController {

    @Autowired
    private PhoneBookDao phoneBookDao;

    /**
     * Exposes the URI "phonebook/contacts" and "listens" for GET requests. If the request does not contain any more parameters,
     * the method will return all contacts that exist in the phonebook. If some of the optional parameters "name", "surname" or "phone" exist in the HTTP request,
     * it will return the contacts matching these values.
     *
     * @param name    - the name to be looked up in phonebook contacts.
     * @param address - the surname to be looked up in phonebook contacts.
     * @param phone   - the phone to be looked up in phonebook contacts.
     * @return a collection of contacts that match with the name, surname and/or phone provided by the request. If no parameters are provided returns all the contacts in the phonebook.
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Collection<IndexedContact> getContacts(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "address", required = false) String address,
                                                  @RequestParam(value = "phone", required = false) String phone) {
        return phoneBookDao.findContacts(Optional.ofNullable(name), Optional.ofNullable(address), Optional.ofNullable(phone));
    }

    /**
     * Searches in phonebook for a contact that matches the GET request's id and if such a contact exists, returns it.
     *
     * @param id - the contact id to be looked up in the phone book.
     * @return the contact that matches request's id, if such contact exists.
     * @throws ContactNotFoundException if no contact is found with this id.
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json")
    public IndexedContact getContact(@PathVariable String id) throws ContactNotFoundException {
        return phoneBookDao.findContact(Long.valueOf(id)).orElseThrow(() -> new ContactNotFoundException(id));
    }

    /**
     * Adds the contact included in the request body to the phone book. The {@link Contact} provided by the client
     * will contain no indexing, but just the name, surname and phone details. It does not matter if there is already
     * another client with the same details. The new client will be provided with a unique id and be added in
     * the phone book.
     *
     * @param contact - the contact to be added in the phone book.
     * @return an {@link IndexedContact}, which has the same details with the contact provided, but also the unique id that identifies it in the phone book.
     */
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public IndexedContact addContact(@RequestBody Contact contact) {
        return phoneBookDao.addContact(contact);
    }

    /**
     * Adds the {@link IndexedContact} in the request body to the phone book. If there already exists a contact
     * under the specific URI it will update the contact. For this reason the URI's id and the {@link IndexedContact}'s id
     * should be the same. If there is not contact yet, it will create it with
     * the request body.
     *
     * @param id             - the id that identifies the resource (contact) to be added.
     * @param indexedContact - the contact to be added under the specific URI.
     * @return the contact added to the phone book.
     * @throws IllegalContactException if the URI's id and the {@link IndexedContact}'s id are not the same.
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public IndexedContact putContact(@PathVariable String id, @RequestBody IndexedContact indexedContact) throws IllegalContactException {
        if (indexedContact.getId() == null || !id.equals(indexedContact.getId().toString())) {
            throw new IllegalContactException("The contact's id should be the same with the URI's id.");
        }
        return phoneBookDao.putContact(indexedContact);
    }

    /**
     * Deletes the contact under the specific URI.
     *
     * @param id - the id of the contact to be deleted.
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable String id) {
        phoneBookDao.deleteContact(Long.valueOf(id));
    }

}
