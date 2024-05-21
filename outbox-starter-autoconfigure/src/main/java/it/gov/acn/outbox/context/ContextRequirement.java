package it.gov.acn.outbox.context;


public interface ContextRequirement {
    boolean isSatisfied();
    String getDescription(); // Describes why the requirement is not satisfied
    boolean isError(); // If true, the client code should log the description and block the starter from starting
}