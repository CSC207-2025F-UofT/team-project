package use_case.communicate_request;

/**
 * Output boundary for UC1 - Build a Request.
 */
public interface CommunicateRequestOutputBoundary {

    void prepareSuccessView(CommunicateRequestOutputData outputData);

    void prepareFailView(String errorMessage);
}
