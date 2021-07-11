package ir.broker.maktab.broker.service.validator;

import ir.broker.maktab.broker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueNationalIdValidator implements ConstraintValidator<UniqueNationalId,String> {

    private UserRepository repository ;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(String nationalId, ConstraintValidatorContext constraintValidatorContext) {
        return !repository.existsByNationalId(nationalId);
    }
}
