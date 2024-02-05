package com.hoaxify.ws.user.validation;

import com.hoaxify.ws.file.FileService;
import com.hoaxify.ws.shared.Messages;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileTypeValidator implements ConstraintValidator<FileType, String> {
    private final FileService fileService;
    private String[] types;

    @Autowired
    public FileTypeValidator(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void initialize(FileType constraintAnnotation) {
        this.types = constraintAnnotation.types();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value) || value.isEmpty()) {
            return true;
        }

        String type = fileService.detectType(value);

        for (String validType : types) {
            if (type.contains(validType)) {
                return true;
            }
        }

        String validTypes = String.join(", ", types);
        String message = Messages.getValidationMessageForLocale("hoaxify.constraint.filetype", validTypes);
        context.disableDefaultConstraintViolation();;
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
                context.unwrap(HibernateConstraintValidatorContext.class);

        hibernateConstraintValidatorContext.addMessageParameter("message", message);
        hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addConstraintViolation();

        return false;
    }
}
