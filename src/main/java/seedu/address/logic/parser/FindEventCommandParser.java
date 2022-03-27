package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import seedu.address.logic.commands.FindEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindEventPersonCommand object
 */
public class FindEventCommandParser implements Parser<FindEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindEventCommand
     * and returns a FindEventCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindEventCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_COMPANY,
                        PREFIX_DATE, PREFIX_TIME, PREFIX_LOCATION, PREFIX_TAG);

        if (!isValid(argMultimap)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindEventCommand.MESSAGE_USAGE));
        }

        return new FindEventCommand(argMultimap);
    }

    private boolean isValid(ArgumentMultimap argumentMultimap) throws ParseException {
        boolean namePresent = argumentMultimap.getValue(PREFIX_NAME).isPresent();
        boolean companyNamePresent = argumentMultimap.getValue(PREFIX_COMPANY).isPresent();
        boolean datePresent = argumentMultimap.getValue(PREFIX_DATE).isPresent();
        boolean timePresent = argumentMultimap.getValue(PREFIX_TIME).isPresent();
        boolean locationPresent = argumentMultimap.getValue(PREFIX_LOCATION).isPresent();
        boolean tagPresent = argumentMultimap.getValue(PREFIX_TAG).isPresent();

        if (namePresent) {
            ParserUtil.parseName(argumentMultimap.getValue(PREFIX_NAME).get());
        }
        if (companyNamePresent) {
            ParserUtil.parseCompanyName(argumentMultimap.getValue(PREFIX_COMPANY).get());
        }
        if (datePresent) {
            ParserUtil.parseDate(argumentMultimap.getValue(PREFIX_DATE).get());
        }
        if (timePresent) {
            ParserUtil.parseTime(argumentMultimap.getValue(PREFIX_TIME).get());
        }
        if (locationPresent) {
            ParserUtil.parseLocation(argumentMultimap.getValue(PREFIX_LOCATION).get());
        }
        if (tagPresent) {
            ParserUtil.parseTag(argumentMultimap.getValue(PREFIX_TAG).get());
        }

        return namePresent || companyNamePresent || datePresent || timePresent
                || locationPresent || tagPresent;
    }
}
