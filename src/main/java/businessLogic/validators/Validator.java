package businessLogic.validators;

/**
 * Interface for validating business model objects.
 *
 * @param <T> the type of object to validate
 */
public interface Validator<T> {
    public void validate(T t);
}

