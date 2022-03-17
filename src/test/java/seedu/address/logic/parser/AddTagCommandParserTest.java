package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddTagCommand;

class AddTagCommandParserTest {

    private static final String TAG1 = "TAG1";
    private AddTagCommandParser parser = new AddTagCommandParser();


    @Test
    void parse_validArgs_returnsAddTagCommand() {
        AddTagCommand expectedAddTagCommand = new AddTagCommand(INDEX_FIRST_PERSON, TAG1);
        assertParseSuccess(parser, "1 t/TAG1", expectedAddTagCommand);
        // Messy user input with multiple whitespaces
        assertParseSuccess(parser, " 1 t/  TAG1", expectedAddTagCommand);
    }

    @Test
    void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser,
                "asdkfasdfl",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTagCommand.MESSAGE_USAGE));
    }
}
