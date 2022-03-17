package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.name.Name;
import seedu.address.model.note.Note;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class DeleteTagCommand extends Command {
    public static final String COMMAND_WORD = "tag-del";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Delete a tag from a person in our contact list. "
            + "Parameters: "
            + "INDEX (must be a positive integer) "
            + "TAG NAME (must be non-empty)\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + "friend";
    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Deleted tag: %1$s";
    public static final String MESSAGE_INVALID_TAG = "This person does not have this tag!";

    private final Index index;
    private final String tagName;

    /**
     * Public constructor for DeleteTagCommand
     * @param index Index of person to delete tag from
     * @param tagName Name of tag
     */

    public DeleteTagCommand(Index index, String tagName) {
        requireAllNonNull(index, tagName);

        this.index = index;
        this.tagName = tagName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Exception when index out of bounds
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }


        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = deleteTagFromPerson(personToEdit);

        // Exception when a non-existent tag is indicated for deletion
        Tag testTag = new Tag(this.tagName);
        if (!personToEdit.getTags().contains(testTag)) {
            throw new CommandException(MESSAGE_INVALID_TAG);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_DELETE_TAG_SUCCESS, this.tagName));
    }

    /**
     * Creates and returns a {@code Person} with a new tag {@code tagName} added to
     * {@code personToEdit}
     *
     * @param personToEdit Person to be edited
     * @return New Person object with the tag added (tag list updated)
     */
    private Person deleteTagFromPerson(Person personToEdit) {
        // Keep all other fields the same
        Name updatedName = personToEdit.getName();
        Phone updatedPhone = personToEdit.getPhone();
        Email updatedEmail = personToEdit.getEmail();
        Address updatedAddress = personToEdit.getAddress();
        List<Note> updatedNotes = personToEdit.getNotes();

        // Changing tags
        // Make modifiable copy since Person#getTags returns an unmodifiable Set
        Set<Tag> tagList = new HashSet<>(personToEdit.getTags());
        tagList.remove(new Tag(this.tagName));

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, tagList, updatedNotes);
    }

    @Override
    public boolean equals(Object other) {

        return other == this // short circuit if same object
                || (other instanceof DeleteTagCommand // instanceof handles nulls
                && (index.equals(((DeleteTagCommand) other).index)
                && tagName.equals((((DeleteTagCommand) other).tagName))));
    }
}
