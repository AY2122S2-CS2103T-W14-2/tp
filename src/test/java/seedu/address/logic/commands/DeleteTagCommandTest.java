package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

class DeleteTagCommandTest {
    // Test tags
    // Because PersonBuilder#withTags takes String ... instead of Set<Tag>
    private static final String TAG_1 = "friends"; // ALICE has a tag called "friends"

    // Test model
    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalTaskBook(), new UserPrefs());

    @Test
    void execute_deleteTagCommandUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Removing 1 tag to the editedPerson
        Set<Tag> firstPersonTags = new HashSet<>(firstPerson.getTags()); // Copy of Set<Tag> of ALICE (first person)
        firstPersonTags.remove(new Tag(TAG_1));

        // Convert Set<Tag> to array for PersonBuilder#withTags
        String[] firstPersonTagsStringArray = firstPersonTags
                .stream()
                .map(x -> x.tagName)
                .toArray(String[]::new);

        Person editedPerson = new PersonBuilder(firstPerson).withTags(firstPersonTagsStringArray).build();

        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, TAG_1);

        String expectedMessage = String.format(DeleteTagCommand.MESSAGE_DELETE_TAG_SUCCESS, TAG_1);

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()),
                new TaskBook(model.getTaskBook()),
                new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(deleteTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        AddTagCommand addTagCommand = new AddTagCommand(outOfBoundIndex, TAG_1);

        assertCommandFailure(addTagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final DeleteTagCommand command = new DeleteTagCommand(INDEX_FIRST_PERSON, TAG_1);

        // If they are the same objects, they are equal
        assertTrue(command.equals(command));

    }
}
