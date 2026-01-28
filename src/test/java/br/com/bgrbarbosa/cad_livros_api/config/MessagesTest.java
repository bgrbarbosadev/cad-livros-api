package br.com.bgrbarbosa.cad_livros_api.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessagesTest {

    @Test
    void constants_shouldContainExpectedValues() {
        assertAll("Messages constants",
                () -> assertEquals("The field must be a valid email address", Messages.INVALID_EMAIL),
                () -> assertEquals("The field cannot be null, empty or blank", Messages.NOT_BLANK),
                () -> assertEquals("The field must not be null or empty.", Messages.NOT_EMPTY),
                () -> assertEquals("The field cannot be zero.", Messages.NOT_NULL),
                () -> assertEquals("Resource not found: ", Messages.RESOURCE_NOT_FOUND),
                () -> assertEquals("Existing Value", Messages.ILLEGAL_ARGUMENT_EXCEPTION),
                () -> assertEquals("Invalid CPF", Messages.INVALID_CPF),
                () -> assertEquals("Invalid CNPJ", Messages.INVALID_CNPJ),
                () -> assertEquals("Field must contain between {min} and {max} characters", Messages.FIELD_SIZE_MESSAGE),
                () -> assertEquals("Database Exception", Messages.DATABASE_EXCEPTION),
                () -> assertEquals("Error saving the record, check the values and try again", Messages.BAD_REQUEST),
                () -> assertEquals("Validation exception", Messages.VALIDATION_EXCEPTION),
                () -> assertEquals("Validation errors!! Check the errors found below", Messages.VALIDATION_MESSAGE),
                () -> assertEquals("Forbiden", Messages.FORBIDEN),
                () -> assertEquals("Access denied!", Messages.ACCESS_DENIED),
                () -> assertEquals("Enter a valid email address", Messages.EMAIL_VALID),
                () -> assertEquals("Error Gateway", Messages.ENTITY_EXCEPTION),
                () -> assertEquals("Error Inserting Record: ", Messages.ERROR_INSERTING_RECORD),
                () -> assertEquals("Error Inserting Record: ", Messages.ERROR_DELETION_RECORD),
                () -> assertEquals("Existing User in the Database", Messages.Existing_User),
                () -> assertEquals("User not exists", Messages.USER_NOT_EXISTS),
                () -> assertEquals("Existing role name", Messages.EXISTING_ROLE),
                () -> assertEquals("Error while authenticating", Messages.ERROR_WHILE_AUTHENTICATION)
        );
    }
}