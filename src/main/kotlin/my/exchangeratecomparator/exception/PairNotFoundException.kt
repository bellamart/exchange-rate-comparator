package my.exchangeratecomparator.exception

/**
 * Exception thrown when an exchange pair is not found.
 */
class PairNotFoundException(message: String): RuntimeException(message)