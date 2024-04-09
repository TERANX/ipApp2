//package com.example.springsecurity.validation;
//
//import com.google.common.base.Joiner;
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import org.passay.*;
//
//import java.util.Arrays;
//
//public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
//
//    @Override
//    public void initialize(final ValidPassword arg0) {
//
//    }
//
//    @Override
//    public boolean isValid(final String password, final ConstraintValidatorContext context) {
//        // @formatter:off
//        final PasswordValidator validator = new PasswordValidator(Arrays.asList(
//                new LengthRule(8, 30),
//                new AllowedCharacterRule(new char[] { 'a', 'b', 'c' }),
//                new CharacterRule(EnglishCharacterData.LowerCase, 5),
//                new LengthRule(8, 10),
//                new WhitespaceRule()
//        ));
//        final RuleResult result = validator.validate(new PasswordData(password));
//        if (result.isValid()) {
//            return true;
//        }
//        context.disableDefaultConstraintViolation();
//        context.buildConstraintViolationWithTemplate(Joiner.on(",").join(validator.getMessages(result))).addConstraintViolation();
//        return false;
//    }
//
//}
