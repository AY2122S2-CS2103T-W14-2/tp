package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteTagCommand;

class DeleteTagCommandParserTest {

    private static final String TAG1 = "friends";
    private DeleteTagCommandParser parser = new DeleteTagCommandParser();


    @Test
    void parse_validArgs_returnsDeleteTagCommand() {
        DeleteTagCommand expectedDeleteTagCommand = new DeleteTagCommand(INDEX_FIRST_PERSON, TAG1);
        assertParseSuccess(parser, "1 t/friends", expectedDeleteTagCommand);
        // Messy user input with multiple whitespaces
        assertParseSuccess(parser, " 1 t/  friends", expectedDeleteTagCommand);
    }

    @Test
    void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser,
                "asdkfasdfl",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTagCommand.MESSAGE_USAGE));
    }
}
