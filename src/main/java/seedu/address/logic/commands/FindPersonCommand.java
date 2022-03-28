package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.model.Model;
import seedu.address.model.entry.predicate.PersonContainsKeywordsPredicate;


/**
 * Finds and lists all persons in address book whose name, companyName, and tags contains any of the argument keywords.
 * Keyword matching is case insensitive
 * Find person command uses User input to specify what attributes the displayed person should have.
 * The relationship between attributes is "AND" while the relationship between keywords of the same attribute
 * is "OR".
 * For example, "findp n/ alice bob" will return alice and bob (any person whose name is "alice" or "bob").
 * In addition, "findp c/ shopsg t/ hr will return any person whose companyName is "shopsg" and tags contains "hr".
 */
public class FindPersonCommand extends Command {

    public static final String COMMAND_WORD = "findp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose name, companyName, and tags"
            + " contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_COMPANY + "COMPANY] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + " alice bob ";

    private final ArgumentMultimap argumentMultimap;

    /**
     * Constructs FindPersonCommand object
     * @param argumentMultimap A hashmap containing person prefixes and its value from user input
     */
    public FindPersonCommand(ArgumentMultimap argumentMultimap) {
        requireNonNull(argumentMultimap);
        this.argumentMultimap = argumentMultimap;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        String[] nameKeywords = argumentMultimap.getValue(PREFIX_NAME).orElse("").split("\\s+");
        String[] companyNameKeywords = argumentMultimap.getValue(PREFIX_COMPANY).orElse("").split("\\s+");
        String[] tagKeywords = argumentMultimap.getValue(PREFIX_TAG).orElse("").split("\\s+");

        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(Arrays.asList(nameKeywords),
                Arrays.asList(companyNameKeywords), Arrays.asList(tagKeywords));

        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()),
                false, false, true, false, false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindPersonCommand // instanceof handles nulls
                && argumentMultimap.equals(((FindPersonCommand) other).argumentMultimap)); // state check
    }
}
